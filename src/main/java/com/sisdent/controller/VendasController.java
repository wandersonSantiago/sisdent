package com.sisdent.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import com.sisdent.controller.page.PageWrapper;
import com.sisdent.controller.validator.VendaValidator;
import com.sisdent.model.Cliente;
import com.sisdent.model.ItemVenda;
import com.sisdent.model.Servico;
import com.sisdent.model.StatusVenda;
import com.sisdent.model.TipoPessoa;
import com.sisdent.model.Venda;
import com.sisdent.repository.Servicos;
import com.sisdent.repository.Vendas;
import com.sisdent.repository.filter.VendaFilter;
import com.sisdent.security.UsuarioSistema;
import com.sisdent.service.CadastroVendaService;
import com.sisdent.service.ClienteService;
import com.sisdent.service.RelatorioService;
import com.sisdent.service.RelatorioUtil;
import com.sisdent.session.TabelasItensSession;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private Servicos servicos;
	
	@Autowired
	private TabelasItensSession tabelaItens;
	
	@Autowired
	private CadastroVendaService cadastroVendaService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private VendaValidator vendaValidator;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private Vendas vendas;
	
	@Autowired
	private RelatorioService relatorioService;
	
	@GetMapping("/nova")
	public ModelAndView nova(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		
		setUuid(venda);
		
		mv.addObject("itens", venda.getItens());
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		mv.addObject("valorTotalItens", tabelaItens.getValorTotal(venda.getUuid()));
		
		return mv;
	}
	
	@PostMapping(value = "/nova", params = "salvar")
	public ModelAndView salvar(@Valid Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Orçamento salvo com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}

	@PostMapping(value = "/nova", params = "emitir")
	public ModelAndView emitir(@Valid Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.emitir(venda);
		attributes.addFlashAttribute("mensagem", "Orçamento emitido com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping(value = "/nova", params = "imprimir")
	public ResponseEntity<byte[]>  salvarEImprimir(@Valid Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) throws Exception {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return null;
		}		
		venda.setUsuario(usuarioSistema.getUsuario());		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Orçamento emitido com sucesso");
		
		List<Venda> vendas = new ArrayList<>();
		vendas.add(venda);
		byte[] relatorio = relatorioService.gerarRelatorioGenerico(vendas, "/relatorios/orcamento.jasper");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	
	@PostMapping(value = "/nova", params = "enviarImprimir")
	public ResponseEntity<byte[]> enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema)throws Exception  {
		List<Venda> vendas = new ArrayList<>();
		vendas.add(venda);
		byte[] relatorio = relatorioService.gerarRelatorioGenerico(vendas, "/relatorios/orcamento.jasper");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	
	@PostMapping("/item")
	public ModelAndView adicionarItem(Long codigoServico, String uuid) {
		Servico servico =  servicos.findById(codigoServico).get();
		tabelaItens.adicionarItem(uuid, servico, 1);
		return mvTabelaItensVenda(uuid);
	}
	
	@PutMapping("/item/{codigoServico}")
	public ModelAndView alterarQuantidadeItem(@PathVariable("codigoServico") Servico servico
			, Integer quantidade, String uuid) {
		tabelaItens.alterarQuantidadeItens(uuid, servico, quantidade);
		return mvTabelaItensVenda(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{codigoServico}")
	public ModelAndView excluirItem(@PathVariable("codigoServico") Servico servico
			, @PathVariable String uuid) {
		tabelaItens.excluirItem(uuid, servico);
		return mvTabelaItensVenda(uuid);
	}
	
	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter,
			@PageableDefault(size = 10) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("venda/PesquisaVendas");
		mv.addObject("todosStatus", StatusVenda.values());
		mv.addObject("tiposPessoa", TipoPessoa.values());
		
		PageWrapper<Venda> paginaWrapper = new PageWrapper<>(vendas.filtrar(vendaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping( "/cliente/{codigo}" )
	public ModelAndView pesquisarCliente(@PathVariable("codigo") Long codigo
			, @PageableDefault(size = 24) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		
		Venda venda = new Venda();		
		Cliente cliente = clienteService.findOne(codigo);		
		venda.setCliente(cliente);
		mv.addObject(venda);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Venda venda = vendas.buscarComItens(codigo);
		
		setUuid(venda);
		for (ItemVenda item : venda.getItens()) {
			tabelaItens.adicionarItem(venda.getUuid(), item.getServico(), item.getQuantidade());
		}
		
		ModelAndView mv = nova(venda);
		
		mv.addObject(venda);
		return mv;
	}
	
	@PostMapping(value = "/nova", params = "cancelar")
	public ModelAndView cancelar(Venda venda, BindingResult result
				, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		try {
			cadastroVendaService.cancelar(venda);
		} catch (AccessDeniedException e) {
			ModelAndView mv = new ModelAndView("error");
			mv.addObject("status", 403);
			return mv;
		}
		
		attributes.addFlashAttribute("mensagem", "Orçamento cancelada com sucesso");
		return new ModelAndView("redirect:/vendas/" + venda.getCodigo());
	}
	
	@GetMapping("/totalPorMes")
	public @ResponseBody List<VendaMes> listarTotalVendaPorMes() {
		return vendas.totalPorMes();
	}
	
	@GetMapping("/porOrigem")
	public @ResponseBody List<VendaOrigem> vendasPorNacionalidade() {
		return this.vendas.totalPorOrigem();
	}
	
	private ModelAndView mvTabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItens.getItens(uuid));
		mv.addObject("valorTotal", tabelaItens.getValorTotal(uuid));
		return mv;
	}
	
	private void validarVenda(Venda venda, BindingResult result) {
		venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		vendaValidator.validate(venda, result);
	}
	
	private void setUuid(Venda venda) {
		if (StringUtils.isEmpty(venda.getUuid())) {
			venda.setUuid(UUID.randomUUID().toString());
		}
	}

}
