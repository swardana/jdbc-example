package id.swhp.jdbc.dao;

import java.util.List;

/**
 * Basic CRUD action
 *
 * @param <K> Entity Class
 * @param <V> ID
 */
public interface BasicAction<K, V> {
    public void create(K entity);

    public void update(V id, K entity);

    public void delete(V id);

    public List<K> findAll(Integer currentPage, Integer resultRow);

    public K findById(V id);
}
