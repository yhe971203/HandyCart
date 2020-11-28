package kr.ac.konkuk.handycart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {    //제품 정보 데이터베이스 생성

    public static final String DATABASE_NAME = "Cart.db";
    public static final String PRODUCTS_TABLE_NAME = "products";
    public static final String PRODUCTS_COLUMN_ID = "id";
    public static final String PRODUCTS_COLUMN_NAME = "name";
    public static final String PRODUCTS_COLUMN_PRICE = "price";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table products " +
                        "(id integer primary key,name text, price text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    public boolean insertProduct(String name, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("price", price);

        db.insert("products", null, contentValues);
        return true;
    }

    public Cursor getData(int id) { //제품 정보 입력
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from products where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PRODUCTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateProduct(Integer id, String name, String price) {   //제품 정보 추가
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        db.update("movies", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteProduct(Integer id) {  //제품 정보 삭제
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("products",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList getAllProducts() { //입력된 제품 리스트
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from products", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME))+" / "+res.getString(res.getColumnIndex(PRODUCTS_COLUMN_PRICE)));
            res.moveToNext();
        }
        return array_list;
    }
}
