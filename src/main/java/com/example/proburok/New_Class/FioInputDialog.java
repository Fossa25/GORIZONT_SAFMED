package com.example.proburok.New_Class;



import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Window;

public class FioInputDialog extends Dialog<String> {

    public FioInputDialog(Window owner) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        setTitle("Ввод данных");
        setHeaderText("Введите фамилию и инициалы ответственного лица");

        // Создаем поле ввода
        TextField fioField = new TextField();
        fioField.setPromptText("Например: Иванов И.И.");
        fioField.setPrefWidth(300);

        // Создаем layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));

        grid.add(new Label("Фамилия и инициалы:"), 0, 0);
        grid.add(fioField, 1, 0);

        getDialogPane().setContent(grid);

        // Добавляем кнопки
        ButtonType saveButtonType = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Валидация при нажатии OK
        final Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String fio = fioField.getText().trim();

            // Проверка на пустое поле
            if (fio.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Поле не может быть пустым!");
                alert.showAndWait();
                event.consume(); // Блокируем закрытие диалога
                return;
            }

            // Проверка формата (Фамилия И.О.)
            if (!fio.matches("^[а-яА-ЯёЁa-zA-Z]+\\s+[А-ЯA-Z]\\.[А-ЯA-Z]\\.$")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка формата");
                alert.setHeaderText(null);
                alert.setContentText("Неверный формат!\n\n" +
                        "Пожалуйста, введите данные в формате:\n" +
                        "Фамилия И.О.\n\n" +
                        "Например: Иванов И.И. или Петров П.П.");
                alert.showAndWait();
                event.consume(); // Блокируем закрытие диалога
                return;
            }
        });

        // Устанавливаем результат
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return fioField.getText().trim();
            }
            return null;
        });

        // Фокус на поле ввода
        javafx.application.Platform.runLater(fioField::requestFocus);
    }
}