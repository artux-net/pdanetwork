package net.artux.pdanetwork.authentication.login;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.UpdateData;
import net.artux.pdanetwork.authentication.login.model.LoginUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;
import org.bson.conversions.Bson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.set;
import static net.artux.pdanetwork.utills.ServletContext.mongoUsers;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        /*
            авторизация, получение токена
         */

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            LoginUser loginUser = gson.fromJson(ServletHelper.getString(request), LoginUser.class);
            ServletContext.log("New login " + loginUser.getEmailOrLogin());
            Status status = mongoUsers.tryLogin(loginUser.getEmailOrLogin(), loginUser.getPassword());
            ServletContext.log("result of " + loginUser.getEmailOrLogin() + " code "
                    +status.getCode() + " desc "+ status.getDescription());
            ServletHelper.setResponse(response, status);
        } catch (Exception ex) {
            ServletContext.error("Login Error", ex);
            response.setStatus(400);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*
            обновление информации профиля с сервера
         */
        String token = req.getHeader("t");
        if (token!=null){
            Member member = mongoUsers.getByToken(token);
            ServletHelper.setResponse(resp, member);
        } else {
          resp.sendError(400);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getHeader("t");
        UpdateData updateData = gson.fromJson(ServletHelper.getString(req), UpdateData.class);

        List<Bson> listSets = new ArrayList<>();
        for (String key : updateData.values.keySet()) {
            listSets.add(set(key, updateData.values.get(key)));
        }
        mongoUsers.changeFields(token, listSets);

        resp.getWriter().println("ok");
    }
}
