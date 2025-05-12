package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

@Entity
@Table(name = "UTILISATEURS")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_UTILISATEUR")
    private int id_utilisateur;

    @Column(name = "NOM", length = 100)
    private String nom;

    @Column(name = "PRENOM", length = 100)
    private String prenom;

    @Column(name = "EMAIL", unique = true, length = 150)
    private String email;

    @Column(name = "PASSWORD", length = 255)
    private String password;

    @Column(name = "ROLE", length = 50)
    private String role;

    @Column(name = "ACTIF")
    private boolean actif = true;

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getMotDePasse() {
        return password;
    }

    public void setMotDePasse(String motDePasse) {
        this.password = motDePasse;
    }
}
