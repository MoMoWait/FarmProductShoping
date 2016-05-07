/**
 * 
 */
package cn.edu.fjnu.shop.activity;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.system.UserInfo;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author GaoFei
 * 密码修改页面
 */
public class PasswordChangeActivity extends Activity {

	private AQuery aQuery;
	private EditText origenPasswordEditText;
	private EditText newPasswordEditText;
	private EditText commitPasswordEditText;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_change);
		initView();
		initData();
		initEvent();
		
	}
	
	public void initView(){
		
		aQuery=new AQuery(this);
	}
	
	public void initData(){
		
	}
	
	public void initEvent(){
		
		aQuery.id(R.id.img_passwordchange_back).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PasswordChangeActivity.this.finish();
			}
		});
		
		
		aQuery.id(R.id.btn_commit_change).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//set
				
				/**检测输入内容是否为空*/
				origenPasswordEditText=(EditText)findViewById(R.id.edit_orign_password);
				newPasswordEditText=(EditText)findViewById(R.id.edit_new_password);
				commitPasswordEditText=(EditText)findViewById(R.id.edit_commit_new_password);
				if(TextUtils.isEmpty(origenPasswordEditText.getText().toString())
						||TextUtils.isEmpty(newPasswordEditText.getText().toString())
						||TextUtils.isEmpty(commitPasswordEditText.getText().toString())){
					Toast.makeText(getApplicationContext(), "输入内容为空", Toast.LENGTH_SHORT).show();			
					return;
				}
				
				if(!newPasswordEditText.getText().toString().equals(commitPasswordEditText.getText().toString())){
					Toast.makeText(getApplicationContext(), "新密码与确认密码不一致", Toast.LENGTH_SHORT).show();
				}
				
				/**发送ajax请求给服务器*/
				//aQuery.
				Map<String, String> params=new HashMap<String, String>();
				params.put("command", "3");
				params.put("ID", String.valueOf(UserInfo.userID));
				params.put("userID",String.valueOf(UserInfo.userID));
				params.put("orignPassword",origenPasswordEditText.getText().toString());
				params.put("newPassword", newPasswordEditText.getText().toString());
				aQuery.ajax(URLForService.MYSERVICE, params, JSONObject.class, PasswordChangeActivity.this, "responsePasswordChange");
			}
		});
		
		
		
		
	}
	
	
	public void responsePasswordChange(String url,JSONObject data,AjaxStatus status){
		
		if(status.getCode()!=200){
			//closeProgressDialog();
			Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();						
			
		}
		
		if(data!=null){
			
			try {
				String changeState=data.getString("changeState");
				if("orignPasswordError".equals(changeState)){
					Toast.makeText(getApplicationContext(), "原始密码错误", Toast.LENGTH_SHORT).show();			
					
				}else if("changeSuccess".equals(changeState)){
					Toast.makeText(getApplicationContext(), "密码修改成功", Toast.LENGTH_SHORT).show();			
					setResult(RESULT_OK);
					PasswordChangeActivity.this.finish();
				}else if("unknowError".equals(changeState)){
					Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();			
				}else{
					Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();			

				}
				//if(data.getString("changeState"))
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();			

			}
			
			
		}
	}
}
