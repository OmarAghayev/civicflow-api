-- V3: Advanced Performance Tuning & Composite Indexes

-- 1. Composite Index for Status & Date Sorting (Optimizes Municipal Dashboard queries)
CREATE INDEX idx_permits_status_created ON permit_applications(status, created_at DESC);

-- 2. Foreign Key Join Optimization Index
CREATE INDEX idx_permits_citizen_fk ON permit_applications(citizen_id);

-- 3. Case-Insensitive Email Index for Citizen Search
CREATE INDEX idx_citizens_email_lower ON citizens(LOWER(email));