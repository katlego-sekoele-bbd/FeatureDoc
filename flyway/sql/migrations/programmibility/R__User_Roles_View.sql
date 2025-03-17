CREATE OR REPLACE VIEW UserRoleView AS
SELECT 
    u."UserID",
    r."RoleID",
    u."Name",
    u."Email",
    r."RoleName"
FROM 
    "User" u
JOIN 
    "UserRole" ur ON u."UserID" = ur."UserID"
JOIN 
    "Role" r ON ur."RoleID" = r."RoleID";