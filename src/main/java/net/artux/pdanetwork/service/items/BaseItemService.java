package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.items.BaseItemEntity;
import net.artux.pdanetwork.entity.items.ItemType;
import net.artux.pdanetwork.repository.items.BaseItemRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class BaseItemService {

    private final ObjectMapper objectMapper;
    private final BaseItemRepository repository;

    @PostConstruct
    public void init() {
        repository.saveAll(readAll());
    }

    private List<BaseItemEntity> readAll() {
        try {
            Resource resource = new ClassPathResource("static/base/items/" + "base.json");
            return Arrays.stream(objectMapper.readValue(resource.getInputStream(), BaseItemEntity[].class)).toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Transactional
    public BaseItemEntity getBaseItem(long baseId) {
        return repository.findById(baseId).orElseThrow();
    }

    @Transactional
    public List<BaseItemEntity> getTypeItems(ItemType type) {
        return repository.findAllByType(type);
    }

}