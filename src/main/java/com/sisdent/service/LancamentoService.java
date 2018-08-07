package com.sisdent.service;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Lancamento;
import com.sisdent.model.Parcela;
import com.sisdent.repository.LancamentoRepository;
import com.sisdent.repository.ParcelaRepository;

@Service
public class LancamentoService {
		
	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);

	

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ParcelaRepository parcelaRepository;

	
	@Scheduled(cron = "0 0 6 * * *")
	public void avisarSobreLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de "
					+ "e-mails de aviso de lançamentos vencidos.");
		}
		
		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		
		if (vencidos.isEmpty()) {
			logger.info("Sem lançamentos vencidos para aviso.");
			
			return;
		}
		
		logger.info("Exitem {} lançamentos vencidos.", vencidos.size());

	
	}
	
	/*public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream(
				"/relatorios/lancamentos-por-pessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}*/

	public Lancamento salvar(Lancamento lancamento) {		

		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
	
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		Parcela parcela = parcelaRepository.findByLancamento(lancamento);
		if(parcela != null && lancamento.getDataPagamento() != null) {
			parcela.setDataPagamento(lancamento.getDataPagamento());
			parcela.setPago(true);
		}
		
		return lancamentoRepository.save(lancamentoSalvo);
	}


	private Lancamento buscarLancamentoExistente(Long codigo) {
		Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);
		if (!lancamentoSalvo.isPresent()) {
			throw new IllegalArgumentException();
		}
		return lancamentoSalvo.get();
	}

	@Transactional
	public void excluir(Lancamento lancamento) {
		lancamentoRepository.delete(lancamento);
	}

}
