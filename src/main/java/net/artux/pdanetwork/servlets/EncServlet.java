package net.artux.pdanetwork.servlets;

import net.artux.pdanetwork.utills.FileGenerator;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/enc/*")
public class EncServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() != null) {
            String[] path = req.getPathInfo().split("/");
            resp.setContentType("text/html; charset=UTF-8");
            try {
                resp.getWriter().print(FileGenerator.Articles.getArticle(path[1]));
            } catch (Exception e) {
                ServletContext.error("Enc error",e);
                ServletHelper.setError(404, "Еще не готово, проверь позже.", req, resp);
            }

        } else
            ServletHelper.setError(500, "Такой ссылки нет", req, resp);

    }
}
