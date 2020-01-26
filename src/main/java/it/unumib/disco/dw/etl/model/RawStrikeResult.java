package it.unumib.disco.dw.etl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RawStrikeResult
{
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class RawResult
    {
        List<RawStrike> records;

        public List<RawStrike> getRecords()
        {
            return records;
        }

        public void setRecords(List<RawStrike> records)
        {
            this.records = records;
        }

        @Override
        public String toString()
        {
            return "RawResult{" +
                    "records=" + records.size() +
                    '}';
        }
    }


    private RawResult result;

    public RawResult getResult()
    {
        return result;
    }

    public void setResult(RawResult result)
    {
        this.result = result;
    }


}
