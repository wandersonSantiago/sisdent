package com.sisdent.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sisdent.controller.page.PageWrapper;
import com.sisdent.model.Agenda;
import com.sisdent.model.Cliente;
import com.sisdent.model.StatusAgenda;
import com.sisdent.model.StatusVenda;
import com.sisdent.model.Venda;
import com.sisdent.repository.Agendas;
import com.sisdent.repository.filter.AgendaFilter;
import com.sisdent.security.UsuarioSistema;
import com.sisdent.service.AgendasService;
import com.sisdent.service.ClienteService;
import com.sisdent.service.RelatorioService;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;
import com.sisdent.service.exception.NomeOuCodigoJaCadastradoException;

@Controller
@RequestMapping("/agendas")
public class AgendaController {

	@Autowired
	private AgendasService agendaService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private Agendas agendas;
	
	@Autowired
	private RelatorioService relatorioService;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Agenda agenda) {
		ModelAndView mv = new ModelAndView("agenda/agenda.form");
		mv.addObject(agenda);
		return mv;
	}
	
	@PostMapping("/novo" )
	public ModelAndView cadastrar( @Valid Agenda agenda, BindingResult result, RedirectAttributes attributes,  @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		if (result.hasErrors()) {
			return novo(agenda);
		}
		try {
			agendaService.salvar(agenda, usuarioSistema);
		} catch (NomeOuCodigoJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(agenda);
		}
		
		attributes.addFlashAttribute("mensagem", "Agenda salva com sucesso");
		return new ModelAndView("redirect:/agendas");
	}
	
	@PostMapping(value = "/novo", params = "enviarImprimir")
	public ResponseEntity<byte[]> enviarEmail(@Valid Agenda agenda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema)throws Exception  {
		List<Agenda> agendas = new ArrayList<>();
		agendas.add(agendaService.salvar(agenda, usuarioSistema));
		byte[] relatorio = relatorioService.gerarRelatorioGenerico(agendas, "/relatorios/agendamento.jrxml");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	
	@PostMapping(value = "/{codigo}", params = "enviarImprimir")
	public ResponseEntity<byte[]> enviarImprimir(@PathVariable Long codigo,@Valid Agenda agenda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema)throws Exception  {
		List<Agenda> agendas = new ArrayList<>();
		if(codigo != null) {
			agenda.setCodigo(codigo);
		}
		agendas.add(agendaService.salvar(agenda, usuarioSistema));
		byte[] relatorio = relatorioService.gerarRelatorioGenerico(agendas, "/relatorios/agendamento.jrxml");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	@PostMapping( "/{codigo}" )
	public ModelAndView editar(@PathVariable Long codigo, @Valid Agenda agenda, BindingResult result, RedirectAttributes attributes,  @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		if (result.hasErrors()) {
			return novo(agenda);
		}
		try {
			if(codigo != null) {
				agenda.setCodigo(codigo);
			}
			agendaService.salvar(agenda, usuarioSistema);
		} catch (NomeOuCodigoJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(agenda);
		}
		
		attributes.addFlashAttribute("mensagem", "Agenda salva com sucesso");
		return new ModelAndView("redirect:/agendas");
	}
	@GetMapping
	public ModelAndView pesquisar(AgendaFilter agendaFilter, BindingResult result
			, @PageableDefault(size = 24) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("agenda/agenda.list");
		mv.addObject("status", StatusAgenda.values());
		PageWrapper<Agenda> paginaWrapper = new PageWrapper<>(agendas.filtrar(agendaFilter, pageable)
				, httpServletRequest);
		
		List<Agenda> hoje = agendaService.buscarSomenteDoDia();
		mv.addObject("agendas", hoje);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping( "/cliente/{codigo}" )
	public ModelAndView pesquisarCliente(@PathVariable("codigo") Long codigo
			, @PageableDefault(size = 24) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("agenda/agenda.form");
		
		Agenda agenda = new Agenda();		
		Cliente cliente = clienteService.findOne(codigo);		
		agenda.setCliente(cliente);
		mv.addObject(agenda);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Agenda agenda) {
		try {
			agendaService.excluir(agenda);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Agenda agenda = agendaService.findOne(codigo);
		ModelAndView mv = novo(new Agenda());
		if(agenda != null) {
			mv.addObject(agenda);
			mv.addObject("status", StatusAgenda.values());
		}
		return mv;
	}
	
}
