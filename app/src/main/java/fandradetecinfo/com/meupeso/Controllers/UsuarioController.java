package fandradetecinfo.com.meupeso.Controllers;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    private Double novo_peso;
    private String novo_id_usuario;

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
        model.setAltura(Double.parseDouble(etAltura.getText().toString()));
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

    public void atualizar(String id_usuario, Double peso) {

        novo_peso = peso;
        novo_id_usuario = id_usuario;

        itemsRef.document(id_usuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Usuario usuario = new Usuario(activity.getBaseContext());

                            Map<String, Object> dataToLoad = task.getResult().getData();

                            Long num_registros = (Long) dataToLoad.get("num_registros");
                            Double peso_medio = (Double) dataToLoad.get("peso_medio");

                            peso_medio = ((peso_medio * num_registros) + novo_peso) / (++num_registros);

                            itemsRef.document(novo_id_usuario).update("num_registros", num_registros);
                            itemsRef.document(novo_id_usuario).update("peso_medio", peso_medio);

                            Log.d("LogX " + TAG, task.getResult().getId() + " => " + task.getResult().getData());
                        } else {
                            Log.d("LogX " + TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("altura", model.getAltura());
        dataToSave.put("data_nascimento", model.getDataNascimento());
        dataToSave.put("nome", model.getNome());
        dataToSave.put("num_registros", model.getNum_registros());
        dataToSave.put("peso_medio", model.getPeso_medio());
        dataToSave.put("sexo", model.getSexo());

        itemsRef
                .add(dataToSave)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("LogX: " + TAG, "documento salvo");
            }
        });

    }
}
