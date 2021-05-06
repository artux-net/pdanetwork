package net.artux.pdanetwork.servlets;

import net.artux.pdanetwork.utills.FileGenerator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/enc/*")
public class EncServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() != null) {
            String[] path = req.getPathInfo().split("/");
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().print(FileGenerator.Articles.getArticle(path[1]));
            resp.setStatus(200);
        } else resp.sendError(404, "File not found..");

    }
}
