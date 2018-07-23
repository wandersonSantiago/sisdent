package com.sisdent.repository.listener;

import javax.persistence.PostLoad;

import com.sisdent.SisDentApplication;
import com.sisdent.model.Produto;
import com.sisdent.storage.FotoStorage;

public class ProdutoEntityListener {

	@PostLoad
	public void postLoad(final Produto produto) {
		FotoStorage fotoStorage = SisDentApplication.getBean(FotoStorage.class);
		
	//	produto.setUrlFoto(fotoStorage.getUrl(produto.getFotoOuMock()));
	//	produto.setUrlThumbnailFoto(fotoStorage.getUrl(FotoStorage.THUMBNAIL_PREFIX + produto.getFotoOuMock()));
	}
	
}
