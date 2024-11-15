package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.tipoDocumentobdd;
import com.krakedev.inventarios.entidades.tipoDocumento;
import com.krakedev.inventarios.exception.Karakedevexception;

@Path("TipoDocumentos")
public class ServiciosTipoDocumentos {
	@Path("buscarTipoDocumento/{subcadena}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarTipoDocumento(@PathParam("subcadena") String subcadena) {
		tipoDocumentobdd documentoTipo = new tipoDocumentobdd();
		ArrayList<tipoDocumento> documento = null;
		try {
			documento = documentoTipo.BuscarTipoDocumento(subcadena);
			return Response.ok(documento).build();
		} catch (Karakedevexception e) {
			return Response.serverError().build();
		}
	}

}