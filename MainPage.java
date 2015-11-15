package nikhiltyagi.shopapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class MainPage extends AppCompatActivity {

    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {}

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastInScreenItem = firstVisibleItem + visibleItemCount;
            if (lastInScreenItem == totalItemCount) {
                try {
                    (new GetItems(view)).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        ArrayList<ShopItem> shoppingItems = new ArrayList<>();
        setupItemsList(shoppingItems);
        setupItemsGrid(shoppingItems);

    }

    private void setupItemsGrid(ArrayList<ShopItem> shoppingItems){
        GridView itemsGrid = (GridView) findViewById(R.id.shopItemsGrid);
        itemsGrid.setAdapter(new GridItemsAdapter(this, R.id.shopItemsGrid, shoppingItems));
        //itemsGrid.setOnScrollListener(scrollListener);
    }

    private void setupItemsList(ArrayList<ShopItem> shoppingItems){
        ListView itemsList = (ListView)findViewById(R.id.shopItemsList);
        itemsList.setAdapter(new ListItemsAdapter(this, R.id.shopItemsList, shoppingItems));
        itemsList.setOnScrollListener(scrollListener);
    }

    public void changeView(View v){
        ImageButton imageButton = (ImageButton)v;
        ListView itemsList = (ListView)findViewById(R.id.shopItemsList);
        GridView itemsGrid = (GridView) findViewById(R.id.shopItemsGrid);
        if(itemsGrid.getVisibility()==View.VISIBLE) {
            itemsGrid.setVisibility(View.GONE);
            itemsList.setVisibility(View.VISIBLE);
            imageButton.setImageResource(R.drawable.big_grid);
        }
        else{
            itemsGrid.setVisibility(View.VISIBLE);
            itemsList.setVisibility(View.GONE);
            imageButton.setImageResource(R.drawable.list_image);

        }
    }

    public void sort(View v){
        final ListView itemsList = (ListView)findViewById(R.id.shopItemsList);
        final GridView itemsGrid = (GridView) findViewById(R.id.shopItemsGrid);
        AlertDialog.Builder sortDialog = new AlertDialog.Builder(this);
        sortDialog.setTitle("Select Sort Type");
        sortDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ArrayAdapter<String> dialogOptions = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice);
        dialogOptions.add("Name A-Z");
        dialogOptions.add("Name Z-A");
        dialogOptions.add("Cost Low to High");
        dialogOptions.add("Cost High to Low");
        sortDialog.setAdapter(dialogOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Comparator<ShopItem> sortPreference = null;
                switch (which) {
                    case 0:
                        sortPreference = new Comparator<ShopItem>() {
                            @Override
                            public int compare(ShopItem lhs, ShopItem rhs) {
                                return lhs.getItemName().compareTo(rhs.getItemName());
                            }
                        };
                        break;
                    case 1:
                        sortPreference = new Comparator<ShopItem>() {
                            @Override
                            public int compare(ShopItem lhs, ShopItem rhs) {
                                return rhs.getItemName().compareTo(lhs.getItemName());
                            }
                        };
                        break;
                    case 2:
                        sortPreference = new Comparator<ShopItem>() {
                            @Override
                            public int compare(ShopItem lhs, ShopItem rhs) {
                                return Float.compare(lhs.getItemPrice(), rhs.getItemPrice());
                            }
                        };
                        break;
                    case 3:
                        sortPreference = new Comparator<ShopItem>() {
                            @Override
                            public int compare(ShopItem lhs, ShopItem rhs) {
                                return Float.compare(rhs.getItemPrice(), lhs.getItemPrice());
                            }
                        };
                        break;
                }
                ((ListItemsAdapter) itemsList.getAdapter()).sort(sortPreference);
                ((ListItemsAdapter) itemsList.getAdapter()).notifyDataSetChanged();
                ((GridItemsAdapter) itemsGrid.getAdapter()).sort(sortPreference);
                ((ListItemsAdapter) itemsList.getAdapter()).notifyDataSetChanged();
            }
        });

        sortDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar,menu);
        return true;
    }

    private class GetItems extends AsyncTask<HashMap<String,String>,Void,ArrayList<ShopItem>>{

        private ArrayList<ShopItem> shoppingItems;
        private AbsListView view;
        private int originalItemsCount;

        public GetItems(ArrayList<ShopItem> shoppingItems){
            this.shoppingItems = shoppingItems;
            this.originalItemsCount = shoppingItems.size();
        }

        public GetItems(AbsListView view){
            this.view = view;
            ArrayAdapter<ShopItem> viewAdapter = (ArrayAdapter < ShopItem >)view.getAdapter();
            this.originalItemsCount = viewAdapter.getCount();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<ShopItem> doInBackground(HashMap<String, String>... params) {
            NetworkController getItems = new NetworkController("http://192.168.0.2/learning/GetItems.php");
            HashMap<String,String> parameters = new HashMap<String,String>();
            parameters.put("itemsLength", String.valueOf(originalItemsCount));
            ArrayList<ShopItem> shoppingItems = new ArrayList<>();
            try {
                JSONArray result = getItems.getJSONArrayFromUrl(parameters);
                for(int i=0;i<result.length();i++){
                    JSONObject item = result.getJSONObject(i);
                    shoppingItems.add(new ShopItem(item.getString("name"),item.getString("description"),Float.parseFloat(item.getString("price"))));
                }
                return shoppingItems;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ShopItem> shoppingItems) {
            if(this.shoppingItems!=null)
                this.shoppingItems.addAll(shoppingItems);

            if(view!=null){
                ArrayAdapter<ShopItem> viewAdapter = (ArrayAdapter < ShopItem >)view.getAdapter();
                viewAdapter.addAll(shoppingItems);
                viewAdapter.notifyDataSetChanged();
                if(shoppingItems.size()==0)
                    view.setOnScrollListener(null);
            }
        }
    }
}
