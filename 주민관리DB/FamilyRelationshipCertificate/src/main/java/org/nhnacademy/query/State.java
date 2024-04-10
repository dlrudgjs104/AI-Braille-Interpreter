package org.nhnacademy.query;

public class State {
    private State() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `state`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `state` (" +
                "    `state_id` INT NOT NULL AUTO_INCREMENT," +
                "    `state_name` VARCHAR(64) NOT NULL," +
                "    PRIMARY KEY (`state_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }

    public static String addData(String stateName) {
        return ("INSERT INTO state (`state_name`) VALUES (\"" + stateName + "\");");
    }
}
