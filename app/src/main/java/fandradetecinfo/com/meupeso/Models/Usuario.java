package fandradetecinfo.com.meupeso.Models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;

public class Usuario  {

    private String altura;
    private Date data_nascimento;
    private String nome;
    private String sexo;

    private String doc_id;

    public Usuario() {
    }

    public Usuario(String altura, Date data_nascimento, String nome, String sexo) {
        this.altura = altura;
        this.data_nascimento = data_nascimento;
        this.nome = nome;
        this.sexo = sexo;
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

    public String getDocId() {
        return doc_id;
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

    public void setDocId(String doc_id) {
        this.doc_id = doc_id;
    }
	
    public String getIdade() {
        try {
            return String.valueOf(calcularIdade(getDataFormatada(getDataNascimento())));

        } catch (ParseException pe) {
            return pe.getMessage();
        }
    }

    public String getDataFormatada(Date pData)
    {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String datFormatada = formato.format(pData);
            return datFormatada;
        } catch (Exception e) {
            throw e;
        }
    }

    public String contaDias(String dataInicialBR, String dataFinalBR) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date dataInicio = df.parse(dataInicialBR);
        Date dataFim = df.parse(dataFinalBR);
        long dt = (dataFim.getTime() - dataInicio.getTime()) + 3600000;
        Long diasCorridosAnoLong = (dt / 86400000L);
        Integer diasDecorridosInt = Integer.valueOf(diasCorridosAnoLong.toString());
        String diasDecorridos = String.valueOf(diasDecorridosInt); //Sem numeros formatados;
        return diasDecorridos;
    }

    public BigDecimal calcularIdade(String dataDoMeuNascimento) throws ParseException{
        BigDecimal qtdDias = new BigDecimal(contaDias(dataDoMeuNascimento,getDataDiaBr()));
        BigDecimal ano = new BigDecimal(365.25);
        BigDecimal idade = qtdDias.divide(ano,0, RoundingMode.DOWN);
        return idade;
    }

    public String getDataDiaBr() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String diaIguana = df.format(new Date());
        return diaIguana;
    }

    public Date getDataTimestamp(String pData)
    {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            return formato.parse(pData);
        } catch (ParseException pe) {
            pe.printStackTrace();
            Date d = new Date("01/01/1900");
            return d;
        } catch (Exception e) {
            Log.d("logX", e.getMessage());
            throw e;
        }
    }
}
