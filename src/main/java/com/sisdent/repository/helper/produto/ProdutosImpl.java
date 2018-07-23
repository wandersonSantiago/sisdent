package com.sisdent.repository.helper.produto;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.dto.ProdutoDTO;
import com.algaworks.brewer.dto.ValorItensEstoque;
import com.sisdent.model.Produto;
import com.sisdent.repository.filter.ProdutoFilter;
import com.sisdent.repository.paginacao.PaginacaoUtil;

public class ProdutosImpl implements ProdutosQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Produto> filtrar(ProdutoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Produto.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	@Override
	public List<ProdutoDTO> porCodigoOuNome(String codigoOuNome) {
		String jpql = "select new com.algaworks.brewer.dto.ProdutoDTO(codigo, codigoBarras, nome, origem, valor) "
				+ "from Produto where lower(codigoBarras) like lower(:codigoOuNome) or lower(nome) like lower(:codigoOuNome)";
		List<ProdutoDTO> produtoFiltrados = manager.createQuery(jpql, ProdutoDTO.class)
					.setParameter("codigoOuNome", codigoOuNome + "%")
					.getResultList();
		return produtoFiltrados;
	}
	
	@Override
	public ValorItensEstoque valorItensEstoque() {
		String query = "select new com.algaworks.brewer.dto.ValorItensEstoque(sum(valor * quantidadeEstoque), sum(quantidadeEstoque)) from Produto";
		return manager.createQuery(query, ValorItensEstoque.class).getSingleResult();
	}
	
	private Long total(ProdutoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Produto.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(ProdutoFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getCodigoBarras())) {
				criteria.add(Restrictions.eq("codigoBarras", filtro.getCodigoBarras()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
					
			if (!StringUtils.isEmpty(filtro.getCategoria())) {
				criteria.add(Restrictions.eq("categoria", filtro.getCategoria()));
			}
			
			if (filtro.getValorDe() != null) {
				criteria.add(Restrictions.ge("valor", filtro.getValorDe()));
			}

			if (filtro.getValorAte() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorAte()));
			}
		}
	}
	
	

}
