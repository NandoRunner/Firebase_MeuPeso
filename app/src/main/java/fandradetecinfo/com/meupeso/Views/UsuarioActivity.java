package fandradetecinfo.com.meupeso.Views;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fandradetecinfo.com.meupeso.Controllers.UsuarioController;
import fandradetecinfo.com.meupeso.MainActivity;
import fandradetecinfo.com.meupeso.R;

public class UsuarioActivity extends _BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUsuario);
        setSupportActionBar(toolbar);

        myEdit = (EditText) findViewById(R.id.txtUsrData);

        myEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v1, boolean hasFocus) {
                tratarData(hasFocus);
            }
        });

        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.back_w_48px);

            toolbar.setTitle("Usuário Novo");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.posFragment = 0;
                    NavUtils.navigateUpFromSameTask(UsuarioActivity.this);
                }
            });
            toolbar.inflateMenu(R.menu.menu_usuario);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                MainActivity.posFragment = 0;
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.salvar_usuario:
                if (gravarUsuario()) {
                    MainActivity.posFragment = 0;
                    NavUtils.navigateUpFromSameTask(this);
                }return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean gravarUsuario()
    {
        try
        {
            UsuarioController.getInstance().init(this);

            //todo verificar se usuário já existe

            if(!UsuarioController.getInstance().validarDados()) return false;

            UsuarioController.getInstance().pegarDoFormulario();

            UsuarioController.getInstance().inserir();

            Log.i("LogX", "Usuario gravado!");
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
