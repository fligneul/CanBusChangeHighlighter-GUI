package com.florian_ligneul.canbus.change_highlighter.view.controller;

import com.adr.fonticon.IconFontGlyph;
import com.florian_ligneul.canbus.change_highlighter.service.config.ConfigService;
import com.florian_ligneul.canbus.change_highlighter.service.config.EMessageDataFormat;
import com.florian_ligneul.canbus.change_highlighter.service.reader.ICanBusReaderService;
import com.florian_ligneul.canbus.change_highlighter.view.model.CanBusConnectionModel;
import com.florian_ligneul.canbus.change_highlighter.view.utils.TableViewUtils;
import com.florian_ligneul.canbus.model.message.CanBusMessage;
import com.florian_ligneul.canbus.model.message.CanBusMessageDatum;
import com.florian_ligneul.canbus.model.uart.EBaudRate;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.florian_ligneul.canbus.change_highlighter.inject.CanBusModule.NAMED_CAN_BUS_READER_SERVICE;
import static com.florian_ligneul.canbus.change_highlighter.view.CanBusChangeHighligtherConstants.DARK_THEME_CSS;

/**
 * JavaFx controller of the application
 */
public class CanBusChangeHighlighterController implements Initializable {
    private static final String ID_FORMAT = "%08X";
    private static final String DATUM_CHANGED_CSS = "has-changed";
    private static final String DATUM_NOT_CHANGED_CSS = "has-not-changed";

    @FXML
    private ToggleGroup dataDisplayFormatToggleGroup;
    @FXML
    private CheckMenuItem darkModeMenuItem;
    @FXML
    private ComboBox<String> comPortComboBox;
    @FXML
    private ComboBox<EBaudRate> baudRateComboBox;
    @FXML
    private ToggleButton connectToggleButton;

    @FXML
    private HBox filterHBox;
    @FXML
    private TextField filterTextField;
    @FXML
    private Button filterResetButton;

    @FXML
    private TableView<CanBusMessage> canBusMessageTableView;
    @FXML
    private TableColumn<CanBusMessage, String> idColumn;
    @FXML
    private TableColumn<CanBusMessage, Boolean> effColumn;
    @FXML
    private TableColumn<CanBusMessage, Boolean> rtrColumn;
    @FXML
    private TableColumn<CanBusMessage, Boolean> errColumn;
    @FXML
    private TableColumn<CanBusMessage, ObservableList<CanBusMessageDatum>> dataColumn;

    @Inject
    private ConfigService configService;

    @Inject
    private CanBusConnectionModel canBusConnectionModel;

    @Inject
    @Named(NAMED_CAN_BUS_READER_SERVICE)
    private ICanBusReaderService canBusReaderService;

    private FilteredList<CanBusMessage> canBusMessageFilteredList;
    private Function<Integer, String> dataStringFormatter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableViewUtils.clearSelectionOnEmptyRow(canBusMessageTableView);
        dataStringFormatter = EMessageDataFormat.getFormatter(configService.getDefaultFormatter());
        configService.getDataFormatter().doOnNext(newDataFormatter -> {
            dataStringFormatter = EMessageDataFormat.getFormatter(newDataFormatter);
            canBusMessageTableView.refresh();
        }).subscribe();
        initMenuControls();
        initConnectionControls();
        initMessageTableViewColumns();
        initMessageIdFilter();
    }

    private void initMenuControls() {
        dataDisplayFormatToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
                Optional.ofNullable(newValue)
                        .map(MenuItem.class::cast)
                        .map(MenuItem::getText)
                        .map(EMessageDataFormat::fromString)
                        .ifPresent(formatter -> configService.setDataFormatter(formatter)));
        darkModeMenuItem.selectedProperty().addListener((observable, oldValue, newValue) ->
                Optional.ofNullable(canBusMessageTableView.getScene())
                        .ifPresent(scene -> {
                            if (newValue) {
                                scene.getStylesheets().add(getClass().getResource(DARK_THEME_CSS).toExternalForm());
                            } else {
                                scene.getStylesheets().remove(getClass().getResource(DARK_THEME_CSS).toExternalForm());
                            }
                        }));
    }

    private void initConnectionControls() {
        comPortComboBox.setItems(canBusReaderService.getComPortList());
        comPortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> canBusConnectionModel.setComPort(newV));
        comPortComboBox.disableProperty().bind(canBusConnectionModel.isConnectedProperty());

        baudRateComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(EBaudRate.values())));
        baudRateComboBox.setConverter(EBaudRate.getConverter());
        baudRateComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> canBusConnectionModel.setBaudRate(newV.getBaudRate()));
        baudRateComboBox.disableProperty().bind(canBusConnectionModel.isConnectedProperty());

        connectToggleButton.setOnAction(event -> {
            if (canBusConnectionModel.isConnected()) {
                canBusReaderService.disconnect();
            } else {
                canBusReaderService.connect();
            }
        });

    }

    private void initMessageTableViewColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asString(ID_FORMAT));

        effColumn.setCellValueFactory(cellData -> cellData.getValue().isExtendedFrameFormatProperty());
        effColumn.setCellFactory(param -> TableViewUtils.iconTableCellFactory(IconFontGlyph.FA_SOLID_CHECK, IconFontGlyph.FA_SOLID_TIMES));

        rtrColumn.setCellValueFactory(cellData -> cellData.getValue().isRemoteTransmissionRequestProperty());
        rtrColumn.setCellFactory(param -> TableViewUtils.iconTableCellFactory(IconFontGlyph.FA_SOLID_CHECK, IconFontGlyph.FA_SOLID_TIMES));

        errColumn.setCellValueFactory(cellData -> cellData.getValue().isErrorMessageProperty());
        errColumn.setCellFactory(param -> TableViewUtils.iconTableCellFactory(IconFontGlyph.FA_SOLID_CHECK, IconFontGlyph.FA_SOLID_TIMES));

        dataColumn.setCellValueFactory(cellData -> cellData.getValue().getMessageData());
        dataColumn.setCellFactory(param -> new TableCell<CanBusMessage, ObservableList<CanBusMessageDatum>>() {
            @Override
            protected void updateItem(final ObservableList<CanBusMessageDatum> item, final boolean empty) {
                super.updateItem(item, empty);
                if ((item == null) || empty) {
                    setGraphic(null);
                    setTooltip(null);
                    return;
                }
                HBox messageData = new HBox(10);
                messageData.setFillHeight(true);
                messageData.setTranslateY(-1);
                messageData.setAlignment(Pos.CENTER_LEFT);
                messageData.getChildren().addAll(
                        item.stream()
                                .map(canBusMessageData -> {
                                    Label dataByte = new Label(dataStringFormatter.apply(canBusMessageData.getDatum()));
                                    dataByte.getStyleClass().add(canBusMessageData.hasChanged() ? DATUM_CHANGED_CSS : DATUM_NOT_CHANGED_CSS);
                                    return dataByte;
                                })
                                .collect(Collectors.toList()));

                setGraphic(messageData);
            }

        });

        dataColumn.prefWidthProperty().bind(
                canBusMessageTableView.widthProperty()
                        .subtract(idColumn.widthProperty())
                        .subtract(effColumn.widthProperty())
                        .subtract(rtrColumn.widthProperty())
                        .subtract(errColumn.widthProperty())
                        .subtract(2)  // a border stroke?
        );

        canBusMessageFilteredList = new FilteredList<>(canBusReaderService.getCanBusMessageList(), p -> true);
        SortedList<CanBusMessage> canBusMessageSortedList = new SortedList<>(canBusMessageFilteredList);
        canBusMessageSortedList.comparatorProperty().bind(canBusMessageTableView.comparatorProperty());
        canBusMessageTableView.setItems(canBusMessageFilteredList);
    }

    private void initMessageIdFilter() {
        // Filter available only when CanBus service is connected
        filterHBox.disableProperty().bind(BooleanBinding.booleanExpression(canBusConnectionModel.isConnectedProperty()).not());
        filterResetButton.setOnAction(event -> {
            filterTextField.clear();
        });
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            canBusMessageFilteredList.setPredicate(canBusMessage -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return String.format(ID_FORMAT, canBusMessage.getId()).contains(newValue);
            });
        });
    }
}
