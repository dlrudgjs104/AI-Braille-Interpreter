package org.nhnacademy;

import org.nhnacademy.controller.DatabaseController;

public class Main {
    public static void main(String[] args) {
        DatabaseController databaseController = new DatabaseController();
        databaseController.buildTable();
        databaseController.makeData();
    }
}