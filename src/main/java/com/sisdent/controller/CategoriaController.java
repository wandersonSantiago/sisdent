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
import com.sisdent.model.Categoria;
import com.sisdent.model.Produto;
import com.sisdent.repository.Categorias;
import com.sisdent.repository.filter.CategoriaFilter;
import com.sisdent.service.CategoriaService;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;
import com.sisdent.service.exception.NomeOuCodigoJaCadastradoException;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private Categorias categorias;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Categoria categoria) {
		ModelAndView mv = new ModelAndView("categoria/categoria.form");
		return mv;
	}
	
	@RequestMapping(value = { "/novo", "{\\d+}" }, method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Categoria categoria, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(categoria);
		}
		
		try {
			categoriaService.salvar(categoria);
		} catch (NomeOuCodigoJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(categoria);
		}
		
		attributes.addFlashAttribute("mensagem", "Categoria salva com sucesso");
		return new ModelAndView("redirect:/categorias/novo");
	}
	
	@RequestMapping(value =  "/editar/{codigo}", method = RequestMethod.POST)
	public ModelAndView editar(@Valid Categoria categoria, BindingResult result, RedirectAttributes attributes) {  
		if (result.hasErrors()) {
			return novo(categoria);
		}
		try {
			categoriaService.salvar(categoria);
		} catch (NomeOuCodigoJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(categoria);
		}
		
		attributes.addFlashAttribute("mensagem", "Editada com sucesso");
		return new ModelAndView("redirect:/categorias");
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Categoria categoria, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
		}
		
		categoria = categoriaService.salvar(categoria);
		return ResponseEntity.ok(categoria);
	}
	
	@GetMapping
	public ModelAndView pesquisar(CategoriaFilter categoriaFilter, BindingResult result
			, @PageableDefault(size = 24) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("categoria/categoria.list");
		
		PageWrapper<Categoria> paginaWrapper = new PageWrapper<>(categorias.filtrar(categoriaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Categoria categoria) {
		try {
			categoriaService.excluir(categoria);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Categoria categoria = categoriaService.findOne(codigo);
		ModelAndView mv = novo(categoria);
		mv.addObject(categoria);
		return mv;
	}
	
}
