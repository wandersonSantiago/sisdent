package com.sisdent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisdent.model.Venda;
import com.sisdent.repository.helper.venda.VendasQueries;

public interface Vendas extends JpaRepository<Venda, Long>, VendasQueries {

}
