package net.artux.pdanetwork.service.files;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.artux.pdanetwork.models.story.seller.Seller;
import net.artux.pdanetwork.models.story.seller.SellerDto;
import net.artux.pdanetwork.service.util.ValuesService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SellerManager implements FileService {

    private static List<Seller> sellers;
    private static List<SellerDto> sellerDtos;
    private final ValuesService valuesService;
    private final ItemProvider itemProvider;
    private final ObjectMapper mapper;

    public SellerManager(ValuesService valuesService, ItemProvider itemProvider, ObjectMapper mapper) {
        this.valuesService = valuesService;
        this.itemProvider = itemProvider;
        this.mapper = mapper;
        reset();
    }

    private List<Seller> readSellers() {
        try {
            String commonFile = readFile(valuesService.getConfigUrl() + "base/items/sellers.json");
            return Arrays.asList(mapper.readValue(commonFile, Seller[].class));
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
        if (sellers != null)
            for (Seller seller : getSellers()) {
                //todo
                //sellerDtos.add(new SellerDto(seller, itemProvider));
            }
    }
}
