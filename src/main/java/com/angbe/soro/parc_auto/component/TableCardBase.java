package com.angbe.soro.parc_auto.component;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

/**
 * TableCardBase est une classe qui utilise DynamicTable pour créer une carte
 * contenant un tableau avec un titre et des boutons d'action.
 *
 * @param <T> Le type des éléments affichés dans le DynamicTable
 */
public class TableCardBase<T> extends VBox {
    /**
     * Label affichant le titre de la carte
     */
    protected Label titleLabel;

    /**
     * Conteneur pour les boutons d'action à droite du titre
     */
    protected HBox actionButtons;

    /**
     * DynamicTable principal pour afficher les données
     */
    protected DynamicTable<T> tableView;

    /**
     * Constructeur simplifié pour créer une carte avec tableau.
     *
     * @param title   Le titre à afficher en haut de la carte (non null)
     * @param actions Liste des boutons d'action à ajouter (peut être null)
     * @throws IllegalArgumentException si le titre est null
     */
    public TableCardBase(String title, List<Button> actions) {
        this(title, null, null, actions);
    }

    /**
     * Constructeur principal pour créer une carte avec tableau.
     *
     * @param title   Le titre à afficher en haut de la carte (non null)
     * @param columns Liste des colonnes à ajouter au TableView (peut être null)
     * @param items   Liste observable des éléments à afficher (peut être null)
     * @param actions Liste des boutons d'action à ajouter (peut être null)
     * @throws IllegalArgumentException si le titre est null
     */
    public TableCardBase(String title, List<TableColumn<T, ?>> columns, ObservableList<T> items, List<Button> actions) {
        super(10); // Espace vertical de 10px entre les éléments
        if (title == null) {
            throw new IllegalArgumentException("Le titre ne peut pas être null");
        }
        this.setPadding(new Insets(0));
        this.getStyleClass().add("dynamic-table-card");

        // Initialisation des composants
        createHeader(title, actions);
        createTable(columns, items);

        // Construction du layout
        VBox content = new VBox();
        content.setPadding(new Insets(15));
        content.getChildren().add(buildHeader());
        content.getChildren().add(tableView);

        this.getChildren().add(content);
    }

    /**
     * Crée et configure la partie haute de la carte (titre + boutons d'action).
     *
     * @param title   Le titre à afficher
     * @param actions Liste des boutons d'action à ajouter
     */
    protected void createHeader(String title, List<Button> actions) {
        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("header-title");
        titleLabel.setFont(new Font(18));

        actionButtons = new HBox(8); // Espace horizontal de 8px entre les boutons
        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        if (actions != null) {
            for (Button btn : actions) {
                btn.getStyleClass().add("card-action-button");
                actionButtons.getChildren().add(btn);
            }
        }
    }

    /**
     * Assemble le header (titre + boutons) dans un conteneur HBox.
     *
     * @return Le conteneur HBox configuré
     */
    protected HBox buildHeader() {
        HBox header = new HBox(10); // Espace horizontal de 10px
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 10, 0));
        header.getStyleClass().add("card-header");

        HBox.setHgrow(actionButtons, Priority.ALWAYS);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titleLabel, spacer, actionButtons);
        return header;
    }

    /**
     * Crée et configure le DynamicTable avec les colonnes et données fournies.
     *
     * @param columns Liste des colonnes à ajouter
     * @param items   Liste des éléments à afficher
     */
    protected void createTable(List<TableColumn<T, ?>> columns, ObservableList<T> items) {
        // Utiliser DynamicTable au lieu de TableView standard
        tableView = new DynamicTable<>();
        tableView.getStyleClass().add("dynamic-table-view");

        if (columns != null) {
            tableView.getColumns().addAll(columns);
        }

        if (items != null) {
            tableView.updateData(items);
        }

        VBox.setVgrow(tableView, Priority.ALWAYS);
    }

    /**
     * Modifie le titre de la carte.
     *
     * @param title Le nouveau titre (non null)
     * @throws IllegalArgumentException si le titre est null
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Le titre ne peut pas être null");
        }
        titleLabel.setText(title);
    }

    /**
     * Remplace les boutons d'action existants par une nouvelle liste.
     *
     * @param actions La nouvelle liste de boutons (peut être null pour supprimer tous les boutons)
     */
    public void setActions(List<Button> actions) {
        actionButtons.getChildren().clear();
        if (actions != null) {
            for (Button btn : actions) {
                btn.getStyleClass().add("card-action-button");
                actionButtons.getChildren().add(btn);
            }
        }
    }

    /**
     * Modifie les données affichées dans le DynamicTable.
     *
     * @param items La nouvelle liste observable d'éléments (peut être null)
     */
    public void setItems(ObservableList<T> items) {
        if (items != null) {
            tableView.updateData(items);
        } else {
            tableView.clearData();
        }
    }

    /**
     * Retourne une référence au DynamicTable pour des personnalisations avancées.
     *
     * @return Le DynamicTable utilisé dans cette carte
     */
    public DynamicTable<T> getTableView() {
        return tableView;
    }
}

