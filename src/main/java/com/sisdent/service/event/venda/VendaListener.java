package com.sisdent.service.event.venda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sisdent.model.ItemVenda;
import com.sisdent.model.Produto;
import com.sisdent.repository.Produtos;

@Component
public class VendaListener {

	@Autowired
	private Produtos produtos;
	
	@EventListener
	public void vendaEmitida(VendaEvent vendaEvent) {
		for (ItemVenda item : vendaEvent.getVenda().getItens()) {
			Produto produto = produtos.getOne(item.getProduto().getCodigo());
			produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
			produtos.save(produto);
		}
	}
	
}
