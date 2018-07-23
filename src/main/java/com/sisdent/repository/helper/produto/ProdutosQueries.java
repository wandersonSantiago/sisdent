package com.sisdent.repository.helper.produto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.dto.ProdutoDTO;
import com.algaworks.brewer.dto.ValorItensEstoque;
import com.sisdent.model.Produto;
import com.sisdent.repository.filter.ProdutoFilter;

public interface ProdutosQueries {

	public Page<Produto> filtrar(ProdutoFilter filtro, Pageable pageable);
	
	public List<ProdutoDTO> porCodigoOuNome(String codigoOuNome);
	
	public ValorItensEstoque valorItensEstoque();
	
}
