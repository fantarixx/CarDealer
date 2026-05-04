INSERT INTO users (id, user_name, role, created_at, updated_at, removed) VALUES
    (gen_random_uuid(), 'client', 'CLIENT', now(), now(), false),
    (gen_random_uuid(), 'manager', 'SALON_MANAGER', now(), now(), false),
    (gen_random_uuid(), 'warehouse_admin', 'WAREHOUSE_ADMIN', now(), now(), false),
    (gen_random_uuid(), 'system_admin', 'SYSTEM_ADMIN', now(), now(), false);

INSERT INTO models (id, name, brand, base_price, engine_type, year, body_type, drive_type, transmission, seats_count, created_at, updated_at, removed) VALUES
    (gen_random_uuid(), '320i', 'BMW', 3000000, 'GASOLINE', 2023, 'SEDAN', 'RWD', 'AUTOMATIC_8', 5, now(), now(), false);

INSERT INTO components (id, type, name, price, created_at, updated_at, removed) VALUES
    (gen_random_uuid(), 'WHEELS', '17" Standard', 0, now(), now(), false),
    (gen_random_uuid(), 'WHEELS', '19" M-Sport', 95000, now(), now(), false),
    (gen_random_uuid(), 'WHEELS', '18" Aero', 45000, now(), now(), false),
    (gen_random_uuid(), 'TRANSMISSION', 'Automatic 8AT', 0, now(), now(), false),
    (gen_random_uuid(), 'TRANSMISSION', 'Manual 6MT', 30000, now(), now(), false),
    (gen_random_uuid(), 'INTERIOR', 'Fabric Graphite', 0, now(), now(), false),
    (gen_random_uuid(), 'INTERIOR', 'Leather Dakota', 110000, now(), now(), false),
    (gen_random_uuid(), 'INTERIOR', 'Sport Performance', 160000, now(), now(), false),
    (gen_random_uuid(), 'WHEELS', 'Sport Leather', 0, now(), now(), false),
    (gen_random_uuid(), 'WHEELS', 'M-Sport Heated', 25000, now(), now(), false);


INSERT INTO model_standard_components (model_id, component_type, component_id)
SELECT m.id, 'WHEELS', c.id FROM models m CROSS JOIN components c WHERE m.name = '320i' AND c.name = '17" Standard' AND c.type = 'WHEELS'
UNION
SELECT m.id, 'TRANSMISSION', c.id FROM models m CROSS JOIN components c WHERE m.name = '320i' AND c.name = 'Automatic 8AT' AND c.type = 'TRANSMISSION'
UNION
SELECT m.id, 'INTERIOR', c.id FROM models m CROSS JOIN components c WHERE m.name = '320i' AND c.name = 'Fabric Graphite' AND c.type = 'INTERIOR'
UNION
SELECT m.id, 'WHEELS', c.id FROM models m CROSS JOIN components c WHERE m.name = '320i' AND c.name = 'Sport Leather' AND c.type = 'STEERING_WHEEL';


INSERT INTO component_compatible_models (component_id, model_id)
SELECT c.id, m.id FROM components c, models m WHERE c.name = '19" M-Sport' AND m.name IN ('320i', '330i', 'M340i')
UNION
SELECT c.id, m.id FROM components c, models m WHERE c.name = '18" Aero' AND m.name IN ('320i', '330i')
UNION
SELECT c.id, m.id FROM components c, models m WHERE c.name = 'Manual 6MT' AND m.name IN ('320i', '330i')
UNION
SELECT c.id, m.id FROM components c, models m WHERE c.name = 'Leather Dakota' AND m.name IN ('320i', '330i')
UNION
SELECT c.id, m.id FROM components c, models m WHERE c.name = 'Sport Performance' AND m.name IN ('330i', 'M340i')
UNION
SELECT c.id, m.id FROM components c, models m WHERE c.name = 'M-Sport Heated' AND m.name IN ('320i', '330i', 'M340i');