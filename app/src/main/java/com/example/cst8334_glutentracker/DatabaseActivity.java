package com.example.cst8334_glutentracker;

public final class DatabaseActivity {

    private DatabaseActivity() {}

    public static class Products {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_ID = "productID";
        public static final String COLUMN_NAME_PRODUCT_NAME = "productName";
        public static final String COLUMN_NAME_DESCRIPTION = "productDescription";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_GLUTEN = "isGlutenFree";
    }

    public static class Receipts {
        public static final String TABLE_NAME = "receipts";
        public static final String COLUMN_NAME_ID = "receiptID";
        public static final String COLUMN_NAME_FILE = "receiptFile";
        public static final String COLUMN_NAME_TOTAL_DEDUCTION = "totalTaxDeduction";
        public static final String COLUMN_NAME_TOTAL_PRICE = "totalPrice";
        public static final String COLUMN_NAME_DATE = "date";
    }

    public static class ProductReceipt{
        public static final String TABLE_NAME = "productReceipt";
        public static final String COLUMN_NAME_PRODUCT_ID = "productID";
        public static final String COLUMN_NAME_RECEIPT_ID = "receiptID";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_DEDUCTION = "deduction";
        public static final String COLUMN_NAME_LINKED_PRODUCT_ID = "linkedProductID";
        public static final String COLUMN_NAME_LINKED_PRODUCT_PRICE = "linkedProductPrice";

    }

    //public static class Users {

    //}





}