package it.unumib.disco.dw.etl.transformers;

import it.unumib.disco.dw.etl.model.ParsedStrike;
import it.unumib.disco.dw.etl.model.RawStrike;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class StrikeTransformer
{

    private static final Logger LOG = LogManager.getLogger();

    public ParsedStrike transform(RawStrike strike)
    {
        ParsedStrike parsedStrike = new ParsedStrike();
        parsedStrike.setBegin(toDate(strike.getDataInizio(), "yyyy-MM-dd'T'HH:mm:ss"));
        parsedStrike.setEnd(toDate(strike.getDataFine(), "yyyy-MM-dd'T'HH:mm:ss"));
        parsedStrike.setCity(getCity(strike.getNome_provincia()));
        parsedStrike.setName(strike.getCategoria());
        parsedStrike.setSector(strike.getSettore());
        return parsedStrike;
    }

    private static String getCity(String str)
    {
        if (StringUtils.equalsIgnoreCase(str, "tutte"))
        {
            return "*";
        }
        return str;
    }


    private static LocalDateTime toDate(String date, String format)
    {
        try
        {

            Date d = new SimpleDateFormat(format, Locale.ITALIAN).parse(date);
            return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        }
        catch (Exception e)
        {
            LOG.error("Cannot parse date `{}` with format `{}`.", date, format);
            return null;
        }
    }

}
