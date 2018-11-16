/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.dao;

import org.magicmate.impl.data.model.Detail;

/**
 *
 * @author mnachev
 */
public class DetailJpaDao extends AbstractGenericJpaDao<Detail, Integer> {

    public DetailJpaDao() {
        super("GenericJpaDao");
    }

}
