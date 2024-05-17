CREATE TABLE IF NOT EXISTS `seal_like`
(
    `id`        BIGINT NOT NULL auto_increment,
    `member_id` BIGINT NOT NULL,
    `seal_id`   BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `stamp_like`
(
    `id`        BIGINT NOT NULL auto_increment,
    `member_id` BIGINT NOT NULL,
    `stamp_id`  BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `member`
(
    `id`            BIGINT     NOT NULL auto_increment,
    `social_id`     BIGINT     NOT NULL,
    `nickname`      VARCHAR(255), -- unique
    `image`         VARCHAR(255),
    `usable`        TINYINT(1) NOT NULL,
    `refresh_token` varchar(255),
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `seal`
(
    `id`              BIGINT       NOT NULL auto_increment,
    `member_id`       BIGINT       NOT NULL,
    `name`            VARCHAR(255) NOT NULL,
    `nickname`        VARCHAR(255) NOT NULL,
    `image_url`       VARCHAR(255) NOT NULL,
    `created_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `number_of_likes` MEDIUMINT    NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `stamp`
(
    `id`              BIGINT       NOT NULL auto_increment,
    `name`            VARCHAR(255) NOT NULL,
    `image_url`       VARCHAR(255) NOT NULL,
    `created_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `number_of_likes` MEDIUMINT    NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `mark_to_seal`
(
    `id`       BIGINT NOT NULL auto_increment,
    `seal_id`  BIGINT NOT NULL,
    `stamp_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `stamp_record`
(
    `id`        BIGINT NOT NULL auto_increment,
    `member_id` BIGINT NOT NULL,
    `stamp_id`  BIGINT NOT NULL,
    PRIMARY KEY (`id`)
)
    engine = innodb
    auto_increment = 1
    DEFAULT charset = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

