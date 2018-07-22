package com.example.android.inventoryapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.text.TextUtils;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {


    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        Button saleButton = (Button) view.findViewById(R.id.sale);




        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_Price);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_Quantity);
        String InventoryName = cursor.getString(nameColumnIndex);
        String InventoryPrice = cursor.getString(priceColumnIndex);
        final String InventoryQuantity = cursor.getString(quantityColumnIndex);
        final String productID = cursor.getString(nameColumnIndex);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CatalogActivity editor = (CatalogActivity) context;
                editor.productSaleCount(Integer.valueOf(productID), Integer.valueOf(InventoryQuantity));
            }
        });

        if (TextUtils.isEmpty(InventoryPrice)) {

            InventoryPrice = context.getString(R.string.unknown_price);

        }
        nameTextView.setText(InventoryName);
        summaryTextView.setText(InventoryPrice + " USD");
        quantityTextView.setText(InventoryQuantity + " available units");
    }
}