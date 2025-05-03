package com.angbe.soro.parc_auto.models;

import jakarta.persistence.*;

@Entity
@Table(name = "PARTICIPER")
@IdClass(ParticiperId.class)
public class Participer {
    @Id
    @ManyToOne
    @JoinColumn(name = "ID_PERSONNEL")
    private Personnel personnel;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_MISSION")
    private Mission mission;

    public Participer() {

    }

    public Participer(Personnel personnel, Mission mission) {
        this.personnel = personnel;
        this.mission = mission;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

}
