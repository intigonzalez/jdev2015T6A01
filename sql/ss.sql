-- MySQL Script generated by MySQL Workbench
-- Mon May 11 11:39:29 2015
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ss_development
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ss_development
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ss_development` DEFAULT CHARACTER SET latin1 ;
USE `ss_development` ;

-- -----------------------------------------------------
-- Table `ss_development`.`activity_objects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activity_objects` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL DEFAULT '',
  `description` TEXT NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `object_type` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`actors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`actors` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL DEFAULT '',
  `slug` VARCHAR(255) NULL DEFAULT NULL,
  `subject_type` VARCHAR(255) NULL DEFAULT NULL,
  `notify_by_email` TINYINT(1) NULL DEFAULT '1',
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `logo_file_name` VARCHAR(255) NULL DEFAULT NULL,
  `logo_content_type` VARCHAR(255) NULL DEFAULT NULL,
  `logo_file_size` INT(11) NULL DEFAULT NULL,
  `logo_updated_at` DATETIME NULL DEFAULT NULL,
  `notification_settings` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_actors_on_slug` (`slug` ASC),
  INDEX `index_actors_on_activity_object_id` (`activity_object_id` ASC),
  INDEX `index_actors_on_email` (`email` ASC),
  CONSTRAINT `actors_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`activity_verbs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activity_verbs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`activities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activities` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_verb_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `ancestry` VARCHAR(255) NULL DEFAULT NULL,
  `author_id` INT(11) NULL DEFAULT NULL,
  `user_author_id` INT(11) NULL DEFAULT NULL,
  `owner_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_activities_on_activity_verb_id` (`activity_verb_id` ASC),
  INDEX `index_activities_on_author_id` (`author_id` ASC),
  INDEX `index_activities_on_user_author_id` (`user_author_id` ASC),
  INDEX `index_activities_on_owner_id` (`owner_id` ASC),
  CONSTRAINT `index_activities_on_owner_id`
    FOREIGN KEY (`owner_id`)
    REFERENCES `ss_development`.`actors` (`id`),
  CONSTRAINT `index_activities_on_activity_verb_id`
    FOREIGN KEY (`activity_verb_id`)
    REFERENCES `ss_development`.`activity_verbs` (`id`),
  CONSTRAINT `index_activities_on_author_id`
    FOREIGN KEY (`author_id`)
    REFERENCES `ss_development`.`actors` (`id`),
  CONSTRAINT `index_activities_on_user_author_id`
    FOREIGN KEY (`user_author_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`activity_actions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activity_actions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actor_id` INT(11) NULL DEFAULT NULL,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `follow` TINYINT(1) NULL DEFAULT '0',
  `like` TINYINT(1) NULL DEFAULT '0',
  `owner` TINYINT(1) NULL DEFAULT '0',
  `author` TINYINT(1) NULL DEFAULT '0',
  `user_author` TINYINT(1) NULL DEFAULT '0',
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_activity_actions_on_actor_id` (`actor_id` ASC),
  INDEX `index_activity_actions_on_activity_object_id` (`activity_object_id` ASC),
  CONSTRAINT `index_activity_actions_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`),
  CONSTRAINT `index_activity_actions_on_actor_id`
    FOREIGN KEY (`actor_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`activity_object_activities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activity_object_activities` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_id` INT(11) NULL DEFAULT NULL,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `object_type` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_activity_object_activities_on_activity_id` (`activity_id` ASC),
  INDEX `index_activity_object_activities_on_activity_object_id` (`activity_object_id` ASC),
  CONSTRAINT `activity_object_activities_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`),
  CONSTRAINT `index_activity_object_activities_on_activity_id`
    FOREIGN KEY (`activity_id`)
    REFERENCES `ss_development`.`activities` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`relations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`relations` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actor_id` INT(11) NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `sender_type` VARCHAR(255) NULL DEFAULT NULL,
  `receiver_type` VARCHAR(255) NULL DEFAULT NULL,
  `ancestry` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_relations_on_actor_id` (`actor_id` ASC),
  INDEX `index_relations_on_ancestry` (`ancestry` ASC),
  CONSTRAINT `relations_on_actor_id`
    FOREIGN KEY (`actor_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`activity_object_audiences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activity_object_audiences` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `relation_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `activity_object_audiences_on_activity_object_id` (`activity_object_id` ASC),
  INDEX `activity_object_audiences_on_relation_id` (`relation_id` ASC),
  CONSTRAINT `activity_object_audiences_on_relation_id`
    FOREIGN KEY (`relation_id`)
    REFERENCES `ss_development`.`relations` (`id`),
  CONSTRAINT `activity_object_audiences_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`activity_object_properties`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activity_object_properties` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `property_id` INT(11) NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `main` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_activity_object_properties_on_activity_object_id` (`activity_object_id` ASC),
  INDEX `index_activity_object_properties_on_property_id` (`property_id` ASC),
  CONSTRAINT `index_activity_object_properties_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`),
  CONSTRAINT `index_activity_object_properties_on_property_id`
    FOREIGN KEY (`property_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`actor_keys`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`actor_keys` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actor_id` INT(11) NULL DEFAULT NULL,
  `key_der` BLOB NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_actor_keys_on_actor_id` (`actor_id` ASC),
  CONSTRAINT `actor_keys_on_actor_id`
    FOREIGN KEY (`actor_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`audiences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`audiences` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `relation_id` INT(11) NULL DEFAULT NULL,
  `activity_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_audiences_on_activity_id` (`activity_id` ASC),
  INDEX `index_audiences_on_relation_id` (`relation_id` ASC),
  CONSTRAINT `audiences_on_relation_id`
    FOREIGN KEY (`relation_id`)
    REFERENCES `ss_development`.`relations` (`id`),
  CONSTRAINT `audiences_on_activity_id`
    FOREIGN KEY (`activity_id`)
    REFERENCES `ss_development`.`activities` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `encrypted_password` VARCHAR(128) NOT NULL DEFAULT '',
  `password_salt` VARCHAR(255) NULL DEFAULT NULL,
  `reset_password_token` VARCHAR(255) NULL DEFAULT NULL,
  `reset_password_sent_at` DATETIME NULL DEFAULT NULL,
  `remember_created_at` DATETIME NULL DEFAULT NULL,
  `sign_in_count` INT(11) NULL DEFAULT '0',
  `current_sign_in_at` DATETIME NULL DEFAULT NULL,
  `last_sign_in_at` DATETIME NULL DEFAULT NULL,
  `current_sign_in_ip` VARCHAR(255) NULL DEFAULT NULL,
  `last_sign_in_ip` VARCHAR(255) NULL DEFAULT NULL,
  `authentication_token` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `actor_id` INT(11) NULL DEFAULT NULL,
  `language` VARCHAR(255) NULL DEFAULT NULL,
  `connected` TINYINT(1) NULL DEFAULT '0',
  `status` VARCHAR(255) NULL DEFAULT 'available',
  `chat_enabled` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `index_users_on_reset_password_token` (`reset_password_token` ASC),
  INDEX `index_users_on_actor_id` (`actor_id` ASC),
  CONSTRAINT `users_on_actor_id`
    FOREIGN KEY (`actor_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`authentications`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`authentications` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NULL DEFAULT NULL,
  `provider` VARCHAR(255) NULL DEFAULT NULL,
  `uid` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_authentications_on_user_id` (`user_id` ASC),
  CONSTRAINT `authentications_on_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `ss_development`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`comments` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_comments_on_activity_object_id` (`activity_object_id` ASC),
  CONSTRAINT `comments_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`contacts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`contacts` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `sender_id` INT(11) NULL DEFAULT NULL,
  `receiver_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `inverse_id` INT(11) NULL DEFAULT NULL,
  `ties_count` INT(11) NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `index_contacts_on_inverse_id` (`inverse_id` ASC),
  INDEX `index_contacts_on_receiver_id` (`receiver_id` ASC),
  INDEX `index_contacts_on_sender_id` (`sender_id` ASC),
  CONSTRAINT `contacts_on_sender_id`
    FOREIGN KEY (`sender_id`)
    REFERENCES `ss_development`.`actors` (`id`),
  CONSTRAINT `contacts_on_receiver_id`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`conversations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`conversations` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `subject` VARCHAR(255) NULL DEFAULT '',
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`devices`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`devices` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NULL DEFAULT NULL,
  `os_type` VARCHAR(16) NULL DEFAULT NULL,
  `registration_id` VARBINARY(4096) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `devices_on_user_id_idx` (`user_id` ASC),
  CONSTRAINT `devices_on_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `ss_development`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`documents`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`documents` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `file_file_name` VARCHAR(255) NULL DEFAULT NULL,
  `file_content_type` VARCHAR(255) NULL DEFAULT NULL,
  `file_file_size` VARCHAR(255) NULL DEFAULT NULL,
  `file_processing` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_documents_on_activity_object_id` (`activity_object_id` ASC),
  CONSTRAINT `documents_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`rooms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`rooms` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actor_id` INT(11) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_rooms_on_actor_id` (`actor_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`events` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `start_at` DATETIME NULL DEFAULT NULL,
  `end_at` DATETIME NULL DEFAULT NULL,
  `all_day` TINYINT(1) NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `room_id` INT(11) NULL DEFAULT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `frequency` INT(11) NULL DEFAULT '0',
  `interval` INT(11) NULL DEFAULT NULL,
  `days` INT(11) NULL DEFAULT '0',
  `interval_flag` INT(11) NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `index_events_on_room_id` (`room_id` ASC),
  INDEX `events_on_activity_object_id` (`activity_object_id` ASC),
  CONSTRAINT `index_events_on_room_id`
    FOREIGN KEY (`room_id`)
    REFERENCES `ss_development`.`rooms` (`id`),
  CONSTRAINT `events_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`groups` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actor_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_groups_on_actor_id` (`actor_id` ASC),
  CONSTRAINT `groups_on_actor_id`
    FOREIGN KEY (`actor_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`links`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`links` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `url` VARCHAR(255) NULL DEFAULT NULL,
  `callback_url` VARCHAR(255) NULL DEFAULT NULL,
  `image` VARCHAR(255) NULL DEFAULT NULL,
  `width` INT(11) NULL DEFAULT '470',
  `height` INT(11) NULL DEFAULT '353',
  PRIMARY KEY (`id`),
  INDEX `index_links_on_activity_object_id` (`activity_object_id` ASC),
  CONSTRAINT `links_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`notifications`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`notifications` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `body` TEXT NULL DEFAULT NULL,
  `subject` VARCHAR(255) NULL DEFAULT '',
  `sender_id` INT(11) NULL DEFAULT NULL,
  `sender_type` VARCHAR(255) NULL DEFAULT NULL,
  `conversation_id` INT(11) NULL DEFAULT NULL,
  `draft` TINYINT(1) NULL DEFAULT '0',
  `updated_at` DATETIME NOT NULL,
  `created_at` DATETIME NOT NULL,
  `notified_object_id` INT(11) NULL DEFAULT NULL,
  `notified_object_type` VARCHAR(255) NULL DEFAULT NULL,
  `notification_code` VARCHAR(255) NULL DEFAULT NULL,
  `attachment` VARCHAR(255) NULL DEFAULT NULL,
  `global` TINYINT(1) NULL DEFAULT '0',
  `expires` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_notifications_on_conversation_id` (`conversation_id` ASC),
  CONSTRAINT `notifications_on_conversation_id`
    FOREIGN KEY (`conversation_id`)
    REFERENCES `ss_development`.`conversations` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`sites`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`sites` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `config` TEXT NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `actor_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_sites_on_actor_id` (`actor_id` ASC),
  CONSTRAINT `index_sites_on_actor_id`
    FOREIGN KEY (`actor_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`oauth2_tokens`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`oauth2_tokens` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` INT(11) NULL DEFAULT NULL,
  `site_id` INT(11) NULL DEFAULT NULL,
  `token` VARCHAR(255) NULL DEFAULT NULL,
  `redirect_uri` VARCHAR(255) NULL DEFAULT NULL,
  `refresh_token_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  `expires_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_oauth2_tokens_on_user_id` (`user_id` ASC),
  INDEX `index_oauth2_tokens_on_site_id` (`site_id` ASC),
  INDEX `index_oauth2_tokens_on_token` (`token` ASC),
  INDEX `index_oauth2_tokens_on_refresh_token_id` (`refresh_token_id` ASC),
  CONSTRAINT `index_oauth2_tokens_on_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `ss_development`.`users` (`id`),
  CONSTRAINT `index_oauth2_tokens_on_site_id`
    FOREIGN KEY (`site_id`)
    REFERENCES `ss_development`.`sites` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`permissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`permissions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(255) NULL DEFAULT NULL,
  `object` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`posts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`posts` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_posts_on_activity_object_id` (`activity_object_id` ASC),
  CONSTRAINT `posts_on_activity_object_id`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`profiles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`profiles` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actor_id` INT(11) NULL DEFAULT NULL,
  `birthday` DATE NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `organization` VARCHAR(45) NULL DEFAULT NULL,
  `phone` VARCHAR(45) NULL DEFAULT NULL,
  `mobile` VARCHAR(45) NULL DEFAULT NULL,
  `fax` VARCHAR(45) NULL DEFAULT NULL,
  `address` VARCHAR(255) NULL DEFAULT NULL,
  `city` VARCHAR(255) NULL DEFAULT NULL,
  `zipcode` VARCHAR(45) NULL DEFAULT NULL,
  `province` VARCHAR(45) NULL DEFAULT NULL,
  `country` VARCHAR(45) NULL DEFAULT NULL,
  `prefix_key` INT(11) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `experience` VARCHAR(255) NULL DEFAULT NULL,
  `website` VARCHAR(255) NULL DEFAULT NULL,
  `skype` VARCHAR(45) NULL DEFAULT NULL,
  `im` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_profiles_on_actor_id` (`actor_id` ASC),
  CONSTRAINT `profiles_on_actor_id`
    FOREIGN KEY (`actor_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`receipts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`receipts` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `receiver_id` INT(11) NULL DEFAULT NULL,
  `receiver_type` VARCHAR(255) NULL DEFAULT NULL,
  `notification_id` INT(11) NOT NULL,
  `is_read` TINYINT(1) NULL DEFAULT '0',
  `trashed` TINYINT(1) NULL DEFAULT '0',
  `deleted` TINYINT(1) NULL DEFAULT '0',
  `mailbox_type` VARCHAR(25) NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_receipts_on_notification_id` (`notification_id` ASC),
  CONSTRAINT `receipts_on_notification_id`
    FOREIGN KEY (`notification_id`)
    REFERENCES `ss_development`.`notifications` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`relation_permissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`relation_permissions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `relation_id` INT(11) NULL DEFAULT NULL,
  `permission_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_relation_permissions_on_permission_id` (`permission_id` ASC),
  INDEX `index_relation_permissions_on_relation_id` (`relation_id` ASC),
  CONSTRAINT `relation_permissions_on_relation_id`
    FOREIGN KEY (`relation_id`)
    REFERENCES `ss_development`.`relations` (`id`),
  CONSTRAINT `relation_permissions_on_permission_id`
    FOREIGN KEY (`permission_id`)
    REFERENCES `ss_development`.`permissions` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`remote_subjects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`remote_subjects` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `actor_id` INT(11) NULL DEFAULT NULL,
  `webfinger_id` VARCHAR(255) NULL DEFAULT NULL,
  `webfinger_info` TEXT NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_remote_subjects_on_actor_id` (`actor_id` ASC),
  CONSTRAINT `remote_subjects_on_actor_id`
    FOREIGN KEY (`actor_id`)
    REFERENCES `ss_development`.`actors` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`schema_migrations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`schema_migrations` (
  `version` VARCHAR(255) NOT NULL,
  UNIQUE INDEX `unique_schema_migrations` (`version` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`taggings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`taggings` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `tag_id` INT(11) NULL DEFAULT NULL,
  `taggable_id` INT(11) NULL DEFAULT NULL,
  `taggable_type` VARCHAR(255) NULL DEFAULT NULL,
  `tagger_id` INT(11) NULL DEFAULT NULL,
  `tagger_type` VARCHAR(255) NULL DEFAULT NULL,
  `context` VARCHAR(128) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_taggings_on_tag_id` (`tag_id` ASC),
  INDEX `index_taggings_on_taggable_id_and_taggable_type_and_context` (`taggable_id` ASC, `taggable_type` ASC, `context` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`tags` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`ties`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`ties` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `contact_id` INT(11) NULL DEFAULT NULL,
  `relation_id` INT(11) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `updated_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `index_ties_on_contact_id` (`contact_id` ASC),
  INDEX `index_ties_on_relation_id` (`relation_id` ASC),
  CONSTRAINT `ties_on_relation_id`
    FOREIGN KEY (`relation_id`)
    REFERENCES `ss_development`.`relations` (`id`),
  CONSTRAINT `ties_on_contact_id`
    FOREIGN KEY (`contact_id`)
    REFERENCES `ss_development`.`contacts` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`activity_counter`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activity_counter` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  `like_count` INT(11) NULL DEFAULT '0',
  `follower_count` INT(11) NULL DEFAULT '0',
  `visit_count` INT(11) NULL DEFAULT '0',
  `comment_count` INT(11) NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `index_posts_on_activity_object_id` (`activity_object_id` ASC),
  CONSTRAINT `posts_on_activity_object_id0`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`metadata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`metadata` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `metadata` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`activity_metadata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`activity_metadata` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `metadata_id` INT(11) NULL DEFAULT NULL,
  `activity_object_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `activity_metadata_on_activity_object_id00_idx` (`activity_object_id` ASC),
  INDEX `activity_metadata_on_metadata_id00_idx` (`metadata_id` ASC),
  CONSTRAINT `activity_metadata_on_activity_object_id00`
    FOREIGN KEY (`activity_object_id`)
    REFERENCES `ss_development`.`activity_objects` (`id`),
  CONSTRAINT `activity_metadata_on_metadata_id00`
    FOREIGN KEY (`metadata_id`)
    REFERENCES `ss_development`.`metadata` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ss_development`.`devices_analytic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ss_development`.`devices_analytic` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `devices_id` INT(11) NULL DEFAULT NULL,
  `manufacturer` VARCHAR(255) NULL DEFAULT NULL,
  `model` VARCHAR(255) NULL DEFAULT NULL,
  `os_version` VARCHAR(16) NULL DEFAULT NULL,
  `app_version` VARCHAR(16) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `devices_on_user_id0_idx` (`devices_id` ASC),
  CONSTRAINT `devices_on_user_id0`
    FOREIGN KEY (`devices_id`)
    REFERENCES `ss_development`.`devices` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;