package org.nhnacademy.query;

public class Address {
    private Address() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `address`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `address` (" +
                "    `address_id` INT NOT NULL AUTO_INCREMENT," +
                "    `address_name` VARCHAR(128) NOT NULL," +
                "    PRIMARY KEY (`address_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }

    public static String addData(String addressName) {
        return ("INSERT INTO address (`address_name`) VALUES (\"" + addressName + "\");");
    }
}
