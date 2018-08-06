package fandradetecinfo.com.meupeso.Controllers;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import fandradetecinfo.com.meupeso.Models.Usuario;

import fandradetecinfo.com.meupeso.R;

public class UsuarioController extends _BaseController {

    private static UsuarioController instancia = null;

    private CollectionReference itemsRef = FirebaseFirestore.getInstance().collection("Usuario");

    private List<Usuario> lstUsuario;

    private Usuario model;

    private EditText etNome;
    private EditText etAltura;
    private EditText etData;
    private Spinner spSexo;

    private Map<String, String> mapUsuario = new Hashtable<>();

    private UsuarioController() {
        super();
        this.TAG = "Usuario";
        lstUsuario = new ArrayList<Usuario>();
    }

    public void init(Activity activity)
    {
        this.activity = activity;
        this.model = new Usuario(activity.getBaseContext());

        this.etNome = (EditText) activity.findViewById(R.id.txtUsrNome);
        this.etAltura = (EditText) activity.findViewById(R.id.txtUsrAltura);
        this.etData = (EditText) activity.findViewById(R.id.txtUsrData);
        this.spSexo = (Spinner) activity.findViewById(R.id.spnUsrSexo);
    }
    public static UsuarioController getInstance()
    {
        if (instancia == null)
        {
            instancia = new UsuarioController();
        }
        return instancia;
    }

    public List<Usuario> getLstRegistro() {
        return lstUsuario;
    }

    public void setLstRegistro(List<Usuario> lstUsuario) {
        this.lstUsuario = lstUsuario;
    }

    public void setMapUsuario(Map<String, String> mapUsuario) {
        this.mapUsuario = mapUsuario;
    }

    public Map<String, String> getMapUsuario() {
        return mapUsuario;
    }

    public String getUsuarioNome(String doc_id) {
        return (String)getKeyFromValue(mapUsuario, doc_id);
    }

    public String getUsuarioDocId(String nome)
    {
        return mapUsuario.get(nome);
    }


    public Usuario getModel() {
        return model;
    }

    public void setModel(Usuario model) {
        this.model = model;
    }

    public void pegarDoFormulario()
    {
        model.setNome(etNome.getText().toString());
        model.setAltura(etAltura.getText().toString());
        model.setDataNascimento(model.getDataTimestamp(etData.getText().toString()));
        model.setSexo(mapSexo.get(spSexo.getSelectedItemPosition()));
    }

    public boolean validarDados()
    {
        if (!validarCampo(etNome)) return false;
        if (!validarLista(spSexo, "Sexo", 1)) return false;
        if (!validarCampo(etAltura)) return false;
        return validarCampo(etData);

    }

    public void alertarUsuarioPossuiRegistro()
    {
        montarAlerta("Meu Peso Diário ->  Excluir Usuário", "Usuário possui registros!");
    }

    public void apagar() {

        FirebaseFirestore.getInstance().collection("BalancaDigital")
                .whereEqualTo("id_usuario", model.getDocId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                itemsRef.document(model.getDocId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i("LogX", "Usuário apagado!");
                                                Toast.makeText(activity, "Usuário apagado com sucesso", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Log.i("LogX " + TAG, " id_usuario " + model.getDocId()
                                        + " possui " + task.getResult().size() + " registros");
                                alertarUsuarioPossuiRegistro();
                            }
                        } else {
                            Log.e("LogX " + TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void alertarUsuarioExistente()
    {
        montarAlerta("Meu Peso Diário ->  Novo Usuário", "Usuário já cadastrado!");
    }

    public void inserir()
    {
        itemsRef
                .whereEqualTo("nome", model.getNome())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Map<String, Object> dataToSave = new HashMap<String, Object>();
                                dataToSave.put("nome", model.getNome());
                                dataToSave.put("altura", model.getAltura());
                                dataToSave.put("sexo", model.getSexo());
                                dataToSave.put("data_nascimento", model.getDataNascimento());

                                itemsRef.add(dataToSave).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("LogX: " + TAG, "documento salvo");
                                    }
                                });

                            } else {
                                Log.i("LogX " + TAG, " Usuário " + model.getNome().toUpperCase()
                                        + " já cadastrado");
                                alertarUsuarioExistente();
                            }
                        } else {
                            Log.e("LogX " + TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
