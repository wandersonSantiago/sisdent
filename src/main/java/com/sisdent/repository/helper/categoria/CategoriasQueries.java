package com.sisdent.repository.helper.categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sisdent.model.Categoria;
import com.sisdent.repository.filter.CategoriaFilter;

public interface CategoriasQueries {
	
	public Page<Categoria> filtrar(CategoriaFilter filtro, Pageable pageable);
	
}
