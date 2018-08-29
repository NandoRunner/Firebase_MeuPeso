package fandradetecinfo.com.javalib;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
    public BigDecimal calcularIdade(String dataDoMeuNascimento) throws ParseException{
        BigDecimal qtdDias = new BigDecimal(contarDias(dataDoMeuNascimento,getDataBr()));
        BigDecimal ano = new BigDecimal(365.25);
        BigDecimal idade = qtdDias.divide(ano,0, RoundingMode.DOWN);
        return idade;
    }

    public String getDataBr() {
        try {
            return getDataBr(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getDataBr(Date pData)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataFormatada = sdf.format(pData);
            return dataFormatada;
        } catch (Exception e) {
            throw e;
        }
    }

    public String contarDias(String dataInicialBR, String dataFinalBR) throws ParseException {
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

}
