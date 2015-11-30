package cn.edu.fjnu.shop.fragment;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.activity.CommitOrderActivity;
import cn.edu.fjnu.shop.activity.LoginActivity;
import cn.edu.fjnu.shop.activity.MainActivity;
import cn.edu.fjnu.shop.customview.CarShopContentView;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.dialog.LoadingDialog;
import cn.edu.fjnu.shop.domain.DetailCarShop;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.system.CommonValues;
import cn.edu.fjnu.utils.OPUtils;
import cn.edu.fjnu.utils.SizeUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author GaoFei
 * 
 */
public class CartFragment extends Fragment {
	private static final String TAG = "CartFragment";
	private MainActivity mainActivity;
	private LinearLayout layout;
	private ScrollView contentScrollView;
	private TextView cartNoDataTextView;
	private AQuery aQuery;
	private List<DetailCarShop> mShops;
	private float all=0;
	private AQuery callAQuery;
	private int orignNumber;
	private List<CarShopContentView> contentViews=new ArrayList<CarShopContentView>();
	private DecimalFormat decimalFormat=new DecimalFormat();
	private LoadingDialog loadingDialog;
	//选中的购物车列表视图
	private CarShopContentView currentCarShopContentView;
	//选中的商品数量
	private int currentShopNumber;
	//对于商品的数据请求查询
	private AQuery currentAQuery;
	public CartFragment(List<DetailCarShop> shops){
		UserInfo.isEmptyShopCar=false;
		mShops=shops;
	}
	
	public CartFragment(){
		
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		RelativeLayout relativeLayout=new RelativeLayout(getActivity());
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
		//relativeLayout.
		
		if(UserInfo.isLogin){
			if(UserInfo.isEmptyShopCar)
				return inflater.inflate(R.layout.fragment_cart_login, relativeLayout,false);
			else
				return inflater.inflate(R.layout.fragment_carshop,relativeLayout, false);
		}else{
			return inflater.inflate(R.layout.fragment_cart_no_login, relativeLayout,false);
		}

	}
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mainActivity=(MainActivity)getActivity();
		aQuery=new AQuery(mainActivity);
		decimalFormat.setMaximumFractionDigits(2);
		initView();
		initData();
	}
	public void initView(){
		if(UserInfo.isLogin){
			if(UserInfo.isEmptyShopCar==false){
				contentScrollView=(ScrollView)mainActivity.findViewById(R.id.scroll_content);
				RelativeLayout.LayoutParams scrollLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
						CommonValues.screenPixHeight-SizeUtils.dp2px(170));
				scrollLayoutParams.addRule(RelativeLayout.BELOW, R.id.layout_title);
				//contentScrollView.setLayoutParams(n)
				contentScrollView.setLayoutParams(scrollLayoutParams);
				layout=(LinearLayout) mainActivity.findViewById(R.id.layout_content);
				for(int i=0;i<mShops.size();i++){
					
					DetailCarShop shop=mShops.get(i);
					final  CarShopContentView carShopContentView=new CarShopContentView(mainActivity);
					contentViews.add(carShopContentView);
					final AQuery contentAQuery=new AQuery(carShopContentView);
					contentAQuery.id(R.id.img_product_photo).image(shop.getProductPhoto(),false,true);
					contentAQuery.id(R.id.img_shop_select).clicked(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							AQuery selectAQuery=new AQuery(v);
							if(carShopContentView.isSelected){
								
								selectAQuery.image(R.drawable.carshop_uncheck);
								carShopContentView.isSelected=false;
								/**减掉总金额*/
								DetailCarShop selectShop=mShops.get(carShopContentView.index);
								all=all-selectShop.getProductPrice()*selectShop.getProductNumber();
								/**隐藏控件*/
								contentAQuery.id(R.id.text_product_min_calc).invisible();
								callAQuery.id(R.id.text_calc_all).text("合计:¥"+decimalFormat.format(all));
							}else{
								selectAQuery.image(R.drawable.carshop_check);
								carShopContentView.isSelected=true;
								/**加上总金额*/
								DetailCarShop selectShop=mShops.get(carShopContentView.index);
								all=all+selectShop.getProductPrice()*selectShop.getProductNumber();
								/**显示控件*/
								contentAQuery.id(R.id.text_product_min_calc).visible();
								callAQuery.id(R.id.text_calc_all).text("合计:¥"+decimalFormat.format(all));
							}
						}
					});
					
					contentAQuery.id(R.id.text_delete).clicked(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							//向服务器发送删除请求
						//	DetailCarShop serviceDeleteCarShop=new DetailCarShop();
							Map<String, String>  params=new HashMap<String, String>();
							//客户端提供userID和productID
							params.put("command", "3");
							params.put("userID",String.valueOf(UserInfo.userID));
							params.put("productID",String.valueOf(mShops.get(carShopContentView.index).getProductID()));
							params.put("unit", mShops.get(carShopContentView.index).getProductUnit());
							aQuery.ajax(URLForService.CARSHOPSERVICE, params, JSONObject.class, CartFragment.this, "responseDeleteCarShop");
							//载入进度条对话框
							showLoadingDialog();
							
							currentCarShopContentView=carShopContentView;
							
							
						}
					});
					contentAQuery.id(R.id.text_product_name).text(shop.getProductName());
					//获取单位
					if("袋".equals(shop.getProductUnit())){
						contentAQuery.id(R.id.text_product_price).text("¥"+shop.getProductBagPrice()+"/袋");
					}else{
						
						contentAQuery.id(R.id.text_product_price).text("¥"+shop.getProductPrice()+"/斤");
					}
					//contentAQuery.id(R.id.text_product_price).text("¥"+shop.getProductPrice()+"/斤");
					contentAQuery.id(R.id.edit_number).text(""+shop.getProductNumber()).clicked(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							orignNumber=mShops.get(carShopContentView.index).getProductNumber();
							final EditText numberEditText=new EditText(mainActivity);
							numberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
							numberEditText.setText(""+orignNumber);
							new AlertDialog.Builder(mainActivity).setTitle("输入商品数量")
							.setView(numberEditText).setPositiveButton("确定",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									try {
										//dialog.dismiss();
										int num =Integer.parseInt(numberEditText.getText().toString().trim());
										if(num>0){
											dialog.dismiss();
											//修改服务器中数据库中的数据
											Map<String, String> params=new HashMap<String, String>();
											params.put("command", "1");
											params.put("userID",String.valueOf(UserInfo.userID));
											params.put("productID",String.valueOf(mShops.get(carShopContentView.index).getProductID()));
											params.put("productNumber", String.valueOf(num-orignNumber));
											
											aQuery.ajax(URLForService.CARSHOPSERVICE, params, JSONObject.class, CartFragment.this, "responseChangeProductNumber");
											showLoadingDialog();
											currentCarShopContentView=carShopContentView;
											currentShopNumber=num;
											currentAQuery=contentAQuery;
											
										}else{
											OPUtils.showToast("输入的商品数量至少为1",Toast.LENGTH_SHORT);
										}
										
									} catch (Exception e) {
										// TODO: handle exception
										OPUtils.showToast("输入错误",Toast.LENGTH_SHORT);
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
					contentAQuery.id(R.id.text_product_min_calc).text("小计:¥"+decimalFormat.format(shop.getProductPrice()*shop.getProductNumber()));
					carShopContentView.isSelected=true;
					carShopContentView.index=i;
					//contentAQuery.id(R.id.text_)
					//carShopContentView.findViewById(R.id.img_product_photo)
					//carShopContentView.fin
					layout.addView(carShopContentView);
					
					
				}
				
				callAQuery=new AQuery(mainActivity);
				callAQuery.id(R.id.img_select_all).invisible();
				callAQuery.id(R.id.text_select_all).invisible();
				callAQuery.id(R.id.text_go_call).clicked(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						boolean isSelected=false;
						if(contentViews.isEmpty()){
							OPUtils.showToast("购物车为空", Toast.LENGTH_SHORT);
							return ;
						}else{
							for(int i=0;i<contentViews.size();i++){
								
								isSelected=isSelected||contentViews.get(i).isSelected;
							}
							
							if(isSelected==false){
								OPUtils.showToast("未选中任何商品", Toast.LENGTH_SHORT);
								return ;
							}
						}
						
						
						
						Intent intent=new Intent(mainActivity, CommitOrderActivity.class);
						/**传递商品总金额*/
						intent.putExtra("productMoney", all);
						String productIDs="";
						String productNums="";
						String units="";
						/**传递订单商品ID和数量*/
						//System.out.println(contentViews.size());
						for(int i=0;i<contentViews.size();i++){
							/**如果这个控件被选中*/
							if(contentViews.get(i).isSelected){
								//mShops[contentViews.get(i).index] 
								DetailCarShop detail=mShops.get(contentViews.get(i).index);
								productIDs+=(String.valueOf(detail.getProductID())+";");
								productNums+=(detail.getProductNumber()+";");
								units+=(detail.getProductUnit()+";");
								//p
							}
						}
						
						intent.putExtra("productIDs", productIDs);
						intent.putExtra("productNums", productNums);
						intent.putExtra("unit", units);
						intent.putExtra("fromType", "2");
						mainActivity.startActivityForResult(intent, 3);
						mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
						
					}
				});
				
				for(int i=0;i<mShops.size();i++){
					
					DetailCarShop detail= mShops.get(i);
					all+=detail.getProductPrice()*detail.getProductNumber();
					callAQuery.id(R.id.text_calc_all).text("合计:¥"+decimalFormat.format(all));
				}
				
			}else{
				
				cartNoDataTextView=(TextView)mainActivity.findViewById(R.id.text_cart_no_data);
			}
			
		}else{
			//用户未登录
			aQuery.id(R.id.btn_login_carshop).clicked(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//OPUtils.startActivityForResult(activity, otherClass, requestCode)
					Intent intent=new Intent(mainActivity, LoginActivity.class);
					mainActivity.startActivityForResult(intent, 5);
					mainActivity.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
				}
			});
			//btn_login_carshop
		}
	}
	
	public void initData(){
		if(UserInfo.isLogin){
			if(UserInfo.isEmptyShopCar){
				Map<String, String> params=new HashMap<String, String>();
				params.put("command","2");
				params.put("userID",String.valueOf(UserInfo.userID));
				aQuery.ajax(URLForService.CARSHOPSERVICE, params, JSONObject.class, this, "responseCarShop");
				showLoadingDialog();
				cartNoDataTextView.setText("购物车同步中...");
				//mainActivity.setCartFragment();
			}
			
		}

	}
	
	public void responseCarShop(String url,JSONObject data,AjaxStatus status){
		//mainActivity.getsu
		
		
		if(status.getCode()!=200){
			closeLoadingDialog();
			OPUtils.showToast("未知错误("+status.getCode()+")", Toast.LENGTH_SHORT);
			
		}
		if(data!=null){
			try {
				
				List<DetailCarShop> shops=new ArrayList<DetailCarShop>();
				JSONArray jsonShops =data.getJSONArray("shops");
				//jsonShops.get(index)
				for(int i=0;i<jsonShops.length();i++){
					JSONObject object=jsonShops.getJSONObject(i);
					DetailCarShop detailCarShop=new DetailCarShop();
					detailCarShop.setUserID(object.getInt("userID"));
					detailCarShop.setProductID(object.getInt("productID"));
					detailCarShop.setProductNumber(object.getInt("productNumber"));
					detailCarShop.setProductName(object.getString("productName"));
					detailCarShop.setProductDes(object.getString("productDes"));
					detailCarShop.setProductPrice(Float.parseFloat(object.getString("productPrice")));
					detailCarShop.setProductPhoto(object.getString("productPhoto"));
					detailCarShop.setProductUnit(object.getString("productUnit"));
					detailCarShop.setProductBagPrice(Float.parseFloat(object.getString("productBagPrice")));
					shops.add(detailCarShop);
					//object.getInt("userID");
					//OPUtils.ShowToast(mainActivity,""+object.getInt("userID"), Toast.LENGTH_SHORT);
				}
				if(shops.isEmpty())
					cartNoDataTextView.setText("购物车肚子空空");
				else{
					
					
					//List<det>
					//OPUtils.showToast(shops.get(0).getProductName(), Toast.LENGTH_SHORT);
					mainActivity.setCartFragment(shops);
					
				}
				
				closeLoadingDialog();
			} catch (Exception e) {
				Log.i(TAG,""+e);
				e.printStackTrace();
				// TODO: handle exception
				closeLoadingDialog();
				//OPUtils.showToast("发生异常"+e.toString(), Toast.LENGTH_SHORT);
				//发送异常之后,重新载入CartFragment
				mainActivity.reloadCartFragment();
			}
		}
	}
	
	public void  responseDeleteCarShop(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			closeLoadingDialog();
			OPUtils.showToast("未知错误("+status.getCode()+")", Toast.LENGTH_SHORT);
			
		}
		if(data!=null){
			try {
				
				
				String deleteState =data.getString("deleteState");
				//jsonShops.get(index)
				//成功删除
				if("success".equals(deleteState)){
					
					//修改商品总额
					DetailCarShop deleteShop= mShops.get(currentCarShopContentView.index);
					
					if(currentCarShopContentView.isSelected){
						all=all- deleteShop.getProductPrice()*deleteShop.getProductNumber();
						//修改商品总额显示
						callAQuery.id(R.id.text_calc_all).text("合计:¥"+decimalFormat.format(all));
						
					}
					//删除mShops中对应的数据,移除相关控件
					mShops.remove(currentCarShopContentView.index);
					contentViews.remove(currentCarShopContentView.index);
					layout.removeView(currentCarShopContentView);
					//重新生成索引
					for(int i=0;i<mShops.size();i++){
						contentViews.get(i).index=i;
					}
					
					closeLoadingDialog();
					OPUtils.showToast("删除成功", Toast.LENGTH_SHORT);
				}else{
					
					closeLoadingDialog();
					OPUtils.showToast("删除失败", Toast.LENGTH_SHORT);
				}
			} catch (Exception e) {
				// TODO: handle exception
				closeLoadingDialog();
				OPUtils.showToast("发生异常", Toast.LENGTH_SHORT);
			}
		}
	}
	
	public void responseChangeProductNumber(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			closeLoadingDialog();
			OPUtils.showToast("未知错误("+status.getCode()+")", Toast.LENGTH_SHORT);
			
		}
		if(data!=null){
			try {
				
				
				String addState =data.getString("addState");
				//jsonShops.get(index)
				//成功修改
				if("success".equals(addState)){
					
					//mShops中的数据修改
					mShops.get(currentCarShopContentView.index).setProductNumber(currentShopNumber);
					//填充数据到数字框中
					DetailCarShop changeShop=mShops.get(currentCarShopContentView.index);
					currentAQuery.id(R.id.edit_number).text(""+changeShop.getProductNumber());
					//修改小计
					currentAQuery.id(R.id.text_product_min_calc).text("小计:¥"+decimalFormat.format(changeShop.getProductPrice()*currentShopNumber));
					//修改合计
					all=all+(currentShopNumber-orignNumber)*mShops.get(currentCarShopContentView.index).getProductPrice();
					callAQuery.id(R.id.text_calc_all).text("合计:¥"+decimalFormat.format(all));
					closeLoadingDialog();
					OPUtils.showToast("修改成功", Toast.LENGTH_SHORT);
				}else{
					closeLoadingDialog();
					OPUtils.showToast("修改失败", Toast.LENGTH_SHORT);
				}
			} catch (Exception e) {
				// TODO: handle exception
				closeLoadingDialog();
				OPUtils.showToast("发生异常"+e.toString(), Toast.LENGTH_SHORT);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(UserInfo.isEmptyShopCar==false)
			UserInfo.isEmptyShopCar=true;
	}
	
	
	public void showLoadingDialog(){
		
		loadingDialog=new LoadingDialog(mainActivity);
		loadingDialog.show();
	}
	
	
	public void closeLoadingDialog(){
		
		if(loadingDialog!=null&&loadingDialog.isShowing())
			loadingDialog.dismiss();
	}
}
