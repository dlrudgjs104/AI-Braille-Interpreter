package org.nhnacademy.query;

public class PlaceDead {
    private PlaceDead() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `place_dead`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `place_dead` (" +
                "    `place_dead_id` INT NOT NULL AUTO_INCREMENT," +
                "    `place_dead_ division` VARCHAR(32)," +
                "    PRIMARY KEY (`place_dead_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}
