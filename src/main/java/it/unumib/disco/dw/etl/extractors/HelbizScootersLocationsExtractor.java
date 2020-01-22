package it.unumib.disco.dw.etl.extractors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unumib.disco.dw.Config;
import it.unumib.disco.dw.etl.HelbizLoader;
import it.unumib.disco.dw.etl.model.HelbizRegion;
import it.unumib.disco.dw.etl.model.HelbizUser;

import it.unumib.disco.dw.etl.model.HelbizVehicle;
import kong.unirest.GenericType;
import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.Unirest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HelbizScootersLocationsExtractor
{
    private static final String REQUESTED_WITH_HEADER = "X-Requested-With";
    private static final String USER_AGENT = "User-Agent";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ACCESS_TOKEN = "X-access-token";
    private String accessToken;
    private HelbizLoader helbizLoader = new HelbizLoader();

    public static final Logger LOG = LogManager.getLogger();
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public HelbizScootersLocationsExtractor()
    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public  <R extends HttpRequest> HttpRequest<R> decorateRequest(HttpRequest<R> request)
    {
        return decorateRequest(request, false);
    }

    public <R extends HttpRequest> HttpRequest<R> decorateRequest(HttpRequest<R> request, boolean authRequired)
    {
        request.header(REQUESTED_WITH_HEADER, "XMLHttpRequest")
                .header(USER_AGENT, "Helbiz (com.helbiz.android)")
                .header(CONTENT_TYPE, "application/json");

        if (authRequired)
        {
            request.header(ACCESS_TOKEN, accessToken);
        }
        return request;
    }

    public void performLogin(String email, String password)
    {
        HttpRequestWithBody req = Unirest.post(Config.ExternalSource.Helbiz.API_ENDPOINT +
                Config.ExternalSource.Helbiz.LOGIN);

        String passwordMd5 = DigestUtils.md5Hex(password);

        decorateRequest(req.body(new HelbizUser(email, passwordMd5))).asJson()
                .ifSuccess(request -> {
                    accessToken = request.getBody().getObject().getString("token");
                    LOG.info("Login request result: success");
                    LOG.info("access token set to " + accessToken);
                })
                .ifFailure(request -> {
                    LOG.error("Login request result: failure");
                    LOG.error("User not logged in, ETL process will stop");
                });
    }

    public List<HelbizRegion> getRegions()
    {
        if (StringUtils.isEmpty(accessToken))
        {
            LOG.error("No access token found - getRegions request requires login");
            LOG.error("ETL process will stop");
        }

        String obj = decorateRequest(Unirest.get(Config.ExternalSource.Helbiz.API_ENDPOINT +
                Config.ExternalSource.Helbiz.REGIONS), true)
                .asString().getBody();

        List<HelbizRegion> regions = null;

        obj = obj.replaceAll("null", "[]");

        return decorateRequest(Unirest.get(Config.ExternalSource.Helbiz.API_ENDPOINT +
                Config.ExternalSource.Helbiz.REGIONS), true)
                .asObject(new GenericType<List<HelbizRegion>>(){})
                .ifFailure(Error.class, request -> {
                    Error e = request.getBody();
                    LOG.error("Region request failed");
                    LOG.error("Error returned: " + request);
                }).getBody();
    }

    public void showListOfRegions(List<HelbizRegion> regions)
    {
        LOG.info("------------------- Start List of regions ---------------------");
        Holder<Integer> count = new Holder<>(0);
        regions.stream().forEach(region -> {
            LOG.info("{} - Name: {} - Country: {} - StartTime: {} - EndTime: {} - Center: {}",
                    count.value,
                    region.getName(),
                    region.getCountry(),
                    region.getStartTime(),
                    region.getEndTime(),
                    region.getCenter());
            count.value++;
        });
        LOG.info("------------------- End  List of regions ---------------------");
    }

    private String printPoint(List<Double> point)
    {
        return String.format("%f,%f", point.get(0), point.get(1));
    }

    public List<HelbizVehicle> getVehicles(HelbizRegion region)
    {
        // get coordinates from region bounds
        if (CollectionUtils.isEmpty(region.getBounds()))
        {
            LOG.warn("No bound available for region with name " + region.getName());
            return Collections.EMPTY_LIST;
        }

        // get north west point
        region.getBounds().sort((x, y) -> {
            return -1 * x.get(0).compareTo(y.get(0));
        });
        List<Double> northWest = region.getBounds().get(0);
        LOG.debug("northWest: " + printPoint(northWest));

        // get south east point
        region.getBounds().sort((x, y) -> {
            return x.get(0).compareTo(y.get(0));
        });
        List<Double> southEast = region.getBounds().get(0);
        LOG.debug("southEast: " + printPoint(southEast));

        String reqDump = decorateRequest(Unirest.get(Config.ExternalSource.Helbiz.API_ENDPOINT +
                Config.ExternalSource.Helbiz.VEHICLES + "?northWest={northWest}&southEast={southEast}"), true)
                .routeParam("northWest", printPoint(northWest))
                .routeParam("southEast", printPoint(southEast)).asString()
                .ifFailure(Error.class, request -> {
                    Error e = request.getBody();
                    LOG.error("Vehicle request failed");
                    LOG.error("Error returned: " + request);
                })
                .getBody();

        List<HelbizVehicle> vehicleLst = new ArrayList<>();

        try{
            vehicleLst = objectMapper.readValue(reqDump, new TypeReference<List<HelbizVehicle>>(){});
        }
        catch(JsonProcessingException e) {
            LOG.error("Error in JSON parsing");
            LOG.error("The following exception has been thrown: ", e);
        }

        return vehicleLst.stream().filter(v -> region.getName().equals(v.getGeofence())).collect(Collectors.toList());
    }

    public void showVehicles(List<HelbizVehicle> vehicles)
    {
        LOG.info("------------------- Start List of vehicles ---------------------");
        LOG.info("Number of available vehicles: " + vehicles.size());
        vehicles.stream().forEach(v ->
                LOG.info("[id: {} - lat: {} - lon: {} - batteryLevelInMiles: {} - range: {} - power: {}]",
                        v.getId(), v.getLat(), v.getLon(), v.getBatteryLevelInMiles(), v.getRange(), v.getPower()));
        LOG.info("------------------- End   List of vehicles ---------------------");
    }

    public void loadVehicles(List<HelbizVehicle> vehicles)
    {
        LOG.info("Start loading of vehicles on database");
        helbizLoader.loadVehiclesPositions(vehicles);
        LOG.info("End loading of vehicles on database");
    }
}
