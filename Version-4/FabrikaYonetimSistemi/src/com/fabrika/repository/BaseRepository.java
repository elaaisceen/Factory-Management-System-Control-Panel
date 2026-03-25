package com.fabrika.repository;

import com.fabrika.model.BaseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseRepository<T extends BaseEntity> {
    private final Map<Integer, T> store = new LinkedHashMap<>();
    private final AtomicInteger idUretici = new AtomicInteger(1);

    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(idUretici.getAndIncrement());
        }
        store.put(entity.getId(), entity);
        return entity;
    }

    public Optional<T> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean deleteById(int id) {
        return store.remove(id) != null;
    }

    public long count() {
        return store.size();
    }
}
