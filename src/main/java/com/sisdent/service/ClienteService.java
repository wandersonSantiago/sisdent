package com.sisdent.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Cliente;
import com.sisdent.repository.Clientes;
import com.sisdent.service.exception.CpfCnpjClienteJaCadastradoException;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;

@Service
public class ClienteService {

	@Autowired
	private Clientes clientes;
	
	@Transactional
	public void salvar(Cliente cliente) {
		Cliente clienteExistente = clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		if (clienteExistente != null && clienteExistente.getCodigo() != cliente.getCodigo()) {
			throw new CpfCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado");
		}
		
		clientes.save(cliente);
	}
	
	@Transactional
	public void excluir(Cliente cliente) {
	try {
		clientes.delete(cliente);
		clientes.flush();
	} catch (PersistenceException e) {
		throw new ImpossivelExcluirEntidadeException("Impossível apagar cliente. Já foi usada no cadastro de alguma anaminese ou vendas.");
	}
	}

	public Cliente findOne(Long codigo) {
		return clientes.findById(codigo).get();
	}
}
