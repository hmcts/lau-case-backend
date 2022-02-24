  -- alter role for application access
  DO
  $do$
  BEGIN
     IF NOT EXISTS (
        SELECT FROM pg_catalog.pg_roles  -- SELECT list can be empty for this
        WHERE  rolname = 'lauuser') THEN
        ALTER ROLE lauuser LOGIN PASSWORD '${LAU_DB_PASSWORD}';
     END IF;
  END
  $do$;
