package net.artux.pdanetwork.servlets;

import com.google.gson.Gson;
import com.mongodb.client.result.UpdateResult;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/resetData")
public class ResetDataServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        UpdateResult updateResult = ServletContext.mongoUsers
                .resetData(httpServletRequest.getHeader("t"));

        httpServletResponse.setContentType("application/json;");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().println(gson
                .toJson(new Status(true, String.valueOf(updateResult.wasAcknowledged()))));
    }

}
