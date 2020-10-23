package com.naturalmotion.servlet;

import java.io.IOException;

import javax.json.JsonObject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.csr_api.service.car.CarServiceFileImpl;
import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.updater.ProfileUpdaterFileImpl;

public class DebanServlet extends HttpServlet {

    private static final long serialVersionUID = 6765684344918999835L;

    private final Logger log = LoggerFactory.getLogger(DebanServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String path = new PathBuilder().build(req);
            new ProfileUpdaterFileImpl(path).deban();
            JsonObject json = new CarServiceFileImpl(path).removeEliteLevel();

            resp.getWriter().write(new NsbFormatter().filterNsb(json, path));
        } catch (IOException
                | NsbException e) {
            log.error("Error deban profile", e);
        }
    }

}
