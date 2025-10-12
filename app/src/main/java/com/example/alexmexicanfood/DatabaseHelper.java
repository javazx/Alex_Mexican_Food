package com.example.alexmexicanfood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mexican_food.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_PRODUCTOS = "productos";
    private static final String TABLE_SALES = "sales";
    private static final String TABLE_SALE_ITEMS = "sale_items";

    // Common column names
    private static final String KEY_ID = "id";

    // PRODUCTOS Table - column names
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_DESCRIPCION = "descripcion";
    private static final String KEY_PRECIO = "precio";
    private static final String KEY_RUTA_IMAGEN = "ruta_imagen";

    // SALES Table - column names
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_TOTAL_PRICE = "total_price";

    // SALE_ITEMS Table - column names
    private static final String KEY_SALE_ID = "sale_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_ITEM_PRICE = "item_price";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTOS_TABLE = "CREATE TABLE " + TABLE_PRODUCTOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOMBRE + " TEXT,"
                + KEY_DESCRIPCION + " TEXT,"
                + KEY_PRECIO + " REAL,"
                + KEY_RUTA_IMAGEN + " TEXT" + ")";

        String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_SALES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIMESTAMP + " INTEGER,"
                + KEY_TOTAL_PRICE + " REAL" + ")";

        String CREATE_SALE_ITEMS_TABLE = "CREATE TABLE " + TABLE_SALE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SALE_ID + " INTEGER,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_ITEM_PRICE + " REAL," + "FOREIGN KEY(" + KEY_SALE_ID + ") REFERENCES " + TABLE_SALES + "(" + KEY_ID + "))";

        db.execSQL(CREATE_PRODUCTOS_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
        db.execSQL(CREATE_SALE_ITEMS_TABLE);

        addInitialData(db);
    }

    private void addInitialData(SQLiteDatabase db) {
        addProduct(db, new Producto(0, "Tacos al Pastor", "Deliciosos tacos con carne de cerdo marinada.", 15.00, ""));
        addProduct(db, new Producto(0, "Guacamole y Totopos", "Aguacate fresco machacado con cilantro, cebolla y chile.", 50.00, ""));
        addProduct(db, new Producto(0, "Enchiladas Suizas", "Tortillas de maíz rellenas de pollo, bañadas en salsa verde y queso gratinado.", 85.50, ""));
        addProduct(db, new Producto(0, "Agua de Horchata", "Bebida refrescante de arroz con canela.", 20.00, ""));
    }

    // Overloaded method to work within onCreate transaction
    public void addProduct(SQLiteDatabase db, Producto producto) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, producto.getNombre());
        values.put(KEY_DESCRIPCION, producto.getDescripcion());
        values.put(KEY_PRECIO, producto.getPrecio());
        values.put(KEY_RUTA_IMAGEN, producto.getRutaImagen());
        db.insert(TABLE_PRODUCTOS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        onCreate(db);
    }

    // Add new Sale
    public void addSale(List<Producto> items, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues saleValues = new ContentValues();
            saleValues.put(KEY_TIMESTAMP, System.currentTimeMillis());
            saleValues.put(KEY_TOTAL_PRICE, totalPrice);

            long saleId = db.insert(TABLE_SALES, null, saleValues);

            for (Producto item : items) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(KEY_SALE_ID, saleId);
                itemValues.put(KEY_PRODUCT_NAME, item.getNombre());
                itemValues.put(KEY_ITEM_PRICE, item.getPrecio());
                db.insert(TABLE_SALE_ITEMS, null, itemValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Get all sales
    public List<Sale> getAllSales() {
        List<Sale> salesList = new ArrayList<>();
        String selectSalesQuery = "SELECT * FROM " + TABLE_SALES + " ORDER BY " + KEY_TIMESTAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor salesCursor = db.rawQuery(selectSalesQuery, null);

        if (salesCursor.moveToFirst()) {
            do {
                Sale sale = new Sale();
                sale.setId(salesCursor.getLong(salesCursor.getColumnIndexOrThrow(KEY_ID)));
                sale.setTimestamp(salesCursor.getLong(salesCursor.getColumnIndexOrThrow(KEY_TIMESTAMP)));
                sale.setTotalPrice(salesCursor.getDouble(salesCursor.getColumnIndexOrThrow(KEY_TOTAL_PRICE)));

                List<SaleItem> saleItems = new ArrayList<>();
                String selectItemsQuery = "SELECT * FROM " + TABLE_SALE_ITEMS + " WHERE " + KEY_SALE_ID + " = ?";
                Cursor itemsCursor = db.rawQuery(selectItemsQuery, new String[]{String.valueOf(sale.getId())});

                if (itemsCursor.moveToFirst()) {
                    do {
                        SaleItem item = new SaleItem();
                        item.setId(itemsCursor.getLong(itemsCursor.getColumnIndexOrThrow(KEY_ID)));
                        item.setSaleId(itemsCursor.getLong(itemsCursor.getColumnIndexOrThrow(KEY_SALE_ID)));
                        item.setProductName(itemsCursor.getString(itemsCursor.getColumnIndexOrThrow(KEY_PRODUCT_NAME)));
                        item.setPrice(itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow(KEY_ITEM_PRICE)));
                        saleItems.add(item);
                    } while (itemsCursor.moveToNext());
                }
                itemsCursor.close();
                sale.setItems(saleItems);
                salesList.add(sale);
            } while (salesCursor.moveToNext());
        }
        salesCursor.close();
        db.close();
        return salesList;
    }

    // Get a single sale by ID
    public Sale getSaleById(long saleId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSaleQuery = "SELECT * FROM " + TABLE_SALES + " WHERE " + KEY_ID + " = ?";
        Cursor salesCursor = db.rawQuery(selectSaleQuery, new String[]{String.valueOf(saleId)});

        Sale sale = null;
        if (salesCursor.moveToFirst()) {
            sale = new Sale();
            sale.setId(salesCursor.getLong(salesCursor.getColumnIndexOrThrow(KEY_ID)));
            sale.setTimestamp(salesCursor.getLong(salesCursor.getColumnIndexOrThrow(KEY_TIMESTAMP)));
            sale.setTotalPrice(salesCursor.getDouble(salesCursor.getColumnIndexOrThrow(KEY_TOTAL_PRICE)));

            List<SaleItem> saleItems = new ArrayList<>();
            String selectItemsQuery = "SELECT * FROM " + TABLE_SALE_ITEMS + " WHERE " + KEY_SALE_ID + " = ?";
            Cursor itemsCursor = db.rawQuery(selectItemsQuery, new String[]{String.valueOf(sale.getId())});

            if (itemsCursor.moveToFirst()) {
                do {
                    SaleItem item = new SaleItem();
                    item.setId(itemsCursor.getLong(itemsCursor.getColumnIndexOrThrow(KEY_ID)));
                    item.setSaleId(itemsCursor.getLong(itemsCursor.getColumnIndexOrThrow(KEY_SALE_ID)));
                    item.setProductName(itemsCursor.getString(itemsCursor.getColumnIndexOrThrow(KEY_PRODUCT_NAME)));
                    item.setPrice(itemsCursor.getDouble(itemsCursor.getColumnIndexOrThrow(KEY_ITEM_PRICE)));
                    saleItems.add(item);
                } while (itemsCursor.moveToNext());
            }
            itemsCursor.close();
            sale.setItems(saleItems);
        }
        salesCursor.close();
        db.close();
        return sale;
    }


    // Add new Product
    public void addProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, producto.getNombre());
        values.put(KEY_DESCRIPCION, producto.getDescripcion());
        values.put(KEY_PRECIO, producto.getPrecio());
        values.put(KEY_RUTA_IMAGEN, producto.getRutaImagen());

        db.insert(TABLE_PRODUCTOS, null, values);
        db.close();
    }

    // Get all products
    public List<Producto> getAllProductos() {
        List<Producto> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setId(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)));
                producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE)));
                producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPCION)));
                producto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRECIO)));
                producto.setRutaImagen(cursor.getString(cursor.getColumnIndexOrThrow(KEY_RUTA_IMAGEN)));
                productList.add(producto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    // Update a product
    public int updateProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, producto.getNombre());
        values.put(KEY_DESCRIPCION, producto.getDescripcion());
        values.put(KEY_PRECIO, producto.getPrecio());
        values.put(KEY_RUTA_IMAGEN, producto.getRutaImagen());

        return db.update(TABLE_PRODUCTOS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(producto.getId())});
    }

    // Delete a product
    public void deleteProducto(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTOS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
