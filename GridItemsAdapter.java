package nikhiltyagi.shopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nikhil Tyagi on 05-11-2015.
 */
public class GridItemsAdapter extends ArrayAdapter<ShopItem> {

    private Context context;
    private List<ShopItem> allItems;

    public GridItemsAdapter(Context context, int resource, List<ShopItem> objects) {
        super(context,resource,objects);
        this.context = context;
        this.allItems = objects;
    }

    public List<ShopItem> getAllItems(){
        return allItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View shoppingGridView = LayoutInflater.from(context).inflate(R.layout.grid_shopping_items, null);
        TextView itemName = (TextView)shoppingGridView.findViewById(R.id.itemName);
        TextView itemPrice = (TextView)shoppingGridView.findViewById(R.id.itemPrice);
        itemName.setText(allItems.get(position).getItemName());
        itemPrice.setText("Rs. "+String.valueOf(allItems.get(position).getItemPrice()));
        shoppingGridView.setLayoutParams(new GridView.LayoutParams(250,300));
        return shoppingGridView;
    }
}
