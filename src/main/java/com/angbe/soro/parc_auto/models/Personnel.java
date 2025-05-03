package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "PERSONNEL")
public class Personnel {
    @Id
    @Column(name = "ID_PERSONNEL")
    private int idPersonnel;

    @ManyToOne
    @JoinColumn(name = "ID_VEHICULE")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "ID_FONCTION")
    private Fonction fonction;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICE")
    private Service service;

    @Column(name = "NOM_PERSONNEL", length = 50)
    private String nom;

    @Column(name = "PRENOM_PERSONNEL", length = 100)
    private String prenom;

    @Column(name = "CONTACT_PERSONNEL", length = 15)
    private String contact;

    @Column(name = "DATE_ATTRIBUTION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAttribution;

    @OneToMany(mappedBy = "personnel")
    private Set<Participer> participations;

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(int idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Fonction getFonction() {
        return fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(Date dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

}
