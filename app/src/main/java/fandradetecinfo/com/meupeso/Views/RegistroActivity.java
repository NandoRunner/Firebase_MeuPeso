package fandradetecinfo.com.meupeso.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fandradetecinfo.com.meupeso.Controllers.BalancaDigitalController;
import fandradetecinfo.com.meupeso.MainActivity;
import fandradetecinfo.com.meupeso.PrefsHandler;
import fandradetecinfo.com.meupeso.R;

public class RegistroActivity extends _BaseActivity
{
    PrefsHandler prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBalancaDigital);
        setSupportActionBar(toolbar);

        myEdit = (EditText) findViewById(R.id.txtData);

        Context ctx = getBaseContext();
        prefs = new PrefsHandler(ctx);

        mySpinner = (Spinner) findViewById(R.id.spinnerUsuario);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Usuario")
                .orderBy("nome", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            List<String> spinnerArray = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                spinnerArray.add(String.valueOf(document.get("nome")));
                            }
                            tratarSpinner(spinnerArray);
                        } else {
                            Log.d("Erro", "Error getting documents: ", task.getException());
                        }
                    }
                });

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                MainActivity.usuario = (String)parent.getAdapter().getItem(position);
                prefs.carregar(findViewById(android.R.id.content), MainActivity.usuario);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });

        EditText myTxt = (EditText) findViewById(R.id.txtData);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        myTxt.setText(df.format("dd/MM/yyyy", new Date()));
        myTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v1, boolean hasFocus) {
                tratarData(hasFocus);
            }
        });


        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.back_w_48px);

            toolbar.setTitle("Registro Novo");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.posFragment = 1;
                    NavUtils.navigateUpFromSameTask(RegistroActivity.this);
                }
            });
            toolbar.inflateMenu(R.menu.menu_registro);

        }

        EditText myTxtFocus = (EditText) findViewById(R.id.txtPeso);

        myTxtFocus.requestFocus();
    }

    private void tratarAdicionarUsuario()
    {
        Intent objIntent = new Intent(this, UsuarioActivity.class);
        startActivity(objIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_registro, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                MainActivity.posFragment = 1;
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.salvar_registro:
                if (gravarRegistro()) {
                    MainActivity.posFragment = 1;
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean gravarRegistro()
    {
        try
        {
            BalancaDigitalController.getInstance().init(this);

            if(!BalancaDigitalController.getInstance().validarDados()) return false;

            BalancaDigitalController.getInstance().pegarDoFormulario();

            if(BalancaDigitalController.getInstance().registroExistente()) {
                BalancaDigitalController.getInstance().alertarRegistroExistente();
                return false;
            }

            if(prefs.registroAnteriorIdentico(BalancaDigitalController.getInstance().getModel())) {
                BalancaDigitalController.getInstance().alertarRegistroAnteriorIdentico();
                return false;
            }

            BalancaDigitalController.getInstance().inserir();

            prefs.salvar(BalancaDigitalController.getInstance().getModel());


            Log.i("LogX", "Registro gravado!");
            return true;
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("LogX", e.getMessage());
            return false;
        }
    }

}
