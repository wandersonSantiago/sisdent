package com.sisdent.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RelatorioUtil {
	
	
	public  String caminhoOrcamento() {
		return "/relatorios/orcamento.jrxml";
	}
	
	
	public  HashMap<String, Object> caminhoMapaDeLogos(String titulo) {
		HashMap<String, Object> hashMap = new HashMap<>();
		/*hashMap.put("Sap", getClass().getResource("/relatorio/imagens/sap.jpg").toString());
		hashMap.put("Brasao", getClass().getResource("/relatorio/imagens/brasao.jpg").toString());
		hashMap.put("Corte", getClass().getResource("/relatorio/imagens/corte.jpg").toString());
		hashMap.put("LogoGpu", getClass().getResource("/relatorio/imagens/logo_gpu.jpg").toString());
		hashMap.put("Titulo", titulo);*/
		return hashMap;
	}

}
