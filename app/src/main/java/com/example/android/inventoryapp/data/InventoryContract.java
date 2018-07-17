package com.example.android.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORYAPP = "inventoryapp";

    public InventoryContract () {}

    public static final class InventoryEntry implements BaseColumns{


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORYAPP);
        public final static String TABLE_NAME = "inventory";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_Inventory_NAME = "name";
        public final static String COLUMN_Inventory_Price = "price";
        public final static String COLUMN_Inventory_Quantity = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier";
        public final static String COLUMN_SUPPLIER_NPHONE_NUMBER = "phone";

        public static final int quantity = 0;

    }
}
