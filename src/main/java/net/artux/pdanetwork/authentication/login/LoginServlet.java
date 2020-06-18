package net.artux.pdanetwork.authentication.login;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.UpdateData;
import net.artux.pdanetwork.authentication.login.model.LoginUser;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;
import org.bson.conversions.Bson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.set;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        /*
            авторизация, получение токена
         */

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            LoginUser loginUser = gson.fromJson(RequestReader.getString(request), LoginUser.class);
            response.getWriter().println(gson.toJson(ServletContext.
                    mongoUsers.tryLogin(loginUser.getEmailOrLogin(), loginUser.getPassword())));
        } catch (Exception ex) {
            ex.printStackTrace();
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
            Member member = ServletContext.mongoUsers.getByToken(token);
            resp.setContentType("application/json;");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().println(gson.toJson(member));
        } else {
          resp.sendError(400);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token  = req.getHeader("t");
        UpdateData updateData = gson.fromJson(RequestReader.getString(req), UpdateData.class);

        List<Bson> listSets = new ArrayList<>();
        for (String key : updateData.values.keySet()) {
            listSets.add(set(key, updateData.values.get(key)));
        }
        ServletContext.mongoUsers.changeFields(token, listSets);

        resp.getWriter().println("ok");
    }


    @Override
    public void destroy() {
        super.destroy();
    }
}
