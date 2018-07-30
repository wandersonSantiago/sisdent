package com.sisdent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisdent.model.Cliente;
import com.sisdent.model.StatusVenda;
import com.sisdent.model.Venda;
import com.sisdent.repository.helper.venda.VendasQueries;

public interface Vendas extends JpaRepository<Venda, Long>, VendasQueries {

	List<Venda> findByClienteAndStatus(Cliente cliente, StatusVenda emitida);

}
