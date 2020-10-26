package com.naturalmotion.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.Configuration;
import com.naturalmotion.csr_api.api.EliteToken;
import com.naturalmotion.csr_api.api.EliteTokenParam;
import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.updater.ProfileUpdaterFileImpl;
import com.naturalmotion.csr_api.service.updater.UpdaterException;

import csr.Extract;

@MultipartConfig
public class Upload extends HttpServlet {

    private static final long serialVersionUID = -1637365817304780292L;

    private final Logger log = LoggerFactory.getLogger(Upload.class);

    private static final String SEPARATOR = "/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        String tokens = req.getParameter("tokens");
        if (tokens != null) {
            uploadForDeban(req, tokens);
        } else {
            uploadNewFile(req, resp);
        }
    }

    public void uploadForDeban(HttpServletRequest req, String tokens) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(tokens));) {
            JsonObject object = jsonReader.readObject();
            List<EliteTokenParam> params = createEliteParams(object);
            new ProfileUpdaterFileImpl(new PathBuilder().build(req)).updateResourceAfterBan(params);
        } catch (UpdaterException
                | NsbException
                | IOException e) {
            log.error("Error updating elite token after deban", e);
        }
    }

    public List<EliteTokenParam> createEliteParams(JsonObject object) {
        List<EliteTokenParam> params = new ArrayList<>();
        params.add(new EliteTokenParam(EliteToken.GREEN, BigDecimal.valueOf(object.getInt("Green"))));
        params.add(new EliteTokenParam(EliteToken.BLUE, BigDecimal.valueOf(object.getInt("Blue"))));
        params.add(new EliteTokenParam(EliteToken.RED, BigDecimal.valueOf(object.getInt("Red"))));
        params.add(new EliteTokenParam(EliteToken.YELLOW, BigDecimal.valueOf(object.getInt("Yellow"))));
        return params;
    }

    public void uploadNewFile(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String user = req.getParameter("user");
            String now = String.valueOf(System.currentTimeMillis());
            String backupPath;
            backupPath = getBackupPath(user, now);
            File actualDir = new File(backupPath + "Original" + SEPARATOR);
            actualDir.mkdirs();

            for (Part part : req.getParts()) {
                try (InputStream stream = part.getInputStream()) {
                    byte[] buffer = new byte[stream.available()];
                    if (stream.read(buffer) > 0) {
                        File target = new File(actualDir.getPath() + SEPARATOR + getFileName(part));
                        try (OutputStream outStream = new FileOutputStream(target)) {
                            outStream.write(buffer);
                        }
                    }
                }
            }

            new Extract().unzipAll(backupPath);
            resp.getWriter().write("{\"user\": \"" + user + "\", \"timestamp\": \"" + now + "\"}");
        } catch (IOException
                | ServletException e) {
            log.error("Error uploading files", e);
        }
    }

    public String getBackupPath(String user, String now) throws IOException {
        Configuration configuration = new Configuration();
        File backupDir = new File(configuration.getString("working.directory"));
        return backupDir.getPath() + SEPARATOR + user + SEPARATOR + now + SEPARATOR;
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return "Default.file";
    }

}
