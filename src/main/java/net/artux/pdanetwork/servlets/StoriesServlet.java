package net.artux.pdanetwork.servlets;

import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.Stories;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/stories")
public class StoriesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> params = RequestReader.splitQuery(req.getQueryString());

        try {
            if (params.get("story") != null && params.get("chapter") != null) {
                int story = Integer.parseInt(params.get("story"));
                int chapter = Integer.parseInt(params.get("chapter"));
                String loc = params.get("loc");

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().println(Stories.getChapter(story, chapter));
            } else {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().println(Stories.getStories());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(400);
        }
    }
}
