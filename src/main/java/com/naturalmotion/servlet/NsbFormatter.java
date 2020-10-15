package com.naturalmotion.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.naturalmotion.Configuration;

public class NsbFormatter {

    private static final List<String> ALLOW = Arrays.asList("name", "caow", "cgpi", "caea", "casp", "goea", "gosp",
            "gbke", "gbks", "gske", "gsks", "ggke", "ggks");

    private static final List<String> CAOW_ALLOW = Arrays.asList("crdb", "nupl", "ctie", "cepi", "elcl", "unid");

    public String getFileContent(Configuration configuration, String directory) throws IOException {
        String content = null;
        String path = configuration.getString("working.directory");
        File file = new File(path + "/" + directory + "/Edited/nsb.json");
        try (InputStream fis = new FileInputStream(file); JsonReader reader = Json.createReader(fis);) {
            JsonObject jsonObject = reader.readObject();
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
                if (ALLOW.contains(entry.getKey())) {
                    objectBuilder.add(entry.getKey(), entry.getValue());
                }
            }
            JsonArrayBuilder newCaows = filteredCaow(jsonObject.getJsonArray("caow"));
            objectBuilder.add("caow", newCaows);

            content = objectBuilder.build().toString();
        }
        return content;
    }

    private JsonArrayBuilder filteredCaow(JsonArray caow) {
        JsonArrayBuilder newCaows = Json.createArrayBuilder();
        JsonObjectBuilder newCaow = null;
        for (int i = 0; i < caow.size(); i++) {
            JsonObject jsonCaow = caow.get(i).asJsonObject();
            newCaow = Json.createObjectBuilder();
            for (Map.Entry<String, JsonValue> entry : jsonCaow.entrySet()) {
                if (CAOW_ALLOW.contains(entry.getKey())) {
                    newCaow.add(entry.getKey(), entry.getValue());
                }
            }
            newCaows.add(newCaow.build());
        }
        return newCaows;
    }
}