package fandradetecinfo.com.meupeso.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import fandradetecinfo.com.meupeso.Controllers.UsuarioController;
import fandradetecinfo.com.meupeso.MainActivity;
import fandradetecinfo.com.meupeso.Models.Usuario;
import fandradetecinfo.com.meupeso.R;
import fandradetecinfo.com.meupeso.UsuarioAdapter;

public class Fragment00 extends _BaseFragment {

    public static List<Usuario> listUsuario = new ArrayList<Usuario>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        TAG = "Usuario";

        this.initListener();

        UsuarioController.getInstance().init(getActivity());

        View vw = inflater.inflate(R.layout.frag_00, container, false);

        FloatingActionButton fab = (FloatingActionButton) vw.findViewById(R.id.fabFrag00);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarAdicionarUsuario();
            }
        });
		
		minhaLista = (ListView) vw.findViewById(R.id.usuario_list);
        registerForContextMenu(minhaLista);


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

    @Override
    protected void carregarLista()
    {
        FirebaseFirestore.getInstance().collection(TAG)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            listUsuario.clear();

                            for (DocumentSnapshot document : task.getResult()) {

                                Usuario usuario = new Usuario(getContext());

                                Map<String, Object> dataToLoad = document.getData();

                                usuario.setNome(dataToLoad.get("nome").toString());
                                usuario.setAltura(dataToLoad.get("altura").toString());
                                usuario.setSexo(dataToLoad.get("sexo").toString());
                                usuario.setDataNascimento((Date)dataToLoad.get("data_nascimento"));
                                usuario.setDocId(document.getId());

                                UsuarioController.getInstance().getMapUsuario().put(
                                        dataToLoad.get("nome").toString(),
                                        document.getId());

                                listUsuario.add(usuario);

                                Log.d("LogX " + TAG, document.getId() + " => " + document.getData());
                            }

                            //montarMapUsuario();

                            UsuarioAdapter adapter = new UsuarioAdapter(listUsuario, getActivity());

                            minhaLista.setAdapter(adapter);
                        } else {
                            Log.d("LogX " + TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void montarMapUsuario()
    {
        Map<String, String> mapUsuario = new Hashtable<>();

        for(Usuario u : listUsuario)
        {
            mapUsuario.put(u.getNome(), u.getDocId());
        }
        UsuarioController.getInstance().setMapUsuario(mapUsuario);

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
