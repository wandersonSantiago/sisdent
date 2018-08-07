package com.sisdent.repository.filter;



import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.sisdent.model.Categoria;
import com.sisdent.model.TipoLancamento;

public class LancamentoFilter {

	private Long codigo;
	private String descricao;
	
	private TipoLancamento tipo;
	
	private Categoria categoria;
	

	private BigDecimal valorMinimo;
	private BigDecimal valorMaximo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoDe;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoAte;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataCadastroDe;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataCadastroAte;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamentoDe;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamentoAte;
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataVencimentoDe() {
		return dataVencimentoDe;
	}

	public void setDataVencimentoDe(LocalDate dataVencimentoDe) {
		this.dataVencimentoDe = dataVencimentoDe;
	}

	public LocalDate getDataVencimentoAte() {
		return dataVencimentoAte;
	}

	public void setDataVencimentoAte(LocalDate dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public BigDecimal getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public LocalDate getDataCadastroDe() {
		return dataCadastroDe;
	}

	public void setDataCadastroDe(LocalDate dataCadastroDe) {
		this.dataCadastroDe = dataCadastroDe;
	}

	public LocalDate getDataCadastroAte() {
		return dataCadastroAte;
	}

	public void setDataCadastroAte(LocalDate dataCadastroAte) {
		this.dataCadastroAte = dataCadastroAte;
	}

	public LocalDate getDataPagamentoDe() {
		return dataPagamentoDe;
	}

	public void setDataPagamentoDe(LocalDate dataPagamentoDe) {
		this.dataPagamentoDe = dataPagamentoDe;
	}

	public LocalDate getDataPagamentoAte() {
		return dataPagamentoAte;
	}

	public void setDataPagamentoAte(LocalDate dataPagamentoAte) {
		this.dataPagamentoAte = dataPagamentoAte;
	}
	

}
