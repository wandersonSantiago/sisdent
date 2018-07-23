package com.sisdent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sisdent.model.Categoria;
import com.sisdent.repository.helper.categoria.CategoriasQueries;

@Repository
public interface Categorias extends JpaRepository<Categoria, Long>, CategoriasQueries {

	public Optional<Categoria> findByNomeIgnoreCase(String nome);
	
}
