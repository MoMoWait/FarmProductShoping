/**
 * 
 */
package cn.edu.fjnu.shop.adapter;

import java.util.List;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.domain.ProductInfo;

import com.androidquery.AQuery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author GaoFei
 * 搜索列表适配器
 */
public class SearchAdapter extends BaseAdapter {

	private Context context;
	private List<ProductInfo> productInfos;
	private AQuery aQuery;
	
	public SearchAdapter(Context context,List<ProductInfo> productInfos){
		this.context=context;
		this.productInfos=productInfos;
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
		LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView=layoutInflater.inflate(R.layout.adapter_category,null);
		TextView productNameTextView=(TextView)convertView.findViewById(R.id.text_category_product_name);
		TextView productPriceTextView=(TextView)convertView.findViewById(R.id.text_category_product_price);
		TextView productBagTextView = (TextView)convertView.findViewById(R.id.text_category_product_bag_price);
		
		ImageView productPhotoImageView=(ImageView)convertView.findViewById(R.id.img_product_min_photo);
		ProductInfo productInfo=(ProductInfo)getItem(position);
		productNameTextView.setText(productInfo.getName());
		if(productInfo.getPrice()>0.0001f)
			productPriceTextView.setText("¥"+productInfo.getPrice()+"/斤");
		else
			productPriceTextView.setVisibility(View.GONE);
		
		if(productInfo.getProductBagPrice()>0.0001f)
			productBagTextView.setText("¥"+productInfo.getProductBagPrice()+"/袋");
		else 
			productBagTextView.setVisibility(View.GONE);
			
		aQuery=new AQuery(productPhotoImageView);
		aQuery.image(productInfo.getPhotoUrl(), false, true);
		convertView.setBackgroundResource(R.drawable.list_item_background);
		return convertView;
	}

}
