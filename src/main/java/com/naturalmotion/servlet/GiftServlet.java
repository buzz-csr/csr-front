package com.naturalmotion.servlet;

import com.naturalmotion.Configuration;
import com.naturalmotion.csr_api.service.car.CarException;
import com.naturalmotion.csr_api.service.gift.GiftService;
import com.naturalmotion.csr_api.service.gift.GiftServiceFileImpl;
import com.naturalmotion.csr_api.service.io.NsbException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GiftServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String dir = req.getParameter("dir");
            Configuration configuration = new Configuration();
            String path = configuration.getString("working.directory");

            GiftService service = new GiftServiceFileImpl(path + "/" + dir);
            service.addEssence();
        } catch (IOException | CarException | NsbException e) {
            e.printStackTrace();
        }
    }
}
