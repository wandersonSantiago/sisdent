package com.sisdent.repository.helper.agenda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sisdent.model.Agenda;
import com.sisdent.repository.filter.AgendaFilter;

public interface AgendasQueries {

	public Page<Agenda> filtrar(AgendaFilter filtro, Pageable pageable);
	
	
}
