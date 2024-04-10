package org.nhnacademy.query;

import lombok.Data;

@Data
public class FamilyRelationship {
    private int familyRelationshipId;
    private String relationshipName;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `family_relationship`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `family_relationship` (\n" +
                "    `family_relationship_id` INT NOT NULL AUTO_INCREMENT,\n" +
                "    `relationship_name` VARCHAR(64) NOT NULL,\n" +
                "    PRIMARY KEY (`family_relationship_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(String relationshipName) {
        return ("INSERT INTO family_relationship (`relationship_name`) VALUES (\"" + relationshipName + "\");");
    }
}
