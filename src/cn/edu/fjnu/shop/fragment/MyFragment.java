package cn.edu.fjnu.shop.fragment;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.activity.AccessActivity;
import cn.edu.fjnu.shop.activity.FeedBackActivity;
import cn.edu.fjnu.shop.activity.LoginActivity;
import cn.edu.fjnu.shop.activity.MainActivity;
import cn.edu.fjnu.shop.activity.MyInfoActivity;
import cn.edu.fjnu.shop.activity.MyOrderActivity;
import cn.edu.fjnu.shop.activity.MyWatchActivity;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.OPUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author GaoFei
 * 
 */
public class MyFragment extends Fragment implements View.OnClickListener{

	private MainActivity mainActivity;
	private ImageView loginImageView;
	private TextView nameTextView;
	private String isLogin="false";
	private int userID;
	private AQuery aQuery;
	private static final String TAG = "MyFragment";
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(UserInfo.isLogin){
			return inflater.inflate(R.layout.fragment_my_login, null,false);
		}else{
			return inflater.inflate(R.layout.fragment_my_no_login, null,false);
		}
		
	}
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mainActivity=(MainActivity)getActivity();
		aQuery=new AQuery(mainActivity);
		findVeiw();
		initData();
		initEvent();
	}
	
	public void initEvent(){
		//layout_feedback
		
		
		aQuery.id(R.id.layout_feedback).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserInfo.isLogin){
					OPUtils.startActivityForResult(mainActivity, LoginActivity.class,new Bundle(), 1);
					mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
					//return ;
				}else{
					OPUtils.startActivity(mainActivity, FeedBackActivity.class);
					mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
				}
			}
		});
		
		aQuery.id(R.id.layout_my_order).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserInfo.isLogin){
					OPUtils.startActivityForResult(mainActivity, LoginActivity.class,new Bundle(), 1);
					mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
					return ;
				}
				
				Intent intent=new Intent(mainActivity,MyOrderActivity.class);
				mainActivity.startActivityForResult(intent, 4);
				mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
				//OPUtils.startActivity(mainActivity, MyOrderActivity.class);
			}
		});
		
		
		aQuery.id(R.id.text_my_watchlist).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserInfo.isLogin){
					OPUtils.startActivityForResult(mainActivity, LoginActivity.class,new Bundle(), 1);
					mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
					return ;
				}
				Intent intent=new Intent(mainActivity,MyWatchActivity.class);
				mainActivity.startActivityForResult(intent, 4);
				mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
				//OPUtils.startActivity(mainActivity,MyWatchActivity.class);
			}
		});
		
		aQuery.id(R.id.img_personal_comment).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!UserInfo.isLogin){
					OPUtils.startActivityForResult(mainActivity, LoginActivity.class,new Bundle(), 1);
					mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
					return ;
				}
				OPUtils.startActivity(mainActivity, AccessActivity.class);
				mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
			}
		});
		
		//注销
		aQuery.id(R.id.layout_log_off).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				mainActivity.reloadMyFragment();
			}
		});
		
		
		//退出
		aQuery.id(R.id.layout_quite).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mainActivity.finish();
				mainActivity.overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
				
			}
		});
	}
	
	public void findVeiw(){
		loginImageView=(ImageView)mainActivity.findViewById(R.id.img_login_head_photo);
		loginImageView.setOnClickListener(this);
		
		if(UserInfo.isLogin){
			//img_login_head_photo
			aQuery.id(R.id.img_login_head_photo).clicked(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent intent=new Intent(mainActivity,MyInfoActivity.class);
					mainActivity.startActivityForResult(intent,6);
					mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
					//OPUtils.startActivity(mainActivity,MyInfoActivity.class);
					//mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
				}
			});
			nameTextView=(TextView)mainActivity.findViewById(R.id.text_user_name);
		}
		
	}
	
	public void initData(){
		if(UserInfo.isLogin){
			Map<String,String> params=new HashMap<String, String>();
			params.put("command","1");
			params.put("ID",String.valueOf(UserInfo.userID));
			aQuery.ajax(URLForService.MYSERVICE, params, JSONObject.class, this,"responseUserInfo");
		}
		
	}
	

	
	public void responseUserInfo(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			OPUtils.showToast("未知错误("+status.getCode()+")", Toast.LENGTH_SHORT);
			
		}
		if(data!=null){
			
			
			try {
				
				//OPUtils.showToast(data.getString("name"));
				aQuery.id(R.id.text_user_name).text(data.getString("name"));		
				DecimalFormat decimalFormat=new DecimalFormat();
				decimalFormat.setMaximumFractionDigits(2);
				aQuery.id(R.id.text_my_pocket).text(decimalFormat.format(Float.parseFloat(data.getString("account"))));
				aQuery.id(R.id.img_login_head_photo).image(data.getString("headPhoto"),false,true);
				
			} catch (Exception e) {
				
				//发生异常,则重新登录
				mainActivity.reloadMyFragment();
				
				//OPUtils.showToast(e.getMessage());
				
				//Log.i(TAG,""+e);
				
			}
			
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_login_head_photo:
			if(!UserInfo.isLogin){
				OPUtils.startActivityForResult(mainActivity, LoginActivity.class,new Bundle(), 1);
				mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
			}
			break;

		default:
			break;
		}
	}
	
}
