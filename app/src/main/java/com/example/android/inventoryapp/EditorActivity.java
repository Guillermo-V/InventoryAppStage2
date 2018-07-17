
package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.android.inventoryapp.data.InventoryDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mSupplierNameEditText;

    private EditText mSupplierPhoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mNameEditText = (EditText) findViewById(R.id.edit_inventory_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_inventory_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_inventory_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_inventory_supplier_name);
        mSupplierPhoneNumberEditText = (EditText) findViewById(R.id.edit_inventory_supplier_phone_number);
    }

    private void insertInventory() {

        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumber = mSupplierPhoneNumberEditText.getText().toString().trim();

        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_Inventory_NAME, nameString);
        values.put(InventoryEntry.COLUMN_Inventory_Price, priceString);
        values.put(InventoryEntry.COLUMN_Inventory_Quantity, quantity);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NPHONE_NUMBER, supplierPhoneNumber);


        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {

            Toast.makeText(this, "Error with saving Inventory", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Inventory saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:

                insertInventory();

                finish();
                return true;

            case R.id.action_delete:

                return true;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}