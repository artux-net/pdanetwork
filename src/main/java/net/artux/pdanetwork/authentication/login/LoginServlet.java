package net.artux.pdanetwork.authentication.login;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.UpdateData;
import net.artux.pdanetwork.authentication.login.model.LoginStatus;
import net.artux.pdanetwork.authentication.login.model.LoginUser;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.mongo.MongoUsers;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private LoginStatus status;
    private Gson gson = new Gson();
    private MongoUsers users;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        users = new MongoUsers();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
            авторизация, получение токена
         */
        response.setContentType("application/json");
        try {
            LoginUser loginUser = gson.fromJson(RequestReader.getString(request), LoginUser.class);
            status = ServletContext.mongoUsers.tryLogin(loginUser.getEmailOrLogin(), loginUser.getPassword());
        } catch (Exception ex) {
            ex.printStackTrace();
            status.setSuccess(false);
            status.setDescription(ex.getMessage());
        }

        response.getWriter().println(gson.toJson(status));

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*
            обновление информации профиля с сервера
         */
        String token = req.getHeader("t");
        if (token!=null){
            Member member = users.getByToken(token);
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
        users.updateMember(token, updateData);

        resp.getWriter().println("ok");
    }


    @Override
    public void destroy() {
        super.destroy();
        users.close();
    }
}
