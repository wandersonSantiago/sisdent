package com.sisdent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisdent.model.Anaminese;
import com.sisdent.model.Cliente;
import com.sisdent.repository.helper.anaminese.AnaminesesQueries;

public interface Anamineses extends JpaRepository<Anaminese, Long>, AnaminesesQueries {

	Anaminese findByCliente(Cliente cliente);

}
