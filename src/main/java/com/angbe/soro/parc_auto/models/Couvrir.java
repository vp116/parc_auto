package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;


@Entity
@Table(name = "COUVRIR")
@IdClass(CouvrirId.class)
public class Couvrir {
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_VEHICULE")
    private Vehicule vehicule;

    @Id
    @ManyToOne
    @JoinColumn(name = "NUM_CARTE_ASSURANCE")
    private Assurance assurance;

    public Couvrir() {}
    public Couvrir(Vehicule v, Assurance a) {
        vehicule = v;
        assurance = a;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }
}
