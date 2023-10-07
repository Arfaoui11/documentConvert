package com.example.documentconvert.Controller;



import com.example.documentconvert.Services.IProduitService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;


@RestController
@Api(tags = "Gestion des produits")
@RequestMapping(value = "/produit",produces = "application/json;charset=UTF-8")
public class ProduitRestController {

	@Autowired
	IProduitService produitService;



	@GetMapping("/pdf")
	@ResponseBody
	public ResponseEntity<ByteArrayResource> generatePdf() {
	try {
		ByteArrayResource fileResponse = produitService.convertDocxToPDF();
		if (fileResponse != null)
		{
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION)
					.body(fileResponse);
		}

	}catch (HttpClientErrorException ex)
	{
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	}

	@GetMapping("/savePDF")
	@ResponseBody
	public ResponseEntity<HttpStatus> generateAndSavePdf() {
		try {
			produitService.convertDocx();
			return new ResponseEntity<>(HttpStatus.CREATED);
		}catch (HttpClientErrorException ex)
		{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}






}
