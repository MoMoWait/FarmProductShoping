package cn.edu.fjnu.shop.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.activity.MainActivity;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.dialog.AccessDialog;
import cn.edu.fjnu.shop.dialog.ExitDialog;
import cn.edu.fjnu.shop.domain.OrderItemInfo;
import cn.edu.fjnu.shop.domain.ProductInfo;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.system.CommonValues;
import cn.edu.fjnu.utils.OPUtils;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AccessAdapter extends BaseAdapter {

	private Context context;
	private List<OrderItemInfo> infos;
	
	public AccessAdapter(Context context,List<OrderItemInfo> productInfos){
		this.context=context;
		this.infos=productInfos;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int orderPosition=position;
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view= inflater.inflate(R.layout.adapter_access, null);
		AQuery aQuery=new AQuery(view);
		OrderItemInfo info= (OrderItemInfo)getItem(position);
		aQuery.id(R.id.text_order_no).text(info.getOrderID());
		aQuery.id(R.id.text_order_date).text(info.getOrderDate());
		aQuery.id(R.id.text_real_pay).text("实付:¥"+info.getOrderMoney());
		aQuery.id(R.id.text_order_product).text(info.getOrderProduct());
		aQuery.id(R.id.text_away_access).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	final AccessDialog accessDialog=new AccessDialog(context);
		    	accessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	accessDialog.setCancelable(false);
		    	accessDialog.setContentView(R.layout.dialog_access);
		    	accessDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_backgoroud_exit);
		    	accessDialog.getWindow().setLayout((int)(CommonValues.screenPixWidth*0.8f), -2);
				//exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		    	accessDialog.getWindow().findViewById(R.id.btn_exit_cancel).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						accessDialog.dismiss();
					}
				});
				
		    	accessDialog.getWindow().findViewById(R.id.btn_exit_commit).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						//accessDialog.dismiss();
						EditText contentEditText=(EditText)accessDialog.getWindow().findViewById(R.id.text_access_content);
						if(OPUtils.isEmpty(contentEditText.getText().toString())){
							OPUtils.showToast("反馈内容为空", Toast.LENGTH_SHORT);
							return ;
						}
						//MainActivity.this.finish();
						//发送评价请求
						AQuery accessAQuery=new AQuery(context);
						Map<String, String> params=new HashMap<String, String>();
						params.put("command","1");
						params.put("userID",String.valueOf(UserInfo.userID));
						params.put("orderID", infos.get(orderPosition).getOrderID());
						params.put("accessContent",contentEditText.getText().toString());
						accessAQuery.ajax(URLForService.ACCESSSERVICE, params, JSONObject.class,new AjaxCallback<JSONObject>(){
							@Override
							public void callback(String url, JSONObject object,
									AjaxStatus status) {
								// TODO Auto-generated method stub
								if (status.getCode() != 200) {
									OPUtils.showToast("未知错误(" + status.getCode() + ")",
											Toast.LENGTH_SHORT);

								}

								if(object!=null){
									try {
										String state=object.getString("accessState");
										if("success".equals(state)){
											OPUtils.showToast("评价成功", Toast.LENGTH_SHORT);
											infos.remove(orderPosition);
											notifyDataSetChanged();
											accessDialog.dismiss();
										}
											
										else
											OPUtils.showToast("评价失败", Toast.LENGTH_SHORT);
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										OPUtils.showToast("评价失败", Toast.LENGTH_SHORT);
									}
								}
							}
							
						});
					}
				});
		    	accessDialog.show();
			}
		});
		view.setBackgroundResource(R.drawable.list_item_background);
		return view;
	}

}
