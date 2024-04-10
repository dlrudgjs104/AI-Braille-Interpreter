package org.nhnacademy.query;

import lombok.Data;

@Data
public class BirthRegistration {
    private int personBirthId;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private int citizenId;
    private int placeBirthId;
    private int addressId;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `birth_registration`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `birth_registration` (\n" +
                "    `person_birth_id` INT NOT NULL,\n" +
                "    `email` VARCHAR(64),\n" +
                "    `phone_number` VARCHAR(16),\n" +
                "    `birth_date` DATETIME NOT NULL,\n" +
                "    `reporter_Qualifications` VARCHAR(16) NOT NULL,\n" +
                "    `report_date` DATETIME NOT NULL,\n" +
                "    `citizen_id` INT NOT NULL,\n" +
                "    `place_birth_id` INT NOT NULL,\n" +
                "    `address_id` INT NOT NULL,\n" +
                "    PRIMARY KEY (`person_birth_id`),\n" +
                "    KEY `fk_citizen_taget` (`person_birth_id`),\n" +
                "    KEY `fk_citizen_relation` (`citizen_id`),\n" +
                "    KEY `fk_place_birth` (`place_birth_id`),\n" +
                "    KEY `fk_address` (`address_id`),\n" +
                "    CONSTRAINT `fk_citizen_taget` FOREIGN KEY (`person_birth_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_citizen_relation` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_place_birth` FOREIGN KEY (`place_birth_id`) REFERENCES `place_birth` (`place_birth_id`),\n" +
                "    CONSTRAINT `fk_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData
    (int personBirthId, String email, String phoneNumber, String birthDate, String reporterQualifications, String reportDate, int citizenId, int placeBirthId, int addressId) {
        return ("INSERT INTO birth_registration (`person_birth_id`, `email`, `phone_number`, `birth_date`, `reporter_Qualifications`, `report_date`, `citizen_id`, `place_birth_id`, `address_id`) " + 
                "VALUES (" +
                        personBirthId + "," +
                        "\"" + email + "\"," +
                        "\"" + phoneNumber + "\"," +
                        "\"" + birthDate + "\"," +
                        "\"" + reporterQualifications + "\"," +
                        "\"" + reportDate + "\"," +
                        citizenId + "," +
                        placeBirthId + "," +
                        addressId +
                ");");
    }
}
