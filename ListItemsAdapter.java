package nikhiltyagi.shopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nikhil Tyagi on 05-11-2015.
 */
public class ListItemsAdapter extends ArrayAdapter<ShopItem> {
    private Context context;
    private List<ShopItem> allItems;
    public ListItemsAdapter(Context context, int resource, List<ShopItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.allItems = objects;
    }

    public List<ShopItem> getAllItems(){
        return allItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View shoppingListView = LayoutInflater.from(context).inflate(R.layout.list_shopping_items, null);
        TextView itemName = (TextView)shoppingListView.findViewById(R.id.itemName);
        TextView itemDesc = (TextView)shoppingListView.findViewById(R.id.itemDesc);
        TextView itemPrice = (TextView)shoppingListView.findViewById(R.id.itemPrice);
        itemName.setText(allItems.get(position).getItemName());
        itemDesc.setText(allItems.get(position).getItemDesc());
        itemPrice.setText("Rs. "+String.valueOf(allItems.get(position).getItemPrice()));
        return shoppingListView;
    }
}
