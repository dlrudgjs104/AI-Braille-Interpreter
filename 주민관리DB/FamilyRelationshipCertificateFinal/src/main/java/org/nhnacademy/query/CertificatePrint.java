package org.nhnacademy.query;

import lombok.Data;

@Data
public class CertificatePrint {
    private int certificatePrintId;
    private String issueDate;
    private int citizenId;
    private int certificateCodeId;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `certificate_print`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `certificate_print` (\n" +
                "    `certificate_print_id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "    `issue_date` DATETIME NOT NULL,\n" +
                "    `citizen_id` INT NOT NULL,\n" +
                "    `certificate_code_id` INT NOT NULL,\n" +
                "    PRIMARY KEY (`certificate_print_id`),\n" +
                "    KEY `fk_certificate_print_citizen` (`citizen_id`),\n" +
                "    KEY `fk_certificate_print_certificate_code` (`certificate_code_id`),\n" +
                "    CONSTRAINT `fk_certificate_print_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_certificate_print_certificate_code` FOREIGN KEY (`certificate_code_id`) REFERENCES `certificate_code` (`certificate_code_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String issueDate, int citizenId, int certificateCodeId) {
        return ("INSERT INTO certificate_print (`issue_date`, `citizen_id`, `certificate_code_id`) VALUES (" +
                    "\"" + issueDate + "\"," +
                    citizenId + "," +
                    certificateCodeId +
                ");");
    }
}