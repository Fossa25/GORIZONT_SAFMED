package com.example.proburok.job;

import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import com.example.proburok.MQ.DatabaseHandler;
import com.example.proburok.New_Class.*;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ProhodCOD extends Configs {
    @FXML private DatePicker calendar;
    @FXML private TextField gorizont;
    @FXML private TextField sehen;
    @FXML private TextField nameProhod;
    @FXML private TextField nomer;
    @FXML private TextField idi;
    @FXML private Button singUpButtun;

    private Date DATA;
    private String HIFR;
    @FXML private TableView<Probnik> Tablshen;
    @FXML private TableView<Probnik> TablshenGPR;
    @FXML private TableView<Probnik> TablshenGNR;
    @FXML private TableView<Probnik> TablshenV;
    @FXML private TableView<Probnik1> Tablshen1;
    @FXML private Label L_sehen;
    @FXML private TableColumn<Probnik, String> stolb1;
    @FXML private TableColumn<Probnik, Double> stolb2;
    @FXML private TableColumn<Probnik, Double> stolb3;
    @FXML private TableColumn<Probnik, String> stolbGPR1;
    @FXML private TableColumn<Probnik, Double> stolbGPR2;
    @FXML private TableColumn<Probnik, Double> stolbGPR3;
    @FXML private TableColumn<Probnik, String> stolbGNR1;
    @FXML private TableColumn<Probnik, Double> stolbGNR2;
    @FXML private TableColumn<Probnik, Double> stolbGNR3;
    @FXML private TableColumn<Probnik, String> stolb1V;
    @FXML private TableColumn<Probnik, Double> stolb2V;
    @FXML private TableColumn<Probnik, Double> stolb3V;
    @FXML private TableColumn<Probnik1, String> stolb11;
    @FXML private TableColumn<Probnik1, String> stolb21;
    @FXML private TableColumn<Probnik, Double> stolb31;
    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView pethat;
    @FXML private ImageView tabl;
    @FXML private ImageView othet;
    @FXML private ComboBox<String> ushatok;
    @FXML private TextField dlina;
    @FXML private TextField privazka;
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView planVNS;
    @FXML private ImageView planVNSNE;
    @FXML private ImageView planobnov;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView poperVNS;
    @FXML private ImageView poperVNSNE;
    @FXML private ImageView poperobnov;
    @FXML private Label privaz_L;
    @FXML private Label L_dlina;
    @FXML private ComboBox<String> ChoiceBox;
    @FXML private Label Choice_Label;
    @FXML
    void initialize() {
        Tablshen.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TablshenGPR.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TablshenGNR.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TablshenV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Tablshen1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ChoiceBox.setVisible(false);
        Choice_Label.setVisible(false);
        ChoiceBox.setValue("-");
        calendar.setValue(LocalDate.now());

        String uchastok = UserSession.getUchastok();
        if (uchastok != null) {
            ushatok.setValue(uchastok);
            Avto_Nomber();}

        sehen.setVisible(false);
        L_sehen.setVisible(false);
        idi.setVisible(false);
        tabltrabl();
        Filter_TextGor(gorizont);
        Filter_Text(dlina);

        pethat.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Pehat.fxml","no"));
        openImagedok(tabl,instr,dokumGeolog,dokumGeolog11,"yes");
        othet.setOnMouseClicked(mouseEvent -> {OpenDok(Put_othet,"Отчет_");});
        ushatok.getItems().addAll("Участок КПУ ГПР №1 ");
        ushatok.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && calendar.getValue() != null) {
                Avto_Nomber();
            }
        });
        singUpButtun.setOnMouseClicked(mouseEvent -> {

            HIFR = idi.getText() != null ? idi.getText().trim() : "";
            LocalDate selectedDate = calendar.getValue();
            DATA = Date.valueOf(selectedDate);
            setupImageHandlers();
            try {
                validateInputs();
                InputData input = validateInputs();

                String nameBD = input.selectedName + " № " + input.NoberValue;
                String prim =  "Требуется геологическое описание" ;
                // Все данные валидны - сохраняем
                System.out.print(HIFR);
                DatabaseHandler Tabl = new DatabaseHandler();
                Tabl.Dobavlenie(input.NoberValue, DATA, input.sectionValue, input.selectedGor,  nameBD,input.selectedName,
                        HIFR,ushatok.getValue(),input.dlinaValue,input.selectPrivizka,prim, ChoiceBox.getValue());

                if (HIFR.equals("19")||HIFR.equals("21")){
                     prim =  "Все данные внесены" ;
                     String tippas;

                    switch (HIFR){
                        case"19" -> tippas = "60";
                        case"21" -> tippas = "65";
                        default -> throw new IllegalStateException("Unexpected value: " + HIFR);
                    }
                    Tabl.DobavlenieGEOLOG_SOPR("-","Выработка проходится в искусственном закладочном массиве с прочностью более 0,5 МПа. " +
                            "Контакт с горным массивом отсутствует",  input.selectedGor, nameBD,tippas,prim) ;
                }
                ohistka();
            } catch (DateTimeException e) {
                showAlert("Ошибка в формате даты!");
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });


        setupImageHandlers();
    }
    private InputData validateInputs() throws ValidationException {

        InputData data = new InputData();
        data.selectedGor = gorizont.getText();
        data.selectedName = nameProhod.getText();
        data.selectPrivizka = privazka.getText();
        data.dlinaValue = dlina.getText();
        data.sectionValue = sehen.getText();
        data.NoberValue = nomer.getText();
        data.planVisbl1=planVNS;
        data.planVisbl2=planVNSNE;
        validateRequiredFields(data);
        return data;
    }
    private void validateRequiredFields(InputData data) throws ValidationException {
        validateRequiredFields_Chek(data,"miner");
        StringBuilder errors = new StringBuilder();

        LocalDate selectedDate = calendar.getValue();
        if (selectedDate == null) {errors.append("- Не выбрана дата\n");
        } else {DATA = Date.valueOf(selectedDate); }
        if ( ChoiceBox.getValue().isEmpty()) errors.append("Пустое поле- "+Choice_Label.getText()+ "\n");
            if(idi.getText().equals("19")||idi.getText().equals("21")){

                if (poperVNS.isVisible() || planVNSNE.isVisible()) {
                    errors.append("- Не внесён графический материал ").append("\n");
                }
            }

        if (errors.length() > 0) {
            throw new ValidationException(" \n" + errors.toString());
        }
    }
    private void ohistka() {
        ushatok.setValue("");
        nomer.setText("");
        sehen.setText("");
        sehen.setVisible(false);
        L_sehen.setVisible(false);
        gorizont.setText("");
        dlina.setText("");
        privazka.setText("");
        nameProhod.setText("");
        idi.setText("");
        setupImageHandlers();
        ChoiceBox.getItems().clear();
        Choice_Label.setText("");
    }
    private void tabltrabl(){
        getStl(stolb1,stolb2,stolb3);
        getStl(stolbGNR1,stolbGNR2,stolbGNR3);
        getStl(stolbGPR1,stolbGPR2,stolbGPR3);
        getStl(stolb1V,stolb2V,stolb3V);

        // Для второй таблицы
        stolb11.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVirobotka1()));
        stolb21.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNety()));
        stolb31.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getId()).asObject());

        ObservableList<Probnik> tabl = FXCollections.observableArrayList(
                new Probnik("Автотранспортный съезд (на закруглении)", 20.31,1),
                new Probnik("Автотранспортный съезд (прямой участок)",24.43 ,2),
                new Probnik("Квершлаг, штрек (прямой участок)",19.92 ,3),
                new Probnik("Квершлаг, штрек (на закруглении)", 23.78,4),
                new Probnik("Штрек, автоуклон (прямой участок)",19.14 ,5),
                new Probnik("Штрек, автоуклон (на закруглении)", 23.17,6),
                new Probnik("Заезд, ходок", 11.08,7),
                new Probnik("Ниша", 9.72,8)

        );
        ObservableList<Probnik> tablGPR = FXCollections.observableArrayList(
                new Probnik("Камера хранения материалов (ГКР, ГПР)",25.18 ,9),
                new Probnik("Заезд (ГКР, ГПР)",20.50 ,10),
                new Probnik("Камера УПП (ГКР, ГПР)",17.40 ,11),
                new Probnik("Камера скважин, ниша ВХВ (ГКР, ГПР)", 14.60,12),
                new Probnik("Ниша ВМП (ГКР, ГПР)", 9.60,13),
                new Probnik("Камера ВМП (ГКР, ГПР)",13.00 ,14),
                new Probnik("Штрек (ГПР)", 16.40,15)
                );

        ObservableList<Probnik> tablGNR = FXCollections.observableArrayList(
                new Probnik("Буровая выработка",20.93 ,16),
                new Probnik("Буровая выработка", 25.75,17),
                new Probnik("Буровая выработка (вприсечку с массивом)", 21.15,18),
                new Probnik("Буровая выработка (в закладке)", 21.15,19),
                new Probnik("Буровая выработка (вприсечку с массивом)", 20.21,20),
                new Probnik("Буровая выработка (в закладке)", 20.21,21),
                new Probnik("Буровая выработка (вприсечку с закладкой)",20.93 ,22),
                new Probnik("Буровая выработка (в закладке, вприсечку с массивом)",20.93 ,23),
                new Probnik("Буровая выработка (вприсечку с закладкой)",25.75 ,24),
                new Probnik("Буровая выработка (в закладке, вприсечку с массивом)", 25.75,25)
        );
        ObservableList<Probnik> tablV = FXCollections.observableArrayList(
                new Probnik("ВХВ", 6.25,26),
                new Probnik("ВХВ", 12.00,27)
        );
        ObservableList<Probnik1> tabl1 = FXCollections.observableArrayList(
                new Probnik1 ("«Т»-образное. Штрек/Камера материалов","-",28),
                new Probnik1 ("«Т»-образное. Штрек/Заезд на горизонт","19.1/19.4",29),
                new Probnik1 ("«Т»-образное. Штрек/Секционный орт","19.4/19.4",30),
                new Probnik1 ("«У»-образное. Штрек/Секционный орт","19.4/19.4",31),
                new Probnik1 ("«Т»-образное. Транспортная/Буровая","19.4/20.93",32),
                new Probnik1 ("Камера разворота","-",33),
                new Probnik1 ("Камера разворота на уклоне","-",34),
                new Probnik1 ("Камера разворота (погрузки)","-",35)
        );

        StailStolb(stolb1,stolb2);
        StailStolb(stolbGNR1,stolbGNR2);
        StailStolb(stolbGPR1,stolbGPR2);
        StailStolb(stolb1V,stolb2V);

        stolb11.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        stolb21.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        Tablshen.setItems(tabl); TablshenV.setItems(tablV);
        TablshenGPR.setItems(tablGPR);TablshenGNR.setItems(tablGNR);
        Tablshen1.setItems(tabl1);
        clikTabl(Tablshen); clikTabl(TablshenGPR); clikTabl(TablshenGNR); clikTabl(TablshenV);

        Tablshen1.setOnMouseClicked(mouseEvent -> {
            Probnik1 selectedPerson = Tablshen1.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Отображаем значение из первого столбца ("Имя") в TextField
                sehen.setText(String.valueOf(selectedPerson.getNety()));
                idi.setText(String.valueOf(selectedPerson.getId()));
                sehen.setVisible(true);
                L_sehen.setVisible(true);
                dlina.setText("0");
                dlina.setVisible(false);
                L_dlina.setVisible(false);
               // animateNodes(true);
            }
        });
    }
    private void getStl(TableColumn<Probnik, String> X1,TableColumn<Probnik, Double> X2,TableColumn<Probnik, Double> X3){
        X1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVirobotka()));
        X2.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlohad()).asObject());
        X3.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getId()).asObject());
    }
    private void StailStolb(TableColumn<Probnik, String> X1,TableColumn<Probnik, Double> X2){
        X1.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
        X2.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(item));
                setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 14px;");
            }
        });
    }
    private void clikTabl( TableView<Probnik> XX){
        XX.setOnMouseClicked(mouseEvent -> {
            Probnik selectedPerson = XX.getSelectionModel().getSelectedItem();
            if (selectedPerson != null) {
                // Отображаем значение из первого столбца ("Имя") в TextField
                sehen.setText(String.valueOf(selectedPerson.getPlohad()));
                idi.setText(String.valueOf(selectedPerson.getId()));
                sehen.setVisible(true);
                L_sehen.setVisible(true);
                dlina.setVisible(true);
                L_dlina.setVisible(true);
                ChoiceBox.getItems().clear();
                Choice_Label.setText("");
                if(Choice_List.contains(idi.getText())){
                    ChoiceBox.setVisible(true);
                    Choice_Label.setVisible(true);
                    ChoiceBox.setValue("");
                    SetChoice_Box(idi.getText());
                }
                else {ChoiceBox.setVisible(false);
                    Choice_Label.setVisible(false);
                ChoiceBox.setValue("-");}
            }
        });
    }
    //"9","10","11","12","13","14" ,  "15","16","17","18","20","22","23","24","25"
    private void SetChoice_Box(String Pas){
        switch (Pas) {
            case "9","10","11","12","13","14"  -> {

                ChoiceBox.getItems().addAll("ГКР","ГПР");
                Choice_Label.setText("Назначение выработки");
            }

            case "15","16","17","18","20","22","23","24","25"-> {
                ChoiceBox.getItems().addAll("меньше 3 месяцев","больше 3 месяцев");
                Choice_Label.setText("Срок службы");
            }
        };
    }
    private void setupImageHandlers() {
        AllsetupImageHandlers("План",nomer.getText(),hintPlan,planVNS, planVNSNE, PlanVKL, PlanVKLNe, planobnov);
        AllsetupImageHandlers("Разрез",nomer.getText(),hintrazrez,
                poperVNS, poperVNSNE, PoperVKL, PoperVKLNe, poperobnov);

    }

    private void Avto_Nomber(){
        String year = String.valueOf(calendar.getValue().getYear());
        String prefix = "";
        if (ushatok.getValue().equals("Участок КПУ ГПР №1 ")) prefix = "1";
        // Получаем следующий порядковый номер из БД
        DatabaseHandler dbHandler = new DatabaseHandler();
        int nextNumber = dbHandler.getNextSequenceNumber(prefix, year);
        // Устанавливаем номер в формате "1-5-2025"
        nomer.setText(prefix + "-" + nextNumber + "-" + year);
        setupImageHandlers();
    }
}






