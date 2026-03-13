-- Harvest / Ternak Lele - PostgreSQL Schema
-- Run this script to create tables and indexes.

-- ============== ENUMS ==============
CREATE TYPE harvest_role AS ENUM ('owner', 'admin', 'user');
CREATE TYPE transaction_type_enum AS ENUM ('income', 'expense');

-- ============== TABLES ==============

CREATE TABLE organization (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE harvest_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    role harvest_role NOT NULL DEFAULT 'user',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(email)
);

CREATE TABLE ponds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(500),
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE seed (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ponds_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    type VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    stock_date DATE NOT NULL,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE feed_schedule (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ponds_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    feed_type VARCHAR(255) NOT NULL,
    feed_amount DECIMAL(20, 4) NOT NULL,
    feed_time TIME NOT NULL,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE growth_record (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ponds_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    record_date DATE NOT NULL,
    average_width DECIMAL(20, 4),
    average_length DECIMAL(20, 4),
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE death_record (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ponds_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    record_date DATE NOT NULL,
    death_count INT NOT NULL DEFAULT 0,
    notes TEXT,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE watter_quality (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ponds_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    record_date DATE NOT NULL,
    ph DECIMAL(5, 2),
    temperature DECIMAL(5, 2),
    dissolved_oxygen DECIMAL(5, 2),
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE diseases_record (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ponds_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    disease_name VARCHAR(255) NOT NULL,
    symptoms TEXT,
    infected_fish_count INT NOT NULL DEFAULT 0,
    diseases_date DATE NOT NULL,
    notes TEXT,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE harvest_record (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ponds_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    ponds_name VARCHAR(255) NOT NULL,
    harvest_date DATE NOT NULL,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    harvest_fish_count INT NOT NULL DEFAULT 0,
    total_weight DECIMAL(20, 4),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE finance_record (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_type transaction_type_enum NOT NULL,
    transaction_date DATE NOT NULL,
    amount DECIMAL(20, 4) NOT NULL,
    category VARCHAR(255) NOT NULL,
    notes TEXT,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE supplier (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    phone VARCHAR(50),
    email VARCHAR(255),
    notes TEXT,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contact (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    supplier_id UUID NOT NULL REFERENCES supplier(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    phone VARCHAR(50),
    email VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE feed_reminder (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ponds_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    reminder_date DATE NOT NULL,
    reminder_time TIME NOT NULL,
    feed_type VARCHAR(255),
    notes TEXT,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE harvest_reminder (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pond_id UUID NOT NULL REFERENCES ponds(id) ON DELETE CASCADE,
    reminder_date DATE NOT NULL,
    reminder_time TIME NOT NULL,
    notes TEXT,
    organization_id UUID NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ============== INDEXES ==============

-- organization
CREATE INDEX idx_organization_name ON organization(name);

-- harvest_user
CREATE UNIQUE INDEX idx_harvest_user_email ON harvest_user(email);
CREATE INDEX idx_harvest_user_organization_id ON harvest_user(organization_id);

-- ponds
CREATE INDEX idx_ponds_organization_id ON ponds(organization_id);
CREATE INDEX idx_ponds_name ON ponds(name);

-- seed
CREATE INDEX idx_seed_ponds_id ON seed(ponds_id);
CREATE INDEX idx_seed_organization_id ON seed(organization_id);
CREATE INDEX idx_seed_stock_date ON seed(stock_date);

-- feed_schedule
CREATE INDEX idx_feed_schedule_ponds_id ON feed_schedule(ponds_id);
CREATE INDEX idx_feed_schedule_organization_id ON feed_schedule(organization_id);

-- growth_record
CREATE INDEX idx_growth_record_ponds_id ON growth_record(ponds_id);
CREATE INDEX idx_growth_record_organization_id ON growth_record(organization_id);
CREATE INDEX idx_growth_record_record_date ON growth_record(record_date);

-- death_record
CREATE INDEX idx_death_record_ponds_id ON death_record(ponds_id);
CREATE INDEX idx_death_record_organization_id ON death_record(organization_id);
CREATE INDEX idx_death_record_record_date ON death_record(record_date);

-- watter_quality
CREATE INDEX idx_watter_quality_ponds_id ON watter_quality(ponds_id);
CREATE INDEX idx_watter_quality_organization_id ON watter_quality(organization_id);
CREATE INDEX idx_watter_quality_record_date ON watter_quality(record_date);

-- diseases_record
CREATE INDEX idx_diseases_record_ponds_id ON diseases_record(ponds_id);
CREATE INDEX idx_diseases_record_organization_id ON diseases_record(organization_id);
CREATE INDEX idx_diseases_record_diseases_date ON diseases_record(diseases_date);

-- harvest_record
CREATE INDEX idx_harvest_record_ponds_id ON harvest_record(ponds_id);
CREATE INDEX idx_harvest_record_organization_id ON harvest_record(organization_id);
CREATE INDEX idx_harvest_record_harvest_date ON harvest_record(harvest_date);

-- finance_record
CREATE INDEX idx_finance_record_organization_id ON finance_record(organization_id);
CREATE INDEX idx_finance_record_transaction_date ON finance_record(transaction_date);
CREATE INDEX idx_finance_record_transaction_type ON finance_record(transaction_type);

-- supplier
CREATE INDEX idx_supplier_organization_id ON supplier(organization_id);
CREATE INDEX idx_supplier_name ON supplier(name);

-- contact
CREATE INDEX idx_contact_supplier_id ON contact(supplier_id);
CREATE INDEX idx_contact_name ON contact(name);

-- feed_reminder
CREATE INDEX idx_feed_reminder_ponds_id ON feed_reminder(ponds_id);
CREATE INDEX idx_feed_reminder_organization_id ON feed_reminder(organization_id);
CREATE INDEX idx_feed_reminder_reminder_date ON feed_reminder(reminder_date);

-- harvest_reminder
CREATE INDEX idx_harvest_reminder_pond_id ON harvest_reminder(pond_id);
CREATE INDEX idx_harvest_reminder_organization_id ON harvest_reminder(organization_id);
CREATE INDEX idx_harvest_reminder_reminder_date ON harvest_reminder(reminder_date);
