package org.nhnacademy.query;

import lombok.Data;

@Data
public class FamilyRelationMerge {
    private int relativeSubjectId;
    private int relativeObjectId;
    private int relationshipId;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `family_relation_merge`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `family_relation_merge` (\n" +
                "    `relative_subject_id` INT NOT NULL,\n" +
                "    `relative_object_id` INT NOT NULL,\n" +
                "    `relationship_id` INT NOT NULL,\n" +
                "    KEY `fk_family_relation_subject` (`relative_subject_id`),\n" +
                "    KEY `fk_family_relation_object` (`relative_object_id`),\n" +
                "    KEY `fk_family_relation_merge_relation` (`relationship_id`),\n" +
                "    CONSTRAINT `fk_family_relation_subject` FOREIGN KEY (`relative_subject_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_family_relation_object` FOREIGN KEY (`relative_object_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_family_relation_merge_relation` FOREIGN KEY (`relationship_id`) REFERENCES `family_relationship` (`family_relationship_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(int relativeSubjectId, int relativeObjectId, int relationshipId) {
        return ("INSERT INTO family_relation_merge (`relative_subject_id`, `relative_object_id`, `relationship_id`) VALUES (" +
                relativeSubjectId + "," +
                relativeObjectId + "," +
                relationshipId +
                ");");
    }
}

