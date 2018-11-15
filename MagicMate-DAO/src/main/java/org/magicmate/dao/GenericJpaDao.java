/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author mnachev
 */
public interface GenericJpaDao<ET extends Serializable, ID extends Serializable> extends GenericDao<ET, ID> {

    public String getPersistenceUnitName();

    public void setPersistenceUnitName(String persistenceUnitName);

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory);

    public EntityManager createEntityManager();

    public ET persist(EntityManager em, ET entity);

    public ET[] persistAll(EntityManager em, ET... entities);

    public Collection<ET> persistAll(EntityManager em, Collection<ET> entities);

    public ET findById(EntityManager em, ID key);

    public List<ET> findAll(EntityManager em);

    public List<ET> findByField(EntityManager em, String fieldName, String value);

    public List<ET> getByField(EntityManager em, String fieldName, String value);

    public boolean delete(EntityManager em, ET entity);

    public boolean deleteById(EntityManager em, ID key);

}
