package com.wcd.servlet;

import com.wcd.dao.MonitoringTargetDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/targets/delete")
public class TargetDeleteServlet extends HttpServlet {

    private final MonitoringTargetDAO dao = new MonitoringTargetDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing target id");
            return;
        }

        try {
            long id = Long.parseLong(idParam);
            dao.delete(id);
            request.getSession().setAttribute("flash",
                "Target #" + id + " was deleted successfully.");
            response.sendRedirect(request.getContextPath() + "/targets");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid target id");
        } catch (SQLException e) {
            throw new ServletException("Unable to delete monitoring target", e);
        }
    }
}
