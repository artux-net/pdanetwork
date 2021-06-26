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
import java.util.Date;

import static net.artux.pdanetwork.utills.ServletContext.mongoFeed;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if(httpServletRequest.getParameter("id")!=null){
            try{
                Article article = mongoFeed.getArticle(Integer.parseInt(httpServletRequest.getParameter("id")));
                if (article!=null) {
                    httpServletRequest.setAttribute("title", article.getTitle());
                    httpServletRequest.setAttribute("content", article.getContent());
                    httpServletRequest.setAttribute("tags", article.getStringTags());
                    httpServletRequest.setAttribute("date", new Date(article.getPublished()).toString());
                    httpServletRequest.setAttribute("comments", 0);

                    httpServletRequest.getRequestDispatcher("/article.jsp").forward(httpServletRequest, httpServletResponse);
                }else
                    ServletHelper.setError(404, "Эта запись была удалена или не существует.", httpServletRequest, httpServletResponse);
            }catch (Exception exception){
                ServletHelper.setError(404, "Эта запись не существует. " + exception.getMessage(), httpServletRequest, httpServletResponse);
            }
        }else
            ServletHelper.setResponse(httpServletResponse, mongoFeed.getArticles(0));

    }
}
