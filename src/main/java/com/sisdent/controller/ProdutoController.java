package com.sisdent.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sisdent.controller.page.PageWrapper;
import com.sisdent.model.Produto;
import com.sisdent.repository.Categorias;
import com.sisdent.repository.Produtos;
import com.sisdent.repository.filter.ProdutoFilter;
import com.sisdent.service.ProdutoService;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {
	
	@Autowired
	private Categorias categorias;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private Produtos produtos;

	@RequestMapping("/novo")
	public ModelAndView nova(Produto produto) {
		ModelAndView mv = new ModelAndView("produto/produto.form");
		mv.addObject("categorias", categorias.findAll());
		return mv;
	}
	
	@RequestMapping(value = { "/novo", "{\\d+}" }, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Produto produto, BindingResult result, Model model, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return nova(produto);
		}
		
		produtoService.salvar(produto);
		attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
		return new ModelAndView("redirect:/produtos/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(ProdutoFilter produtoFilter, BindingResult result
			, @PageableDefault(size = 24) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("produto/produto.list");
		mv.addObject("categorias", categorias.findAll());
		
		PageWrapper<Produto> paginaWrapper = new PageWrapper<>(produtos.filtrar(produtoFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Produto> pesquisar(String codigoOuNome) {
		List<Produto> produtoFiltrados = produtos.findByNomeIgnoreCaseContains(codigoOuNome);
		return produtoFiltrados;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Produto produto) {
		try {
			produtoService.excluir(produto);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Produto produto) {
		ModelAndView mv = nova(produto);
		mv.addObject(produto);
		return mv;
	}
	
}
