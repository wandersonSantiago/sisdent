package com.sisdent.repository.helper.servico;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.dto.ServicoDTO;
import com.sisdent.model.Servico;
import com.sisdent.repository.filter.ServicoFilter;

public interface ServicosQueries {

	public Page<Servico> filtrar(ServicoFilter filtro, Pageable pageable);
	
	public List<ServicoDTO> porCodigoOuNome(String codigoOuNome);
		
}
