package net.artux.pdanetwork.frontend.adminpanel;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.authentication.UserManager;
import net.artux.pdanetwork.utills.mongo.MongoAdmin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminController", urlPatterns = "/admin")
public class AdminController extends HttpServlet {

    MongoAdmin mongoAdmin = new MongoAdmin();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Member member = (Member) request.getSession().getAttribute("m");
        response.setContentType("text/html;");
        response.setCharacterEncoding("UTF-8");

        if (member != null) {
            request.getSession().setAttribute("username", member.getLogin());
            request.setAttribute("username", member.getLogin());

            String action = request.getParameter("action");
            request.setAttribute("link_index", "/admin");
            request.setAttribute("link_chat", "/admin/chat");
            request.setAttribute("link_users", "/admin?action=users");
            request.setAttribute("link_reset", "/admin?action=reset");
            switch (action == null ? "info" : action) {
                case "update":
                    request.getRequestDispatcher("/update.jsp").forward(request, response);
                    break;
                case "users":
                    request.setAttribute("users", mongoAdmin.find(""));
                    request.getRequestDispatcher("/users.jsp").forward(request, response);
                    break;
                case "info":
                    request.setAttribute("total_registrations", mongoAdmin.getSize());
                    request.setAttribute("rating", mongoAdmin.getRating(0));
                    request.getRequestDispatcher("/admin.jsp").forward(request, response);
                    break;
                case "reset":
                    request.getSession().invalidate();
                    request.getRequestDispatcher("/loginAdmin").forward(request, response);
                    break;
                default:
                    request.setAttribute("rating", mongoAdmin.getRating(0));
                    request.getRequestDispatcher("/admin.jsp").forward(request, response);
                    break;
            }

        } else {
            response.sendRedirect(request.getContextPath() + "/loginAdmin");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        UserManager userManager = new UserManager();
        switch (action) {
            case "add":
                userManager.addItem(Integer.parseInt(request.getParameter("pdaId")),
                        Integer.parseInt(request.getParameter("type")),
                        Integer.parseInt(request.getParameter("item")),
                        Integer.parseInt(request.getParameter("quantity")));
                break;
            case "find":
                String q = request.getParameter("q");
                request.setAttribute("users", mongoAdmin.find(q));
                request.getRequestDispatcher("/users.jsp").forward(request, response);
                break;
            case "addmoney":
                int money = Integer.parseInt(request.getParameter("money"));
                int pdaId = Integer.parseInt(request.getParameter("pda"));
                request.setAttribute("msg", userManager.addMoney(pdaId, money));
                request.getRequestDispatcher("/users.jsp").forward(request, response);
                break;
        }

        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

}
