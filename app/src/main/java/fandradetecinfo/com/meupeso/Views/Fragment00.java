package fandradetecinfo.com.meupeso.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import fandradetecinfo.com.meupeso.Controllers.UsuarioController;
import fandradetecinfo.com.meupeso.MainActivity;
import fandradetecinfo.com.meupeso.Models.Usuario;
import fandradetecinfo.com.meupeso.R;
import fandradetecinfo.com.meupeso.UsuarioListAdapter;

public class Fragment00 extends _BaseFragment {

    private RecyclerView recyclerView;

    private List<Usuario> listUsuario;
    private UsuarioListAdapter uAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        TAG = "Usuario";

        View vw = inflater.inflate(R.layout.frag_00, container, false);

        FloatingActionButton fab = (FloatingActionButton) vw.findViewById(R.id.fabFrag00);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarAdicionarUsuario();
            }
        });

		
		listUsuario = new ArrayList<>();

        uAdapter = new UsuarioListAdapter(listUsuario);

        recyclerView = (RecyclerView) vw.findViewById(R.id.usuario_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(uAdapter);

        FirebaseFirestore.getInstance().collection(TAG)
                .orderBy("nome")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        try {
                            if (e != null) {
                                Log.d("LogX Firelog", "Exception", e);
                            }

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    Usuario usuario = doc.getDocument().toObject(Usuario.class);
                                    usuario.setDocId(doc.getDocument().getId());
                                    usuario.setDataNascimento((Date)doc.getDocument().get("data_nascimento"));
                                    listUsuario.add(usuario);
                                    uAdapter.notifyDataSetChanged();

                                }
                                else if (doc.getType() == DocumentChange.Type.REMOVED) {

                                    Usuario uRemovido = doc.getDocument().toObject(Usuario.class);
                                    uRemovido.setDocId(doc.getDocument().getId());

                                    Iterator<Usuario> itr = listUsuario.iterator();
                                    while (itr.hasNext()) {
                                        Usuario usuario = itr.next();
                                        if (usuario.getDocId().equals(uRemovido.getDocId())) {
                                            itr.remove();
                                        }
                                    }
                                    uAdapter.notifyDataSetChanged();
                                }
                                else if (doc.getType() == DocumentChange.Type.MODIFIED) {

                                    Usuario uModificado = doc.getDocument().toObject(Usuario.class);
                                    uModificado.setDocId(doc.getDocument().getId());
                                    uModificado.setDataNascimento((Date)doc.getDocument().get("data_nascimento"));

                                    for (int i = 0; i < listUsuario.size(); i++)
                                    {
                                        if (listUsuario.get(i).getDocId().equals(uModificado.getDocId()))
                                        {
                                            listUsuario.get(i).setNome(uModificado.getNome());
                                            listUsuario.get(i).setAltura(uModificado.getAltura());
                                            listUsuario.get(i).setSexo(uModificado.getSexo());
                                            listUsuario.get(i).setDataNascimento(uModificado.getDataNascimento());
                                        }
                                    }
                                    uAdapter.notifyDataSetChanged();
                                }

                            }
                        }catch (Exception ex)
                        {
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG);
                        }
                    }
                });
        return vw;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Usuario usuarioSelecionado = (Usuario) listUsuario.get(info.position);

        final MenuItem itemPadrao = menu.add("Definir como Usuário Padrão");
        final MenuItem itemEditar = menu.add("Editar Usuário");
        final MenuItem itemApagar = menu.add("Apagar Usuário");

        itemPadrao.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MainActivity.usuario = UsuarioController.getInstance().getMapUsuario().get(usuarioSelecionado.getNome());
                String msg = "Usuário " + MainActivity.usuario + " selecionado!";
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                return false;
            }
        });
        itemEditar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        itemApagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                apagarUsuario(usuarioSelecionado.getDocId());
                return false;
            }
        });
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
	

    private void tratarAdicionarUsuario()
    {
        Intent objIntent = new Intent(getActivity(), UsuarioActivity.class);
        startActivity(objIntent);
    }

    public static Fragment00 newInstance(String text)
    {
        Fragment00 f = new Fragment00();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void apagarUsuario(String id) {
        UsuarioController.getInstance().init(getActivity());
		
		try {
            UsuarioController.getInstance().getModel().setDocId(id);
            UsuarioController.getInstance().apagar();
        }
        catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("LogX " + TAG, e.getMessage());
        }
    }
}
