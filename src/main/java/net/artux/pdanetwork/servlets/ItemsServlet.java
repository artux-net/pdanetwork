package net.artux.pdanetwork.servlets;

import com.google.gson.Gson;
import net.artux.pdanetwork.models.profile.items.Armor;
import net.artux.pdanetwork.models.profile.items.Artifact;
import net.artux.pdanetwork.models.profile.items.Item;
import net.artux.pdanetwork.models.profile.items.Weapon;
import net.artux.pdanetwork.utills.RequestReader;
import net.artux.pdanetwork.utills.Sellers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static net.artux.pdanetwork.utills.ServletContext.userManager;

@WebServlet("/items")
public class ItemsServlet extends HttpServlet {

    private Sellers sellers = new Sellers();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        int id = Integer.parseInt(httpServletRequest.getParameter("id"));
        httpServletResponse.getWriter().println(gson.toJson(sellers.getSeller(id)));
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String action = httpServletRequest.getParameter("action");
        Map<String, String> headers = RequestReader.getHeaders(httpServletRequest);
        String token = headers.get("t");

        int type = Integer.parseInt(headers.get("type"));
        String body = RequestReader.getString(httpServletRequest);
        Item item = getItem(type, body);

        if (item != null)
            switch (action) {
                case "buy":
                    int sellerId = Integer.parseInt(headers.get("sellerId"));
                    httpServletResponse.getWriter().println(gson.toJson(userManager.buy(token, item, sellers, sellerId)));
                    break;
                case "sell":
                    httpServletResponse.getWriter().println(gson.toJson(userManager.sell(token, item)));
                    break;
                case "set":
                    if (item instanceof Weapon) {
                        httpServletResponse.getWriter().println(gson.toJson(
                                userManager.setWeapon(token, (Weapon) item)));
                    } else if (item instanceof Armor)
                        httpServletResponse.getWriter().println(gson.toJson(
                                userManager.setArmor(token, (Armor) item)));
                    break;
            }

    }

    private Item getItem(int type, String body) {
        Item item;
        switch (type) {
            case 0:
            case 1:
                item = gson.fromJson(body, Weapon.class);
                break;
            case 3:
                item = gson.fromJson(body, Artifact.class);
                break;
            case 4:
                item = gson.fromJson(body, Armor.class);
                break;
            default:
                item = gson.fromJson(body, Item.class);
                break;
        }
        return item;
    }
}
