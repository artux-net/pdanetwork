package net.artux.pdanetwork.frontend.adminpanel;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.service.util.ValuesService;
import net.artux.pdanetwork.utills.mongo.MongoAdmin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static net.artux.pdanetwork.utills.ServletContext.mongoFeed;

//@WebServlet(name = "AdminController", urlPatterns = "/admin")
public class AdminController extends HttpServlet {

    MongoAdmin mongoAdmin = new MongoAdmin(new ValuesService());

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm a");


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Member member = (Member) request.getSession().getAttribute("m");
        response.setContentType("text/html;");
        response.setCharacterEncoding("UTF-8");

        if (member != null) {
            Date d1 = new Date();
            request.getSession().setAttribute("username", member.getLogin());
            request.getSession().setAttribute("server_time", df.format(d1));
            request.setAttribute("username", member.getLogin());

            String action = request.getParameter("action");
            request.setAttribute("link_index", "/pda/admin");
            request.setAttribute("link_chat", "/pda/admin/chat");
            request.setAttribute("link_articles", "/pda/admin?action=article");
            request.setAttribute("link_users", "/pda/admin?action=users");
            request.setAttribute("link_logsx", "/pda/log");
            request.setAttribute("link_reset", "/pda/admin?action=reset");
            request.setAttribute("link_manager", "/manager");
            switch (action == null ? "info" : action) {
                case "update":
                    request.getRequestDispatcher("/update.jsp").forward(request, response);
                    break;
                case "users":
                    request.setAttribute("users", mongoAdmin.find(""));
                    request.getRequestDispatcher("/users.html").forward(request, response);
                    break;
                case "reset":
                    request.getSession().invalidate();
                    request.getRequestDispatcher("/loginAdmin").forward(request, response);
                    break;
                case "article":
                    request.setAttribute("articles", mongoFeed.getArticles(0));
                    request.getRequestDispatcher("/articleCreation.html").forward(request, response);
                    break;
                case "editArticle":
                    request.setAttribute("a", mongoFeed.getArticle(Integer.parseInt(request.getParameter("feedId"))));
                    request.getRequestDispatcher("/editArticle.html").forward(request, response);
                    break;

                default:
                    request.setAttribute("total_registrations", mongoAdmin.getSize());
                    request.setAttribute("rating", mongoAdmin.getRating(0));
                    request.setAttribute("online", mongoAdmin.getOnline());
                    request.setAttribute("registrations", mongoAdmin.getRegistrations());
                    request.getRequestDispatcher("/admin.html").forward(request, response);
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
        /*ActionService actionService = new ActionService();
        switch (action) {
            case "add":
                actionService.addItem(Integer.parseInt(request.getParameter("pdaId")),
                        Integer.parseInt(request.getParameter("type")),
                        Integer.parseInt(request.getParameter("item")),
                        Integer.parseInt(request.getParameter("quantity")));
                break;
            case "find":
                String q = request.getParameter("q");
                request.setAttribute("users", mongoAdmin.find(q));
                request.getRequestDispatcher("/users.html").forward(request, response);
                break;
            case "addMoney":
                int money = Integer.parseInt(request.getParameter("money"));
                int pdaId = Integer.parseInt(request.getParameter("pda"));
                request.setAttribute("msg", actionService.addMoney(pdaId, money));
                request.getRequestDispatcher("/users.html").forward(request, response);
                break;
            case "addArticle":
                *//*mongoFeed.addArticle(request.getParameter("title"),
                        request.getParameter("desc"), request.getParameter("pic"),
                        request.getParameter("tags"), request.getParameter("content"));;*//*
                request.setAttribute("articles", mongoFeed.getArticles(0));
                request.getRequestDispatcher("/articleCreation.html").forward(request, response);
                break;
            case "removeArticle":
                mongoFeed.removeArticle(Integer.parseInt(request.getParameter("feedId")));
                request.setAttribute("articles", mongoFeed.getArticles(0));
                request.getRequestDispatcher("/articleCreation.html").forward(request, response);
                break;
            case "editArticle":
                mongoFeed.editArticle(request.getParameter("feedId"), request.getParameter("title"),
                        request.getParameter("desc"), request.getParameter("pic"),
                        request.getParameter("tags"), request.getParameter("content"));
                request.setAttribute("articles", mongoFeed.getArticles(0));
                request.getRequestDispatcher("/articleCreation.html").forward(request, response);
                break;
            default:
                doGet(request, response);
                break;
        }*/
    }

}
