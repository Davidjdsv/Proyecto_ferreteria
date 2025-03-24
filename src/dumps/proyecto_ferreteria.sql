CREATE TABLE clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    direccion VARCHAR(50) NOT NULL
);

CREATE TABLE empleados (
    id_empleado INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    cargo ENUM('admin', 'vendedor') NOT NULL,
    salario FLOAT(8,2) NOT NULL
);

CREATE TABLE proveedores (
    id_proveedor INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    contacto VARCHAR(50) NOT NULL,
    categoria_producto VARCHAR(50) NOT NULL
);

CREATE TABLE inventario_productos (
    id_producto INT PRIMARY KEY AUTO_INCREMENT,
    nombre_producto VARCHAR(50) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    cantidad_stock INT NOT NULL,
    precio_producto INT NOT NULL,
    id_proveedor_asociado INT,
    FOREIGN KEY (id_proveedor_asociado) REFERENCES proveedores(id_proveedor) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE ordenes_compra (
    id_orden_compra INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    id_empleado INT NOT NULL,
    id_producto INT NOT NULL,
    estado_orden ENUM('pendiente', 'pagada', 'enviada') NOT NULL,
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES inventario_productos(id_producto) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE registro_ventas (
    id_venta INT PRIMARY KEY AUTO_INCREMENT,
    id_orden_compra INT NOT NULL,
    total INT NOT NULL,
    FOREIGN KEY (id_orden_compra) REFERENCES ordenes_compra(id_orden_compra) ON DELETE CASCADE ON UPDATE CASCADE
);
