package com.sisdent.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sisdent.model.Agenda;
import com.sisdent.model.Cliente;
import com.sisdent.model.StatusAgenda;
import com.sisdent.repository.helper.agenda.AgendasQueries;

public interface Agendas extends JpaRepository<Agenda, Long> , AgendasQueries{

	Agenda findByCliente(Cliente cliente);

	@Query("Select a From Agenda a where a.cliente = ?1  and a.status = ?2")
	List<Agenda> findByClienteAndStatus(Cliente cliente, StatusAgenda realizada);

	List<Agenda> findByDataLessThanEqualAndDataGreaterThanEqual(LocalDate now, LocalDate now2);

	List<Agenda> findByClienteAndDataLessThanEqualAndDataGreaterThanEqual(Cliente cliente, LocalDate now,
			LocalDate now2);





}
