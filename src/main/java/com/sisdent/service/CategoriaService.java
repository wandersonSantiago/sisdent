package com.sisdent.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Categoria;
import com.sisdent.repository.Categorias;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;
import com.sisdent.service.exception.NomeOuCodigoJaCadastradoException;

@Service
public class CategoriaService {

	@Autowired
	private Categorias categorias;
	
	@Transactional
	public Categoria salvar(Categoria estilo) {
		Optional<Categoria> estiloOptional = categorias.findByNomeIgnoreCase(estilo.getNome());
		if (estiloOptional.isPresent()) {
			throw new NomeOuCodigoJaCadastradoException("Não foi possivel completar a operação, nome da Categoria já tem cadastrado");
		}
		
		return categorias.saveAndFlush(estilo);
	}

	@Transactional
	public void excluir(Categoria categoria) {
	try {
		categorias.delete(categoria);
		categorias.flush();
	} catch (PersistenceException e) {
		throw new ImpossivelExcluirEntidadeException("Impossível apagar categoria. Já foi usada no cadastro de algum produto.");
	}
	}

	public Categoria findOne(Long codigo) {
		return categorias.findById(codigo).get();
	}
	
}
