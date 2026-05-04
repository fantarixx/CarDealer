CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS models (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    base_price BIGINT NOT NULL,
    engine_type VARCHAR(50) NOT NULL,
    year INTEGER NOT NULL,
    body_type VARCHAR(50) NOT NULL,
    drive_type VARCHAR(50) NOT NULL,
    transmission VARCHAR(50) NOT NULL,
    seats_count INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS components (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS cars (
    id UUID PRIMARY KEY,
    model_id UUID NOT NULL,
    color VARCHAR(50) NOT NULL,
    is_available BOOLEAN NOT NULL,
    available_for_test_drive BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (model_id) REFERENCES models(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    current_state VARCHAR(50) NOT NULL,
    client_id UUID NOT NULL,
    manager_id UUID NOT NULL,
    basic_car_id UUID,
    model_id UUID,
    total_price BIGINT NOT NULL,
    car_color VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    confirmed_at TIMESTAMP,
    paid_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (basic_car_id) REFERENCES cars(id) ON DELETE SET NULL,
    FOREIGN KEY (model_id) REFERENCES models(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS test_drives (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    car_id UUID NOT NULL,
    scheduled_at TIMESTAMP NOT NULL,
    state VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    removed BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS model_standard_components (
    model_id UUID NOT NULL,
    component_type VARCHAR(50) NOT NULL,
    component_id UUID NOT NULL,
    PRIMARY KEY (model_id, component_type),
    FOREIGN KEY (model_id) REFERENCES models(id) ON DELETE CASCADE,
    FOREIGN KEY (component_id) REFERENCES components(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS car_components (
    car_id UUID NOT NULL,
    component_type VARCHAR(50) NOT NULL,
    component_id UUID NOT NULL,
    PRIMARY KEY (car_id, component_type),
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE,
    FOREIGN KEY (component_id) REFERENCES components(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS component_compatible_models (
    component_id UUID NOT NULL,
    model_id UUID NOT NULL,
    PRIMARY KEY (component_id, model_id),
    FOREIGN KEY (component_id) REFERENCES components(id) ON DELETE CASCADE,
    FOREIGN KEY (model_id) REFERENCES models(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_selected_components (
    order_id UUID NOT NULL,
    component_id UUID NOT NULL,
    PRIMARY KEY (order_id, component_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (component_id) REFERENCES components(id) ON DELETE CASCADE
);