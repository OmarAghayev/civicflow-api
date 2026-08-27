-- V2: Seed initial test records for verification
INSERT INTO citizens (citizen_pin, full_name, email, phone_number, created_at)
VALUES 
    ('99AA123', 'Aysel Aliyeva', 'aysel.aliyeva@example.gov', '+994501234567', CURRENT_TIMESTAMP),
    ('99BB456', 'Rashad Mammadov', 'rashad.mammadov@example.gov', '+994509876543', CURRENT_TIMESTAMP);

INSERT INTO permit_applications (tracking_number, permit_type, status, citizen_id, created_at)
VALUES 
    ('CV-1001AABB', 'BUILDING_RENOVATION', 'SUBMITTED', 1, CURRENT_TIMESTAMP),
    ('CV-2002CCDD', 'SOLAR_PANEL_INSTALLATION', 'APPROVED', 2, CURRENT_TIMESTAMP);