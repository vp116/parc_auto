package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "VEHICULES")
public class Vehicule {
    @Id
    @Column(name = "ID_VEHICULE")
    private int idVehicule;

    @ManyToOne
    @JoinColumn(name = "ID_ETAT_VOITURE")
    private EtatVoiture etatVoiture;

    @Column(name = "NUMERO_CHASSI", length = 20)
    private String numeroChassi;

    @Column(name = "IMMATRICULATION", length = 20)
    private String immatriculation;

    @Column(name = "MARQUE", length = 30)
    private String marque;

    @Column(name = "MODELE", length = 30)
    private String modele;

    @Column(name = "NB_PLACES")
    private Integer nbPlaces;

    @Column(name = "ENERGIE", length = 25)
    private String energie;

    @Column(name = "DATE_ACQUISITION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAcquisition;

    @Column(name = "DATE_AMMORTISSEMENT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAmmortissement;

    @Column(name = "DATE_MISE_EN_SERVICE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateMiseEnService;

    @Column(name = "PUISSANCE")
    private Integer puissance;

    @Column(name = "COULEUR", length = 20)
    private String couleur;

    @Column(name = "PRIX_VEHICULE")
    private Integer prix;

    @Column(name = "DATE_ETAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEtat;

    @OneToMany(mappedBy = "vehicule")
    private Set<Entretien> entretiens;

    @OneToMany(mappedBy = "vehicule")
    private Set<Mission> missions;

    @OneToMany(mappedBy = "vehicule")
    private Set<Couvrir> assurances;

    @OneToMany(mappedBy = "vehicule")
    private Set<Personnel> personnels;

    public Date getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(Date dateEtat) {
        this.dateEtat = dateEtat;
    }

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public Integer getPuissance() {
        return puissance;
    }

    public void setPuissance(Integer puissance) {
        this.puissance = puissance;
    }

    public Date getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(Date dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public Date getDateAmmortissement() {
        return dateAmmortissement;
    }

    public void setDateAmmortissement(Date dateAmmortissement) {
        this.dateAmmortissement = dateAmmortissement;
    }

    public Date getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(Date dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public String getEnergie() {
        return energie;
    }

    public void setEnergie(String energie) {
        this.energie = energie;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getNumeroChassi() {
        return numeroChassi;
    }

    public void setNumeroChassi(String numeroChassi) {
        this.numeroChassi = numeroChassi;
    }

    public EtatVoiture getEtatVoiture() {
        return etatVoiture;
    }

    public void setEtatVoiture(EtatVoiture etatVoiture) {
        this.etatVoiture = etatVoiture;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

}
