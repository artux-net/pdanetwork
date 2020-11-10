package net.artux.pdanetwork.frontend.adminpanel;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.utills.mongo.MongoAdmin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/chat")
public class ChatController extends HttpServlet {

    MongoAdmin mongoAdmin = new MongoAdmin();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Member member = (Member) request.getSession().getAttribute("m");
        response.setContentType("text/html;");
        response.setCharacterEncoding("UTF-8");

        if (member != null) {
            request.getSession().setAttribute("username", member.getLogin());
            request.setAttribute("username", member.getLogin());

            request.setAttribute("link_back", "/admin/chat?action=back");

            String action = request.getParameter("action");
            switch (action == null ? "info" : action) {
                case "info":
                    request.getRequestDispatcher("/chat.jsp").forward(request, response);
                    break;
                case "back":
                    request.getRequestDispatcher("/admin").forward(request, response);
                    break;

            }

        } else {
            response.sendRedirect(request.getContextPath() + "/loginAdmin");
        }
    }


}
