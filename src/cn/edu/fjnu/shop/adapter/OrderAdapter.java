/**
 * 
 */
package cn.edu.fjnu.shop.adapter;
import java.util.List;

import com.androidquery.AQuery;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.domain.OrderItemInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * @author GaoFei
 * 订单列表适配器
 */
public class OrderAdapter extends BaseAdapter {

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	private Context context;
	private List<OrderItemInfo> infos;
	public OrderAdapter(Context context,List<OrderItemInfo> infos){
		this.context=context;
		this.infos=infos;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view= inflater.inflate(R.layout.adapter_my_order, null);
		AQuery aQuery=new AQuery(view);
		OrderItemInfo info= (OrderItemInfo)getItem(position);
		aQuery.id(R.id.text_order_no).text(info.getOrderID());
		aQuery.id(R.id.text_order_date).text(info.getOrderDate());
		aQuery.id(R.id.text_expire_date).text(info.getExpireDate());
		aQuery.id(R.id.text_order_state).text(info.getOrderState());
		aQuery.id(R.id.text_order_money).text(info.getOrderMoney());
		aQuery.id(R.id.text_order_product).text(info.getOrderProduct());
		view.setBackgroundResource(R.drawable.list_item_background);
		return view;
		//return null;
	}

}
