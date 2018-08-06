package fandradetecinfo.com.meupeso;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import fandradetecinfo.com.meupeso.Models.BalancaDigital;



public class PrefsHandler {

    Context context;
    SharedPreferences shp;

    public PrefsHandler(Context ctx) {
        this.context = ctx;
        String MINHAS_PREFS = "MEUPESO_PREFS";
        shp = context.getSharedPreferences(MINHAS_PREFS, 0);
    }

    public void carregar(View v, String usuario)
    {
        String u = usuario;
        ((EditText) v.findViewById(R.id.txtPeso)).setText(String.format("%.1f", shp.getFloat("peso"+u, 0)));
        ((EditText) v.findViewById(R.id.txtGordura)).setText(String.format("%.1f", shp.getFloat("gordura"+u, 0)));
        ((EditText) v.findViewById(R.id.txtHidratacao)).setText(String.format("%.1f", shp.getFloat("hidratacao"+u, 0)));
        ((EditText) v.findViewById(R.id.txtMusculo)).setText(String.format("%.1f", shp.getFloat("musculo"+u, 0)));
        ((EditText) v.findViewById(R.id.txtOsso)).setText(String.format("%.1f", shp.getFloat("osso"+u, 0)));
    }

    public void salvar(BalancaDigital balanca)
    {
        String u = balanca.getId_usuario();
        SharedPreferences.Editor editor = shp.edit();
        editor.putFloat("peso"+u, Float.parseFloat(balanca.getPeso()));
        editor.putFloat("gordura"+u, Float.parseFloat(balanca.getGordura()));
        editor.putFloat("hidratacao"+u, Float.parseFloat(balanca.getHidratacao()));
        editor.putFloat("musculo"+u, Float.parseFloat(balanca.getMusculo()));
        editor.putFloat("osso"+u, Float.parseFloat(balanca.getOsso()));
        editor.apply();
    }

    public boolean registroAnteriorIdentico(BalancaDigital balanca)
    {
        String u = balanca.getId_usuario();
        if (!balanca.getPeso().equals(String.format("%.1f", shp.getFloat("peso"+u, 0)).replace(',', '.')))
            return false;
        else if (!balanca.getGordura().equals(String.format("%.1f", shp.getFloat("gordura"+u, 0)).replace(',', '.')))
            return false;
        else if (!balanca.getHidratacao().equals(String.format("%.1f", shp.getFloat("hidratacao"+u, 0)).replace(',', '.')))
            return false;
        else if (!balanca.getMusculo().equals(String.format("%.1f", shp.getFloat("musculo"+u, 0)).replace(',', '.')))
            return false;
        else if (!balanca.getOsso().equals(String.format("%.1f", shp.getFloat("osso"+u, 0)).replace(',', '.')))
            return false;

        return true;
    }

}
