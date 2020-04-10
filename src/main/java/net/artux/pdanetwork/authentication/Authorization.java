package net.artux.pdanetwork.authentication;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.register.ConfirmationServlet;
import net.artux.pdanetwork.authentication.register.model.RegisterUser;
import net.artux.pdanetwork.models.Status;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.mail.MailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class Authorization {

    private Gson gson = new Gson();
    private MailService mailService = new MailService();

    public void handleRegister(HttpServletRequest request, HttpServletResponse response){
        String loc = request.getHeader("locale");

        try {
            RegisterUser newUser = gson.fromJson(RequestReader.getString(request), RegisterUser.class);
            Status status = ServletContext.mongoUsers.checkUser(newUser.getLogin(),
                    newUser.getEmail());

            String token = Base64.getEncoder().encodeToString(newUser.getLogin().getBytes());

            if(status.getSuccess()){
                System.out.println(ConfirmationServlet.registerUserMap.keySet());
                if (!ConfirmationServlet.registerUserMap.containsKey(token))
                    if(mailService.sendConfirmLetter(newUser, token)){
                        request.setAttribute("newUser", newUser);
                        request.setAttribute("token", token);

                        request.getServletContext().getRequestDispatcher("/confirm")
                                    .include(request, response);
                        status = new Status(200, "Проверьте почту", true);
                    }else {
                        status = new Status(500, "Не удалось отправить письмо, попробуйте позже", false);
                    }
                else
                    status = new Status(false, "Пользователь ожидает регистрации");
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(gson.toJson(status));
        } catch (Exception ex) {
            response.setStatus(400);
            System.out.println(ex.getMessage());
        }
    }
}
