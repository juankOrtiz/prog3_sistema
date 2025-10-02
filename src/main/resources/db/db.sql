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

CREATE TABLE cuentas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    saldo DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE movimientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cuenta_origen_id INT NOT NULL,
    cuenta_destino_id INT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL, -- Ej: 'TRANSFERENCIA', 'DEPOSITO', 'RETIRO'
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Restricciones de Claves Foráneas
    FOREIGN KEY (cuenta_origen_id) REFERENCES cuentas(id) ON DELETE RESTRICT,
    FOREIGN KEY (cuenta_destino_id) REFERENCES cuentas(id) ON DELETE RESTRICT
);

INSERT INTO cuentas (usuario_id, descripcion, saldo)
VALUES (1, 'Cuenta Principal', 1000.00), (1, 'Cuenta de Ahorros', 500.00), (2, 'Cuenta de Inversiones', 2500.00);