package com.sisdent.model;

public enum StatusVenda {

	ORCAMENTO("Orçamento"), 
	EMITIDA("Emitido"), 
	CANCELADA("Cancelado");

	private String descricao;

	StatusVenda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
