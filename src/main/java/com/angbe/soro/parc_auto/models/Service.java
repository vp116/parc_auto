package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "SERVICE")
public class Service {
    @Id
    @Column(name = "ID_SERVICE")
    private int idService;

    @Column(name = "LIB_SERVICE", length = 30)
    private String libelle;

    @Column(name = "LOCALISATION_SERVICE", length = 50)
    private String localisation;

    @OneToMany(mappedBy = "service")
    private Set<Personnel> personnels;

    public Service() {
    }

    public Service(int idService, String libelle, String localisation) {
        this.idService = idService;
        this.libelle = libelle;
        this.localisation = localisation;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
}
