package it.unumib.disco.dw.etl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RawWeatherValue
{
    private String idsensore;

    private String data;

    private String valore;

    private String stato;

    private String idOperatore;

    public String getIdOperatore()
    {
        return idOperatore;
    }

    public void setIdOperatore(String idOperatore)
    {
        this.idOperatore = idOperatore;
    }

    public String getIdsensore()
    {
        return idsensore;
    }

    public void setIdsensore(String idsensore)
    {
        this.idsensore = idsensore;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getValore()
    {
        return valore;
    }

    public void setValore(String valore)
    {
        this.valore = valore;
    }

    public String getStato()
    {
        return stato;
    }

    public void setStato(String stato)
    {
        this.stato = stato;
    }
}
