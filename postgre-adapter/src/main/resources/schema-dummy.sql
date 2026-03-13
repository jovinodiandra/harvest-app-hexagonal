-- Dummy data for Harvest / Ternak Lele
-- Run after schema.sql. Uses fixed UUIDs for reproducibility.

-- Organization
INSERT INTO organization (id, name) VALUES
  ('a0000000-0000-0000-0000-000000000001', 'Ternak Lele Maju'),
  ('a0000000-0000-0000-0000-000000000002', 'Kolam Sejahtera');

-- Users (password: password123 - hash as bcrypt or plain for dummy)
INSERT INTO harvest_user (id, name, email, password, organization_id, role) VALUES
  ('b0000000-0000-0000-0000-000000000001', 'Admin Satu', 'admin1@example.com', 'password123', 'a0000000-0000-0000-0000-000000000001', 'owner'),
  ('b0000000-0000-0000-0000-000000000002', 'User Dua', 'user2@example.com', 'password123', 'a0000000-0000-0000-0000-000000000001', 'user');

-- Ponds
INSERT INTO ponds (id, name, capacity, location, organization_id) VALUES
  ('c0000000-0000-0000-0000-000000000001', 'Kolam A', 1000, 'Blok 1', 'a0000000-0000-0000-0000-000000000001'),
  ('c0000000-0000-0000-0000-000000000002', 'Kolam B', 800, 'Blok 2', 'a0000000-0000-0000-0000-000000000001');

-- Seed
INSERT INTO seed (id, ponds_id, type, quantity, stock_date, organization_id) VALUES
  ('d0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'Lele Sangkuriang', 500, '2025-01-15', 'a0000000-0000-0000-0000-000000000001'),
  ('d0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000002', 'Lele Phyton', 400, '2025-02-01', 'a0000000-0000-0000-0000-000000000001');

-- Feed schedule
INSERT INTO feed_schedule (id, ponds_id, feed_type, feed_amount, feed_time, organization_id) VALUES
  ('e0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'Pelet 781-2', 2.5, '08:00:00', 'a0000000-0000-0000-0000-000000000001'),
  ('e0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000001', 'Pelet 781-2', 2.5, '17:00:00', 'a0000000-0000-0000-0000-000000000001');

-- Growth record
INSERT INTO growth_record (id, ponds_id, record_date, average_width, average_length, organization_id) VALUES
  ('f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', '2025-02-15', 2.5, 12.0, 'a0000000-0000-0000-0000-000000000001'),
  ('f0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000001', '2025-03-01', 3.0, 15.0, 'a0000000-0000-0000-0000-000000000001');

-- Death record
INSERT INTO death_record (id, ponds_id, record_date, death_count, notes, organization_id) VALUES
  ('g0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', '2025-02-10', 5, 'Saat grading', 'a0000000-0000-0000-0000-000000000001');

-- Watter quality
INSERT INTO watter_quality (id, ponds_id, record_date, ph, temperature, dissolved_oxygen, organization_id) VALUES
  ('h0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', '2025-03-01', 7.2, 28.5, 5.0, 'a0000000-0000-0000-0000-000000000001');

-- Diseases record
INSERT INTO diseases_record (id, ponds_id, disease_name, symptoms, infected_fish_count, diseases_date, notes, organization_id) VALUES
  ('i0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'Bintik putih', 'Bintik putih di tubuh', 10, '2025-02-20', 'Sudah diberi obat', 'a0000000-0000-0000-0000-000000000001');

-- Harvest record
INSERT INTO harvest_record (id, ponds_id, ponds_name, harvest_date, organization_id, harvest_fish_count, total_weight, notes) VALUES
  ('j0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'Kolam A', '2025-03-10', 'a0000000-0000-0000-0000-000000000001', 450, 225.5, 'Panen perdana');

-- Finance record
INSERT INTO finance_record (id, transaction_type, transaction_date, amount, category, notes, organization_id) VALUES
  ('k0000000-0000-0000-0000-000000000001', 'expense', '2025-01-10', 5000000, 'Bibit', 'Beli bibit lele', 'a0000000-0000-0000-0000-000000000001'),
  ('k0000000-0000-0000-0000-000000000002', 'income', '2025-03-10', 15000000, 'Penjualan', 'Hasil panen Kolam A', 'a0000000-0000-0000-0000-000000000001');

-- Supplier
INSERT INTO supplier (id, name, address, phone, email, notes, organization_id) VALUES
  ('l0000000-0000-0000-0000-000000000001', 'Toko Pakan Ternak Jaya', 'Jl. Raya Pakan No. 1', '08123456789', 'pakan@example.com', 'Supplier pakan', 'a0000000-0000-0000-0000-000000000001');

-- Contact
INSERT INTO contact (id, supplier_id, name, address, phone, email, notes) VALUES
  ('m0000000-0000-0000-0000-000000000001', 'l0000000-0000-0000-0000-000000000001', 'Budi Sales', 'Jl. Raya Pakan No. 1', '08123456780', 'budi@example.com', 'Sales utama');

-- Feed reminder
INSERT INTO feed_reminder (id, ponds_id, reminder_date, reminder_time, feed_type, notes, organization_id) VALUES
  ('n0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', '2025-03-13', '08:00:00', 'Pelet 781-2', 'Pagi', 'a0000000-0000-0000-0000-000000000001');

-- Harvest reminder
INSERT INTO harvest_reminder (id, pond_id, reminder_date, reminder_time, notes, organization_id) VALUES
  ('o0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000002', '2025-04-01', '06:00:00', 'Jadwal panen Kolam B', 'a0000000-0000-0000-0000-000000000001');
