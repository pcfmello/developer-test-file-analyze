package br.com.segware.entity;

import org.joda.time.DateTime;

import br.com.segware.Tipo;

public class Evento {

	private Integer codSequencial;
	private String codCliente;
	private String codEvento;
	private Tipo tipoEvento;
	private DateTime dataInicio;
	private DateTime dataFim;
	private String codAtendente;
	
	public Evento(Integer codSequencial, String codCliente, String codEvento, Tipo tipoEvento, DateTime dataInicio,
			DateTime dataFim, String codAtendente) {
		this.codSequencial = codSequencial;
		this.codCliente = codCliente;
		this.codEvento = codEvento;
		this.tipoEvento = tipoEvento;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.codAtendente = codAtendente;
	}
	
	public Integer getCodSequencial() {
		return codSequencial;
	}

	public String getCodCliente() {
		return codCliente;
	}

	public String getCodEvento() {
		return codEvento;
	}

	public Tipo getTipoEvento() {
		return tipoEvento;
	}

	public DateTime getDataInicio() {
		return dataInicio;
	}

	public DateTime getDataFim() {
		return dataFim;
	}

	public String getCodAtendente() {
		return codAtendente;
	}

}
