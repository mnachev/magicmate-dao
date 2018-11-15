/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.dao;

import org.magicmate.impl.data.model.Master;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author mnachev
 */
public class AbstractGenericDaoNGTest {

    /**
     * Test of getPersistentClass method, of class AbstractGenericDao.
     */
    @Test
    public void testGetPersistentClass() {
        System.out.println("getPersistentClass");
        MasterJpaDao instance = new MasterJpaDao();
        Class expResult = Master.class;
        Class result = instance.getPersistentClass();
        assertEquals(result, expResult);
    }

}
