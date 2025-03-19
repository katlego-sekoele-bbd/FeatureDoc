---------------------------------------------------------------
-- # HELPER FUNCTION
---------------------------------------------------------------
CREATE OR REPLACE FUNCTION create_role_helper
(
  role_name TEXT,
  role_password TEXT DEFAULT NULL
)
RETURNS void
AS $$
BEGIN
  IF role_password IS NULL THEN
    EXECUTE 'CREATE ROLE ' || quote_ident(role_name);
  ELSE
    EXECUTE 'CREATE ROLE ' || quote_ident(role_name) || ' LOGIN PASSWORD ' || quote_literal(role_password);
  END IF;

  EXCEPTION WHEN duplicate_object
  THEN
    RAISE NOTICE '%. Revoking all privileges from role.', SQLERRM USING ERRCODE = SQLSTATE;
    -- revoke everything to override any grants from previous migrations
    EXECUTE 'REVOKE ALL PRIVILEGES ON DATABASE "${DB_NAME}" FROM ' || quote_ident(role_name);
    EXECUTE 'REVOKE ALL PRIVILEGES ON SCHEMA public FROM ' || quote_ident(role_name);
    EXECUTE 'REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public FROM ' || quote_ident(role_name);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE create_role
(
  role_name TEXT,
  role_password TEXT DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
  PERFORM create_role_helper(role_name => role_name, role_password => role_password);
END;
$$;

-------------------------------------------------------------------------------------------------------

-- ## GROUP
CALL create_role(role_name => 'FeatureDocAdminGroup');
ALTER ROLE "FeatureDocAdminGroup" WITH BYPASSRLS;

-- ### DB LEVEL PERMISSIONS
-- Allow creation of temporary tables on the database
GRANT TEMPORARY
ON DATABASE "${DB_NAME}"
TO "FeatureDocAdminGroup";

-- ### SCHEMA LEVEL PERMISSIONS
-- Allow creation and usage of database objects in the public schema
GRANT CREATE, USAGE
ON SCHEMA public
TO "FeatureDocAdminGroup";

-- ### OBJECT LEVEL PERMISSIONS
-- allow all except deleting on all tables in the public schema
GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES, TRIGGER
ON ALL TABLES IN SCHEMA public
TO "FeatureDocAdminGroup";

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO "FeatureDocAdminGroup";


-- allow all (Execute) on all functions in the schema
GRANT ALL
ON ALL FUNCTIONS IN SCHEMA public
TO "FeatureDocAdminGroup";

-- allow all (Execute) on all procedures in the schema
GRANT ALL
ON ALL PROCEDURES IN SCHEMA public
TO "FeatureDocAdminGroup";

-- allow all (Execute) on all routines in the schema
GRANT ALL
ON ALL ROUTINES IN SCHEMA public
TO "FeatureDocAdminGroup";

-- ## USER
CALL create_role(role_name => 'FeatureDocAdmin', role_password => '${ADMIN_PASS}');

GRANT CONNECT
ON DATABASE "${DB_NAME}"
TO "FeatureDocAdmin";

GRANT "FeatureDocAdminGroup"
TO "FeatureDocAdmin";

-------------------------------------------------------------------------------------------------------

-- ## GROUP
CALL create_role(role_name => 'FeatureDocAppGroup');
ALTER ROLE "FeatureDocAppGroup" WITH BYPASSRLS;

-- ### DB LEVEL PERMISSIONS
-- Allow creation of temporary tables on the database
GRANT TEMPORARY
ON DATABASE "${DB_NAME}"
TO "FeatureDocAppGroup";

-- ### SCHEMA LEVEL PERMISSIONS
-- Allow creation and usage of database objects in the public schema
GRANT CREATE, USAGE
ON SCHEMA public
TO "FeatureDocAppGroup";

-- ### OBJECT LEVEL PERMISSIONS
GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES, TRIGGER
ON ALL TABLES IN SCHEMA public
TO "FeatureDocAppGroup";

GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO "FeatureDocAppGroup";

-- allow all (Execute) on all functions in the schema
GRANT ALL
ON ALL FUNCTIONS IN SCHEMA public
TO "FeatureDocAppGroup";

-- allow all (Execute) on all procedures in the schema
GRANT ALL
ON ALL PROCEDURES IN SCHEMA public
TO "FeatureDocAppGroup";

-- allow all (Execute) on all routines in the schema
GRANT ALL
ON ALL ROUTINES IN SCHEMA public
TO "FeatureDocAppGroup";

-- ## USER
CALL create_role(role_name => 'FeatureDocApp', role_password => '${APP_PASS}');

GRANT CONNECT
ON DATABASE "${DB_NAME}"
TO "FeatureDocApp";

GRANT "FeatureDocAppGroup"
TO "FeatureDocApp";