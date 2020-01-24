package com.florian_ligneul.canbus.change_highlighter.view.utils;

import com.adr.fonticon.IconBuilder;
import com.adr.fonticon.IconFontGlyph;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Utility class for JavaFx TableView
 */
public class TableViewUtils {
    private TableViewUtils() {
    }

    /**
     * Install row factory for clearing row selection when an empty one is selected
     *
     * @param tableView Row factory target
     * @param <T>       The type of the objects contained within the TableView items list.
     */
    public static <T> void clearSelectionOnEmptyRow(TableView<T> tableView) {
        tableView.setRowFactory(tv -> {
            final TableRow<T> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (row.getItem() == null) {
                    tv.getSelectionModel().clearSelection();
                    event.consume();
                }
            });
            return row;
        });
    }

    /**
     * Create TableCell for displaying icons binary state
     *
     * @param trueIcon  {@link IconFontGlyph} used when the cell value is {@code true}
     * @param falseIcon {@link IconFontGlyph} used when the cell value is {@code false}
     */
    public static <T> TableCell<T, Boolean> iconTableCellFactory(IconFontGlyph trueIcon, IconFontGlyph falseIcon) {
        return new TableCell<T, Boolean>() {
            @Override
            protected void updateItem(final Boolean item, final boolean empty) {
                super.updateItem(item, empty);
                if ((item == null) || empty) {
                    setGraphic(null);
                    return;
                }
                setGraphic(IconBuilder.create(item ? trueIcon : falseIcon, 20).color(Color.GRAY).build());
            }
        };
    }
}
