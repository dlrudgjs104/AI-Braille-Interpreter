package org.nhnacademy.query;

import lombok.Data;

@Data
public class HouseholdChangeReason {
    private int changeReasonId;
    private String changeReasonName;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `household_change_reason`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `household_change_reason` (\n" +
                "    `change_reason_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `change_reason_name` VARCHAR(64),\n" +
                "    PRIMARY KEY (`change_reason_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String changeReasonName) {
        return ("INSERT INTO household_change_reason (`change_reason_name`) VALUES (" +
                    "\"" + changeReasonName + "\"" +
                ");");
    }
}
