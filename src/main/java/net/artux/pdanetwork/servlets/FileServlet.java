package net.artux.pdanetwork.servlets;

import net.artux.pdanetwork.utills.FileGenerator;
import net.artux.pdanetwork.utills.RequestReader;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Map;

@WebServlet("/files")
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            Map<String, String> params = RequestReader.splitQuery(req.getQueryString());
            if(params.containsKey("id")){

            }else{
                resp.setContentType("image/gif");
                resp.setHeader("Content-Disposition", "filename=\"" + params.get("file") + "\"");

                BufferedImage bi = ImageIO.read(FileGenerator.Icons.getIcon(params.get("file")));
                OutputStream out = resp.getOutputStream();
                ImageIO.write(bi,   "gif", out);
                out.close();
            }

        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
        }

    }
}
