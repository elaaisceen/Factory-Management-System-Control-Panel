-- Factory Management System Database Schema
-- Standardizing structures for IT, HR, Finance, Stock, Production, and Purchasing

-- 1. Personnel Table (Human Resources)
CREATE TABLE personnel (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dept VARCHAR(100),
    expert VARCHAR(100),
    status VARCHAR(50) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. IT Support Tickets
CREATE TABLE it_support_tickets (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    requester VARCHAR(255),
    dept VARCHAR(100),
    priority VARCHAR(50),
    status VARCHAR(50) DEFAULT 'Beklemede',
    task_status VARCHAR(50) DEFAULT '0',
    assignee VARCHAR(255),
    description TEXT,
    rejection_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Production Operations
CREATE TABLE production_database (
    id BIGINT PRIMARY KEY,
    sub_dept VARCHAR(100), -- montaj, paketleme, vb.
    name VARCHAR(255), -- Makine/Istasyon Adi
    status VARCHAR(50), -- Aktif, Pasif, Bakimda
    performance INT, -- 0-100
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Finance Transactions
CREATE TABLE finance_database (
    id VARCHAR(50) PRIMARY KEY, -- TXN-XXXXX
    company VARCHAR(255),
    detail TEXT,
    amount DECIMAL(15, 2),
    status VARCHAR(50), -- Ödendi, İşleniyor, Gecikmiş
    transaction_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Stock Inventory
CREATE TABLE stock_database (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    sku VARCHAR(100),
    qty DECIMAL(15, 2),
    unit VARCHAR(50),
    status VARCHAR(50), -- Sağlıklı, Sipariş Verilmeli, Kritik Düşük
    dept VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Purchasing Orders
CREATE TABLE purchasing_database (
    id VARCHAR(50) PRIMARY KEY, -- PO-XXXX
    supplier VARCHAR(255),
    item VARCHAR(255),
    qty DECIMAL(15, 2),
    status VARCHAR(50), -- Pending, On Way, Quality Inspection
    eta VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. System Activity Logs
CREATE TABLE system_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255),
    action VARCHAR(255),
    location VARCHAR(255),
    status VARCHAR(50),
    log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
