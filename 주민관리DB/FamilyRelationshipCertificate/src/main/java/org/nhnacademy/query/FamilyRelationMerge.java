package org.nhnacademy.query;

public class FamilyRelationMerge {
    private FamilyRelationMerge() throws IllegalAccessException {
        throw new IllegalAccessException("this class is util class");
    }

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `family_relation_merge`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `family_relation_merge` (" +
                "    `relative_subject_id` INT NOT NULL," +
                "    `relative_object_id` INT NOT NULL," +
                "    `relationship_id` INT NOT NULL," +
                "    KEY `fk_family_relation_subject` (`relative_subject_id`)," +
                "    KEY `fk_family_relation_object` (`relative_object_id`)," +
                "    KEY `fk_family_relation_merge_relation` (`relationship_id`)," +
                "    CONSTRAINT `fk_family_relation_subject` FOREIGN KEY (`relative_subject_id`) REFERENCES `citizen` (`citizen_id`)," +
                "    CONSTRAINT `fk_family_relation_object` FOREIGN KEY (`relative_object_id`) REFERENCES `citizen` (`citizen_id`)," +
                "    CONSTRAINT `fk_family_relation_merge_relation` FOREIGN KEY (`relationship_id`) REFERENCES `family_relationship` (`family_relationship_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
    }
}

