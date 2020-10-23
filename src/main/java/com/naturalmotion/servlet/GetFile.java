package com.naturalmotion.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetFile extends HttpServlet {

    private static final long serialVersionUID = 5739502846788943953L;

    private final Logger log = LoggerFactory.getLogger(GetFile.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter writer = resp.getWriter();) {
            String type = req.getParameter("type");
            String path = new PathBuilder().build(req);

            String content = null;
            if ("profile".equals(type)) {
                content = new NsbFormatter().getFileContent(path);
            } else {
                content = new NsbFormatter().getId(path);
            }
            writer.write(content);
        } catch (Exception e) {
            log.error("Error reading profile files", e);
        }
    }

}
