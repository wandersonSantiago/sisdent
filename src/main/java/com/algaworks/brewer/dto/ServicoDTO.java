package com.algaworks.brewer.dto;

import java.math.BigDecimal;

public class ServicoDTO {

	private Long codigo;
	private String nome;
	private String descricao;
	private BigDecimal valor;

	public ServicoDTO(Long codigo, String nome, String descricao,  BigDecimal valor) {
		this.codigo = codigo;
		this.nome = nome;
		this.descricao = descricao;
		this.valor = valor;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
}
