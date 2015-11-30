package cn.edu.fjnu.shop.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.NetWorkUtils;
import cn.edu.fjnu.utils.OPUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author GaoFei
 * 
 */
public class FeedBackActivity extends Activity{

	private ImageView backImageView;
	private TextView commiTextView;
	private EditText conetentEditText;
	private EditText wayEditText;
	private AQuery aQuery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		aQuery=new AQuery(this);
		
		initView();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			FeedBackActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return false;
	}
	public void initView(){
		backImageView=(ImageView)findViewById(R.id.reply_back);
		commiTextView=(TextView)findViewById(R.id.text_commit);
		conetentEditText=(EditText)findViewById(R.id.edit_content_feedback);
		wayEditText=(EditText)findViewById(R.id.edit_way_feedback);
		backImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FeedBackActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			}
		});
		commiTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(OPUtils.isEmpty(conetentEditText.getText().toString())){
					OPUtils.showToast("请输入反馈内容", Toast.LENGTH_SHORT);
					return;
				}
			
				Map<String, String> params=new HashMap<String, String>();
				if(UserInfo.isLogin){
					params.put("userID", String.valueOf(UserInfo.userID));
				}else {
					params.put("userID", "-1");
				}
				params.put("content",conetentEditText.getText().toString());
				params.put("way", wayEditText.getText().toString());
				params.put("ip", NetWorkUtils.getLocalIpAddress());
				//发送ajax请求给服务器
				aQuery.ajax(URLForService.FEEDBACKSERVICE, params, JSONObject.class, FeedBackActivity.this,"responseFeedBack");
				
				
			}
		});
	}
	
	/**
	 * 反馈请求响应函数
	 */
	public void responseFeedBack(String url,JSONObject data,AjaxStatus status){
		
		if(status.getCode()!=200){
			OPUtils.showToast("未知错误("+status.getCode()+")", Toast.LENGTH_SHORT);
			
		}
		if(data!=null){
			try {
				String state= data.getString("feedback");
				if("success".equals(state)){
					OPUtils.showToast("意见反馈成功", Toast.LENGTH_SHORT);
					FeedBackActivity.this.finish();
					overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
				}else{
					OPUtils.showToast("意见反馈失败", Toast.LENGTH_SHORT);
				}
			} catch (Exception e) {
				// TODO: handle eeption
				OPUtils.showToast("意见反馈成功", Toast.LENGTH_SHORT);
			}
		}
	}
}
