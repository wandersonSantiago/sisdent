package com.sisdent.repository.helper.lancamento;



import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sisdent.model.Lancamento;
import com.sisdent.model.TipoLancamento;
import com.sisdent.repository.filter.LancamentoFilter;
import com.sisdent.repository.filter.ResumoLancamento;
import com.sisdent.repository.paginacao.PaginacaoUtil;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Lancamento> filtrar(LancamentoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Lancamento.class);
		criteria.addOrder(Order.desc("dataPagamento"));
		criteria.add(Restrictions.isNotNull("dataPagamento"));
		paginacaoUtil.preparar(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	@Override
	public List<ResumoLancamento> porNome(String nome) {
		String jpql = "select new com.algaworks.brewer.dto.ResumoLancamento(codigo, nome, descricao, valor) "
				+ "from Lancamento where lower(nome) like lower(:nome)";
		List<ResumoLancamento> produtoFiltrados = manager.createQuery(jpql, ResumoLancamento.class)
					.setParameter("nome", nome + "%")
					.getResultList();
		return produtoFiltrados;
	}
	
	private Long total(LancamentoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Lancamento.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(LancamentoFilter filtro, Criteria criteria) {
		if (filtro != null) {
		
			if (!StringUtils.isEmpty(filtro.getCodigo())) {
				criteria.add(Restrictions.eq("codigo", filtro.getCodigo()));
			}
			if (!StringUtils.isEmpty(filtro.getDescricao())) {
				criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
			}
			if (!StringUtils.isEmpty(filtro.getTipo())) {
				criteria.add(Restrictions.eq("tipo", filtro.getTipo()));
			}
			if (!StringUtils.isEmpty(filtro.getCategoria())) {
				criteria.add(Restrictions.eq("categoria", filtro.getCategoria()));
			}
			if (filtro.getDataVencimentoDe()!= null) {
				criteria.add(Restrictions.ge("dataVencimento", filtro.getDataVencimentoDe()));
			}
			if (filtro.getDataVencimentoAte() != null) {
				criteria.add(Restrictions.le("dataVencimento", filtro.getDataVencimentoAte()));
			}
			if (filtro.getDataCadastroDe()!= null) {
				criteria.add(Restrictions.ge("dataCadastro", filtro.getDataCadastroDe()));
			}
			if (filtro.getDataCadastroAte() != null) {
				criteria.add(Restrictions.le("dataCadastro", filtro.getDataCadastroAte()));
			}
			if (filtro.getDataPagamentoDe()!= null) {
				criteria.add(Restrictions.ge("dataPagamento", filtro.getDataPagamentoDe()));
			}
			if (filtro.getDataPagamentoAte() != null) {
				criteria.add(Restrictions.le("dataPagamento", filtro.getDataPagamentoAte()));
			}		
			if (filtro.getValorMinimo() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorMinimo()));
			}
			if (filtro.getDataVencimentoAte() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorMaximo()));
			}
		}
	}

	@Override
	public BigDecimal totalContasAReceber() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valor) from Lancamento where dataPagamento IS NULL and tipo = :tipo", BigDecimal.class)
					/*.setParameter("data", LocalDate.now())*/
					.setParameter("tipo", TipoLancamento.RECEITA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal totalcontasAPagar() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valor) from Lancamento where dataPagamento IS NULL and tipo = :tipo", BigDecimal.class)
					.setParameter("tipo", TipoLancamento.DESPESA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	public BigDecimal totalcontasPagas() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valor) from Lancamento where dataPagamento IS NOT NULL and tipo = :tipo", BigDecimal.class)
					.setParameter("tipo", TipoLancamento.DESPESA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	public BigDecimal totalcontasRecebidas() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				manager.createQuery("select sum(valor) from Lancamento where dataPagamento IS NOT NULL and tipo = :tipo", BigDecimal.class)
					.setParameter("tipo", TipoLancamento.RECEITA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal saldo() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				totalcontasRecebidas().subtract(totalcontasPagas()));
		return optional.orElse(BigDecimal.ZERO);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Lancamento> lancamentosFuturos(LancamentoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Lancamento.class);
		criteria.addOrder(Order.desc("dataPagamento"));
		criteria.add(Restrictions.isNull("dataPagamento"));
		paginacaoUtil.preparar(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
}
