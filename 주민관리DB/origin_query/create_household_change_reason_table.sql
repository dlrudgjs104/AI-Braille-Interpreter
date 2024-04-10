DROP TABLE IF EXISTS `household_change_reason`;
CREATE TABLE `household_change_reason` (
    `change_reason_id` INT NOT NULL AUTO_INCREMENT,
    `change_reason_name` VARCHAR(64),
    PRIMARY KEY (`change_reason_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;