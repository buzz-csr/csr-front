package com.naturalmotion.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.naturalmotion.Configuration;

public class GetFile extends HttpServlet {

    private static final long serialVersionUID = 5739502846788943953L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter writer = resp.getWriter();) {
            Configuration configuration = new Configuration();
            String fileType = req.getParameter("file");
            String directory = req.getParameter("dir");

            String content = getFileContent(configuration, fileType, directory);
            writer.write(content);
        } catch (Exception e) {
            // TODO add logger
        }
    }

    public String getFileContent(Configuration configuration, String fileType, String directory) throws IOException {
        String path = configuration.getString("working.directory");
        File file = new File(path + "/" + directory + "/Edited/" + fileType + ".json");
        byte[] bytes = FileUtils.readFileToByteArray(file);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
