package com.example.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class TwoFactorAuthController {
    @FXML
    private TextField twoFactorCodeField;
    @FXML
    private Button verifyButton;
    @FXML
    private Label warningLabel;
    private Stage stage;
    private Scene scene;
    private double xOffset = 0;
    private double yOffset = 0;
    private String userPhoneNumber;
    private int userId;

    public void setUserPhoneNumber(String phoneNumber) {
        this.userPhoneNumber = phoneNumber;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    public void verifyCode() {
        String enteredCode = twoFactorCodeField.getText();
        boolean isVerified = TwilioService.verifyCode(userPhoneNumber, enteredCode);

        if (isVerified) {
            toMainApp();
        } else {
            warningLabel.setText("Invalid 2FA code. Please try again.");
        }
    }

    private void toMainApp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("stage1.fxml"));
            Parent root = loader.load();
            HelloController helloController = loader.getController();
            helloController.setUserId(userId);
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) verifyButton.getScene().getWindow();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}