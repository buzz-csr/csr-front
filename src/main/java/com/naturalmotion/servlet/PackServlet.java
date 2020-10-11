package com.naturalmotion.servlet;

import com.naturalmotion.Configuration;
import csr.Compress;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class PackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String dir = req.getParameter("dir");
        try {
            Configuration configuration = new Configuration();
            File backupDir = new File(configuration.getString("working.directory"));
            new Compress().zipAll(backupDir.getPath() + "/" + dir + "/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
