package cn.edu.fjnu.shop.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.activity.MainActivity;
import cn.edu.fjnu.shop.activity.ProductDetailActivity;
import cn.edu.fjnu.shop.activity.SearchActivity;
import cn.edu.fjnu.shop.adapter.CategoryAdapter;
import cn.edu.fjnu.shop.data.IDForHoteProductDes;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.domain.CategoryInfo;
import cn.edu.fjnu.shop.domain.ProductInfo;
import cn.edu.fjnu.utils.system.CommonValues;
import cn.edu.fjnu.utils.OPUtils;
import cn.edu.fjnu.utils.SizeUtils;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author GaoFei 分类页面
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {

	private ListView categoryListView;
	private MainActivity mainActivity;
	private List<ProductInfo> productInfos = new ArrayList<ProductInfo>();
	private int currentType = 1;
	private AQuery aQuery;
	private EditText shopSearchEditText;
	private TextView fruitTextView;
	private TextView vegatableTextView;
	private TextView freshTextView;
	private TextView otherTextView;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_categry, null, false);
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
		aQuery = new AQuery(getActivity());
		initView();
		initData(currentType);
		initEvent();

	}

	public void initView() {
		categoryListView = (ListView) mainActivity
				.findViewById(R.id.list_category);
		
		LinearLayout.LayoutParams layoutParams=new 
				LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
						CommonValues.screenPixHeight-SizeUtils.dp2px(150));
		
		categoryListView.setLayoutParams(layoutParams);
		shopSearchEditText=(EditText)mainActivity.findViewById(R.id.edit_shop_search);
		
		fruitTextView=(TextView)mainActivity.findViewById(R.id.text_category_fruit);
		vegatableTextView=(TextView)mainActivity.findViewById(R.id.text_category_vegatbale);
		//freshTextView=(TextView)mainActivity.findViewById(R.id.text_category_fresh);
		otherTextView=(TextView)mainActivity.findViewById(R.id.text_category_other);
		
	}

	public void initData(int type) {
		
		if(currentType==1){
			fruitTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null,getResources().getDrawable(R.drawable.blue_line));
		}else if(currentType==2){
			vegatableTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.blue_line));
		}else if(currentType==3){
			freshTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.blue_line));
		}else{
			otherTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.blue_line));
		}
		
		if(type==1){
			fruitTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null, getResources().getDrawable(R.drawable.red_line));
		}else if(type==2){
			vegatableTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.red_line));
		}else if(type==3){
			freshTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.red_line));
		}else{
			otherTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.red_line));
		}
		
		currentType=type;
		
		AQuery categoryQuery = new AQuery(mainActivity);
		/** 发送get请求 */
		categoryQuery.ajax(URLForService.CATEGORYSERVICE + type,
				JSONArray.class, this, "requestCallback");
		
		

	}

	public void setCurrentType(int currentType) {
		this.currentType = currentType;
	}
	public void requestCallback(String url, JSONArray data, AjaxStatus status) {

		// AQuery aQuery=new AQuery(contentScrollView);
		if (status.getCode() != 200) {
			OPUtils.showToast("未知错误(" + status.getCode() + ")",
					Toast.LENGTH_SHORT);

		}

		try {
			if (data != null) {
				JSONObject product;
				productInfos.clear();
				// AQUtility.cleanCacheAsync(mainActivity);
				for (int i = 0; i < data.length(); i++) {

					product = data.getJSONObject(i);
					ProductInfo productInfo = new ProductInfo();
					productInfo.setId(Integer.parseInt(product
							.getString("productID")));
					productInfo.setName(product.getString("productName"));
					productInfo.setType(product.getString("productType"));
					productInfo.setDes(product.getString("productDes"));
					if(!OPUtils.isEmpty(product.getString("productPrice")))
						productInfo.setPrice(Float.parseFloat(product.getString("productPrice")));
					productInfo.setPhotoUrl(product.getString("productPhoto"));
					//productInfo.setProductBagPrice(productBagPrice)
					if(!OPUtils.isEmpty(product.getString("producttBagPrice")))
						productInfo.setProductBagPrice(Float.parseFloat(product.getString("producttBagPrice")));
					productInfos.add(productInfo);

				}

				CategoryAdapter categoryAdapter = new CategoryAdapter(
						mainActivity, productInfos);
				categoryListView.setAdapter(categoryAdapter);

			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void initEvent() {
		categoryListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mainActivity,
								ProductDetailActivity.class);
						intent.putExtra("productInfo", (ProductInfo) parent
								.getAdapter().getItem(position));
						mainActivity.startActivityForResult(intent, 2);
						mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
						// OPUtils.startActivity(mainActivity,
						// ProductDetailActivity.class);
					}
				});

		aQuery.id(R.id.text_category_fruit).clicked(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// initData()
				if (currentType == 1)
					return;
				else
					initData(1);
					
				
					
			}
		});

		aQuery.id(R.id.text_category_vegatbale).clicked(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (currentType == 2)
							return;
						else
							initData(2);
					}
				});
		

		aQuery.id(R.id.text_category_other).clicked(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (currentType == 4)
							return;
						else
							initData(4);
					}
				});
		
		aQuery.id(R.id.img_shop_search).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(OPUtils.isEmpty(shopSearchEditText.getText().toString())){
					OPUtils.showToast("请输入搜索内容", Toast.LENGTH_SHORT);
					return;
				}
				
				Intent intent=new Intent(mainActivity,SearchActivity.class);
				intent.putExtra("searchName", shopSearchEditText.getText().toString());
				mainActivity.startActivityForResult(intent, 4);
				mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
