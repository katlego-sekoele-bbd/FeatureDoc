
ALTER TABLE "Role"
ADD CONSTRAINT "Role_RoleName_Unique" UNIQUE ("RoleName");


ALTER TABLE "Priority"
ADD CONSTRAINT "Priority_Description_Unique" UNIQUE ("Description");


ALTER TABLE "FeatureStatus"
ADD CONSTRAINT "FeatureStatus_Description_Unique" UNIQUE ("Description");


ALTER TABLE "FeatureVersion"
ALTER COLUMN "URL" DROP NOT NULL;


ALTER TABLE "FeatureVersion"
ALTER COLUMN "AssignedTo" DROP NOT NULL;


ALTER TABLE "UserRole"
ADD CONSTRAINT "UserRole_RoleID_UserID_Unique" UNIQUE ("RoleID", "UserID");