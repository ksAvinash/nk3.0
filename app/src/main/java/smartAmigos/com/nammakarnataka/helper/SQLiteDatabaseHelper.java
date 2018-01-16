package smartAmigos.com.nammakarnataka.helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by avinashk on 07/01/18.
 */

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nk_database";
    private static final String TABLE_PLACES = "nk_places";
    private static final String TABLE_FAVOURITE = "nk_fav";
    private static final String TABLE_VISITED = "nk_visited";
    private static final String TABLE_CONFIGURATION = "nk_configuration";

    private static final String PLACE_ID = "id";
    private static final String PLACE_NAME = "name";
    private static final String PLACE_DESCRIPTION = "description";
    private static final String PLACE_DISTRICT = "district";
    private static final String PLACE_BESTSEASON = "bestSeason";
    private static final String PLACE_ADDITIONALINFO = "additionalInformation";
    private static final String PLACE_LATITUDE = "latitude";
    private static final String PLACE_LONGITUDE = "longitude";
    private static final String PLACE_CATEGORY = "category";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_place_table =
                "create table "+TABLE_PLACES+" ("
                        +PLACE_ID+" integer primary key, "
                        +PLACE_NAME+"  text, "
                        +PLACE_DESCRIPTION+" text, "
                        +PLACE_DISTRICT+" text, "
                        +PLACE_BESTSEASON+" text, "
                        +PLACE_ADDITIONALINFO+" text, "
                        +PLACE_LATITUDE+" double, "
                        +PLACE_LONGITUDE+" double, "
                        +PLACE_CATEGORY+" text );";
        db.execSQL(create_place_table);

        String create_favourite_table = "create table "
                +TABLE_FAVOURITE+" ("+PLACE_ID+" integer primary key);";
        db.execSQL(create_favourite_table);

        String create_visited_table = "create table "
                +TABLE_VISITED+" ("+PLACE_ID+" integer primary key);";
        db.execSQL(create_visited_table);

//        String create_configuration_table = "create table configuration "

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+TABLE_PLACES);
        db.execSQL("drop table if exists "+TABLE_FAVOURITE);
        db.execSQL("drop table if exists "+TABLE_VISITED);

        onCreate(db);
    }

    public void deleteTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,0,1);
    }


    public boolean insertIntoPlace(int id, String name, String description,
                                   String district, String bestseason,
                                   String additionalInfo,
                                   double latitude,
                                   double longitude,
                                   String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_ID, id);
        contentValues.put(PLACE_NAME, name);
        contentValues.put(PLACE_DESCRIPTION, description);
        contentValues.put(PLACE_DISTRICT, district);
        contentValues.put(PLACE_BESTSEASON, bestseason);
        contentValues.put(PLACE_ADDITIONALINFO, additionalInfo);
        contentValues.put(PLACE_LATITUDE, latitude);
        contentValues.put(PLACE_LONGITUDE, longitude);
        contentValues.put(PLACE_CATEGORY, category);

        db.insert(TABLE_PLACES, null, contentValues);
        return true;
    }

    public boolean insertIntoFavourites(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_ID, id);
        db.insert(TABLE_FAVOURITE, null, contentValues);

        return true;
    }

    public boolean insertIntoVisited(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_ID, id);
        db.insert(TABLE_VISITED, null, contentValues);

        return true;
    }


    public Cursor getAllPlacesByCategory(String category){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = '"+category +"';",null);
    }

}
