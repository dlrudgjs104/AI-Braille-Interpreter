package org.nhnacademy.query;

import lombok.Data;

@Data
public class CertificateCode {
    private int certificateCodeId;
    private String certificateCodeName;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `certificate_code`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `certificate_code` (\n" +
                "    `certificate_code_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `certificate_code_name` VARCHAR(64) NOT NULL,\n" +
                "    PRIMARY KEY(certificate_code_id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String certificateCodeName) {
        return ("INSERT INTO certificate_code (`certificate_code_name`) VALUES (\"" + certificateCodeName + "\");");
    }
}

