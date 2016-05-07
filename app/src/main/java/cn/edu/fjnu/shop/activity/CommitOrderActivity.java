
package cn.edu.fjnu.shop.activity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.dialog.LoadingDialog;
import cn.edu.fjnu.shop.system.UserInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author GaoFei
 * 确认订单页面
 */
public class CommitOrderActivity extends Activity {

	private AQuery aQuery;
	private String userID;
	private String command="1";
	private ImageView backImageView;
	private  Map<String, String> params=new HashMap<String, String>();
	private double account;
	private double all;
	private LoadingDialog loadingDialog;
	private TextView cityTextView;
	private static final String[] citys = {"福州","厦门","漳州","泉州"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit_order);
		initView();
		initData();
		initEvent();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			CommitOrderActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return false;
	}
	
	public void initView(){
		
		cityTextView = (TextView)findViewById(R.id.text_city);
		//默认配送城市是福州
		cityTextView.setText("福州");
	}
	
	
	public void initData(){
		
		Bundle bundle=getIntent().getExtras();
		String productID= bundle.getString("productIDs");
		String productNum=bundle.getString("productNums");
		float orderMoney= bundle.getFloat("productMoney");
		String city = bundle.getString("city");
		String unit = bundle.getString("unit");
		String fromType = bundle.getString("fromType");
		if(!TextUtils.isEmpty(city))
			cityTextView.setText(city);
		
		aQuery=new AQuery(this);
		DecimalFormat decimalFormat=new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(2);
		all=getIntent().getExtras().getFloat("productMoney");
		aQuery.id(R.id.text_product_money_red).text(""+decimalFormat.format(getIntent().getExtras().getFloat("productMoney")));
		
		userID=String.valueOf(UserInfo.userID);
		command="1";
		params.put("command", command);
		params.put("ID", userID);
		params.put("productID", productID);
		params.put("productNumber", productNum);
		params.put("orderMoney", ""+orderMoney);
		params.put("city", city);
		params.put("unit", unit);
		//来自类型
		params.put("fromType", fromType);
		params.put("expireTime",aQuery.id(R.id.text_expire_date).getText().toString());
		aQuery.ajax(URLForService.MYSERVICE, params, JSONObject.class, this, "responsePersonalInfo");
		
		aQuery.id(R.id.img_commit_order).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(account<all){
					Toast.makeText(getApplicationContext(), "资金不足，请充值", Toast.LENGTH_SHORT).show();
					return;
				}else{
					
					
					UserInfo.isEmptyShopCar=true;
					params.put("expireTime",aQuery.id(R.id.text_expire_date).getText().toString());
					params.put("city", cityTextView.getText().toString());
					aQuery.ajax(URLForService.ORDERSERVICE, params, JSONObject.class, CommitOrderActivity.this, "responseOrderlInfo");
					showLoadingDialog();
				}
			
			}
		});
		
		
	}
	
	public void initEvent(){
		aQuery.id(R.id.img_commit_back).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				CommitOrderActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			}
		});
		
		
		aQuery.id(R.id.layout_date).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Calendar c = Calendar.getInstance();
				int startYear = c.get(Calendar.YEAR);
				int startMonth = c.get(Calendar.MONTH);
				int startDay = c.get(Calendar.DAY_OF_MONTH);
				
				DatePickerDialog datePickerDialog= new DatePickerDialog(CommitOrderActivity.this, new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						// TODO Auto-generated method stub
						//text_expire_date
						//System.out.println(year);
						//System.out.println(monthOfYear+1);
						//System.out.println(dayOfMonth);
						aQuery.id(R.id.text_expire_date).text(""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
					}
				}, startYear, startMonth, startDay);
				
				datePickerDialog.setTitle("日期选择");
				//datePickerDialog.setButton(, text, listener)
				datePickerDialog.show();
			}
		});
		
		
		aQuery.id(R.id.layout_city).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new AlertDialog.Builder(CommitOrderActivity.this)
				.setTitle("选择配送城市").setItems(citys, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						cityTextView.setText(citys[which]);
						
					}
				}).show();
				
			}
		});
		
	}
	
	public void responsePersonalInfo(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			Toast.makeText(getApplicationContext(), "位置错误", Toast.LENGTH_SHORT).show();			
		}
		if(data!=null){
			try {
				aQuery.id(R.id.text_name).text(data.getString("name"));
				aQuery.id(R.id.text_phone).text(data.getString("phoneNumber"));
				aQuery.id(R.id.text_address).text(data.getString("address"));
				account=data.getDouble("account");
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), "个人信息获取失败", Toast.LENGTH_SHORT).show();				
			}
		}
	}
	
	public void responseOrderlInfo(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
			
		}
		if(data!=null){
			try {
				String orderState=data.getString("orderState");
				if("success".equals(orderState)){
					closeLoadingDialog();
					Toast.makeText(getApplicationContext(), "订单提交成功", Toast.LENGTH_SHORT).show();
				//	closeLoadingDialog();
					setResult(RESULT_OK);
					CommitOrderActivity.this.finish();
					overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
				}else{
					closeLoadingDialog();
					Toast.makeText(getApplicationContext(), "订单提交失败", Toast.LENGTH_SHORT).show();
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				closeLoadingDialog();
				Toast.makeText(getApplicationContext(), "订单提交失败", Toast.LENGTH_SHORT).show();				
			}
		}
	}
	
	public void showLoadingDialog(){
		
		loadingDialog=new LoadingDialog(this);
		loadingDialog.show();
	}
	
	
	public void closeLoadingDialog(){
		
		if(loadingDialog!=null&&loadingDialog.isShowing())
			loadingDialog.dismiss();
	}
	
}
