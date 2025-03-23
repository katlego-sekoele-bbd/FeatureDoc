CREATE UNIQUE INDEX "FeatureStatus_Description_Unique_Index"
ON "FeatureStatus" (LOWER("Description"));

CREATE UNIQUE INDEX "Role_RoleName_Unique_Index"
ON "Role" (LOWER("RoleName"));

CREATE UNIQUE INDEX "Priority_Description_Unique_Index"
ON "Priority" (LOWER("Description"));