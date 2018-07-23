package com.sisdent.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sisdent.model.Categoria;

@Component
public class EstiloConverter implements Converter<String, Categoria> {

	@Override
	public Categoria convert(String codigo) {
		if (!StringUtils.isEmpty(codigo)) {
			Categoria estilo = new Categoria();
			estilo.setCodigo(Long.valueOf(codigo));
			return estilo;
		}
		
		return null;
	}

}
