package org.nhnacademy.query;

public class CertificateCode {
    private CertificateCode() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `certificate_code`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `certificate_code` (" +
                "    `certificate_code_id` INT NOT NULL AUTO_INCREMENT," +
                "    `certificate_code_name` VARCHAR(64) NOT NULL," +
                "    PRIMARY KEY(certificate_code_id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}

