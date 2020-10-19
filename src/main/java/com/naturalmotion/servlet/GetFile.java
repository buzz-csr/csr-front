package com.naturalmotion.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.Configuration;

public class GetFile extends HttpServlet {

    private static final long serialVersionUID = 5739502846788943953L;

    private final Logger log = LoggerFactory.getLogger(GetFile.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter writer = resp.getWriter();) {
            Configuration configuration = new Configuration();
            String directory = req.getParameter("dir");
            String user = req.getParameter("user");
            String type = req.getParameter("type");

            String content = null;
            if ("profile".equals(type)) {
                content = new NsbFormatter().getFileContent(configuration, directory, user);
            } else {
                content = new NsbFormatter().getId(configuration, directory, user);
            }
            writer.write(content);
        } catch (Exception e) {
            log.error("Error reading profile files", e);
        }
    }

}
