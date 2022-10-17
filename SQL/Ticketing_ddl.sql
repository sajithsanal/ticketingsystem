create schema ticketing_schema;

CREATE TABLE `ticketing_schema`.`product_details` (
id INT AUTO_INCREMENT,
name VARCHAR(200),
PRIMARY KEY(id)
);


CREATE TABLE `ticketing_schema`.`agent_details` (
  id INT AUTO_INCREMENT,
  full_name VARCHAR(200),
  user_name VARCHAR(200),
  password VARCHAR(200),
  PRIMARY KEY(id)
  );

CREATE TABLE `ticketing_schema`.`customer_details` (
  id INT AUTO_INCREMENT,
  full_name VARCHAR(200),
  email_id VARCHAR(200),
  password VARCHAR(200),
  mobile_no VARCHAR(200),
  date_of_birth DATE,
  PRIMARY KEY(id)
  );


CREATE TABLE `ticketing_schema`.`ticket_details` (
    ticket_id INT AUTO_INCREMENT,
    product_id INT,
    customer_id INT,
    complaint_details TEXT,
    status VARCHAR(100),
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    created_by INT,
    updated_by INT,
    assigned_to INT,
    creator_type VARCHAR(100),
    PRIMARY KEY(ticket_id)
    );

    CREATE TABLE `ticketing_schema`.`ticket_trans_details` (
        id INT AUTO_INCREMENT,
        ticket_id INT NOT NULL REFERENCES ticket_details(ticket_id),
        notes TEXT,
        status VARCHAR(100),
        updated_date TIMESTAMP,
        updated_by INT,
		PRIMARY KEY(id)
        );


        CREATE TABLE `ticketing_schema`.`ticket_attachments` (
                id INT AUTO_INCREMENT,
                ticket_id INT,
                trans_id INT,
                attachment_name VARCHAR(200),
                attachment BLOB,
                PRIMARY KEY(id)
                );

create table `ticketing_schema`.`ticket_details_aud` (
       ticket_id bigint not null,
        rev integer not null,
        revtype tinyint,
        assigned_to varchar(255),
        complaint_details varchar(255),
        created_by bigint,
        created_date datetime,
        creator_type varchar(255),
        customer_id bigint,
        updated_by bigint,
        updated_date datetime,
        product_id bigint,
        status varchar(255),
        primary key (ticket_id, rev)
    );

	create table `ticketing_schema`.`ticket_trans_details_aud` (
       id bigint not null,
        rev integer not null,
        revtype tinyint,
        updated_by bigint,
        updated_date datetime,
        notes varchar(255),
        status varchar(255),
        ticket_id bigint,
        primary key (id, rev)
    );

    create table `ticketing_schema`.`REVINFO` (
            REV integer AUTO_INCREMENT,
            REVTSTMP bigint,
            primary key (REV)
        );

