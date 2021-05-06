package net.artux.pdanetwork.utills;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.artux.pdanetwork.models.Seller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static net.artux.pdanetwork.utills.FileGenerator.readFile;
import static net.artux.pdanetwork.utills.ServletContext.getPath;

public class Sellers {

    private static List<Seller> sellers;

    public Sellers() {
        sellers = getSellers();
    }

    private List<Seller> getSellers() {
        try {
            String commonFile = readFile(getPath() + "base/items/sellers", StandardCharsets.UTF_8);
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, Seller.class).getType();
            return new Gson().fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            ServletContext.error("Sellers error", e);
        }
        return null;
    }

    public Seller getSeller(int id) {
        for (Seller seller : sellers)
            if (seller.id == id)
                return seller;
        return null;
    }

}
