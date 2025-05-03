package com.angbe.soro.parc_auto.models;

import java.io.Serializable;
import java.util.Objects;

// Classe pour la clé composite
class ParticiperId implements Serializable {
    private int personnel;
    private int mission;

    public ParticiperId(int personnel, int mission) {
        this.personnel = personnel;
        this.mission = mission;
    }

    public ParticiperId() {
    }

    public int getPersonnel() {
        return personnel;
    }

    public void setPersonnel(int personnel) {
        this.personnel = personnel;
    }

    public int getMission() {
        return mission;
    }

    public void setMission(int mission) {
        this.mission = mission;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParticiperId that = (ParticiperId) o;
        return personnel == that.personnel && mission == that.mission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personnel, mission);
    }
}
