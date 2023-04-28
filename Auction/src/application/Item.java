/*
* EE422C Final Project submission by
* Replace <...> with your actual data.
* Roberto Reyes
* rcr2662
* 17360
* Spring 2022
*/

package application;

public class Item{
	String itemName;
	String itemDes;
	double minPrice;
	double currBid;
	double buyPrice;
	int sold;
	
	String image;

	Item(){
		itemName = "no item name";
		itemDes = "no item description";
		currBid = 1.0;
		minPrice = 1.0;
		buyPrice = 1.0;
		sold = 0;
		image = "no image";
	}
	
	Item(String itemName, String itemDes, double minPrice, double currBid, double buyPrice, int sold, String img){
		this.itemName = itemName;
		this.itemDes = itemDes;
		this.minPrice = minPrice;
		this.currBid = currBid;
		this.buyPrice = buyPrice;
		this.sold = sold;
		this.image = img;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String s) {
		itemName = s;
	}
	
	public String getItemDes() {
		return itemDes;
	}
	
	public void setItemDes(String s) {
		itemDes = s;
	}
	
	
	public double getMinPrice() {
		return minPrice;
	}
	
	public void setMinPrice(double p) {
		minPrice = p;
	}
	
	public double getCurrBid() {
		return currBid;
	}
	
	public void setCurrBid(double d) {
		currBid = d;
	}
	
	public double getBuyPrice() {
		return buyPrice;
	}
	
	public void setBuyPrice(double d) {
		buyPrice = d;
	}
	
	public int getSold() {
		return sold;
	}
	
	public void setSold(int d) {
		sold= d;
	}
	
	public String getImageString() {
		return image;
	}
	
	public void setImageString(String d) {
		image = d;
	}
	public String toString() {
		return itemName + " " + Double.toString(currBid) + " "+ Double.toString(buyPrice);
	}
}
