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
public class CategoryInfo implements Parcelable{

	private String photoUrl;
	private String name;
	private String price;
	public CategoryInfo(){
		
	}
	public CategoryInfo(Parcel in){
		photoUrl=in.readString();
		name=in.readString();
		price=in.readString();
	}
	
	public CategoryInfo(String photoUrl, String name, String price) {
		super();
		this.photoUrl = photoUrl;
		this.name = name;
		this.price = price;
	}


	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(photoUrl);
		dest.writeString(name);
		dest.writeString(price);
	}
	
	public static final  Parcelable.Creator<CategoryInfo> CREATOR=new Parcelable.Creator<CategoryInfo>() {

		@Override
		public CategoryInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			//return new CategoryInfo(photoUrl, name, price);
			return new CategoryInfo(source);
		}

		@Override
		public CategoryInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new CategoryInfo[size];
		}
	};
}
