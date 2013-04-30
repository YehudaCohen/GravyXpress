# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                        bigint not null,
  menu_id                   bigint not null,
  name                      varchar(255),
  enabled                   boolean,
  constraint pk_category primary key (id))
;

create table menu (
  id                        bigint not null,
  constraint pk_menu primary key (id))
;

create table menu_item (
  id                        bigint not null,
  category_id               bigint not null,
  name                      varchar(255),
  price                     float,
  description               varchar(255),
  enabled                   boolean,
  constraint pk_menu_item primary key (id))
;

create table restaurant (
  id                        bigint not null,
  name                      varchar(255),
  owner                     varchar(255),
  password                  varchar(255),
  hours                     TEXT,
  about                     TEXT,
  menu_id                   bigint,
  constraint pk_restaurant primary key (id))
;

create sequence category_seq;

create sequence menu_seq;

create sequence menu_item_seq;

create sequence restaurant_seq;

alter table category add constraint fk_category_menu_1 foreign key (menu_id) references menu (id) on delete restrict on update restrict;
create index ix_category_menu_1 on category (menu_id);
alter table menu_item add constraint fk_menu_item_category_2 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_menu_item_category_2 on menu_item (category_id);
alter table restaurant add constraint fk_restaurant_menu_3 foreign key (menu_id) references menu (id) on delete restrict on update restrict;
create index ix_restaurant_menu_3 on restaurant (menu_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists category;

drop table if exists menu;

drop table if exists menu_item;

drop table if exists restaurant;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists category_seq;

drop sequence if exists menu_seq;

drop sequence if exists menu_item_seq;

drop sequence if exists restaurant_seq;

