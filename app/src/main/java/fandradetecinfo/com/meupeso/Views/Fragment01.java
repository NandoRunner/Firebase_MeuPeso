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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

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
    PrefsHandler prefs;

    List<BalancaDigital> lstRegistro = new ArrayList<BalancaDigital>();

    ListView minhaLista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        TAG = "BalancaDigital";

        View vw = inflater.inflate(R.layout.frag_01, container, false);

        FloatingActionButton fab = (FloatingActionButton) vw.findViewById(R.id.fabFrag01);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratarAdicionarRegistro();
            }
        });
		
		minhaLista = (ListView) vw.findViewById(R.id.lstRegistro);
        registerForContextMenu(minhaLista);

        BalancaDigitalController.getInstance().init(getActivity());

        return vw;
    }
    @Override
    public void onResume() {
        carregaLista();
        super.onResume();
    }
	
	private void carregaLista()
    {
        FirebaseFirestore.getInstance().collection(TAG)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            lstRegistro.clear();

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

                                lstRegistro.add(reg);

                                Log.d("LogX " + TAG, document.getId() + " => " + document.getData());
                            }

                            RegistroAdapter adapter = new RegistroAdapter(lstRegistro, getActivity());

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
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
