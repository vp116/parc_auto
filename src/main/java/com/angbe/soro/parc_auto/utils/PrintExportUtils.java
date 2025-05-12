package com.angbe.soro.parc_auto.utils;

import com.angbe.soro.parc_auto.components.DynamicTableCard;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Classe utilitaire fournissant des méthodes statiques pour l'impression et l'exportation de données
 * dans toute l'application.
 * <p>
 * Cette classe offre des fonctionnalités pour :
 * - Exporter des données de tableaux vers différents formats (CSV, etc.)
 * - Imprimer des tableaux et autres contenus
 * - Générer des rapports basiques
 * <p>
 * Toutes les méthodes sont statiques et peuvent être utilisées directement sans instanciation.
 */
public class PrintExportUtils {

    /**
     * Exporte les données d'un tableau vers un fichier CSV ou Excel.
     *
     * @param <T>          Type des éléments du tableau
     * @param tableCard    Le DynamicTableCard contenant les données à exporter
     * @param parentWindow La fenêtre parente pour afficher les dialogues
     * @param title        Titre du dialogue d'exportation
     */
    public static <T> void exportTableData(DynamicTableCard<T> tableCard, Window parentWindow, String title) {
        try {
            // Créer un sélecteur de fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(title != null ? title : "Exporter les données");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV", "*.csv"),
                    new FileChooser.ExtensionFilter("Excel", "*.xlsx")
            );

            // Définir un nom de fichier par défaut avec la date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String defaultFileName = "export_" + dateFormat.format(new Date());
            fileChooser.setInitialFileName(defaultFileName);

            // Afficher le dialogue de sauvegarde
            File file = fileChooser.showSaveDialog(parentWindow);

            if (file != null) {
                String extension = getFileExtension(file.getName()).toLowerCase();

                if (extension.equals("csv")) {
                    exportToCSV(tableCard, file);
                } else if (extension.equals("xlsx")) {
                    // Pour l'exportation Excel, vous pourriez utiliser une bibliothèque comme Apache POI
                    // Cette implémentation est simplifiée et exporte en CSV même pour xlsx
                    exportToCSV(tableCard, file);
                }

                // Afficher un message de succès
                showSuccessAlert("Exportation réussie",
                        "Les données ont été exportées avec succès vers " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showErrorAlert("Erreur d'exportation",
                    "Une erreur s'est produite lors de l'exportation: " + e.getMessage());
        }
    }

    /**
     * Exporte les données d'un tableau vers un fichier CSV.
     *
     * @param <T>       Type des éléments du tableau
     * @param tableCard Le DynamicTableCard contenant les données à exporter
     * @param file      Le fichier de destination
     * @throws IOException Si une erreur se produit lors de l'écriture du fichier
     */
    private static <T> void exportToCSV(DynamicTableCard<T> tableCard, File file) throws IOException {
        TableView<T> tableView = tableCard.getTableView();
        StringBuilder csvContent = new StringBuilder();

        // En-têtes des colonnes
        List<TableColumn<T, ?>> columns = tableView.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) csvContent.append(",");
            csvContent.append(escapeCSV(columns.get(i).getText()));
        }
        csvContent.append("\n");

        // Données des lignes
        ObservableList<T> items = tableView.getItems();
        for (T item : items) {
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) csvContent.append(",");

                // Obtenir la valeur de la cellule
                TableColumn<T, ?> column = columns.get(i);
                Object cellData = getCellData(column, item);
                csvContent.append(escapeCSV(cellData != null ? cellData.toString() : ""));
            }
            csvContent.append("\n");
        }

        // Écrire dans le fichier
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(csvContent.toString());
        }
    }


    /**
     * Récupère la valeur d'une cellule de manière typesafe.
     *
     * @param <T>    Type de l'objet contenu dans la ligne
     * @param <V>    Type de la valeur de la cellule
     * @param column La colonne concernée
     * @param item   L'objet (ligne) dont on veut extraire la valeur
     * @return La valeur de la cellule, ou null en cas d'erreur
     */
    private static <T, V> V getCellData(TableColumn<T, V> column, T item) {
        if (column == null || item == null) {
            return null;
        }

        try {
            Callback<TableColumn.CellDataFeatures<T, V>, ObservableValue<V>> factory = column.getCellValueFactory();

            if (factory != null) {
                TableColumn.CellDataFeatures<T, V> features =
                        new TableColumn.CellDataFeatures<>(column.getTableView(), column, item);

                ObservableValue<V> observable = factory.call(features);
                return observable != null ? observable.getValue() : null;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'accès à la donnée : " + e.getMessage());
        }

        return null;
    }



    /**
     * Échappe les caractères spéciaux pour le format CSV.
     *
     * @param value La valeur à échapper
     * @return La valeur échappée
     */
    private static String escapeCSV(String value) {
        if (value == null) return "";

        // Si la valeur contient des virgules, des guillemets ou des sauts de ligne, l'entourer de guillemets
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            // Remplacer les guillemets par des doubles guillemets
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }

        return value;
    }

    /**
     * Imprime le contenu d'un tableau.
     *
     * @param <T>          Type des éléments du tableau
     * @param tableCard    Le DynamicTableCard contenant les données à imprimer
     * @param parentWindow La fenêtre parente pour afficher les dialogues
     * @param title        Titre du rapport à imprimer
     */
    public static <T> void printTableData(DynamicTableCard<T> tableCard, Window parentWindow, String title) {
        try {
            // Créer une tâche d'impression
            PrinterJob job = PrinterJob.createPrinterJob();

            if (job != null && job.showPrintDialog(parentWindow)) {
                // Créer le contenu à imprimer
                VBox printContent = createPrintContent(tableCard, title);

                // Configurer la mise en page
                PageLayout pageLayout = job.getPrinter().createPageLayout(
                        Paper.A4, PageOrientation.LANDSCAPE, 10, 10, 10, 10);
                job.getJobSettings().setPageLayout(pageLayout);

                // Imprimer le contenu
                boolean success = job.printPage(printContent);

                if (success) {
                    job.endJob();
                    showSuccessAlert("Impression réussie",
                            "Les données ont été envoyées à l'imprimante avec succès.");
                } else {
                    showErrorAlert("Erreur d'impression",
                            "L'impression a échoué.");
                }
            }
        } catch (Exception e) {
            showErrorAlert("Erreur d'impression",
                    "Une erreur s'est produite lors de l'impression: " + e.getMessage());
        }
    }

    /**
     * Crée le contenu à imprimer pour un tableau.
     *
     * @param <T>       Type des éléments du tableau
     * @param tableCard Le DynamicTableCard contenant les données à imprimer
     * @param title     Titre du rapport
     * @return Un nœud contenant le contenu formaté pour l'impression
     */
    private static <T> VBox createPrintContent(DynamicTableCard<T> tableCard, String title) {
        // Créer une nouvelle scène pour l'impression
        VBox printContent = new VBox(10);
        printContent.setPadding(new Insets(20));

        // Ajouter un titre
        Label titleLabel = new Label(title != null ? title : "Rapport");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        printContent.getChildren().add(titleLabel);

        // Ajouter la date d'impression
        Label dateLabel = new Label("Date d'impression: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        dateLabel.setStyle("-fx-font-size: 12px;");
        printContent.getChildren().add(dateLabel);

        // Ajouter un séparateur
        Separator separator = new Separator();
        separator.setPadding(new Insets(5, 0, 15, 0));
        printContent.getChildren().add(separator);

        // Créer un tableau pour l'impression
        TableView<T> originalTable = tableCard.getTableView();
        TableView<T> printTable = new TableView<>();
        printTable.setItems(originalTable.getItems());

        // Copier les colonnes (sans les colonnes d'action)
        for (TableColumn<T, ?> column : originalTable.getColumns()) {
            // Ignorer les colonnes d'action (généralement la dernière colonne)
            if (!column.getText().equals("Actions")) {
                printTable.getColumns().add(column);
            }
        }

        // Ajouter le tableau au contenu d'impression
        printContent.getChildren().add(printTable);

        return printContent;
    }

    /**
     * Obtient l'extension d'un nom de fichier.
     *
     * @param fileName Le nom du fichier
     * @return L'extension du fichier (sans le point)
     */
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * Affiche une alerte de succès.
     *
     * @param title   Titre de l'alerte
     * @param message Message à afficher
     */
    private static void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Affiche une alerte d'erreur.
     *
     * @param title   Titre de l'alerte
     * @param message Message d'erreur à afficher
     */
    private static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}