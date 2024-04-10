package org.nhnacademy.query;

import lombok.Data;

@Data
public class DeadRegistration {
    private int personDeadId;
    private String email;
    private String phoneNumber;
    private String deadDate;
    private String reporterQualifications;
    private String reportDate;
    private int citizenId;
    private int placeDeadId;
    private int addressId;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `dead_registration`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `dead_registration` (\n" +
                "    `person_dead_id` INT NOT NULL,\n" +
                "    `email` VARCHAR(64),\n" +
                "    `phone_number` VARCHAR(16),\n" +
                "    `dead_date` DATETIME NOT NULL,\n" +
                "    `reporter_Qualifications` VARCHAR(16) NOT NULL,\n" +
                "    `report_date` DATETIME NOT NULL,\n" +
                "    `citizen_id` INT NOT NULL,\n" +
                "    `place_dead_id` INT NOT NULL,\n" +
                "    `address_id` INT NOT NULL,\n" +
                "    PRIMARY KEY (`person_dead_id`),\n" +
                "    KEY `fk_citizen_dead_taget` (`person_dead_id`),\n" +
                "    KEY `fk_citizen_dead_relation` (`citizen_id`),\n" +
                "    KEY `fk_place_dead` (`place_dead_id`),\n" +
                "    KEY `fk_dead_address` (`address_id`),\n" +
                "    CONSTRAINT `fk_citizen_dead_taget` FOREIGN KEY (`person_dead_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_citizen_dead_relation` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_place_dead` FOREIGN KEY (`place_dead_id`) REFERENCES `place_dead` (`place_dead_id`),\n" +
                "    CONSTRAINT `fk_dead_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData
    (int personDeadId, String email, String phoneNumber, String deadDate, String reporterQualifications, String reportDate, int citizenId, int placeDeadId, int addressId) {
        return ("INSERT INTO dead_registration (person_dead_id, email, phone_number, dead_date, reporter_Qualifications, report_date, citizen_id, place_dead_id, address_id)" + 
                "VALUES (" +
                    personDeadId + ", " +
                    (email == null ? "NULL, " : "\"" + email + "\", ") +
                    (phoneNumber == null ? "NULL, " : "\"" + phoneNumber + "\", ") +
                    "\"" + deadDate + "\", " +
                    "\"" + reporterQualifications + "\", " +
                    "\"" + reportDate + "\", " +
                    citizenId + ", " +
                    placeDeadId + ", " +
                    addressId +
                ");");
    }
}
