package net.artux.pdanetwork.servlets;

import com.google.gson.Gson;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/ratings")
public class RatingServlet extends HttpServlet {

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Map<String, String> params = ServletHelper.splitQuery(httpServletRequest.getQueryString());
        httpServletResponse.setCharacterEncoding("UTF-8");
        if (!params.containsKey("from"))
            httpServletResponse.getWriter().print(gson
                    .toJson(ServletContext.mongoUsers.sort(0)));
        else
            httpServletResponse.getWriter().print(gson
                    .toJson(ServletContext.mongoUsers.sort(Integer.parseInt(params.get("from")))));

    }
}
