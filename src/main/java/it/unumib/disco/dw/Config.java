package it.unumib.disco.dw;

public class Config
{

    public static class Database
    {
        public static final String NAME = "dw";

        public static final String URL = "dbc:mysql://localhost:3306/" + NAME;

        public static final String USER = "root";

        public static final String PASSWORD = "root";
    }

    public static class ExternalSource
    {
        public static class Weather
        {


            public static final String SENSORS_DESCRIPTION = "https://www.dati.lombardia.it/resource/nf78-nj6b.json?$limit=2000";

            public static final String SENSORS_VALUE = "https://www.dati.lombardia.it/resource/647i-nhxk.json";
        }


    }


}
