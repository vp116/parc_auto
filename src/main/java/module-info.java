module com.angbe.soro.parc_auto {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    requires java.naming;
    requires com.cardosama.fontawesome_fx_6;

    // Ouvrir les packages auxquels Hibernate a besoin d'accéder
    opens com.angbe.soro.parc_auto.models to org.hibernate.orm.core;

    exports com.angbe.soro.parc_auto.models;
    exports com.angbe.soro.parc_auto.repository;
    exports com.angbe.soro.parc_auto.services;
    exports com.angbe.soro.parc_auto.controllers;
    exports com.angbe.soro.parc_auto.components;

    opens com.angbe.soro.parc_auto to javafx.fxml;
    opens com.angbe.soro.parc_auto.controllers to javafx.fxml;
    opens com.angbe.soro.parc_auto.views to javafx.fxml;
    opens com.angbe.soro.parc_auto.components to javafx.fxml;
    exports com.angbe.soro.parc_auto;
    exports com.angbe.soro.parc_auto.views;
}