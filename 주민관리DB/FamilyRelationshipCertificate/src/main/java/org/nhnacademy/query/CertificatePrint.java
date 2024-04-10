package org.nhnacademy.query;

public class CertificatePrint {
    private CertificatePrint() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `certificate_print`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `certificate_print` (" +
                "    `certificate_print_id` INT NOT NULL AUTO_INCREMENT," +
                "    `issue_date` DATETIME NOT NULL," +
                "    `citizen_id` INT NOT NULL," +
                "    `certificate_code_id` INT NOT NULL," +
                "    PRIMARY KEY (`certificate_print_id`)," +
                "    KEY `fk_certificate_print_citizen` (`citizen_id`)," +
                "    KEY `fk_certificate_print_certificate_code` (`certificate_code_id`)," +
                "    CONSTRAINT `fk_certificate_print_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`)," +
                "    CONSTRAINT `fk_certificate_print_certificate_code` FOREIGN KEY (`certificate_code_id`) REFERENCES `certificate_code` (`certificate_code_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}