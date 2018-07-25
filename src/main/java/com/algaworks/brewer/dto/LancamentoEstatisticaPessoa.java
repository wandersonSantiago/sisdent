package com.algaworks.brewer.dto;



import java.math.BigDecimal;

import com.sisdent.model.TipoLancamento;

public class LancamentoEstatisticaPessoa {
	
	private TipoLancamento tipo;
	
	
	private BigDecimal total;

	public LancamentoEstatisticaPessoa(TipoLancamento tipo,  BigDecimal total) {
		this.tipo = tipo;
		this.total = total;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
