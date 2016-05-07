package cn.edu.fjnu.shop.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.utils.CommonUtils;
import cn.edu.fjnu.shop.utils.DialogUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author GaoFei
 * 注册页面
 */
public class RegisterActivity extends Activity {

	private AQuery aQuery;
	private String userName,sex,address,phoneNumber,password,commitPassword;
	private EditText userNameEditText,sexEditText,addressEditText,phoneNumberEditText,
	passwordEditText,commitPasswordEditText;
	private Button registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		initEvent();
	}
	
	public void initView(){
		aQuery=new AQuery(this);
		
		userNameEditText=(EditText)findViewById(R.id.edit_register_name);
		sexEditText=(EditText)findViewById(R.id.edit_register_sex);
		addressEditText=(EditText)findViewById(R.id.edit_register_address);
		phoneNumberEditText=(EditText)findViewById(R.id.edit_register_phonenumber);
		passwordEditText=(EditText)findViewById(R.id.edit_register_password);
		commitPasswordEditText=(EditText)findViewById(R.id.edit_register_commitpassword);
		registerButton=(Button)findViewById(R.id.btn_register);
	}
	
	
	public void initEvent(){
		aQuery.id(R.id.img_register_back).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegisterActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
				
			}
		});
		
		sexEditText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//typeDialog.setcon
				/**调整页面布局*/
				final Dialog sexDialog=new Dialog(RegisterActivity.this);
				sexDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				sexDialog.setCancelable(true);
				sexDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_backgoroud_exit);
				sexDialog.setContentView(R.layout.dialog_sex);
				sexDialog.getWindow().setLayout((int)(CommonUtils.getScreenPixWidth(RegisterActivity.this)*0.8f), -2);
				ListView sexListView=(ListView)sexDialog.getWindow().findViewById(R.id.list_sex);
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(sexDialog.getContext(), R.layout.list_item_sex,R.id.text_sex_item,
						new String[]{"男","女"});
				
				sexListView.setAdapter(adapter);
				sexDialog.show();
				sexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						sex=(String)parent.getAdapter().getItem(position).toString();
						sexEditText.setText(sex);
						sexDialog.dismiss();
					}
				});
			
			}
		});
		
		registerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//获取各个EditText的值
				userName=userNameEditText.getText().toString().trim();
				sex=sexEditText.getText().toString();
				address=addressEditText.getText().toString().trim();
				phoneNumber=phoneNumberEditText.getText().toString().trim();
				password=passwordEditText.getText().toString();
				commitPassword=commitPasswordEditText.getText().toString();
				//sex=sexEditText.getText().toString();
				if(checkRegister()){
					//准备参数
					Map<String, String> params=new HashMap<String, String>();
					params.put("command","1");
					params.put("userName",userName);
					params.put("sex",sex);
					params.put("address",address);
					params.put("phoneNumber",phoneNumber);
					params.put("password",password);
					aQuery.ajax(URLForService.REGISTERSERVICE, params, JSONObject.class,RegisterActivity.this,"responseRegister");
					DialogUtils.showLoadingDialog(RegisterActivity.this);
				}
			}
		});
		
	}
	
	
	public boolean checkRegister(){
		
		//address,phoneNumber,password,commitPassword;
		//非空检测
		if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(address)||TextUtils.isEmpty(phoneNumber)
				||TextUtils.isEmpty(password)||TextUtils.isEmpty(commitPassword)){
			Toast.makeText(getApplicationContext(), "输入内容为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		//密码是否一致检测
		if(!password.equals(commitPassword)){
			Toast.makeText(getApplicationContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		//手机号检测
		if(!isPhoneNumber(phoneNumber)){
			Toast.makeText(getApplicationContext(), "手机号码不正确", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		//	return false;
		return true;
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			RegisterActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void responseRegister(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			DialogUtils.closeLoadingDialog();
			Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();			
			
		}
		
		if(data!=null){
			
			try {
				String state= data.getString("registerState");
				String userID=data.getString("userID");
				if("success".equals(state)){
					
					DialogUtils.closeLoadingDialog();
					/**注册成功*/
					Toast.makeText(getApplicationContext(), "用户注册成功", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent();
					intent.putExtra("userID", userID);
					setResult(RESULT_OK, intent);
					RegisterActivity.this.finish();
					overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
				}else{
					DialogUtils.closeLoadingDialog();
					Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				DialogUtils.closeLoadingDialog();
				Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	public boolean isPhoneNumber(String phoneNumber){
		Pattern p = Pattern.compile("1\\d{10}$");  
		  
		Matcher m = p.matcher(phoneNumber);  
		  
		  
		return m.matches();  
	}
	
}
