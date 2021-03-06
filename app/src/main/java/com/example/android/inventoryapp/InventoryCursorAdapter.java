package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.TextUtils;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    int idColumnIndex;

    int quantity;

    public static final String TAG = InventoryCursorAdapter.class.getSimpleName();
    TextView quantityTextView;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.price);
        quantityTextView = (TextView) view.findViewById(R.id.quantity);

        idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);

        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_Price);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_Quantity);
        final int id = cursor.getInt(cursor.getColumnIndex(InventoryEntry._ID));
        String InventoryName = cursor.getString(nameColumnIndex);
        String InventoryPrice = cursor.getString(priceColumnIndex);
        final String InventoryQuantity = cursor.getString(quantityColumnIndex);
        quantity = cursor.getInt(quantityColumnIndex);
        final String InventoryID = cursor.getString(idColumnIndex);
        if (TextUtils.isEmpty(InventoryPrice)) {

            InventoryPrice = context.getString(R.string.unknown_price);

        }

        Button saleButton = (Button) view.findViewById(R.id.sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogActivity catalogActivity = (CatalogActivity) context;
                catalogActivity.sellProduct(Integer.valueOf(InventoryID), Integer.valueOf(InventoryQuantity));

                Log.v(TAG, "Item with id: " + id + " has been clicked");
            }
        });

        LinearLayout llListItemContainer = (LinearLayout) view.findViewById(R.id.llListItemContainer);
        llListItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SeeProductDetailsActivity.class);

                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

                intent.setData(currentInventoryUri);

                context.startActivity(intent);
            }
        });

        nameTextView.setText(InventoryName);
        summaryTextView.setText(InventoryPrice + " USD");
        quantityTextView.setText(InventoryQuantity + " available units");

    }


    private void updateInventory(int productQuantity, String text) {
        Log.d("message", "updateProduct at ViewActivity");


        quantityTextView.setText(String.valueOf(productQuantity) + "" + " available units");

    }

    public void decreaseCount(int inventoryID, int inventoryQuantity) {
        inventoryQuantity = inventoryQuantity - 1;
        if (inventoryQuantity >= 0) {
            updateInventory(inventoryQuantity, "");


            Log.d("Log msg", " - InventoryID " + inventoryID + " - quantity " + inventoryQuantity + " , decreaseCount has been called.");
        } else {
            Log.d("Log msg", " - InventoryID " + inventoryID + " - quantity " + inventoryQuantity + " We do not have this product");

            quantityTextView.setText("We do not have this product, sale button has not processed any sale");
        }
    }


}