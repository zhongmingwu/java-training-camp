DROP TABLE IF EXISTS `himly_dubbo_account`;

CREATE TABLE `himly_dubbo_account` (
    `id` int(11) NOT NULL PRIMARY KEY,
    `name` VARCHAR(64) NOT NULL,
    `us_wallet` INT(11) NOT NULL,
    `cny_wallet` INT(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;