package com.sisdent.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Produto;
import com.sisdent.repository.Produtos;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;

@Service
public class ProdutoService {

	@Autowired
	private Produtos produtos;
	
	
	@Transactional
	public void salvar(Produto produto) {
		produtos.save(produto);
	}
	
	@Transactional
	public void excluir(Produto produto) {
		try {
			produtos.delete(produto);
			produtos.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar produto. Já foi usada em alguma venda.");
		}
	}
	
}
