package com.sisdent.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sisdent.controller.page.PageWrapper;
import com.sisdent.model.Anaminese;
import com.sisdent.model.Cliente;
import com.sisdent.repository.Anamineses;
import com.sisdent.repository.filter.AnamineseFilter;
import com.sisdent.service.AnamineseService;
import com.sisdent.service.ClienteService;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;
import com.sisdent.service.exception.NomeOuCodigoJaCadastradoException;

@Controller
@RequestMapping("/anamineses")
public class AnamineseController {

	@Autowired
	private AnamineseService anamineseService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private Anamineses anamineses;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Anaminese anaminese) {
		ModelAndView mv = new ModelAndView("anaminese/anaminese.form");
		mv.addObject(anaminese);
		return mv;
	}
	
	@RequestMapping(value = "/novo/{codigo}", method = RequestMethod.POST)
	public ModelAndView cadastrar(@PathVariable Long codigo, @Valid Anaminese anaminese, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(anaminese);
		}
		Cliente cliente = clienteService.findOne(codigo);
		anaminese.setCliente(cliente);
		try {
			anamineseService.salvar(anaminese);
		} catch (NomeOuCodigoJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(anaminese);
		}
		
		attributes.addFlashAttribute("mensagem", "Anaminese salva com sucesso");
		return new ModelAndView("redirect:/clientes");
	}
	
	@GetMapping
	public ModelAndView pesquisar(AnamineseFilter anamineseFilter, BindingResult result
			, @PageableDefault(size = 24) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("anaminese/anaminese.list");
		
		PageWrapper<Anaminese> paginaWrapper = new PageWrapper<>(anamineses.filtrar(anamineseFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Anaminese anaminese) {
		try {
			anamineseService.excluir(anaminese);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Cliente cliente = clienteService.findOne(codigo);
		Anaminese anaminese = anamineseService.findByCliente(cliente);
		ModelAndView mv = novo(new Anaminese());
		if(anaminese != null) {
			mv.addObject(anaminese);
		}
		mv.addObject(cliente);
		return mv;
	}
	
}
