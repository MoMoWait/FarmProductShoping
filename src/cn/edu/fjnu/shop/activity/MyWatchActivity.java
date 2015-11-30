/**
 * 
 */
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
import cn.edu.fjnu.shop.adapter.WatchAdapter;
import cn.edu.fjnu.shop.data.FragmentIndex;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.domain.ProductInfo;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.OPUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author GaoFei
 * 我的关注页面
 */
public class MyWatchActivity extends Activity {

	private ListView myWatchListView;
	private AQuery aQuery;
	private List<ProductInfo> productInfos=new ArrayList<ProductInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_watch);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			MyWatchActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==2){
			if(resultCode==3){
				setResult(RESULT_OK);
				MyWatchActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
				//MainActivity.mainActivity.setFragmentIndex(FragmentIndex.CART_FRAGMENT);
				//this.setFragmentIndex(FragmentIndex.CART_FRAGMENT);
			}
			
		}
	}
	
	public void initView(){
		
		
		aQuery=new AQuery(this);
		
		myWatchListView=(ListView)findViewById(R.id.list_mywatch);
	}
	
	public void initData(){
		
		Map<String, String> params=new HashMap<String, String>();
		params.put("command","1");
		params.put("userID",String.valueOf(UserInfo.userID));
		aQuery.ajax(URLForService.WATCHSERVICE, params, JSONObject.class, this, "responseWatchProduct");
	}
	
	public void initEvent(){
		myWatchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyWatchActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("productInfo", (ProductInfo) parent
						.getAdapter().getItem(position));
				startActivityForResult(intent, 2);
				overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
				//OPUtils.startActivity(MyWatchActivity.this, ProductDetailActivity.class);
			}
		});
		
		aQuery.id(R.id.img_mywatch_back).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyWatchActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			}
		});
	}
	
	public void responseWatchProduct(String url,JSONObject data,AjaxStatus status){
		if (status.getCode() != 200) {
			OPUtils.showToast("未知错误(" + status.getCode() + ")",
					Toast.LENGTH_SHORT);

		}
		if(data!=null){
			
			try {
				JSONArray products =data.getJSONArray("products");
				for(int i=0;i<products.length();i++){
					JSONObject product=products.getJSONObject(i);
					ProductInfo productInfo=new ProductInfo();
					productInfo.setId(product.getInt("productID"));
					productInfo.setName(product.getString("productName"));
					productInfo.setType(product.getString("productType"));
					productInfo.setDes(product.getString("productDes"));
					productInfo.setPrice(Float.parseFloat(product.getString("productPrice")));
					productInfo.setPhotoUrl(product.getString("productPhoto"));
					productInfo.setProductBagPrice(Float.parseFloat(product.getString("productBagPrice")));
					productInfos.add(productInfo);
					
				}
				WatchAdapter watchAdapter=new WatchAdapter(this, productInfos);
				myWatchListView.setAdapter(watchAdapter);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
