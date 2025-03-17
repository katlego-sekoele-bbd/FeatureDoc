CREATE OR REPLACE VIEW "FeatureView" AS
SELECT 
    fv."Name" AS "FeatureName",
    u1."Name" AS "CreatedBy",
    f."CreatedAt",
    f."FeatureID",
    fv."FeatureVersionID",
    u2."Name" AS "UpdateBy",
    fs."Description" AS "FeatureStatus",
    p."Description" AS "Priority",
    u3."Name" AS "AssignedTo",
    fv."Name",
    fv."ShortDescription",
    fv."URL",
    fv."UpdatedDate",
    fv."DeletedDate"
FROM 
    "Feature" f
INNER JOIN "FeatureVersion" fv ON f."FeatureID" = fv."FeatureID"
INNER JOIN "User" u1 ON f."CreatedBy" = u1."UserID" 
INNER JOIN "User" u2 ON fv."UpdateBy" = u2."UserID"  
LEFT JOIN "User" u3 ON fv."AssignedTo" = u3."UserID"
INNER JOIN "FeatureStatus" fs ON fv."FeatureStatusID" = fs."FeatureStatusID"
INNER JOIN "Priority" p ON fv."PriorityID" = p."PriorityID";
