/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.magicmate.impl.data.model.Detail;
import org.magicmate.impl.data.model.Master;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author mnachev
 */
public class MasterDetailRelationshipsNGTest {

    private static MasterJpaDao masterJpaDao;
    private static DetailJpaDao detailJpaDao;

    @BeforeClass
    public static void setUpClass() throws Exception {
        masterJpaDao = new MasterJpaDao();
        detailJpaDao = new DetailJpaDao();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (masterJpaDao != null) {
            masterJpaDao.close();
            masterJpaDao = null;
        }

        if (detailJpaDao != null) {
            detailJpaDao.close();
            detailJpaDao = null;
        }
    }

    /**
     * Test of Master-Detail Relationships
     */
    @Test
    public void testMasterDetailRelationships() {
        System.out.println("Master-Detail Relationships");

        Master m1 = new Master();
        m1.setMasterCode("1");
        m1.setMasterName("The 1st master item from many");
        m1 = masterJpaDao.persist(m1);
        System.out.println("m1=" + m1);
        Integer id = m1.getMasterId();
        assertNotNull(id);

        Detail d1 = new Detail();
        d1.setDetailCode("1");
        d1.setMaster(m1);
        detailJpaDao.persist(d1);
        System.out.println("d1=" + d1);

        m1 = masterJpaDao.findById(id);
        System.out.println("m1=" + m1);
        List<Detail> details = m1.getDetails();
        System.out.println("details.getClass()=" + details.getClass());
        System.out.println("details.size()=" + details.size());
        
        System.out.println("m1.getDetails()=" + details);

        List<Master> masters = masterJpaDao.findAll();
        for(Master m : masters) {
            System.out.println("m=" + m);
            System.out.println("m.getDetails()=" + m.getDetails());
        }

        EntityManager em = masterJpaDao.createEntityManager();
        m1 = em.merge(m1);
        em.close();
        System.out.println("m1=" + m1);
        System.out.println("m1.getDetails()=" + m1.getDetails());
    }
}
