package com.example.passwordmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class Createaccount {
    @FXML
    private Rectangle exitRect;

    @FXML
    private Rectangle exitRect1;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField passwordTextField1;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label warningText;

    private Stage stage;
    private Scene scene;
    private double xOffset = 0;
    private double yOffset = 0;

    public void changeExitColor1() {
        exitRect.setStyle("-fx-fill:#cdcd6f");
    }
    public void changeExitColor2() {exitRect.setStyle("-fx-fill:#f1f184");}
    public void changeExitColor3() {
        exitRect1.setStyle("-fx-fill:#cdcd6f");
    }
    public void changeExitColor4() {
        exitRect1.setStyle("-fx-fill:#f1f184");
    }

    @FXML
    void exitProgram() {
        System.exit(1);
    }

    @FXML
    void registerButtonOnAction() throws IOException {
        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank() && !phoneTextField.getText().isBlank()) {
            if(passwordTextField.getText().equals(passwordTextField1.getText())) {
                validateRegistration();
            } else {
                warningText.setText("Passwords do not match.");
            }

        } else {
            warningText.setText("Required Fields are blank");
        }

    }

    private void validateRegistration() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin = "INSERT INTO passwordmanageraccounts (Username, Password, PhoneNumber) VALUES ('" +
                usernameTextField.getText() +
                "', '" +
                passwordTextField.getText() +
                "', '" +
                phoneTextField.getText() +
                "');";
        try {
            Statement statement = connectDB.createStatement();
            int queryResult = statement.executeUpdate(verifyLogin);

            if (queryResult != 0) {
                toLogin();
            } else {
                warningText.setText("Invalid Registration");
            }
        } catch(Exception e) {
            e.printStackTrace();
            warningText.setText("An error occurred. Please try again.");
        }
    }


    public void toLogin() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
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

}
