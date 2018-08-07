package com.sisdent.model;



public enum TipoCategoria {

	PRODUTO("Produto"),
	FINANCEIRO("Financeiro"),
	SERVICO("Serviço")
	
	;
	
	private final String descricao;
	
	TipoCategoria(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}	
}
