package fandradetecinfo.com.meupeso.Models;

import android.content.Context;
import android.util.Log;

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
