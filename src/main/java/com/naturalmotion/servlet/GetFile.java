package com.naturalmotion.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalmotion.Configuration;
import org.apache.commons.io.FileUtils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GetFile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Configuration configuration = new Configuration();
        String fileType = req.getParameter("file");
        String directory = req.getParameter("dir");
        String path = configuration.getString("working.directory");
        File file = new File(path + "/" + directory + "/Edited/" + fileType + ".json");
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        resp.getWriter().write(new ObjectMapper().writeValueAsString(content));
    }
}
