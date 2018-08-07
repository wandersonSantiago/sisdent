package com.sisdent.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Categoria;
import com.sisdent.model.Lancamento;
import com.sisdent.model.Parcela;
import com.sisdent.model.StatusPagamento;
import com.sisdent.model.StatusVenda;
import com.sisdent.model.TipoLancamento;
import com.sisdent.model.Venda;
import com.sisdent.repository.Categorias;
import com.sisdent.repository.LancamentoRepository;
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
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private CategoriaService categoriaService;
	
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
		if(venda.getQtdParcelas() == 0) {
			
			venda.setStatusPagamento(StatusPagamento.PENDENTE);
			venda.setQtdParcelas(1);
			addParcela(venda);
		}	
		if (!venda.isNova()) {
			venda.getParcelas().forEach(p ->{			
				p.setLancamento(salvarLancamento(p));
			});
		}
		return vendas.saveAndFlush(venda);
	}

	@Transactional
	public void emitir(Venda venda) {		
		venda.setStatus(StatusVenda.EMITIDA);
		salvar(venda);		
		publisher.publishEvent(new VendaEvent(venda));
	}

	@Transactional
	private Lancamento salvarLancamento(Parcela parcela) {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setCategoria(categoriaService.criaCategoriaParcela("Parcelado"));
		lancamento.setDataVencimento(parcela.getDataVencimento());
		lancamento.setValor(parcela.getValor());
		lancamento.setTipo(TipoLancamento.RECEITA);
		lancamento.setObservacao("Parcela de número: "+ parcela.getNumero() +  " de um total de " + parcela.getOrcamento().getQtdParcelas());
		lancamento.setDescricao("Paciente: " +  parcela.getOrcamento().getCliente().getNome());
		
		return lancamentoRepository.saveAndFlush(lancamento);
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

	public Venda findOne(Long codigo) {
		return vendas.findById(codigo).get();
	}

}
