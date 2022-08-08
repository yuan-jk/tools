package com.yuanjk.json.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.yuanjk.json.serializer.ItemDeserializer;
import org.junit.Test;

/**
 * @author yuanjk
 * @version 21/4/9
 */
public class CustomDeserializerTest {

    private static String json = "{\"id\": 1, \"itemName\": \"theItem\", \"createdBy\": 2}";

    @Test
    public void manuallyConfig() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Item.class, new ItemDeserializer());
        mapper.registerModule(module);

        Item readValue = mapper.readValue(json, Item.class);

        System.out.println("owner id: " + readValue.owner.id);
    }

    @Test
    public void annotationConfig() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Item readValue = mapper.readValue(json, Item.class);

        System.out.println("owner id: " + readValue.owner.id);
    }

}
