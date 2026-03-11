-- ==============================
-- HTD GYM DATABASE FULL
-- ==============================

DROP DATABASE IF EXISTS htd_gym;
CREATE DATABASE htd_gym CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE htd_gym;

-- ==============================
-- TABLE USERS
-- ==============================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    gender ENUM('male','female','other'),
    date_of_birth DATE,
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    profile_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================
-- TABLE MEMBERS
-- ==============================
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    membership_type VARCHAR(50),
    start_date DATE,
    end_date DATE,
    status ENUM('active','inactive','expired') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================
-- TABLE WORKOUTS
-- ==============================
CREATE TABLE workouts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    description TEXT,
    duration INT,
    calories INT,
    difficulty ENUM('beginner','intermediate','advanced'),
    image_url VARCHAR(255),
    video_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================
-- TABLE EXERCISES
-- ==============================
CREATE TABLE exercises (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_id INT,
    name VARCHAR(100) NOT NULL,
    sets VARCHAR(50),
    reps VARCHAR(50),
    description TEXT,
    image_url VARCHAR(255),
    video_url VARCHAR(255),
    order_index INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

-- ==============================
-- TABLE USER STATS
-- ==============================
CREATE TABLE user_stats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    date DATE NOT NULL,
    calories_burned INT DEFAULT 0,
    workout_duration INT DEFAULT 0,
    workouts_completed INT DEFAULT 0,
    steps INT DEFAULT 0,
    water_intake INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_date (user_id, date)
);

-- ==============================
-- TABLE WORKOUT SESSIONS
-- ==============================
CREATE TABLE workout_sessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    workout_id INT,
    start_time DATETIME,
    end_time DATETIME,
    duration INT,
    calories_burned INT,
    status ENUM('in_progress','completed','cancelled') DEFAULT 'in_progress',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (workout_id) REFERENCES workouts(id)
);

-- ==============================
-- TABLE PAYMENTS
-- ==============================
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT,
    amount DECIMAL(10,2),
    payment_date DATE,
    payment_method VARCHAR(50),
    description TEXT,
    status ENUM('pending','completed','cancelled') DEFAULT 'completed',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- ==============================
-- TABLE EQUIPMENT
-- ==============================
CREATE TABLE equipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(50),
    quantity INT DEFAULT 0,
    condition_status ENUM('good','fair','poor','broken') DEFAULT 'good',
    purchase_date DATE,
    last_maintenance DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================
-- TABLE VIDEOS
-- ==============================
CREATE TABLE videos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    description TEXT,
    category VARCHAR(50),
    duration INT,
    thumbnail_url VARCHAR(255),
    video_url VARCHAR(255),
    views INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================
-- TABLE TRAINERS
-- ==============================
CREATE TABLE trainers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    specialization VARCHAR(100),
    experience_years INT,
    bio TEXT,
    image_url VARCHAR(255),
    status ENUM('active','inactive') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================
-- SAMPLE DATA
-- ==============================

-- Admin user (password: 123456)
INSERT INTO users (username,email,password,full_name)
VALUES ('admin','admin@htdgym.com','$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','Admin HTD GYM');

-- Workouts
INSERT INTO workouts (name,category,description,duration,calories,difficulty) VALUES
('Ngực','chest','Tập ngực toàn diện',35,280,'intermediate'),
('Vai','shoulder','Phát triển vai',30,240,'intermediate'),
('Chân','legs','Tăng sức mạnh chân',45,350,'advanced'),
('Bụng','abs','Săn chắc bụng',25,200,'beginner'),
('Lưng','back','Lưng chữ V',35,270,'intermediate');

-- Exercises
INSERT INTO exercises (workout_id,name,sets,reps,description,order_index) VALUES
(1,'Push-up','4x15','15','Hít đất cơ bản',1),
(1,'Incline Push-up','3x12','12','Hít đất nghiêng',2),
(2,'Shoulder Press','4x10','10','Đẩy vai',1),
(3,'Squat','4x12','12','Gánh tạ squat',1),
(4,'Plank','3x30s','30s','Plank bụng',1);