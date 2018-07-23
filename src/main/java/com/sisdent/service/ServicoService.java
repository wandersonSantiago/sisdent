package com.sisdent.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Servico;
import com.sisdent.repository.Servicos;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;
import com.sisdent.service.exception.NomeOuCodigoJaCadastradoException;

@Service
public class ServicoService {

	@Autowired
	private Servicos servicos;
		
	@Transactional
	public void salvar(Servico servico) {
		if(servicos.findTop1ByCodigoBarras(servico.getCodigoBarras()) != null ||
				servicos.findTop1ByNome(servico.getNome()) != null) {
			throw new NomeOuCodigoJaCadastradoException("Nome ou codigo ja estão cadastrados. Favor escolher outro");
		}
		servicos.save(servico);
	}
	
	@Transactional
	public void excluir(Servico servico) {
		try {
			servicos.delete(servico);
			servicos.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar servico. Já foi usada em alguma venda.");
		}
	}
	
}
