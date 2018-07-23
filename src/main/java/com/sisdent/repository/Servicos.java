package com.sisdent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sisdent.model.Servico;
import com.sisdent.repository.helper.servico.ServicosQueries;

@Repository
public interface Servicos extends JpaRepository<Servico, Long>, ServicosQueries {

	Servico findTop1ByCodigoBarras(String b);

	Servico findTop1ByNome(String nome);


}
