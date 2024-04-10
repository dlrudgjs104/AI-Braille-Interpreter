package org.nhnacademy.query;

public class FamilyRelationship {
    private FamilyRelationship() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `family_relationship`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `family_relationship` (" +
                "    `family_relationship_id` INT NOT NULL AUTO_INCREMENT," +
                "    `relationship_name` VARCHAR(64) NOT NULL," +
                "    PRIMARY KEY (`family_relationship_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}
