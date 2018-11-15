/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import org.magicmate.dao.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mnachev
 */
public abstract class AbstractGenericDao<ET extends Serializable, ID extends Serializable> implements GenericDao<ET, ID> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractGenericDao.class);

    private Class<ET> persistentClass;

    @Override
    public Class<ET> getPersistentClass() {
        if (persistentClass == null) {
            Class daoClass = getClass();
            Type daoType = daoClass.getGenericSuperclass();
            LOG.debug("DAO Class=" + daoClass + "; DAO Type=" + daoType);
            Type typeArgs[] = ((ParameterizedType) daoType).getActualTypeArguments();
            LOG.debug("typeArgs=" + Arrays.toString(typeArgs));
            this.persistentClass = (Class<ET>) typeArgs[0];
            LOG.debug("persistentClass=" + persistentClass);
        }

        return persistentClass;
    }

}
