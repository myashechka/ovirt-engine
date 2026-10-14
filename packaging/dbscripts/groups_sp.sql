CREATE OR REPLACE FUNCTION InsertGroup (
    v_group_id UUID,
    v_group_name VARCHAR(255),
    v_group_description VARCHAR(4000)
    )
RETURNS VOID AS $FUNCTION$
BEGIN
    INSERT INTO groups (
        group_id,
        group_name,
        group_description
        )
    VALUES (
        v_group_id,
        v_group_name,
        v_group_description
        );
END;$FUNCTION$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION UpdateGroup (
    v_group_id UUID,
    v_group_name VARCHAR(255),
    v_group_description VARCHAR(4000)
    )
RETURNS VOID AS $FUNCTION$
BEGIN
    UPDATE groups
    SET group_name = v_group_name,
        group_description = v_group_description
    WHERE group_id = v_group_id;
END;$FUNCTION$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION DeleteGroup (v_group_id UUID)
RETURNS VOID AS $FUNCTION$
BEGIN
    DELETE FROM groups
    WHERE group_id = v_group_id;
END;$FUNCTION$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION GetGroupById (v_group_id UUID)
RETURNS SETOF groups STABLE AS $FUNCTION$
BEGIN
    RETURN QUERY

    SELECT groups.*
    FROM groups
    WHERE group_id = v_group_id;
END;$FUNCTION$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION GetGroupByName (v_group_name VARCHAR(255))
RETURNS SETOF groups STABLE AS $FUNCTION$
BEGIN
    RETURN QUERY

    SELECT groups.*
    FROM groups
    WHERE group_name = v_group_name;
END;$FUNCTION$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION GetAllGroups ()
RETURNS SETOF groups STABLE AS $FUNCTION$
BEGIN
    RETURN QUERY

    SELECT groups.*
    FROM groups
    ORDER BY group_name;
END;$FUNCTION$
LANGUAGE plpgsql;
