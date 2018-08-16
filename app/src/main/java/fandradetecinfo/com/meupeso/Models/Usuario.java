package fandradetecinfo.com.meupeso.Models;

import android.content.Context;

import java.io.Serializable;
import java.text.DecimalFormat;
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
    private Double altura;
    private Date data_nascimento;
    private Double peso_medio;
    private Long num_registros;

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
        peso_medio = 0.0;
        num_registros = Long.parseLong("0");
    }


    public String getNome() {
        return nome;
    }

    public String getSexo() {
        return sexo;
    }

    public Double getAltura() {
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

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.data_nascimento = dataNascimento;
    }

    public Double getPeso_medio() {
        return peso_medio;
    }

    public void setPeso_medio(Double peso_medio) {
        this.peso_medio = peso_medio;
    }

    public Long getNum_registros() {
        return num_registros;
    }

    public void setNum_registros(Long num_registros) {
        this.num_registros = num_registros;
    }

    public String getPeso_medioFormatado()
    {
        double d = getPeso_medio();

        return new DecimalFormat("0.##").format(d).toString();

    }

    public String getIMC()
    {
        double d = getPeso_medio()/(getAltura()*getAltura());

        return new DecimalFormat("0.00").format(d).toString();

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
