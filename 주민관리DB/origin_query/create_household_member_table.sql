-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db#Query
DROP TABLE IF EXISTS `household_member`;
CREATE TABLE `household_member` (
    `household_id` INT NOT NULL,
    `citizen_id` INT NOT NULL,
    `report_date` DATETIME NOT NULL,
    `change_reason_id` INT NOT NULL,
    `relation_id` INT NOT NULL,
    KEY `fk_household_member_household` (`household_id`),
    KEY `fk_household_member_citizen` (`citizen_id`),
    KEY `fk_household_member_change_reason` (`change_reason_id`),
    KEY `fk_household_member_relation` (`relation_id`),
    CONSTRAINT `household_member_group_pk` PRIMARY KEY (`household_id`, `citizen_id`),
    CONSTRAINT `fk_household_member_household` FOREIGN KEY (`household_id`) REFERENCES `household` (`household_id`),
    CONSTRAINT `fk_household_member_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),
    CONSTRAINT `fk_household_member_change_reason` FOREIGN KEY (`change_reason_id`) REFERENCES `household_change_reason` (`change_reason_id`),
    CONSTRAINT `fk_household_member_relation` FOREIGN KEY (`relation_id`) REFERENCES `household_relation` (`household_relation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;