package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "MISSION")
public class Mission {
    @Id
    @Column(name = "ID_MISSION")
    private int idMission;

    @ManyToOne
    @JoinColumn(name = "ID_VEHICULE")
    private Vehicule vehicule;

    @Column(name = "LIB_MISSION", length = 100)
    private String libelle;

    @Column(name = "DATE_DEBUT_MISSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;

    @Column(name = "DATE_FIN_MISSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;

    @Column(name = "COUT_MISSION")
    private Integer cout;

    @Column(name = "COUT_CARBURANT")
    private Integer coutCarburant;

    @Column(name = "OBSERVATION_MISSION", length = 200)
    private String observation;

    @Column(name = "CIRCUIT_MISSION", length = 200)
    private String circuit;

    @OneToMany(mappedBy = "mission")
    private Set<Participer> participants;

    public int getIdMission() {
        return idMission;
    }

    public void setIdMission(int idMission) {
        this.idMission = idMission;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getCout() {
        return cout;
    }

    public void setCout(Integer cout) {
        this.cout = cout;
    }

    public Integer getCoutCarburant() {
        return coutCarburant;
    }

    public void setCoutCarburant(Integer coutCarburant) {
        this.coutCarburant = coutCarburant;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public Set<Participer> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participer> participants) {
        this.participants = participants;
    }
}
