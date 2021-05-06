package net.artux.pdanetwork.servlets;

import com.google.gson.Gson;
import net.artux.pdanetwork.utills.FileGenerator;
import net.artux.pdanetwork.utills.ServletContext;
import net.artux.pdanetwork.utills.ServletHelper;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@WebServlet("/files")
@MultipartConfig
public class FileServlet extends HttpServlet {

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Map<String, String> params = ServletHelper.splitQuery(req.getQueryString());
            if (params.containsKey("id")) {

            } else {
                resp.setContentType("image/gif");
                resp.setHeader("Content-Disposition", "filename=\"" + params.get("file") + "\"");

                BufferedImage bi = ImageIO.read(FileGenerator.Icons.getIcon(params.get("file")));
                OutputStream out = resp.getOutputStream();
                ImageIO.write(bi, "gif", out);
                out.close();
            }

        } catch (Exception e) {
            resp.setStatus(500);
            ServletContext.error("FilesServlet Error", e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Collection<Part> parts = request.getParts();
        List<String> filenames = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        for (Part part :
                parts) {
            if (part.getName().contains("files")) {
                String filename = getFilename(part);
                if (filename != null) {
                    InputStream fileContent = part.getInputStream();
                    filename = filename.replace(" ", "-");

                    File file = new File(ServletContext.getPath() + "/uploads/articles/" + filename);
                    try (OutputStream outputStream = new FileOutputStream(file)) {
                        IOUtils.copy(fileContent, outputStream);
                        filenames.add(filename);
                    } catch (IOException e) {
                        ServletContext.error("Files", e);
                    }
                }
            }
        }

        Upload upload = new Upload(filenames.toArray(new String[0]), messages.toArray(new String[0]));
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(upload));
    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("Content-Disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

}
