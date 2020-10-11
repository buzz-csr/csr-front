package com.naturalmotion.servlet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.json.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.naturalmotion.Configuration;

public class GetFile extends HttpServlet {

    private static final long serialVersionUID = 5739502846788943953L;

    private static final List<String> ALLOW = Arrays.asList("name", "caow", "cgpi");

    private static final List<String> CAOW_ALLOW = Arrays.asList("crdb", "nupl", "ctie", "cepi", "elcl", "unid");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter writer = resp.getWriter();) {
            Configuration configuration = new Configuration();
            String directory = req.getParameter("dir");

            String content = getFileContent(configuration, directory);
            writer.write(content);
        } catch (Exception e) {
            // TODO add logger
        }
    }

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
