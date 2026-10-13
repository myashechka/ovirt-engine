CREATE TABLE groups (
    group_id UUID NOT NULL,
    group_name VARCHAR(255) NOT NULL,
    group_description VARCHAR(4000),
    CONSTRAINT pk_groups PRIMARY KEY (group_id)
);
