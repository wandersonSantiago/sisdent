package com.sisdent.repository.helper.lancamento;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.dto.LancamentoEstatisticaCategoria;
import com.algaworks.brewer.dto.LancamentoEstatisticaDia;
import com.algaworks.brewer.dto.LancamentoEstatisticaPessoa;
import com.sisdent.model.Lancamento;
import com.sisdent.repository.filter.LancamentoFilter;
import com.sisdent.repository.filter.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	
}
