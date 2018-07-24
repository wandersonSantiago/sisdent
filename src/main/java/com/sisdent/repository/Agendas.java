package com.sisdent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisdent.model.Agenda;
import com.sisdent.model.Cliente;
import com.sisdent.model.StatusAgenda;
import com.sisdent.repository.helper.agenda.AgendasQueries;

public interface Agendas extends JpaRepository<Agenda, Long> , AgendasQueries{

	Agenda findByCliente(Cliente cliente);

	List<Agendas> findByClienteAndStatus(Cliente cliente, StatusAgenda realizada);




}
