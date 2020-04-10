package net.artux.pdanetwork.communication;

import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.utills.mongo.MongoUsers;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dialogs")
//TODO: change to messages
public class DialogsServlet extends HttpServlet {

    MongoUsers mongoUsers = new MongoUsers();

    @Override
    public void init(ServletConfig config) throws ServletException {
        config.getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("t");
        Member member = mongoUsers.getByToken(token);
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(member.getDialogs());
    }

    @Override
    public void destroy() {
        super.destroy();
        //TODO: make destroy void
    }
}
