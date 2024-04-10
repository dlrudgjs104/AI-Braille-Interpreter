package org.nhnacademy.query;

import lombok.Data;

@Data
public class HouseholdMember {
    private int householdId;
    private int citizenId;
    private String reportDate;
    private int changeReasonId;
    private int relationId;

    public static String dropTable() {
        return ("DROP TABLE IF EXISTS `household_member`;");
    }

    public static String createTable() {
        return ("CREATE TABLE `household_member` (\n" +
                "    `household_id` INT NOT NULL,\n" +
                "    `citizen_id` INT NOT NULL,\n" +
                "    `report_date` DATETIME NOT NULL,\n" +
                "    `change_reason_id` INT NOT NULL,\n" +
                "    `relation_id` INT NOT NULL,\n" +
                "    KEY `fk_household_member_household` (`household_id`),\n" +
                "    KEY `fk_household_member_citizen` (`citizen_id`),\n" +
                "    KEY `fk_household_member_change_reason` (`change_reason_id`),\n" +
                "    KEY `fk_household_member_relation` (`relation_id`),\n" +
                "    CONSTRAINT `household_member_group_pk` PRIMARY KEY (`household_id`, `citizen_id`),\n" +
                "    CONSTRAINT `fk_household_member_household` FOREIGN KEY (`household_id`) REFERENCES `household` (`household_id`),\n" +
                "    CONSTRAINT `fk_household_member_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),\n" +
                "    CONSTRAINT `fk_household_member_change_reason` FOREIGN KEY (`change_reason_id`) REFERENCES `household_change_reason` (`change_reason_id`),\n" +
                "    CONSTRAINT `fk_household_member_relation` FOREIGN KEY (`relation_id`) REFERENCES `household_relation` (`household_relation_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n");
    }

    public static String insertData(int householdId, int citizenId, String reportDate, int changeReasonId, int relationId) {
        return ("INSERT INTO household_member (`household_id`, `citizen_id`, `report_date`, `change_reason_id`, `relation_id`) VALUES (" +
                    householdId + "," +
                    citizenId + "," +
                    "\"" + reportDate + "\"," +
                    changeReasonId + "," +
                    relationId +
                ");");
    }
}
