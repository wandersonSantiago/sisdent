package com.sisdent.controller;

import java.time.LocalDate;
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
import com.sisdent.model.Lancamento;
import com.sisdent.model.TipoCategoria;
import com.sisdent.model.TipoLancamento;
import com.sisdent.repository.Categorias;
import com.sisdent.repository.LancamentoRepository;
import com.sisdent.repository.filter.LancamentoFilter;
import com.sisdent.service.LancamentoService;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/lancamentos")
public class LancamentoController {
	
	@Autowired
	private Categorias categorias;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private LancamentoRepository lancamentos;

	@RequestMapping("/novo")
	public ModelAndView nova(Lancamento lancamento) {
		ModelAndView mv = new ModelAndView("financas/lancamento.form");
		mv.addObject("categorias", categorias.findByTipo(TipoCategoria.FINANCEIRO));
		mv.addObject("tipos", TipoLancamento.values());
		return mv;
	}
	
	@RequestMapping(value = { "/novo", "{\\d+}" }, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Lancamento lancamento, BindingResult result, Model model, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return nova(lancamento);
		}
		
		lancamentoService.salvar(lancamento);
		attributes.addFlashAttribute("mensagem", "Lancamento salvo com sucesso!");
		return new ModelAndView("redirect:/lancamentos/novo");
	}
	
	@RequestMapping(value = { "/editar/{codigo}" }, method = RequestMethod.POST)
	public ModelAndView editar(@PathVariable("codigo") Long codigo,  Lancamento lancamento, BindingResult result, Model model, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return nova(lancamento);
		}		
		lancamentoService.atualizar(codigo, lancamento);
		attributes.addFlashAttribute("mensagem", "Lancamento editado com sucesso!");
		return new ModelAndView("redirect:/lancamentos");
	}
	
	@GetMapping
	public ModelAndView pesquisar(LancamentoFilter lancamentoFilter, BindingResult result
			, @PageableDefault(size = 24) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("financas/lancamento.list");
		mv.addObject("categorias", categorias.findByTipo(TipoCategoria.FINANCEIRO));
		mv.addObject("tipos", TipoLancamento.values());
		mv.addObject("totalAPagar", lancamentos.totalcontasAPagar());
		mv.addObject("totalAReceber", lancamentos.totalContasAReceber());
		mv.addObject("totalSaldo", lancamentos.saldo());
		PageWrapper<Lancamento> paginaWrapper = new PageWrapper<>(lancamentos.filtrar(lancamentoFilter, pageable)
				, httpServletRequest);
		PageWrapper<Lancamento> lancamentoFuturoWrapper = new PageWrapper<>(lancamentos.lancamentosFuturos(lancamentoFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		mv.addObject("paginaLancamentoFuturo", lancamentoFuturoWrapper);
		return mv;
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Lancamento> pesquisar(String codigoOuNome) {
		List<Lancamento> lancamentoFiltrados = lancamentos.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		return lancamentoFiltrados;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Lancamento lancamento) {
		try {
			lancamentoService.excluir(lancamento);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Lancamento lancamento) {
		ModelAndView mv = nova(lancamento);
		mv.addObject(lancamento);
		return mv;
	}
	
}
