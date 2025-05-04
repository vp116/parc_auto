package com.angbe.soro.parc_auto.components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DynamicTable<T> extends TableView<T> {

    private final ObservableList<T> dataList = FXCollections.observableArrayList();

    public DynamicTable() {
        super();
        this.setItems(dataList);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    /**
     * Ajoute une colonne à la table
     *
     * @param columnName Le nom de la colonne
     * @param property   La propriété à afficher dans la colonne
     */
    public void addColumn(String columnName, String property) {
        TableColumn<T, String> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> {
            try {
                // Utilisation de la réflexion pour accéder à la propriété
                Object value = cellData.getValue().getClass()
                        .getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1))
                        .invoke(cellData.getValue());
                return new SimpleStringProperty(value != null ? value.toString() : "");
            } catch (Exception e) {
                return new SimpleStringProperty("");
            }
        });
        this.getColumns().add(column);
    }


    /**
     * Ajoute une colonne avec un extracteur de valeur personnalisé
     *
     * @param columnName     Le nom de la colonne
     * @param valueExtractor Fonction pour extraire la valeur de l'objet
     */
    public <U> void addColumn(String columnName, java.util.function.Function<T, U> valueExtractor) {
        TableColumn<T, U> column = new TableColumn<>(columnName);
        column.setCellValueFactory(cellData -> {
            U value = valueExtractor.apply(cellData.getValue());
            return new SimpleObjectProperty<>(value);
        });
        this.getColumns().add(column);
    }

    /**
     * Met à jour les données de la table
     *
     * @param newData Les nouvelles données
     */
    public void updateData(ObservableList<T> newData) {
        dataList.setAll(newData);
    }

    /**
     * Ajoute un élément à la table
     *
     * @param item L'élément à ajouter
     */
    public void addItem(T item) {
        dataList.add(item);
    }

    /**
     * Supprime un élément de la table
     *
     * @param item L'élément à supprimer
     */
    public void removeItem(T item) {
        dataList.remove(item);
    }

    /**
     * Efface toutes les données de la table
     */
    public void clearData() {
        dataList.clear();
    }

    /**
     * Efface toutes les colonnes de la table
     */
    public void clearColumns() {
        this.getColumns().clear();
    }
}