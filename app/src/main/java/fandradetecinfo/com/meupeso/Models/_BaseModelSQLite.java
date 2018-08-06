package fandradetecinfo.com.meupeso.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class _BaseModelSQLite {

    protected String table;

    public static final String DATABASE_NAME = "meupesodb";
    public static final int DATABASE_VERSION = 3;

    public static String TABLE_CREATE_1 = "CREATE TABLE IF NOT EXISTS balancadigital "
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_usuario INT, data DATETIME, peso DOUBLE, gordura DOUBLE, hidratacao DOUBLE, "
            + "musculo DOUBLE, osso DOUBLE)";

    public static String TABLE_CREATE_2 = "CREATE TABLE IF NOT EXISTS usuario "
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, sexo_masculino BOOLEAN, "
            + "altura DOUBLE, data_nascimento DATETIME)";

    protected DataBaseHelper dbhelper;
    protected Context context;
    protected SQLiteDatabase db;

    public _BaseModelSQLite(Context ctx) {
        this.context = ctx;
        dbhelper = new DataBaseHelper(context);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {
        //Create a helper object to create, open, and/or manage a database.
        public DataBaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        //Called when the database is created for the first time.
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_1);
            db.execSQL(TABLE_CREATE_2);
        }

        @Override
        //Called when the database needs to be upgraded.
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_2);
            onCreate(db);
        }
    }

    public _BaseModelSQLite open() {
        //Create and/or open a database that will be used for reading and writing.
        db = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        //Close any open database object.
        dbhelper.close();
    }

    //Insert record in the database

    public long insert(ContentValues content) {
        try {
            return db.insertOrThrow(table, null, content);
        } catch (Exception e) {
            throw e;
        }
    }

    public long deleteById(String Id) {
        try {
            String args[] = {Id};
            return db.delete(table, "id = ?", args);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean exists(String sql, String[] args) {
        boolean ret = false;
        try {
            Cursor c = buscarCursor(sql, args);
            c.moveToFirst();
            if (!c.getString(0).equals("0")) {
                ret = true;
            }
            c.close();
        } catch (Exception e) {
            throw e;
        }
        return ret;
    }

    protected Cursor buscarCursor(String sql, String[] args) {
        try {
            //db.execSQL("update usuario set id = 4 where id = 5");
            //db.execSQL("update balancadigital set id_usuario = 3 where id_usuario = 2 and peso < 20");

            return db.rawQuery(sql, args, null);
        } catch (Exception e) {
            throw e;
        }
    }

    protected Cursor buscarCursor(String sql) {
        try {
            return db.rawQuery(sql, null);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getDataDiaBr() {
//        GregorianCalendar calendario = new GregorianCalendar();
//        int dia = calendario.get(calendario.DAY_OF_MONTH);
//        int mes = calendario.get(calendario.MONTH) + 1;
//        int ano = calendario.get(calendario.YEAR);
//        String dataIguana = String.valueOf(dia + "/" + mes + "/" + ano);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String diaIguana = df.format(new Date());
        return diaIguana;
    }

    public String getDataFormatada(Date pData)
    {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String datFormatada = formato.format(pData);
            return datFormatada;
        } catch (Exception e) {
            throw e;
        }
    }

    public Date getDataTimestamp(String pData)
    {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            return formato.parse(pData);
        } catch (ParseException pe) {
            pe.printStackTrace();
            Date d = new Date("01/01/1900");
            return d;
        } catch (Exception e) {
            Log.d("logX", e.getMessage());
            throw e;
        }
    }
}
