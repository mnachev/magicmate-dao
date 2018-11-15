/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.magicmate.impl.data.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mnachev
 */
@Entity
@Table(name = "Detail", schema = "PUBLIC", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"MASTER_ID", "DETAIL_CODE"})})
@XmlRootElement
public class Detail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Gen_Detail")
    @SequenceGenerator(name = "Gen_Detail", sequenceName = "detail_seq", allocationSize = 10)
    @Basic(optional = false)
    @Column(name = "DETAIL_ID", nullable = false, updatable = false)
    private Integer detailId;

    @Column(name = "DOCUMENT_DATE")
    private LocalDate documentDate;

    @JoinColumn(name = "MASTER_ID", referencedColumnName = "MASTER_ID", nullable = false)
    @ManyToOne
    private Master master;

    @Basic(optional = false)
    @Column(name = "DETAIL_CODE", nullable = false, length = 16)
    private String detailCode;

    public Detail() {
    }

    public Detail(Integer detailId, LocalDate documentDate, Master master, String detailCode) {
        this.detailId = detailId;
        this.documentDate = documentDate;
        this.master = master;
        this.detailCode = detailCode;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode;
    }

}
