/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.data.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mnachev
 */
@Entity
@Table(name = "Master", schema = "PUBLIC", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"MASTER_CODE"}),
    @UniqueConstraint(columnNames = {"MASTER_NAME"})})
@XmlRootElement
public class Master implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Gen_Master")
    @SequenceGenerator(name = "Gen_Master", sequenceName = "master_seq", allocationSize = 10)
    @Basic(optional = false)
    @Column(name = "MASTER_ID", nullable = false, updatable = false)
    private Integer masterId;

    @Basic(optional = false)
    @Column(name = "MASTER_CODE", nullable = false, length = 16)
    private String masterCode;

    @Basic(optional = false)
    @Column(name = "MASTER_NAME", nullable = false, length = 64)
    private String masterName;

    @JoinTable
    @OneToMany
    private List<Detail> details;

    public Master() {
    }

    public Master(String masterCode, String masterName) {
        this.masterCode = masterCode;
        this.masterName = masterName;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public String getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    @XmlTransient
    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Master{" + "masterId=" + masterId + ", masterCode=" + masterCode + ", masterName=" + masterName + '}' + ": " + super.toString();
    }
}
