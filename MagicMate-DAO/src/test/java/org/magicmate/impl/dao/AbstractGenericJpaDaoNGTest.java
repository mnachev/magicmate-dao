/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.commons.lang3.reflect.FieldUtils;

import org.magicmate.impl.data.model.Detail;
import org.magicmate.impl.data.model.Master;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * https://wiki.eclipse.org/EclipseLink/Examples
 * http://www.eclipse.org/eclipselink/documentation/2.7/jpa/extensions/persistenceproperties_ref.htm
 */
/**
 *
 * @author mnachev
 */
public class AbstractGenericJpaDaoNGTest {

    private static MasterJpaDao masterJpaDao;
    private Integer masterOneId;
    private Integer masterTwoId;
    private Integer masterThreeId;

    public AbstractGenericJpaDaoNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        masterJpaDao = new MasterJpaDao();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (masterJpaDao != null) {
            masterJpaDao.close();
            masterJpaDao = null;
        }
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of createEntityManager method, of class AbstractGenericJpaDao.
     */
    @Test
    public void testCreateEntityManager() {
        System.out.println("createEntityManager");
        EntityManager em = masterJpaDao.createEntityManager();
        assertNotNull(em);
        em.close();
    }

    /**
     * Test of Database schema generation - JPA to Database
     */
    @Test(dependsOnMethods = {"testCreateEntityManager"})
    public void testDatabaseSchemaGeneration() {
        System.out.println("Database schema generation");
        EntityManager em = masterJpaDao.createEntityManager();
        checkTable(em, Master.class);
        checkTable(em, Detail.class);
        em.close();
    }

    private void checkTable(EntityManager em, Class<? extends Serializable> entityClass) {
        Table tableAnnot = (Table) entityClass.getAnnotation(Table.class);
        String tableName = tableAnnot.name();
        Query q = em.createNativeQuery("show columns from " + tableName + ";");
        List<Object[]> resList = q.getResultList();
//        obj=[MASTER_ID, INTEGER(10), NO, PRI, NULL]
//        obj=[MASTER_CODE, VARCHAR(16), NO, UNI, NULL]
//        obj=[MASTER_NAME, VARCHAR(64), NO, UNI, NULL]

//        arr=[DETAIL_ID, INTEGER(10), NO, PRI, NULL]
//        arr=[DETAIL_CODE, VARCHAR(16), NO, UNI, NULL]
//        arr=[DOCUMENT_DATE, DATE(10), YES, , NULL]
//        arr=[MASTER_ID, INTEGER(10), NO, UNI, NULL]
        TreeSet<String> tableColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Object arr[] : resList) {
            System.out.println("arr=" + Arrays.toString(arr));
            String columnName = (String) arr[0];
            System.out.println("columnName=" + columnName);
            tableColumns.add(columnName);
        }
        System.out.println("tableColumns=" + tableColumns);

//        @Column(name = "DETAIL_ID", nullable = false, updatable = false)
//        @JoinColumn(name = "MASTER_ID", referencedColumnName = "MASTER_ID", nullable = false)
        TreeSet<String> entityColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        HashSet<Field> fields = new HashSet<>();
        fields.addAll(FieldUtils.getFieldsListWithAnnotation(entityClass, Column.class));
        fields.addAll(FieldUtils.getFieldsListWithAnnotation(entityClass, JoinColumn.class));
        for (Field field : fields) {
            System.out.println("field=" + field);
            Column column = field.getAnnotation(Column.class);
            System.out.println("column=" + column);
            if (column != null) {
                String columnName = column.name();
                System.out.println("column.name()=" + columnName);
                entityColumns.add(columnName);
            } else {
                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                System.out.println("joinColumn=" + joinColumn);
                String columnName = joinColumn.name();
                System.out.println("joinColumn.name()=" + columnName);
                entityColumns.add(columnName);
            }
        }
        System.out.println("entityColumns=" + entityColumns);
        assertTrue(tableColumns.containsAll(entityColumns));
        assertTrue(entityColumns.containsAll(tableColumns));
    }

    /**
     * Test of persist method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testDatabaseSchemaGeneration"})
    public void testPersist() {
        System.out.println("persist");
        Master master = new Master();
        master.setMasterCode("1");
        master.setMasterName("The 1st master item from many");
        master = masterJpaDao.persist(master);
        System.out.println("master=" + master);
        masterOneId = master.getMasterId();
        assertNotNull(masterOneId);
    }

    /**
     * Test of persistAll method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testPersist"})
    public void testPersistAll() {
        System.out.println("persistAll");

        Master m1 = new Master();
        m1.setMasterCode("2");
        m1.setMasterName("The 2nd master item");

        Master m2 = new Master();
        m2.setMasterCode("3");
        m2.setMasterName("The 3rd master item");

        Master[] masters = masterJpaDao.persistAll(m1, m2);
        System.out.println("masters=" + Arrays.asList(masters));
        assertEquals(masters.length, 2);
        for (Master m : masters) {
            Integer id = m.getMasterId();
            assertNotNull(id);
            switch (m.getMasterCode()) {
                case "2":
                    masterTwoId = m.getMasterId();
                    break;

                case "3":
                    masterThreeId = m.getMasterId();
                    break;
            }
        }
    }

    /**
     * Test of findById method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testPersistAll"})
    public void testFindById() {
        System.out.println("findById");
        Master master = masterJpaDao.findById(masterOneId);
        System.out.println("master=" + master);
        Integer id = master.getMasterId();
        assertNotNull(id);
    }

    /**
     * Test of findAll method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testFindById"})
    public void testFindAll() {
        System.out.println("findAll");

        List<Master> masters = masterJpaDao.findAll();
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 3);
    }

    /**
     * Test of findByField method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testFindAll"})
    public void testFindByField() {
        System.out.println("findByField");

        List<Master> masters = masterJpaDao.findByField("masterName", "The 2nd master item");
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 1);

        masters = masterJpaDao.findByField("masterName", "%master item");
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 2);

        masters = masterJpaDao.findByField("masterName", "%master item%");
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 3);
    }

    /**
     * Test of getByField method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testFindByField"})
    public void testGetByField() {
        System.out.println("getByField");

        List<Master> masters = masterJpaDao.getByField("masterName", "The 2nd master item");
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 1);

        masters = masterJpaDao.getByField("masterName", "%master item");
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 0);
    }

    /**
     * Test of deleteById method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testGetByField"})
    public void testDeleteById() {
        System.out.println("deleteById");

        boolean status = masterJpaDao.deleteById(99);
        assertFalse(status);

        status = masterJpaDao.deleteById(masterTwoId);
        assertTrue(status);

        List<Master> masters = masterJpaDao.findAll();
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 2);
    }

    /**
     * Test of delete method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testDeleteById"})
    public void testDelete() {
        System.out.println("delete");

        Master m = new Master();
        m.setMasterId(masterThreeId);
        m.setMasterCode("3");
        m.setMasterName("The 3rd master item");

        boolean status = masterJpaDao.delete(m);
        assertTrue(status);

        List<Master> masters = masterJpaDao.findAll();
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 1);

        m = masterJpaDao.findById(masterOneId);
        assertNotNull(m);

        status = masterJpaDao.delete(m);
        assertTrue(status);

        masters = masterJpaDao.findAll();
        System.out.println("masters=" + masters);
        assertEquals(masters.size(), 0);
    }

    /**
     * Test of close method, of class AbstractGenericJpaDao.
     */
    @Test(dependsOnMethods = {"testDelete"})
    public void testClose() {
        System.out.println("close");
        masterJpaDao.close();
    }
}
