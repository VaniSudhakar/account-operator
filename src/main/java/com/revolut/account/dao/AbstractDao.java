package com.revolut.account.dao;

import com.google.inject.Inject;
import org.hibernate.type.EntityType;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Abstract Data-Access Object class to be implemented by all DAO's.
 */
public abstract class AbstractDao<T> {
    protected Class<T> entityClass;

    @Inject
    private EntityManager em;

    public AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Transactional
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    @Transactional
    public void update(T entity) {
        getEntityManager().merge(entity);
    }

    @Transactional
    public void remove(Long entityId) {
        T entity = find(entityId);

        if (entity != null)
            remove(entity);
    }

    @Transactional
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder()
                .createQuery(entityClass);
        cq.select(cq.from(entityClass));

        return getEntityManager().createQuery(cq).getResultList();
    }

    public int count() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        Long count = getEntityManager().createQuery(cq).getSingleResult();

        return count.intValue();
    }

}
