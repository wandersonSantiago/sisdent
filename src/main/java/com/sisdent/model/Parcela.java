package com.sisdent.model;



import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;


@Entity
public class Parcela implements Serializable {
	
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false)
    private Long codigo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero", nullable = false)
    private int numero;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false, precision = 16, scale = 2)
    private BigDecimal valor;
    
    @Column(name = "data_vencimento")
	private LocalDate dataVencimento;
    
    @Column(name = "data_pagamento")
	private LocalDate dataPagamento;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "pago", nullable = false)
    private boolean pago;
    
    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    private Venda orcamento;

    @OneToOne
    private Lancamento lancamento;
    
    public Parcela() {
    }

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public Venda getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Venda orcamento) {
		this.orcamento = orcamento;
	}
	

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate localDate) {
		this.dataVencimento = localDate;
	}

	
	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	
	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parcela other = (Parcela) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	

	

}
