package it.unumib.disco.dw.etl.extractors;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import java.io.IOException;
import java.net.URL;

public abstract class AbstractJSONExtractor
{


    protected JsonParser getParser(String url)
    {
        JsonFactory jsonFactory = new MappingJsonFactory();
        try
        {
            return jsonFactory.createParser(new URL(url));

        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }
    }


}
