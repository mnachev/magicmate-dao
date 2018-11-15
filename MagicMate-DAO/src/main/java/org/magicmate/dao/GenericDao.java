/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.dao;

import java.io.Closeable;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mnachev
 */
public interface GenericDao<ET extends Serializable, ID extends Serializable> extends Closeable {

    public Class<ET> getPersistentClass();

    public List<ET> findAll();

    public ET findById(ID key);

    public List<ET> findByField(String fieldName, String value);

    public List<ET> getByField(String fieldName, String value);

    public ET persist(ET entity);

    public ET[] persistAll(ET... entities);

    public Collection<ET> persistAll(Collection<ET> entities);

    public boolean delete(ET entity);

    public boolean deleteById(ID key);
}
