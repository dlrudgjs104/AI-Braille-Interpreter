package org.nhnacademy.query;

public class TransAddressRecord {
    private TransAddressRecord() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `trans_address_record`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `trans_address_record` (" +
                "    `report_date` DATETIME NOT NULL," +
                "    `citizen_id` INT NOT NULL," +
                "    `address_id` INT NOT NULL," +
                "    KEY `fk_trans_address_citizen` (`citizen_id`)," +
                "    KEY `fk_trans_address_record_address` (`address_id`)," +
                "    CONSTRAINT `fk_trans_address_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`)," +
                "    CONSTRAINT `fk_trans_address_record_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}
