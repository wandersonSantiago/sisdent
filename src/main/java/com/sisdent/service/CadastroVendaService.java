package com.sisdent.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Parcela;
import com.sisdent.model.StatusPagamento;
import com.sisdent.model.StatusVenda;
import com.sisdent.model.Venda;
import com.sisdent.repository.ParcelaRepository;
import com.sisdent.repository.Vendas;
import com.sisdent.service.event.venda.VendaEvent;
import com.sisdent.service.exception.NomeCidadeJaCadastradaException;

@Service
public class CadastroVendaService {

	@Autowired
	private Vendas vendas;
	
	@Autowired
	private ParcelaRepository parcelaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Transactional
	public Venda salvar(Venda venda) {
		if (venda.isSalvarProibido()) {
			throw new NomeCidadeJaCadastradaException("Usuário tentando salvar um  orçamento proibido");
		}
	
		if (venda.isNova()) {
			venda.setDataCriacao(LocalDateTime.now());
		} else {
			Venda vendaExistente = vendas.buscarComItens(venda.getCodigo());
			venda.setDataCriacao(vendaExistente.getDataCriacao());
			if(vendaExistente.getParcelas().size() > 0) {
				for(Parcela parcela : vendaExistente.getParcelas()) {
					parcelaRepository.deleteByCodigo(parcela.getCodigo());
				}
			}
		}
		if(venda.getQtdParcelas() > 0) {
			
			venda.setStatusPagamento(StatusPagamento.PARCELADO);
			addParcela(venda);
		}		
		return vendas.saveAndFlush(venda);
	}

	@Transactional
	public void emitir(Venda venda) {
		if (venda.getStatus().ordinal() != StatusVenda.ORCAMENTO.ordinal()) {
			throw new NomeCidadeJaCadastradaException("Não foi possivel realizar esta operação este orçamento ja foi Emitido ou cancelado");
		}
		venda.setStatus(StatusVenda.EMITIDA);
		salvar(venda);
		
		publisher.publishEvent(new VendaEvent(venda));
	}

	@PreAuthorize("#venda.usuario == principal.usuario or hasRole('CANCELAR_VENDA')")
	@Transactional
	public void cancelar(Venda venda) {
		if (venda.getStatus() != StatusVenda.ORCAMENTO) {
			throw new NomeCidadeJaCadastradaException("Não foi possivel realizar esta operação este orçamento ja foi Emitido ou cancelado");
		}
		Venda vendaExistente = vendas.getOne(venda.getCodigo());
		
		vendaExistente.setStatus(StatusVenda.CANCELADA);
		vendas.save(vendaExistente);
	}
	
	public void addParcela(Venda venda) {
		venda.calcularValorTotal();
		BigDecimal valorParcela = venda.getValorTotal().subtract(venda.getValorEntrada()).divide(new BigDecimal(venda.getQtdParcelas()));

		if (venda.getDataEntrega() == null) {			
			venda.setDataEntrega(LocalDate.now());
		}
		
		if(venda.getQtdParcelas() > 0) {
			for(int i = 1; i <= venda.getQtdParcelas(); i++) {
				Parcela parcela = new Parcela();
				parcela.setNumero(i);
				parcela.setOrcamento(venda);
				parcela.setPago(false);
				parcela.setValor(valorParcela);
				parcela.setDataVencimento(venda.getDataEntrega().plusMonths(i));
				venda.addParcela(parcela);
			}
		}
	}

}
