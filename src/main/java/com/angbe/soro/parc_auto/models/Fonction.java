package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "FONCTION")
public class Fonction {
    @Id
    @Column(name = "ID_FONCTION")
    private int idFonction;

    @Column(name = "LIB_FONCTION", length = 50)
    private String libelleFonction;

    @OneToMany(mappedBy = "fonction")
    private Set<Personnel> personnels;

    public Fonction() {

    }

    public Fonction(int idFonction, String libelleFonction) {
        this.idFonction = idFonction;
        this.libelleFonction = libelleFonction;
    }

    public int getIdFonction() {
        return idFonction;
    }

    public void setIdFonction(int idFonction) {
        this.idFonction = idFonction;
    }

    public String getLibelleFonction() {
        return libelleFonction;
    }

    public void setLibelleFonction(String libelleFonction) {
        this.libelleFonction = libelleFonction;
    }

}
