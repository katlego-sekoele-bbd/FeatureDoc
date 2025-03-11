-- Create the User table
CREATE TABLE "User" (
    "UserID" SERIAL PRIMARY KEY,
    "Name" VARCHAR(255) NOT NULL,
    "Email" VARCHAR(254) UNIQUE NOT NULL
);

-- Create the Role table
CREATE TABLE "Role" (
    "RoleID" SERIAL PRIMARY KEY,
    "RoleName" VARCHAR(255) NOT NULL
);

-- Create the UserRole table (many-to-many relationship between User and Role)
CREATE TABLE "UserRole" (
    "RoleID" INT NOT NULL,
    "UserID" INT NOT NULL,
    PRIMARY KEY ("RoleID", "UserID"),
    FOREIGN KEY ("RoleID") REFERENCES "Role"("RoleID") ON DELETE CASCADE,
    FOREIGN KEY ("UserID") REFERENCES "User"("UserID") ON DELETE CASCADE
);

-- Create the Feature table
CREATE TABLE "Feature" (
    "FeatureID" SERIAL PRIMARY KEY,
    "CreatedBy" INT NOT NULL,
    "CreatedAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ("CreatedBy") REFERENCES "User"("UserID")
);

-- Create the Priority table
CREATE TABLE "Priority" (
    "PriorityID" SERIAL PRIMARY KEY,
    "Description" VARCHAR(255) NOT NULL
);

-- Create the FeatureStatus table
CREATE TABLE "FeatureStatus" (
    "FeatureStatusID" SERIAL PRIMARY KEY,
    "Description" VARCHAR(225) NOT NULL
);

-- Create the FeatureVersion table
CREATE TABLE "FeatureVersion" (
    "FeatureVersionID" SERIAL PRIMARY KEY,
    "UpdateBy" INT NOT NULL,
    "FeatureID" INT NOT NULL,
    "FeatureStatusID" INT NOT NULL,
    "PriorityID" INT NOT NULL,
    "AssignedTo" INT NOT NULL,
    "Name" VARCHAR(255) NOT NULL,
    "ShortDescription" VARCHAR(255),
    "UpdatedDate" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    "DeletedDate" TIMESTAMP NULL,
    "URL" VARCHAR(225) NOT NULL,
    FOREIGN KEY ("UpdateBy") REFERENCES "User"("UserID"),
    FOREIGN KEY ("FeatureID") REFERENCES "Feature"("FeatureID") ON DELETE CASCADE,
    FOREIGN KEY ("FeatureStatusID") REFERENCES "FeatureStatus"("FeatureStatusID"),
    FOREIGN KEY ("PriorityID") REFERENCES "Priority"("PriorityID"),
    FOREIGN KEY ("AssignedTo") REFERENCES "User"("UserID")
);
