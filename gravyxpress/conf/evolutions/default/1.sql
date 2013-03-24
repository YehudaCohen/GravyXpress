# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table restaurant (
  id                        bigint not null,
  name                      varchar(255),
  owner                     varchar(255),
  constraint pk_restaurant primary key (id))
;

create sequence restaurant_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists restaurant;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists restaurant_seq;

