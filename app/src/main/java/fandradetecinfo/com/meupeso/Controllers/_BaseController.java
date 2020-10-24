package fandradetecinfo.com.meupeso.Controllers;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Hashtable;
import java.util.Map;
import fandradetecinfo.com.meupeso.R;

public class _BaseController {

    protected Activity activity;
    protected String TAG;
    protected Map<Integer, String> mapSexo;

    public _BaseController() {
        mapSexo = new Hashtable<>();
        mapSexo.put(1, "M");
        mapSexo.put(2, "F");
    }

    protected boolean validarCampo(EditText edt) {
        if (edt.getText().toString().length() == 0) {
            Toast.makeText(activity, edt.getHint().toString() + " em branco",
                    Toast.LENGTH_LONG).show();
            edt.setError(edt.getHint().toString() + " em branco");
            return false;
        }
        return true;
    }

    protected boolean validarLista(Spinner spn, String nome, int minSeletion) {
        if(spn.getSelectedItemPosition() < minSeletion)
        {
            Toast.makeText(activity, nome + " em branco", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    protected void montarAlerta(String titulo, String mensagem)
    {
        AlertDialog.Builder builderAlerta = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        builderAlerta.setTitle(titulo);
        builderAlerta.setMessage(mensagem);
        builderAlerta.setIcon(R.drawable.heart_monitor_48px);

        builderAlerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("LogX","Clicou no Ok!");
            }
        });

        AlertDialog meuAlerta = builderAlerta.create();
        meuAlerta.show();
    }

    protected Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

}
