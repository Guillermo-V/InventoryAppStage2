package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public final class InventoryContract {

    public InventoryContract () {}

    public static final class InventoryEntry implements BaseColumns{

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
