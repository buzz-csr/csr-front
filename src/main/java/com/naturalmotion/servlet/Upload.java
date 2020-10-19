package com.naturalmotion.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.naturalmotion.Configuration;

import csr.Extract;

@MultipartConfig
public class Upload extends HttpServlet {

    private static final String SEPARATOR = "/";
    private static final long serialVersionUID = -1637365817304780292L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String now = String.valueOf(System.currentTimeMillis());
        String backupPath = getBackupPath(user, now);
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
