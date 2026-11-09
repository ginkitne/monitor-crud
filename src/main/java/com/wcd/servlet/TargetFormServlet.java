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

@WebServlet(urlPatterns = {"/targets/new", "/targets/edit"})
public class TargetFormServlet extends HttpServlet {

    private final MonitoringTargetDAO dao = new MonitoringTargetDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MonitoringTarget target;
        boolean isNew = "/targets/new".equals(request.getServletPath());

        if (isNew) {
            target = new MonitoringTarget();
            target.setActive(true);
        } else {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing target id");
                return;
            }
            try {
                target = dao.findById(Long.parseLong(idParam));
                if (target == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Target not found");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid target id");
                return;
            } catch (SQLException e) {
                throw new ServletException("Unable to load target", e);
            }
        }

        request.setAttribute("target", target);
        request.setAttribute("isNew", isNew);
        request.getRequestDispatcher("/WEB-INF/views/target-form.jsp")
               .forward(request, response);
    }
}
