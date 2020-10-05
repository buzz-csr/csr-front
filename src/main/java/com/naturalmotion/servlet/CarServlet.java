package com.naturalmotion.servlet;

import java.io.IOException;

import javax.json.JsonObject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.naturalmotion.Configuration;
import com.naturalmotion.csr_api.service.car.CarException;
import com.naturalmotion.csr_api.service.car.CarServiceFileImpl;

public class CarServlet extends HttpServlet {

    private static final long serialVersionUID = -7980787428623402843L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=UTF-8");

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String dir = req.getParameter("dir");

            Configuration configuration = new Configuration();
            String path = configuration.getString("working.directory");
            JsonObject json = new CarServiceFileImpl(path + "/" + dir).full(id);

            resp.getWriter().write(json.toString());
        } catch (IOException
                | CarException e) {
            // TODO Add logger
            e.printStackTrace();
        }

    }

}
