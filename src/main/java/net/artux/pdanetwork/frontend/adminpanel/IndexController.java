package net.artux.pdanetwork.frontend.adminpanel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "IndexController", urlPatterns = "/")
public class IndexController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = (String) request.getSession().getAttribute("token");
        response.setContentType("text/html;");
        response.setCharacterEncoding("UTF-8");
        if (token != null) {
            request.getRequestDispatcher("/admin").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/loginAdmin");
        }

    }


}
