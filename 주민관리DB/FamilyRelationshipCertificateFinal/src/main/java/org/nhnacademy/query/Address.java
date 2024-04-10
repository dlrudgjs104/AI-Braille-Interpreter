package org.nhnacademy.query;

import lombok.Data;

@Data
public class Address {
    private int addressId;
    private String addressName;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `address`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `address` (\n" +
                "    `address_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `address_name` VARCHAR(128) NOT NULL,\n" +
                "    PRIMARY KEY (`address_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String addressName) {
        return ("INSERT INTO address (`address_name`) VALUES (\"" + addressName + "\");");
    }
}
