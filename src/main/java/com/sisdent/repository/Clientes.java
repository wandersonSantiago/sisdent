package com.sisdent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisdent.model.Cliente;
import com.sisdent.repository.helper.cliente.ClientesQueries;

public interface Clientes extends JpaRepository<Cliente, Long>, ClientesQueries {

	public Cliente findByCpfOuCnpj(String cpfOuCnpj);

	public List<Cliente> findByNomeStartingWithIgnoreCase(String nome);

}
