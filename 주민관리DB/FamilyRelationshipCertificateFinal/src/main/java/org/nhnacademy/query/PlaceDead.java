package org.nhnacademy.query;

import lombok.Data;

@Data
public class PlaceDead {
    private int placeDeadId;
    private String placeDeadDivision;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `place_dead`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `place_dead` (\n" +
                "    `place_dead_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `place_dead_division` VARCHAR(32),\n" +
                "    PRIMARY KEY (`place_dead_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String placeDeadDivision) {
        return ("INSERT INTO place_dead (`place_dead_division`) VALUES (\"" + placeDeadDivision + "\")");
    }
}
