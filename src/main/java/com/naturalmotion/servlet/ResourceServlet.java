package com.naturalmotion.servlet;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.naturalmotion.Configuration;
import com.naturalmotion.csr_api.api.ResourceType;
import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.updater.ProfileUpdater;
import com.naturalmotion.csr_api.service.updater.ProfileUpdaterFileImpl;
import com.naturalmotion.csr_api.service.updater.UpdaterException;

public class ResourceServlet extends HttpServlet {

    private static final long serialVersionUID = -2698675847367966977L;

    private static final String SEPARATOR = "/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        String type = req.getParameter("type");
        if ("cash".equals(type)) {
            addCashOr(req, resp);
        } else {
            addKeys(req, resp);
        }
    }

    public void addKeys(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String bronze = req.getParameter("bronze");
            String gold = req.getParameter("gold");
            String silver = req.getParameter("silver");
            String dir = req.getParameter("dir");
            String user = req.getParameter("user");

            Configuration configuration = new Configuration();
            String path = configuration.getString("working.directory");

            ProfileUpdater profileUpdater = new ProfileUpdaterFileImpl(path + SEPARATOR + user + SEPARATOR + dir);
            profileUpdater.updateResource(ResourceType.BRONZE_KEY, new BigDecimal(bronze));
            profileUpdater.updateResource(ResourceType.GOLD_KEY, new BigDecimal(gold));
            profileUpdater.updateResource(ResourceType.SILVER_KEY, new BigDecimal(silver));

            resp.getWriter().write(new NsbFormatter().getFileContent(configuration, dir, user));
        } catch (IOException
                | UpdaterException
                | NsbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addCashOr(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String cash = req.getParameter("cash");
            String gold = req.getParameter("gold");
            String dir = req.getParameter("dir");
            String user = req.getParameter("user");

            Configuration configuration = new Configuration();
            String path = configuration.getString("working.directory");

            ProfileUpdater profileUpdater = new ProfileUpdaterFileImpl(path + SEPARATOR + user + SEPARATOR + dir);
            profileUpdater.updateResource(ResourceType.CASH, new BigDecimal(cash));
            profileUpdater.updateResource(ResourceType.GOLD, new BigDecimal(gold));

            resp.getWriter().write(new NsbFormatter().getFileContent(configuration, dir, user));
        } catch (IOException
                | UpdaterException
                | NsbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
