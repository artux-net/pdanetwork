package net.artux.pdanetwork.frontend.adminpanel;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.UserManager;
import net.artux.pdanetwork.communication.model.UserMessage;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.artux.pdanetwork.utills.ServletContext.mongoUsers;

@WebServlet(name = "AdminController", urlPatterns = "/admin")
public class AdminController extends HttpServlet {

    Gson mGson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = (String) request.getSession().getAttribute("token");
        response.setContentType("text/html;");
        response.setCharacterEncoding("UTF-8");

        if(token!=null){
            String loginOfAdmin = mongoUsers.getByToken(token).getLogin();
            String loginedAs = "Вы вошли как: " + loginOfAdmin;
            request.getSession().setAttribute("userName", loginOfAdmin);
            request.setAttribute("loginedAs", loginedAs);


            String action = request.getParameter("action");
            switch (action == null ? "info" : action) {
                case "update":
                    request.getRequestDispatcher("/update.jsp").forward(request, response);
                    break;
                case "info":
                default:
                    request.getRequestDispatcher("admin.jsp").forward(request, response);
                    break;
            }

        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                UserManager userManager = new UserManager();
                userManager.addItem(Integer.parseInt(request.getParameter("pdaId")),
                        Integer.parseInt(request.getParameter("type")),
                        Integer.parseInt(request.getParameter("item")),
                        Integer.parseInt(request.getParameter("quantity")));
                break;
        }

        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

    private List<UserMessage> chatList = new ArrayList<>();

    private void newMessage(String text, HttpServletRequest httpServletRequest){
        UserMessage userMessage = mGson.fromJson(text, UserMessage.class);
        chatList.add(userMessage);
        httpServletRequest.setAttribute("chatList", userMessage);

    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        HttpServletRequest httpServletRequest;

        EchoWebSocketListener(HttpServletRequest httpServletRequest){
            this.httpServletRequest = httpServletRequest;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("message");

            newMessage(text, httpServletRequest);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        }
    }

}
