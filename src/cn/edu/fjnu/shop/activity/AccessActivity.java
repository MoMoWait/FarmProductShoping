package cn.edu.fjnu.shop.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.adapter.AccessAdapter;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.domain.OrderItemInfo;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.OPUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author GaoFei 
 * 订单评价页面
 */
public class AccessActivity extends Activity {

	private AQuery aQuery;
	private ListView accessListView;
	private ImageView backImageView;
	private List<OrderItemInfo> infos = new ArrayList<OrderItemInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_access);
		initView();
		initData();
		initEvent();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			AccessActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void initView() {

		aQuery = new AQuery(this);

		accessListView = (ListView) findViewById(R.id.list_access);

		backImageView=(ImageView)findViewById(R.id.img_access_back);
	}

	public void initData() {

		/** 请求订单数据 */
		Map<String, String> params = new HashMap<String, String>();
		params.put("command", "3");
		params.put("userID", String.valueOf(UserInfo.userID));
		aQuery.ajax(URLForService.ORDERSERVICE, params, JSONObject.class, this,
				"responseOrderData");

	}

	public void initEvent() {
		backImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AccessActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);

			}
		});
	}

	public void responseOrderData(String url, JSONObject data, AjaxStatus status) {
		if (status.getCode() != 200) {
			OPUtils.showToast("未知错误(" + status.getCode() + ")",
					Toast.LENGTH_SHORT);

		}
		if (data != null) {
			try {

				JSONArray infoData = data.getJSONArray("orderInfos");
				System.out.println(infoData.length());
				for (int i = 0; i < infoData.length(); i++) {
					JSONObject orderItemInfo = infoData.getJSONObject(i);
					OrderItemInfo orderInfo = new OrderItemInfo();
					orderInfo.setOrderID(orderItemInfo.getString("orderID"));
					orderInfo
							.setOrderDate(orderItemInfo.getString("orderDate"));
					orderInfo.setExpireDate(orderItemInfo
							.getString("expireDate"));
					orderInfo.setOrderState(orderItemInfo
							.getString("orderState"));
					orderInfo.setOrderProduct(orderItemInfo
							.getString("orderProduct"));
					orderInfo.setOrderMoney(orderItemInfo
							.getString("orderMoney"));
					infos.add(orderInfo);
				}
				AccessAdapter accessAdapter = new AccessAdapter(this, infos);
				accessListView.setAdapter(accessAdapter);
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {

		}
	}
}
