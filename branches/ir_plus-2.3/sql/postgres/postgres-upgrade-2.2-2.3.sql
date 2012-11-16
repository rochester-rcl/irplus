-- ---------------------------------------------
-- Create an old Ip address Ignore table
-- ---------------------------------------------
CREATE TABLE ir_statistics.old_ip_address_ignore
(
    old_ip_address_ignore_id BIGINT PRIMARY KEY,
    name TEXT,
    store_counts BOOLEAN NOT NULL,
    description TEXT,
    from_ip_address_part1 INTEGER,
    from_ip_address_part2 INTEGER,
    from_ip_address_part3 INTEGER,
    from_ip_address_part4 INTEGER,
    to_ip_address_part4 INTEGER,
    version INTEGER
    
);
ALTER TABLE ir_statistics.old_ip_address_ignore OWNER TO ir_plus;

-- The field sequence
CREATE SEQUENCE ir_statistics.old_ip_address_ignore_seq ;
ALTER TABLE ir_statistics.old_ip_address_ignore_seq OWNER TO ir_plus;

-- copy over all of the data

INSERT INTO ir_statistics.old_ip_address_ignore (old_ip_address_ignore_id,name,
    store_counts,
    description,
    from_ip_address_part1,
    from_ip_address_part2,
    from_ip_address_part3,
    from_ip_address_part4,
    to_ip_address_part4,
    version)
SELECT nextval('ir_statistics.old_ip_address_ignore_seq'), 
    name,
    store_counts,
    description,
    from_ip_address_part1,
    from_ip_address_part2,
    from_ip_address_part3,
    from_ip_address_part4,
    to_ip_address_part4,
    version 
FROM ir_statistics.ip_address_ignore;

-- ---------------------------------------------
-- DROP and re-create the IP address table
-- ---------------------------------------------
DROP TABLE ir_statistics.ip_address_ignore;
DROP SEQUENCE ir_statistics.ip_address_ignore_seq;

CREATE TABLE ir_statistics.ip_address_ignore
(
    ip_address_ignore_id BIGINT PRIMARY KEY,
    name TEXT,
    store_counts BOOLEAN NOT NULL,
    description TEXT,
    ip_address TEXT NOT NULL UNIQUE,
    version INTEGER
    
);
ALTER TABLE ir_statistics.ip_address_ignore OWNER TO ir_plus;


-- The field sequence
CREATE SEQUENCE ir_statistics.ip_address_ignore_seq;
ALTER TABLE ir_statistics.ip_address_ignore_seq OWNER TO ir_plus;



