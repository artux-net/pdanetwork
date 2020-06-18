package net.artux.pdanetwork.frontend.adminpanel;

import com.google.gson.Gson;
import net.artux.pdanetwork.communication.model.UserMessage;
import net.artux.pdanetwork.utills.ServletContext;
import okhttp3.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.artux.pdanetwork.utills.ServletContext.mongoUsers;

@WebServlet(name = "AdminController", urlPatterns = "/admin")
public class AdminController extends HttpServlet {

    Gson mGson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = (String) request.getSession().getAttribute("token");

        if(token!=null){
            String loginOfAdmin = mongoUsers.getByToken(token).getLogin();
            String loginedAs = "Вы вошли как: " + loginOfAdmin;
            request.getSession().setAttribute("userName", loginOfAdmin);
            request.setAttribute("loginedAs", loginedAs);
            makePage(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }

    }

    private void makePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setPage(request, response);

        OkHttpClient client;
        WebSocket ws;

        client = new OkHttpClient();
        Request request1 = new Request.Builder().url("ws://" + ServletContext.host + ":8080" + "/chat"
                + "?t=" + request.getSession().getAttribute("token"))
                .build();


        EchoWebSocketListener listener = new EchoWebSocketListener(request);
        ws = client.newWebSocket(request1, listener);

        UserMessage userMessage = new UserMessage();
        userMessage.avatarId = 30;
        userMessage.time = new Date().toString();
        userMessage.pdaId = 0;
        userMessage.senderLogin = "System";

        userMessage.groupId = 1;


        client.dispatcher().executorService().shutdown();
    }

    @Override
    protected void doPost(HttpServletRequest request1, HttpServletResponse response) throws ServletException, IOException {
        setPage(request1, response);

    }

    private List<UserMessage> chatList = new ArrayList<>();

    private void newMessage(String text, HttpServletRequest httpServletRequest){
        UserMessage userMessage = mGson.fromJson(text, UserMessage.class);
        chatList.add(userMessage);
        httpServletRequest.setAttribute("chatList", userMessage);

    }

    private void setPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
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
