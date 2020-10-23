package com.naturalmotion.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.naturalmotion.Configuration;

public class PathBuilder {

    private static final String SEPARATOR = "/";

    public String build(HttpServletRequest req) throws IOException {
        String dir = req.getParameter("dir");
        String user = req.getParameter("user");
        Configuration configuration = new Configuration();
        String path = configuration.getString("working.directory");
        return path + SEPARATOR + user + SEPARATOR + dir;
    }
}
