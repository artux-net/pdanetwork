package net.artux.pdanetwork.service;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.BaseEntity;
import org.springframework.data.repository.CrudRepository;

@RequiredArgsConstructor
public class CRUDService <T extends BaseEntity> {

    protected final CrudRepository<T, Long> repository;

    protected T create(T t){
        return repository.save(t);
    }

    protected T read(Long id){
        return repository.findById(id).orElseThrow();
    }

    protected T update(T t){
        return repository.save(t);
    }

    protected void delete(Long id){
        repository.deleteById(id);
    }

}
