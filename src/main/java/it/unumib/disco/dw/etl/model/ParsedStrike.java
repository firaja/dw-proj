package it.unumib.disco.dw.etl.model;

import java.time.LocalDateTime;

public class ParsedStrike
{
    private LocalDateTime begin;

    private LocalDateTime end;

    private String name;

    private String city;


    private String sector;

    public LocalDateTime getBegin()
    {
        return begin;
    }

    public void setBegin(LocalDateTime begin)
    {
        this.begin = begin;
    }

    public LocalDateTime getEnd()
    {
        return end;
    }

    public void setEnd(LocalDateTime end)
    {
        this.end = end;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Override
    public String toString()
    {
        return "ParsedStrike{" +
                "begin=" + begin +
                ", end=" + end +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", sector='" + sector + '\'' +
                '}';
    }
}
