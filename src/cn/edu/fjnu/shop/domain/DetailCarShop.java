package cn.edu.fjnu.shop.domain;

/**
 * @author GaoFei
 * 
 */
public class DetailCarShop {

	private int userID;
	private int productID;
	private int productNumber;
	private String productName;
	private String productDes;
	private float productPrice;
	private String productPhoto;
	/**商品单位*/
	private String productUnit;
	/**商品单价(以袋为单位)*/
	private float productBagPrice;
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public int getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(int productNumber) {
		this.productNumber = productNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDes() {
		return productDes;
	}
	public void setProductDes(String productDes) {
		this.productDes = productDes;
	}
	public float getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(float productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductPhoto() {
		return productPhoto;
	}
	public void setProductPhoto(String productPhoto) {
		this.productPhoto = productPhoto;
	}
	
	public String getProductUnit() {
		return productUnit;
	}
	
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
	
	public float getProductBagPrice() {
		return productBagPrice;
	}
	
	public void setProductBagPrice(float productBagPrice) {
		this.productBagPrice = productBagPrice;
	}
	
}
