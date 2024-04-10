package org.nhnacademy.query;

import lombok.Data;

@Data
public class Citizen {
    private static final String ENCODE_KEY = "p@ssW@rd";

    private int citizenId;
    private String citizenRegNum;
    private String citizenName;
    private char citizenGender;
    private String citizenBirth;
    private int addressId;
    private int stateId;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `citizen`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `citizen` (\n" +
                "    `citizen_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `citizen_reg_num` BLOB NOT NULL,\n" +
                "    `citizen_name` VARCHAR(32) NOT NULL,\n" +
                "    `citizen_gender` CHAR(1) NOT NULL,\n" +
                "    `citizen_birth` DATETIME NOT NULL,\n" +
                "    `address_id` INT NOT NULL,\n" +
                "    `state_id` INT NOT NULL,\n" +
                "    PRIMARY KEY (`citizen_id`),\n" +
                "    KEY `fk_citizen_address` (`address_id`),\n" +
                "    KEY `fk_citizen_state` (`state_id`),\n" +
                "    CONSTRAINT `fk_citizen_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`),\n" +
                "    CONSTRAINT `fk_citizen_state` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData
    (String citizenRegNum, String citizenName, char citizenGender, String citizenBirth, int addressId, int stateId) {
        return ("INSERT INTO citizen (" +
                    "`citizen_reg_num`," + 
                    "`citizen_name`," +
                    "`citizen_gender`," +
                    "`citizen_birth`," +
                    "`address_id`," +
                    "`state_id`" +
                ") VALUES (" + 
                    "hex(aes_encrypt(" + "\"" + citizenRegNum + "\"," +"\"" + ENCODE_KEY + "\")), " +
                    "\"" + citizenName + "\", " + 
                    "\'" + citizenGender + "\', " +
                    "\"" + citizenBirth + "\", " +
                    addressId + ", " +
                    stateId +
                ");");
    }
}
