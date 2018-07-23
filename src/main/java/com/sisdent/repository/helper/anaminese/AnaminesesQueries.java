package com.sisdent.repository.helper.anaminese;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sisdent.model.Anaminese;
import com.sisdent.repository.filter.AnamineseFilter;

public interface AnaminesesQueries {

	public Page<Anaminese> filtrar(AnamineseFilter filtro, Pageable pageable);
	
		
}
