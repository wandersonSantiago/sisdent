package com.sisdent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Lancamento;
import com.sisdent.model.Parcela;

public interface ParcelaRepository extends JpaRepository<Parcela, Long>{

	@Transactional
	void deleteByCodigo(Long codigo);

	Parcela findByLancamento(Lancamento lancamento);


}
