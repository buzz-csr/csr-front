package com.naturalmotion.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalmotion.Configuration;
import csr.Extract;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

@MultipartConfig
public class Upload extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Configuration configuration = new Configuration();

        String now = String.valueOf(System.currentTimeMillis());
        File backupDir = new File(configuration.getString("working.directory"));
        if (!backupDir.exists()) {
            backupDir.mkdir();
        }
        File actualDir = new File(backupDir.getPath() + "/" + now + "/Original/");
        actualDir.mkdirs();

        for (Part part : req.getParts()) {
            try (InputStream stream = part.getInputStream()) {
                byte[] buffer = new byte[stream.available()];
                if (stream.read(buffer) > 0) {
                    File target = new File(actualDir.getPath() + "/" + getFileName(part));
                    try (OutputStream outStream = new FileOutputStream(target)) {
                        outStream.write(buffer);
                    }
                }
            }
        }

        new Extract().unzipAll(backupDir.getPath() + "/" + now + "/");
        resp.getWriter().write(now);
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return "Default.file";
    }

}
