package it.unumib.disco.dw.etl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RawWeatherSensor
{

    private String idsensore;

    private String nomestazione;


    private String tipologia;


    private String unit_dimisura;


    private String provincia;


    public String getNomestazione()
    {
        return nomestazione;
    }

    public void setNomestazione(String nomestazione)
    {
        this.nomestazione = nomestazione;
    }

    public String getIdsensore()
    {
        return idsensore;
    }

    public void setIdsensore(String idsensore)
    {
        this.idsensore = idsensore;
    }

    public String getTipologia()
    {
        return tipologia;
    }

    public void setTipologia(String tipologia)
    {
        this.tipologia = tipologia;
    }

    public String getUnit_dimisura()
    {
        return unit_dimisura;
    }

    public void setUnit_dimisura(String unit_dimisura)
    {
        this.unit_dimisura = unit_dimisura;
    }

    public String getProvincia()
    {
        return provincia;
    }

    public void setProvincia(String provincia)
    {
        this.provincia = provincia;
    }

    @Override
    public String toString()
    {
        return "RawWeatherSensor{" +
                "idsensore='" + idsensore + '\'' +
                ", nomestazione='" + nomestazione + '\'' +
                ", tipologia='" + tipologia + '\'' +
                ", unit_dimisura='" + unit_dimisura + '\'' +
                ", provincia='" + provincia + '\'' +
                '}';
    }
}
