-- liquibase formatted sql

-- changeset gordey_dovydenko:1

create sequence s_organization_id start with 1 increment by 1;

create table organization
(
    organization_id        bigint default nextval('s_organization_id'),
    name                   varchar(255) not null,
    primary key (organization_id)
);

-- rollback DROP TABLE organization;
-- rollback DROP SEQUENCE s_organization_id;

-- changeset gordey_dovydenko:2

create table organization_manager
(
    organization_id         bigint not null,
    manager_id              varchar(60) not null,
    foreign key (organization_id) references organization (organization_id)
);

-- rollback DROP TABLE organization_manager;

-- changeset gordey_dovydenko:3

create sequence s_applications_for_membership_id start with 1 increment by 1;

create table applications_for_membership
(
    application_id          bigint default nextval('s_applications_for_membership_id'),
    organization_id         bigint not null,
    manager_id              varchar(60) not null,
    status                  varchar(20) check (status in ('PENDING', 'APPROVED', 'REJECTED')) not null,
    created_at              timestamp not null,
    primary key (application_id),
    foreign key (organization_id) references organization (organization_id)
);

-- rollback DROP TABLE applications_for_membership;
-- rollback DROP SEQUENCE s_application_id;

-- changeset gordey_dovydenko:4

create sequence s_event_id start with 1 increment by 1;

create table event
(
    event_id                bigint default nextval('s_event_id'),
    google_event_id         varchar(255) not null,
    organization_id         bigint not null,
    creator_id             varchar(60) not null,
    name                    varchar(255) not null,
    description             text,
    location_name           varchar(255) not null,
    start_date              timestamp not null,
    end_date                timestamp not null,
    deadline                timestamp not null,
    primary key (event_id),
    foreign key (organization_id) references organization (organization_id)
);

-- rollback DROP TABLE event;
-- rollback DROP SEQUENCE s_event_id;

-- changeset gordey_dovydenko:5

create table user_event
(
    user_id                 varchar(60) not null,
    event_id                bigint not null,
    primary key (user_id, event_id),
    foreign key (event_id) references event (event_id)
);

-- rollback DROP TABLE user_event;
