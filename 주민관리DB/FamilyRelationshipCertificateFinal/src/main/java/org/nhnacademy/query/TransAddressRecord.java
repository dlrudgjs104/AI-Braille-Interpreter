package org.nhnacademy.query;

import lombok.Data;

@Data
public class TransAddressRecord {
    private String reportDate;
    private int citizenId;
    private int addressId;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `trans_address_record`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `trans_address_record` (\n" +
                "    `report_date` DATETIME NOT NULL,\n" +
                "    `citizen_id` INT NOT NULL,\n" +
                "    `address_id` INT NOT NULL,\n" +
                "    KEY `fk_trans_address_citizen` (`citizen_id`),\n" +
                "    KEY `fk_trans_address_record_address` (`address_id`),\n" +
                "    CONSTRAINT `fk_trans_address_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_trans_address_record_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String reportDate, int citizenId, int addressId) {
        return ("INSERT INTO trans_address_record (`report_date`, `citizen_id`, `address_id`) VALUES (" +
                    "\"" + reportDate + "\"," + 
                    citizenId + "," +
                    addressId +
                ");");
    }
}
