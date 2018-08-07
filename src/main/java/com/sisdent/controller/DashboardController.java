package com.sisdent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sisdent.repository.Clientes;
import com.sisdent.repository.LancamentoRepository;
import com.sisdent.repository.Produtos;
import com.sisdent.repository.Vendas;

@Controller
public class DashboardController {

	@Autowired
	private Vendas vendas;
	
	@Autowired
	private Produtos produtos;
	
	@Autowired
	private Clientes clientes;
	
	@Autowired
	private LancamentoRepository lancamentos;
	
	@GetMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("Dashboard");
		
		mv.addObject("vendasNoAno", vendas.valorTotalNoAno());
		mv.addObject("vendasNoMes", vendas.valorTotalNoMes());
		mv.addObject("ticketMedio", vendas.valorTicketMedioNoAno());
		
		mv.addObject("valorItensEstoque", produtos.valorItensEstoque());
		mv.addObject("totalClientes", clientes.count());
		
		mv.addObject("totalAPagar", lancamentos.totalcontasAPagar());
		mv.addObject("totalAReceber", lancamentos.totalContasAReceber());
		mv.addObject("totalSaldo", lancamentos.saldo());
		
		return mv;
	}
	
}
