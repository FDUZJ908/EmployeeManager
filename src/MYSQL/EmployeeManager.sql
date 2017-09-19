/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/8/11 22:54:16                           */
/*==============================================================*/

CREATE DATABASE `EmployeeManager` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

use EmployeeManager;

drop table if exists caseReport;

drop table if exists department;

drop table if exists generalReport;

drop table if exists leaderReport;

drop table if exists photo;

drop table if exists undealedCaseReport;

drop table if exists undealedGeneralReport;

drop table if exists user;

/*==============================================================*/
/* Table: caseReport                                            */
/*==============================================================*/
create table caseReport
(
   reportID             int not null auto_increment,
   userID               char(32) not null,
   leaderName           char(16) ,
   members              char(64) ,
   category             tinyint not null,
   reportText           text,
   reportPath           text,
   scoreType            bool,
   singleScore          int DEFAULT 0,
   submitTime           datetime,
   checkTime            datetime,
   isPass               bool,
   comment              text,
   primary key (reportID)
);

/*==============================================================*/
/* Table: department                                            */
/*==============================================================*/
create table department
(
   dID                  char(3) not null,
   userID               char(32) not null,
   dName                char(32) not null,
   isLeader             bool not null default false,
   primary key (dID, userID)
);

/*==============================================================*/
/* Table: generalReport                                         */
/*==============================================================*/
create table generalReport
(
   reportID             int not null auto_increment,
   userID               char(32) not null,
   leaderName           char(16),
   category             tinyint not null,
   reportText           text,
   reportPath           text,
   submitTime           datetime,
   checkTime            datetime,
   isPass               bool,
   comment              text,
   primary key (reportID)
);

/*==============================================================*/
/* Table: leaderReport                                          */
/*==============================================================*/
create table leaderReport
(
   reportID             int not null auto_increment,
   userID               char(32) not null,
   members              char(64) ,
   category             tinyint not null,
   reportText           text,
   reportPath           text,
   scoreType            bool,
   singleScore          int DEFAULT 0,
   submitTime           datetime,
   checkTime            datetime,
   isPass               bool,
   comment              text,
   primary key (reportID)
);

/*==============================================================*/
/* Table: photo                                                 */
/*==============================================================*/
create table photo
(
   photoID              int not null,
   userID               char(32) not null,
   details              text,
   photoPath            text not null,
   primary key (photoID)
);

/*==============================================================*/
/* Table: undealedCaseReport                                    */
/*==============================================================*/
create table undealedCaseReport
(
   reportID             int not null auto_increment,
   userID               char(32) not null,
   leaderName           char(16) not null,
   members              char(64),
   category             tinyint,
   reportText           text,
   reportPath           text,
   scoreType            bool,
   singleScore          int DEFAULT 0,
   submitTime           datetime,
   checkTime            datetime,
   isPass               bool,
   comment              text,
   primary key (reportID)
);

/*==============================================================*/
/* Table: undealedGeneralReport                                 */
/*==============================================================*/
create table undealedGeneralReport
(
   reportID             int not null auto_increment,
   userID               char(32) not null,
   leaderName           char(16) not null,
   category             tinyint,
   reportText           text,
   reportPath           text,
   submitTime           datetime,
   checkTime            datetime,
   isPass               bool,
   comment              text,
   primary key (reportID)
);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   userID               char(32) not null,
   avatarURL           text,
   userName             char(16) not null,
   duty                 char(64),
   title                tinyint ,
   position             tinyint ,
   status               tinyint DEFAULT 0,
   f_score              int DEFAULT 0,
   s_score              int DEFAULT 0,
   tel                  char(11),
   gender				char(1),
   email				char(32),
   remark               text,
   primary key (userID)
)
auto_increment = 1;

alter table caseReport add constraint FK_Reference_2 foreign key (userID)
      references user (userID) on delete restrict on update restrict;

alter table department add constraint FK_Reference_7 foreign key (userID)
      references user (userID) on delete restrict on update restrict;

alter table generalReport add constraint FK_Reference_1 foreign key (userID)
      references user (userID) on delete restrict on update restrict;

alter table leaderReport add constraint FK_Reference_3 foreign key (userID)
      references user (userID) on delete restrict on update restrict;

alter table photo add constraint FK_Reference_4 foreign key (userID)
      references user (userID) on delete restrict on update restrict;

alter table undealedCaseReport add constraint FK_Reference_6 foreign key (userID)
      references user (userID) on delete restrict on update restrict;

alter table undealedGeneralReport add constraint FK_Reference_8 foreign key (userID)
      references user (userID) on delete restrict on update restrict;

