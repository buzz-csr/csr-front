package com.naturalmotion.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalmotion.Configuration;
import csr.Compress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String dir = req.getParameter("dir");
        try {
            Configuration configuration = new Configuration();
            new Compress().zipAll(configuration.getString("working.directory") + "/" + dir + "/");

            File finalDir = new File(configuration.getString("working.directory") + "/" + dir + "/Final");
            String[] list = finalDir.list();

            resp.getWriter().write(new ObjectMapper().writeValueAsString(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dir = req.getParameter("dir");
        try {
            Configuration configuration = new Configuration();
            File backupDir = new File(configuration.getString("working.directory"));
            String type = req.getParameter("type");
            if ("nsb".equals(type)) {
                writeFile(resp, dir, backupDir, type);
            } else if ("scb".equals(type)) {
                writeFile(resp, dir, backupDir, type);
            } else if ("android".equals(type)) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(HttpServletResponse resp, String dir, File backupDir, String type) throws IOException {
        resp.setContentType("application/zip");
        resp.setHeader("Content-Disposition", "attachment;filename=\"nsb\"");
        OutputStream out = resp.getOutputStream();

        FileInputStream fis = new FileInputStream(backupDir.getPath() + "/" + dir + "/Final/" +
                type);
        int bytes;
        while ((bytes = fis.read()) != -1) {
            System.out.println(bytes);
            out.write(bytes);
        }
        fis.close();
        out.flush();
        resp.flushBuffer();
    }

}
