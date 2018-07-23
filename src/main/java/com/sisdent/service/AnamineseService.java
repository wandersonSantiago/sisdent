package com.sisdent.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Anaminese;
import com.sisdent.model.Cliente;
import com.sisdent.repository.Anamineses;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;

@Service
public class AnamineseService {

	@Autowired
	private Anamineses anamineses;
	
	
	@Transactional
	public  Anaminese salvar(Anaminese anaminese) {
		Anaminese a = findByCliente(anaminese.getCliente());
		if(a != null) {
			anaminese.setCodigo(a.getCodigo());
		}
		return anamineses.save(anaminese);
	}
	
	@Transactional
	public void excluir(Anaminese anaminese) {
		try {
			anamineses.delete(anaminese);
			anamineses.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar anaminese. Já foi usada em alguma venda.");
		}
	}

	public Anaminese findOne(Long codigo) {
		return anamineses.findById(codigo).get();
	}

	public Anaminese findByCliente(Cliente cliente) {
		return anamineses.findByCliente(cliente);
	}
	
}
