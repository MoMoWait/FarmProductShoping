/**
 * 
 */
package cn.edu.fjnu.shop.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author GaoFei
 *
 */
public class ProductInfo implements Parcelable {

	private int id;
	private String name;
	private String type;
	private String des;
	private float price;
	private String photoUrl;
	private float productBagPrice;
	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	
	public ProductInfo(){
		
	}
	
	
	public ProductInfo(int id, String name, String type, String des,
			float price, String photoUrl,float productBagPrice) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.des = des;
		this.price = price;
		this.photoUrl = photoUrl;
		this.productBagPrice = productBagPrice;
	}


	private ProductInfo(Parcel in){
		id=in.readInt();
		name=in.readString();
		type=in.readString();
		des=in.readString();
		price=in.readFloat();
		photoUrl=in.readString();
		productBagPrice=in.readFloat();
	}
	public static final  Parcelable.Creator<ProductInfo> CREATOR=new Parcelable.Creator<ProductInfo>() {

		@Override
		public ProductInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			//return new CategoryInfo(photoUrl, name, price);
			return new ProductInfo(source);
		}

		@Override
		public ProductInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ProductInfo[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(type);
		dest.writeString(des);
		dest.writeFloat(price);
		dest.writeString(photoUrl);
		dest.writeFloat(productBagPrice);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public float getProductBagPrice() {
		return productBagPrice;
	}
	
	public void setProductBagPrice(float productBagPrice) {
		this.productBagPrice = productBagPrice;
	}
	
	
}
