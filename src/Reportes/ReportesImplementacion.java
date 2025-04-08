package Reportes;

import Conexion.ConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportesImplementacion {
    private Connection conexion;
    private JTable reportesTable;
    private DefaultTableModel tableModel;

    public ReportesImplementacion(Connection conexion, JTable reportesTable, DefaultTableModel tableModel) {
        this.conexion = conexion;
        this.reportesTable = reportesTable;
        this.tableModel = tableModel;
    }

    // Método para generar reporte de ventas por periodo
    public void generarReporteVentasPorPeriodo(String periodo) {
        tableModel.setRowCount(0);

        // Configurar columnas específicas para este reporte
        configurarColumnas(new String[]{"Fecha", "Total Ventas", "Cantidad de Órdenes"});

        try {
            String sql = "";
            String groupBy = "";
            String dateFormat = "";

            switch (periodo.toLowerCase()) {
                case "diario":
                    sql = "SELECT DATE(fecha_compra) as fecha, SUM(total) as total_ventas, COUNT(*) as num_ordenes " +
                            "FROM ordenes_compra " +
                            "WHERE fecha_compra >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY) " +
                            "GROUP BY DATE(fecha_compra) " +
                            "ORDER BY fecha DESC";
                    break;
                case "semanal":
                    sql = "SELECT YEARWEEK(fecha_compra, 1) as semana, " +
                            "CONCAT('Semana ', WEEK(fecha_compra, 1), ' - ', YEAR(fecha_compra)) as periodo, " +
                            "SUM(total) as total_ventas, COUNT(*) as num_ordenes " +
                            "FROM ordenes_compra " +
                            "WHERE fecha_compra >= DATE_SUB(CURRENT_DATE(), INTERVAL 12 WEEK) " +
                            "GROUP BY YEARWEEK(fecha_compra, 1) " +
                            "ORDER BY semana DESC";
                    break;
                case "mensual":
                    sql = "SELECT CONCAT(YEAR(fecha_compra), '-', MONTH(fecha_compra)) as mes, " +
                            "CONCAT(MONTHNAME(fecha_compra), ' ', YEAR(fecha_compra)) as periodo, " +
                            "SUM(total) as total_ventas, COUNT(*) as num_ordenes " +
                            "FROM ordenes_compra " +
                            "WHERE fecha_compra >= DATE_SUB(CURRENT_DATE(), INTERVAL 12 MONTH) " +
                            "GROUP BY YEAR(fecha_compra), MONTH(fecha_compra) " +
                            "ORDER BY YEAR(fecha_compra) DESC, MONTH(fecha_compra) DESC";
                    break;
                default:
                    throw new IllegalArgumentException("Periodo no válido: " + periodo);
            }

            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row;
                if (periodo.equalsIgnoreCase("diario")) {
                    row = new Object[]{
                            rs.getString("fecha"),
                            String.format("$%.2f", rs.getDouble("total_ventas")),
                            rs.getInt("num_ordenes")
                    };
                } else {
                    row = new Object[]{
                            rs.getString("periodo"),
                            String.format("$%.2f", rs.getDouble("total_ventas")),
                            rs.getInt("num_ordenes")
                    };
                }
                tableModel.addRow(row);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte de ventas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para generar reporte de productos más vendidos
    public void generarReporteProductosMasVendidos(int limite) {
        tableModel.setRowCount(0);

        // Configurar columnas específicas para este reporte
        configurarColumnas(new String[]{"ID Producto", "Nombre Producto", "Categoría", "Cantidad Vendida", "Total Generado"});

        try {
            String sql = "SELECT p.id_producto, p.nombre_producto, p.categoria, " +
                    "COUNT(o.id_orden_compra) as num_ventas, " +
                    "SUM(o.total) as total_generado " +
                    "FROM inventario_productos p " +
                    "JOIN ordenes_compra o ON p.id_producto = o.id_producto " +
                    "GROUP BY p.id_producto, p.nombre_producto, p.categoria " +
                    "ORDER BY num_ventas DESC, total_generado DESC " +
                    "LIMIT ?";

            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, limite);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getInt("id_producto"),
                        rs.getString("nombre_producto"),
                        rs.getString("categoria"),
                        rs.getInt("num_ventas"),
                        String.format("$%.2f", rs.getDouble("total_generado"))
                };
                tableModel.addRow(row);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte de productos más vendidos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para generar reporte de clientes con más compras
    public void generarReporteClientesConMasCompras(int limite) {
        tableModel.setRowCount(0);

        // Configurar columnas específicas para este reporte
        configurarColumnas(new String[]{"ID Cliente", "Nombre Cliente", "Compras Realizadas", "Total Gastado", "Última Compra"});

        try {

            //  La tabla clientes con id_cliente y nombre
            String sql = "SELECT c.id_cliente, c.nombre as nombre_cliente, " +
                    "COUNT(o.id_orden_compra) as num_compras, " +
                    "SUM(o.total) as total_gastado, " +
                    "MAX(o.fecha_compra) as ultima_compra " +
                    "FROM clientes c " +
                    "JOIN ordenes_compra o ON c.id_cliente = o.id_cliente " +
                    "GROUP BY c.id_cliente, c.nombre " +
                    "ORDER BY num_compras DESC, total_gastado DESC " +
                    "LIMIT ?";

            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, limite);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getInt("id_cliente"),
                        rs.getString("nombre_cliente"),
                        rs.getInt("num_compras"),
                        String.format("$%.2f", rs.getDouble("total_gastado")),
                        rs.getTimestamp("ultima_compra")
                };
                tableModel.addRow(row);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte de clientes con más compras: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para generar reporte de productos con stock bajo
    public void generarReporteStockBajo(int umbralStock) {
        tableModel.setRowCount(0);

        // Configurar columnas específicas para este reporte
        configurarColumnas(new String[]{"ID Producto", "Nombre Producto", "Categoría", "Stock Actual", "Precio", "Proveedor ID"});

        try {
            String sql = "SELECT p.id_producto, p.nombre_producto, p.categoria, " +
                    "p.cantidad_stock, p.precio_producto, p.id_proveedor_asociado " +
                    "FROM inventario_productos p " +
                    "WHERE p.cantidad_stock <= ? " +
                    "ORDER BY p.cantidad_stock ASC, p.nombre_producto ASC";

            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, umbralStock);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getInt("id_producto"),
                        rs.getString("nombre_producto"),
                        rs.getString("categoria"),
                        rs.getInt("cantidad_stock"),
                        String.format("$%.2f", rs.getDouble("precio_producto")),
                        rs.getInt("id_proveedor_asociado")
                };
                tableModel.addRow(row);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte de stock bajo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para configurar las columnas de la tabla según el tipo de reporte
    private void configurarColumnas(String[] columnas) {
        tableModel.setColumnCount(0);
        for (String columna : columnas) {
            tableModel.addColumn(columna);
        }
    }

    // Método para exportar reporte actual a PDF
    public void exportarReporteActualAPDF(String tipoReporte) {
        // Implementación de la exportación a PDF
        // Este es un método que se completaría con una biblioteca como iText o JasperReports
        // Esta por implementar
        JOptionPane.showMessageDialog(null,
                "La exportación a PDF para el reporte " + tipoReporte + " será implementada con una biblioteca como iText.",
                "Exportación a PDF", JOptionPane.INFORMATION_MESSAGE);
    }
}