package com.example.proburok.job;

import com.example.proburok.New_Class.*;
import com.example.proburok.MQ.DatabaseHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PehatCOD extends Configs {
    private static final Logger log = LogManager.getLogger(PehatCOD.class);

    private String ankerPM;
    private String ankerR;
    private String zakladkaPM;
    private String zakladkaR;
    private String torkretPM; private String torkretR;

    private String karkasR;
    private String ampulaPM;
    private String ampulaR;
    private String ampulaPM2;
    private String ampulaR2;

    @FXML private TextField famGeo;
    @FXML private ImageView instr;
    @FXML private TextField cehen;
    @FXML private TextField bdname;
    @FXML private ComboBox<String> gorbox;
    @FXML private TextField katigoria;
    @FXML private TextArea opisanie;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField nomer;
    @FXML private TextField nomer1;

    @FXML private Button singUpButtun;
    @FXML private TextField privazka;
    @FXML private TextField idi;
    @FXML private TextField ush;
    @FXML private TextField dlina;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    @FXML private TextArea primhanie;
    @FXML private CheckBox cb;
    @FXML private Button singUpButtun1;
    @FXML private ImageView sxemaVKL;
    @FXML private ImageView sxemaVKLNe;
    @FXML private ImageView sxemaVNS;
    @FXML private ImageView sxemaVNSNE;
    @FXML private ImageView sxemaobnov;
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;

    @FXML private TextField katigoria1;
    @FXML private TextField opisanie1;

    @FXML private Button singUpButtun2;

    @FXML private TextField nomer11;
    @FXML private TextField interval;
    @FXML private TextField dop;
    @FXML private TableView<ObservableList<StringProperty>> dataTable;
    @FXML private TableColumn<ObservableList<StringProperty>, String>  column1;
    @FXML private TableColumn<ObservableList<StringProperty>, String>  column2;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column3;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column4;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column5;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column6;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column7;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column8;
    private String OT;
    private String DO;
    @FXML private TextField Choice;
    @FXML private Label Choice_Label;

    @FXML private Label L_long;
    @FXML private Label  L_sehen;
    @FXML private Label L_Plan;
    @FXML private Label L_Razrez;
    @FXML private Label L_Sxema;
    private int templateNumber;
    private String Installation_diagram;
    private String longAnker;
    private String thicknessNabr;
    private String [] schemaM;
    @FXML
    void initialize() {
        dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        primhanie.visibleProperty().bind(cb.selectedProperty());
        singUpButtun1.visibleProperty().bind(cb.selectedProperty());
        singUpButtun2.setVisible(false);
        cb.setVisible(false);
        idi.setVisible(false);
        bdname.setVisible(false);
        ush.setVisible(false);
        famGeo.setVisible(false);
        singUpButtun1.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();
            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {
                    errors.append("- Не выбран горизонт\n");
                }
                if (selectedName == null || selectedName.isEmpty()) {
                    errors.append("- Не выбрано название выработки\n");
                }
                // Если есть ошибки - показываем их
                if (errors.length() > 0) {
                    showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }
                new DatabaseHandler().DobavleniePRIM(primhanie.getText(), selectedGor, selectedName);
                clearFields();
                //gorbox.setValue(null);
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
        setupComboBoxes();
        setupButtonAction();
        setupImageHandlers();
        singUpButtun.setVisible(false);
        instr.setOnMouseClicked(mouseEvent -> {OpenDok(Put_instr, "Инструкция_");});
        setupTable();
    }
    private void setupComboBoxes() {
        ObservableList<String> horizons = FXCollections.observableArrayList(
                dbHandler.getAllBaza().stream()
                        .map(Baza::getGORIZONT)
                        .distinct()
                        .collect(Collectors.toList())
        );
        gorbox.setItems(horizons);

        gorbox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {

                Choice_Label.setText("");
                updateNamesComboBox(newVal);
            }
        });
        namebox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null  && !newVal.isEmpty() && gorbox.getValue() != null) {

                Choice_Label.setText("");
                populateFields(gorbox.getValue(), newVal);
                cb.setVisible(true);

            }
        });

        addTextChangeListener(nomer);addTextChangeListener(bdname);
        addTextChangeListener(katigoria);addTextChangeListener(cehen);
        addTextChangeListener(nomer1);addTextChangeListener(privazka);
        addTextChangeListener(dlina);
    }

    private void addTextChangeListener(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            checkFieldsAndUpdateButton();
        });
    }
    private void checkFieldsAndUpdateButton() {
        boolean allValid = validateRequiredFields() &&
                gorbox.getValue() != null &&
                namebox.getValue() != null;
        singUpButtun.setDisable(!allValid);
    }
    private void updateNamesComboBox(String horizon) {
        ObservableList<String> names = FXCollections.observableArrayList(
                dbHandler.poiskName(horizon).stream()
                        .map(Baza::getNAME)
                        .distinct()
                        .collect(Collectors.toList())
        );
        clearFields();
        namebox.setItems(names);
    }

    private void populateFields(String horizon, String name) {
        Baza data = dbHandler.danii(horizon, name);
        if (data != null) {
            updateUI(data);
        } else {
            clearFields();
            singUpButtun.setVisible(false);
        }
    }


    private void updateUI(Baza data) {
        Platform.runLater(() -> {
            try {
                nomer.setText(data.getNOMER());
                bdname.setText(data.getNAME_BD());
                katigoria.setText(data.getKATEGORII());
                opisanie.setText(data.getOPISANIE());
                cehen.setText(data.getSEHEN());
                privazka.setText(data.getPRIVIZKA());
                nomer1.setText(data.getTIPPAS());
                dlina.setText(data.getDLINA());
                primhanie.setText(data.getPRIM());
                idi.setText(data.getIDI());
                ush.setText(data.getUHASTOK());
                famGeo.setText(data.getSLOI());

                setupImageHandlers();
                // Обновляем изображения
                Choice.setText(data.getUGOL());

                if(Choice_List.contains(idi.getText())){
                    Choice.setVisible(true);
                    Choice_Label.setVisible(true);
                    SetChoice_Box(idi.getText(),katigoria.getText(),nomer1.getText());
                }
                else {Choice.setVisible(false);
                    Choice_Label.setVisible(false);
                    Installation_diagram=getUstanovka(nomer1.getText());
                  }
                if(soprigenii.contains(idi.getText())){
                    cehen.setVisible(false);
                    dlina.setVisible(false);
                    L_long.setVisible(false);
                    L_sehen.setVisible(false);
                }else {
                      cehen.setVisible(true);
                      dlina.setVisible(true);
                     L_long.setVisible(true);
                    L_sehen.setVisible(true);
                }
                // Проверяем заполненность полей
                boolean allValid = validateRequiredFields();
                singUpButtun.setVisible(allValid);
                singUpButtun.setDisable(!allValid);

                if (!allValid && primhanie.getText() != null && !primhanie.getText().isEmpty()) {

                    showAlert(primhanie.getText());
                }
            } catch (Exception e) {
                log.error("Error updating UI", e);
                showAlert("Ошибка при обновлении данных: " + e.getMessage());
            }

            loadDataFromDatabase(data.getNOMER(),dataTable);
        });
    }

    private void SetChoice_Box(String id,String kat,String pas){
        switch (id) {
            case "9","10","11","12","13","14"  -> Choice_Label.setText("Назначение выработки");
            case "15","16","17","18","20","22","23","24","25"-> Choice_Label.setText("Срок службы");
        }
        if (kat.equals("1")&& Choice.getText().equals("ГПР")){
            Installation_diagram=getUstanovka_DOP(pas);
        } else if (kat.equals("4")&& Choice.getText().equals("меньше 3 месяцев")){
            Installation_diagram=getUstanovka_DOP(pas);
        } else if (kat.equals("2")&& id.equals("16") &&Choice.getText().equals("меньше 3 месяцев")){
        Installation_diagram=getUstanovka_DOP(pas);
        } else if (kat.equals("2")&&id.equals("17") &&Choice.getText().equals("меньше 3 месяцев")){
            Installation_diagram=getUstanovka_DOP(pas);
        }else {  Installation_diagram=getUstanovka(pas);}
    }
    private boolean validateRequiredFields() {
        return isFieldValid(nomer) &&
                isFieldValid(bdname) &&
                isFieldValid(katigoria) &&
                isFieldValid(cehen) &&
                isFieldValid(nomer1) &&
                isFieldValid(dlina) &&
                isFieldValid(ush) &&
                isFieldValid(privazka)&&
                isPrimValid(primhanie.getText());
    }
    private boolean isFieldValid(TextField field) {
        return field != null && field.getText() != null && !field.getText().trim().isEmpty();
    }
    private boolean isPrimValid(String tx) {
        if (tx == null) return true;

        return switch(tx.trim()) {
            case "Требуется геологическое описание"-> false;
            default -> true;
        };
    }
    private void clearFields() {
      //  namebox.setValue(null);
        singUpButtun2.setVisible(false);

        Choice_Label.setText("");
        nomer.clear();bdname.clear();
        katigoria.clear();opisanie.setText("");
        cehen.clear();nomer1.clear();
        privazka.clear();dlina.clear();
        primhanie.clear();cb.setSelected(false);
        idi.clear();ush.clear();
        this.ankerPM = "";this.ankerR = "";
        this.zakladkaPM = "";this.zakladkaR = "";
        this.torkretPM = "";this.karkasR = "";
        this.ampulaPM = "";this.ampulaR = "";
        this.ampulaPM2 = "";this.ampulaR2 = "";
        katigoria1.clear();opisanie1.setText("");
        setupImageHandlers();
        famGeo.setText("");
        Choice.setText("");
        clearTable(dataTable);

    }
    private void setupButtonAction() {
        singUpButtun.setOnAction(event -> handleDocumentGeneration());
        singUpButtun2.setOnAction(event -> handleDocumentGeneration_dop());
    }
    private void handleDocumentGeneration() {
        setupImageHandlers();
        try {
            validateInputs();
            try {
                if (validateInput_START()) {
                    generateWordDocument(
                    );
                }
            } catch (IOException | InvalidFormatException e) {
                handleError(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
           System.out.print("FFFFF");
            showAlert("Произошла ошибка: " + e.getMessage());
        }
    }
    private void validateInputs() throws ValidationException {
        InputData data = new InputData();
        data.selectedGor = gorbox.getValue();
        data.selectedName = namebox.getValue();
        data.kategoriyaValue = katigoria.getText();
        data.opisanieValue = opisanie.getText();
        data.selectPrivizka = privazka.getText();
        data.dlinaValue = dlina.getText();
        data.planVisbl1=sxemaVNS;
        data.planVisbl2=sxemaVNSNE;
        validateRequiredFields_Chek(data,"pethat");
    }


    private void validateInputs_dop() throws ValidationException {
        InputData data = new InputData();
        data.selectedGor = gorbox.getValue();
        data.selectedName = namebox.getValue();
        data.kategoriyaValue = katigoria1.getText();
        data.opisanieValue = opisanie1.getText();
        data.selectPrivizka = privazka.getText();
        data.dlinaValue = interval.getText();
        data.planVisbl1=sxemaVNS;
        data.planVisbl2=sxemaVNSNE;
        validateRequiredFields_Chek(data,"pethat");
    }


    private void handleDocumentGeneration_dop() {
        setupImageHandlers();
        try {
            validateInputs_dop();
            try {
                if (validateInput_START()) {
                    generateWordDocument_dop(
                    );
                }
            } catch (IOException | InvalidFormatException e) {
                handleError(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            showAlert("Произошла ошибка: " + e.getMessage());
        }
    }

    private boolean validateInput_START() {
        if (gorbox.getValue() == null || namebox.getValue() == null) {
            showAlert( "Выберите горизонт и название!");
            return false;
        }
        return true;
    }
    private void rashet(String list,String DD) throws ParseException {
        // Проверка входных данных
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }
        if (dlina.getText() == null || dlina.getText().trim().isEmpty()) {
            throw new ParseException("Значение длины не может быть пустым", 0);
        }

        switch (list) {
            case "1","7","13","54","66","70" -> setTabl(7.0,  0.3,0.0);
            case"19","22", "34", "37", "68","72" -> setTabl(5.0, 0.2,0.0);
            case "56","58","61","63" -> setTabl(4.0, 0.1,5.5);
            case "59","64" -> setTabl(4.0, 0.2,5.5);
            case "57","62"-> setTabl(4.4, 0.2,5.5);
            case "20", "23", "38", "43", "76", "80", "40" -> setTabl(6.0, 0.2,0.0);
            case "25","31","46","50" -> setTabl(6.0, 0.3,0.0);
            case "60","65" -> setTabl(6.6, 0.0,0.0);
            case "24","39" -> setTabl(7.8, 0.5,0.0);
            case "35", "74", "78", "82", "41" -> setTabl(8.0, 0.2,0.0);
            case "4","10","16","28","44","83","84" -> setTabl(8.0, 0.3,0.0);

            case "21","67","69","71","73" -> setTabl(8.9, 0.5,0.0);
            case "42"-> setTabl(8.9, 0.4,0.0);
            case "2","8","14","29","32" -> setTabl(9.0, 0.3,0.0);
            case "5","11","17","48"-> setTabl(10.0, 0.3,0.0);
            case "52" -> setTabl(11.0, 0.3,0.0);
            case "47","75","77","79","81" -> setTabl(10.0, 0.6,0.0);
            case "36","45" -> setTabl(11.1, 0.6,0.0);
            case "26" -> setTabl(12.0, 0.3,0.0);
            case "3","9","15","30","51" -> setTabl(12.2, 0.7,0.0);
            case "33"-> setTabl(12.2, 0.6,0.0);
            case "6","12","18","55" -> setTabl(13.3, 0.8,0.0);
            case "49"-> setTabl(13.3, 0.7,0.0);
            case "85","86","87" -> setTabl(14.0, 0.4,0.0);
            case "27","53" -> setTabl(14.4, 0.8,0.0);
            default ->  setTabl(0.0, 0.0,0.0);
        }

        // Обработка ввода с заменой запятых на точки
        String input = DD.trim().replace(',', '.');

        try {
            double dlina_Dobl = Double.parseDouble(input);
            double ankerPM_Dobl = Double.parseDouble(this.ankerPM);
            double zakladkaPM_Dobl = Double.parseDouble(this.zakladkaPM);
            double nabrizgPM_Dobl = Double.parseDouble(this.torkretPM);

            double ankerR_Doble = dlina_Dobl * ankerPM_Dobl;
            double zakladkaR_Doble = dlina_Dobl * zakladkaPM_Dobl;
            double nabrizgR_Doble = dlina_Dobl * nabrizgPM_Dobl;
            double ampulaPM_Dobl = ankerPM_Dobl * getAmpul(idi.getText()) ;
            double ampulaR_Doble = dlina_Dobl * ampulaPM_Dobl;

            double ampulaPM2_Dobl = zakladkaPM_Dobl * getAmpul(idi.getText()) ;
            double ampulaR2_Doble = dlina_Dobl * ampulaPM2_Dobl;


            this.ankerR = String.format(Locale.US, "%.0f", Math.ceil(ankerR_Doble));
            this.zakladkaR = String.format(Locale.US, "%.0f",zakladkaR_Doble);
            this.torkretR = String.format(Locale.US, "%.1f", Math.ceil(nabrizgR_Doble));
            this.ampulaPM = String.format(Locale.US, "%.0f", ampulaPM_Dobl);
            this.ampulaR = String.format(Locale.US, "%.0f", Math.ceil(ampulaR_Doble));

            this.ampulaPM2 = String.format(Locale.US, "%.0f", ampulaPM2_Dobl);
            this.ampulaR2 = String.format(Locale.US, "%.0f", Math.ceil(ampulaR2_Doble));
            System.out.println(this.ampulaPM + "  "+ this.ampulaR );

        } catch (NumberFormatException e) {
            throw new ParseException("Некорректный числовой формат", 0);
        }
    }
    private void setTabl(Double anker,  Double nabrizk, Double zakladka) {
        this.ankerR = "";this.zakladkaR = "";
        this.karkasR = "";this.ampulaPM = "";
        this.ampulaR = "";
        this.ankerPM = String.valueOf(anker);
        this.zakladkaPM= String.valueOf(zakladka);
        this.torkretPM = String.valueOf(nabrizk);
    }
    private void setTabl_SOPR(Double anker2_5, Double amp2_5, Double tor,Double anker1_8,Double amp1_8) {

        this.ankerR =String.valueOf(anker2_5);
        this.ampulaR=String.valueOf(amp2_5);
        this.torkretR = String.valueOf(tor);

        this.ankerPM = String.valueOf(anker1_8);
        this.ampulaPM = String.valueOf(amp1_8);

    }

    private void rashet_SOPR(String list) throws ParseException {
        // Проверка входных данных
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }
        switch (list) {
            case "91","92" -> setTabl_SOPR(18.0,90.0,2.7,0.0,0.0);
            case "88","89" -> setTabl_SOPR(18.0,90.0,3.2,0.0,0.0);
            case "100","101" -> setTabl_SOPR(18.0,90.0,3.7,0.0,0.0);
            case "93" -> setTabl_SOPR(18.0,90.0,6.8,0.0,0.0);
            case "105" -> setTabl_SOPR(18.0,90.0,8.3,70.0,280.0);
            case "108" -> setTabl_SOPR(18.0,90.0,9.3,70.0,280.0);

            case "103" -> setTabl_SOPR(15.0,75.0,2.7,42.0,168.0);
            case "104" -> setTabl_SOPR(15.0,75.0,4.7,56.0,224.0);
            case "109" -> setTabl_SOPR(15.0,75.0,4.3,42.0,168.0);
            case "106" -> setTabl_SOPR(15.0,75.0,3.6,36.0,144.0);
            case "107" -> setTabl_SOPR(15.0,75.0,5.5,48.0,192.0);
            case "110" -> setTabl_SOPR(15.0,75.0,6.3,56.0,224.0);

            case "96" -> setTabl_SOPR(19.0,95.0,9.3,0.0,0.0);
            case "94","95" -> setTabl_SOPR(20.0,100.0,3.7,0.0,0.0);
            case "102" -> setTabl_SOPR(21.0,105.0,9.4,0.0,0.0);
            case "90" -> setTabl_SOPR(24.0,120.0,8.1,0.0,0.0);

            case "111" -> setTabl_SOPR(24.0,120.0,10.8,70.0,280.0);

            case "97","98" -> setTabl_SOPR(30.0,150.0,4.5,0.0,0.0);
            case "99" -> setTabl_SOPR(33.0,165.0,9.3,0.0,0.0);
            default ->  setTabl(0.0, 0.0, 0.0);
        }

    }

    private void generateWordDocument() throws IOException, InvalidFormatException, ParseException {

        Map<String, String> tableData = new HashMap<>();
        tableData.put("${nomer}", nomer.getText());
        tableData.put("${name}", bdname.getText());
        tableData.put("${sehen}", cehen.getText());
        tableData.put("${ush}", ush.getText().replace("Участок","").trim());
        tableData.put("${ush1}", ush.getText().replace("Участок","").trim());
        tableData.put("${gorizont}", gorbox.getValue());
        tableData.put("${privazka}", privazka.getText());
        tableData.put("${dlina}", dlina.getText());
        tableData.put("${kategorii}", katigoria.getText());
        tableData.put("${opisanie}", opisanie.getText());
        tableData.put("${interval}","0 ÷ "+ dlina.getText());
        tableData.put( "${FamGeo}", famGeo.getText());

        tableData.put("${konstrk}", getIlement(idi.getText()));
        tableData.put("${obvid}",   getRESURS(HABLON_PATH_VID,  nomer1.getText() + ".jpg"));
        tableData.put("${sxematexfakt}",Installation_diagram );
        tableData.put("${plan}",   getPoto(Put + "/" + nomer.getText() + "/" + "План", 0));
        tableData.put("${poper}",  getPoto(Put + "/" + nomer.getText() + "/" + "Разрез", 0));
        tableData.put("${sxema}",  getPoto(Put + "/" + nomer.getText() + "/" + "Схема", 0));
        //${obvid}" "${konstrk}" "${sxematexfakt}"
        if (soprigenii.contains(idi.getText())) {
            rashet_SOPR(nomer1.getText());
            this.schemaM=getUstanovka_Coupling(idi.getText(),katigoria.getText());
            System.out.println(schemaM[0]);
            System.out.println(schemaM[1]);
            tableData.put("${sxematexfakt}",schemaM[0] );
            tableData.put("${sxematexfakt2}",schemaM[1] );
        } else {
            rashet(nomer1.getText(),dlina.getText());
            tableData.put("${interval}","0 ÷ "+ dlina.getText());
        }
        choiceTemplate(idi.getText(),nomer1.getText(),Choice.getText(),katigoria.getText());

        String techText = TechInstruction.forNumber(templateNumber).getText();
        String safetyText = SafetyInstruction.forNumber(templateNumber).getText();
         tableData.put("${Tech}", techText);
         tableData.put("${Safety}", safetyText);
        System.out.println( getRESURS(HABLON_PATH_VID,  nomer1.getText() + ".jpg"));
        System.out.println("Kat- "+  getIlement(idi.getText())+" TP- "+ Installation_diagram + " temp- "+templateNumber);
        // Заменяем плейсхолдер на таблицу
        String outputFileName = OUTPUT_PATH + nomer.getText() + "_" + gorbox.getValue() + ".docx";
        File outputFile = new File(outputFileName);
        try {
            TemplateResource templateResource;
            // Получаем поток входных данных для ресурса
            //InputStream inputStream = getClass().getResourceAsStream(TEMPLATE_PATH);
            if (soprigenii.contains(idi.getText())) {
                templateResource = new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH1S), TEMPLATE_PATH1S);
            } else {
                templateResource = new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH1), TEMPLATE_PATH1);;
            }
            InputStream inputStream = templateResource.getInputStream();
            String templatePath = templateResource.getTemplatePath();
            if (inputStream == null) {
                throw new FileNotFoundException("Ресурс не найден: " + templatePath);
            }
            // Создаем временный файл
            Path tempFile = Files.createTempFile("hablon_", ".docx");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            try (FileInputStream template = new FileInputStream(tempFile.toFile());
                 XWPFDocument document = new XWPFDocument(template)) {
                replaceTablWord.replacePlaceholderWithTableSimpleFixed(document, "${table_placeholder}", get_WorTablr(katigoria.getText()),templateNumber,idi.getText());
                replaceTablWord.replacePlaceholderWithTableGrafic(document, "${scheduleTable}", get_GraficTablr(),templateNumber);
                replaceTablePlaceholders(document, tableData);

                try (FileOutputStream out = new FileOutputStream(outputFile)) {
                    document.write(out);
                }
                // Автоматическое открытие файла после сохранения
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (outputFile.exists()) {
                        desktop.open(outputFile);
                    } else {
                        System.err.println("Файл не найден: " + outputFile.getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Ошибка генерации документа: " + e.getMessage());
            }
            // Опционально: удаление временного файла после использования
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println("Не удалось удалить временный файл: " + tempFile);
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при открытии документа", e);
        }
        String prim = "Паспорт создан";
        new DatabaseHandler().DobavleniePRIM(prim, gorbox.getValue(), namebox.getValue());
        clearFields();
    }
    private void generateWordDocument_dop() throws IOException, InvalidFormatException, ParseException {

        Map<String, String> tableData = new HashMap<>();
        tableData.put("${nomer}", nomer.getText());
        tableData.put("${name}", bdname.getText());
        tableData.put("${sehen}", cehen.getText().replace(".",","));
        tableData.put("${ush}", ush.getText().replace("Участок","").trim());
        tableData.put("${ush1}", ush.getText().replace("Участок","").trim());
        tableData.put("${gorizont}", gorbox.getValue());
        tableData.put("${privazka}", privazka.getText());
        tableData.put("${dlina}", dlina.getText());
        tableData.put("${dnomer}", dop.getText());
        tableData.put( "${FamGeo}", famGeo.getText());

        tableData.put("${kategorii}", katigoria1.getText());
        tableData.put("${opisanie}", opisanie1.getText());
        tableData.put("${interval}", interval.getText());
        tableData.put("${interval1}", interval.getText());
        tableData.put("${plan}",   getPoto(Put + "/" + nomer.getText() + "/" + "План_"+interval.getText(), 0));
        tableData.put("${poper}",  getPoto(Put + "/" + nomer.getText() + "/" + "Разрез_"+interval.getText(), 0));
        tableData.put("${sxema}",  getPoto(Put + "/" + nomer.getText() + "/" + "Схема_"+interval.getText(), 0));
        tableData.put("${konstrk}", getIlement(idi.getText()));
        tableData.put("${obvid}",   getRESURS(HABLON_PATH_VID,  nomer11.getText() + ".jpg"));
        tableData.put("${sxematexfakt}",Installation_diagram );


        if (soprigenii.contains(idi.getText())) {
            tableData.put("${interval}","");
            rashet_SOPR(nomer11.getText());
            this.schemaM=getUstanovka_Coupling(idi.getText(),katigoria1.getText());
            System.out.println(schemaM[0]);
            System.out.println(schemaM[1]);
            tableData.put("${sxematexfakt}",schemaM[0] );
            tableData.put("${sxematexfakt2}",schemaM[1] );
        } else {
            double OT_Dobl = Double.parseDouble(this.OT);
            double DO_Dobl = Double.parseDouble(this.DO);
            String SUMOTDO = String.format(Locale.US, "%.1f",DO_Dobl-OT_Dobl );
            rashet(nomer11.getText(),SUMOTDO);
            System.out.print(SUMOTDO);
        }

        choiceTemplate(idi.getText(),nomer11.getText(),Choice.getText(),katigoria1.getText());

        String techText = TechInstruction.forNumber(templateNumber).getText();
        String safetyText = SafetyInstruction.forNumber(templateNumber).getText();
        tableData.put("${Tech}", techText);
        tableData.put("${Safety}", safetyText);
        System.out.println( getRESURS(HABLON_PATH_VID,  nomer11.getText() + ".jpg"));
        System.out.println("Kat- "+  getIlement(idi.getText())+" TP- "+ Installation_diagram + " temp- "+templateNumber);
        // Заменяем плейсхолдер на таблицу
        String outputFileName = OUTPUT_PATH + nomer.getText() + "_" + gorbox.getValue() + ".docx";
        File outputFile = new File(outputFileName);
        try {
            TemplateResource templateResource;
            // Получаем поток входных данных для ресурса
            //InputStream inputStream = getClass().getResourceAsStream(TEMPLATE_PATH);
            if (soprigenii.contains(idi.getText())) {
                templateResource = new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH1S_1), TEMPLATE_PATH1S_1);
            } else {
                templateResource = new TemplateResource(getClass().getResourceAsStream(TEMPLATE_PATH1_1), TEMPLATE_PATH1_1);;
            }

            InputStream inputStream = templateResource.getInputStream();
            String templatePath = templateResource.getTemplatePath();
            if (inputStream == null) {
                throw new FileNotFoundException("Ресурс не найден: " + templatePath);
            }
            // Создаем временный файл
            Path tempFile = Files.createTempFile("hablon_", ".docx");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            try (FileInputStream template = new FileInputStream(tempFile.toFile());
                 XWPFDocument document = new XWPFDocument(template)) {
                replaceTablWord.replacePlaceholderWithTableSimpleFixed(document, "${table_placeholder}", get_WorTablr(katigoria1.getText()),templateNumber,idi.getText());
                replaceTablWord.replacePlaceholderWithTableGrafic(document, "${scheduleTable}", get_GraficTablr(),templateNumber);
                replaceTablePlaceholders(document, tableData);

                try (FileOutputStream out = new FileOutputStream(outputFile)) {
                    document.write(out);
                }
                // Автоматическое открытие файла после сохранения
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (outputFile.exists()) {
                        desktop.open(outputFile);
                    } else {
                        System.err.println("Файл не найден: " + outputFile.getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Ошибка генерации документа: " + e.getMessage());
            }
            // Опционально: удаление временного файла после использования
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println("Не удалось удалить временный файл: " + tempFile);
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при открытии документа", e);
        }
        String prim = "Дополение создан";
        new DatabaseHandler().DobavleniePRIM(prim, gorbox.getValue(), namebox.getValue());
        clearFields();
    }
    private List<List<String>> get_WorTablr(String kategori){
        getNomberAnkerandNabrizg(kategori);
        List<List<String>> tableData_1 = new ArrayList<>();
        // Заголовок таблицы
        if (templateNumber == 1 || templateNumber == 5 || templateNumber == 6){
            tableData_1.add(Arrays.asList("№", "Наименование", "Параметры", "Расход на 1,0 п.м.", "Расход на выработку", "Примечание"));

            // Данные таблицы
            tableData_1.add(Arrays.asList("1", "Анкер СПА 20В", this.longAnker+"м, ø22 мм", this.ankerPM, this.ankerR,"--- шт. в ряду"));
            tableData_1.add(Arrays.asList("2", "Шайба", "150х150 мм, ø150 мм 4 мм", this.ankerPM, this.ankerR,""));
            tableData_1.add(Arrays.asList("3", "Гайка сферическая", "D20", this.ankerPM, this.ankerR,""));
            tableData_1.add(Arrays.asList("4", "Ампула полимерная ", "Ø28 мм, 300 мм", this.ampulaPM, this.ampulaR,  getAmpul(idi.getText())+" шт. на шпур"));
            tableData_1.add(Arrays.asList("5", "Армокаркас", "950х950 мм, 12 мм", this.ankerPM, this.ankerR,""));
            if (templateNumber == 1 || templateNumber == 5){
            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("1", "Набрызгбетон ", this.thicknessNabr+" мм", this.torkretPM, this.torkretR,""));}

        } else if (templateNumber == 2 || templateNumber == 3 ){
            tableData_1.add(Arrays.asList("№", "Наименование", "Параметры", "Расход на 1,0 п.м.", "Расход на выработку", "Примечание"));

            // Данные таблицы
            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("1", "Анкер СПА 20В", this.longAnker+"м, ø20 мм", this.ankerPM, this.ankerR,"--- шт. в ряду"));
            tableData_1.add(Arrays.asList("2", "Шайба", "150х150 мм, ø150 мм 4 мм", this.ankerPM, this.ankerR,""));
            tableData_1.add(Arrays.asList("3", "Гайка сферическая", "D20", this.ankerPM, this.ankerR,""));
            tableData_1.add(Arrays.asList("4", "Ампула полимерная ", "Ø24 мм, 450 мм", this.ampulaPM, this.ampulaR,  getAmpul(idi.getText())+" шт. на шпур"));
            tableData_1.add(Arrays.asList("5", "Армокаркас", "950х950 мм, 12 мм", this.ankerPM, this.ankerR,""));
            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("1", "Анкер ФА",this.longAnker+"м, ø46 мм", this.ankerPM, this.ankerR,"--- шт. в ряду"));
            tableData_1.add(Arrays.asList("2", "Шайба", "200х200 мм, 4 мм", this.ankerPM, this.ankerR,""));
            tableData_1.add(Arrays.asList("3", "Армокаркас", "950х950 мм, 12 мм", this.ankerPM, this.ankerR,""));
            if ( templateNumber == 3 ){
                tableData_1.add(Arrays.asList("", "", "", "", "", ""));
                tableData_1.add(Arrays.asList("1", "Набрызгбетон ",  this.thicknessNabr+" мм", this.torkretPM, this.torkretR,""));
            }
            if (idi.getText().equals("18")||idi.getText().equals("20")){
                tableData_1.add(Arrays.asList("", "", "", "", "", ""));
                tableData_1.add(Arrays.asList("1", "Анкер СПА 20В", this.longAnker+"м, ø20 мм", this.zakladkaPM, this.zakladkaR,"--- шт. в ряду"));
                tableData_1.add(Arrays.asList("2", "Шайба", "150х150 мм, ø150 мм 4 мм", this.zakladkaPM, this.zakladkaR,""));
                tableData_1.add(Arrays.asList("3", "Гайка сферическая", "D20", this.zakladkaPM, this.zakladkaR,""));
                tableData_1.add(Arrays.asList("4", "Ампула полимерная ", "Ø24 мм, 450 мм", this.ampulaPM2, this.ampulaR2,  getAmpul(idi.getText())+" шт. на шпур"));
                tableData_1.add(Arrays.asList("5", "Армокаркас", "950х950 мм, 12 мм", this.zakladkaPM, this.zakladkaR,""));
                tableData_1.add(Arrays.asList("", "", "", "", "", ""));
                tableData_1.add(Arrays.asList("1", "Анкер ФА",this.longAnker+"м, ø46 мм", this.zakladkaPM, this.zakladkaR,"--- шт. в ряду"));
                tableData_1.add(Arrays.asList("2", "Шайба", "200х200 мм, 4 мм", this.zakladkaPM, this.zakladkaR,""));
                tableData_1.add(Arrays.asList("3", "Армокаркас", "950х950 мм, 12 мм", this.zakladkaPM, this.zakladkaR,""));
                if ( templateNumber == 3 ){
                    tableData_1.add(Arrays.asList("", "", "", "", "", ""));
                    tableData_1.add(Arrays.asList("1", "Набрызгбетон ",  this.thicknessNabr+" мм", this.torkretPM, this.torkretR,""));
                }
            }
            
            
        } else if (templateNumber == 4 ){
            tableData_1.add(Arrays.asList("№", "Наименование", "Параметры", "Расход на 1,0 п.м.", "Расход на выработку", "Примечание"));

            tableData_1.add(Arrays.asList("1", "Анкер ФА", this.longAnker+"м, ø46 мм", this.ankerPM, this.ankerR,"--- шт. в ряду"));
            tableData_1.add(Arrays.asList("2", "Шайба", "200х200 мм, 4 мм", this.ankerPM, this.ankerR,""));
            tableData_1.add(Arrays.asList("3", "Армокаркас", "950х950 мм, 12 мм", this.ankerPM, this.ankerR,""));
        }
        else if (templateNumber == 7 ){
            tableData_1.add(Arrays.asList("№", "Наименование", "Параметры", "Расход на выработку", "Примечание"));

            // Данные таблицы
            tableData_1.add(Arrays.asList("1", "Анкер СПА 20В", "2.5 м, ø20 мм",  this.ankerR,"крепь усиления"));
            tableData_1.add(Arrays.asList("2", "Шайба", "150х150 мм, ø150 мм 4 мм", this.ankerR,""));
            tableData_1.add(Arrays.asList("3", "Гайка сферическая", "D20",  this.ankerR,""));
            tableData_1.add(Arrays.asList("4", "Ампула полимерная ", "Ø24 мм, 450 мм",  this.ampulaR, "5 шт. на шпур"));

            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("1", "Набрызгбетон ", this.thicknessNabr+" мм",  this.torkretR,"доп. объем на раскоску"));
        } else if (templateNumber == 8 ){
            tableData_1.add(Arrays.asList("№", "Наименование", "Параметры", "Расход на выработку", "Примечание"));

            // Данные таблицы
            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("1", "Анкер СПА 20В", "2.5 м, ø20 мм",  this.ankerR,"крепь усиления"));
            tableData_1.add(Arrays.asList("2", "Шайба", "150х150 мм, ø150 мм 4 мм", this.ankerR,""));
            tableData_1.add(Arrays.asList("3", "Гайка сферическая", "D20",  this.ankerR,""));
            tableData_1.add(Arrays.asList("4", "Ампула полимерная ", "Ø24 мм, 450 мм",  this.ampulaR, "5 шт. на шпур"));

            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("1", "Анкер СПА 20В", "1.8 м, ø20 мм",  this.ankerPM,"крепь усиления"));
            tableData_1.add(Arrays.asList("2", "Шайба", "150х150 мм, ø150 мм 4 мм", this.ankerPM,""));
            tableData_1.add(Arrays.asList("3", "Гайка сферическая", "D20",  this.ankerPM,""));
            tableData_1.add(Arrays.asList("4", "Ампула полимерная ", "Ø24 мм, 450 мм",  this.ampulaPM, "4 шт. на шпур"));
            tableData_1.add(Arrays.asList("5", "Армокаркас", "950х950 мм, 12 мм", this.ankerPM,""));

            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("1", "Анкер ФА", "1.8 м, ø46 мм",  this.ankerPM,""));
            tableData_1.add(Arrays.asList("2", "Шайба", "200х200 мм, 4 мм", this.ankerPM,""));
            tableData_1.add(Arrays.asList("3", "Армокаркас", "950х950 мм, 12 мм", this.ankerPM,""));

            tableData_1.add(Arrays.asList("", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("1", "Набрызгбетон ", this.thicknessNabr+" мм",  this.torkretR,"доп. объем на раскоску + ниша разворота"));
        }
        return tableData_1;
    }
    private List<List<String>> get_GraficTablr(){
        List<List<String>> tableData_1 = new ArrayList<>();
        // Заголовок таблицы 4
        if (templateNumber == 5 || templateNumber == 6 ) {
            tableData_1.add(Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("", " ", "20", "21", "22", "23","24","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"));
            // Данные таблицы1
            tableData_1.add(Arrays.asList("1", "Проветривание","Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y"));

            tableData_1.add(Arrays.asList("2", "Приведение выработки в безопасное состояние, оборка заколов", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y"));
            tableData_1.add(Arrays.asList("3", "Подготовка оборудования", "N", "Y", "N", "N", "N", "N", "O", "N", "N", "N", "N", "N","N", "Y", "N", "N", "N", "N", "O", "N", "N", "N", "N", "N"));
            tableData_1.add(Arrays.asList("4", "Обустройство рабочего места", "N", "N", "Y", "Y", "N", "N", "O", "N", "N", "N", "N", "N","N", "N", "Y", "Y", "N", "N", "O", "N", "N", "N", "N", "N"));
            tableData_1.add(Arrays.asList("5", "Доставка крепежных материалов","N", "N", "N", "N", "Y", "N", "O", "N", "N", "N", "N", "N","N", "N", "N", "N", "Y", "N", "O", "N", "N", "N", "N", "N"));
            tableData_1.add(Arrays.asList("6", "Крепление","N", "N", "N", "N", "N", "Y", "O", "Y", "Y", "Y", "Y", "N","N", "N", "N", "N", "N", "Y", "O", "Y", "Y", "Y", "Y", "N"));
            tableData_1.add(Arrays.asList("7", "Уборка оборудования", "N", "N", "N", "N", "N", "N", "O", "N", "N", "N", "N", "Y","N", "N", "N", "N", "N", "N", "O", "N", "N", "N", "N", "Y"));

        } else {
            tableData_1.add(Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
            tableData_1.add(Arrays.asList("", " ", "20", "21", "22", "23","24","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"));
            // Данные таблицы1
            tableData_1.add(Arrays.asList("1", "Проветривание", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y"));

            tableData_1.add(Arrays.asList("2", "Приведение выработки в безопасное состояние, оборка заколов", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y", "Y"));
            tableData_1.add(Arrays.asList("3", "Подготовка оборудования","N", "Y", "N", "N", "N", "N", "O", "N", "N", "N", "N", "N","N", "Y", "N", "N", "N", "N", "O", "N", "N", "N", "N", "N"));
            tableData_1.add(Arrays.asList("4", "Уборка горной массы", "N", "N", "Y", "Y", "N", "N", "O", "N", "N", "N", "N", "N","N", "N", "Y", "Y", "N", "N", "O", "N", "N", "N", "N", "N"));
            tableData_1.add(Arrays.asList("5", "Доставка крепежных материалов","N", "N", "N", "N", "Y", "N", "O", "N", "N", "N", "N", "N","N", "N", "N", "N", "Y", "N", "O", "N", "N", "N", "N", "N"));
            tableData_1.add(Arrays.asList("6", "Крепление", "N", "N", "N", "N", "Y", "Y", "O", "Y", "N", "N", "N", "N","N", "N", "N", "N", "Y", "Y", "O", "Y", "N", "N", "N", "N"));
            tableData_1.add(Arrays.asList("7", "Уборка оборудования","N", "N", "N", "N", "N", "N", "O", "N", "Y", "N", "N", "N","N", "N", "N", "N", "N", "N", "O", "N", "Y", "N", "N", "N"));
            tableData_1.add(Arrays.asList("8", "Бурение забоя, заряжание","N", "N", "N", "N", "N", "N", "O", "N", "N", "Y", "Y", "N","N", "N", "N", "N", "N", "N", "O", "N", "N", "Y", "Y", "N"));
            tableData_1.add(Arrays.asList("9", "Взрывные работы","N", "N", "N", "N", "N", "N", "O", "N", "N", "N", "N", "Y","N", "N", "N", "N", "N", "N", "O", "N", "N", "N", "N", "Y"));
        }
        return tableData_1;
    }
    private void replaceTablePlaceholders(XWPFDocument doc, Map<String, String> tableData) {
        int W_IMG = 0;
        int H_IMG = 0;
        boolean Reso = false;
        String pf =getIlement(idi.getText());
        // Перебираем все таблицы в документе
        for (XWPFTable table : doc.getTables()) {
            // Перебираем все строки в таблице
            for (XWPFTableRow row : table.getRows()) {
                // Перебираем все ячейки в строке
                for (XWPFTableCell cell : row.getTableCells()) {
                    // Перебираем все абзацы в ячейке
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        // Перебираем все плейсхолдеры
                        for (Map.Entry<String, String> entry : tableData.entrySet()) {
                            String placeholder = entry.getKey();
                            String value = entry.getValue();
                            //${obvid}" "${konstrk}" "${sxematexfakt}"

                            // Если абзац содержит плейсхолдер - заменяем его
                            if (paragraph.getText().contains(placeholder)) {
                                boolean isImagePlaceholder = true;

                                switch (placeholder) {
                                    case "${plan}": case "${poper}": W_IMG = 470;H_IMG = 330;Reso = false;
                                        break;
                                    case "${sxema}": W_IMG = 460;H_IMG = 620;Reso = false;
                                        break;
                                    case "${sxematexfakt}", "${sxematexfakt2}": W_IMG = 460;H_IMG = 600;Reso = true;
                                        break;
                                    case "${obvid}": W_IMG = 700;H_IMG = 420;Reso = true;
                                        break;
                                    case "${konstrk}": if (pf.equals("/com/example/proburok/hablon/ilement/4.jpg")
                                            || pf.equals("/com/example/proburok/hablon/ilement/5.jpg")
                                            || pf.equals("/com/example/proburok/hablon/ilement/9.jpg")) {
                                        W_IMG = 470;H_IMG = 500;Reso = true;}
                                        else{ W_IMG = 470;H_IMG = 300;Reso = true;}
                                        break;
                                    default:
                                        replaceTextInParagraph(paragraph, placeholder, value);
                                        isImagePlaceholder = false;
                                        break;
                                }

                                if (isImagePlaceholder) {
                                    replaceImageInParagraph(paragraph, placeholder, value, W_IMG, H_IMG, Reso);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void replaceTextInParagraph(XWPFParagraph p, String placeholder, String replacement) {
        String text = p.getText();
        if (text == null || !text.contains(placeholder)) return;

        // Удаляем все runs
        for (int i = p.getRuns().size() - 1; i >= 0; i--) {
            p.removeRun(i);
        }

        // Разбиваем текст на строки, если есть \n
        if (replacement.contains("\n")) {
            String[] lines = replacement.split("\n");
            for (int i = 0; i < lines.length; i++) {
                XWPFRun run = p.createRun();
                run.setText(lines[i]);
                run.setFontFamily("Times New Roman");
                run.setFontSize(12);
                if (i < lines.length - 1) {
                    run.addBreak(); // перенос строки
                }
            }
        } else if (List.of("${ush}", "${FamGeo}","${kategorii}", "${opisanie}", "${interval}").contains(placeholder)) {
            XWPFRun run = p.createRun();
            run.setText(text.replace(placeholder, replacement));
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
        }
        else{ XWPFRun run = p.createRun();
            run.setText(text.replace(placeholder, replacement));
            run.setFontFamily("Times New Roman");
            run.setFontSize(14);}
    }

    private void replaceImageInParagraph(XWPFParagraph p, String placeholder, String imagePath, int width, int height, boolean isResource) {
        replaceTextInParagraph(p, placeholder, "");

        if (imagePath == null || imagePath.isEmpty()) {
            System.err.println("Путь к изображению пуст");
            return;
        }
        try (InputStream is = isResource
                ? getClass().getResourceAsStream(imagePath.startsWith("/") ? imagePath : "/" + imagePath)
                : new FileInputStream(imagePath)) {
            if (is == null) {
                System.err.println("Источник не найден: " + imagePath);
                return;
            }
            byte[] imageBytes = IOUtils.toByteArray(is);
            PictureData.PictureType type = isResource
                    ? determineImageType(imageBytes)
                    : getImageType(imagePath);

            XWPFRun run = p.createRun();
            run.addPicture(
                    new ByteArrayInputStream(imageBytes),
                    type.ordinal(),
                    "image",
                    Units.toEMU(width),
                    Units.toEMU(height)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private PictureData.PictureType determineImageType(byte[] imageData) {
        if (imageData.length >= 4 &&
                imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
            return PictureData.PictureType.JPEG;
        } else if (imageData.length >= 8 &&
                new String(imageData, 0, 8).equals("\211PNG\r\n\032\n")) {
            return PictureData.PictureType.PNG;
        }
        return PictureData.PictureType.JPEG; // fallback
    }
    // Определение типа изображения
    private PictureData.PictureType getImageType(String imagePath) {
        String ext = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
        return switch (ext) {
            case "jpg", "jpeg" -> PictureData.PictureType.JPEG;
            case "png" -> PictureData.PictureType.PNG;
            case "gif" -> PictureData.PictureType.GIF;
            case "emf" -> PictureData.PictureType.EMF;
            case "wmf" -> PictureData.PictureType.WMF;
            default -> throw new IllegalArgumentException("Unsupported image type: " + ext);
        };
    }
    // Сформируйте полный путь к ресурсу
    public String getRESURS(String resourcePath, String fileName) {
        // Возвращаем путь в формате "/папка/файл"
        if (fileName == null) return "";
        return (resourcePath.startsWith("/") ? "" : "/") +
                resourcePath.replace("\\", "/") +
                "/" + fileName;
    }
    public String getUstanovka(String list) {
        return switch (list) {
            case "13","14","16","17","19","20","22","23"-> getRESURS(HABLON_PATH_USTANOVKA, "1.jpg");
            case "15", "18", "21","24"-> getRESURS(HABLON_PATH_USTANOVKA, "2.jpg");
            case "1","2","4","5","7","8","10","11" -> getRESURS(HABLON_PATH_USTANOVKA, "3.jpg");
            case "3", "6","9","12","49","51","53","55"-> getRESURS(HABLON_PATH_USTANOVKA, "4.jpg");
            case "25","26","28","29","31","32","34","35","37","38","40","41","44","46"
                    -> getRESURS(HABLON_PATH_USTANOVKA, "5.jpg");
            case "27","30","33","36","39","42","45","47" -> getRESURS(HABLON_PATH_USTANOVKA, "6.jpg");
            case "43"  -> getRESURS(HABLON_PATH_USTANOVKA, "7.jpg");
            case "48", "52", "50", "54" -> getRESURS(HABLON_PATH_USTANOVKA, "9.jpg");
            case "56","61"-> getRESURS(HABLON_PATH_USTANOVKA, "10.jpg");
            case "57","59","62","64"-> getRESURS(HABLON_PATH_USTANOVKA, "11.jpg");
            case "58","63"-> getRESURS(HABLON_PATH_USTANOVKA, "12.jpg");
            case "60","65"-> getRESURS(HABLON_PATH_USTANOVKA, "13.jpg");
            case "66","70","74","78"  -> getRESURS(HABLON_PATH_USTANOVKA, "14.jpg");
            case "67","69","71","73","75","77","79","81"   -> getRESURS(HABLON_PATH_USTANOVKA, "15.jpg");
            case "68","72","76","80"-> getRESURS(HABLON_PATH_USTANOVKA, "16.jpg");
            case "82","85"-> getRESURS(HABLON_PATH_USTANOVKA, "17.jpg");
            case "83","84","86","87"-> getRESURS(HABLON_PATH_USTANOVKA, "18.jpg");

            default ->  "null";
        };
    }

    public String getUstanovka_DOP(String list) {
        return switch (list) {
            case "25","28","31","34","37","40","46"  -> getRESURS(HABLON_PATH_USTANOVKA, "7.jpg");
            case "58","63"-> getRESURS(HABLON_PATH_USTANOVKA, "10.jpg");
            case "50","54","48","52" -> getRESURS(HABLON_PATH_USTANOVKA, "8.jpg");
            case "68","72","76","80"-> getRESURS(HABLON_PATH_USTANOVKA, "14.jpg");
            default ->  null;
        };
    }

    public String getIlement(String list) {
        return switch (list) {
            case  "5", "6", "7","8"-> getRESURS(HABLON_PATH_ILIMENT, "1.jpg");
            case "1", "2","3", "4" -> getRESURS(HABLON_PATH_ILIMENT, "2.jpg");
            case "26", "27" -> getRESURS(HABLON_PATH_ILIMENT, "3.jpg");
            case "9","10","11", "12","13","14","15"  -> getRESURS(HABLON_PATH_ILIMENT, "4.jpg");
            case "16", "17" -> getRESURS(HABLON_PATH_ILIMENT, "5.jpg");
            case "18", "19","20", "21" -> getRESURS(HABLON_PATH_ILIMENT, "6.jpg");
            case "22", "23","24", "25" -> getRESURS(HABLON_PATH_ILIMENT, "7.jpg");
            case "28", "29","30", "31","32" -> getRESURS(HABLON_PATH_ILIMENT, "8.jpg");
            case "33","34","35"-> getRESURS(HABLON_PATH_ILIMENT, "9.jpg");
            default -> null;
        };
    }
    public String[] getUstanovka_Coupling(String Idi, String kat) {
        String scheme1 = "-";
        String scheme2 = "-";
        if (coupling8.contains(Idi)) {
            if ("1".equals(kat)) {
                scheme1 = getRESURS(HABLON_PATH_USTANOVKA, "19.jpg");
                scheme2 = getRESURS(HABLON_PATH_USTANOVKA, "21.jpg");
            } else if ("2".equals(kat)) {
                scheme1 = getRESURS(HABLON_PATH_USTANOVKA, "19.jpg");
                scheme2 = getRESURS(HABLON_PATH_USTANOVKA, "22.jpg");
            } else if ("3".equals(kat)) {
                scheme1 = getRESURS(HABLON_PATH_USTANOVKA, "20.jpg");
                scheme2 = getRESURS(HABLON_PATH_USTANOVKA, "23.jpg");
            }
        }
        else if (coupling7.contains(Idi)) {
            if ("3".equals(kat)) {
                scheme1 = getRESURS(HABLON_PATH_USTANOVKA, "20.jpg");
            } else {
                scheme1 = getRESURS(HABLON_PATH_USTANOVKA, "19.jpg");
            }
            scheme2 = "-";
        }
        return new String[]{scheme1, scheme2};
    }
    public int getAmpul(String list) {
        return switch (list) {
            case "26", "27" -> 3;
            case "5", "6", "7","8","9","10","11", "12","13","14","15","16",
                 "17","18", "19","20", "21", "22", "23","24", "25" -> 4;
            case "1", "2","3", "4" ->5;
            default -> 0;
        };
    }
    public  String getPoto(String imagePath, int i) {
        if (imagePath == null || imagePath.isEmpty()) return "";
        try {
            File folder = new File(imagePath);
            // Проверяем, существует ли папка
            if (!folder.exists() || !folder.isDirectory()) {
                System.err.println("Папка не найдена: " + imagePath);
                return "";
            }
            // Получаем список файлов в папке
            File[] files = folder.listFiles((dir, name) -> {
                // Фильтруем файлы по расширению (изображения)
                String lowerCaseName = name.toLowerCase();
                return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                        lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
            });
            // Проверяем, есть ли изображения в папке
            if (files == null || files.length == 0) {
                System.err.println("Нет изображений в: " + imagePath);
                return "";
            }
            return files[i].getAbsolutePath();
        } catch (SecurityException e) {
            showAlert( "Нет доступа к папке: " + imagePath);
            return "";
        }
    }
   private void setupImageHandlers() {
       L_Plan.setText("План выработки");
       L_Razrez.setText("Разрез");
       L_Sxema.setText("Схема вентиляции");
        AllsetupImageHandlers("Схема",nomer.getText(),hintsxema,
               sxemaVNS, sxemaVNSNE, sxemaVKL, sxemaVKLNe, sxemaobnov);
       ImageManager.updateVisibilitySimple(nomer.getText(),"План",PlanVKL,PlanVKLNe);
       ImageManager.updateVisibilitySimple(nomer.getText(),"Разрез",PoperVKL,PoperVKLNe);
       ImageManager.setOpenFirstImageHandler(PlanVKL,"План",nomer.getText());
       ImageManager.setOpenFirstImageHandler(PoperVKL,"Разрез",nomer.getText());

    }
    private void setupImageHandlers_DOP() {
        L_Plan.setText("План выработки (доп.)");
        L_Razrez.setText("Разрез (доп.)");
        L_Sxema.setText("Схема вентиляции (доп.)");
        AllsetupImageHandlers("Схема_"+interval.getText(),nomer.getText(),hintsxema,
                sxemaVNS, sxemaVNSNE, sxemaVKL, sxemaVKLNe, sxemaobnov);
        ImageManager.updateVisibilitySimple(nomer.getText(),"План_"+interval.getText(),PlanVKL,PlanVKLNe);
        ImageManager.updateVisibilitySimple(nomer.getText(),"Разрез_"+interval.getText(),PoperVKL,PoperVKLNe);
        ImageManager.setOpenFirstImageHandler(PlanVKL,"План_"+interval.getText(),nomer.getText());
        ImageManager.setOpenFirstImageHandler(PoperVKL,"Разрез_"+interval.getText(),nomer.getText());

//        AllsetupImageHandlers("План_"+interval.getText()+"_"+dop.getText(),nomer.getText(),hintPlan,
//                planVNS1, planVNSNE1, PlanVKL1, PlanVKLNe1,planobnov1);
    }

    private void handleError(Exception e) {
        e.printStackTrace();
        showAlert( "Произошла ошибка: " + e.getMessage());
    }

    private void setupTable() {
        // 1. Делаем таблицу редактируемой
        dataTable.setEditable(false);
        // 2. Настраиваем, как данные извлекаются для каждого столбца
        setupCellValueFactories();
        // 3. Настраиваем редактирование ячеек
        setupCellEditing();

        dataTable.setOnMouseClicked(mouseEvent -> {

            int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();

            if (selectedIndex >= 0) {
                ObservableList<StringProperty> row = dataTable.getItems().get(selectedIndex);
                 this.OT=(row.get(1).get()).trim().replace(',', '.');;
                this.DO=(row.get(2).get()).trim().replace(',', '.');;
                katigoria1.setText(row.get(3).get());
                opisanie1.setText(row.get(4).get());
                interval.setText(row.get(5).get());
                nomer11.setText(row.get(6).get());
                dop.setText(row.get(0).get());
                singUpButtun2.setVisible(true);
                setupImageHandlers_DOP();
                SetChoice_Box(idi.getText(),katigoria1.getText(),nomer11.getText());
            }
        });
    }
    private void setupCellValueFactories() {
        // Столбец 1: берет значение из первой ячейки строки
        column1.setCellValueFactory(param -> {
            // param.getValue() - получаем всю строку (ObservableList<StringProperty>)
            ObservableList<StringProperty> row = param.getValue();
            // Если в строке есть хотя бы 1 элемент, возвращаем его значение
            // Если нет - возвращаем пустую строку
            if (row.size() > 0) {
                return row.get(0);  // StringProperty первой ячейки
            } else {
                return new SimpleStringProperty("");  // Пустая строка
            }
        });

        // Аналогично для остальных столбцов
        column2.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 1 ? row.get(1) : new SimpleStringProperty("");
        });
        column3.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 2 ? row.get(2) : new SimpleStringProperty("");
        });
        column4.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 3 ? row.get(3) : new SimpleStringProperty("");
        });
        column5.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 4 ? row.get(4) : new SimpleStringProperty("");
        });
        column6.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 5 ? row.get(5) : new SimpleStringProperty("");
        });
        column7.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 6 ? row.get(6) : new SimpleStringProperty("");
        });
        column8.setCellValueFactory(param -> {
            ObservableList<StringProperty> row = param.getValue();
            return row.size() > 7 ? row.get(7) : new SimpleStringProperty("");
        });
    }

    private void setupCellEditing() {
        // Для каждого столбца настраиваем возможность редактирования
        setupColumnForEditing(column1, 0);  // 0 - индекс столбца
        setupColumnForEditing(column2, 1);  // 1 - индекс столбца
        setupColumnForEditing(column3, 2);  // 2 - индекс столбца
        setupColumnForEditing(column4, 3);  // 3 - индекс столбца
        setupColumnForEditing(column5, 4);  // 3 - индекс столбца
        setupColumnForEditing(column6, 5);  // 3 - индекс столбца
        setupColumnForEditing(column7, 6);  // 3 - индекс столбца
        setupColumnForEditing(column8, 7);  // 3 - индекс столбца
    }

    private void choiceTemplate(String id, String tp, String chText, String category) {
        // Значение по умолчанию на случай, если ничего не подойдет
        int defaultTemplate = 0;
        templateNumber = defaultTemplate;
        if (id == null) {
            return;
        }
        if (template1Ids.contains(id)) {
            templateNumber = 1;
        } else if (template4Ids.contains(id)) {
            templateNumber = 4;
        } else if (template5Ids.contains(id)) {
            if (tp == null) {
                return;
            }
            switch (tp) {
                case "82", "85" -> templateNumber = 6;
                case "83", "84", "86", "87" -> templateNumber = 5;
                default -> throw new IllegalArgumentException("Не могу определить шаблон для ID=" + id);

            }
        } else if (choiceTemplate9_14.contains(id)) {
            if (chText == null) {
                return;
            }
            switch (chText) {
                case "ГКР" -> templateNumber = 1;
                case "ГПР" -> {
                    if ("1".equals(category)) {
                        templateNumber = 2;
                    } else {
                        templateNumber = 3;
                    }
                }
                default -> throw new IllegalArgumentException("Не могу определить шаблон для ID=" + id);
            }
        } else if (choiceTemplate18_25.contains(id)) {
            if (category == null) {
                return;
            }
            switch (category) {
                case "2" -> templateNumber = 2;
                case "3", "5" -> templateNumber = 3;
                case "4" -> {
                    if ("меньше 3 месяцев".equals(chText)) {
                        templateNumber = 2;
                    } else {
                        templateNumber = 3;
                    }
                }
                default -> throw new IllegalArgumentException("Не могу определить шаблон для ID=" + id);
            }
        } else if (choiceTemplate16_17.contains(id)) {
            if (category == null) {
                return;
            }
            switch (category) {
                case "3", "5" -> templateNumber = 3;
                case  "2","4" -> {
                    if ("меньше 3 месяцев".equals(chText)) {
                        templateNumber = 2;
                    } else {
                        templateNumber = 3;
                    }
                }
                default -> throw new IllegalArgumentException("Не могу определить шаблон для ID=" + id);
            }
        } else if ("15".equals(id)) {
            if (category == null) {
                return;
            }
            switch (category) {
                case "1" -> templateNumber = 2;
                case "2", "3", "5" -> templateNumber = 3;
                case "4" -> {
                    if ("меньше 3 месяцев".equals(chText)) {
                        templateNumber = 2;
                    } else {
                        templateNumber = 3;
                    }
                }
                default -> throw new IllegalArgumentException("Не могу определить шаблон для ID=" + id);
            }
        } else if (coupling7.contains(id)) {
             templateNumber = 7;
        } else if (coupling8.contains(id)) {
            templateNumber = 8;
        }
        if (templateNumber == defaultTemplate) {
            throw new IllegalArgumentException("Не могу определить шаблон для ID=" + id);
        }
    }

    private void getNomberAnkerandNabrizg ( String kat) {
        String id=idi.getText();
        if (id.isEmpty()) {
            return;
        }
        if (!kat.isEmpty()){
            switch (id) {
                case "1","2", "3","4"->TP_3(kat,"2.2","20","50");
                case "5","6", "7","8","9","10","11","12","13","14" ->TP_3(kat,"1.8","20","50");
                case "26","27" ->TP_3(kat,"1.4","30","30");

                case "15","18","20" ->TP_5(kat,"1.8","20","50");
                case "16","17","22","23","24","25" ->TP_5(kat,"2.2","20","50");
                case "19","21" ->{this.longAnker = "1.8";this.thicknessNabr ="0";}
                case "28","29","30","31","32","33","34","35" ->TP_3(kat,"0.0","20","50");

                default -> {this.longAnker = "0";this.thicknessNabr="0";}
            }}
    }

    private void TP_3 (String kat,String x1,String x2,String x3) {

        switch (kat) {
            case "1","2" -> {this.longAnker = x1;this.thicknessNabr =x2; }
            case "3" -> {this.longAnker = x1;this.thicknessNabr =x3;}
            default ->  {this.longAnker = "0";this.thicknessNabr="0";}
        }
    }
    private void TP_5 (String kat,String x1,String x2,String x3) {

        switch (kat) {
            case "1" -> {
                if (Kat_5.contains(idi.getText())) {
                    this.longAnker = x1;this.thicknessNabr ="0";
                }else {
                    this.longAnker = "0";this.thicknessNabr="0";}}
            case "2","4" -> {this.longAnker = x1;this.thicknessNabr =x2;}
            case "3","5"-> {this.longAnker = x1;this.thicknessNabr =x3;}

            default ->  {this.longAnker = "0";this.thicknessNabr="0";}
        }
    }
}
