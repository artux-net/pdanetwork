package net.artux.pdanetwork.service;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class CRUDService <T extends BaseEntity> {

    protected final CrudRepository<T, UUID> repository;

    protected T create(T t){
        return repository.save(t);
    }

    protected T read(UUID id){
        return repository.findById(id).orElseThrow();
    }

    protected T update(T t){
        return repository.save(t);
    }

    protected void delete(UUID id){
        repository.deleteById(id);
    }

}
