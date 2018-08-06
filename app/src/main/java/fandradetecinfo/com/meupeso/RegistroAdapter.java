package fandradetecinfo.com.meupeso;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fandradetecinfo.com.meupeso.Controllers.UsuarioController;
import fandradetecinfo.com.meupeso.Models.BalancaDigital;

/**
 * Created by Fernando on 14/07/2017.
 */

public class RegistroAdapter extends _BaseAdapter {

    private final List<BalancaDigital> registros;

    public RegistroAdapter(List<BalancaDigital> registros, Activity activity) {
        this.registros = registros;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.registros.size();
    }

    @Override
    public Object getItem(int position) {
        return this.registros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.registros.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View linha = convertView;
        BalancaDigital registro = registros.get(position);

        if (linha == null)
        {
            linha = this.activity.getLayoutInflater().inflate(R.layout.cell_frag_01, parent, false);
        }

        TextView data = (TextView) linha.findViewById(R.id.txtRegData);
        TextView usuario = (TextView) linha.findViewById(R.id.txtRegUsuario);
        TextView peso = (TextView) linha.findViewById(R.id.txtRegPeso);

        tratarCores(linha, position);

        String userName = UsuarioController.getInstance().getUsuarioNome(registro.getId_usuario());

        data.setText(registro.getDataFormatada(registro.getData_registro()));
        usuario.setText(userName);
        peso.setText(registro.getPeso());

        return linha;
    }
}
