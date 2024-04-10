package org.nhnacademy.query;

public class PlaceBirth {
    private PlaceBirth() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `place_birth`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `place_birth` (" +
                "    `place_birth_id` INT NOT NULL AUTO_INCREMENT," +
                "    `place_name` VARCHAR(44) NOT NULL," +
                "    PRIMARY KEY (`place_birth_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }

    public static String addData(String placeName) {
        return ("INSERT INTO place_birth (`place_name`) VALUES (\"" + placeName + "\");");
    }
}
