package org.nhnacademy.query;

import lombok.Data;

@Data
public class HouseholdRelation {
    private int householdRelationId;
    private String relationName;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `household_relation`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `household_relation` (\n" +
                "    `household_relation_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `relation_name` VARCHAR(64),\n" +
                "    PRIMARY KEY (`household_relation_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String relationName) {
        return ("INSERT INTO household_relation (`relation_name`) VALUES (\"" + relationName + "\")");
    }
}
