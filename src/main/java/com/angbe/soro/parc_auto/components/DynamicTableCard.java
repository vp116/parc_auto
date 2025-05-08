package com.angbe.soro.parc_auto.components;

import com.cardosama.fontawesome_fx_6.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * DynamicTableCard est une extension de TableCardBase qui ajoute des fonctionnalités avancées
 * comme la pagination et des méthodes utilitaires pour créer des colonnes spéciales.
 *
 * @param <T> Type d'élément affiché dans le tableau
 */
public class DynamicTableCard<T> extends TableCardBase<T> {
    private final Map<String, TableColumn<T, ?>> columnMap = new LinkedHashMap<>(); // Pour stocker les colonnes par nom
    private PaginationBox paginationBox; // Zone de pagination

    /**
     * Constructeur principal avec pagination optionnelle.
     *
     * @param title      Le titre à afficher en haut de la carte
     * @param columns    Liste de colonnes pour le TableView
     * @param items      Liste d'éléments à afficher
     * @param actions    Liste de boutons d'action à droite du titre (peut être null)
     * @param pagination Indique si la pagination doit être ajoutée
     */
    public DynamicTableCard(String title, List<TableColumn<T, ?>> columns, ObservableList<T> items,
                            List<Button> actions, boolean pagination) {
        super(title, columns, items, actions);


        // Ajouter la pagination si nécessaire
        if (pagination) {
            addPagination(items != null ? items.size() : 0);
        }

        // Initialiser la map des colonnes si des colonnes sont fournies
        if (columns != null) {
            for (TableColumn<T, ?> column : columns) {
                columnMap.put(column.getText(), column);
            }
        }
    }

    /**
     * Constructeur simplifié sans pagination.
     *
     * @param title   Le titre à afficher en haut de la carte
     * @param columns Liste de colonnes pour le TableView
     * @param items   Liste d'éléments à afficher
     * @param actions Liste de boutons d'action
     */
    public DynamicTableCard(String title, List<TableColumn<T, ?>> columns, ObservableList<T> items, List<Button> actions) {
        this(title, columns, items, actions, false);
    }

    /**
     * Crée une colonne standard pour le TableView.
     *
     * @param <S>      Type des éléments du tableau
     * @param <U>      Type de la valeur de la colonne
     * @param title    Titre de la colonne
     * @param property Nom de la propriété à lier
     * @param width    Largeur minimale de la colonne
     * @return TableColumn configurée
     */
    public static <S, U> TableColumn<S, U> createColumn(String title, String property, int width) {
        TableColumn<S, U> column = new TableColumn<>(title);
        column.setMinWidth(width);
        column.setStyle("-fx-alignment: CENTER;");
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    /**
     * Crée une colonne avec badges colorés.
     *
     * @param <S>                Type des éléments du tableau
     * @param title              Titre de la colonne
     * @param property           Nom de la propriété à lier
     * @param badgeClassProvider Fonction qui détermine la classe CSS à utiliser selon la valeur
     * @return TableColumn avec badges
     */
    public static <S> TableColumn<S, String> createBadgeColumn(String title, String property, Function<String, String> badgeClassProvider) {
        TableColumn<S, String> column = new TableColumn<>(title);
        column.setMinWidth(150);
        column.setStyle("-fx-alignment: CENTER;");

        column.setCellValueFactory(new PropertyValueFactory<>(property));

        column.setCellFactory(col -> new TableCell<S, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Créer un Badge avec le texte de l'état
                    Badge badge = new Badge(item);

                    // Récupérer la classe CSS selon l'état
                    String badgeClass = badgeClassProvider.apply(item);

                    // Nettoyer les anciennes classes
                    badge.getStyleClass().removeIf(style -> style.startsWith("badge-"));

                    // Ajouter la nouvelle classe CSS
                    badge.getStyleClass().add(badgeClass);

                    setGraphic(badge);
                    setText(null); // Pas de texte brut
                }
            }
        });

        return column;
    }

    /**
     * Crée une colonne avec badges colorés.
     *
     * @param <S>                Type des éléments du tableau
     * @param title              Titre de la colonne
     * @param propertyGetter     Nom de la propriété à lier
     * @param styleDeterminer Fonction qui détermine la classe CSS à utiliser selon la valeur
     * @return TableColumn avec badges
     */
    public static <S> TableColumn<S, String> createBadgeColumn(
            String title,
            Function<S, String> propertyGetter,
            Function<String, String> styleDeterminer) {

        TableColumn<S, String> column = new TableColumn<>(title);
        column.setMinWidth(100);
        column.setStyle("-fx-alignment: CENTER;");

        column.setCellValueFactory(cellData -> new SimpleStringProperty(propertyGetter.apply(cellData.getValue())));

        column.setCellFactory(col -> new TableCell<S, String>() {
            private final Badge badge = new Badge(); // Une seule instance

            {
                // Configuration initiale
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(badge);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    badge.setContent(item);

                    // Appliquer le style selon la logique fournie
                    String styleClass = styleDeterminer.apply(item);
                    if (styleClass != null) {
                        applyBadgeStyle(badge, styleClass);
                    }

                    setGraphic(badge);
                }
            }

            private void applyBadgeStyle(Badge badge, String styleClass) {
                // Utilisation des méthodes existantes de votre classe Badge
                switch (styleClass) {
                    case "badge-yellow" -> badge.setYellowStyle();
                    case "badge-red" -> badge.setRedStyle();
                    case "badge-green" -> badge.setGreenStyle();
                    case "badge-blue" -> badge.setBlueStyle();
                    case "badge-gray" -> badge.setGrayStyle();
                    case "badge-purple" -> badge.setPurpleStyle();
                    case "badge-pink" -> badge.setPinkStyle();
                    default -> badge.setConsistentRandomStyle(styleClass);
                }
            }
        });

        return column;
    }

    /**
     * Crée une colonne avec des boutons d'action (visualiser, éditer, supprimer).
     *
     * @param <S>      Type des éléments du tableau
     * @param title    Titre de la colonne
     * @param onView   Action lorsqu'on clique sur "Visualiser"
     * @param onEdit   Action lorsqu'on clique sur "Éditer"
     * @param onDelete Action lorsqu'on clique sur "Supprimer"
     * @return TableColumn avec les actions
     */
    public static <S> TableColumn<S, Void> createActionsColumn(String title,
                                                               Consumer<S> onView,
                                                               Consumer<S> onEdit,
                                                               Consumer<S> onDelete) {
        TableColumn<S, Void> column = new TableColumn<>(title);
        column.setMaxWidth(160);
        column.setMinWidth(160);

        column.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = createIconButton("eye", "action-view");
            private final Button editBtn = createIconButton("penToSquare", "action-edit");
            private final Button deleteBtn = createIconButton("trash", "action-delete");
            private final HBox pane = new HBox(5, viewBtn, editBtn, deleteBtn);

            {
                pane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);

                    // Configure les événements des boutons
                    S rowItem = getTableView().getItems().get(getIndex());
                    viewBtn.setOnAction(e -> onView.accept(rowItem));
                    editBtn.setOnAction(e -> onEdit.accept(rowItem));
                    deleteBtn.setOnAction(e -> onDelete.accept(rowItem));
                }
            }
        });

        return column;
    }

    /**
     * Crée un bouton avec une icône FontAwesome.
     *
     * @param iconName   Nom de l'icône FontAwesome
     * @param styleClass Classe CSS à appliquer
     * @return Bouton configuré
     */
    private static Button createIconButton(String iconName, String styleClass) {
        var fontIcon = new FontAwesomeIconView(iconName);
        fontIcon.getStyleClass().add(styleClass);
        Button button = new Button("", fontIcon);
        button.setStyle("-fx-background-color: transparent; ");
        return button;
    }

    /**
     * Utilise les fonctionnalités du DynamicTable pour ajouter des colonnes directement
     * par nom de propriété, et stocke la colonne dans la map.
     *
     * @param columnName Titre de la colonne
     * @param property   Nom de la propriété à afficher
     * @return La colonne créée pour permettre d'autres customisations
     */
    public TableColumn<T, String> addPropertyColumn(String columnName, String property) {
        // Créer une colonne standard au lieu d'utiliser addColumn pour avoir un meilleur contrôle
        TableColumn<T, String> column = new TableColumn<>(columnName);
        column.setMinWidth(150);
        column.setStyle("-fx-alignment: center;");
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

        // Ajouter la colonne à la map et au tableView
        columnMap.put(columnName, column);
        tableView.getColumns().add(column);

        return column;
    }

    public void addColumn(TableColumn<T, ?> column) {
        columnMap.put(column.getText(), column);
        tableView.getColumns().add(column);
    }

    /**
     * Utilise les fonctionnalités du DynamicTable pour ajouter des colonnes personnalisées,
     * et stocke la colonne dans la map.
     *
     * @param columnName     Titre de la colonne
     * @param valueExtractor Fonction pour extraire la valeur
     * @param <U>            Type de la valeur retournée
     * @return La colonne créée pour permettre d'autres customisations
     */
    public <U> TableColumn<T, U> addCustomColumn(String columnName, Function<T, U> valueExtractor) {
        TableColumn<T, U> column = new TableColumn<>(columnName);
        column.setMinWidth(150);
        column.setStyle("-fx-alignment: center");
        column.setCellValueFactory(cellData -> {
            U value = valueExtractor.apply(cellData.getValue());
            return new SimpleObjectProperty<>(value);
        });

        // Ajouter la colonne à la map et au tableView
        columnMap.put(columnName, column);
        tableView.getColumns().add(column);

        return column;
    }

    /**
     * Permet de réordonner les colonnes selon une liste de noms de colonnes.
     * Les colonnes non mentionnées dans la liste seront placées à la fin, dans leur ordre actuel.
     *
     * @param columnNames Liste ordonnée des noms de colonnes
     */
    public void reorderColumns(List<String> columnNames) {
        if (columnNames == null || columnNames.isEmpty()) {
            return;
        }

        // Créer une nouvelle liste pour les colonnes réordonnées
        List<TableColumn<T, ?>> reorderedColumns = new ArrayList<>();

        // Ajouter d'abord les colonnes selon l'ordre spécifié
        for (String name : columnNames) {
            TableColumn<T, ?> column = columnMap.get(name);
            if (column != null && tableView.getColumns().contains(column)) {
                reorderedColumns.add(column);
            }
        }

        // Ajouter les colonnes restantes qui n'étaient pas dans la liste
        for (TableColumn<T, ?> column : tableView.getColumns()) {
            if (!reorderedColumns.contains(column)) {
                reorderedColumns.add(column);
            }
        }

        // Mettre à jour les colonnes du tableView
        tableView.getColumns().clear();
        tableView.getColumns().addAll(reorderedColumns);
    }

    /**
     * Renomme une colonne existante.
     *
     * @param oldName Nom actuel de la colonne
     * @param newName Nouveau nom pour la colonne
     * @return true si la colonne a été trouvée et renommée, false sinon
     */
    public boolean renameColumn(String oldName, String newName) {
        TableColumn<T, ?> column = columnMap.get(oldName);
        if (column != null) {
            column.setText(newName);
            // Mettre à jour la map des colonnes
            columnMap.remove(oldName);
            columnMap.put(newName, column);
            return true;
        }
        return false;
    }

    /**
     * Ajoute la pagination à la carte.
     *
     * @param totalItems Nombre total d'éléments pour la pagination
     */
    private void addPagination(int totalItems) {
        paginationBox = new PaginationBox(1, totalItems, 10);
        paginationBox.setPadding(new Insets(10, 15, 10, 15));
        paginationBox.getStyleClass().add("pagination-box");
        this.getChildren().add(paginationBox);
    }

    @Override
    public void setItems(ObservableList<T> items) {
        super.setItems(items);

        // Mettre à jour la pagination si elle existe
        if (paginationBox != null) {
            paginationBox.setTotalItems(items != null ? items.size() : 0);
        }
    }

    /**
     * Ajoute un élément au tableau.
     *
     * @param item L'élément à ajouter
     */
    public void addItem(T item) {
        tableView.addItem(item);

        // Mettre à jour la pagination si elle existe
        if (paginationBox != null) {
            paginationBox.setTotalItems(tableView.getItems().size());
        }
    }

    /**
     * Supprime un élément du tableau.
     *
     * @param item L'élément à supprimer
     */
    public void removeItem(T item) {
        tableView.removeItem(item);

        // Mettre à jour la pagination si elle existe
        if (paginationBox != null) {
            paginationBox.setTotalItems(tableView.getItems().size());
        }
    }

    /**
     * Récupère une colonne par son nom.
     *
     * @param columnName Le nom de la colonne à récupérer
     * @return La colonne correspondante ou null si non trouvée
     */
    public TableColumn<T, ?> getColumnByName(String columnName) {
        return columnMap.get(columnName);
    }

    /**
     * Récupère la liste des noms de colonnes dans leur ordre actuel d'affichage.
     *
     * @return Liste ordonnée des noms de colonnes
     */
    public List<String> getColumnOrder() {
        List<String> columnOrder = new ArrayList<>();
        for (TableColumn<T, ?> column : tableView.getColumns()) {
            columnOrder.add(column.getText());
        }
        return columnOrder;
    }

    /**
     * Applique une configuration de colonnes (noms et ordre) à partir d'une liste
     * de paires de chaînes [ID propriété, Nom affiché].
     *
     * @param columnConfigs Liste de paires [propriété, nom affiché]
     */
    public void configureColumns(List<Map.Entry<String, String>> columnConfigs) {
        // Effacer les colonnes existantes
        tableView.clearColumns();
        columnMap.clear();

        // Ajouter les nouvelles colonnes dans l'ordre spécifié
        for (Map.Entry<String, String> config : columnConfigs) {
            String property = config.getKey();
            String displayName = config.getValue();
            addPropertyColumn(displayName, property);
        }
    }

    /**
     * Cache ou affiche une colonne par son nom.
     *
     * @param columnName Le nom de la colonne
     * @param visible    true pour afficher, false pour cacher
     * @return true si l'opération a réussi, false si la colonne n'a pas été trouvée
     */
    public boolean setColumnVisibility(String columnName, boolean visible) {
        TableColumn<T, ?> column = columnMap.get(columnName);
        if (column != null) {
            column.setVisible(visible);
            return true;
        }
        return false;
    }

    /**
     * Classe interne pour gérer la pagination.
     */
    private static class PaginationBox extends HBox {
        private final Label infoLabel;
        private final HBox buttonBox;
        private final int itemsPerPage;
        private int currentPage;
        private int totalItems;

        /**
         * Constructeur de la pagination.
         *
         * @param currentPage  Page actuelle
         * @param totalItems   Nombre total d'éléments
         * @param itemsPerPage Nombre d'éléments par page
         */
        public PaginationBox(int currentPage, int totalItems, int itemsPerPage) {
            super(10);
            this.currentPage = currentPage;
            this.totalItems = totalItems;
            this.itemsPerPage = itemsPerPage;

            setAlignment(Pos.CENTER);
            getStyleClass().add("pagination-container");

            // Label d'info
            infoLabel = new Label();
            infoLabel.getStyleClass().add("pagination-info");

            // Espace flexible
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Boutons de pagination
            buttonBox = new HBox(5);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            getChildren().addAll(infoLabel, spacer, buttonBox);
            updateUI();
        }

        /**
         * Définit le nombre total d'éléments.
         */
        public void setTotalItems(int totalItems) {
            this.totalItems = totalItems;
            updateUI();
        }

        /**
         * Définit la page actuelle.
         */
        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
            updateUI();
        }

        /**
         * Met à jour l'interface de pagination.
         */
        private void updateUI() {
            // Mettre à jour le texte d'info
            int start = totalItems > 0 ? (currentPage - 1) * itemsPerPage + 1 : 0;
            int end = Math.min(currentPage * itemsPerPage, totalItems);
            infoLabel.setText(String.format("Affichage de %d à %d sur %d éléments", start, end, totalItems));

            // Mettre à jour les boutons de pagination
            buttonBox.getChildren().clear();

            int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

            // Pas de pages si pas d'éléments
            if (totalPages == 0) {
                return;
            }

            // Bouton précédent
            Button prevButton = new Button("<");
            prevButton.getStyleClass().add("pagination-button");
            prevButton.setDisable(currentPage == 1);
            prevButton.setOnAction(e -> setCurrentPage(currentPage - 1));

            buttonBox.getChildren().add(prevButton);

            // Boutons des pages
            for (int i = 1; i <= Math.min(totalPages, 5); i++) {
                Button pageBtn = new Button(String.valueOf(i));
                pageBtn.getStyleClass().add("pagination-button");
                if (i == currentPage) {
                    pageBtn.getStyleClass().add("pagination-current");
                }

                final int pageNum = i;
                pageBtn.setOnAction(e -> setCurrentPage(pageNum));

                buttonBox.getChildren().add(pageBtn);
            }

            // Bouton suivant
            Button nextButton = new Button(">");
            nextButton.getStyleClass().add("pagination-button");
            nextButton.setDisable(currentPage == totalPages);
            nextButton.setOnAction(e -> setCurrentPage(currentPage + 1));

            buttonBox.getChildren().add(nextButton);
        }
    }
}
