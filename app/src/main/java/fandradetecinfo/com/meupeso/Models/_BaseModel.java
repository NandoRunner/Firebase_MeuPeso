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

public class _BaseModel {

    protected Context context;

    public _BaseModel()
    {

    }

    public _BaseModel(Context ctx) {
        this.context = ctx;
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
