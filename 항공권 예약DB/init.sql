CREATE DATABASE IF not exists `flight_ticket_reservation_db` DEFAULT CHARACTER SET = 'utf8mb4';

-- Active: 1667279984414@@127.0.0.1@3306@flight_ticket_reservation_db

DROP TABLE IF EXISTS `combined_product_detail`;
DROP TABLE IF EXISTS `airline_ticket`;
DROP TABLE IF EXISTS `airplane_seat`;
DROP TABLE IF EXISTS `refund`;
DROP TABLE IF EXISTS `refund_receipt`;
DROP TABLE IF EXISTS `refund_fee`;
DROP TABLE IF EXISTS `refund_tax`;
DROP TABLE IF EXISTS `refund_payment`;
DROP TABLE IF EXISTS `ticket`;
DROP TABLE IF EXISTS `receipt`;
DROP TABLE IF EXISTS `fare`;
DROP TABLE IF EXISTS `tax`;
DROP TABLE IF EXISTS `discount`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `payment_type`;
DROP TABLE IF EXISTS `coupon`;
DROP TABLE IF EXISTS `extra_service`;
DROP TABLE IF EXISTS `extra_service_type`;
DROP TABLE IF EXISTS `affilate_product`;
DROP TABLE IF EXISTS `affiliate_company`;
DROP TABLE IF EXISTS `accessory`;
DROP TABLE IF EXISTS `combined_product`;
DROP TABLE IF EXISTS `flight_path_relation`;
DROP TABLE IF EXISTS `airway`;
DROP TABLE IF EXISTS `departure_arrival`;
DROP TABLE IF EXISTS `ATC_Advisory`;
DROP TABLE IF EXISTS `favorite`;
DROP TABLE IF EXISTS `flight_plan`;
DROP TABLE IF EXISTS `flight_path`;
DROP TABLE IF EXISTS `airport`;
DROP TABLE IF EXISTS `crew_organization`;
DROP TABLE IF EXISTS `crew_flight_plan`;
DROP TABLE IF EXISTS `crew`;
DROP TABLE IF EXISTS `crew_type`;
DROP TABLE IF EXISTS `seat`;
DROP TABLE IF EXISTS `seat_type`;
DROP TABLE IF EXISTS `airline_aircraft_relation`;
DROP TABLE IF EXISTS `aircraft`;
DROP TABLE IF EXISTS `corporation_benefit`;
DROP TABLE IF EXISTS `corporation_customer_employee`;
DROP TABLE IF EXISTS `corporation_customer`;
DROP TABLE IF EXISTS `simple_customer`;
DROP TABLE IF EXISTS `regular_personal_customer`;
DROP TABLE IF EXISTS `personal_customer`;
DROP TABLE IF EXISTS `customer_id_info`;
DROP TABLE IF EXISTS `extra_service_reserved`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `airline`;
DROP TABLE IF EXISTS `SSR`;
DROP TABLE IF EXISTS `SEG`;
DROP TABLE IF EXISTS `PAX`;
DROP TABLE IF EXISTS `PNR`;
DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `SSR_type`;


-- Active: 1667279984414@@127.0.0.1@3306@flight_ticket_reservation_db
CREATE TABLE `SSR_type`
(
    `SSR_type_id`       INT          NOT NULL AUTO_INCREMENT,
    `SSR_type_value`    VARCHAR(64)  NOT NULL,
    `SSR_type_descript` VARCHAR(512) NOT NULL,
    PRIMARY KEY (`SSR_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `customer`
(
    `customer_id` int         NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(45) NOT NULL,
    `cellphone`   VARCHAR(13) NOT NULL,
    PRIMARY KEY (`customer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `PNR`
(
    `PNR_id`        int      NOT NULL AUTO_INCREMENT,
    `customer_id`   int      NOT NULL,
    `reserved_time` DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY (`PNR_id`),
    KEY `fk_PNR_cousomer` (`customer_id`),
    CONSTRAINT `fk_PNR_cousomer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `PAX`
(
    `PAX_id`       int         NOT NULL AUTO_INCREMENT,
    `PNR_id`       int         NOT NULL,
    `name`         VARCHAR(45) NOT NULL,
    `age`          int         NOT NULL,
    `phone_number` VARCHAR(13) NULL,
    KEY `fk_PAX_PNR` (`PNR_id`),
    CONSTRAINT `fk_PAX_PNR` FOREIGN KEY (`PNR_id`) REFERENCES `PNR` (`PNR_id`),
    CONSTRAINT `PAX_group_key` PRIMARY KEY (`PAX_id`, `PNR_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `SEG`
(
    `SEG_id` int NOT NULL AUTO_INCREMENT,
    `PNR_id` int NOT NULL,
    KEY `fk_SEG_PNR` (`PNR_id`),
    CONSTRAINT `fk_SEG_PNR` FOREIGN KEY (`PNR_id`) REFERENCES `PNR` (`PNR_id`),
    CONSTRAINT `SEG_group_key` PRIMARY KEY (`SEG_id`, `PNR_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `SSR`
(
    `SEG_id`      int NOT NULL,
    `PAX_id`      int NOT NULL,
    `SSR_type_id` int NOT NULL,
    KEY `fk_SSR_PAX` (`PAX_id`),
    KEY `fk_SSR_SEG` (`SEG_id`),
    KEY `fk_SSR_type` (`SSR_type_id`),
    CONSTRAINT `fk_SSR_PAX` FOREIGN KEY (`PAX_id`) REFERENCES `PAX` (`PAX_id`),
    CONSTRAINT `fk_SSR_SEG` FOREIGN KEY (`SEG_id`) REFERENCES `SEG` (`SEG_id`),
    CONSTRAINT `fk_SSR_type` FOREIGN KEY (`SSR_type_id`) REFERENCES `SSR_type` (`SSR_type_id`),
    CONSTRAINT `SSR_group_key` PRIMARY KEY (`SEG_id`, `PAX_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `airline`
(
    `airline_id`   int         NOT NULL AUTO_INCREMENT,
    `airline_name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`airline_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `product`
(
    `product_id`  int         NOT NULL AUTO_INCREMENT,
    `airline_id`  int         NOT NULL,
    `name`        VARCHAR(45) NOT NULL,
    `price`       DECIMAL     NOT NULL DEFAULT 999999999,
    `description` TEXT        NULL,
    PRIMARY KEY (`product_id`),
    KEY `fk_product_airline` (`airline_id`),
    CONSTRAINT `fk_product_airline` FOREIGN KEY (`airline_id`) REFERENCES `airline` (`airline_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `extra_service_reserved`
(
    `extra_service_id` int NOT NULL AUTO_INCREMENT,
    `SEG_id`           int NOT NULL,
    `PAX_id`           int NOT NULL,
    `product_id`       int NOT NULL,
    KEY `fk_extra_service_reserved_SEF` (`SEG_id`),
    KEY `fk_extra_service_reserved_PAX` (`PAX_id`),
    KEY `fk_extra_service_reserved_product` (`product_id`),
    CONSTRAINT `fk_extra_service_reserved_SEF` FOREIGN KEY (`SEG_id`) REFERENCES `SEG` (`SEG_id`),
    CONSTRAINT `fk_extra_service_reserved_PAX` FOREIGN KEY (`PAX_id`) REFERENCES `PAX` (`PAX_id`),
    CONSTRAINT `fk_extra_service_reserved_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
    CONSTRAINT `extra_service_reserved_group_key` PRIMARY KEY (
                                                               `extra_service_id`,
                                                               `SEG_id`,
                                                               `PAX_id`,
                                                               `product_id`
        )
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `customer_id_info`
(
    `customer_id`       int         NOT NULL,
    `passport`          VARCHAR(45) NOT NULL,
    `registration_card` VARCHAR(45) NOT NULL,
    KEY `fk_customer_id_info_coustomer` (`customer_id`),
    CONSTRAINT `fk_customer_id_info_coustomer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
    CONSTRAINT `extra_service_reserved_group_key` PRIMARY KEY (`customer_id`)
);

CREATE TABLE `personal_customer`
(
    `customer_id` int NOT NULL,
    KEY `fk_personal_customer_coustomer` (`customer_id`),
    CONSTRAINT `fk_personal_customer_coustomer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
    PRIMARY KEY (`customer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `regular_personal_customer`
(
    `customer_id` int          NOT NULL,
    `id`          VARCHAR(32)  NOT NULL,
    `password`    VARCHAR(128) NOT NULL,
    KEY `fk_regular_personal_customer_personal_customer` (`customer_id`),
    CONSTRAINT `fk_regular_personal_customer_personal_customer` FOREIGN KEY (`customer_id`) REFERENCES `personal_customer` (`customer_id`),
    PRIMARY KEY (`customer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `simple_customer`
(
    `customer_id` int NOT NULL,
    KEY `fk_simple_customer_personal_customer` (`customer_id`),
    CONSTRAINT `fk_simple_customer_personal_customer` FOREIGN KEY (`customer_id`) REFERENCES `personal_customer` (`customer_id`),
    PRIMARY KEY (`customer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `corporation_customer`
(
    `customer_id`                  int         NOT NULL,
    `CEO`                          VARCHAR(45) NOT NULL,
    `business_registration_number` BIGINT      NOT NULL,
    `corporation_code`             VARCHAR(45) NOT NULL,
    KEY `fk_corporation_customer_coustomer` (`customer_id`),
    CONSTRAINT `fk_corporation_customer_coustomer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
    PRIMARY KEY (`customer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `corporation_customer_employee`
(
    `customer_id`  int NOT NULL,
    `customer_id2` int NOT NULL,
    KEY `fk_corporation_customer_employee_personal_customer` (`customer_id`),
    KEY `fk_corporation_customer_employee_corporation_customer` (`customer_id2`),
    CONSTRAINT `fk_corporation_customer_employee_personal_customer` FOREIGN KEY (`customer_id`) REFERENCES `personal_customer` (`customer_id`),
    CONSTRAINT `fk_corporation_customer_employee_corporation_customer` FOREIGN KEY (`customer_id2`) REFERENCES `corporation_customer` (`customer_id`),
    CONSTRAINT `corporation_customer_employee_group_key` PRIMARY KEY (`customer_id`, `customer_id2`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `corporation_benefit`
(
    `benefit_id`               int         NOT NULL AUTO_INCREMENT,
    `customer_id`              int         NOT NULL,
    `coporation_benefit_value` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`benefit_id`),
    KEY `fk_corporation_benefit_corporation_customer` (`customer_id`),
    CONSTRAINT `fk_corporation_benefit_corporation_customer` FOREIGN KEY (`customer_id`) REFERENCES `corporation_customer` (`customer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `aircraft`
(
    `aircraft_id`      int         NOT NULL AUTO_INCREMENT,
    `aircraft_code`    varchar(16) NOT NULL,
    `aircraft_model`   varchar(16) NOT NULL,
    `aircraft_payload` varchar(64) NOT NULL,
    PRIMARY KEY (`aircraft_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `airline_aircraft_relation`
(
    `aircraft_id` int NOT NULL,
    `airline_id`  int NOT NULL,
    KEY `fk_airline_aircraft_relation_airline` (`airline_id`),
    KEY `fk_airline_aircraft_relation_aircraft` (`aircraft_id`),
    CONSTRAINT `fk_airline_aircraft_relation_airline` FOREIGN KEY (`airline_id`) REFERENCES `airline` (`airline_id`),
    CONSTRAINT `fk_airline_aircraft_relation_aircraft` FOREIGN KEY (`aircraft_id`) REFERENCES `aircraft` (`aircraft_id`),
    CONSTRAINT `airline_aircraft_relation_group_key` PRIMARY KEY (`aircraft_id`, `airline_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `seat_type`
(
    `seat_type_id`   int         NOT NULL AUTO_INCREMENT,
    `seat_type_name` VARCHAR(32) NOT NULL,
    PRIMARY KEY (`seat_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `seat`
(
    `seat_id`      int         NOT NULL AUTO_INCREMENT,
    `seat_type_id` int         NOT NULL,
    `aircraft_id`  int         NOT NULL,
    `seat_name`    VARCHAR(10) NOT NULL,
    PRIMARY KEY (`seat_id`),
    KEY `fk_seat_seat_type` (`seat_type_id`),
    KEY `fk_seat_aircraft` (`aircraft_id`),
    CONSTRAINT `fk_seat_seat_type` FOREIGN KEY (`seat_type_id`) REFERENCES `seat_type` (`seat_type_id`),
    CONSTRAINT `fk_seat_aircraft` FOREIGN KEY (`aircraft_id`) REFERENCES `aircraft` (`aircraft_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `crew_type`
(
    `crew_type_id`   int         NOT NULL AUTO_INCREMENT,
    `crew_type_name` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`crew_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `crew`
(
    `crew_id`      int         NOT NULL AUTO_INCREMENT,
    `crew_name`    VARCHAR(45) NOT NULL,
    `crew_type_id` int         NOT NULL,
    PRIMARY KEY (`crew_id`),
    KEY `fk_crew_crew_type` (`crew_type_id`),
    CONSTRAINT `fk_crew_crew_type` FOREIGN KEY (`crew_type_id`) REFERENCES `crew_type` (`crew_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `crew_flight_plan`
(
    `crew_flight_plan_id`    int          NOT NULL AUTO_INCREMENT,
    `crew_flight_plan_value` VARCHAR(255) NULL,
    PRIMARY KEY (`crew_flight_plan_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `crew_organization`
(
    `cavin_crew_id`       int NOT NULL,
    `crew_id`             int NOT NULL,
    `crew_flight_plan_id` int NOT NULL,
    KEY `fk_crew_organization_crew` (`crew_id`),
    KEY `fk_crew_organization_crew_flight_plan` (`crew_flight_plan_id`),
    CONSTRAINT `fk_crew_organization_crew` FOREIGN KEY (`crew_id`) REFERENCES `crew` (`crew_id`),
    CONSTRAINT `fk_crew_organization_crew_flight_plan` FOREIGN KEY (`crew_flight_plan_id`) REFERENCES `crew_flight_plan` (`crew_flight_plan_id`),
    CONSTRAINT `crew_organization_group_key` PRIMARY KEY (`cavin_crew_id`, `crew_id`, `crew_flight_plan_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `airport`
(
    `airport_id` int         NOT NULL AUTO_INCREMENT,
    `IATA_code`  VARCHAR(3)  NOT NULL,
    `ICAO_code`  VARCHAR(4)  NOT NULL,
    `name`       VARCHAR(45) NOT NULL,
    PRIMARY KEY (`airport_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `flight_path`
(
    `flight_path_id`       int NOT NULL AUTO_INCREMENT,
    `departure_airport_id` int NOT NULL,
    `arrival_airport_id`   int NOT NULL,
    PRIMARY KEY (`flight_path_id`),
    KEY `fk_flight_path_start_airport` (`departure_airport_id`),
    KEY `fk_flight_path_end_airport` (`arrival_airport_id`),
    CONSTRAINT `fk_flight_path_start_airport` FOREIGN KEY (`departure_airport_id`) REFERENCES `airport` (`airport_id`),
    CONSTRAINT `fk_flight_path_end_airport` FOREIGN KEY (`arrival_airport_id`) REFERENCES `airport` (`airport_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `flight_plan`
(
    `flight_plan_id`      int         NOT NULL AUTO_INCREMENT,
    `aircraft_id`         int         NOT NULL,
    `flight_path_id`      int         NOT NULL,
    `departure_time`      DATETIME    NULL,
    `arrival_time`        DATETIME    NULL,
    `season_code`         VARCHAR(45) NOT NULL,
    `airline_id`          int         NOT NULL,
    `crew_flight_plan_id` int         NOT NULL,
    PRIMARY KEY (`flight_plan_id`),
    KEY `fk_flight_plan_flight_path` (`flight_path_id`),
    KEY `fk_flight_plan_airline` (`airline_id`),
    KEY `fk_flight_plan_crew_flight_plan` (`crew_flight_plan_id`),
    KEY `fk_flight_plan_aircraft` (`aircraft_id`),
    CONSTRAINT `fk_flight_plan_flight_path` FOREIGN KEY (`flight_path_id`) REFERENCES `flight_path` (`flight_path_id`),
    CONSTRAINT `fk_flight_plan_airline` FOREIGN KEY (`airline_id`) REFERENCES `airline` (`airline_id`),
    CONSTRAINT `fk_flight_plan_crew_flight_plan` FOREIGN KEY (`crew_flight_plan_id`) REFERENCES `crew_flight_plan` (`crew_flight_plan_id`),
    CONSTRAINT `fk_flight_plan_aircraft` FOREIGN KEY (`aircraft_id`) REFERENCES `aircraft` (`aircraft_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `favorite`
(
    `customer_id`    int NOT NULL,
    `flight_path_id` int NOT NULL,
    KEY `fk_favorite_customer` (`customer_id`),
    KEY `fk_favorite_flight_path` (`flight_path_id`),
    CONSTRAINT `fk_favorite_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
    CONSTRAINT `fk_favorite_flight_path` FOREIGN KEY (`flight_path_id`) REFERENCES `flight_path` (`flight_path_id`),
    CONSTRAINT `favorite_group_key` PRIMARY KEY (`customer_id`, `flight_path_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `ATC_Advisory`
(
    `ATC_record_id` int         NOT NULL AUTO_INCREMENT,
    `time`          DATETIME    NOT NULL,
    `weather`       VARCHAR(45) NOT NULL,
    `wind_speed`    DECIMAL     NOT NULL,
    `sight`         DECIMAL     NOT NULL,
    `side_wind`     DECIMAL     NOT NULL,
    `delay_report`  VARCHAR(45) NULL,
    `airport_id`    int         NOT NULL,
    PRIMARY KEY (`ATC_record_id`),
    KEY `fk_ATC_Advisory_airport` (`airport_id`),
    CONSTRAINT `fk_ATC_Advisory_airport` FOREIGN KEY (`airport_id`) REFERENCES `airport` (`airport_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `departure_arrival`
(
    `departure_arrival_id` int NOT NULL AUTO_INCREMENT,
    `flight_path_id`       int NOT NULL,
    `Mileage`              int NULL,
    PRIMARY KEY (`departure_arrival_id`),
    KEY `fk_departure_arrival_flight_path` (`flight_path_id`),
    CONSTRAINT `fk_departure_arrival_flight_path` FOREIGN KEY (`flight_path_id`) REFERENCES `flight_path` (`flight_path_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `airway`
(
    `airway_id`   int         NOT NULL AUTO_INCREMENT,
    `airway_name` VARCHAR(45) NULL,
    PRIMARY KEY (`airway_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `flight_path_relation`
(
    `flight_path_id` int NOT NULL,
    `airway_id`      int NOT NULL,
    KEY `fk_flight_path_relation_flight_path` (`flight_path_id`),
    KEY `fk_flight_path_relation_airway` (`airway_id`),
    CONSTRAINT `fk_flight_path_relation_flight_path` FOREIGN KEY (`flight_path_id`) REFERENCES `flight_path` (`flight_path_id`),
    CONSTRAINT `fk_flight_path_relation_airway` FOREIGN KEY (`airway_id`) REFERENCES `airway` (`airway_id`),
    CONSTRAINT `flight_path_relation_group_key` PRIMARY KEY (`flight_path_id`, `airway_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `combined_product`
(
    `combined_product_id` int NOT NULL,
    PRIMARY KEY (`combined_product_id`),
    KEY `fk_combined_product` (`combined_product_id`),
    CONSTRAINT `fk_combined_product_combined_product` FOREIGN KEY (`combined_product_id`) REFERENCES `product` (`product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `accessory`
(
    `accessory_id` int NOT NULL,
    PRIMARY KEY (`accessory_id`),
    KEY `fk_accessory_product` (`accessory_id`),
    CONSTRAINT `fk_accessory_product` FOREIGN KEY (`accessory_id`) REFERENCES `product` (`product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `affiliate_company`
(
    `affiliate_company_id` int         NOT NULL AUTO_INCREMENT,
    `company_name`         VARCHAR(64) NOT NULL,
    `company_tell_number`  VARCHAR(32) NOT NULL,
    `company_expire_date`  DATETIME    NOT NULL,
    PRIMARY KEY (`affiliate_company_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `affilate_product`
(
    `affilate_product_id`  int      NOT NULL,
    `affiliate_company_id` int      NOT NULL,
    `expire_date`          DATETIME NOT NULL,
    PRIMARY KEY (`affilate_product_id`),
    KEY `fk_affilate_product` (`affilate_product_id`),
    KEY `fk_affilate_product_company` (`affiliate_company_id`),
    CONSTRAINT `fk_affilate_product` FOREIGN KEY (`affilate_product_id`) REFERENCES `product` (`product_id`),
    CONSTRAINT `fk_affilate_product_company` FOREIGN KEY (`affiliate_company_id`) REFERENCES `affiliate_company` (`affiliate_company_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `extra_service_type`
(
    `extra_type_id`    int         NOT NULL AUTO_INCREMENT,
    `extra_type_value` varchar(45) NULL,
    PRIMARY KEY (`extra_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `extra_service`
(
    `extra_service_id` int NOT NULL,
    `extra_type_id`    int NOT NULL,
    PRIMARY KEY (`extra_service_id`),
    KEY `fk_extra_service_product` (`extra_service_id`),
    KEY `fk_extra_service_type` (`extra_type_id`),
    CONSTRAINT `fk_extra_service_product` FOREIGN KEY (`extra_service_id`) REFERENCES `product` (`product_id`),
    CONSTRAINT `fk_extra_service_type` FOREIGN KEY (`extra_type_id`) REFERENCES `extra_service_type` (`extra_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `coupon`
(
    `coupon_id`       int          NOT NULL AUTO_INCREMENT,
    `SEG_id`          int          NOT NULL,
    `coupon_name`     varchar(128) NOT NULL,
    `coupon_discount` int          NOT NULL,
    PRIMARY KEY (`coupon_id`),
    KEY `fk_coupon_SEG` (`SEG_id`),
    CONSTRAINT `fk_coupon_SEG` FOREIGN KEY (`SEG_id`) REFERENCES `SEG` (`SEG_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `payment_type`
(
    `payment_type_id`   int          NOT NULL AUTO_INCREMENT,
    `payment_type_name` VARCHAR(128) NOT NULL,
    PRIMARY KEY (`payment_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `payment`
(
    `payment_id`      int NOT NULL AUTO_INCREMENT,
    `payment_type_id` int NOT NULL,
    PRIMARY KEY (`payment_id`),
    KEY `fk_payment_state` (`payment_type_id`),
    CONSTRAINT `fk_payment_state` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_type` (`payment_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `discount`
(
    `discount_id`         int          NOT NULL AUTO_INCREMENT,
    `discount_conditions` VARCHAR(128) NOT NULL,
    `discount_rate`       int          NOT NULL DEFAULT 0,
    PRIMARY KEY (`discount_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `tax`
(
    `tax_id`     int     NOT NULL AUTO_INCREMENT,
    `tax`        DECIMAL NULL,
    `airport_id` int     NOT NULL,
    KEY `fk_tax_airport` (`airport_id`),
    CONSTRAINT `fk_tax_airport` FOREIGN KEY (`airport_id`) REFERENCES `airport` (`airport_id`),
    CONSTRAINT `tax_group_key` PRIMARY KEY (`tax_id`, `airport_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `fare`
(
    `fare_id`   int     NOT NULL AUTO_INCREMENT,
    `fare`      DECIMAL NULL,
    `airway_id` int     NOT NULL,
    KEY `fk_fare_airway` (`airway_id`),
    CONSTRAINT `fk_fare_airway` FOREIGN KEY (`airway_id`) REFERENCES `airway` (`airway_id`),
    CONSTRAINT `fare_group_key` PRIMARY KEY (`fare_id`, `airway_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `receipt`
(
    `receipt_id`   int NOT NULL AUTO_INCREMENT,
    `payment_id`   int NOT NULL,
    `discount_id`  int NOT NULL,
    `tax_id`       int NOT NULL,
    `tax_id2`      int NOT NULL,
    `fare_id`      int NOT NULL,
    `payment_time` DATETIME DEFAULT NOW(),
    PRIMARY KEY (`receipt_id`),
    KEY `fk_receipt_payment` (`payment_id`),
    KEY `fk_receipt_discount` (`discount_id`),
    KEY `fk_receipt_start_tax` (`tax_id`),
    KEY `fk_receipt_end_tax` (`tax_id2`),
    KEY `fk_receipt_fare` (`fare_id`),
    CONSTRAINT `fk_receipt_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`),
    CONSTRAINT `fk_receipt_discount` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`),
    CONSTRAINT `fk_receipt_start_tax` FOREIGN KEY (`tax_id`) REFERENCES `tax` (`tax_id`),
    CONSTRAINT `fk_receipt_end_tax` FOREIGN KEY (`tax_id2`) REFERENCES `tax` (`tax_id`),
    CONSTRAINT `fk_receipt_fare` FOREIGN KEY (`fare_id`) REFERENCES `fare` (`fare_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `ticket`
(
    `ticket_id`  int NOT NULL AUTO_INCREMENT,
    `PAX_id`     int NOT NULL,
    `product_id` int NOT NULL,
    `receipt_id` int NOT NULL,
    PRIMARY KEY (`ticket_id`),
    KEY `fk_ticket_PAX` (`PAX_id`),
    KEY `fk_ticket_product` (`product_id`),
    KEY `fk_ticket_receipt` (`receipt_id`),
    CONSTRAINT `fk_ticket_PAX` FOREIGN KEY (`PAX_id`) REFERENCES `PAX` (`PAX_id`),
    CONSTRAINT `fk_ticket_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
    CONSTRAINT `fk_ticket_receipt` FOREIGN KEY (`receipt_id`) REFERENCES `receipt` (`receipt_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `refund_payment`
(
    `refund_payment_id`      int NOT NULL AUTO_INCREMENT,
    `refund_payment_type_id` int NOT NULL,
    PRIMARY KEY (`refund_payment_id`),
    KEY `fk_refund_payment_type` (`refund_payment_type_id`),
    CONSTRAINT `fk_refund_payment_type` FOREIGN KEY (`refund_payment_type_id`) REFERENCES `payment_type` (`payment_type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `refund_tax`
(
    `refund_tax_id` int     NOT NULL AUTO_INCREMENT,
    `refund_fax`    DECIMAL NOT NULL,
    PRIMARY KEY (`refund_tax_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `refund_fee`
(
    `refund_fee_id`   int         NOT NULL AUTO_INCREMENT,
    `refund_fee`      DECIMAL     NOT NULL,
    `departure_point` varchar(50) NOT NULL,
    PRIMARY KEY (`refund_fee_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `refund_receipt`
(
    `refund_receipt_id` int NOT NULL AUTO_INCREMENT,
    `refund_tax_id`     int NOT NULL,
    `refund_payment_id` int NOT NULL,
    `refund_fee_id`     int NOT NULL,
    PRIMARY KEY (`refund_receipt_id`),
    KEY `fk_refund_receipt_tax` (`refund_tax_id`),
    KEY `fk_refund_receipt_payment` (`refund_payment_id`),
    KEY `fk_refund_receipt_fee` (`refund_fee_id`),
    CONSTRAINT `fk_refund_receipt_tax` FOREIGN KEY (`refund_tax_id`) REFERENCES `refund_tax` (`refund_tax_id`),
    CONSTRAINT `fk_refund_receipt_payment` FOREIGN KEY (`refund_payment_id`) REFERENCES `refund_payment` (`refund_payment_id`),
    CONSTRAINT `fk_refund_receipt_fee` FOREIGN KEY (`refund_fee_id`) REFERENCES `refund_fee` (`refund_fee_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `refund`
(
    `refund_id`         int NOT NULL AUTO_INCREMENT,
    `ticket_id`         int NOT NULL,
    `refund_receipt_id` int NOT NULL,
    KEY `fk_refund_ticket` (`ticket_id`),
    KEY `fk_refund_receipt` (`refund_receipt_id`),
    CONSTRAINT `fk_refund_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`ticket_id`),
    CONSTRAINT `fk_refund_receipt` FOREIGN KEY (`refund_receipt_id`) REFERENCES `refund_receipt` (`refund_receipt_id`),
    CONSTRAINT `refund_group_key` PRIMARY KEY (`refund_id`, `ticket_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `airplane_seat`
(
    `airplane_seat_id` INT     NOT NULL AUTO_INCREMENT,
    `flight_plan_id`   INT     NOT NULL,
    `seat_id`          INT     NOT NULL,
    `sold`             BOOLEAN NOT NULL DEFAULT FALSE,
    KEY `fk_airplane_flight_plan` (`flight_plan_id`),
    KEY `fk_airplane_seat` (`seat_id`),
    CONSTRAINT `fk_airplane_flight_plan` FOREIGN KEY (`flight_plan_id`) REFERENCES `flight_plan` (`flight_plan_id`),
    CONSTRAINT `fk_airplane_seat` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`),
    CONSTRAINT `airplane_seat_group_key` PRIMARY KEY (`airplane_seat_id`, `flight_plan_id`, `seat_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `airline_ticket`
(
    `airline_ticket_id` INT NOT NULL AUTO_INCREMENT,
    `ticket_product_id` INT NOT NULL,
    `ticket_seat_id`    INT NOT NULL,
    KEY `fk_airline_ticket_product` (`ticket_product_id`),
    KEY `fk_ticket_seat` (`ticket_seat_id`),
    CONSTRAINT `fk_ticket_seat` FOREIGN KEY (`ticket_seat_id`) REFERENCES `airplane_seat` (`airplane_seat_id`),
    CONSTRAINT `fk_airline_ticket_product` FOREIGN KEY (`ticket_product_id`) REFERENCES `product` (`product_id`),
    CONSTRAINT `airline_ticket_group_key` PRIMARY KEY (`airline_ticket_id`, `ticket_product_id`, `ticket_seat_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE combined_product_detail
(
    combined_product_id int NOT NULL,
    airline_ticket_id   int NULL DEFAULT NULL,
    extra_service_id    int NULL DEFAULT NULL,
    affilate_product_id int NULL DEFAULT NULL,
    accessory_id        int NULL DEFAULT NULL,
    KEY fk_combined_product_detail_product (combined_product_id),
    KEY fk_combined_product_detail_airline_ticket (airline_ticket_id),
    KEY fk_combined_product_detail_extra_service (extra_service_id),
    KEY fk_combined_product_detail_affilate_product (affilate_product_id),
    KEY fk_combined_product_detail_accessory (accessory_id),
    CONSTRAINT fk_combined_product_detail_product FOREIGN KEY (combined_product_id) REFERENCES combined_product (combined_product_id),
    CONSTRAINT fk_combined_product_detail_airline_ticket FOREIGN KEY (airline_ticket_id) REFERENCES airline_ticket (airline_ticket_id),
    CONSTRAINT fk_combined_product_detail_extra_service FOREIGN KEY (extra_service_id) REFERENCES extra_service (extra_service_id),
    CONSTRAINT fk_combined_product_detail_affilate_product FOREIGN KEY (affilate_product_id) REFERENCES affilate_product (affilate_product_id),
    CONSTRAINT fk_combined_product_detail_accessory FOREIGN KEY (accessory_id) REFERENCES accessory (accessory_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


#삭제
DROP PROCEDURE IF EXISTS init;
DROP PROCEDURE IF EXISTS insert_flight_constant;
DROP PROCEDURE IF EXISTS insert_customer_constant;
DROP PROCEDURE IF EXISTS insert_product_constant;
DROP PROCEDURE IF EXISTS insert_ticket_constant;
DROP PROCEDURE IF EXISTS insert_revation_constant;


DELIMITER //
CREATE PROCEDURE init()
BEGIN
    CALL `insert_flight_constant`();
    CALL insert_customer_constant();
    CALL insert_product_constant();
    CALL insert_ticket_constant();
    CALL insert_revation_constant();
end //


CREATE PROCEDURE insert_flight_constant()
BEGIN
    INSERT INTO airport (name, IATA_code, ICAO_code)
    VALUES ('인천국제공항', 'ICN', 'RKSI'),
           ('사이판국제공항', 'SPN', 'PGSN'),
           ('나리타국제공항', 'NRT', 'RJAA'),
           ('홍콩국제공항', 'HKG', 'VHHH');

    -- ATC Advisory
    INSERT INTO ATC_Advisory (ATC_record_id, time, weather, wind_speed, sight, side_wind, delay_report, airport_id)
    VALUES (1, '2024-04-13 09:10:15', 'foggy', 10, 10, 5, '안개로 인한 지연', 1);

    -- 운항경로
    INSERT INTO flight_path (departure_airport_id, arrival_airport_id)
    VALUES (1, 2), -- 인천 - 사이판    1 -4
           (1, 3), -- 인천 - 나리타    2 -7
           (1, 4), -- 인천 - 홍콩      3 -10
           (2, 1), -- 사이판 - 인천    4 -1
           (2, 3), -- 사이판 - 나리타  5 -8
           (2, 4), -- 사이판 - 홍콩    6 -11
           (3, 1), -- 나리타 - 인천    7 -2
           (3, 2), -- 나리타 - 사이판  8 - 5
           (3, 4), -- 나리타 - 홍콩    9 -12
           (4, 1), -- 홍콩 - 인천  10 -3
           (4, 2), -- 홍콩 - 사이판    11 -6
           (4, 3);
    -- 홍콩 - 나리타   12 -9

    -- 출발/도착
    INSERT INTO departure_arrival (flight_path_id, mileage)
    VALUES (1, 100),
           (2, 50),
           (3, 200),
           (4, 100),
           (5, 120),
           (6, 150),
           (7, 50),
           (8, 120),
           (9, 180),
           (10, 200),
           (11, 150),
           (12, 180);
    -- 경로
    INSERT INTO airway (airway_name)
    VALUES ('인천-사이판'),
           ('인천-나리타'),
           ('인천-홍콩'),
           ('사이판-인천'),
           ('사이판-나리타'),
           ('사이판-홍콩'),
           ('나리타-인천'),
           ('나리타-사이판'),
           ('나리타-홍콩'),
           ('홍콩-인천'),
           ('홍콩-사이판'),
           ('홍콩-나리타');

    -- 운항경로 관계
    INSERT INTO flight_path_relation (airway_id, flight_path_id)
    VALUES (1, 1),
           (2, 2),
           (3, 3),
           (4, 4),
           (5, 5),
           (6, 6),
           (7, 7),
           (8, 8),
           (9, 9),
           (10, 10),
           (11, 11),
           (12, 12);

    -- 항공기
    INSERT INTO aircraft (aircraft_code, aircraft_model, aircraft_payload)
    VALUES ('7C3406', 'Boeing 738', '68t'),
           ('6C1234', 'Boeing 747', '60t'),
           ('8D3424', 'Airbus A380', '120t'),
           ('5F0031', 'Airbus A380', '90t'),
           ('7G9912', 'Boeing 737-800', '80t');

    -- 좌석 등급
    INSERT INTO seat_type (seat_type_name)
    VALUES ('퍼스트 클래스'),
           ('비지니스 클래스'),
           ('이코노미 클래스');

    -- 좌석
    INSERT INTO seat (seat_name, seat_type_id, aircraft_id)
    VALUES ('AA1', 2, 1),
           ('AA2', 2, 1),
           ('AA3', 2, 1),
           ('AC1', 2, 1),
           ('AC2', 2, 1),
           ('AC3', 2, 1),
           ('AH1', 2, 1),
           ('AH2', 2, 1),
           ('AH3', 2, 1),
           ('AK1', 2, 1),
           ('AK2', 2, 1),
           ('AK3', 2, 1),
           ('AA15', 3, 1),
           ('AA16', 3, 1),
           ('AA17', 3, 1),
           ('AA18', 3, 1),
           ('AA19', 3, 1),
           ('AA20', 3, 1),
           ('AA21', 3, 1),
           ('AA22', 3, 1),
           ('AA23', 3, 1),
           ('AA24', 3, 1),
           ('AA25', 3, 1),
           ('AA26', 3, 1),
           ('AA27', 3, 1),
           ('AA28', 3, 1),
           ('AA29', 3, 1),
           ('AA30', 3, 1),
           ('AA31', 3, 1),
           ('AA32', 3, 1),
           ('AA33', 3, 1),
           ('AA34', 3, 1),
           ('AA35', 3, 1),
           ('AA36', 3, 1),
           ('AB15', 3, 1),
           ('AB16', 3, 1),
           ('AB17', 3, 1),
           ('AB18', 3, 1),
           ('AB19', 3, 1),
           ('AB20', 3, 1),
           ('AB21', 3, 1),
           ('AB22', 3, 1),
           ('AB23', 3, 1),
           ('AB24', 3, 1),
           ('AB25', 3, 1),
           ('AB26', 3, 1),
           ('AB27', 3, 1),
           ('AB28', 3, 1),
           ('AB29', 3, 1),
           ('AB30', 3, 1),
           ('AB31', 3, 1),
           ('AB32', 3, 1),
           ('AB33', 3, 1),
           ('AB34', 3, 1),
           ('AB35', 3, 1),
           ('AB36', 3, 1),
           ('AC15', 3, 1),
           ('AC16', 3, 1),
           ('AC17', 3, 1),
           ('AC18', 3, 1),
           ('AC19', 3, 1),
           ('AC20', 3, 1),
           ('AC21', 3, 1),

           ('BB2', 2, 2),
           ('BB2', 2, 2),
           ('BB3', 2, 2),
           ('BC2', 2, 2),
           ('BC2', 2, 2),
           ('BC3', 2, 2),
           ('BH2', 2, 2),
           ('BH2', 2, 2),
           ('BH3', 2, 2),
           ('BK2', 2, 2),
           ('BK2', 2, 2),
           ('BK3', 2, 2),
           ('BB25', 3, 2),
           ('BB26', 3, 2),
           ('BB27', 3, 2),
           ('BB28', 3, 2),
           ('BB29', 3, 2),
           ('BB20', 3, 2),
           ('BB22', 3, 2),
           ('BB22', 3, 2),
           ('BB23', 3, 2),
           ('BB24', 3, 2),
           ('BB25', 3, 2),
           ('BB26', 3, 2),
           ('BB27', 3, 2),
           ('BB28', 3, 2),
           ('BB29', 3, 2),
           ('BB30', 3, 2),
           ('BB32', 3, 2),
           ('BB32', 3, 2),
           ('BB33', 3, 2),
           ('BB34', 3, 2),
           ('BB35', 3, 2),
           ('BB36', 3, 2),
           ('BB25', 3, 2),
           ('BB26', 3, 2),
           ('BB27', 3, 2),
           ('BB28', 3, 2),
           ('BB29', 3, 2),
           ('BB20', 3, 2),
           ('BB22', 3, 2),
           ('BB22', 3, 2),
           ('BB23', 3, 2),
           ('BB24', 3, 2),
           ('BB25', 3, 2),
           ('BB26', 3, 2),
           ('BB27', 3, 2),
           ('BB28', 3, 2),
           ('BB29', 3, 2),
           ('BB30', 3, 2),
           ('BB32', 3, 2),
           ('BB32', 3, 2),
           ('BB33', 3, 2),
           ('BB34', 3, 2),
           ('BB35', 3, 2),
           ('BB36', 3, 2),
           ('BC25', 3, 2),
           ('BC26', 3, 2),
           ('BC27', 3, 2),
           ('BC28', 3, 2),
           ('BC29', 3, 2),
           ('BC20', 3, 2),
           ('BC22', 3, 2),
           ('CC3', 2, 3),
           ('CC2', 2, 3),
           ('CC3', 2, 3),
           ('CC3', 2, 3),
           ('CC2', 2, 3),
           ('CC3', 2, 3),
           ('CH3', 2, 3),
           ('CH2', 2, 3),
           ('CH3', 2, 3),
           ('CK3', 2, 3),
           ('CK2', 2, 3),
           ('CK3', 2, 3),
           ('CC35', 3, 3),
           ('CC36', 3, 3),
           ('CC37', 3, 3),
           ('CC38', 3, 3),
           ('CC39', 3, 3),
           ('CC20', 3, 3),
           ('CC23', 3, 3),
           ('CC22', 3, 3),
           ('CC23', 3, 3),
           ('CC24', 3, 3),
           ('BC25', 3, 3),
           ('CC26', 3, 3),
           ('CC27', 3, 3),
           ('CC28', 3, 3),
           ('CC29', 3, 3),
           ('CC30', 3, 3),
           ('CC33', 3, 3),
           ('CC32', 3, 3),
           ('CC33', 3, 3),
           ('CC34', 3, 3),
           ('CC35', 3, 3),
           ('CC36', 3, 3),
           ('CB35', 3, 3),
           ('CB36', 3, 3),
           ('CB37', 3, 3),
           ('CB38', 3, 3),
           ('CB39', 3, 3),
           ('CB20', 3, 3),
           ('CB23', 3, 3),
           ('CB22', 3, 3),
           ('CB23', 3, 3),
           ('CB24', 3, 3),
           ('CB25', 3, 3),
           ('CB26', 3, 3),
           ('CB27', 3, 3),
           ('CB28', 3, 3),
           ('CB29', 3, 3),
           ('CB30', 3, 3),
           ('CB33', 3, 3),
           ('CB32', 3, 3),
           ('CB33', 3, 3),
           ('CB34', 3, 3),
           ('CB35', 3, 3),
           ('CB36', 3, 3),
           ('CC35', 3, 3),
           ('CC36', 3, 3),
           ('CC37', 3, 3),
           ('CC38', 3, 3),
           ('CC39', 3, 3),
           ('CC20', 3, 3),
           ('CC23', 3, 3),
           ('DD4', 2, 4),
           ('DD2', 2, 4),
           ('DD3', 2, 4),
           ('DC4', 2, 4),
           ('DC2', 2, 4),
           ('DC3', 2, 4),
           ('DH4', 2, 4),
           ('DH2', 2, 4),
           ('DH3', 2, 4),
           ('DK4', 2, 4),
           ('DK2', 2, 4),
           ('DK3', 2, 4),
           ('DD45', 3, 4),
           ('DD46', 3, 4),
           ('DD47', 3, 4),
           ('DD48', 3, 4),
           ('DD49', 3, 4),
           ('DD20', 3, 4),
           ('DD24', 3, 4),
           ('DD22', 3, 4),
           ('DD23', 3, 4),
           ('DD24', 3, 4),
           ('BD25', 3, 4),
           ('DD26', 3, 4),
           ('DD27', 3, 4),
           ('DD28', 3, 4),
           ('DD29', 3, 4),
           ('DD30', 3, 4),
           ('DD34', 3, 4),
           ('DD32', 3, 4),
           ('DD33', 3, 4),
           ('DD34', 3, 4),
           ('DD35', 3, 4),
           ('DD36', 3, 4),
           ('DB45', 3, 4),
           ('DB46', 3, 4),
           ('DB47', 3, 4),
           ('DB48', 3, 4),
           ('DB49', 3, 4),
           ('DB20', 3, 4),
           ('DB24', 3, 4),
           ('DB22', 3, 4),
           ('DB23', 3, 4),
           ('DB24', 3, 4),
           ('DB25', 3, 4),
           ('DB26', 3, 4),
           ('DB27', 3, 4),
           ('DB28', 3, 4),
           ('DB29', 3, 4),
           ('DB30', 3, 4),
           ('DB34', 3, 4),
           ('DB32', 3, 4),
           ('DB33', 3, 4),
           ('DB34', 3, 4),
           ('DB35', 3, 4),
           ('DB36', 3, 4),
           ('DC45', 3, 4),
           ('DC46', 3, 4),
           ('DC47', 3, 4),
           ('DC48', 3, 4),
           ('DC49', 3, 4),
           ('DC20', 3, 4),
           ('DC24', 3, 4),
           ('EE5', 2, 5),
           ('EE2', 2, 5),
           ('EE3', 2, 5),
           ('EC5', 2, 5),
           ('EC2', 2, 5),
           ('EC3', 2, 5),
           ('EH5', 2, 5),
           ('EH2', 2, 5),
           ('EH3', 2, 5),
           ('EK5', 2, 5),
           ('EK2', 2, 5),
           ('EK3', 2, 5),
           ('EE55', 3, 5),
           ('EE56', 3, 5),
           ('EE57', 3, 5),
           ('EE58', 3, 5),
           ('EE59', 3, 5),
           ('EE20', 3, 5),
           ('EE25', 3, 5),
           ('EE22', 3, 5),
           ('EE23', 3, 5),
           ('EE24', 3, 5),
           ('BE25', 3, 5),
           ('EE26', 3, 5),
           ('EE27', 3, 5),
           ('EE28', 3, 5),
           ('EE29', 3, 5),
           ('EE30', 3, 5),
           ('EE35', 3, 5),
           ('EE32', 3, 5),
           ('EE33', 3, 5),
           ('EE34', 3, 5),
           ('EE35', 3, 5),
           ('EE36', 3, 5),
           ('EB55', 3, 5),
           ('EB56', 3, 5),
           ('EB57', 3, 5),
           ('EB58', 3, 5),
           ('EB59', 3, 5),
           ('EB20', 3, 5),
           ('EB25', 3, 5),
           ('EB22', 3, 5),
           ('EB23', 3, 5),
           ('EB24', 3, 5),
           ('EB25', 3, 5),
           ('EB26', 3, 5),
           ('EB27', 3, 5),
           ('EB28', 3, 5),
           ('EB29', 3, 5),
           ('EB30', 3, 5),
           ('EB35', 3, 5),
           ('EB32', 3, 5),
           ('EB33', 3, 5),
           ('EB34', 3, 5),
           ('EB35', 3, 5),
           ('EB36', 3, 5),
           ('EC55', 3, 5),
           ('EC56', 3, 5),
           ('EC57', 3, 5),
           ('EC58', 3, 5),
           ('EC59', 3, 5),
           ('EC20', 3, 5),
           ('EC25', 3, 5);

    -- 승무원 비행계획
    INSERT INTO crew_flight_plan (crew_flight_plan_id, crew_flight_plan_value)
    VALUES (1, '제주항공, 7C3406, Boeing 738, 인천-사이판, 출발: 2024-04-13 22:10, 도착: 2024-04-14 03:40'),
           (2, '제주항공, 7C3406, Boeing 738, 사이판-인천, 출발: 2024-04-15 04:45, 도착: 2024-04-15 08:35');

    -- 승무원 타입
    INSERT INTO crew_type (crew_type_id, crew_type_name)
    VALUES (1, '기장'),
           (2, '부기장'),
           (3, '사무'),
           (4, '객실 승무원');

    -- 승무원
    INSERT INTO crew (crew_id, crew_name, crew_type_id)
    VALUES (1, '김주원', 1),
           (2, '박남준', 2),
           (3, '박주희', 3),
           (4, '박세인', 4),
           (5, '송이현', 4),
           (6, '김세림', 4);

    -- 승무원 편조
    INSERT INTO crew_organization (cavin_crew_id, crew_id, crew_flight_plan_id)
    VALUES (1, 1, 1),
           (1, 2, 1),
           (1, 3, 1),
           (1, 4, 1),
           (1, 5, 1),
           (1, 6, 1);

    -- 항공사
    INSERT INTO airline (airline_id, airline_name)
    VALUES (1, '제주 항공');

    -- 항공기 항공사 관계
    INSERT INTO airline_aircraft_relation (aircraft_id, airline_id)
    VALUES (1, 1);

    -- 운항계획
    INSERT INTO flight_plan(departure_time, arrival_time, season_code, flight_path_id, airline_id,
                            aircraft_id, crew_flight_plan_id)
    VALUES ('2024-04-13 22:10', '2024-04-14 03:40', 'off-season', 1, 1, 1, 1),
           ('2024-04-15 04:45', '2024-04-15 08:35', 'off-season', 2, 1, 2, 1),
           ('2024-04-15 13:25', '2024-04-15 14:33', 'off-season', 3, 1, 3, 1),
           ('2024-04-15 14:45', '2024-04-15 16:35', 'off-season', 4, 1, 4, 1),
           ('2024-04-16 06:45', '2024-04-16 08:35', 'off-season', 5, 1, 5, 1),
           ('2024-04-16 08:45', '2024-04-16 10:35', 'off-season', 6, 1, 1, 1),
           ('2024-04-16 13:11', '2024-04-16 14:03', 'off-season', 7, 1, 2, 1),
           ('2024-04-16 15:32', '2024-04-16 16:20', 'off-season', 8, 1, 3, 1),
           ('2024-04-17 10:45', '2024-04-17 11:35', 'off-season', 9, 1, 4, 1),
           ('2024-04-18 09:45', '2024-04-18 10:35', 'off-season', 10, 1, 5, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 11, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 1, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 2, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 3, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 4, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 5, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 6, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 7, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 8, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 9, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 10, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 11, 1, 1, 1),
           ('2024-04-19 11:25', '2024-04-19 14:35', 'off-season', 12, 1, 1, 1);


    INSERT INTO airplane_seat (flight_plan_id, seat_id)
    VALUES (1, 1),
           (1, 2),
           (1, 3),
           (1, 4),
           (1, 5),
           (1, 6),
           (1, 7),
           (1, 8),
           (1, 9),
           (2, 65),
           (2, 66),
           (2, 67),
           (2, 68),
           (2, 69),
           (2, 70),
           (2, 71),
           (2, 72),
           (2, 73),
           (3, 129),
           (3, 130),
           (3, 131),
           (3, 132),
           (3, 133),
           (3, 134),
           (3, 135),
           (3, 136),
           (3, 137),
           (4, 193),
           (4, 194),
           (4, 195),
           (4, 196),
           (4, 197),
           (4, 198),
           (4, 199),
           (4, 200),
           (4, 201),
           (5, 257),
           (5, 258),
           (5, 259),
           (5, 260),
           (5, 261),
           (5, 262),
           (5, 263),
           (5, 264),
           (5, 265),
           (12, 1),
           (12, 2),
           (12, 3),
           (12, 4),
           (12, 5),
           (12, 6),
           (12, 7),
           (12, 8),
           (12, 9),
           (13, 65),
           (13, 66),
           (13, 67),
           (13, 68),
           (13, 69),
           (13, 70),
           (13, 71),
           (13, 72),
           (13, 73),
           (14, 129),
           (14, 130),
           (14, 131),
           (14, 132),
           (14, 133),
           (14, 134),
           (14, 135),
           (14, 136),
           (14, 137),
           (15, 193),
           (15, 194),
           (15, 195),
           (15, 196),
           (15, 197),
           (15, 198),
           (15, 199),
           (15, 200),
           (15, 201),
           (16, 257),
           (16, 258),
           (16, 259),
           (16, 260),
           (16, 261),
           (16, 262),
           (16, 263),
           (16, 264),
           (16, 265);
end //


#고객 상수 정의
CREATE PROCEDURE insert_customer_constant()
BEGIN
    INSERT INTO customer (name, cellphone)
    VALUES ('이세희', '010-3746-6071'),
           ('강인준', '010-1111-1111'),
           ('장기환', '010-2222-3333'),
           ('한태환', '010-3333-4444'),
           ('이경헌', '010-4444-5555');
    INSERT INTO corporation_customer (customer_id, CEO, business_registration_number, corporation_code)
    VALUES (2, '김사장', 1234111, 'abaa013');
    INSERT INTO personal_customer (customer_id) VALUES (1), (3), (4), (5);
    INSERT INTO corporation_customer_employee (customer_id, customer_id2) VALUES (1, 2);
end //



#예약 상수 정의
CREATE PROCEDURE insert_revation_constant()
BEGIN
    #특수 주문 요청 타입
    INSERT INTO SSR_type (SSR_type_value, SSR_type_descript)
    VALUES ('/ADTK', '발권된 경우 통지(PNR은 담당 사무소의 대기열 1, 카테고리 6 에 배치됨)'),
           ('*AVIH', '보관 중인 동물(숫자, 유형, 혈통, 용기 중량 및 치수 명시)'),
           ('AVML', '채식 힌두교 식사'),
           ('BBML', '이유식'),
           ('BIKE', '보관 중인 자전거, 번호 지정(아래 참조)'),
           ('BLML', '부드러운 식사'),
           ('BLND', '맹인 승객(안내견 동반 여부 명시)'),
           ('BSCT', '요람/캐리콧/아기 바구니'),
           ('BULK', '부피가 큰 수하물(개수, 무게 및 치수 명시)'),
           ('CBBG', '좌석이 필요한 기내 수하물(숫자, 무게 명시)'),
           ('CHLD', '어린이'),
           ('CHML', '어린이 식사'),
           ('CKIN', '공항 직원을 위한 정보'),
           ('CLID', '모든 GDS에서 사용하는 기업 클라이언트 코드'),
           ('COUR', '상업용 택배(무게 및 택배사 명시)'),
           ('CRUZ', '크루즈 승객'),
           ('DBML', '당뇨식'),
           ('DEAF', '청각 장애인 승객(안내견 동반 시 명시)'),
           ('DEPA', '에스코트를 동반한 추방자'),
           ('DEPU', '동반자 없는 추방자'),
           ('DOCA', 'APIS(Advanced Passenger Information System) 주소 세부 정보'),
           ('DOCO', '사전 승객 정보 시스템(APIS) 비자'),
           ('DOCS', 'APIS(Advanced Passenger Information System) 여권 또는 신분증'),
           ('DPNA', '도움이 필요한 지적 또는 발달 장애가 있는 장애인 승객'),
           ('EPAY', '항공권이 없는 항공사를 위한 전자 결제'),
           ('ESAN', '정서적 지원/정신적 지원이 있는 승객 또는 기내 동물'),
           ('EXST', '추가 좌석'),
           ('FOID', '신분증의 형태'),
           ('FPML', '과일 플래터 식사'),
           ('FQTR', '상용 고객 마일리지 프로그램 사용'),
           ('FQTS', '상용고객 서비스 요청'),
           ('FQTU', '상용 고객 우대 업그레이드 및 적립'),
           ('FQTV', '상용 고객 마일리지 프로그램 적립'),
           ('FRAG', '깨지기 쉬운 수하물(개수, 무게 및 치수 명시)'),
           ('FRAV', '먼저 사용 가능'),
           ('GFML', '글루텐 불내성 식사'),
           ('GPST', '그룹 좌석 요청(숫자 및 선호 위치 지정)'),
           ('GRPF', '단체 운임(운임 코드 명시)'),
           ('GRPS', '공통 신분을 사용하여 함께 여행하는 승객'),
           ('HNML', '힌두교 식사'),
           ('INFT', '유아(이름 및 생년월일 명시)'),
           ('JPML', '일식( LH 특정 - 항공사 협회 필요)'),
           ('KSML', '코셔 식사'),
           ('LANG', '구사 가능한 언어'),
           ('LCML', '저칼로리 식사'),
           ('LFML', '저지방 식사'),
           ('LSML', '저염식'),
           ('MAAS', '마중 및 지원(노인, 장애인 승객 또는 임산부와 같은 세부 정보 명시)'),
           ('MEDA', '의료용 케이스(특별한 주의가 필요한 장애인 승객에게 사용 가능: IATA MEDA 절차에 따름)'),
           ('MOML', '무슬림 식사'),
           ('NAME', '이름(항공사가 다른 이름으로 예약한 경우)'),
           ('NFML', '어분 없음( LH 특정 - 항공사 협회 필요)'),
           ('NLML', '저유당 식사'),
           ('NSSA', '금연 통로 좌석'),
           ('NSSB', '금연 격벽 좌석'),
           ('NSST', '금연석(좌석 번호가 포함될 수 있음)'),
           ('NSSW', '금연 창가 좌석'),
           ('OTHS', '다른 SSR 코드로 지정되지 않은 기타 서비스'),
           ('PCTC', '승객의 비상 연락처 정보'),
           ('PETC', '기내 동물(숫자, 유형, 혈통, 용기 중량 및 크기 명시)'),
           ('PICA', '구금된 승객, 동반'),
           ('PICU', '동반자가 없는 구금 승객'),
           ('RQST', '좌석 요청(좌석 번호 또는 선호 사항 포함)'),
           ('RVML', '원시 채식 식사'),
           ('SEAT', '탑승권이 발급되었거나 발급 예정인 사전 지정석'),
           ('SEMN', '선원 - 선원'),
           ('SFML', '해산물 식사'),
           ('SLPR', '캐빈 내 침대/침대'),
           ('SMSA', '흡연 통로 좌석'),
           ('SMSB', '흡연 격벽 좌석'),
           ('SMST', '흡연석(특정 좌석 번호가 포함될 수 있음)'),
           ('SMSW', '흡연 창가 좌석'),
           ('SPEQ', '스포츠 장비'),
           ('SPML', '특별식(식품 품목 명시)'),
           ('STCR', '들것 승객'),
           ('SVAN', '기내 서비스 동물 동반 승객( LH 전용)'),
           ('/TKNA', 'FA 항목의 항공권 번호(항공사로 자동 발송)'),
           ('/TKNC', '티켓 번호 전송'),
           ('/TKNE', 'FA 요소의 전자 항공권 번호'),
           ('/TKNM', 'FH 항목의 항공권 번호'),
           ('TKTL', '발권 시간 제한(자동 생성)'),
           ('TWOV', '비자 없이 환승 또는 환승'),
           ('UMNR', '비동반 미성년자(연령 명시)'),
           ('VGML', '채식 비건 식사'),
           ('VJML', '채식 자이나교 식사'),
           ('VLML', '채식 락토 오보 식사'),
           ('VOML', '채식 동양식 식사'),
           ('WCBD', '휠체어 - 건전지'),
           ('WCBW', '휠체어 - 습전지'),
           ('WCHC', '휠체어 - 좌석까지'),
           ('WCHR', '휠체어 - 램프용'),
           ('WCHS', '휠체어 - 위아래 단계'),
           ('WCMP', '휠체어 - 수동 전원(미국 통신사만 해당)'),
           ('WCOB', '휠체어 - 기내'),
           ('XBAG', '초과 수하물(개수, 중량 및 치수 명시)'),
           ('WEAP', '위탁 수하물로 운송되는 무기, 화기 또는 탄약');


end //


#상품 상수정의
DELIMITER //
CREATE PROCEDURE insert_product_constant()
BEGIN
    # product id 1
    insert into product (name, price, description, airline_id)
    values ('여름 제주 이벤트권', '100000', '제주도 여름 특가 항공권', 1);
#항공사 티켓 상품 넣기
    insert into airline_ticket (ticket_product_id, ticket_seat_id) value (1, 1);


# product id 2
    insert into product (name, price, description, airline_id)
    values ('제주 감귤 볼펜', '5000', '제주도 스페셜 에디션', 1);
#액세서리 상품 넣기
    insert into accessory
    values (2);


# product id 3
    insert into product (name, price, description, airline_id)
    values ('그랜드 하얏트 제주', '40000', '제주도 스페셜 에디션', 1);
#제휴사 넣기
    insert into affiliate_company (company_name, company_tell_number, company_expire_date)
    values ('world of hyatt', '064-907-1234', '2024-07-01');
# 제휴상품 넣기
    insert into affilate_product (affilate_product_id, affiliate_company_id, expire_date)
    values (3, 1, '2024-07-01');


# product id 4
    insert into product (airline_id, name, price, description)
    VALUES (1, '육개장 소컵', '1000', '육개장 라면 소컵');
    #5
    insert into product (airline_id, name, price, description)
    VALUES (1, '위탁 수하물 + 30KG', '20000', '위탁수하물 추가 30kg');
    #6
    insert into product (airline_id, name, price, description)
    VALUES (1, '보험', '0', '여행자 보험');
    #7
    insert into product (airline_id, name, price, description)
    VALUES (1, '전 좌석 지정', '0', '모든 좌석에서 선택가능');
    #8
    insert into product (airline_id, name, price, description)
    VALUES (1, '우선 탑승', '0', '우선 탑승 가능');
    #9
    insert into product (airline_id, name, price, description)
    VALUES (1, '공항 수속 무료', '0', '공항 수속 무료');
    #10
    insert into product (airline_id, name, price, description)
    VALUES (1, '전용 수속 카운터', '0', '전용 수속 카운터 제공');
    #11
    insert into product (airline_id, name, price, description)
    VALUES (1, '위탁 수하물 + 20KG', '15000', '위탁 수하물 + 20kg');
    #12
    insert into product (airline_id, name, price, description)
    VALUES (1, '일반 좌석 지정', '0', '(세미 프론트/앞/비상구열 제외)');
    #13
    insert into product (airline_id, name, price, description)
    VALUES (1, '빠른 짐 찾기', '15000', '빠른 짐 찾기');
    #14
    insert into product (airline_id, name, price, description)
    VALUES (1, '우선 탑승', '0', '우선 탑승 가능');


# 부가서비스 종류 넣기
    insert into extra_service_type (extra_type_value)
    VALUES ('보험'),
           ('음식'),
           ('좌석'),
           ('수하물'),
           ('서비스');

# 부가서비스 상품 넣기
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (4, 2);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (5, 5);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (6, 1);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (7, 5);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (8, 5);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (9, 5);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (10, 5);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (11, 5);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (12, 5);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (13, 5);
    insert into extra_service (extra_service_id, extra_type_id)
    VALUES (14, 5);


# product id 15
    insert into product (airline_id, name, price, description)
    VALUES (1, '제주도 여름 여행 패키지', '400000', '특가 제주도 여행 패키지 숙박 + 부가서비스 포함');
# product id 16
    insert into product (airline_id, name, price, description)
    VALUES (1, '프리미엄 플러스', '22300', '위탁 수하물 및 6개 서비스 포함');
# product id 17
    insert into product (airline_id, name, price, description)
    VALUES (1, '플러스', '11000', '위탁 수하물 및 4개 서비스 포함');

# 결합 상품 넣기
    insert into combined_product (combined_product_id) values (15);
    insert into combined_product (combined_product_id) values (16);
    insert into combined_product (combined_product_id) values (17);
# 결합 상품 상세 정보 넣기
    insert into combined_product_detail (combined_product_id, airline_ticket_id, extra_service_id, affilate_product_id,
                                         accessory_id)
    VALUES (15, 1, 4, 3, 2);

    insert into combined_product_detail (combined_product_id, extra_service_id)
    VALUES (16, 5),
           (16, 7),
           (16, 8),
           (16, 9),
           (16, 10);

    insert into combined_product_detail (combined_product_id, extra_service_id)
    VALUES (17, 11),
           (17, 12),
           (17, 13),
           (17, 14);

    INSERT INTO product (airline_id, name, price, description)
    VALUES (1, '인천 - 사이판 항공권 1번 좌석', 80000, '인천 - 사이판 항공권'), -- P 18
           (1, '인천 - 사이판 항공권 2번 좌석', 80000, '인천 - 사이판 항공권'), -- P 19
           (1, '인천 - 사이판 항공권 3번 좌석', 80000, '인천 - 사이판 항공권'), -- P 20
           (1, '인천 - 사이판 항공권 4번 좌석', 80000, '인천 - 사이판 항공권'), -- P 21
           (1, '인천 - 사이판 항공권 5번 좌석', 80000, '인천 - 사이판 항공권'), -- P 22
           (1, '인천 - 사이판 항공권 6번 좌석', 80000, '인천 - 사이판 항공권'), -- P 23
           (1, '인천 - 사이판 항공권 7번 좌석', 80000, '인천 - 사이판 항공권'), -- P 24
           (1, '인천 - 사이판 항공권 8번 좌석', 80000, '인천 - 사이판 항공권'), -- P 25
           (1, '인천 - 사이판 항공권 9번 좌석', 80000, '인천 - 사이판 항공권'), -- P 26

           (1, '인천 - 나리타 항공권', 80000, '인천 - 나리타 항공권'),       -- P 27
           (1, '인천 - 홍콩 항공권', 80000, '인천 - 홍콩 항공권'),
           (1, '사이판 - 인천 항공권', 80000, '사이판 - 인천  항공권'),
           (1, '사이판 - 나리타 항공권', 80000, '사이판  - 나리타 항공권'),
           (1, '사이판 - 홍콩 항공권', 80000, ' 사이판 - 홍콩  항공권'),
           (1, '나리타 - 인천 항공권', 80000, '나리타 - 인천 항공권'),
           (1, '나리타 - 사이판 항공권', 80000, '나리타 - 사이판 항공권'),
           (1, '나리타 - 홍콩 항공권', 80000, '나리타 - 홍콩 항공권'),
           (1, '홍콩 - 인천 항공권', 80000, '홍콩 - 인천 항공권'),

           (1, '인천 - 사이판 항공권', 80000, '인천 - 사이판 항공권'),
           (1, '인천 - 사이판 항공권', 80000, '인천 - 사이판 항공권'),
           (1, '인천 - 사이판 항공권', 80000, '인천 - 사이판 항공권');


    INSERT INTO airline_ticket (ticket_product_id, ticket_seat_id)
    VALUES (18, 1),
           (19, 2),
           (20, 3),
           (21, 4),
           (22, 5),
           (23, 6),
           (24, 7),
           (25, 8),
           (26, 9),

           (36, 46),
           (37, 47),
           (38, 48);


end //


#티켓 상수정의
DELIMITER //

CREATE PROCEDURE insert_ticket_constant()
BEGIN
    -- 할인 조건 삽입
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('소아', 10);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('제주도민', 25);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('재외제주도민', 15);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('4.3생존 희생자 및 유족', 50);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('군산시민', 10);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('출장목적의 군인', 10);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('국가유공상이자', 40);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('독립유공자', 40);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('장애인', 40);
    INSERT INTO discount (discount_conditions, discount_rate) VALUES ('없음', 0);

    -- 결제 종류 삽입
    INSERT INTO payment_type (payment_type_name) VALUES ('빠른결제');
    INSERT INTO payment_type (payment_type_name) VALUES ('간편결제');
    INSERT INTO payment_type (payment_type_name) VALUES ('카드 일반결제');
    INSERT INTO payment_type (payment_type_name) VALUES ('해외카드 결제');
    INSERT INTO payment_type (payment_type_name) VALUES ('계좌이체');

    -- 결제 수단 삽입
    INSERT INTO payment (payment_type_id) VALUES (1);
    INSERT INTO payment (payment_type_id) VALUES (2);
    INSERT INTO payment (payment_type_id) VALUES (3);
    INSERT INTO payment (payment_type_id) VALUES (4);
    INSERT INTO payment (payment_type_id) VALUES (5);

    -- 환불 결제 수단
    INSERT INTO refund_payment (refund_payment_type_id) VALUES (1);
    INSERT INTO refund_payment (refund_payment_type_id) VALUES (2);
    INSERT INTO refund_payment (refund_payment_type_id) VALUES (3);
    INSERT INTO refund_payment (refund_payment_type_id) VALUES (4);
    INSERT INTO refund_payment (refund_payment_type_id) VALUES (5);

    -- 환불 수수료 삽입
    INSERT INTO refund_fee (departure_point, refund_fee) VALUES ('~ 출발 61일 전', '1000');
    INSERT INTO refund_fee (departure_point, refund_fee) VALUES ('출발 60일 ~ 출발 31일 전', '3000');
    INSERT INTO refund_fee (departure_point, refund_fee) VALUES ('출발 30일 ~ 출발 15일 전', '5000');
    INSERT INTO refund_fee (departure_point, refund_fee) VALUES ('출발 14일 ~ 출발 2일 전', '7000');
    INSERT INTO refund_fee (departure_point, refund_fee) VALUES ('출발 1일 전 ~ 출발 시간 전', '12000');

    -- 환불 세금 삽입
    INSERT INTO refund_tax(refund_tax_id, refund_fax) VALUES (1, 3000);

    #운임(경로 필요)
    INSERT INTO fare(fare, airway_id)
    VALUES (837800, 1),
           (837800, 2);

    #세금(공항시설사용료) (공항 필요)
    INSERT INTO tax(tax, airport_id)
    VALUES (28000, 1),
           (14400, 2);

    #영수증 (결제수단, 할인, 세금(공항시설이용료), 운임 필요)
    INSERT INTO receipt(tax_id, tax_id2, fare_id, payment_time, discount_id, payment_id)
    VALUES (1, 2, 1, '2024-04-13', 10 , 1);

    #환불 영수증 (환불세금, 환불 수수료, 환불 결제수단 필요)
    INSERT INTO refund_receipt(refund_tax_id, refund_payment_id, refund_fee_id)
    VALUES (1, 1, 1);

    #티켓은 PAX 정보 필요
    #환불 티켓 필요

END//

DELIMITER ;

#실행
CALL init();



DROP FUNCTION IF EXISTS calculateAge;
DROP FUNCTION IF EXISTS insert_pax;
DROP FUNCTION IF EXISTS insert_pnr;
DROP FUNCTION IF EXISTS insert_seg;
DROP PROCEDURE IF EXISTS `one_way_reservation`;


-- Active: 1667279984414@@127.0.0.1@3306@flight_ticket_reservation_db
DELIMITER //

CREATE FUNCTION insert_seg(inserted_PNR_id INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE seg_id INT;

    -- Insert the PNR record into the PNR table
    INSERT INTO SEG (`PNR_id`) VALUES (inserted_PNR_id);

    -- Get the last inserted PNR ID
    SET seg_id = LAST_INSERT_ID();

    -- Return the PNR ID
    RETURN seg_id;
END//

DELIMITER ;


-- Active: 1667279984414@@127.0.0.1@3306@flight_ticket_reservation_db
DELIMITER //

CREATE FUNCTION insert_pnr(customer_id INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE pnr_id INT;

    -- Insert the PNR record into the PNR table
    INSERT INTO PNR (customer_id) VALUES (customer_id);

    -- Get the last inserted PNR ID
    SET pnr_id = LAST_INSERT_ID();

    -- Return the PNR ID
    RETURN pnr_id;
END//

DELIMITER ;

-- Active: 1667279984414@@127.0.0.1@3306@flight_ticket_reservation_db
DELIMITER //

CREATE FUNCTION insert_pax(
    `pnr_id` INT,
    `name` VARCHAR(45),
    `age` INT,
    `phone_number` VARCHAR(13)
)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE `pax_id` INT;

    -- Insert the PNR record into the PNR table
    INSERT INTO `PAX` (
        `PNR_id`,
        `name`,
        `age`,
        `phone_number`
    )
    VALUES (
        `pnr_id`,
        `name`,
        `age`,
        `phone_number`
    );

    -- Get the last inserted PNR ID
    SET `pax_id` = LAST_INSERT_ID();

    -- Return the PNR ID
    RETURN `pax_id`;
END//

DELIMITER ;

-- Active: 1667279984414@@127.0.0.1@3306@flight_ticket_reservation_db
DELIMITER //

CREATE FUNCTION calculateAge(birthdate DATE)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE age INT;
    DECLARE `current_date` DATE;
    DECLARE birth_date DATE;

    SET `current_date` = CURDATE();
    SET birth_date = DATE(birthdate);

    SET age = YEAR(`current_date`) - YEAR(birth_date);

    IF (MONTH(`current_date`) < MONTH(birth_date)) OR
       (MONTH(`current_date`) = MONTH(birth_date) AND DAY(`current_date`) < DAY(birth_date)) THEN
        SET age = age - 1;
    END IF;

    RETURN age;
END//

DELIMITER ;




DELIMITER //

CREATE PROCEDURE `one_way_reservation`(
    `departure_airport_id` INT,
    `arrival_airport_id` INT,
    `customer_id` INT,
    `product_id_list` TEXT,
    `adult_count` INT,
    `children_count` INT,
    `infants_count` INT,
    `payment_type` INT,
    `fare_id` INT,
    `combined_product_id` INT,
    `reservation_person_name_list` TEXT,
    `reservation_person_birth_list` TEXT,
    `reservation_person_discount_id` INT,
    `reservation_representative_phone_number` VARCHAR(13),
    `reservation_representative_email` VARCHAR(45),
    `checked_baggage_list` TEXT,
    `extra_service_reserved_list` TEXT
    )
BEGIN
    DECLARE total_person_count INT;
    DECLARE inserted_PNR_id INT;
    DECLARE inserted_PAX_id INT;
    DECLARE inserted_SEG_id INT;
    DECLARE i INT DEFAULT 1;
    DECLARE `names` TEXT;
    DECLARE genders TEXT;
    DECLARE births TEXT;
    DECLARE product_id INT;

    SET total_person_count = adult_count + children_count + infants_count;
    SET inserted_PNR_id = (SELECT insert_pnr(customer_id));
    SET inserted_SEG_id = (SELECT insert_seg(inserted_PNR_id));

    WHILE i <= total_person_count DO
        SET `names` = SUBSTRING_INDEX(SUBSTRING_INDEX(reservation_person_name_list, ',', i), ',', -1);
        SET births = SUBSTRING_INDEX(SUBSTRING_INDEX(reservation_person_birth_list, ',', i), ',', -1);
        SET product_id = SUBSTRING_INDEX(SUBSTRING_INDEX(product_id_list, ',', i), ',', -1);
        SET inserted_PAX_id = (SELECT insert_pax(inserted_PNR_id , `names`, (SELECT calculateAge(births)), reservation_representative_phone_number));
        INSERT INTO receipt (payment_id, discount_id, tax_id, tax_id2, fare_id) VALUES (payment_type, reservation_person_discount_id, departure_airport_id, arrival_airport_id, fare_id);
        INSERT INTO ticket (PAX_id, product_id, receipt_id) VALUES (inserted_PAX_id, product_id, LAST_INSERT_ID());
        SET i = i + 1;
    END WHILE;

    SET i = 1;
    WHILE i <= LENGTH(extra_service_reserved_list) - LENGTH(REPLACE(extra_service_reserved_list, ',', '')) + 1 DO
        SET product_id = SUBSTRING_INDEX(SUBSTRING_INDEX(extra_service_reserved_list, ',', i), ',', -1);
        INSERT INTO extra_service_reserved (SEG_id, PAX_id, product_id) VALUES (inserted_SEG_id, inserted_PAX_id, product_id);
        SET i = i + 1;
    END WHILE;

END //




#강사님 아래처럼 데이터 넣고 실행하시면 예약 정보가 담깁니다.
CALL one_way_reservation(1, 2, 1, "18,19", 2, 0, 0, 3, 1, null, "장기환,이세희", "1999-03-19,1999-04-13", 10, "010-1111-1111", "email@email.com", null, "1,2,3");