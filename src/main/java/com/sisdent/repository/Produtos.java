package com.sisdent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sisdent.model.Produto;
import com.sisdent.repository.helper.produto.ProdutosQueries;

@Repository
public interface Produtos extends JpaRepository<Produto, Long>, ProdutosQueries {

	List<Produto> findByNomeIgnoreCaseContains(String codigoOuNome);

}
