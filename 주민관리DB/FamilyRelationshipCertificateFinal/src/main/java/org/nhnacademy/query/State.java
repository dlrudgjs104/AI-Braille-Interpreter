package org.nhnacademy.query;

import lombok.Data;

@Data
public class State {
    private int stateId;
    private String stateName;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `state`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `state` (\n" +
                "    `state_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `state_name` VARCHAR(64) NOT NULL,\n" +
                "    PRIMARY KEY (`state_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String stateName) {
        return ("INSERT INTO state (`state_name`) VALUES (\"" + stateName + "\");");
    }
}
