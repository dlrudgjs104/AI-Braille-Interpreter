package org.nhnacademy.query;

public class HouseholdRelation {
    private HouseholdRelation() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `household_relation`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `household_relation` (" +
                "    `household_relation_id` INT NOT NULL AUTO_INCREMENT," +
                "    `relation_name` VARCHAR(64)," +
                "    PRIMARY KEY (`household_relation_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}
