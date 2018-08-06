package fandradetecinfo.com.meupeso;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fandradetecinfo.com.meupeso.Models.Usuario;

public class UsuarioListAdapter extends RecyclerView.Adapter<UsuarioListAdapter.ViewHolder> {

    public List<Usuario> usuariosList;

    public UsuarioListAdapter(List<Usuario> usuariosList) {
        this.usuariosList = usuariosList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_frag_00, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nomeText.setText(usuariosList.get(position).getNome());
        holder.alturaText.setText(usuariosList.get(position).getAltura());
        holder.idadeText.setText(usuariosList.get(position).getIdade());
        holder.sexoText.setText(usuariosList.get(position).getSexo());
        holder.docidText.setText(usuariosList.get(position).getDocId());
    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public TextView nomeText;
        public TextView alturaText;
        public TextView idadeText;
        public TextView sexoText;
        public TextView docidText;

        public ViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;

            nomeText = (TextView)mView.findViewById(R.id.txtUsuNome);
            alturaText = (TextView)mView.findViewById(R.id.txtUsuAltura);
            idadeText = (TextView)mView.findViewById(R.id.txtUsuIdade);
            sexoText = (TextView)mView.findViewById(R.id.txtUsuSexo);
            docidText = (TextView)mView.findViewById(R.id.txtUsuID);
        }

    }
}
