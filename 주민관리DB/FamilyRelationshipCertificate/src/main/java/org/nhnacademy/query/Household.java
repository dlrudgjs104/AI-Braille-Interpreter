package org.nhnacademy.query;

public class Household {
    private Household() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `household`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `household` (" +
                "    `household_id` INT NOT NULL," +
                "    `citizen_id` INT NOT NULL," +
                "    `address_id` INT NOT NULL," +
                "    PRIMARY KEY (`household_id`)," +
                "    KEY `fk_household_citizen` (`citizen_id`)," +
                "    KEY `fk_household_address` (`address_id`)," +
                "    CONSTRAINT `fk_household_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`)," +
                "    CONSTRAINT `fk_household_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}
