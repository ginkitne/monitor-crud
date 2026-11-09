package com.wcd.servlet;

import com.wcd.dao.MonitoringTargetDAO;
import com.wcd.model.MonitoringTarget;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/targets/save")
public class TargetSaveServlet extends HttpServlet {

    private final MonitoringTargetDAO dao = new MonitoringTargetDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam       = request.getParameter("id");
        String url           = trimOrEmpty(request.getParameter("url"));
        String checkInterval = trimOrEmpty(request.getParameter("checkInterval"));
        String cssSelector   = trimOrEmpty(request.getParameter("cssSelector"));
        boolean active       = request.getParameter("active") != null;

        boolean isNew = (idParam == null || idParam.isBlank());

        if (url.isEmpty() || checkInterval.isEmpty()) {
            MonitoringTarget target = new MonitoringTarget();
            target.setId(isNew ? null : Long.parseLong(idParam));
            target.setUrl(url);
            target.setCheckInterval(checkInterval);
            target.setCssSelector(cssSelector);
            target.setActive(active);

            request.setAttribute("error", "URL and check interval are required.");
            request.setAttribute("target", target);
            request.setAttribute("isNew", isNew);
            request.getRequestDispatcher("/WEB-INF/views/target-form.jsp")
                   .forward(request, response);
            return;
        }

        try {
            MonitoringTarget target = new MonitoringTarget();
            target.setUrl(url);
            target.setCheckInterval(checkInterval);
            target.setCssSelector(cssSelector.isEmpty() ? null : cssSelector);
            target.setActive(active);

            if (isNew) {
                dao.insert(target);
                request.getSession().setAttribute("flash",
                    "Target \"" + target.getUrl() + "\" was created successfully.");
            } else {
                target.setId(Long.parseLong(idParam));
                dao.update(target);
                request.getSession().setAttribute("flash",
                    "Target \"" + target.getUrl() + "\" was updated successfully.");
            }

            response.sendRedirect(request.getContextPath() + "/targets");

        } catch (SQLException e) {
            throw new ServletException("Unable to save monitoring target", e);
        }
    }

    private static String trimOrEmpty(String s) {
        return s == null ? "" : s.trim();
    }
}
