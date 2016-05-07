package cn.edu.fjnu.shop.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.activity.LoginActivity;
import cn.edu.fjnu.shop.activity.MainActivity;
import cn.edu.fjnu.shop.activity.MyWatchActivity;
import cn.edu.fjnu.shop.activity.ProductDetailActivity;
import cn.edu.fjnu.shop.activity.SearchActivity;
import cn.edu.fjnu.shop.data.FragmentIndex;
import cn.edu.fjnu.shop.data.IDForHoteProductDes;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.domain.ProductInfo;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.shop.utils.CommonUtils;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * @author GaoFei
 * 
 */

public class HomeFragment extends Fragment {

	private static final String TAG = "HomeFragment";
	private ScrollView contentScrollView;
	private EditText homeSearchEditText;
	private MainActivity mainActivity;
	private AQuery aQuery;
	private List<ProductInfo> hoteProductInfos = new ArrayList<ProductInfo>();
	private boolean isTest = true;
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		
		Log.i(TAG,"oncreateview");
		return inflater.inflate(R.layout.fragment_head, null, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		initEvent();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onStart");
		super.onStart();
		
	}
	
	public void initView() {
		contentScrollView = (ScrollView) getActivity().findViewById(
				R.id.scroll_content);
		LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				CommonUtils.getScreenPixHeight(getActivity()) - CommonUtils.dp2px(getActivity(), 120));
		contentScrollView.setLayoutParams(contentLayoutParams);

		homeSearchEditText = (EditText) getActivity().findViewById(
				R.id.edit_home_search);
		

	}

	public void initData() {
		
		mainActivity = (MainActivity) getActivity();
		aQuery = new AQuery(mainActivity);
		Log.i(TAG,"调用HomeService方法");
	/*	Map<String,String> reqParams=new HashMap<String, String>();
		reqParams.put("command","1");
		aQuery.ajax(URLForService.HOMESERVICE, reqParams, JSONObject.class, this, "requestCallback");*/

		new GetHoteProductInfoTask().execute();
		if(UserInfo.isLogin){
			Map<String,String> params=new HashMap<String, String>();
			params.put("command","1");
			params.put("ID",String.valueOf(UserInfo.userID));
			aQuery.ajax(URLForService.MYSERVICE, params, JSONObject.class, this,"responseUserInfo");
		}
		// aQuery.ajax
	}

	public void initEvent() {
		aQuery.id(R.id.img_more).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainActivity.getCategoryFragment().setCurrentType(1);
				mainActivity.setFragmentIndex(FragmentIndex.CATEGORY_FRAGMENT);

			}
		});
		aQuery.id(R.id.text_more).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainActivity.getCategoryFragment().setCurrentType(1);
				mainActivity.setFragmentIndex(FragmentIndex.CATEGORY_FRAGMENT);
			}
		});

		aQuery.id(R.id.img_vegetables_more).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainActivity.getCategoryFragment().setCurrentType(2);
				mainActivity.setFragmentIndex(FragmentIndex.CATEGORY_FRAGMENT);
			}
		});

		aQuery.id(R.id.text_vegatable_more).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainActivity.getCategoryFragment().setCurrentType(2);
				mainActivity.setFragmentIndex(FragmentIndex.CATEGORY_FRAGMENT);
			}
		});

		aQuery.id(R.id.img_other_more).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainActivity.getCategoryFragment().setCurrentType(4);
				mainActivity.setFragmentIndex(FragmentIndex.CATEGORY_FRAGMENT);
			}
		});

		aQuery.id(R.id.text_other_more).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainActivity.getCategoryFragment().setCurrentType(4);
				mainActivity.setFragmentIndex(FragmentIndex.CATEGORY_FRAGMENT);
			}
		});

		aQuery.id(R.id.img_my_watch).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (UserInfo.userID == -1) {
					Intent intent = new Intent(mainActivity,
							LoginActivity.class);
					mainActivity.startActivity(intent);
					mainActivity.overridePendingTransition(
							R.anim.activity_enter_right,
							R.anim.activity_enter_left);
					return;
				}

				Intent intent = new Intent(mainActivity, MyWatchActivity.class);
				mainActivity.startActivityForResult(intent, 4);
				mainActivity
						.overridePendingTransition(R.anim.activity_enter_right,
								R.anim.activity_enter_left);
				// OPUtils.startActivity(mainActivity, MyWatchActivity.class);
			}
		});

		aQuery.id(R.id.img_shop_search).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(homeSearchEditText.getText().toString())) {
					Toast.makeText(getActivity(), "请输入搜索内容", Toast.LENGTH_SHORT).show();

					return;
				}

				Intent intent = new Intent(mainActivity, SearchActivity.class);
				intent.putExtra("searchName", homeSearchEditText.getText()
						.toString());
				mainActivity.startActivityForResult(intent, 4);
				mainActivity
						.overridePendingTransition(R.anim.activity_enter_right,
								R.anim.activity_enter_left);
			}
		});
	}

	public void requestCallback(String url, JSONObject data, AjaxStatus status) {

		// AQuery aQuery=new AQuery(contentScrollView);
		//data.to
		//Log.i(TAG,""+"返回最热商品数据:"+data.toString());


		JSONArray hoteProducts = null;
		try {
			//data.getjs
			hoteProducts = data.getJSONArray("hoteProducts");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			Log.i(TAG,""+e1);
			e1.printStackTrace();
		}
		String productType;
		String productPhotoUrl;
		String productName;
		// int position;
		int fruitCount = 0;
		int vegatableCount = 0;
		int freshCount = 0;
		int otherCount = 0;
		JSONObject hoteProductDes;
		// AQUtility.cleanCacheAsync(mainActivity);
		for (int i = 0; i < hoteProducts.length(); i++) {
			try {
				// aQuery=new AQuery(getActivity());
				final ProductInfo productInfo = new ProductInfo();
				hoteProductDes = hoteProducts.getJSONObject(i);
				productInfo.setId(Integer.parseInt(hoteProductDes
						.getString("productID")));
				productInfo
						.setName(hoteProductDes.getString("productName"));
				productInfo
						.setType(hoteProductDes.getString("productType"));
				productInfo.setDes(hoteProductDes.getString("productDes"));
				productInfo.setPrice(Float.parseFloat(hoteProductDes
						.getString("productPrice")));
				productInfo.setPhotoUrl(hoteProductDes
						.getString("productPhoto"));
				productInfo.setProductBagPrice(Float.parseFloat(hoteProductDes
						.getString("productBagPrice")));
				hoteProductInfos.add(productInfo);
				productType = hoteProductDes.getString("productType");
				productPhotoUrl = hoteProductDes.getString("productPhoto");
				productName = hoteProductDes.getString("productName");
				if (productType.equals("花果山")) {
					// aQuery=new AQuery(mainActivity);
					aQuery.id(
							IDForHoteProductDes.fruitHoteTextID[fruitCount])
							.text(productName);
					aQuery.id(
							IDForHoteProductDes.fruitHoteImageID[fruitCount])
							.image(productPhotoUrl, false, true, 150, 0)
							.clicked(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											mainActivity,
											ProductDetailActivity.class);
									intent.putExtra("productInfo",
											productInfo);
									mainActivity.startActivityForResult(
											intent, 2);
									mainActivity.overridePendingTransition(
											R.anim.activity_enter_right,
											R.anim.activity_enter_left);
								}
							});

					fruitCount++;
				} else if (productType.equals("朋香阁")) {
					// AQuery aQuery=new AQuery(mainActivity);
					aQuery.id(
							IDForHoteProductDes.vegatableHoteTextID[vegatableCount])
							.text(productName);
					aQuery.id(
							IDForHoteProductDes.vegatableHoteImageID[vegatableCount])
							.image(productPhotoUrl, false, true, 150, 0)
							.clicked(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											mainActivity,
											ProductDetailActivity.class);
									intent.putExtra("productInfo",
											productInfo);
									mainActivity.startActivityForResult(
											intent, 2);
									mainActivity.overridePendingTransition(
											R.anim.activity_enter_right,
											R.anim.activity_enter_left);
								}
							});

					vegatableCount++;
				} else if (productType.equals("生鲜")) {
					// AQuery aQuery=new AQuery(mainActivity);
					aQuery.id(
							IDForHoteProductDes.freshHoteTextID[freshCount])
							.text(productName);
					aQuery.id(
							IDForHoteProductDes.freshHoteImageID[freshCount])
							.image(productPhotoUrl, false, true, 150, 0)
							.clicked(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											mainActivity,
											ProductDetailActivity.class);
									intent.putExtra("productInfo",
											productInfo);
									mainActivity.startActivityForResult(
											intent, 2);
									mainActivity.overridePendingTransition(
											R.anim.activity_enter_right,
											R.anim.activity_enter_left);
								}
							});
					freshCount++;
				} else {
					// AQuery aQuery=new AQuery(mainActivity);
					aQuery.id(
							IDForHoteProductDes.otherHoteTextID[otherCount])
							.text(productName);
					aQuery.id(
							IDForHoteProductDes.otherHoteImageID[otherCount])
							.image(productPhotoUrl, false, true, 150, 0)
							.clicked(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											mainActivity,
											ProductDetailActivity.class);
									intent.putExtra("productInfo",
											productInfo);
									mainActivity.startActivityForResult(
											intent, 2);
									mainActivity.overridePendingTransition(
											R.anim.activity_enter_right,
											R.anim.activity_enter_left);
								}
							});
					otherCount++;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		hideView(1, fruitCount);
		hideView(2, vegatableCount);
		//hideView(3, freshCount);
		hideView(4, otherCount);
		// if(ve)
	}

	public void hideView(int type, int count) {

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				(int) ((CommonUtils.getScreenPixHeight(getActivity()) - CommonUtils.dp2px(getActivity(), 20)) / 2.0f),
				(int) ((CommonUtils.getScreenPixWidth(getActivity()) - CommonUtils.dp2px(getActivity(), 20)) / 2.0f * 0.75f));
		if (type == 1) {
			if (count == 0)
				mainActivity.findViewById(R.id.layout_fruit_hote)
						.setVisibility(View.GONE);
			for (int i = count; i < 4; i++) {
				mainActivity.findViewById(
						IDForHoteProductDes.fruitHoteTextID[i]).setVisibility(
						View.GONE);
				mainActivity.findViewById(
						IDForHoteProductDes.freshHoteImageID[i]).setVisibility(
						View.GONE);
			}
			for (int i = 0; i < count; i++) {

				mainActivity.findViewById(
						IDForHoteProductDes.fruitHoteImageID[i])
						.setLayoutParams(layoutParams);
			}

		} else if (type == 2) {
			if (count == 0)
				mainActivity.findViewById(R.id.layout_vegatable_hote)
						.setVisibility(View.GONE);
			for (int i = count; i < 4; i++) {
				mainActivity.findViewById(
						IDForHoteProductDes.vegatableHoteTextID[i])
						.setVisibility(View.GONE);
				mainActivity.findViewById(
						IDForHoteProductDes.vegatableHoteImageID[i])
						.setVisibility(View.GONE);
			}

			for (int i = 0; i < count; i++) {
				mainActivity.findViewById(
						IDForHoteProductDes.vegatableHoteImageID[i])
						.setLayoutParams(layoutParams);
			}
		} else {
			if (count == 0)
				mainActivity.findViewById(R.id.layout_other_hote)
						.setVisibility(View.GONE);
			for (int i = count; i < 4; i++) {
				mainActivity.findViewById(
						IDForHoteProductDes.otherHoteTextID[i]).setVisibility(
						View.GONE);
				mainActivity.findViewById(
						IDForHoteProductDes.otherHoteImageID[i]).setVisibility(
						View.GONE);
			}
			for (int i = 0; i < count; i++) {
				mainActivity.findViewById(
						IDForHoteProductDes.otherHoteImageID[i])
						.setLayoutParams(layoutParams);
			}
		}
	}
	
	
	public void responseUserInfo(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();			
		}
		if(data!=null){
			
			
			try {
				
				data.getString("name");
				
			
				
			} catch (Exception e) {
				
				//发生异常,则重新登录
				mainActivity.deleteFromXML();
				
				//OPUtils.showToast(e.getMessage());
				
				//Log.i(TAG,""+e);
				
			}
			
		}
	}
	
	
	class GetHoteProductInfoTask extends AsyncTask<String, Integer, Integer>{

		String productInfoContent = null;
		@Override
		protected Integer doInBackground(String... params) {
			
			try {
				HttpClient client = getHttpClient();
				//client.getParams().setParameter("command", "1");
				
				HttpPost post = new HttpPost(URLForService.HOMESERVICE+"?command=1");
				
				//	post = new HttpPost(URLForService.HOMESERVICE+"?command=1");
				//JSONObject commondObject = new JSONObject();
				
				HttpResponse response = client.execute(post);
				HttpEntity responeEntity= response.getEntity();
				productInfoContent = EntityUtils.toString(responeEntity,"utf-8");
				//Log.i(TAG,""+content);
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG,"http:错误"+e);
				//重新发送http请求
				new GetHoteProductInfoTask().execute();
			}
			
			
			//HttpEntity entity = new multipar
			//client.exec
			return 0;
		}
		
		
		@Override
		protected void onPostExecute(Integer result) {
			
			
			if(!TextUtils.isEmpty(productInfoContent)){
				try {
					
					JSONObject resultJsonObject = new JSONObject(productInfoContent);
					AjaxStatus status = new AjaxStatus();
					status.code(200);
;					requestCallback("", resultJsonObject, null);
				} catch (Exception e) {
					
					Log.i(TAG,""+e);
				}
				
				
				
			}
		}
	}
	
	
	public static HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		// 设置连接的过期时间
		int connTimeout = 2000;
		int soTimeout = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParams, connTimeout);
		HttpConnectionParams.setSoTimeout(httpParams, soTimeout);
		HttpClient client = new DefaultHttpClient(httpParams); 
		return client;
	}
}
