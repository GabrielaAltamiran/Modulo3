package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.exception.Karakedevexception;
import com.krakedev.inventarios.utilis.conexionbdd;

public class PedidosBDD {
	public void insertar(Pedido pedido) throws Karakedevexception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rsclave = null;
		Date fechaActual = new Date();
		PreparedStatement psDet = null;
		int codigoCabecera = 0;
		java.sql.Date fechaSql = new java.sql.Date(fechaActual.getTime());

		try {
			con = conexionbdd.obtenerConexion();
			ps = con.prepareStatement("insert into cabecera_pedidos (proveedor, fecha, estado) values(?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, pedido.getProveedor().getIndentificacion());
			ps.setDate(2, fechaSql);
			ps.setString(3, "S");

			ps.executeUpdate();

			rsclave = ps.getGeneratedKeys();
			if (rsclave.next()) {
				codigoCabecera = rsclave.getInt(1);
			}
			ArrayList<DetallePedido> detallesPediddos = pedido.getDetalles();
			DetallePedido det;
			for (int i = 0; i < detallesPediddos.size(); i++) {
				det = detallesPediddos.get(i);
				psDet = con.prepareStatement("INSERT INTO detalle_pedidos"
						+ "	( cabecera_pedido, codigo_pro, cantidad_solicitada, subtotal, cantidad_recibida)"
						+ "	VALUES ( ?, ?, ?, ?, ?);");
				psDet.setInt(1, codigoCabecera);
				psDet.setString(2, det.getProductos().getCodigoProducto());
				psDet.setInt(3, det.getCantidadSolicitada());
				psDet.setInt(5, 0); //NO ES NECESARIO QUE LLEGUE LA ANTIDAD RECIBIDA
				BigDecimal pv = det.getProductos().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadSolicitada());
				BigDecimal total =pv.multiply(cantidad);
				psDet.setBigDecimal(4, total);
				
				psDet.executeUpdate();
				
			}

		} catch (Karakedevexception e) {
			e.printStackTrace();
			throw e;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new Karakedevexception("Error al insertar pedido. Detalle: " + e.getMessage());

		} finally {
			try {
				if (rsclave != null)
					rsclave.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
