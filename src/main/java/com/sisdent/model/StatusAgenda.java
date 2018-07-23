package com.sisdent.model;

public enum StatusAgenda {

	PENDENTE("Pendente"), 
	REALIZADA("Realizada"), 
	CANCELADA("Cancelada");

	private String descricao;

	StatusAgenda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
