package org.nhnacademy.query;

public class Citizen {
    private static final String ENCODE_KEY = "p@ssW@rd";

    private Citizen() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `citizen`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `citizen` (" +
                "    `citizen_id` INT NOT NULL AUTO_INCREMENT," +
                "    `citizen_reg_num` BLOB NOT NULL," +
                "    `citizen_name` VARCHAR(32) NOT NULL," +
                "    `citizen_gender` CHAR(1) NOT NULL," +
                "    `citizen_birth` DATETIME NOT NULL," +
                "    `address_id` INT NOT NULL," +
                "    `state_id` INT NOT NULL," +
                "    PRIMARY KEY (`citizen_id`)," +
                "    KEY `fk_citizen_address` (`address_id`)," +
                "    KEY `fk_citizen_state` (`state_id`)," +
                "    CONSTRAINT `fk_citizen_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)," +
                "    CONSTRAINT `fk_citizen_state` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }

    public static String addData
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
