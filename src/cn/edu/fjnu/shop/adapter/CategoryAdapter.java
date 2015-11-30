package cn.edu.fjnu.shop.adapter;

import java.util.LinkedList;
import java.util.List;
import com.androidquery.AQuery;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.domain.ProductInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author GaoFei
 * 分类列表下的适配器
 */
public class CategoryAdapter extends BaseAdapter {

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	
	private Context context;
	private List<ProductInfo> productInfos;
	private AQuery aQuery;
	private List<ViewHolder> viewHolders=new LinkedList<ViewHolder>();
	private ViewHolder currentViewHolder=new ViewHolder();
	public CategoryAdapter(Context context,List<ProductInfo> productInfos){
		this.context=context;
		this.productInfos=productInfos;
		aQuery=new AQuery(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return productInfos.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return productInfos.get(position);
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
		
		//先判断viewHolders是否存在convertView
		//if(viewHolders)
		currentViewHolder.position=position;
		int viewSize=viewHolders.size();
		for(int i=0;i<viewSize;i++){
			if(viewHolders.get(i).equals(currentViewHolder))
				return viewHolders.get(i).view;
		}
		LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView=layoutInflater.inflate(R.layout.adapter_category,null);
		TextView productNameTextView=(TextView)convertView.findViewById(R.id.text_category_product_name);
		TextView productPriceTextView=(TextView)convertView.findViewById(R.id.text_category_product_price);
		TextView productBagTextView = (TextView)convertView.findViewById(R.id.text_category_product_bag_price);
		ImageView productPhotoImageView=(ImageView)convertView.findViewById(R.id.img_product_min_photo);
		ProductInfo productInfo=(ProductInfo)getItem(position);
		productNameTextView.setText(productInfo.getName());
		if(productInfo.getPrice()<0.0001f)
			productPriceTextView.setVisibility(View.GONE);
		else
			productPriceTextView.setText("¥"+productInfo.getPrice()+"/斤");
		
		if(productInfo.getProductBagPrice()<0.0001f)
			productBagTextView.setVisibility(View.GONE);
		else
			productBagTextView.setText("¥"+productInfo.getProductBagPrice()+"/袋");
		//productBagTextView.setText(text)
		aQuery=new AQuery(productPhotoImageView);
		aQuery.image(productInfo.getPhotoUrl(), false, true,100,0);
		convertView.setBackgroundResource(R.drawable.list_item_background);
		ViewHolder viewHolder=new ViewHolder();
		viewHolder.position=position;
		viewHolder.view=convertView;
		//addViewToHolder();
		addViewToHolder(viewHolder);
		return convertView;
		//return null;
	}

	
	private class ViewHolder{
		
		public View view;
		public int position;
		
		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			
			if(o!=null&& (o instanceof ViewHolder)){
				ViewHolder other=(ViewHolder)o;
				return this.position==other.position;
			}
			return false;
		}
		
	}
	
	
	public void addViewToHolder(ViewHolder viewHolder ){
		if(viewHolders.size()<20){
			viewHolders.add(viewHolder);
		}else{
			viewHolders.remove(0);
			viewHolders.add(viewHolder);
		}
	}
	
}
