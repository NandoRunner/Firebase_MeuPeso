package fandradetecinfo.com.meupeso;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fandradetecinfo.com.meupeso.Models.Usuario;

/**
 * Created by Fernando on 22/02/2017.
 */

public class UsuarioAdapter extends _BaseAdapter {

    private final List<Usuario> usuarios;

    public UsuarioAdapter(List<Usuario> usuarios, Activity activity) {
        this.usuarios = usuarios;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return this.usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.usuarios.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View linha = convertView;
        Usuario usuario = usuarios.get(position);

        if (linha == null)
        {
            linha = this.activity.getLayoutInflater().inflate(R.layout.cell_frag_00, parent, false);
        }

        TextView nome = (TextView) linha.findViewById(R.id.txtUsuNome);
        TextView altura = (TextView) linha.findViewById(R.id.txtUsuAltura);
        TextView idade = (TextView) linha.findViewById(R.id.txtUsuIdade);
        TextView docid = (TextView) linha.findViewById(R.id.txtUsuID);
        TextView sexo = (TextView) linha.findViewById(R.id.txtUsuSexo);

        tratarCores(linha, position);
					
        nome.setText(usuario.getNome());
        altura.setText(usuario.getAltura().toString());
        idade.setText(usuario.getIdade());
        sexo.setText(usuario.getSexo());
        docid.setText(String.valueOf(usuario.getDocId()));

        return linha;
    }
}
