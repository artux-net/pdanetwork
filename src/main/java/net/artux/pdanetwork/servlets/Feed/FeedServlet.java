package net.artux.pdanetwork.servlets.Feed;

import com.google.gson.Gson;
import net.artux.pdanetwork.servlets.Feed.Models.Article;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.artux.pdanetwork.utills.ServletContext.mongoFeed;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if(httpServletRequest.getParameter("id")!=null){
            Article article = mongoFeed.getArticle(Integer.parseInt(httpServletRequest.getParameter("id")));
            if (article!=null) {

                httpServletRequest.setAttribute("title", article.getTitle());
                httpServletRequest.setAttribute("content", article.getContent());
                httpServletRequest.getRequestDispatcher("/article.jsp").forward(httpServletRequest, httpServletResponse);
            }else
                httpServletResponse.setStatus(404);
        }else
            ServletHelper.setResponse(httpServletResponse, mongoFeed.getArticles(0));

    }
}
