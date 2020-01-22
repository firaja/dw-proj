package it.unumib.disco.dw;

public interface Config
{

    interface Database
    {
        public static final String NAME = "dw";

        public static final String URL = "jdbc:mysql://localhost:3306/" + NAME;

        public static final String USER = "root";

        public static final String PASSWORD = "root";
    }

    interface ExternalSource
    {
        interface Weather
        {

            String REALTIME = "https://www.torinometeo.org/api/v1/realtime/data/";

            String HISTORICAL = "https://www.torinometeo.org/api/v1/realtime/history/%s/%s/%s";

        }

        interface Strike
        {

            String REALTIME = "http://dati.mit.gov.it/catalog/api/action/datastore_search?resource_id=6838feb1-1f3d-40dc-845f-d304088a92cd";

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
