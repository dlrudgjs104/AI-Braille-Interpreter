package org.nhnacademy.query;

import lombok.Data;

@Data
public class Household {
    private int householdId;
    private int citizenId;
    private int addressId;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `household`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `household` (\n" +
                "    `household_id` INT NOT NULL,\n" +
                "    `citizen_id` INT NOT NULL,\n" +
                "    `address_id` INT NOT NULL,\n" +
                "    PRIMARY KEY (`household_id`),\n" +
                "    KEY `fk_household_citizen` (`citizen_id`),\n" +
                "    KEY `fk_household_address` (`address_id`),\n" +
                "    CONSTRAINT `fk_household_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_household_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(int householdId, int citizenId, int addressId) {
        return ("INSERT INTO household (`household_id`, `citizen_id`, `address_id`) VALUES (" +
                householdId + "," +
                citizenId + "," +
                addressId +
                ");");
    }
}
