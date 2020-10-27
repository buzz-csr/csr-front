package com.naturalmotion.servlet;

import java.io.IOException;

import javax.json.JsonObject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.Configuration;
import com.naturalmotion.csr_api.service.car.CarException;
import com.naturalmotion.csr_api.service.car.CarServiceFileImpl;
import com.naturalmotion.csr_api.service.io.NsbException;

public class CarServlet extends HttpServlet {

    private static final String SEPARATOR = "/";
    private static final long serialVersionUID = -7980787428623402843L;

    private final Logger log = LoggerFactory.getLogger(CarServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=UTF-8");

        String action = req.getParameter("action");
        if ("full".equals(action)) {
            fullCar(req, resp);
        } else if ("add".equals(action)) {
            addCar(req, resp);
        } else if ("elite".equals(action)) {
            eliteCar(req, resp);
        } else {
            replaceCar(req, resp);
        }
    }

    private void eliteCar(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String dir = req.getParameter("dir");
            String user = req.getParameter("user");

            Configuration configuration = new Configuration();
            String path = configuration.getString("working.directory");
            JsonObject json = new CarServiceFileImpl(path + SEPARATOR + user + SEPARATOR + dir).elite(id);

            resp.getWriter().write(new NsbFormatter().filteredCar(json).toString());
        } catch (IOException
                | CarException
                | NsbException e) {
            log.error("Error adding elite on car", e);
        }
    }

    public void addCar(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathNewCar = req.getParameter("path");

            String path = new PathBuilder().build(req);
            new CarServiceFileImpl(path).add(pathNewCar + ".txt");

            resp.getWriter().write(new NsbFormatter().getFileContent(path));
        } catch (IOException
                | CarException
                | NsbException e) {
            log.error("Error adding car", e);
        }
    }

    private void replaceCar(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String dir = req.getParameter("dir");
            String user = req.getParameter("user");
            String pathNewCar = req.getParameter("path");

            Configuration configuration = new Configuration();
            String path = configuration.getString("working.directory");
            JsonObject json = new CarServiceFileImpl(path + SEPARATOR + user + SEPARATOR + dir).replace(id,
                    pathNewCar + ".txt");
            resp.getWriter().write(new NsbFormatter().filteredCar(json).toString());
        } catch (IOException
                | CarException
                | NsbException e) {
            log.error("Error replacing car", e);
        }
    }

    private void fullCar(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String dir = req.getParameter("dir");
            String user = req.getParameter("user");

            Configuration configuration = new Configuration();
            String path = configuration.getString("working.directory");
            JsonObject json = new CarServiceFileImpl(path + SEPARATOR + user + SEPARATOR + dir).full(id);

            resp.getWriter().write(new NsbFormatter().filteredCar(json).toString());
        } catch (IOException
                | CarException
                | NsbException e) {
            log.error("Error max car", e);
        }
    }

}
