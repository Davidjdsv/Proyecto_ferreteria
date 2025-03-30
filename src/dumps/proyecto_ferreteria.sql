-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 31-03-2025 a las 00:00:08
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `proyecto_ferreteria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `telefono` varchar(50) NOT NULL,
  `direccion` varchar(50) NOT NULL,
  `correo` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `telefono`, `direccion`, `correo`) VALUES
(1, 'david', '312231', 'calle 23', 'dasd@cas.com'),
(5, 'julian', '3125554355', 'calle32-44', 'juli231@gmail.com'),
(6, 'Rasputin', '31244421', '3123', 'rasp213@jasdj.com'),
(7, 'Carlos Pérez', '3123456789', 'Calle 1', 'carlos.perez@example.com'),
(8, 'Mariana López', '3129876543', 'Avenida Siempre Viva 742', 'mariana.lopez@example.com'),
(9, 'Andrés García', '3125678901', 'Calle Falsa 123', 'andres.garcia@example.com'),
(10, 'Sofía Rodríguez', '3124567890', 'Avenida Central 456', 'sofia.rodriguez@example.com'),
(11, 'Miguel Hernández', '3126789012', 'Calle 2', 'miguel.hernandez@example.com');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `id_empleado` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `cargo` varchar(50) DEFAULT NULL,
  `salario` float(8,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id_empleado`, `nombre`, `cargo`, `salario`) VALUES
(1, 'Alojo', 'Empleado', 350000.00),
(4, 'Jhoan', 'Empleado', 200000.00),
(5, 'Luis Fernández', 'Gerente', 1000000.00),
(6, 'Ana Torres', 'Cajera', 1000000.00),
(7, 'Pedro Gómez', 'Vendedor', 1000000.00),
(8, 'Camila Ramírez', 'Auxiliar de Almacén', 1000000.00),
(9, 'Jorge Castillo', 'Supervisor', 1000000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario_productos`
--

CREATE TABLE `inventario_productos` (
  `id_producto` int(11) NOT NULL,
  `nombre_producto` varchar(50) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `cantidad_stock` int(11) NOT NULL,
  `precio_producto` int(11) NOT NULL,
  `id_proveedor_asociado` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `inventario_productos`
--

INSERT INTO `inventario_productos` (`id_producto`, `nombre_producto`, `categoria`, `cantidad_stock`, `precio_producto`, `id_proveedor_asociado`) VALUES
(2, 'Cristian', 'Herramienta', 40, 500, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ordenes_compra`
--

CREATE TABLE `ordenes_compra` (
  `id_orden_compra` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `id_empleado` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `total` double(8,2) DEFAULT NULL,
  `estado_orden` enum('pendiente','pagada','enviada') NOT NULL,
  `fecha_compra` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedores`
--

CREATE TABLE `proveedores` (
  `id_proveedor` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `contacto` varchar(50) NOT NULL,
  `categoria_producto` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `proveedores`
--

INSERT INTO `proveedores` (`id_proveedor`, `nombre`, `contacto`, `categoria_producto`) VALUES
(1, 'Jhoan David', '12345', 'Herramientas'),
(4, 'Karol Arbelaez', '31123441', 'Herramientas'),
(5, 'Quico', '5671222132', 'Maquinaria'),
(7, 'Suministros Industriales S.A.', '3123456789', 'Herramientas'),
(8, 'ElectroPartes LTDA', '3159876543', 'Materiales Eléctricos'),
(9, 'Maderas y Construcción', '3101122334', 'Madera y Derivados'),
(10, 'FerreMax Distribuidores', '3195566778', 'Tornillos y Fijaciones'),
(11, 'TecnoPlásticos S.R.L.', '3112233445', 'Tubos y Conexiones');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_ventas`
--

CREATE TABLE `registro_ventas` (
  `id_venta` int(11) NOT NULL,
  `id_orden_compra` int(11) NOT NULL,
  `id_producto` int(11) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `sub_total` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`id_empleado`);

--
-- Indices de la tabla `inventario_productos`
--
ALTER TABLE `inventario_productos`
  ADD PRIMARY KEY (`id_producto`),
  ADD KEY `id_proveedor_asociado` (`id_proveedor_asociado`);

--
-- Indices de la tabla `ordenes_compra`
--
ALTER TABLE `ordenes_compra`
  ADD PRIMARY KEY (`id_orden_compra`),
  ADD KEY `id_cliente` (`id_cliente`),
  ADD KEY `id_empleado` (`id_empleado`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD PRIMARY KEY (`id_proveedor`);

--
-- Indices de la tabla `registro_ventas`
--
ALTER TABLE `registro_ventas`
  ADD PRIMARY KEY (`id_venta`),
  ADD KEY `id_orden_compra` (`id_orden_compra`),
  ADD KEY `fk_id_producto` (`id_producto`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id_empleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `inventario_productos`
--
ALTER TABLE `inventario_productos`
  MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `ordenes_compra`
--
ALTER TABLE `ordenes_compra`
  MODIFY `id_orden_compra` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  MODIFY `id_proveedor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `registro_ventas`
--
ALTER TABLE `registro_ventas`
  MODIFY `id_venta` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `inventario_productos`
--
ALTER TABLE `inventario_productos`
  ADD CONSTRAINT `inventario_productos_ibfk_1` FOREIGN KEY (`id_proveedor_asociado`) REFERENCES `proveedores` (`id_proveedor`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `ordenes_compra`
--
ALTER TABLE `ordenes_compra`
  ADD CONSTRAINT `ordenes_compra_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ordenes_compra_ibfk_2` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ordenes_compra_ibfk_3` FOREIGN KEY (`id_producto`) REFERENCES `inventario_productos` (`id_producto`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `registro_ventas`
--
ALTER TABLE `registro_ventas`
  ADD CONSTRAINT `fk_id_producto` FOREIGN KEY (`id_producto`) REFERENCES `inventario_productos` (`id_producto`),
  ADD CONSTRAINT `registro_ventas_ibfk_1` FOREIGN KEY (`id_orden_compra`) REFERENCES `ordenes_compra` (`id_orden_compra`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
