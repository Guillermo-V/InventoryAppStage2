package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.EditorActivity;
import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.android.inventoryapp.data.InventoryDbHelper;
import com.example.android.inventoryapp.data.InventoryDbHelper;

import java.util.function.Supplier;

public class CatalogActivity extends AppCompatActivity {

    /** Database helper that will provide us access to the database */
    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new InventoryDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the inventory database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String [] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_Inventory_NAME,
                InventoryEntry.COLUMN_Inventory_Price,
                InventoryEntry.COLUMN_Inventory_Quantity,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_NPHONE_NUMBER,
        };

        Cursor cursor = db.query(
                InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );


        try {

            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // inventory table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_inventory);
            displayView.setText("Number of rows in inventory database table: " + cursor.getCount());


            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_Price);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_Quantity);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NPHONE_NUMBER);


            while(cursor.moveToNext()){

                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                String currentQuantity = cursor.getString(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierNamPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);



                displayView.append ("\n" + currentID + " - "
                        + currentName + " - " + currentPrice+ " - " +currentQuantity + " - " + currentSupplierName + " - " + currentSupplierNamPhoneNumber);

            }

        } finally {

            cursor.close();
        }
    }


    private void insertInventory() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_Inventory_NAME, "Device");
        values.put(InventoryEntry.COLUMN_Inventory_Price, "123");
        values.put(InventoryEntry.COLUMN_Inventory_Quantity, 7);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, "supplier name");
        values.put(InventoryEntry.COLUMN_SUPPLIER_NPHONE_NUMBER, "123456789");

        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertInventory();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}