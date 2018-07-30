package com.sisdent.model;

public enum StatusPagamento {

	PAGO("Pago"), 
	PENDENTE("Pendente"), 
	PARCELADO("Parcelado");

	private String descricao;

	StatusPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
