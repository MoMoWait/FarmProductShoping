package cn.edu.fjnu.shop.activity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.adapter.OrderAdapter;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.domain.OrderItemInfo;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.OPUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MyOrderActivity extends Activity {

	private AQuery aQuery;
	private ListView orderListView;
	private List<OrderItemInfo> infos=new ArrayList<OrderItemInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder);
		initView();
		initEvent();
		initData();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			MyOrderActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return false;
	}
	public void initView(){
		
		aQuery=new AQuery(this);
		
		orderListView=(ListView)findViewById(R.id.list_order);
		
	}
	
	public void initData(){
		
		
		/**请求订单数据*/
		Map<String, String> params=new HashMap<String, String>();
		params.put("command", "2");
		params.put("userID",String.valueOf(UserInfo.userID));
		aQuery.ajax(URLForService.ORDERSERVICE, params, JSONObject.class, this, "responseOrderData");
		
	
	}
	
	public void initEvent(){
		aQuery.id(R.id.img_myorder_back).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyOrderActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			}
		});
	}
	
	
	public void responseOrderData(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			OPUtils.showToast("未知错误("+status.getCode()+")", Toast.LENGTH_SHORT);
			
		}
		if(data!=null){
			try {
				
				JSONArray infoData= data.getJSONArray("orderInfos");
				//System.out.println(infoData.length());
				for(int i=0;i<infoData.length();i++){
					JSONObject orderItemInfo=infoData.getJSONObject(i);
					OrderItemInfo orderInfo=new OrderItemInfo();
					orderInfo.setOrderID(orderItemInfo.getString("orderID"));
					orderInfo.setOrderDate(orderItemInfo.getString("orderDate"));
					orderInfo.setExpireDate(orderItemInfo.getString("expireDate"));
					//System.out.println(orderItemInfo.getString("expireDate"));
					orderInfo.setOrderState(orderItemInfo.getString("orderState"));
					orderInfo.setOrderProduct(orderItemInfo.getString("orderProduct"));
					orderInfo.setOrderMoney(orderItemInfo.getString("orderMoney"));
					infos.add(orderInfo);
				}
				OrderAdapter orderAdapter=new OrderAdapter(this, infos);
				orderListView.setAdapter(orderAdapter);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}else{
			
		}
	}
}
