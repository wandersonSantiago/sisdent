package com.sisdent.repository.helper.lancamento;



import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sisdent.model.Lancamento;
import com.sisdent.repository.filter.LancamentoFilter;
import com.sisdent.repository.filter.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<Lancamento> lancamentosFuturos(LancamentoFilter lancamentoFilter, Pageable pageable);
	List<ResumoLancamento> porNome(String nome);
	BigDecimal totalContasAReceber();
	BigDecimal totalcontasAPagar();
	BigDecimal saldo();
	
}
