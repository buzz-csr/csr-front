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

import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.io.NsbReader;
import com.naturalmotion.csr_api.service.reader.ProfileReader;
import com.naturalmotion.csr_api.service.reader.ProfileReaderFileImpl;

public class NsbFormatter {

    private static final String SEPARATOR = "/";

    private static final List<String> ALLOW = Arrays.asList("name", "caow", "cgpi", "caea", "casp", "goea", "gosp",
            "gbke", "gbks", "gske", "gsks", "ggke", "ggks");

    private static final List<String> CAOW_ALLOW = Arrays.asList("crdb", "nupl", "ctie", "cepi", "elcl", "unid");

    public String getFileContent(String path) throws IOException, NsbException {
        String content = null;
        File file = new NsbReader().getNsbFile(path);
        try (InputStream fis = new FileInputStream(file); JsonReader reader = Json.createReader(fis);) {
            JsonObject jsonObject = reader.readObject();
            content = filterNsb(jsonObject, path);
        }
        return content;
    }

    public String filterNsb(JsonObject jsonObject, String path) throws NsbException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        for (Map.Entry<String, JsonValue> entry : jsonObject.entrySet()) {
            if (ALLOW.contains(entry.getKey())) {
                objectBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        JsonArrayBuilder newCaows = filteredCaow(jsonObject.getJsonArray("caow"));
        objectBuilder.add("caow", newCaows);

        objectBuilder.add("brands", getBrands(path));
        return objectBuilder.build().toString();
    }

    private JsonArray getBrands(String path) throws NsbException {
        ProfileReader reader = new ProfileReaderFileImpl(path);
        List<String> brands = reader.getBrands();
        brands.add("-- TOUTES --");
        return Json.createArrayBuilder(brands).build();
    }

    private JsonArrayBuilder filteredCaow(JsonArray caow) {
        JsonArrayBuilder newCaows = Json.createArrayBuilder();
        for (int i = 0; i < caow.size(); i++) {
            JsonObject jsonCaow = caow.get(i).asJsonObject();
            JsonObject json = filteredCar(jsonCaow);
            newCaows.add(json);
        }
        return newCaows;
    }

    public JsonObject filteredCar(JsonObject jsonCaow) {
        JsonObjectBuilder newCaow = Json.createObjectBuilder();
        for (Map.Entry<String, JsonValue> entry : jsonCaow.entrySet()) {
            if (CAOW_ALLOW.contains(entry.getKey())) {
                newCaow.add(entry.getKey(), entry.getValue());
            }
        }
        return newCaow.build();
    }

    public String getId(String path) throws IOException {
        String content = "";
        File file = new File(path + SEPARATOR + "Edited");
        String androidFileName = Arrays.stream(file.list())
                .filter(x -> !"nsb.json".equals(x) && !"scb.json".equals(x))
                .findFirst()
                .orElse(null);
        if (androidFileName != null) {
            File androidFile = new File(file.getPath() + SEPARATOR + androidFileName);
            try (InputStream fis = new FileInputStream(androidFile); JsonReader reader = Json.createReader(fis);) {
                JsonObject jsonObject = reader.readObject();
                content = jsonObject.getString("userid");
            }
        }
        return content;
    }
}
