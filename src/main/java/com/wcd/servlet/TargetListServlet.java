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
import java.util.List;

@WebServlet("/targets")
public class TargetListServlet extends HttpServlet {

    private final MonitoringTargetDAO dao = new MonitoringTargetDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<MonitoringTarget> targets = dao.findAll();
            request.setAttribute("targets", targets);

            Object flash = request.getSession().getAttribute("flash");
            if (flash != null) {
                request.setAttribute("flash", flash);
                request.getSession().removeAttribute("flash");
            }

            request.getRequestDispatcher("/WEB-INF/views/target-list.jsp")
                   .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to retrieve monitoring targets", e);
        }
    }
}
