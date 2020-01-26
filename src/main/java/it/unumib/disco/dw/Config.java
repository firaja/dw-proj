package it.unumib.disco.dw;

public interface Config
{

    interface Database
    {
        String NAME = "dw";

        String URL = "jdbc:mysql://localhost:3306/" + NAME;

        String USER = "root";

        String PASSWORD = "root";
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

            String ALL = "http://dati.mit.gov.it/catalog/api/action/datastore_search?resource_id=6838feb1-1f3d-40dc-845f-d304088a92cd&limit=5000";

            String REALTIME = "http://dati.mit.gov.it/catalog/api/action/datastore_search?resource_id=6838feb1-1f3d-40dc-845f-d304088a92cd&limit=100";


        }

        interface Helbiz
        {
            String API_ENDPOINT = "https://api.helbiz.com";
            String LOGIN = "/prod/user/authenticate";
            String REGIONS = "/prod/regions";
            String VEHICLES = "/prod/vehicles";
        }
    }
}
