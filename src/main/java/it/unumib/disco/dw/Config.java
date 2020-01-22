package it.unumib.disco.dw;

public class Config
{

    public static class Database
    {
        public static final String NAME = "dw";

        public static final String URL = "jdbc:mysql://localhost:3306/" + NAME;

        public static final String USER = "root";

        public static final String PASSWORD = "root";
    }

    public static class ExternalSource
    {
        public static class Weather
        {

            public static final String REALTIME = "https://www.torinometeo.org/api/v1/realtime/data/";

            public static final String HISTORICAL = "https://www.torinometeo.org/api/v1/realtime/history/%s/%s/%s";

        }

        public static class Helbiz
        {
            public static final String API_ENDPOINT = "https://api.helbiz.com";
            public static final String LOGIN = "/prod/user/authenticate";
            public static final String REGIONS = "/prod/regions";
            public static final String VEHICLES = "/prod/vehicles";
        }
    }
}
