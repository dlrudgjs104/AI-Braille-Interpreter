package org.nhnacademy.query;

public class HouseholdChangeReason {
    private HouseholdChangeReason() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `household_change_reason`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `household_change_reason` (" +
                "    `change_reason_id` INT NOT NULL AUTO_INCREMENT," +
                "    `change_reason_name` VARCHAR(64)," +
                "    PRIMARY KEY (`change_reason_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}
