package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "ETAT_VOITURE")
public class EtatVoiture {
    @Id
    @Column(name = "ID_ETAT_VOITURE")
    private int idEtatVoiture;

    @Column(name = "LIB_ETAT_VOITURE", length = 20)
    private String libelleEtat;

    @OneToMany(mappedBy = "etatVoiture")
    private Set<Vehicule> vehicules;

    public EtatVoiture() {

    }

    public EtatVoiture(int id, String libelleEtat) {
        this.idEtatVoiture = id;
        this.libelleEtat = libelleEtat;

    }

    public int getIdEtatVoiture() {
        return idEtatVoiture;
    }

    public void setIdEtatVoiture(int idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    public String getLibelleEtat() {
        return libelleEtat;
    }

    public void setLibelleEtat(String libelleEtat) {
        this.libelleEtat = libelleEtat;
    }
}
