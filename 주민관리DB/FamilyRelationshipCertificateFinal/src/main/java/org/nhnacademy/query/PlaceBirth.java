package org.nhnacademy.query;

import lombok.Data;

@Data
public class PlaceBirth {
    private int placeBirthId;
    private String placeName;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `place_birth`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `place_birth` (\n" +
                "    `place_birth_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `place_name` VARCHAR(44) NOT NULL,\n" +
                "    PRIMARY KEY (`place_birth_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String placeName) {
        return ("INSERT INTO place_birth (`place_name`) VALUES (\"" + placeName + "\");");
    }
}
