package net.artux.pdanetwork.service.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.artux.pdanetwork.models.Seller;
import net.artux.pdanetwork.models.SellerDto;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class SellersService implements FileService{

    private static List<Seller> sellers;
    private static List<SellerDto> sellerDtos;
    private final ValuesService valuesService;
    private final Types types;

    public SellersService(ValuesService valuesService, Types types) {
        this.valuesService = valuesService;
        this.types = types;
        reset();
    }

    private List<Seller> readSellers(){
        try {
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/sellers.json");
            Type itemsListType = TypeToken.getParameterized(ArrayList.class, Seller.class).getType();
            return new Gson().fromJson(commonFile, itemsListType);
        } catch (IOException e) {
            //ServletContext.error("Sellers error", e);
        }
        return null;
    }

    public List<Seller> getSellers() {
        return sellers;
    }

    public Seller getSeller(int id) {
        for (Seller seller : sellers)
            if (seller.getId() == id)
                return seller;
        return null;
    }

    public SellerDto getSellerDto(int id) {
        for (SellerDto seller : sellerDtos)
            if (seller.getId() == id)
                return seller;
        return null;
    }


    @Override
    public void reset() {
        sellers = new ArrayList<>();
        sellerDtos = new ArrayList<>();
        sellers = readSellers();
        if (sellers!=null)
            for(Seller seller : getSellers()){
                sellerDtos.add(new SellerDto(seller, types));
            }
    }
}
