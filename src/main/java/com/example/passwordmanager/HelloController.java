package com.example.passwordmanager;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;


import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelloController implements Initializable {
    passGenerator p = passGenerator.getInstance();
    String password = new String();
    int passIndex = 0;
    @FXML
    private Label newpass;
    @FXML
    private Label passLength;
    @FXML
    private Slider slider;
    @FXML
    private TextField nameForPass;
    @FXML
    private Label warning;
    @FXML
    private Label warning1;
    @FXML
    private CheckBox upperCheck;
    @FXML
    private CheckBox lowerCheck;
    @FXML
    private CheckBox symbolCheck;
    @FXML
    private CheckBox numberCheck;
    @FXML
    private Rectangle exitRect;
    @FXML
    private Rectangle exitRect1;
    @FXML
    private TableView<savedPassword> table;
    @FXML
    private TableColumn<savedPassword, String> nameCol;
    @FXML
    private TableColumn<savedPassword, String> passCol;
    @FXML
    private ImageView trash;
    @FXML
    private ImageView copyClipboard;
    private int userId;

    public void setUserId(int userId) {
        this.userId = userId;
        loadUserPasswords();
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newpass.setText(p.getPass());
        password = p.getPass();

        table.setPlaceholder(new Label(""));
        nameCol.setCellValueFactory(new PropertyValueFactory<savedPassword, String>("name"));
        passCol.setCellValueFactory(new PropertyValueFactory<savedPassword, String>("password"));

    }
    private void loadUserPasswords() {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            String query = "SELECT name, password FROM userpasswords WHERE user_id = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<savedPassword> passwordList = table.getItems();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String decryptedPassword;
                try {
                    decryptedPassword = EncryptionUtil.decrypt(password);
                } catch(IllegalArgumentException e) {
                    decryptedPassword = password;
                }

                savedPassword sa = new savedPassword();
                sa.addPassword(name, decryptedPassword);
                passwordList.add(sa);
            }
            table.setItems(passwordList);

            resultSet.close();
            preparedStatement.close();
            connectDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void genPass() {
        password = p.generatePass((int)slider.getValue());
        newpass.setText(password);
    }
    public void setSlider() {
        passLength.setText(String.valueOf((int)slider.getValue()));
        password = p.generatePass((int)slider.getValue());
        newpass.setText(password);
    }
    public void upperCheckbox() {
        if(p.getNumberSelected() != 1 || upperCheck.isSelected()) {
            p.selectUpper();
            password = p.generatePass((int)slider.getValue());
            newpass.setText(password);
        } else {
            upperCheck.setSelected(true);
        }
    }
    public void lowerCheckbox() {
        if(p.getNumberSelected() != 1 || lowerCheck.isSelected()) {
            p.selectLower();
            password = p.generatePass((int)slider.getValue());
            newpass.setText(password);
        } else {
            lowerCheck.setSelected(true);
        }
    }
    public void symbolsCheckbox() {
        if(p.getNumberSelected() != 1 || symbolCheck.isSelected()) {
            p.selectSymbol();
            password = p.generatePass((int)slider.getValue());
            newpass.setText(password);
        } else {
            symbolCheck.setSelected(true);
        }
    }
    public void numbersCheckbox() {
        if(p.getNumberSelected() != 1 || numberCheck.isSelected()) {
            p.selectNumber();
            password = p.generatePass((int)slider.getValue());
            newpass.setText(password);
        } else {
            numberCheck.setSelected(true);
        }
    }
    public void savePassword() {
        String str = nameForPass.getText();
        int index = str.indexOf(',');
        if (str.equals("") || str.length() >= 20 || index != -1) {
            warning.setText("Enter a valid name under 20 characters (No commas)!");
            warning.setStyle("-fx-text-fill: pink");
        } else {
            try {
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();

                String checkQuery = "SELECT COUNT(*) FROM userpasswords WHERE user_id = ? AND name = ?";
                PreparedStatement checkStmt = connectDB.prepareStatement(checkQuery);
                checkStmt.setInt(1, userId);
                checkStmt.setString(2, str);

                ResultSet checkResult = checkStmt.executeQuery();
                checkResult.next();
                int count = checkResult.getInt(1);

                if (count > 0) {
                    warning.setText("Name already exists. Please enter a unique name!");
                    warning.setStyle("-fx-text-fill: pink");
                } else {
                    String encryptedPassword = EncryptionUtil.encrypt(password);
                    String insertQuery = "INSERT INTO userpasswords (user_id, name, password) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = connectDB.prepareStatement(insertQuery);
                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, str);
                    insertStmt.setString(3, encryptedPassword);

                    insertStmt.executeUpdate();

                    savedPassword s = new savedPassword();
                    s.addPassword(str, password);

                    ObservableList<savedPassword> info = table.getItems();
                    info.add(s);
                    table.setItems(info);

                    warning.setText("Password saved!");
                    warning.setStyle("-fx-text-fill: #77DB77FF");

                    nameForPass.clear();

                    insertStmt.close();
                }
                checkResult.close();
                checkStmt.close();
                connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void copyPass() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent c = new ClipboardContent();

        c.putString(newpass.getText());
        clipboard.setContent(c);
        warning.setText("Password copied to clipboard");
        warning.setStyle("-fx-text-fill: #77DB77FF");
    }
    public void removeInfo() {
        ObservableList<savedPassword> items = table.getItems();
        if(!items.isEmpty()) {
            int selectedIndex = table.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                warning1.setText("Select a row to delete");
                warning1.setStyle("-fx-text-fill: pink");
            } else {
                savedPassword selectedPassword = table.getItems().get(selectedIndex);
                String nameToRemove = selectedPassword.getName();

                try {
                    DatabaseConnection connectNow = new DatabaseConnection();
                    Connection connectDB = connectNow.getConnection();

                    String deleteQuery = "DELETE FROM userpasswords WHERE user_id = ? AND name = ?";
                    PreparedStatement deleteStmt = connectDB.prepareStatement(deleteQuery);
                    deleteStmt.setInt(1, userId);
                    deleteStmt.setString(2, nameToRemove);

                    int rowsAffected = deleteStmt.executeUpdate();

                    if (rowsAffected != 0) {
                        table.getItems().remove(selectedIndex);
                        warning1.setText("Information deleted");
                        warning1.setStyle("-fx-text-fill: #77DB77FF");
                    } else {
                        warning1.setText("Failed to delete information");
                        warning1.setStyle("-fx-text-fill: pink");
                    }
                    deleteStmt.close();
                    connectDB.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    warning1.setText("An error occurred while deleting.");
                    warning1.setStyle("-fx-text-fill: pink");
                }
            }
        }

    }
    public void trashEnter() {

        trash.setStyle("-fx-effect: dropshadow(gaussian, #f1f184, 10, 0, 1, 1)");
    }
    public void trashExit() {
        trash.setStyle(null);

    }
    public void clipEnter() {
        copyClipboard.setStyle("-fx-effect: dropshadow(gaussian, #f1f184, 10, 0, 1, 1)");
    }
    public void clipExit() {
        copyClipboard.setStyle(null);
    }
    public void copyFromTable() {
        ObservableList<savedPassword> items = table.getItems();
        if(!items.isEmpty()) {
            int selectedID = table.getSelectionModel().getSelectedIndex();
            if(selectedID != -1) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent c = new ClipboardContent();

                c.putString(table.getItems().get(selectedID).getPassword());
                clipboard.setContent(c);
                warning1.setText("Password copied to clipboard");
                warning1.setStyle("-fx-text-fill: #77DB77FF");
            } else {
                warning1.setText("Select a row to copy");
                warning1.setStyle("-fx-text-fill: pink");
            }
        }
    }
    public void clearWarning() {
        warning.setText("");
        warning1.setText("");
    }
    public void changeExitColor1() {
        exitRect.setStyle("-fx-fill:#cdcd6f");
    }
    public void changeExitColor2() {
        exitRect.setStyle("-fx-fill:#f1f184");
    }
    public void changeExitColor3() {
        exitRect1.setStyle("-fx-fill:#cdcd6f");
    }
    public void changeExitColor4() {
        exitRect1.setStyle("-fx-fill:#f1f184");
    }
    public void exitProgram() {
        System.exit(1);
    }

//#dada78
}