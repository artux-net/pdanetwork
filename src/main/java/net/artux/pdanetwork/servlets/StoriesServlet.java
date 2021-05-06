package net.artux.pdanetwork.servlets;

import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;
import net.artux.pdanetwork.utills.Stories;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/stories")
public class StoriesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> params = ServletHelper.splitQuery(req.getQueryString());

        try {
            if (params.get("story") != null && params.get("chapter") != null) {
                int story = Integer.parseInt(params.get("story"));
                int chapter = Integer.parseInt(params.get("chapter"));
                String loc = params.get("loc");

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().println(Stories.getChapter(story, chapter));
            } else if (params.get("story") != null && params.get("map") != null) {
                int story = Integer.parseInt(params.get("story"));
                int maps = Integer.parseInt(params.get("map"));

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().println(Stories.getMap(story, maps));
            } else {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().println(Stories.getStories());
            }
        } catch (Exception e) {
            ServletContext.error("StoriesError", e);
            resp.setStatus(400);
        }
    }
}
