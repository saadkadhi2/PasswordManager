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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        newpass.setText(p.getPass());
        password = p.getPass();

        //initialization commands for tableview for storing password info
        table.setPlaceholder(new Label(""));
        nameCol.setCellValueFactory(new PropertyValueFactory<savedPassword, String>("name"));
        passCol.setCellValueFactory(new PropertyValueFactory<savedPassword, String>("password"));

        try {
            String[] info;
            File myFile = new File("src/main/resources/com/example/passwordmanager/information.txt");
            Scanner s = new Scanner(myFile);
            while(s.hasNextLine()) {
                String data = s.nextLine();

                if(!data.equals("")) {
                    info = data.split(",");
                    savedPassword sa = new savedPassword();
                    sa.addPassword(info[0], info[1]);

                    ObservableList<savedPassword> tInfo = table.getItems();
                    tInfo.add(sa);
                    table.setItems(tInfo);
                }
            }
            s.close();
        } catch (FileNotFoundException e){
            System.out.println("An error occurred");
            e.printStackTrace();
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

        boolean alreadyExists = false;
        String str = nameForPass.getText();
        String existingName;
        int index = str.indexOf(',');
        if(str.equals("") || str.length() >= 20 || index != -1) {
            warning.setText("Enter a valid name under 20 characters(No commas)!");
            warning.setStyle("-fx-text-fill: pink");
        } else {
            try {
                File myFile = new File("src/main/resources/com/example/passwordmanager/information.txt");
                Scanner reader = new Scanner(myFile);
                while(reader.hasNextLine()) {
                    existingName = reader.nextLine().split(",")[0];
                    if(str.equalsIgnoreCase(existingName)) {
                        alreadyExists = true;
                    }
                }
                reader.close();
                if(alreadyExists) {
                    warning.setText("Name already exists. Please enter a unique name!");
                    warning.setStyle("-fx-text-fill: pink");
                } else {
                    FileWriter w = new FileWriter("src/main/resources/com/example/passwordmanager/information.txt", true);
                    savedPassword s = new savedPassword();
                    s.addPassword(str, password);
                    w.write(str + "," + password + "\n");

                    ObservableList<savedPassword> info = table.getItems();
                    info.add(s);
                    table.setItems(info);


                    warning.setText("Password saved!");
                    warning.setStyle("-fx-text-fill: #77DB77FF");

                    nameForPass.clear();
                    w.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
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
    public void removeInfo(MouseEvent e) {
        ObservableList<savedPassword> items = table.getItems();
        if(!items.isEmpty()) {
            int selectedID = table.getSelectionModel().getSelectedIndex();
            String stringToRemove = table.getItems().get(selectedID).getName() + "," + table.getItems().get(selectedID).getPassword();
            File oldFile = new File("src/main/resources/com/example/passwordmanager/information.txt");
            File newFile = new File("src/main/resources/com/example/passwordmanager/temp.txt");

            String currentLine;

            try {
                FileWriter fw = new FileWriter("src/main/resources/com/example/passwordmanager/temp.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);

                FileReader fr = new FileReader("src/main/resources/com/example/passwordmanager/information.txt");
                BufferedReader br = new BufferedReader(fr);

                while((currentLine = br.readLine()) != null)
                {

                    if(!stringToRemove.equals(currentLine))
                    {
                        pw.println(currentLine);
                    }
                }
                pw.flush();
                pw.close();
                fr.close();
                br.close();
                bw.close();
                fw.close();

                oldFile.delete();
                File dump = new File("src/main/resources/com/example/passwordmanager/information.txt");
                newFile.renameTo(dump);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }

            if(selectedID == -1) {
                warning1.setText("Select a row to delete");
                warning1.setStyle("-fx-text-fill: pink");
            } else {


                System.out.println();
                table.getItems().remove(selectedID);
                warning1.setText("Information deleted");
                warning1.setStyle("-fx-text-fill: #77DB77FF");
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
                warning1.setText("Select a row to delete");
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