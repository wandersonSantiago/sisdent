package com.sisdent.service.event.venda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sisdent.model.ItemVenda;
import com.sisdent.model.Servico;
import com.sisdent.repository.Servicos;

@Component
public class VendaListener {

	@Autowired
	private Servicos servicos;
	
	@EventListener
	public void vendaEmitida(VendaEvent vendaEvent) {
		for (ItemVenda item : vendaEvent.getVenda().getItens()) {
			Servico servico = servicos.getOne(item.getServico().getCodigo());
			//servico.setQuantidadeEstoque(servico.getQuantidadeEstoque() - item.getQuantidade());
			servicos.save(servico);
		}
	}
	
}
