package fandradetecinfo.com.meupeso.Models;

import android.content.Context;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import fandradetecinfo.com.myjavacorelib.DateUtil;

/**
 * Created by Fernando on 10/02/2017.
 */

public class Usuario extends _BaseModel implements Serializable  {

    private long id;
    private String doc_id;

    private String nome;
    private String sexo;
    private String altura;
    private Date data_nascimento;

    public long getId() {
        return id;
    }

    public String getDocId() {
        return doc_id;
    }

    public void setDocId(String doc_id) {
        this.doc_id = doc_id;
    }

    public Usuario(Context ctx)
    {
        super(ctx);
        //this.table = "usuario";
    }


    public String getNome() {
        return nome;
    }

    public String getSexo() {
        return sexo;
    }

    public String getAltura() {
        return altura;
    }

    public Date getDataNascimento() {
        return data_nascimento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setAltura(String altura) {
        this.altura = altura.replace(',', '.');
    }

    public void setDataNascimento(Date dataNascimento) {
        this.data_nascimento = dataNascimento;
    }

    public String getIdade() {
        try {
            DateUtil du = new DateUtil();
            return String.valueOf(du.calcularIdade(du.getDataBr(getDataNascimento())));
        } catch (ParseException pe) {
            return pe.getMessage();
        }
    }


}
