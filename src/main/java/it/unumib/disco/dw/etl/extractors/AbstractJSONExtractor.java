package it.unumib.disco.dw.etl.extractors;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.sun.istack.internal.NotNull;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public abstract class AbstractJSONExtractor
{

    private static final Logger LOG = LogManager.getLogger();

    protected JsonParser getParser(@NotNull String url)
    {
        JsonParser result;
        JsonFactory jsonFactory = new MappingJsonFactory();


        HttpGet request = new HttpGet(url);
        request.setHeader("Content-type", "application/json");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try
        {
            CloseableHttpResponse response = httpClient.execute(request);
            result = jsonFactory.createParser(response.getEntity().getContent());

        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }


        return result;
    }


}
