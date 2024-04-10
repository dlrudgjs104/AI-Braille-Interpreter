package org.nhnacademy.query;

public class DeadRegistration {
    private DeadRegistration() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `dead_registration`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `dead_registration` (" +
                "    `person_dead_id` INT NOT NULL," +
                "    `email` VARCHAR(64)," +
                "    `phone_number` VARCHAR(16)," +
                "    `dead_date` DATETIME NOT NULL," +
                "    `reporter_Qualifications` VARCHAR(16) NOT NULL," +
                "    `report_date` DATETIME NOT NULL," +
                "    `citizen_id` INT NOT NULL," +
                "    `place_dead_id` INT NOT NULL," +
                "    `address_id` INT NOT NULL," +
                "    PRIMARY KEY (`person_dead_id`)," +
                "    KEY `fk_citizen_dead_taget` (`person_dead_id`)," +
                "    KEY `fk_citizen_dead_relation` (`citizen_id`)," +
                "    KEY `fk_place_dead` (`place_dead_id`)," +
                "    KEY `fk_dead_address` (`address_id`)," +
                "    CONSTRAINT `fk_citizen_dead_taget` FOREIGN KEY (`person_dead_id`) REFERENCES `citizen` (`citizen_id`)," +
                "    CONSTRAINT `fk_citizen_dead_relation` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`)," +
                "    CONSTRAINT `fk_place_dead` FOREIGN KEY (`place_dead_id`) REFERENCES `place_dead` (`place_dead_id`)," +
                "    CONSTRAINT `fk_dead_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}
