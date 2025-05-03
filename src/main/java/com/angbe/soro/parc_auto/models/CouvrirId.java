package com.angbe.soro.parc_auto.models;

import java.util.Objects;

public class CouvrirId {
    private int vehicule;
    private int assurance;

    public CouvrirId(int vehicule, int assurance) {
        this.vehicule = vehicule;
        this.assurance = assurance;
    }

    public CouvrirId() {

    }


    public int getVehicule() {
        return vehicule;
    }

    public void setVehicule(int vehicule) {
        this.vehicule = vehicule;
    }

    public int getAssurance() {
        return assurance;
    }

    public void setAssurance(int assurance) {
        this.assurance = assurance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CouvrirId couvrirId = (CouvrirId) o;
        return vehicule == couvrirId.vehicule && assurance == couvrirId.assurance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicule, assurance);
    }
}
