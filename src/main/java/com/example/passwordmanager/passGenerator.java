package com.example.passwordmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class passGenerator {
    private static final passGenerator instance = new passGenerator();
    private String pass;
    private int numberSelected;
    private String[] upper = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private String[] lower = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private String[] symbols = new String[]{"!", "@", "#", "$", "%", "^", "&", "*", "(", ")"};
    private String[] numbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private boolean upperSelected = true;
    private boolean lowerSelected = true;
    private boolean symbolSelected = true;
    private boolean numbersSelected = true;


    private passGenerator() {
        numberSelected = 4;
        pass = "";
        generatePass(4);
    }
    public static passGenerator getInstance() {
        return instance;
    }
    public String generatePass(int length) {
        pass = "";
        Random r = new Random();
        int f = 0;
        while(f < length) {
            if(f < length) {
                if(upperSelected) {
                    pass = pass + upper[(r.nextInt(0, 25))];
                    f++;
                }
            } else {
                break;
            }
            if(f < length) {
                if(lowerSelected) {
                    pass = pass + lower[(r.nextInt(0, 25))];
                    f++;
                }
            } else {
                break;
            }
            if(f < length) {
                if(symbolSelected) {
                    pass = pass + symbols[(r.nextInt(0, 9))];
                    f++;
                }
            } else {
                break;
            }
            if(f < length) {
                if(numbersSelected) {
                    pass = pass + numbers[(r.nextInt(0, 9))];
                    f++;
                }
            } else {
                break;
            }
        }
        return pass;
    }
    public String getPass() {
        return pass;
    }
    public void selectUpper() {
        if(!upperSelected) {
            upperSelected = true;
            numberSelected++;
        } else {
            upperSelected = false;
            numberSelected--;
        }
    }
    public void selectLower() {
        if(!lowerSelected) {
            lowerSelected = true;
            numberSelected++;
        } else {
            lowerSelected = false;
            numberSelected--;
        }
    }
    public void selectSymbol() {
        if(!symbolSelected) {
            symbolSelected = true;
            numberSelected++;
        } else {
            symbolSelected = false;
            numberSelected--;
        }
    }
    public void selectNumber() {
        if(!numbersSelected) {
            numbersSelected = true;
            numberSelected++;
        } else {
            numbersSelected = false;
            numberSelected--;
        }
    }
    public int getNumberSelected() {
        return numberSelected;
    }

}
