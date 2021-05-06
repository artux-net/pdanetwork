package net.artux.pdanetwork.servlets;

import com.google.gson.Gson;
import net.artux.pdanetwork.authentication.Member;
import net.artux.pdanetwork.models.profile.Achievement;
import net.artux.pdanetwork.utills.mongo.MongoUsers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.artux.pdanetwork.utills.ServletHelper.getHeaders;

@WebServlet("/achievements")
public class AchievementServlet extends HttpServlet {

    private final List<Achievement> achievements = new ArrayList<>();
    private final Gson gson = new Gson();

    static {

    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        int pdaId = Integer.parseInt(getHeaders(httpServletRequest).get("pdaId"));
        Member member = MongoUsers.getMember(pdaId);
        if (member != null) {
            List<Achievement> achievementsOfMember = new ArrayList<>();
            for (Achievement achievement : achievements) {
                if (member.achievements.contains(achievement.cid))
                    achievementsOfMember.add(achievement);
            }

            httpServletResponse.getWriter().println(gson.toJson(achievementsOfMember));
        } else
            httpServletResponse.sendError(404, "Wrong pdaId");
    }
}
