package it.unumib.disco.dw.etl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RawStrike
{

    private String categoria;

    private String settore;

    private String nome_provincia;

    private String dataInizio;

    private String nome_regione;

    private String dataFine;

    public String getCategoria()
    {
        return categoria;
    }

    public void setCategoria(String categoria)
    {
        this.categoria = categoria;
    }

    public String getSettore()
    {
        return settore;
    }

    public void setSettore(String settore)
    {
        this.settore = settore;
    }

    public String getNome_provincia()
    {
        return nome_provincia;
    }

    public void setNome_provincia(String nome_provincia)
    {
        this.nome_provincia = nome_provincia;
    }

    public String getDataInizio()
    {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio)
    {
        this.dataInizio = dataInizio;
    }

    public String getNome_regione()
    {
        return nome_regione;
    }

    public void setNome_regione(String nome_regione)
    {
        this.nome_regione = nome_regione;
    }

    public String getDataFine()
    {
        return dataFine;
    }

    public void setDataFine(String dataFine)
    {
        this.dataFine = dataFine;
    }

    @Override
    public String toString()
    {
        return "Record{" + "categoria='" + categoria + '\'' + ", settore='" + settore + '\'' + ", nome_provincia='" + nome_provincia + '\'' + ", dataInizio='" + dataInizio + '\'' + ", nome_regione='" + nome_regione + '\'' + ", dataFine='" + dataFine + '\'' + '}';
    }

}
