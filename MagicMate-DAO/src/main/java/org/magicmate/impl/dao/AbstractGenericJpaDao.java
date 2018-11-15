/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.magicmate.dao.GenericJpaDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mnachev
 */
public abstract class AbstractGenericJpaDao<ET extends Serializable, ID extends Serializable>
        extends AbstractGenericDao<ET, ID>
        implements GenericJpaDao<ET, ID> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractGenericJpaDao.class);

    private EntityManagerFactory entityManagerFactory;
    private String persistenceUnitName;

    public AbstractGenericJpaDao(String persistenceUnitName) {
        LOG.debug("persistenceUnitName=" + persistenceUnitName);
        this.persistenceUnitName = persistenceUnitName;
    }

    @Override
    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    @Override
    public void setPersistenceUnitName(String persistenceUnitName) {
        LOG.debug("persistenceUnitName=" + persistenceUnitName);
        this.persistenceUnitName = persistenceUnitName;
    }

    private EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName());
            LOG.debug("entityManagerFactory=" + entityManagerFactory);
        }

        return entityManagerFactory;
    }

    @Override
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        LOG.debug("entityManagerFactory=" + entityManagerFactory);
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    @Override
    public ET persist(EntityManager em, ET entity) {
        LOG.debug("entity=" + entity);
        em.persist(entity);
        LOG.debug("Presisted entity=" + entity);
        return entity;
    }

    @Override
    public ET persist(ET entity) {
        LOG.debug("entity=" + entity);
        EntityManager em = createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            persist(em, entity);
            em.flush();
            tx.commit();
        } catch (PersistenceException ex) {
            LOG.error("The transaction will rollback.", ex);
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
        LOG.debug("Presisted entity=" + entity);
        return entity;
    }

    @Override
    public ET[] persistAll(ET... entities) {
        EntityManager em = createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            persistAll(em, entities);
            em.flush();
            tx.commit();
        } catch (PersistenceException ex) {
            LOG.error("The transaction will rollback.", ex);
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
        LOG.debug("Presisted entities=" + Arrays.toString(entities));

        return entities;
    }

    @Override
    public ET[] persistAll(EntityManager em, ET... entities) {
        persistAll(em, Arrays.asList(entities));
        return entities;
    }

    @Override
    public Collection<ET> persistAll(Collection<ET> entities) {
        EntityManager em = createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            persistAll(em, entities);
            em.flush();
            tx.commit();
        } catch (PersistenceException ex) {
            LOG.error("The transaction will rollback.", ex);
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
        LOG.debug("Presisted entities=" + entities);

        return entities;
    }

    @Override
    public Collection<ET> persistAll(EntityManager em, Collection<ET> entities) {
        LOG.debug("entities=" + entities);
        entities.forEach((entity) -> {
            em.persist(entity);
        });
        LOG.debug("Presisted entities=" + entities);
        return entities;
    }

    @Override
    public ET findById(EntityManager em, ID key) {
        LOG.debug("key=" + key);
        ET entity = em.find(getPersistentClass(), key);
        LOG.debug("entity=" + entity);
        return entity;
    }

    @Override
    public ET findById(ID key) {
        LOG.debug("key=" + key);
        EntityManager em = createEntityManager();
        try {
            ET entity = findById(em, key);
            LOG.debug("entity=" + entity);
            return entity;
        } finally {
            em.close();
        }
    }

    @Override
    public List<ET> findAll(EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Class pc = getPersistentClass();
        CriteriaQuery<ET> cq = cb.createQuery(pc);
        Root<ET> root = cq.from(pc);
        cq.select(root);

        TypedQuery<ET> q = em.createQuery(cq);
        return q.getResultList();
    }

    @Override
    public List<ET> findAll() {
        EntityManager em = createEntityManager();
        try {
            return findAll(em);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ET> findByField(String fieldName, String value) {
        EntityManager em = createEntityManager();
        try {
            return findByField(em, fieldName, value);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ET> findByField(EntityManager em, String fieldName, String value) {
        LOG.debug("fieldName=" + fieldName + "; value=" + value);
        TypedQuery<ET> typedQuery = getTQByField(em, fieldName, value, true);
        List<ET> result = typedQuery.getResultList();
        LOG.debug("result=" + result);
        return result;
    }

    protected TypedQuery<ET> getTQByField(EntityManager em, String fieldName, String value, boolean likeWhereClause) {
        Class pc = getPersistentClass();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ET> query = cb.createQuery(pc);
        Root<ET> root = query.from(pc);
        Predicate whereClause;
        if (likeWhereClause) {
            whereClause = cb.like(root.get(fieldName), value);
        } else {
            whereClause = cb.equal(root.get(fieldName), value);
        }
        query.select(root).where(whereClause);
        TypedQuery<ET> typedQuery = em.createQuery(query);
        return typedQuery;
    }

    @Override
    public List<ET> getByField(String fieldName, String value) {
        EntityManager em = createEntityManager();
        try {
            return getByField(em, fieldName, value);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ET> getByField(EntityManager em, String fieldName, String value) {
        LOG.debug("fieldName=" + fieldName + "; value=" + value);
        TypedQuery<ET> typedQuery = getTQByField(em, fieldName, value, false);
        List<ET> result = typedQuery.getResultList();
        LOG.debug("result=" + result);
        return result;
    }

    @Override
    public boolean delete(ET entity) {
        EntityManager em = createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            boolean status = delete(em, entity);
            em.flush();
            tx.commit();
            return status;
        } catch (PersistenceException ex) {
            LOG.error("The transaction will rollback.", ex);
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(EntityManager em, ET entity) {
        LOG.debug("delete(), entity=" + entity);
        if (!em.contains(entity)) {
            entity = em.merge(entity);
        }
        em.remove(entity);
        return true;
    }

    @Override
    public boolean deleteById(ID key) {
        EntityManager em = createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            boolean status = deleteById(em, key);
            em.flush();
            tx.commit();
            return status;
        } catch (PersistenceException ex) {
            LOG.error("The transaction will rollback.", ex);
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteById(EntityManager em, ID key) {
        ET entity = findById(em, key);
        if (entity != null) {
            return delete(em, entity);
        }

        return false;
    }

    @Override
    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
