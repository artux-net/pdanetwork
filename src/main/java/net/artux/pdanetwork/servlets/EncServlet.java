package net.artux.pdanetwork.servlets;

import net.artux.pdanetwork.utills.FileGenerator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@WebServlet("/enc/*")
public class EncServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if(req.getPathInfo() == null){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print(returnList());

        }else if(Arrays.equals(req.getPathInfo().split("/"), new String[]{})){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print(returnList());

        }else {
            String[] path = req.getPathInfo().split("/");
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().print(FileGenerator.Articles.getArticle(path[1]));
            resp.setStatus(200);
        }
    }

    private String returnList() throws IOException {
        return  FileGenerator.readFile("base/list", StandardCharsets.UTF_8);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){

    }
}
