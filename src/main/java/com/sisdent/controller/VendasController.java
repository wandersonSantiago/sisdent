package com.sisdent.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.sisdent.mail.Mailer;
import com.sisdent.model.ItemVenda;
import com.sisdent.model.Produto;
import com.sisdent.model.StatusVenda;
import com.sisdent.model.TipoPessoa;
import com.sisdent.model.Venda;
import com.sisdent.repository.Produtos;
import com.sisdent.repository.Vendas;
import com.sisdent.repository.filter.VendaFilter;
import com.sisdent.security.UsuarioSistema;
import com.sisdent.service.CadastroVendaService;
import com.sisdent.session.TabelasItensSession;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private Produtos cervejas;
	
	@Autowired
	private TabelasItensSession tabelaItens;
	
	@Autowired
	private CadastroVendaService cadastroVendaService;
	
	@Autowired
	private VendaValidator vendaValidator;
	
	@Autowired
	private Vendas vendas;
	
	@Autowired
	private Mailer mailer;
	
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
	public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		//validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Orçamento salva com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}

	@PostMapping(value = "/nova", params = "emitir")
	public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.emitir(venda);
		attributes.addFlashAttribute("mensagem", "Orçamento emitida com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping(value = "/nova", params = "enviarEmail")
	public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		//validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		venda = cadastroVendaService.salvar(venda);
		//mailer.enviar(venda);
		
		attributes.addFlashAttribute("mensagem", String.format("Orçamento nº %d salva com sucesso e e-mail enviado", venda.getCodigo()));
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping("/item")
	public ModelAndView adicionarItem(Long codigoCerveja, String uuid) {
		Produto cerveja =  cervejas.getOne(codigoCerveja);
		tabelaItens.adicionarItem(uuid, cerveja, 1);
		return mvTabelaItensVenda(uuid);
	}
	
	@PutMapping("/item/{codigoCerveja}")
	public ModelAndView alterarQuantidadeItem(@PathVariable("codigoCerveja") Produto cerveja
			, Integer quantidade, String uuid) {
		tabelaItens.alterarQuantidadeItens(uuid, cerveja, quantidade);
		return mvTabelaItensVenda(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{codigoCerveja}")
	public ModelAndView excluirItem(@PathVariable("codigoCerveja") Produto cerveja
			, @PathVariable String uuid) {
		tabelaItens.excluirItem(uuid, cerveja);
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
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Venda venda = vendas.buscarComItens(codigo);
		
		setUuid(venda);
		for (ItemVenda item : venda.getItens()) {
			tabelaItens.adicionarItem(venda.getUuid(), item.getProduto(), item.getQuantidade());
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
		//venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		//vendaValidator.validate(venda, result);
	}
	
	private void setUuid(Venda venda) {
		if (StringUtils.isEmpty(venda.getUuid())) {
			venda.setUuid(UUID.randomUUID().toString());
		}
	}

}
