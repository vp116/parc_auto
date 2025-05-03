package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ENTRETIEN")
public class Entretien {
    @Id
    @Column(name = "ID_ENTRETIEN")
    private int idEntretien;

    @ManyToOne
    @JoinColumn(name = "ID_VEHICULE")
    private Vehicule vehicule;

    @Column(name = "DATE_ENTREE_ENTR")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntree;

    @Column(name = "DATE_SORTIE_ENTR")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSortie;

    @Column(name = "MOTIF_ENTR", length = 100)
    private String motif;

    @Column(name = "OBSERVATION")
    private String observation;

    @Column(name = "COUT_ENTR")
    private Integer cout;

    @Column(name = "LIEU_ENTR", length = 70)
    private String lieu;

    public Entretien() {

    }

    public int getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(int idEntretien) {
        this.idEntretien = idEntretien;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(Date dateEntree) {
        this.dateEntree = dateEntree;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getCout() {
        return cout;
    }

    public void setCout(Integer cout) {
        this.cout = cout;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

}
