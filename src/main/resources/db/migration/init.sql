CREATE DATABASE IF NOT EXISTS `demo`;

CREATE TABLE IF NOT EXISTS `orders`
(
    `id`                    VARCHAR(36)  NOT NULL,
    `origin_latitude`       DECIMAL      NOT NULL,
    `origin_longitude`      DECIMAL      NOT NULL,
    `destination_latitude`  DECIMAL      NOT NULL,
    `destination_longitude` DECIMAL      NOT NULL,
    `distance`              INT UNSIGNED NOT NULL,
    `status`                VARCHAR(32)  NOT NULL DEFAULT 'UNASSIGNED',
    `created_time`          TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_time`          TIMESTAMP(6) NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_created_time` (`created_time`)
);
