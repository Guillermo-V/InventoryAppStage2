
package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;
import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

public class SeeProductDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_Inventory_LOADER = 0;

    private Uri mCurrentInventoryUri;

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mSupplierNameEditText;

    private EditText mSupplierPhoneNumberEditText;

    private boolean mInventoryHasChanged = false;

    int idColumnIndex;

    int quantity;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override


        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventoryHasChanged = true;
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();
        if (mCurrentInventoryUri == null) {
            setTitle("Edit");
            invalidateOptionsMenu();

        } else {
            setTitle("Edit");
            getLoaderManager().initLoader(EXISTING_Inventory_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_inventory_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_inventory_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_inventory_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_inventory_supplier_name);
        mSupplierPhoneNumberEditText = (EditText) findViewById(R.id.edit_inventory_supplier_phone_number);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentInventoryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void saveInventory() {

        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumber = mSupplierPhoneNumberEditText.getText().toString().trim();

        if ((mCurrentInventoryUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supplierName) && TextUtils.isEmpty(supplierPhoneNumber)) | (mCurrentInventoryUri == null | TextUtils.isEmpty(nameString) | TextUtils.isEmpty(priceString) | TextUtils.isEmpty(quantityString) | TextUtils.isEmpty(supplierName) | TextUtils.isEmpty(supplierPhoneNumber))) {
            Toast.makeText(this, "Error updating a product.Please type something in every field",
                    Toast.LENGTH_SHORT).show();

        } else {
            ContentValues values = new ContentValues();
            values.put(InventoryEntry.COLUMN_Inventory_NAME, nameString);
            values.put(InventoryEntry.COLUMN_Inventory_Price, priceString);
            values.put(InventoryEntry.COLUMN_Inventory_Quantity, quantityString);
            values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierName);
            values.put(InventoryEntry.COLUMN_SUPPLIER_NPHONE_NUMBER, supplierPhoneNumber);


            if (mCurrentInventoryUri == null) {
                Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_inventory_failed),
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_inventory_successful),
                            Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_inventory_successful),
                        Toast.LENGTH_SHORT).show();

                int rowsAffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.editor_update_inventory_failed),
                            Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(this, getString(R.string.editor_update_inventory_successful),
                            Toast.LENGTH_SHORT).show();

                }
            }
            finish();
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

                saveInventory();


                return true;

            case R.id.action_delete:


                showDeleteConfirmationDialog();

                return true;

            case android.R.id.home:

                if (!mInventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(SeeProductDetailsActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(SeeProductDetailsActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mInventoryHasChanged) {
            super.onBackPressed();
            return;

        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override


                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();

                    }

                };

        showUnsavedChangesDialog(discardButtonClickListener);

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                deleteInventory();
            }

        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {


                if (dialog != null) {

                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteInventory() {
        if (mCurrentInventoryUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_inventory_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_Inventory_NAME,
                InventoryEntry.COLUMN_Inventory_Price,
                InventoryEntry.COLUMN_Inventory_Quantity,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_NPHONE_NUMBER};
        return new CursorLoader(this,
                mCurrentInventoryUri, projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_Inventory_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_Price);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_Inventory_Quantity);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NPHONE_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierColumnIndex);
            final String supplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);
            mNameEditText.setText(name);
            mPriceEditText.setText(price);
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneNumberEditText.setText(supplierPhoneNumber);

            Button inventoryDecreaseButton = (Button) findViewById(R.id.decrease);
            inventoryDecreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseCount(idColumnIndex, quantity);
                    quantity = quantity - 1;
                }
            });

            Button inventoryIncreaseButton = (Button) findViewById(R.id.increase);
            inventoryIncreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseCount(idColumnIndex, quantity);
                    quantity = quantity + 1;
                }
            });

            Button inventoryDeleteButton = (Button) findViewById(R.id.delete);
            inventoryDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();
                }
            });

            Button phoneButton = (Button) findViewById(R.id.phone);
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = String.valueOf(supplierPhoneNumber);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText(Integer.toString(0));
        mSupplierNameEditText.setText("");
        mSupplierPhoneNumberEditText.setText("");
    }

    private void updateInventory(int productQuantity) {
        Log.d("message", "updateProduct at ViewActivity");
        EditText quantityText = (EditText) findViewById(R.id.edit_inventory_quantity);
        quantityText.setText(String.valueOf(productQuantity));

    }

    public void decreaseCount(int inventoryID, int inventoryQuantity) {
        inventoryQuantity = inventoryQuantity - 1;
        if (inventoryQuantity >= 0) {
            updateInventory(inventoryQuantity);
            Toast.makeText(this, "quantity changed", Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - InventoryID " + inventoryID + " - quantity " + inventoryQuantity + " , decreaseCount has been called.");
        } else {
            Toast.makeText(this, "We do not have this product on stock", Toast.LENGTH_SHORT).show();
        }
    }

    public void increaseCount(int InventoryID, int inventoryQuantity) {
        inventoryQuantity = inventoryQuantity + 1;
        if (inventoryQuantity >= 0) {

            updateInventory(inventoryQuantity);
            Toast.makeText(this, "quantity changed", Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - productID " + InventoryID + " - quantity " + inventoryQuantity + " , decreaseCount has been called.");
        }
    }
}