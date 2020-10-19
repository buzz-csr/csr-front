package com.naturalmotion.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.naturalmotion.Configuration;
import com.naturalmotion.csr_api.service.car.CarException;
import com.naturalmotion.csr_api.service.gift.GiftService;
import com.naturalmotion.csr_api.service.gift.GiftServiceFileImpl;
import com.naturalmotion.csr_api.service.io.NsbException;

public class GiftServlet extends HttpServlet {

    private static final long serialVersionUID = 6868517541785832236L;
    private static final String SEPARATOR = "/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String dir = req.getParameter("dir");
            String user = req.getParameter("user");
            Configuration configuration = new Configuration();
            String path = configuration.getString("working.directory");

            GiftService service = new GiftServiceFileImpl(path + SEPARATOR + user + SEPARATOR + dir);
            service.addEssence();
        } catch (IOException
                | CarException
                | NsbException e) {
            e.printStackTrace();
        }
    }
}
