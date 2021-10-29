package com.redis.lars.exercise5.bootstrap;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class Utilities {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Utilities.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> loadObjectList(Class<T> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            InputStream is = new ClassPathResource(fileName).getInputStream();
            MappingIterator<T> readValues = 
              mapper.readerFor(type).with(bootstrapSchema).readValues(is);
            return readValues.readAll();
        } catch (Exception e) {
            LOGGER.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }

    public static <T> String serializeObject(T object) throws JsonProcessingException   {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        return mapper.writeValueAsString(object);
    }

    public static <T> T deserializeObject(String value, Class<T> clazz) throws JsonMappingException, JsonProcessingException    {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        return mapper.readValue(value, clazz);
    }
}
