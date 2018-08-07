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

import com.algaworks.brewer.dto.ServicoDTO;
import com.sisdent.controller.page.PageWrapper;
import com.sisdent.model.Servico;
import com.sisdent.model.TipoCategoria;
import com.sisdent.repository.Categorias;
import com.sisdent.repository.Servicos;
import com.sisdent.repository.filter.ServicoFilter;
import com.sisdent.service.ServicoService;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/servicos")
public class ServicoController {
	
	@Autowired
	private Categorias categorias;
	
	@Autowired
	private ServicoService servicoService;
	
	@Autowired
	private Servicos servicos;

	@RequestMapping("/novo")
	public ModelAndView nova(Servico servico) {
		ModelAndView mv = new ModelAndView("servico/servico.form");
		mv.addObject("categorias", categorias.findByTipo(TipoCategoria.SERVICO));
		return mv;
	}
	
	@RequestMapping(value = { "/novo", "{\\d+}" }, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Servico servico, BindingResult result, Model model, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return nova(servico);
		}
		
		servicoService.salvar(servico);
		attributes.addFlashAttribute("mensagem", "Servico salvo com sucesso!");
		return new ModelAndView("redirect:/servicos/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(ServicoFilter servicoFilter, BindingResult result
			, @PageableDefault(size = 24) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("servico/servico.list");
		mv.addObject("categorias", categorias.findByTipo(TipoCategoria.SERVICO));
		
		PageWrapper<Servico> paginaWrapper = new PageWrapper<>(servicos.filtrar(servicoFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ServicoDTO> pesquisar(String nome) {
		return servicos.porNome(nome);
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Servico servico) {
		try {
			servicoService.excluir(servico);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Servico servico) {
		ModelAndView mv = nova(servico);
		mv.addObject(servico);
		return mv;
	}
	
}
