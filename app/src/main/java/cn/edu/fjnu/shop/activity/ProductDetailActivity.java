package cn.edu.fjnu.shop.activity;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.data.IDForProductDetail;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.domain.ProductInfo;
import cn.edu.fjnu.shop.fragment.CartFragment;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.shop.utils.CommonUtils;
import cn.edu.fjnu.shop.utils.DialogUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author GaoFei
 * 商品详情页面
 */
public class ProductDetailActivity extends Activity implements OnClickListener{

	private ImageView photoImageView;
	private TextView nameTextView;
	private TextView priceTextView;
	private TextView desTextView;
	private ImageView backImageView;
	private TextView addToCartTextView;
	private TextView subTextView;
	private TextView numTextView;
	private TextView addTextView;
	private TextView buynowTextView;
	private ScrollView contentScrollView;
	private AQuery aQuery;
	private ProductInfo productInfo;
	/**选择的商品价格*/
	private float selctProductPrice;
	/**选择的配送城市*/
	private String selectCity;
	/**选择的商品数量*/
	private int selctProductNum=1;
	/**选择的单位*/
	private String selectUnit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			ProductDetailActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return false;
	}
	
	public void initView(){
		photoImageView=(ImageView)findViewById(R.id.img_product_photo);
		photoImageView.setLayoutParams(
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(CommonUtils.getScreenPixWidth(this)*0.75f)));
		
		
		contentScrollView = (ScrollView)findViewById(R.id.layout_content);
		contentScrollView.setLayoutParams(
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, CommonUtils.getScreenPixHeight(this)- CommonUtils.dp2px(this,120)));
		
		nameTextView=(TextView)findViewById(R.id.text_product_name);
		priceTextView=(TextView)findViewById(R.id.text_product_price);
		desTextView=(TextView)findViewById(R.id.text_product_des);
		addToCartTextView=(TextView)findViewById(R.id.text_add_to_cart);
		backImageView=(ImageView)findViewById(R.id.img_product_detail_back);
		subTextView = (TextView)findViewById(R.id.text_sub);
		numTextView = (TextView)findViewById(R.id.text_product_num);
		addTextView = (TextView)findViewById(R.id.text_add);
		buynowTextView = (TextView)findViewById(R.id.text_buy_now);
		backImageView.setOnClickListener(this);
		cityClick(0);
		
		TextView fuzhouTextView = (TextView)findViewById(R.id.text_fuzhou);
		fuzhouTextView.setBackgroundResource(R.drawable.unit_background_selected);
		
	}
	
	public void initData(){
		
		aQuery=new AQuery(this);
		
		productInfo=(ProductInfo)getIntent().getExtras().get("productInfo");
		nameTextView.setText(productInfo.getName());
		if(productInfo.getPrice()>0.0001f)
			priceTextView.setText("¥"+productInfo.getPrice()+"/斤");
		else
			priceTextView.setText("¥"+productInfo.getProductBagPrice()+"/袋");
		if(productInfo.getPrice()>0.0001f)
			unitClick(0);
		else
			unitClick(1);
		
		if(productInfo.getPrice()<0.0001f){
			//隐藏按钮
			TextView selectIndexTextView = (TextView)findViewById(IDForProductDetail.unitIDs[0]);
			selectIndexTextView.setVisibility(View.GONE);
			
		}
		
		if(productInfo.getProductBagPrice()<0.0001f){
			//隐藏按钮
			TextView selectIndexTextView = (TextView)findViewById(IDForProductDetail.unitIDs[1]);
			selectIndexTextView.setVisibility(View.GONE);
			
		}
		
		desTextView.setText(productInfo.getDes());
		aQuery.id(R.id.img_product_photo).image(productInfo.getPhotoUrl(),false,true);
		aQuery.id(R.id.text_add_to_cart).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String, String> params=new HashMap<String, String>();
				String isLogin="false";
				params.put("command","1");
				if(UserInfo.userID==-1){
					startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
					overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
					return;
				}else{
					
					int userID= UserInfo.userID;
					params.put("userID", String.valueOf(userID));
					params.put("productID",String.valueOf(productInfo.getId()));
					params.put("productNumber",String.valueOf(selctProductNum));
					params.put("unit", selectUnit);
					//params.put("num", ""+selctProductNum);
					DialogUtils.showLoadingDialog(ProductDetailActivity.this);
					aQuery.ajax(URLForService.CARSHOPSERVICE, params, JSONObject.class, ProductDetailActivity.this,"responseAddToCart");
				}
				
					
			}
		});
		
		if(UserInfo.userID!=-1){
			
			Map<String, String> params=new HashMap<String, String>();
			params.put("command", "3");
			params.put("userID",String.valueOf(UserInfo.userID));
			params.put("productID", String.valueOf(productInfo.getId()));;
			aQuery.ajax(URLForService.WATCHSERVICE, params, JSONObject.class, this, "responseWatchExist");
		}
		
	}

	public void initEvent(){
		
		
		
		
		findViewById(R.id.img_cart).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/**跳转到购物车页面*/
				if(UserInfo.userID==-1){
					startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
					overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
					return;
					
				}else{
					setResult(3);
					ProductDetailActivity.this.finish();
					overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
				}
		}
		});
		
		
		aQuery.id(R.id.img_my_watch).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(UserInfo.userID==-1){
					startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
					overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
					return;
					
				}
				
				Map<String, String> params=new HashMap<String, String>();
				params.put("command", "2");
				params.put("userID", String.valueOf(UserInfo.userID));
				//params.put("productID", produc)
				params.put("productID", String.valueOf(productInfo.getId()));
				aQuery.ajax(URLForService.WATCHSERVICE, params, JSONObject.class, new AjaxCallback<JSONObject>(){
					@Override
					public void callback(String url, JSONObject object,
							AjaxStatus status) {
						// TODO Auto-generated method stub
						//super.callback(url, object, status);
						if (status.getCode() != 200) {
							Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();

						}
						if(object!=null){
							try {
								String state=object.getString("state");
								if("addSuccess".equals(state)){
									Toast.makeText(getApplicationContext(), "关注成功", Toast.LENGTH_SHORT).show();

									aQuery.id(R.id.img_my_watch).image(R.drawable.watch_red);
									//accessDialog.dismiss();
								}else if("deleteSuccess".equals(state)){
									Toast.makeText(getApplicationContext(), "取消关注", Toast.LENGTH_SHORT).show();
									aQuery.id(R.id.img_my_watch).image(R.drawable.watch);
								}else{
									Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
								}
									
								
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(), "操作失败", Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
						}
					}
					
				});
			}
		});
		
		for (int i = 0;i<IDForProductDetail.unitIDs.length;i++){
			
			final int index = i;
			TextView tmpTextView = (TextView)findViewById(IDForProductDetail.unitIDs[i]);
			tmpTextView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					unitClick(index);
				}
			});
		}
		
		for (int i = 0;i<IDForProductDetail.cityIDs.length;i++){
			
			final int index = i;
			TextView tmpTextView = (TextView)findViewById(IDForProductDetail.cityIDs[i]);
			tmpTextView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					cityClick(index);
				}
			});
		}
		
		
		subTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//获取原数据
				int originNum = Integer.parseInt(numTextView.getText().toString());
				originNum --;
				numTextView.setText(""+originNum);
				selctProductNum = originNum;
			}
		});
		
		numTextView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				//获取原数据
				int originNum = Integer.parseInt(numTextView.getText().toString());
				selctProductNum = originNum;
				if(originNum<1){
					
					numTextView.setText(""+1);
					selctProductNum = 1;
				}
				
				
			}
		});
		
		numTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final EditText numberEditText=new EditText(ProductDetailActivity.this);
				numberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
				numberEditText.setText(""+selctProductNum);
				new AlertDialog.Builder(ProductDetailActivity.this).setTitle("输入商品数量")
				.setView(numberEditText).setPositiveButton("确定",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {
							//dialog.dismiss();
							int num =Integer.parseInt(numberEditText.getText().toString().trim());
							if(num>0){
								numTextView.setText(""+num);
								dialog.dismiss();
								
							}else{
								Toast.makeText(getApplicationContext(), "输入的商品数量至少为1", Toast.LENGTH_SHORT).show();
							}
							
						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(getApplicationContext(), "输入错误", Toast.LENGTH_SHORT).show();
						}
						
						
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
				
			}
		});
		
		addTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				//获取原数据
				int originNum = Integer.parseInt(numTextView.getText().toString());
				originNum ++;
				numTextView.setText(""+originNum);
				selctProductNum = originNum;
			}
		});
		
		buynowTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductDetailActivity.this, CommitOrderActivity.class);
				intent.putExtra("productIDs", ""+productInfo.getId()+";");
				intent.putExtra("productNums", selctProductNum+";");
				intent.putExtra("productMoney", selctProductPrice*selctProductNum);
				intent.putExtra("city",selectCity);
				intent.putExtra("unit",selectUnit);
				intent.putExtra("fromType","1");
				startActivity(intent);
				overridePendingTransition(R.anim.activity_enter_right,R.anim.activity_enter_left);
				
			}
		});
		
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_product_detail_back:
			ProductDetailActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			break;
		default:
			break;
		}
	}
	
	
	public void responseAddToCart(String url,JSONObject data,AjaxStatus status){
		

		if(status.getCode()!=200){
			Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();			
		}
		if(data!=null){
			try {
				String state=data.getString("addState");
				if("success".equals(state)){
					
					DialogUtils.closeLoadingDialog();
					Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
					
					//ProductDetailActivity.this.finish();
				}else{
					DialogUtils.closeLoadingDialog();
					Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
				DialogUtils.closeLoadingDialog();
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	public void responseWatchExist(String url,JSONObject data,AjaxStatus status){
		//super.callback(url, object, status);
		if (status.getCode() != 200) {
			Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();

		}
		if(data!=null){
			
			try {
				
				String isExist=data.getString("exist");
				if("true".equals(isExist)){
					aQuery.id(R.id.img_my_watch).image(R.drawable.watch_red);
				}else{
					aQuery.id(R.id.img_my_watch).image(R.drawable.watch);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	
	private void unitClick(int index){
		
		if(index == 0){
			//修改价格
			selctProductPrice = productInfo.getPrice();
			priceTextView.setText("¥"+productInfo.getPrice()+"/斤");
		}else{
			
			selctProductPrice = productInfo.getProductBagPrice();
			priceTextView.setText("¥"+productInfo.getProductBagPrice()+"/袋");
		}
		
		for(int i=0;i<IDForProductDetail.unitIDs.length;i++){
			
			if(i == index){
				TextView selectIndexTextView = (TextView)findViewById(IDForProductDetail.unitIDs[i]);
				selectIndexTextView.setBackgroundResource(R.drawable.unit_background_selected);
				selectUnit = selectIndexTextView.getText().toString();
			}else{
				
				TextView unSelectedTextView = (TextView)findViewById(IDForProductDetail.unitIDs[i]);
				unSelectedTextView.setBackgroundResource(R.drawable.unit_background);
			}
		}
	}
	
	
	
	private void cityClick(int index){
		

		
		
		for(int i=0;i<IDForProductDetail.cityIDs.length;i++){
			
			if(i == index){
				TextView selectIndexTextView = (TextView)findViewById(IDForProductDetail.cityIDs[i]);
				selectIndexTextView.setBackgroundResource(R.drawable.unit_background_selected);
				selectCity = selectIndexTextView.getText().toString();
			}else{
				
				TextView unSelectedTextView = (TextView)findViewById(IDForProductDetail.cityIDs[i]);
				unSelectedTextView.setBackgroundResource(R.drawable.unit_background);
			}
		}
	}
	
}
