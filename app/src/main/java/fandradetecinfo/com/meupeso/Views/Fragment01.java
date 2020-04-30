package fandradetecinfo.com.meupeso.Views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fandradetecinfo.com.meupeso.Controllers.BalancaDigitalController;
import fandradetecinfo.com.meupeso.Models.BalancaDigital;
import fandradetecinfo.com.meupeso.PrefsHandler;
import fandradetecinfo.com.meupeso.R;
import fandradetecinfo.com.meupeso.RegistroAdapter;

public class Fragment01 extends _BaseFragment
{
    String nome;
    String email;
    String id;

    AccessToken accessToken;

    PrefsHandler prefs;

    private View vw;

    public static List<BalancaDigital> listRegistro = new ArrayList<BalancaDigital>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        TAG = "BalancaDigital";

        this.initListener();

        BalancaDigitalController.getInstance().init(getActivity());

        vw = inflater.inflate(R.layout.frag_01, container, false);

        FloatingActionButton fab = (FloatingActionButton) vw.findViewById(R.id.fabFrag01);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarAdicionarRegistro();
            }
        });
		
		minhaLista = (ListView) vw.findViewById(R.id.lstRegistro);
        registerForContextMenu(minhaLista);

        return vw;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    nome = object.getString("name");
                    email = object.getString("email");
                    id = object.getString("id");

                    TextView txtUsuarioLogado = (TextView) vw.findViewById(R.id.txtUsuarioLogado);
                    txtUsuarioLogado.setText(nome);

                    //TODO: Buscar e exibir imagem do Facebook
                    //aula 233 7:11

                }catch(JSONException e) {e.printStackTrace();}
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    protected void carregarLista()
    {
        FirebaseFirestore.getInstance().collection(TAG)
                .orderBy("data_registro", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            listRegistro.clear();

                            for (DocumentSnapshot document : task.getResult()) {

                                BalancaDigital reg = new BalancaDigital(getContext());

                                Map<String, Object> dataToLoad = document.getData();

                                reg.setPeso(dataToLoad.get("peso").toString());
                                reg.setGordura(dataToLoad.get("gordura").toString());
                                reg.setHidratacao(dataToLoad.get("hidratacao").toString());
                                reg.setMusculo(dataToLoad.get("musculo").toString());
                                reg.setOsso(dataToLoad.get("osso").toString());
                                reg.setId_usuario(dataToLoad.get("id_usuario").toString());
                                reg.setData_registro((Date)dataToLoad.get("data_registro"));

                                listRegistro.add(reg);

                                Log.d("LogX " + TAG, document.getId() + " => " + document.getData());
                            }

                            RegistroAdapter adapter = new RegistroAdapter(listRegistro, getActivity());

                            minhaLista.setAdapter(adapter);
                        } else {
                            Log.d("LogX " + TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
	
    private void tratarAdicionarRegistro()
    {
        Intent objIntent = new Intent(getActivity(), RegistroActivity.class);
        startActivity(objIntent);
    }


    public static Fragment01 newInstance(String text)
    {
        Fragment01 f = new Fragment01();
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
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }
}
