package com.angbe.soro.parc_auto.controllers;

import com.angbe.soro.parc_auto.models.Entretien;
import com.angbe.soro.parc_auto.models.Mission;
import com.angbe.soro.parc_auto.models.Vehicule;
import com.angbe.soro.parc_auto.services.EntretienService;
import com.angbe.soro.parc_auto.services.MissionService;
import com.angbe.soro.parc_auto.services.VehiculeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsController {

    @FXML
    private ComboBox<String> periodeCombo;

    // Labels pour les statistiques
    @FXML
    private Label totalVehiculesLabel;
    @FXML
    private Label vehiculesDisponiblesLabel;
    @FXML
    private Label totalMissionsLabel;
    @FXML
    private Label missionsEnCoursLabel;
    @FXML
    private Label totalEntretiensLabel;
    @FXML
    private Label coutTotalEntretiensLabel;

    // Graphiques
    @FXML
    private PieChart etatVehiculesChart;
    @FXML
    private PieChart repartitionCoutsChart;
    @FXML
    private LineChart<String, Number> evolutionCoutsChart;
    @FXML
    private PieChart repartitionMarqueChart;
    @FXML
    private BarChart<String, Number> ageVehiculesChart;
    @FXML
    private BarChart<String, Number> missionsParMoisChart;
    @FXML
    private LineChart<String, Number> coutMissionsChart;
    @FXML
    private PieChart typesEntretienChart;
    @FXML
    private BarChart<String, Number> coutEntretiensParVehiculeChart;

    // Repositories
    private VehiculeService vehiculeService;
    private MissionService missionService;
    private EntretienService entretienService;

    // Données filtrées par période
    private List<Vehicule> vehicules;
    private List<Mission> missions;
    private List<Entretien> entretiens;

    // Période sélectionnée
    private String periodeSelectionnee = "Cette année";

    /**
     * Initialise le contrôleur
     */
    @FXML
    public void initialize() {
        // Initialiser les repositories (à adapter selon votre architecture)
        vehiculeService = new VehiculeService();
        missionService = new MissionService();
        entretienService = new EntretienService();

        // Configurer le ComboBox des périodes
        periodeCombo.setItems(FXCollections.observableArrayList(
                "Cette année", "Cette semaine", "Ce mois", "Tous"
        ));
        periodeCombo.setValue(periodeSelectionnee);
        periodeCombo.setOnAction(e -> {
            periodeSelectionnee = periodeCombo.getValue();
            chargerDonnees();
            mettreAJourStatistiques();
        });

        // Charger les données initiales
        chargerDonnees();

        // Mettre à jour les statistiques et graphiques
        mettreAJourStatistiques();
    }

    /**
     * Charge les données selon la période sélectionnée
     */
    private void chargerDonnees() {
        // Déterminer les dates de début et fin selon la période
        Date dateDebut = null;
        Date dateFin = new Date(); // Date actuelle

        LocalDate aujourdhui = LocalDate.now();

        switch (periodeSelectionnee) {
            case "Cette semaine":
                // Début de la semaine (lundi)
                dateDebut = Date.from(aujourdhui.minusDays(aujourdhui.getDayOfWeek().getValue() - 1)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                break;
            case "Ce mois":
                // Début du mois
                dateDebut = Date.from(aujourdhui.withDayOfMonth(1)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                break;
            case "Cette année":
                // Début de l'année
                dateDebut = Date.from(aujourdhui.withDayOfYear(1)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
                break;
            case "Tous":
                // Aucune restriction de date
                dateDebut = null;
                break;
        }

        // Charger les données depuis les repositories
        if (dateDebut == null) {
            vehicules = vehiculeService.getAllVehicules();
            missions = missionService.getAllMissions();
            entretiens = entretienService.getAllEntretiens();
        } else {
            // Filtrer par date (à adapter selon vos méthodes de repository)
            vehicules = vehiculeService.getAllVehicules(); // Les véhicules n'ont pas besoin d'être filtrés par date
            missions = missionService.findByDateDebutBetween(dateDebut, dateFin);
            entretiens = entretienService.findByDateEntreeBetween(dateDebut, dateFin);
        }
    }

    /**
     * Met à jour toutes les statistiques et graphiques
     */
    private void mettreAJourStatistiques() {
        // Mettre à jour les labels
        mettreAJourLabels();

        // Mettre à jour les graphiques
        mettreAJourGraphiqueEtatVehicules();
        mettreAJourGraphiqueRepartitionCouts();
        mettreAJourGraphiqueEvolutionCouts();
        mettreAJourGraphiqueRepartitionMarque();
        mettreAJourGraphiqueAgeVehicules();
        mettreAJourGraphiqueMissionsParMois();
        mettreAJourGraphiqueCoutMissions();
        mettreAJourGraphiqueTypesEntretien();
        mettreAJourGraphiqueCoutEntretiensParVehicule();
    }

    /**
     * Met à jour les labels de statistiques
     */
    private void mettreAJourLabels() {
        // Nombre total de véhicules
        totalVehiculesLabel.setText(String.valueOf(vehicules.size()));

        // Nombre de véhicules disponibles
        long vehiculesDisponibles = vehicules.stream()
                .filter(v -> v.getEtatVoiture() != null && v.getEtatVoiture().getLibelleEtat().equalsIgnoreCase("Disponible"))
                .count();
        vehiculesDisponiblesLabel.setText(vehiculesDisponibles + " disponibles");

        // Nombre total de missions
        totalMissionsLabel.setText(String.valueOf(missions.size()));

        // Nombre de missions en cours
        long missionsEnCours = missions.stream()
                .filter(m -> m.getDateFin() == null || m.getDateFin().after(new Date()))
                .count();
        missionsEnCoursLabel.setText(missionsEnCours + " en cours");

        // Nombre total d'entretiens
        totalEntretiensLabel.setText(String.valueOf(entretiens.size()));

        // Coût total des entretiens
        int coutTotalEntretiens = entretiens.stream()
                .mapToInt(Entretien::getCout)
                .sum();
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        coutTotalEntretiensLabel.setText(format.format(coutTotalEntretiens) + " FCFA");
    }

    /**
     * Met à jour le graphique d'état des véhicules
     */
    private void mettreAJourGraphiqueEtatVehicules() {
        // Compter les véhicules par état
        Map<String, Long> etatCount = vehicules.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getEtatVoiture() != null ? v.getEtatVoiture().getLibelleEtat() : "Non défini",
                        Collectors.counting()
                ));

        // Créer les données pour le graphique
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        etatCount.forEach((etat, count) -> pieChartData.add(new PieChart.Data(etat + " (" + count + ")", count)));

        // Mettre à jour le graphique
        etatVehiculesChart.setData(pieChartData);
    }

    /**
     * Met à jour le graphique de répartition des coûts
     */
    private void mettreAJourGraphiqueRepartitionCouts() {
        // Calculer les coûts totaux
        int coutEntretiens = entretiens.stream().mapToInt(Entretien::getCout).sum();
        int coutMissions = missions.stream().mapToInt(Mission::getCout).sum();
        int coutCarburant = missions.stream().mapToInt(Mission::getCoutCarburant).sum();

        // Créer les données pour le graphique
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Entretiens (" + coutEntretiens + " FCFA)", coutEntretiens),
                new PieChart.Data("Missions (" + coutMissions + " FCFA)", coutMissions),
                new PieChart.Data("Carburant (" + coutCarburant + " FCFA)", coutCarburant)
        );

        // Mettre à jour le graphique
        repartitionCoutsChart.setData(pieChartData);
    }

    /**
     * Met à jour le graphique d'évolution des coûts
     */
    private void mettreAJourGraphiqueEvolutionCouts() {
        // Créer les séries de données
        XYChart.Series<String, Number> serieEntretiens = new XYChart.Series<>();
        serieEntretiens.setName("Entretiens");

        XYChart.Series<String, Number> serieMissions = new XYChart.Series<>();
        serieMissions.setName("Missions");

        XYChart.Series<String, Number> serieCarburant = new XYChart.Series<>();
        serieCarburant.setName("Carburant");

        // Obtenir les 12 derniers mois
        List<Month> derniersMois = new ArrayList<>();
        Month moisActuel = LocalDate.now().getMonth();

        for (int i = 0; i < 12; i++) {
            derniersMois.add(moisActuel.minus(i));
        }
        Collections.reverse(derniersMois);

        // Calculer les coûts par mois
        for (Month mois : derniersMois) {
            String nomMois = mois.getDisplayName(TextStyle.SHORT, Locale.FRANCE);

            // Coûts des entretiens pour ce mois
            int coutEntretiensMois = entretiens.stream()
                    .filter(e -> {
                        if (e.getDateEntree() == null) return false;
                        LocalDate date = e.getDateEntree().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return date.getMonth() == mois;
                    })
                    .mapToInt(Entretien::getCout)
                    .sum();
            serieEntretiens.getData().add(new XYChart.Data<>(nomMois, coutEntretiensMois));

            // Coûts des missions pour ce mois
            int coutMissionsMois = missions.stream()
                    .filter(m -> {
                        if (m.getDateDebut() == null) return false;
                        LocalDate date = m.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return date.getMonth() == mois;
                    })
                    .mapToInt(Mission::getCout)
                    .sum();
            serieMissions.getData().add(new XYChart.Data<>(nomMois, coutMissionsMois));

            // Coûts du carburant pour ce mois
            int coutCarburantMois = missions.stream()
                    .filter(m -> {
                        if (m.getDateDebut() == null) return false;
                        LocalDate date = m.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return date.getMonth() == mois;
                    })
                    .mapToInt(Mission::getCoutCarburant)
                    .sum();
            serieCarburant.getData().add(new XYChart.Data<>(nomMois, coutCarburantMois));
        }

        // Mettre à jour le graphique
        evolutionCoutsChart.getData().clear();
        evolutionCoutsChart.getData().addAll(serieEntretiens, serieMissions, serieCarburant);
    }

    /**
     * Met à jour le graphique de répartition par marque
     */
    private void mettreAJourGraphiqueRepartitionMarque() {
        // Compter les véhicules par marque
        Map<String, Long> marqueCount = vehicules.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getMarque() != null ? v.getMarque() : "Non défini",
                        Collectors.counting()
                ));

        // Créer les données pour le graphique
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        marqueCount.forEach((marque, count) -> pieChartData.add(new PieChart.Data(marque + " (" + count + ")", count)));

        // Mettre à jour le graphique
        repartitionMarqueChart.setData(pieChartData);
    }

    /**
     * Met à jour le graphique d'âge des véhicules
     */
    private void mettreAJourGraphiqueAgeVehicules() {
        // Créer la série de données
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Nombre de véhicules");

        // Calculer l'âge de chaque véhicule
        Map<Integer, Long> ageCount = new HashMap<>();
        LocalDate aujourdhui = LocalDate.now();

        for (Vehicule vehicule : vehicules) {
            if (vehicule.getDateMiseEnService() != null) {
                LocalDate dateMiseEnService = vehicule.getDateMiseEnService().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                int age = aujourdhui.getYear() - dateMiseEnService.getYear();

                ageCount.put(age, ageCount.getOrDefault(age, 0L) + 1);
            }
        }

        // Trier les âges
        List<Integer> agesTries = new ArrayList<>(ageCount.keySet());
        Collections.sort(agesTries);

        // Ajouter les données au graphique
        for (Integer age : agesTries) {
            serie.getData().add(new XYChart.Data<>(age + " ans", ageCount.get(age)));
        }

        // Mettre à jour le graphique
        ageVehiculesChart.getData().clear();
        ageVehiculesChart.getData().add(serie);
    }

    /**
     * Met à jour le graphique des missions par mois
     */
    private void mettreAJourGraphiqueMissionsParMois() {
        // Créer la série de données
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Nombre de missions");

        // Obtenir les 12 derniers mois
        List<Month> derniersMois = new ArrayList<>();
        Month moisActuel = LocalDate.now().getMonth();

        for (int i = 0; i < 12; i++) {
            derniersMois.add(moisActuel.minus(i));
        }
        Collections.reverse(derniersMois);

        // Compter les missions par mois
        for (Month mois : derniersMois) {
            String nomMois = mois.getDisplayName(TextStyle.SHORT, Locale.FRANCE);

            long countMissions = missions.stream()
                    .filter(m -> {
                        if (m.getDateDebut() == null) return false;
                        LocalDate date = m.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return date.getMonth() == mois;
                    })
                    .count();

            serie.getData().add(new XYChart.Data<>(nomMois, countMissions));
        }

        // Mettre à jour le graphique
        missionsParMoisChart.getData().clear();
        missionsParMoisChart.getData().add(serie);
    }

    /**
     * Met à jour le graphique du coût des missions
     */
    private void mettreAJourGraphiqueCoutMissions() {
        // Créer la série de données
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Coût des missions");

        // Obtenir les 12 derniers mois
        List<Month> derniersMois = new ArrayList<>();
        Month moisActuel = LocalDate.now().getMonth();

        for (int i = 0; i < 12; i++) {
            derniersMois.add(moisActuel.minus(i));
        }
        Collections.reverse(derniersMois);

        // Calculer le coût des missions par mois
        for (Month mois : derniersMois) {
            String nomMois = mois.getDisplayName(TextStyle.SHORT, Locale.FRANCE);

            int coutMissionsMois = missions.stream()
                    .filter(m -> {
                        if (m.getDateDebut() == null) return false;
                        LocalDate date = m.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return date.getMonth() == mois;
                    })
                    .mapToInt(m -> m.getCout() + m.getCoutCarburant())
                    .sum();

            serie.getData().add(new XYChart.Data<>(nomMois, coutMissionsMois));
        }

        // Mettre à jour le graphique
        coutMissionsChart.getData().clear();
        coutMissionsChart.getData().add(serie);
    }

    /**
     * Met à jour le graphique des types d'entretien
     */
    private void mettreAJourGraphiqueTypesEntretien() {
        // Compter les entretiens par motif
        Map<String, Long> motifCount = entretiens.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getMotif() != null ? e.getMotif() : "Non défini",
                        Collectors.counting()
                ));

        // Créer les données pour le graphique
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        motifCount.forEach((motif, count) -> {
            // Limiter la longueur du motif pour l'affichage
            String motifAffiche = motif.length() > 20 ? motif.substring(0, 17) + "..." : motif;
            pieChartData.add(new PieChart.Data(motifAffiche + " (" + count + ")", count));
        });

        // Mettre à jour le graphique
        typesEntretienChart.setData(pieChartData);
    }

    /**
     * Met à jour le graphique du coût des entretiens par véhicule
     */
    private void mettreAJourGraphiqueCoutEntretiensParVehicule() {
        // Calculer le coût total des entretiens par véhicule
        Map<Vehicule, Integer> coutParVehicule = new HashMap<>();

        for (Entretien entretien : entretiens) {
            if (entretien.getVehicule() != null) {
                coutParVehicule.put(
                        entretien.getVehicule(),
                        coutParVehicule.getOrDefault(entretien.getVehicule(), 0) + entretien.getCout()
                );
            }
        }

        // Trier les véhicules par coût d'entretien (décroissant)
        List<Map.Entry<Vehicule, Integer>> vehiculesTries = new ArrayList<>(coutParVehicule.entrySet());
        vehiculesTries.sort(Map.Entry.<Vehicule, Integer>comparingByValue().reversed());

        // Limiter à 10 véhicules pour la lisibilité
        List<Map.Entry<Vehicule, Integer>> top10Vehicules = vehiculesTries.stream()
                .limit(10)
                .collect(Collectors.toList());

        // Créer la série de données
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Coût des entretiens");

        // Ajouter les données au graphique
        for (Map.Entry<Vehicule, Integer> entry : top10Vehicules) {
            Vehicule vehicule = entry.getKey();
            Integer cout = entry.getValue();

            String label = vehicule.getImmatriculation() + " (" + vehicule.getMarque() + " " + vehicule.getModele() + ")";
            serie.getData().add(new XYChart.Data<>(label, cout));
        }

        // Mettre à jour le graphique
        coutEntretiensParVehiculeChart.getData().clear();
        coutEntretiensParVehiculeChart.getData().add(serie);
    }

    /**
     * Génère un rapport PDF des statistiques
     */
    @FXML
    public void genererRapportPDF() {
        // Implémenter la génération de rapport PDF
        // Vous pouvez utiliser une bibliothèque comme iText ou Apache PDFBox

        // Exemple simple avec une boîte de dialogue de sauvegarde
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le rapport PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        fileChooser.setInitialFileName("rapport_parc_auto_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        File file = fileChooser.showSaveDialog(periodeCombo.getScene().getWindow());
        if (file != null) {
            // Implémenter la génération du PDF ici
            // Pour l'instant, afficher un message de succès fictif
            System.out.println("Rapport PDF généré : " + file.getAbsolutePath());
        }
    }

    /**
     * Exporte les données des statistiques
     */
    @FXML
    public void exporterDonnees() {
        // Implémenter l'exportation des données (CSV, Excel, etc.)
        // Vous pouvez utiliser votre classe PrintExportUtils existante

        // Exemple simple avec une boîte de dialogue de sauvegarde
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les données");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
        fileChooser.setInitialFileName("statistiques_parc_auto_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        File file = fileChooser.showSaveDialog(periodeCombo.getScene().getWindow());
        if (file != null) {
            // Implémenter l'exportation des données ici
            // Pour l'instant, afficher un message de succès fictif
            System.out.println("Données exportées : " + file.getAbsolutePath());
        }
    }

    /**
     * Génère un rapport en fonction du bouton d'exportation dans l'en-tête
     */
    @FXML
    public void genererRapport() {
        // Appeler la méthode de génération de rapport PDF
        genererRapportPDF();
    }
}