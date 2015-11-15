package nikhiltyagi.shopapp;

/**
 * Created by Nikhil Tyagi on 05-11-2015.
 */
public class ShopItem {
    private String itemName,itemDesc;
    private float itemPrice;

    public ShopItem(String itemName,String itemDesc,float itemPrice){
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public float getItemPrice() {
        return itemPrice;
    }
}
