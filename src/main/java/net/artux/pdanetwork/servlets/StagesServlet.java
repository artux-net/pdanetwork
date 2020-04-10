package net.artux.pdanetwork.servlets;

import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.StageKeeper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@WebServlet("/stages")
public class StagesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> params = null;
        try {
            params = RequestReader.splitQuery(req.getQueryString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int story = Integer.parseInt(params.get("story"));
        int chapter = Integer.parseInt(params.get("chapter"));
        String loc = params.get("loc");

        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().println(StageKeeper.getChapter(story, chapter));
        } catch (Exception e) {
            resp.setStatus(400);
            e.printStackTrace();
        }
    }
}
