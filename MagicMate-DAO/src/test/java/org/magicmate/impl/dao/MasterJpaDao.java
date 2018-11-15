/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.dao;

import org.magicmate.impl.dao.AbstractGenericJpaDao;
import org.magicmate.impl.data.model.Master;

/**
 *
 * @author mnachev
 */
public class MasterJpaDao extends AbstractGenericJpaDao<Master, Integer> {

    public MasterJpaDao() {
        super("GenericJpaDao");
    }
}
