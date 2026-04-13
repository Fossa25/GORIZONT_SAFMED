package com.example.proburok.job;

import com.example.proburok.MQ.DatabaseHandler;
import com.example.proburok.New_Class.UserSession;
import com.example.proburok.animation.Shake;
import com.example.proburok.job_User.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
public class HelloController {

    @FXML private Button authSiginButton;
    @FXML private Button loginSingUpButton;
    @FXML private Button loginSingUpButton1;
    @FXML private Button loginSingUpButton2;
    @FXML private Button loginSingUpButton3;
    @FXML private TextField login_fild;
    @FXML private PasswordField password_fild;
    @FXML private ImageView tablis;
    @FXML
    void initialize() {
        loginSingUpButton.setVisible(false);
        loginSingUpButton1.setVisible(false);
        loginSingUpButton2.setVisible(false);
        loginSingUpButton3.setVisible(false);
        authSiginButton.setOnAction(actionEvent -> {
            String loginText = login_fild.getText().trim();
            String loginPassword = password_fild.getText().trim();
            if (!loginText.isEmpty() && !loginPassword.isEmpty()) {
                loginUser(loginText, loginPassword);}
        });
        tablis.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/statys.fxml","no"));
        loginSingUpButton1.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Prohod.fxml","no"));
        loginSingUpButton2.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Geolog.fxml","yes"));
        loginSingUpButton3.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Pehat.fxml","no"));
        loginSingUpButton.setOnAction(actionEvent -> {
            // Закрытие текущего окна
            if (loginSingUpButton.getScene() != null && loginSingUpButton.getScene().getWindow() != null) {
                loginSingUpButton.getScene().getWindow().hide();}
            openNewScene("/com/example/proburok/signUp.fxml","no");
        });
    }
    private void loginUser(String loginText, String loginPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = dbHandler.getUser(loginText, loginPassword);
        if (user != null) {
            UserSession.setCurrentUser(user);
            if (user.getLocation().equals("Проходчик")) {
                loginSingUpButton.getScene().getWindow().hide();
                openNewScene("/com/example/proburok/Prohod.fxml","no");
            }
            if (user.getLocation().equals("Геолог")) {
                loginSingUpButton.getScene().getWindow().hide();
                openNewScene("/com/example/proburok/Geolog.fxml","yes");
            }
            if (user.getLocation().equals("Администратор")) {
               loginSingUpButton.setVisible(true);
                loginSingUpButton1.setVisible(true);
                loginSingUpButton2.setVisible(true);
                loginSingUpButton3.setVisible(true);
            }
        } else {
            Shake userLoginAnim = new Shake( login_fild);
            Shake userPasAnim = new Shake(password_fild);
                    userLoginAnim.playAnim();
                    userPasAnim.playAnim();
        }
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
}