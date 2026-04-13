package com.example.proburok.job;
import com.example.proburok.New_Class.Baza;
import com.example.proburok.New_Class.Baza_Geolg;
import com.example.proburok.MQ.DatabaseHandler;
import com.example.proburok.New_Class.InputData;
import com.example.proburok.New_Class.ValidationException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import java.io.File;
import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Duration;

public class GeologCOD extends Configs {
    @FXML private TextField DO;
    @FXML private TextField OT;
    @FXML private TextField OSTATOK;
    @FXML private TableView<ObservableList<StringProperty>> dataTable;
    @FXML private TableColumn<ObservableList<StringProperty>, String>  column1;
    @FXML private TableColumn<ObservableList<StringProperty>, String>  column2;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column3;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column4;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column5;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column6;
    @FXML private TableColumn<ObservableList<StringProperty>, String> column7;
    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private Button B3;
    @FXML private Button B4;
    @FXML private Button B5;
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
    @FXML private TextArea opisanie;
    @FXML private TextField katigoria;
    @FXML private TextField nomer;
    @FXML private Button singUpButtun;
    @FXML private ComboBox<String> gorbox;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField privazka;
    @FXML private Label Lb_Klass;
    @FXML private Label L_OT;
    @FXML private Label L_DO;
    @FXML private TextField idi;
    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView tabl;
    @FXML private TextField dlina;
    @FXML private CheckBox cb;
    private String tippas;
    private String tippas_ROW;
    private String prim1 = "Все данные внесены" ;
    private String prim2 =  "Проходка не предусмотрена назначением выработки " ;
    private String prim3 = "Внесён дополнительный интервал" ;
    private String prim;

    @FXML
    void initialize() {
        setupImageHandlers();

        OT.setVisible( false);DO.setVisible(false);
        Filter_Text(OT);Filter_Text(DO);
        L_DO.setVisible(false);L_OT.setVisible(false);
        dataTable.setVisible(false);
        cb.setVisible(false);
        OSTATOK.setVisible(false);
        idi.setVisible(false);
        setupTable();

        openImagedok(tabl,instr,dokumGeolog,dokumGeolog11,"yes");

        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<Baza> bazas = dbHandler.getAllBaza();
        List<String> nom = bazas.stream() //заполнение базы
                .map(Baza::getGORIZONT)
                .distinct()
                .collect(Collectors.toList());

        gorbox.setItems(FXCollections.observableArrayList(nom));
        gorbox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                ObservableList<Baza> namespisok = dbHandler.poiskName(newValue);
                List<String> imi = namespisok.stream() //заполнение базы
                        .map(Baza::getNAME)
                        .distinct()
                        .toList();
                ohistka();
                namebox.setItems(FXCollections.observableArrayList(imi));
                cb.selectedProperty().set(false);
                cb.setVisible(false);
            }
        });
        namebox.valueProperty().addListener((observable, oldValue, newValue) -> {
            cb.selectedProperty().set(false);
            cb.setVisible(false);
            if (newValue != null  && !newValue.equals(oldValue)) {
                poisk(gorbox.getValue(),newValue);
                viborKatigorii();

            }
        });
        idi.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                viborKatigorii();
            }
        });

        singUpButtun.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();
            if (cb.selectedProperty().get()) {
                System.out.println("FGF");
                setupImageHandlers_DOP();
            }else {setupImageHandlers();}

            try {
                 validateInputs();
                // Проверка полей по очереди
              getPas(katigoria.getText());
                if (tippas.equals("-")){
                    throw new ValidationException("Проходка не предусмотрена назначением выработки");
                    //   errors.append("Проходка не предусмотрена назначением выработки").append("\n");
                }

                addRowToTable();
                getPas_Table();

                    if (!cb.selectedProperty().get()) {
                        new DatabaseHandler().DobavlenieGEOLOG_SOPR(katigoria.getText(),opisanie.getText(), selectedGor, selectedName,tippas,prim) ;
                        }else {
                        if (!prim.equals(prim2)){
                        this.prim=prim3;}
                        new DatabaseHandler().DobavleniePRIM(prim, selectedGor, selectedName); }

                dbHandler.deleteRowsWithCheck(nomer.getText());
                saveTableToDatabase();
                System.out.println(idi.getText()+   " ТИПОВОЙ ПАСПОРТ= "+this.tippas+" "+this.prim);
                ohistka();

            }catch (IllegalArgumentException e) {
                // Просто выходим, alert уже показан в addRowToTable
                return;
            }   catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });

        cb.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue) {
                if (soprigenii.contains(idi.getText())) {
                    OT.setVisible(false);
                    DO.setVisible(false);
                    L_OT.setVisible(false);
                    L_DO.setVisible(false);
                }else {
                OT.setVisible(true);
                DO.setVisible(true);
                L_OT.setVisible(true);
                L_DO.setVisible(true);

            }

                List<Baza_Geolg> rows = getTableDataAsRows();
                for (Baza_Geolg row : rows) {
                    double OT_dobl = Double.parseDouble(row.getcolumnOT());
                    double DO_dobl = Double.parseDouble(row.getcolumnDO());
                    double DLINA_dobl = Double.parseDouble(OSTATOK.getText());
                    double sum  = DLINA_dobl + (OT_dobl-DO_dobl);
                    //OSTATOK.setText(String.valueOf(sum));
                    OT.setText(row.getcolumnOT());
                    DO.setText(row.getcolumnDO());
                    katigoria.setText(row.getcolumnKLASS());
                    opisanie.setText(row.getcolumnOPIS());

                    if ((katigoria.getText() ==null) || katigoria.getText().isEmpty() ){
                        obnul("x","");
                    }else {
                        switch (katigoria.getText()){
                            case"1" -> {obnul("x",opisanie.getText());B1.setStyle(red);}
                            case"2" -> {obnul("x",opisanie.getText());B2.setStyle(red);}
                            case"3" -> {obnul("x",opisanie.getText());B3.setStyle(red);}
                        }}
                }// код выполнится при изменении состояния на "отмечено"
            } else { poisk(gorbox.getValue(),namebox.getValue());
                OT.setVisible(false);
                DO.setVisible(false);
                L_OT.setVisible(false);
                L_DO.setVisible(false);
            }
        });
    }


    private void viborKatigorii(){
            klikKatigorii(B1,"1",tex1);
            klikKatigorii(B2,"2",tex2);
            klikKatigorii(B3,"3",tex3);
            klikKatigorii(B4,"4",tex4);
            klikKatigorii(B5,"5",tex5);
        }
    private void klikKatigorii(Button imageView, String kat,  String tx ) {
        imageView.setOnMouseClicked(e -> {
            obnul(kat,tx);
            imageView.setStyle(red);
        });
    }

    private void setupImageHandlers() {
        AllsetupImageHandlers("План",nomer.getText(),hintPlan,planVNS, planVNSNE, PlanVKL, PlanVKLNe, planobnov);
        AllsetupImageHandlers("Разрез",nomer.getText(),hintrazrez,
                poperVNS, poperVNSNE, PoperVKL, PoperVKLNe, poperobnov);
    }
    private void setupImageHandlers_DOP() {
        AllsetupImageHandlers("План_"+OT.getText()+"-"+DO.getText(),nomer.getText(),hintPlan,planVNS, planVNSNE, PlanVKL, PlanVKLNe, planobnov);
        AllsetupImageHandlers("Разрез_"+OT.getText()+"-"+DO.getText(),nomer.getText(),hintrazrez,
                poperVNS, poperVNSNE, PoperVKL, PoperVKLNe, poperobnov);
    }

    public void obnul(String x,String tx){
        if (x.equals("x")) {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            opisanie.setText(tx);
        }else {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            katigoria.setText(x);
            opisanie.setText(tx);
        }
    }

    private void poisk (String viborGOR, String viborName) {
        Platform.runLater(() -> {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Baza poluh = dbHandler.danii(viborGOR, viborName);
        if (poluh != null) {
            nomer.setText(poluh.getNOMER());
            idi.setText(poluh.getIDI());
            privazka.setText(poluh.getPRIVIZKA());
            dlina.setText(poluh.getDLINA());
            loadDataFromDatabase(nomer.getText(),dataTable);
            if (soprigenii.contains(poluh.getIDI())) {
                dlina.setText("0");
                dlina.setVisible(false);
            } else {
                dlina.setVisible(true);
                 }
            OSTATOK.setText(poluh.getDLINA());
            if (poluh.getKATEGORII() != null) {
                //katigoria.setText(poluh.getKATEGORII());
                cb.setVisible(true);
                cb.selectedProperty().set(true);
            } else {
                katigoria.setText("");
                cb.selectedProperty().set(false);
                cb.setVisible(false);
            }
            if (poluh.getOPISANIE() != null) {
                opisanie.setText(poluh.getOPISANIE());
            } else {
                opisanie.setText("");
            }
            setupImageHandlers();

            if ((katigoria.getText() ==null) || katigoria.getText().isEmpty() ){
                obnul("x","");
            }else {
                switch (katigoria.getText()){
                    case"1" -> {obnul("x",opisanie.getText());B1.setStyle(red);}
                    case"2" -> {obnul("x",opisanie.getText());B2.setStyle(red);}
                    case"3" -> {obnul("x",opisanie.getText());B3.setStyle(red);}
                    case"4" -> {obnul("x",opisanie.getText());B4.setStyle(red);}
                    case"5" -> {obnul("x",opisanie.getText());B5.setStyle(red);}

                }}


            if (cb.selectedProperty().get()){
                cb.selectedProperty().set(true);
            }

            System.out.println(idi.getText());
        } else {
            System.out.println("Данные не найдены для " + viborGOR + " - " + viborName);
            nomer.clear();
            privazka.clear();
        }
        });
    }
    private void ohistka(){
        clearTable(dataTable);

       // namebox.setValue(null);
        nomer.setText("");
        privazka.setText("");
        katigoria.setText("");
        opisanie.setText("");
        dlina.setText("");
        dlina.setVisible(true);
        idi.setText("");
        this.tippas="";
        this.prim="";
        OT.setText("");  DO.setText("");OSTATOK.setText("");
        setupImageHandlers();
        obnul("x","");
//        cb.selectedProperty().set(false);
//        cb.setVisible(false);
    }

    private void getPas ( String kat) {
        String id=idi.getText();
        if (id.isEmpty()) {
            tippas = "Типовой паспорт не разработан";
            return;
        }
        if (!kat.isEmpty()){
            switch (id) {
                case "1" ->TP_3(kat,"1","2","3");
                case "2" ->TP_3(kat,"4","5","6");
                case "3" ->TP_3(kat,"7","8","9");
                case "4" ->TP_3(kat,"10","11","12");
                case "5"->TP_3(kat,"13","14","15");
                case "6" ->TP_3(kat,"16","17","18");
                case "7" ->TP_3(kat,"19","20","21");
                case "8" ->TP_3(kat,"22","23","24");
                case "9" ->TP_3(kat,"25","26","27");
                case "10" ->TP_3(kat,"28","29","30");
                case "11" ->TP_3(kat,"31","32","33");
                case "12" ->TP_3(kat,"34","35","36");
                case "13" ->TP_3(kat,"37","38","39");
                case "14" ->TP_3(kat,"40","41","42");
                case "15" ->TP_5(kat,"43","44","45","46","47");
                case "16" ->TP_5(kat,"-","48","49","50","51");
                case "17" ->TP_5(kat,"-","52","53","54","55");
                case "18" ->TP_5(kat,"-","56","57","58","59");
                case "20" ->TP_5(kat,"-","61","62","63","64");
                case "22" ->TP_5(kat,"-","66","67","68","69");
                case "23" ->TP_5(kat,"-","70","71","72","73");
                case "24" ->TP_5(kat,"-","74","75","76","77");
                case "25" ->TP_5(kat,"-","78","79","80","81");
                case "26" ->TP_3(kat,"82","83","84");
                case "27" ->TP_3(kat,"85","86","87");

                case "28" ->TP_3(kat,"88","89","90");
                case "29" ->TP_3(kat,"91","92","93");
                case "30" ->TP_3(kat,"94","95","96");
                case "31" ->TP_3(kat,"97","98","99");
                case "32" ->TP_3(kat,"100","101","102");
                case "33" ->TP_3(kat,"103","104","105");
                case "34" ->TP_3(kat,"106","107","108");
                case "35" ->TP_3(kat,"109","110","111");

                default ->  tippas = "Типовой паспорт не разработан";
            }}
    }

    private void TP_3 (String kat,String x1,String x2,String x3) {

        switch (kat) {
            case "1" -> {this.tippas = x1;this.tippas_ROW =x1; this.prim=prim1;}
            case "2" -> {this.tippas = x2;this.tippas_ROW =x2; this.prim=prim1;}
            case "3" -> {this.tippas = x3;this.tippas_ROW =x3; this.prim=prim1;}
            default ->  {tippas = "-";this.tippas_ROW ="-"; this.prim=prim2;}
        }
        }
    private void TP_5 (String kat,String x1,String x2,String x3,String x4,String x5) {

        switch (kat) {
            case "1" -> {
                if (Kat_5.contains(idi.getText())) {
                    this.tippas = x1;this.tippas_ROW =x1; this.prim=prim1;
                }else {
                tippas = "-";this.tippas_ROW ="-"; this.prim=prim2;}}
            case "2" -> {this.tippas = x2;this.tippas_ROW =x2; this.prim=prim1;}
            case "3" -> {this.tippas = x3;this.tippas_ROW =x3; this.prim=prim1;}
            case "4" -> {this.tippas = x4;this.tippas_ROW =x4; this.prim=prim1;}
            case "5" -> {this.tippas = x5;this.tippas_ROW =x5; this.prim=prim1;}
            default ->  {tippas = "-";this.tippas_ROW ="-"; this.prim=prim2;}
        }
    }
    private void setupTable() {
        // 1. Делаем таблицу редактируемой
        dataTable.setEditable(true);
        // 2. Настраиваем, как данные извлекаются для каждого столбца
        setupCellValueFactories();
        // 3. Настраиваем редактирование ячеек
        setupCellEditing();

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
    }

    private void setupCellEditing() {
        // Для каждого столбца настраиваем возможность редактирования
        setupColumnForEditing(column1, 0);  // 0 - индекс столбца
        setupColumnForEditing(column2, 1);  // 1 - индекс столбца
        setupColumnForEditing(column3, 2);  // 2 - индекс столбца
        setupColumnForEditing(column4, 3);  // 3 - индекс столбца
        setupColumnForEditing(column5, 4);  // 3 - индекс столбца
    }



    private InputData validateInputs() throws ValidationException {

        InputData data = new InputData();
        data.selectedGor = gorbox.getValue();
        data.selectedName = namebox.getValue();
        data.kategoriyaValue = katigoria.getText();
        data.opisanieValue = opisanie.getText();
        data.selectPrivizka = privazka.getText();
        data.dlinaValue = dlina.getText();
        if (dataTable.getItems().isEmpty()|| OT.getText().isEmpty()|| DO.getText().isEmpty() )
        {OT.setText("0.0");DO.setText( dlina.getText());}

        data.OTValue = OT.getText();
        data.DOValue = DO.getText();
        data.planVisbl1=planVNS;
        data.planVisbl2=planVNSNE;
        data.razrez1=poperVNS;
        data.razrez2=poperVNSNE;
        data.selectedTIPPAS=tippas;
        validateRequiredFields(data);
        return data;
    }

    private void validateRequiredFields(InputData data) throws ValidationException {
        validateRequiredFields_Chek(data,"geolog");
        StringBuilder errors = new StringBuilder();

        if (soprigenii.contains(idi.getText())) {
            data.dlinaValue = "0";
            dlina.setVisible(false);
        } else {
            dlina.setVisible(true);
        }
        if (cb.selectedProperty().get()) {
        validateField(data.OTValue, "начало интервала", errors);
        validateField(data.DOValue, "конец интервала", errors);}


        if (errors.length() > 0) {
            throw new ValidationException(" \n" + errors.toString());
        }
    }
    private void addRowToTable() {

        if (!cb.selectedProperty().get()) {
            List<Baza_Geolg> rows = getTableDataAsRows();
            if (rows.isEmpty()) {
               System.out.print("Pusto");

            }else { removeSelectedRow();}

        }
        try {
            InputData data = validateInputs();
            String OT_TOH = data.OTValue.trim().replace(',', '.');
            String DO_TOH = data.DOValue.trim().replace(',', '.');
            String DLINA_TOH;
            if (OSTATOK.getText() == null || OSTATOK.getText().isEmpty()) {
                DLINA_TOH = data.dlinaValue.trim().replace(',', '.');
            } else {
                DLINA_TOH = OSTATOK.getText().trim().replace(',', '.');
            }
            double OT_dobl = Double.parseDouble(OT_TOH);
            double DO_dobl = Double.parseDouble(DO_TOH);
            double DLINA_dobl = Double.parseDouble(DLINA_TOH)+0.5;
            double sum = DLINA_dobl + (OT_dobl - DO_dobl);
            if ( DLINA_dobl > DO_dobl && DLINA_dobl > OT_dobl ) {
                // Все данные валидны - выполняем сохранение
                ObservableList<ObservableList<StringProperty>> items = dataTable.getItems();

                ObservableList<StringProperty> newRow = FXCollections.observableArrayList(
                        new SimpleStringProperty(String.valueOf(items.size() + 1)),
                        new SimpleStringProperty(data.OTValue),
                        new SimpleStringProperty(data.DOValue),
                        new SimpleStringProperty(data.kategoriyaValue),
                        new SimpleStringProperty(data.opisanieValue),
                        new SimpleStringProperty(data.OTValue + "-" + data.DOValue),
                        new SimpleStringProperty(""),
                        new SimpleStringProperty("")
                );
                items.add(newRow);
               // dataTable.scrollTo(items.size() - 1);
                OT.setText(DO_TOH);
                DO.setText("");
                OSTATOK.setText(String.valueOf(sum));
            } else {
                showAlert("Длина интервала больше длины выработки ");
                throw new IllegalArgumentException("Невалидные данные");
            }

        } catch (ValidationException e) {
            showAlert(e.getMessage());
            return;

        } catch (NumberFormatException e) {
            showAlert("Ошибка в числовых полях: " + e.getMessage());
        }
    }

    public List<Baza_Geolg> getTableDataAsRows() {
        List<Baza_Geolg> rows = new ArrayList<>();
        for (ObservableList<StringProperty> tableRow : dataTable.getItems()) {
            Baza_Geolg row = new Baza_Geolg();

            // Безопасное получение значений (чтобы избежать IndexOutOfBoundsException)
            if (tableRow.size() > 0 && tableRow.get(1) != null) {
                row.setcolumnOT(tableRow.get(1).get());
            } else {
                row.setcolumnOT("");
            }
            if (tableRow.size() > 1 && tableRow.get(2) != null) {
                row.setcolumnDO (tableRow.get(2).get());
            } else {
                row.setcolumnDO ("");
            }
            if (tableRow.size() > 2 && tableRow.get(3) != null) {
                row.setcolumnKLASS(tableRow.get(3).get());
            } else {
                row.setcolumnKLASS("");
            }
            if (tableRow.size() > 3 && tableRow.get(4) != null) {
                row.setcolumnOPIS(tableRow.get(4).get());
            } else {
                row.setcolumnOPIS("");
            }
            if (tableRow.size() > 4 && tableRow.get(5) != null) {
                row.setcolumnOTDO(tableRow.get(5).get());
            } else {
                row.setcolumnOTDO("");
            }

            if (tableRow.size() > 5 && tableRow.get(6) != null) {
                row.setcolumnLIST(tableRow.get(6).get());
            } else {
                row.setcolumnLIST("");
            }
            if (tableRow.size() > 6 && tableRow.get(7) != null) {
                row.setColumnFAKTOR(tableRow.get(7).get());
            } else {
                row.setColumnFAKTOR("");
            }
            rows.add(row);
        }
        return rows;
    }

    private void saveTableToDatabase() {
        // 1. Получаем данные из таблицы
        List<Baza_Geolg> rows = getTableDataAsRows();
        // 2. Получаем номер паспорта из текстового поля idi
        String nomPas = nomer.getText().trim();  // .trim() убирает пробелы по краям
        // 3. Проверяем, что номер паспорта не пустой
        if (nomPas.isEmpty()) {
            showAlert( "Поле 'Номер паспорта' не заполнено!");
            return;
        }
        // 4. Проверяем, что есть данные в таблице
        if (rows.isEmpty()) {
            showAlert( "Таблица пуста! Добавьте данные в таблицу.");
            return;
        }
        // 5. Сохраняем в БД, передавая номер паспорта отдельным параметром
        try {
            DatabaseHandler tableDAO = new DatabaseHandler();
            tableDAO.saveAllRows(rows, nomPas);
            //showAlert("Сохранено " + rows.size() + " строк для паспорта №" + nomPas);
        } catch (Exception e) {
            showAlert( "Не удалось сохранить данные: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void removeSelectedRow() {
        int i = dataTable.getItems().size()-1;
            dataTable.getItems().remove(i);
            updateRowNumbers(dataTable);
    }


    private void getPas_Table() {
        ObservableList<ObservableList<StringProperty>> allRows = dataTable.getItems();
        for (int i = 0; i < allRows.size(); i++) {
            ObservableList<StringProperty> row = allRows.get(i);
            if (row.size() > 0) {
              getPas(row.get(3).get());
                row.get(6).set(this.tippas_ROW);
                row.get(7).set(String.valueOf(i + 1));
            }
        }
    }


   }




