package fandradetecinfo.com.meupeso.Controllers;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fandradetecinfo.com.meupeso.MainActivity;
import fandradetecinfo.com.meupeso.Models.BalancaDigital;
import fandradetecinfo.com.meupeso.Models.Usuario;
import fandradetecinfo.com.meupeso.R;
import fandradetecinfo.com.meupeso.Relatorio;
import fandradetecinfo.com.meupeso.Views.Fragment00;
import fandradetecinfo.com.meupeso.Views.Fragment01;

public class BalancaDigitalController extends _BaseController {

    private static BalancaDigitalController instancia = null;

    private List<BalancaDigital> lstRegistro;

    public List<BalancaDigital> getLstRegistro() {
        return lstRegistro;
    }

    public void setLstRegistro(List<BalancaDigital> lstRegistro) {
        this.lstRegistro = lstRegistro;
    }

    private BalancaDigital model;

    private EditText etData;
    private EditText etPeso;
    private EditText etGordura;
    private EditText etHidratacao;
    private EditText etMusculo;
    private EditText etOsso;
    private Spinner spUsuario;

    private Double lastPeso;
    private String lastIdUsuario;


    private BalancaDigitalController() {
        super();
        this.TAG = "BalancaDigital";
        lstRegistro = new ArrayList<BalancaDigital>();
    }

    public void init(Activity activity)
    {
        this.activity = activity;
        this.model = new BalancaDigital(activity.getBaseContext());

        this.etData = (EditText) activity.findViewById(R.id.txtData);
        this.etPeso = (EditText) activity.findViewById(R.id.txtPeso);
        this.etGordura = (EditText) activity.findViewById(R.id.txtGordura);
        this.etHidratacao = (EditText) activity.findViewById(R.id.txtHidratacao);
        this.etMusculo = (EditText) activity.findViewById(R.id.txtMusculo);
        this.etOsso = (EditText) activity.findViewById(R.id.txtOsso);
        this.spUsuario = (Spinner) activity.findViewById(R.id.spinnerUsuario);
    }

    public static BalancaDigitalController getInstance()
    {
        if (instancia == null)
        {
            instancia = new BalancaDigitalController();
        }
        return instancia;
    }


    public BalancaDigital getModel() {
        return model;
    }

    public void setModel(BalancaDigital model) {
        this.model = model;
    }

    public Double getLastPeso() {
        return lastPeso;
    }

    public String getLastIdUsuario() {
        return lastIdUsuario;
    }

    public void pegarDoFormulario()
    {
        model.setPeso(etPeso.getText().toString());
        model.setGordura(etGordura.getText().toString());
        model.setHidratacao(etHidratacao.getText().toString());
        model.setMusculo(etMusculo.getText().toString());
        model.setOsso(etOsso.getText().toString());
        model.setData_registro(model.getDataTimestamp(etData.getText().toString()));

        String docId = UsuarioController.getInstance().getUsuarioDocId(spUsuario.getSelectedItem().toString());
        model.setId_usuario(docId);
    }

    public boolean validarDados()
    {
        if (!validarLista(spUsuario, "Usuário", 0)) return false;
        if (!validarCampo(etData)) return false;
        if (!validarCampo(etPeso)) return false;
        if (!validarCampo(etGordura)) return false;
        if (!validarCampo(etHidratacao)) return false;
        if (!validarCampo(etMusculo)) return false;
        return validarCampo(etOsso);
    }

    public boolean registroExistente()
    {
//        String sql = "SELECT count(*) FROM balancadigital"
//                + " WHERE data_registro = ?"
//                + " AND id_usuario = ?";
//        String args[] = { model.getData_registro(), model.getId_usuario() };
//
//        model.open();
//
//        boolean ret = model.exists(sql, args);
//
//        model.close();

        boolean ret = false;

        return ret;
    }


    public void alertarRegistroAnteriorIdentico()
    {
        montarAlerta("Meu Peso Diário ->  Gravar", "Usuário com registro anterior idêntico!");
    }

    public void alertarRegistroExistente()
    {
        montarAlerta("Meu Peso Diário ->  Gravar", "Usuário/Data já possui registro!");
    }

   public void apagar() {

   }
   

    public void inserir()
    {

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("id_usuario", model.getId_usuario());
        dataToSave.put("data_registro", model.getData_registro());
        dataToSave.put("peso", model.getPeso());
        dataToSave.put("gordura", model.getGordura());
        dataToSave.put("hidratacao", model.getHidratacao());
        dataToSave.put("musculo", model.getMusculo());
		dataToSave.put("osso", model.getOsso());

		lastIdUsuario = model.getId_usuario();
		lastPeso = Double.parseDouble(model.getPeso());

        FirebaseFirestore.getInstance().collection(TAG)
			.add(dataToSave)
			.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("LogX: " + TAG, "documento salvo");
                UsuarioController.getInstance()
                        .atualizar(lastIdUsuario, lastPeso);
            }
        });
    }

    public void carregarGrid(List<String> listHeader, GridView gridViewHeader, GridView gridView, Relatorio rel)
    {
        gridViewHeader.setAdapter(null);
        gridViewHeader.setAdapter(new ArrayAdapter<String>(activity.getBaseContext(), R.layout.cell, listHeader));

        gridView.setAdapter(null);
        ArrayList<String> list=new ArrayList<String>();
        ArrayAdapter adapter=new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item,list);

        //model.open();
        try
        {
            if (rel == Relatorio.Totais)
            {
                NumberFormat formatter = new DecimalFormat("00");

                Iterator<Usuario> usuIterator = Fragment00.listUsuario.iterator();
                while (usuIterator.hasNext()) {

                    Usuario reg = usuIterator.next();

                    list.add(reg.getDocId());
                    list.add(reg.getNome());
                    list.add(reg.getSexo());
                    list.add(reg.getPeso_medioFormatado());
                    list.add(reg.getIMC());
                    list.add(reg.getNum_registros().toString());

                    gridView.setAdapter(adapter);
                }

            }
            else if (rel == Relatorio.Registros){
                Iterator<BalancaDigital> regIterator = Fragment01.listRegistro.iterator();

                if (regIterator.hasNext()) {
                    while (regIterator.hasNext()) {

                        BalancaDigital reg = regIterator.next();

                        if (!reg.getId_usuario().equals(MainActivity.usuarioId)) {
                            continue;
                        }

                        list.add(reg.getDataFormatada(reg.getData_registro()).toString());
                        list.add(reg.getPeso());
                        list.add(reg.getGordura());
                        list.add(reg.getHidratacao());
                        list.add(reg.getMusculo());

                        gridView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(activity, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
                    Log.i("LogX", "Nenhum registro encontrado");
                }
            }
        }catch(Exception e)
        {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("LogX", e.getMessage());
        }
        //model.close();
    }

}
