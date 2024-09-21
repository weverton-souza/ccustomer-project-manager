CREATE TABLE "customer"
(
    id         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name       VARCHAR(255) NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    phone      VARCHAR(20),
    password   VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    CONSTRAINT fk_customer_created_by FOREIGN KEY (created_by) REFERENCES "customer" (id),
    CONSTRAINT fk_customer_updated_by FOREIGN KEY (updated_by) REFERENCES "customer" (id)
);

CREATE TABLE "project"
(
    id          UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    status      VARCHAR(50)  NOT NULL,
    customer_id UUID,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    created_by  UUID,
    updated_by  UUID,
    CONSTRAINT fk_project_customer FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT fk_project_created_by FOREIGN KEY (created_by) REFERENCES "customer" (id),
    CONSTRAINT fk_project_updated_by FOREIGN KEY (updated_by) REFERENCES "customer" (id)
);

CREATE TABLE "task"
(
    id          UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    title       VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    status      VARCHAR(50)  NOT NULL,
    customer_id UUID,
    project_id  UUID,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    created_by  UUID,
    updated_by  UUID,
    CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project (id),
    CONSTRAINT fk_task_created_by FOREIGN KEY (created_by) REFERENCES "customer" (id),
    CONSTRAINT fk_task_updated_by FOREIGN KEY (updated_by) REFERENCES "customer" (id)
);

INSERT INTO customer
(id, "name", email, phone, "password", created_at, updated_at, created_by, updated_by)
VALUES('9728dbc7-7c06-489b-a16b-0eb60d39b806'::uuid, 'Administrator', 'root-user@example.com', '+1234567890', '$2a$10$0c8.7BPq6RVtf9zWqh1Dte4E1S57U5QLm5LcKxorNr4BORN3m/6cu', NULL, NULL, NULL, NULL);