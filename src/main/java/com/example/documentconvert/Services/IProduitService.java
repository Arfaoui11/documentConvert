package com.example.documentconvert.Services;

import org.springframework.core.io.ByteArrayResource;


public interface IProduitService {

	ByteArrayResource convertDocxToPDF();

	void convertDocx();
	void convert() throws Exception;



}
