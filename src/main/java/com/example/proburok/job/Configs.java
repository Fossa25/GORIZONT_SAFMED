package com.example.proburok.job;


import com.example.proburok.MQ.DatabaseHandler;
import com.example.proburok.New_Class.Baza_Geolg;
import com.example.proburok.New_Class.ConfigLoader;
import com.example.proburok.New_Class.InputData;
import com.example.proburok.New_Class.ValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class Configs {
    protected String dbHost = ConfigLoader.getProperty("dbHost");
    protected String dbPort = "3306";
    protected String dbUser = "TOPA";
    protected String dbPass = "300122";
    protected String dbName = "gorizont";
    protected String OUTPUT_PATH = ConfigLoader.getProperty("OUTPUT_PATH");
    protected static String Put =  ConfigLoader.getProperty("Put");
    protected String Put_albom ="/com/example/proburok/docs/AlbomV.pdf";
    protected String Put_geolog ="/com/example/proburok/docs/Geologi.pdf";
    protected String Put_instr ="/com/example/proburok/docs/instruk.pdf";
    protected String Put_othet ="/com/example/proburok/docs/Othet.pdf";
    protected String Put_texusovia ="/com/example/proburok/docs/texuslovia.pdf";
    protected String TEMPLATE_PATH1 = "/com/example/proburok/docs/template_1.docx";
    protected String TEMPLATE_PATH1S = "/com/example/proburok/docs/template_1S.docx";
    protected String TEMPLATE_PATH1_1 = "/com/example/proburok/docs/template_1-1.docx";
    protected String TEMPLATE_PATH1S_1 = "/com/example/proburok/docs/template_1S-1.docx";
    protected String HABLON_PATH = "/com/example/proburok/hablon";
    protected String HABLON_PATH_VID =HABLON_PATH+"/obvid";
    protected String HABLON_PATH_SOPR =HABLON_PATH+"/soprigenii";
    protected String HABLON_PATH_ILIMENT =HABLON_PATH+"/ilement";
    protected String HABLON_PATH_USTANOVKA =HABLON_PATH+"/ustanovka";
    List<String> Kat_5 = Arrays.asList("15","32");
    List<String> soprigenii = Arrays.asList("28","29","30","31","32","33","34","35");
    List<String> Choice_List = Arrays.asList("9","10","11","12","13","14" ,"15","16","17","18","20","22","23","24","25");

    List<String> template1Ids = Arrays.asList("1","2","3","4","5","6","7","8");
    List<String> template4Ids = Arrays.asList("19","21");
    List<String> template5Ids = Arrays.asList("26","27");

    List<String> choiceTemplate9_14 = Arrays.asList("9","10","11","12","13","14");
    List<String> choiceTemplate18_25 = Arrays.asList("18","20","22","23","24","25");
    List<String> choiceTemplate16_17 = Arrays.asList("16","17");
    List<String>  coupling7 = Arrays.asList("28", "29","30", "31","32");
    List<String>  coupling8 = Arrays.asList("33","34","35");

    public String blak= " -fx-border-color: #00000000;-fx-background-color:#00000000;-fx-border-width: 0px";
    public String red = "-fx-border-color: #14b814;-fx-background-color:#00000000;-fx-border-width: 3px";
    public String tex1 = "Мелко- и среднеблочная структура с шероховатыми поверхностями трещин." +
            " Форма блоков неправильная, края раковистые и острые. Массив в зажатом состоянии." +
            " При ударе геологическим молотком издается резкий звенящий звук с образованием царапин. ";
    public String tex2 = "Гидротермально измененные горные породы. Крупные блоки продолговатой формы с гладкими и " +
            "округлыми поверхностями трещин, заполненными мягкими чешуйчатыми минералами. Зеркала и борозды скольжения." +
            " При ударе геологическим молотком остаются вмятины.";
    public String tex3 = "Гидротермально переработанные горные породы. Нечеткая слоистость толщи с гидрослюдами и брекчиями. " +
            "Блоки в виде рваных пластин. Возможно наличие сульфидной минерализации. " +
            "От удара геологического молотка остаются глубокие вмятины.";
    public String tex4 = "Прочный рудный массив в виде чередования слоев небольшой мощности (0,2÷1,0 м). " +
            "Слои могут представлять прочно сцементированную рудную брекчию. Массив в зажатом состоянии." +
            "    При ударе геологическим молотком издается резкий звенящий звук с образованием царапин.";
    public String tex5 = "Рыхлые спрессованные руды. Форма блоков в виде кусков и валунов неправильной формы. " +
            "При ударе геологическим молотком издается глухой хрустящий звук с образованием небольшой вмятины. " +
            "Образец отделяется от массива без усилий.";

    public String [] hintPlan = new String[]{"Внести изображение плана","Показать план",
            "План не внесён","Обновить изображение плана"};

    public String [] hintrazrez = new String[]{"Внести изображение  разреза","Показать  разрез",
            " Разрез не внесён","Обновить изображение  разреза"};

    public String [] hintsxema = new String[]{ "Внести изображение схемы вентиляции", "Показать схему вентиляции",
            "Схема вентиляции не внесена","Обновить изображение схемы вентиляции"};

    public void openImagedok(ImageView tablI,ImageView instI,ImageView geologI,ImageView geomexandpas,String pas){
    tablI.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml","no"));
    instI.setOnMouseClicked(mouseEvent -> OpenDok(Put_instr,"Инструкция_"));
    geologI.setOnMouseClicked(mouseEvent -> OpenDok(Put_geolog,"Геология Гайского месторождения_"));
   if (pas.equals("yes")){
        geomexandpas.setOnMouseClicked(mouseEvent -> {OpenDok(Put_albom,"АЛЬБОМ ТИПОВЫХ ПАСПОРТОВ КРЕПЛЕНИЯ_");});
    }else{
    geomexandpas.setOnMouseClicked(mouseEvent -> OpenDok(Put_texusovia,"Технологические условия_"));}
    }
    public void validateField(String value, String fieldName, StringBuilder errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.append("- Не заполнено поле ").append(fieldName).append("\n");
        }
    }

    public void validateField_Image(ImageView value1,ImageView value2, String fieldName, StringBuilder errors) {
        if (value1.isVisible() || value2.isVisible()) {
            errors.append("- Не внесён графический материал ").append(fieldName).append("\n");
        }
    }
    protected void validateRequiredFields_Chek(InputData data,String WHO) throws ValidationException {
        StringBuilder errors = new StringBuilder();

        validateField(data.selectedGor, "горизонт", errors);
        validateField(data.selectedName, "название выработки", errors);
        validateField(data.selectPrivizka, "привязка", errors);
        validateField(data.dlinaValue, "длина выработки", errors);
        if (WHO.equals("miner")){
            validateField(data.sectionValue, "сечение", errors);
            validateField(data.NoberValue, "номер", errors);
            validateField_Image(data.planVisbl1,data.planVisbl2, "план", errors);
        }
        if (WHO.equals("geolog")  ){
            validateField_Image(data.planVisbl1,data.planVisbl2, "план", errors);
            validateField_Image(data.razrez1,data.razrez2, "разрез", errors);
            validateField(data.kategoriyaValue, "категория", errors);
            validateField(data.opisanieValue, "описание", errors);

        }
        else if (WHO.equals("pethat") ){
            validateField(data.kategoriyaValue, "категория", errors);
            validateField(data.opisanieValue, "описание", errors);
            validateField_Image(data.planVisbl1,data.planVisbl2, "схема", errors);
            }
        if (errors.length() > 0) {
            throw new ValidationException("Заполните обязательные поля:\n" + errors.toString());
        }
    }
    protected void Filter_Text(TextField XX){
        XX.setTextFormatter(new TextFormatter<>(change -> {
            int start = Math.min(change.getRangeStart(), change.getRangeEnd());
            int end = Math.max(change.getRangeStart(), change.getRangeEnd());
            change.setRange(start, end);
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("[0-9]*([.,][0-9]*)?")) {
                return change;
            }
            return null;
        }));
    }
    protected void Filter_TextGor(TextField XX){
        XX.setTextFormatter(new TextFormatter<>(change -> {
            int start = Math.min(change.getRangeStart(), change.getRangeEnd());
            int end = Math.max(change.getRangeStart(), change.getRangeEnd());
            change.setRange(start, end);
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("-?[0-9]*([.,][0-9]*)?")) {
                return change;
            }
            return null;
        }));
    }
    public void openNewScene(String Window,String BIG){
        // Загрузка нового окна
        FXMLLoader loader = new FXMLLoader();
        // Проверка пути к FXML-файлу
        URL fxmlUrl = getClass().getResource(Window);
        loader.setLocation(fxmlUrl);
        try {
            Parent root = loader.load(); // Загрузка FXML
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            if (BIG.equals("yes")){
                stage.setMaximized(true);}
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void OpenDok(String Put,String NemDok){
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Получаем поток входных данных для ресурса
                InputStream inputStream = getClass().getResourceAsStream(Put);
                if (inputStream == null) {
                    throw new FileNotFoundException("Ресурс не найден: " + Put);
                }
                // Создаем временный файл
                Path tempFile = Files.createTempFile(NemDok, ".pdf");
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Открываем временный файл
                desktop.open(tempFile.toFile());
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
        }
    }
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setupColumnForEditing(TableColumn<ObservableList<StringProperty>, String> column,
                                       int columnIndex) {
        // 1. Устанавливаем фабрику ячеек - TextFieldTableCell
        // Это специальная ячейка, которая превращается в TextField при редактировании
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        // 2. Настраиваем обработчик завершения редактирования
        column.setOnEditCommit(event -> {
            // event.getRowValue() - строка, которую редактировали
            ObservableList<StringProperty> row = event.getRowValue();
            // event.getNewValue() - новое значение, которое ввел пользователь
            String newValue = event.getNewValue();
            // Обновляем значение в строке
            if (row.size() > columnIndex) {
                // row.get(columnIndex) - получаем StringProperty нужной ячейки
                // .set(newValue) - устанавливаем новое значение
                row.get(columnIndex).set(newValue);
            }
        });
    }
    public void loadDataFromDatabase(String nomber, TableView<ObservableList<StringProperty>> tabl ) {
        try {
            DatabaseHandler tableDAO = new DatabaseHandler();
            // Получаем данные из БД
            List<Baza_Geolg> rows = tableDAO.getAllRows(nomber);

            // Очищаем таблицу
            tabl.getItems().clear();
            ObservableList<ObservableList<StringProperty>> items = tabl.getItems();
            // Заполняем таблицу данными из БД
            for (Baza_Geolg row : rows) {
                ObservableList<StringProperty> tableRow = FXCollections.observableArrayList(
                        new SimpleStringProperty(String.format(String.valueOf(items.size()))),
                        new SimpleStringProperty(row.getcolumnOT()),
                        new SimpleStringProperty(row.getcolumnDO()),
                        new SimpleStringProperty( row.getcolumnKLASS()),
                        new SimpleStringProperty(row.getcolumnOPIS()),
                        new SimpleStringProperty(row.getcolumnOTDO()),
                        new SimpleStringProperty(row.getcolumnLIST()),
                        new SimpleStringProperty(row.getColumnFAKTOR())
                );
                tabl.getItems().add(tableRow);

            }
        } catch (Exception e) {
            showAlert( "Не удалось загрузить данные из БД: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void updateRowNumbers(TableView<ObservableList<StringProperty>> tabl) {
        ObservableList<ObservableList<StringProperty>> allRows = tabl.getItems();
        for (int i = 0; i < allRows.size(); i++) {
            ObservableList<StringProperty> row = allRows.get(i);
            if (row.size() > 0) {
                row.get(0).set(String.valueOf(i + 1));
            }
        }
    }
    public void clearTable(TableView<ObservableList<StringProperty>> tabl) {
        tabl.getItems().clear();
    }

    protected void AllsetupImageHandlers(String folderName,String Nober_Pas,String[] text_M,
                                         ImageView PlanAdd, ImageView PlanNotAdd,
                                         ImageView PlanOn, ImageView PlanOff, ImageView Planupdate
    ) {
        String planPath = Put + "/" + Nober_Pas + "/"+ folderName;

        ImageManager.updateVisibilityComplex(planPath, PlanAdd, PlanNotAdd, PlanOn, PlanOff, Planupdate,folderName);
        Runnable updatePlanVisibility = () -> {
            ImageManager.updateVisibilityComplex(planPath, PlanAdd, PlanNotAdd, PlanOn, PlanOff, Planupdate,folderName);};
        // Устанавливаем обработчик для кнопки "Обновить" (план)
        ImageManager.setUpdateHandler(Planupdate, folderName, Nober_Pas, updatePlanVisibility);
        ImageManager.setUpdateHandler(PlanAdd, folderName, Nober_Pas, updatePlanVisibility);
        // Обработчик открытия первого изображения
        ImageManager.setOpenFirstImageHandler(PlanOn, folderName, Nober_Pas);
        ImageManager.setTooltips(text_M,PlanAdd,  PlanOn, PlanOff, Planupdate);
        ImageManager.setCursorHand(PlanAdd, PlanNotAdd, PlanOn, PlanOff, Planupdate);

    }
}