package fandradetecinfo.com.meupeso.Models;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fernando on 14/02/2017.
 */

public class BalancaDigital extends _BaseModelSQLite implements Serializable  {

    private long id;
    private String doc_id;

    private String id_usuario;
    private Date data_registro;
    private String peso;
    private String gordura;
    private String hidratacao;
    private String musculo;
    private String osso;

    public long getId() {
        return id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public BalancaDigital(Context ctx) {
        super(ctx);
        this.table = "balancadigital";
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        //this.id_usuario = String.valueOf(Integer.parseInt(id_usuario)) ;
        this.id_usuario = id_usuario;
    }

    public Date getData_registro() {
        return data_registro;
    }

    public void setData_registro(Date data_registro) {
        this.data_registro = data_registro;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso.replace(',', '.');
    }

    public String getGordura() {
        return gordura;
    }

    public void setGordura(String gordura) {
        this.gordura = gordura.replace(',', '.');
    }

    public String getHidratacao() {
        return hidratacao;
    }

    public void setHidratacao(String hidratacao) {
        this.hidratacao = hidratacao.replace(',', '.');
    }

    public String getMusculo() {
        return musculo;
    }

    public void setMusculo(String musculo) {
        this.musculo = musculo.replace(',', '.');
    }

    public String getOsso() {
        return osso;
    }

    public void setOsso(String osso) {
        this.osso = osso.replace(',', '.');
    }

    public Cursor exibirRegistros()
    {
        try
        {
            String sql = "SELECT id_usuario, data_registro, peso"
                    + " FROM balancadigital"
                    + " ORDER BY data_registro desc";
            return buscarCursor(sql);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Cursor exibirRegistros(String usuario)
    {
        try
        {
            String sql = "SELECT data_registro, "
                    + " peso, gordura, hidratacao, id "
                    + " FROM balancadigital"
                    + " WHERE id_usuario = ?"
                    + " ORDER BY data_registro desc";
            String args[] = { usuario };
            return buscarCursor(sql, args);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Cursor exibirMedias(String usuario)
    {
        try {
            String sql = montarAvgSQL("1 sem.", 7, usuario) +
                    " UNION " + montarAvgSQL("2 sem.", 14, usuario) +
                    " UNION " + montarAvgSQL("3 sem.", 21, usuario) +
                    " UNION " + montarAvgSQL("1 m.", 30, usuario) +
                    " UNION " + montarAvgSQL("2 m.", 60, usuario) +
                    " UNION " + montarAvgSQL("3 m.", 90, usuario) +
                    " UNION " + montarAvgSQL("6 m.", 180, usuario) +
                    " UNION " + montarAvgSQL("1 ano", 365, usuario) +
                    " ORDER BY O";
            return buscarCursor(sql);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Cursor exibirTotais()
    {
        try {
            String sql = "SELECT id_usuario, u.nome as nome, "
                    + " u.sexo_masculino as sexo, "
                    + " printf('%.2f', avg(peso)) as peso_medio, "
                    + " printf('%.2f', (avg(peso) / (altura * altura))) as imc, "
                    + " count(*) as registros "
                    + " FROM balancadigital b "
                    + " INNER JOIN usuario u on u.id = b.id_usuario"
                    + " GROUP BY id_usuario"
                    + " ORDER BY id_usuario";
            return buscarCursor(sql);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private String montarAvgSQL(String periodo, int dias, String usuario)
    {
        String sql = " SELECT '" + periodo + "' AS periodo, " +
                " printf('%.2f', avg(peso)), " +
                " printf('%.2f', avg(gordura)), " +
                " printf('%.2f', avg(hidratacao)), " +
                " printf('%.2f', avg(musculo)), " +
                dias + " AS O" +
                " FROM balancadigital" +
                " WHERE (CAST(strftime('%j', 'now') AS INTEGER) - " +
                " CAST(strftime('%j', data_registro) AS INTEGER) < " + dias + ")" +
                " AND id_usuario = " + usuario;
        return sql;
    }

}
