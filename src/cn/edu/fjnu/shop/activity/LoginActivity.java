/**
 * 
 */
package cn.edu.fjnu.shop.activity;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.dialog.LoadingDialog;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.OPUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author GaoFei
 *
 */
public class LoginActivity extends Activity  implements View.OnClickListener{

	private ImageView backImageView;
	private Button loginButton;
	private EditText userNameEditText;
	private EditText passwordEditText;
	//private ProgressDialog mProgressDialog;
	private LoadingDialog loadingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findView();
		initEvent();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			LoginActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1&&resultCode==RESULT_OK){
			String userID= data.getStringExtra("userID");
			new AlertDialog.Builder(this).setTitle("温馨提示")
			.setMessage("请牢记您的帐号为:"+userID).setPositiveButton("确定", null)
			.show();
		}
	}
	
	public void findView(){
		backImageView=(ImageView)findViewById(R.id.img_login_back);
		loginButton=(Button)findViewById(R.id.btn_login);
		userNameEditText=(EditText)findViewById(R.id.edit_userName);
		passwordEditText=(EditText)findViewById(R.id.edit_password);
		
		backImageView.setOnClickListener(this);
		loginButton.setOnClickListener(this);
	}
	
	public boolean checkLogin(){
		if(OPUtils.isEmpty(userNameEditText.getText().toString())){
			OPUtils.showToast("帐号不能为空", Toast.LENGTH_SHORT);
			return false;
		}
			
		if(OPUtils.isEmpty(passwordEditText.getText().toString())){
			OPUtils.showToast("密码不能为空", Toast.LENGTH_SHORT);
			return false;
		}
			
		
		return true;
	}
	
	public void login(){
		AQuery aQuery=new AQuery(this);
		Map<String, String> params=new HashMap<String, String>();
		params.put("userName", userNameEditText.getText().toString());
		params.put("password", passwordEditText.getText().toString());
		aQuery.ajax(URLForService.LOGINSERVICE, params, JSONObject.class,this,"responseLogin");
		//OPUtils.showProgressDialog(LoginActivity.this);
		showLoadingDialog();
		//showProgressDialog();
		//aQuery.ajax(URLForService.LOGINSERVICE,JSONObject.class, this, "responseLogin");
	}
	
	public void responseLogin(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			closeLoadingDialog();
			OPUtils.showToast("未知错误("+status.getCode()+")", Toast.LENGTH_SHORT);
			
			
		}
		
		if(data!=null){
			
			try {
				String state= data.getString("loginState");
				if("success".equals(state)){
					
					closeLoadingDialog();
					/**登录成功*/
					OPUtils.showToast("用户登录成功", Toast.LENGTH_SHORT);
					saveToXML();
					setResult(RESULT_OK);
					LoginActivity.this.finish();
					overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
				}else{
					closeLoadingDialog();
					OPUtils.showToast("用户名或密码错误", Toast.LENGTH_SHORT);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				closeLoadingDialog();
				OPUtils.showToast("未知错误", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_login_back:
			setResult(RESULT_CANCELED);
			LoginActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			break;
		case R.id.btn_login:
			if(checkLogin()){
				login();
			}
			break;
		default:
			break;
		}
	}
	
	
	public void initEvent(){
		
		AQuery registerAQuery=new AQuery(this);
		registerAQuery.id(R.id.text_register).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivityForResult(intent, 1);
			//	startActivity(intent);
				overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
			}
		});
	}
	
	public void saveToXML(){
		
		SharedPreferences sharedPreferences=getSharedPreferences("LoginState",Context.MODE_PRIVATE);
		Editor editor=sharedPreferences.edit();
		editor.putString("login", "true");
		editor.putInt("ID", Integer.parseInt( userNameEditText.getText().toString()));
		editor.putString("password", passwordEditText.getText().toString());
		editor.commit();
		
		UserInfo.userID=Integer.parseInt( userNameEditText.getText().toString());
		UserInfo.isLogin=true;
		UserInfo.isEmptyShopCar=true;
		//usern
	}
	
	
	public void showLoadingDialog() {

		loadingDialog = new LoadingDialog(this);
		loadingDialog.show();
	}

	public void closeLoadingDialog() {

		if (loadingDialog != null && loadingDialog.isShowing())
			loadingDialog.dismiss();
	}
}
