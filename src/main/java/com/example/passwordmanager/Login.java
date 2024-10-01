package com.example.passwordmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextFieldVisible;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label forgotPassword;
    @FXML
    private Rectangle exitRect;
    @FXML
    private Label warningText;
    @FXML
    private ImageView showPassword;
    @FXML
    private Button registerButton;
    private Stage stage;
    private Scene scene;
    private double xOffset = 0;
    private double yOffset = 0;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void loginButtonOnAction(ActionEvent e) {
        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            validateLogin();
        } else {
            warningText.setText("Fields are blank");
        }
    }
    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin;
        if(passwordTextField.isVisible()) {
            verifyLogin = "SELECT * FROM passwordmanageraccounts WHERE Username = '" +
                    usernameTextField.getText() +
                    "' AND Password = '" +
                    passwordTextField.getText() +
                    "'";
        } else {
            verifyLogin = "SELECT * FROM passwordmanageraccounts WHERE Username = '" +
                    usernameTextField.getText() +
                    "' AND Password = '" +
                    passwordTextFieldVisible.getText() +
                    "'";
        }
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            if (queryResult.next() && queryResult.getInt(1) == 1) {
                int userId = queryResult.getInt("idPasswordManagerAccounts");
                String userPhoneNumber = queryResult.getString("PhoneNumber");
                String twoFactorCode = generate2FACode();
                if (!userPhoneNumber.startsWith("+")) {
                    userPhoneNumber = "+1" + userPhoneNumber;
                }

                TwilioService.sendVerificationCode(userPhoneNumber);

                toTwoFactorAuthenticationScreen(userId, userPhoneNumber);
            } else {
                warningText.setText("Invalid login credentials.");
            }
        } catch(Exception e) {
            e.printStackTrace();
            warningText.setText("An error occurred. Please try again.");
        }

    }

    private String generate2FACode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    private void toTwoFactorAuthenticationScreen(int userId, String userPhoneNumber) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("twofactorauth.fxml"));
        Parent root = loader.load();

        TwoFactorAuthController twoFactorAuthController = loader.getController();
        twoFactorAuthController.setUserId(userId);
        twoFactorAuthController.setUserPhoneNumber(userPhoneNumber);

        Scene scene = new Scene(root);
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        this.stage = currentStage;

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        currentStage.setScene(scene);
        currentStage.show();
    }

    public void passwordVisibility() {
        if(passwordTextField.isVisible()) {
            passwordTextFieldVisible.setText(passwordTextField.getText());
            passwordTextField.setVisible(false);
        } else {
            passwordTextField.setText(passwordTextFieldVisible.getText());
            passwordTextField.setVisible(true);
        }
    }
    public void register() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("createaccount.fxml")));
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) registerButton.getScene().getWindow();
        this.stage = currentStage;

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        currentStage.setScene(newScene);
        currentStage.show();
    }

    public void highlightText() {
        forgotPassword.setStyle("-fx-font-weight: bold");
    }
    public void unhighlightText() {
        forgotPassword.setStyle("-fx-font-weight: normal");
    }
    public void changeExitColor1() {
        exitRect.setStyle("-fx-fill:#cdcd6f");
    }
    public void changeExitColor2() {
        exitRect.setStyle("-fx-fill:#f1f184");
    }
    public void loginHover() {
        loginButton.setStyle("-fx-background-color:#3f4040");
    }
    public void loginUnHover() {
        loginButton.setStyle("-fx-background-color:#545656");
    }
    public void exitProgram() {
        System.exit(1);
    }
}
