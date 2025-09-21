package net.artux.pdanetwork.service.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.artux.pdanetwork.entity.items.BaseItemEntity;
import net.artux.pdanetwork.models.items.ItemType;
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
@Slf4j
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
            log.warn("Can not read base items", e);
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public BaseItemEntity getBaseItem(long baseId) {
        return repository.findById(baseId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<BaseItemEntity> getTypeItems(ItemType type) {
        return repository.findAllByType(type);
    }

}