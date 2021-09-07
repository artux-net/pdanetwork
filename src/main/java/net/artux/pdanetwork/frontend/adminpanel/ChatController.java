package net.artux.pdanetwork.frontend.adminpanel;

import net.artux.pdanetwork.models.Member;
import net.artux.pdanetwork.communication.chat.ChatSocket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("/admin/chat")
public class ChatController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Member member = (Member) request.getSession().getAttribute("m");
        response.setContentType("text/html;");
        response.setCharacterEncoding("UTF-8");

        if (member != null) {
            request.getSession().setAttribute("username", member.getLogin());
            request.setAttribute("username", member.getLogin());

            request.setAttribute("link_index", "/pda/admin");
            request.setAttribute("link_chat", "/pda/admin/chat");
            request.setAttribute("link_articles", "/pda/admin?action=article");
            request.setAttribute("link_users", "/pda/admin?action=users");
            request.setAttribute("link_reset", "/pda/admin?action=reset");
            request.setAttribute("link_manager", "/manager");

            String action = request.getParameter("action");
            switch (action == null ? "info" : action) {
                default:
                    request.getRequestDispatcher("/chat.html").forward(request, response);
                    break;
            }

        } else {
            response.sendRedirect(request.getContextPath() + "/loginAdmin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String action = httpServletRequest.getParameter("action");
        switch (action == null ? "info" : action) {
            case "ban":
                Integer pdaId = Integer.parseInt(httpServletRequest.getParameter("pdaId"));
                ChatSocket.addToBanList(pdaId, "");
                break;
            case "remove":
                Long time = Long.parseLong(httpServletRequest.getParameter("time"));
                ChatSocket.removeMessage(time);
                break;
        }
        doGet(httpServletRequest, httpServletResponse);
    }
}
