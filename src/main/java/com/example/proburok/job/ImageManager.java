package com.example.proburok.job;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
        import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Утилитарный класс для работы с изображениями в папках.
 * Позволяет проверять наличие изображений, обновлять видимость элементов UI,
 * устанавливать обработчики открытия, обновления и создания изображений.
 */
public class ImageManager extends Configs {
    private static final Logger log = LogManager.getLogger(ImageManager.class);

    /**
     * Проверяет, есть ли в указанной папке хотя бы одно изображение.
     *
     * @param folderPath путь к папке
     * @return true, если папка существует и содержит файлы с расширениями .jpg, .jpeg, .png, .gif
     */
    public static boolean hasImages(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) return false;

        File[] files = folder.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
                    lower.endsWith(".png") || lower.endsWith(".gif");
        });
        return files != null && files.length > 0;
    }

    /**
     * Простое обновление видимости двух элементов.
     * Если есть изображения – виден первый элемент, если нет – второй.
     *
     * @param documentNumber       номер паспорта
     * @param Image_ON  элемент, видимый при наличии изображений
     * @param Image_OFF элемент, видимый при отсутствии изображений
     */
    public static void updateVisibilitySimple(String documentNumber,String folderName,
                                              ImageView Image_ON,
                                              ImageView Image_OFF) {
        String fullPath = Put + "/" + documentNumber + "/" + folderName;
        boolean exists = hasImages(fullPath);
        if (Image_ON != null) Image_ON.setVisible(exists);
        if (Image_OFF != null) Image_OFF.setVisible(!exists);

    }

    /**
     * Обновление видимости для сложной группы из 5 элементов (как в proverkaImage).
     *
     * @param folderPath        путь к папке
     * @param Image_ADD первый элемент, видимый при наличии изображений
     * @param Image_NOT_ADD первый элемент, видимый при отсутствии изображений
     * @param Image_ON второй элемент, видимый при наличии изображений
     * @param Image_OFF второй элемент, видимый при отсутствии изображений
     * @param Image_Update третий элемент, видимый при наличии изображений
     */
    public static void updateVisibilityComplex(String folderPath,
                                               ImageView Image_ADD,
                                               ImageView Image_NOT_ADD,
                                               ImageView Image_ON,
                                               ImageView Image_OFF,
                                               ImageView Image_Update,String folderName) {
        String PutPlan=Put + "//" + folderName;
        String PutPoper=Put + "//" +  folderName;
        String PutSxema=Put + "//" + folderName;

        if ( PutPlan.equals(folderPath)||PutPoper.equals(folderPath)||PutSxema.equals(folderPath)){

            Image_ADD.setVisible(false); Image_NOT_ADD.setVisible(true);
            Image_ON.setVisible(false);Image_OFF.setVisible(true);Image_Update.setVisible(false);
            return;
        }
          boolean exists = hasImages(folderPath);
        Image_ADD.setVisible(!exists);
        Image_NOT_ADD.setVisible(false);
        Image_ON.setVisible(exists);
        Image_OFF.setVisible(!exists);
        Image_Update.setVisible(exists);
    }



    /**
     * Устанавливает обработчик обновления изображений.
     * При клике очищает папку и предлагает выбрать новое изображение.
     *
     * @param button    кнопка (ImageView), по клику на которую происходит обновление
     * @param folderName путь к папке
     * @param documentNumber  название горизонта
     */
    public static void setUpdateHandler(ImageView button, String folderName, String documentNumber, Runnable onSuccess) {
        if (button == null || documentNumber == null || documentNumber.isEmpty()) return;
        button.setOnMouseClicked(e -> {
            String fullPath = Put + "/" + documentNumber + "/" + folderName;
            File folder = new File(fullPath);
            if (!folder.exists()) folder.mkdirs();

            // Удаляем старые изображения
            File[] oldFiles = folder.listFiles();
            if (oldFiles != null) {
                for (File f : oldFiles) if (f.isFile()) f.delete();
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выберите изображение");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File selected = fileChooser.showOpenDialog(button.getScene().getWindow());
            if (selected != null) {
                try {
                    File dest = new File(folder, selected.getName());
                    Files.copy(selected.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    // Вызываем переданный код обновления
                    if (onSuccess != null) onSuccess.run();
                } catch (IOException ex) {
                    log.error("Ошибка копирования изображения", ex);
                    showAlert("Не удалось сохранить изображение: " + ex.getMessage());
                }
            }
        });
    }

    public static void setOpenFirstImageHandler(ImageView imageView, String folderName, String documentNumber) {
        if (imageView == null || documentNumber == null || documentNumber.isEmpty()) return;
        imageView.setOnMouseClicked(e -> {
            String fullPath = Put + "/" + documentNumber + "/" + folderName;
            File folder = new File(fullPath);
            if (!folder.exists() || !folder.isDirectory()) return;
            File[] images = folder.listFiles((dir, name) -> {
                String lower = name.toLowerCase();
                return lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
                        lower.endsWith(".png") || lower.endsWith(".gif");
            });
            if (images != null && images.length > 0) {
                try {
                    Desktop.getDesktop().open(images[0]);
                } catch (IOException ex) {
                    log.error("Не удалось открыть изображение", ex);
                    showAlert("Не удалось открыть изображение");
                }
            }
        });
    }


    /**
     * Устанавливает курсор "рука" для набора узлов.
     *
     * @param nodes узлы, которым нужно изменить курсор
     */
    public static void setCursorHand(Node... nodes) {
        for (Node node : nodes) {
            if (node != null) node.setCursor(Cursor.HAND);
        }
    }

    /**
     * Устанавливает тултип для узла.
     *
     * @param node узел
     * @param text текст подсказки
     */
    public static void setTooltip(Node node, String text) {
        if (node == null) return;
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(300));
        tooltip.setStyle("-fx-font-size: 14; -fx-background-color: #aa9455;");
        Tooltip.install(node, tooltip);
    }

    /**
     * Устанавливает тултипы для нескольких узлов (каждый со своим текстом).
     *
     * @param texts  тексты подсказок
     * @param nodes  узлы
     */
    public static void setTooltips(String[] texts, Node... nodes) {
        int length = Math.min(texts.length, nodes.length);
        for (int i = 0; i < length; i++) {
            setTooltip(nodes[i], texts[i]);
        }
    }

    // Вспомогательные методы
    private static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex + 1) : "";
    }

//    private static void showAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Ошибка");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
}