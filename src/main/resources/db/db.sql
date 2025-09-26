-- Verificar si la base de datos 'prog3_sistema' ya existe
CREATE DATABASE IF NOT EXISTS prog3_sistema;

-- Usar la base de datos recién creada o existente
USE prog3_sistema;

-- Crear la tabla 'usuarios'
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    activo TINYINT DEFAULT 1
);