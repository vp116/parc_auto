package com.angbe.soro.parc_auto.models;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ASSURANCE")
public class Assurance {
    @Id
    @Column(name = "NUM_CARTE_ASSURANCE")
    private int numCarteAssurance;

    @Column(name = "DATE_DEBUT_ASSURANCE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebutAssurance;

    @Column(name = "DATE_FIN_ASSURANCE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFinAssurance;

    @Column(name = "AGENCE", length = 100)
    private String agence;

    @Column(name = "COUT_ASSURANCE")
    private Double coutAssurance;

    @OneToMany(mappedBy = "assurance")
    private Set<Couvrir> couvertures;

    public Assurance() {

    }

    public int getNumCarteAssurance() {
        return numCarteAssurance;
    }

    public void setNumCarteAssurance(int numCarteAssurance) {
        this.numCarteAssurance = numCarteAssurance;
    }

    public Date getDateDebutAssurance() {
        return dateDebutAssurance;
    }

    public void setDateDebutAssurance(Date dateDebutAssurance) {
        this.dateDebutAssurance = dateDebutAssurance;
    }

    public Date getDateFinAssurance() {
        return dateFinAssurance;
    }

    public void setDateFinAssurance(Date dateFinAssurance) {
        this.dateFinAssurance = dateFinAssurance;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public Double getCoutAssurance() {
        return coutAssurance;
    }

    public void setCoutAssurance(Double coutAssurance) {
        this.coutAssurance = coutAssurance;
    }

    public Set<Couvrir> getCouvertures() {
        return couvertures;
    }

    public void setCouvertures(Set<Couvrir> couvertures) {
        this.couvertures = couvertures;
    }

}


