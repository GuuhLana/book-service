package com.glrtech.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.glrtech.model.Book;
import com.glrtech.proxy.CambioProxy;
import com.glrtech.repository.BookRepository;
import com.glrtech.response.Cambio;

@RestController
@RequestMapping("book-service")
public class BookController {

	@Autowired
	private Environment environment;

	@Autowired
	private BookRepository repository;
	
	@Autowired
	private CambioProxy cambioProxy;

//	 Exemplo de requisição -> http://localhost:8000/book-service/1/BRL
//	@GetMapping(value = "/{id}/{currency}")
//	public Book findBook(
//			@PathVariable("id") Long id, 
//			@PathVariable("currency") String currency) {
//		
//		var book = repository.findById(id);
//		if (book == null) throw new RuntimeException("Book not found");
//		
//		HashMap<String, String> params = new HashMap<>();
//		params.put("amount", book.get().getPrice().toString());
//		params.put("from", "USD");
//		params.put("to", currency);
//		
//		var response = new RestTemplate().getForEntity(
//				"http://localhost:8000/cambio-service/"
//				+ "{amount}/{from}/{to}", 
//				Cambio.class, 
//				params);
//		var cambio = response.getBody();
//		
//		var port = environment.getProperty("local.server.port");
//		Book newBook = book.get();
//		
//		newBook.setEnviroment(port);
//		newBook.setPrice(cambio.getConvertedValue());
//		
//		return newBook;
//	}
	@GetMapping(value = "/{id}/{currency}")
	public Book findBook(
			@PathVariable("id") Long id, 
			@PathVariable("currency") String currency) {
		
		var book = repository.findById(id);
		if (book == null) throw new RuntimeException("Book not found");

		
		var cambio = cambioProxy.getCambio(book.get().getPrice(), "USD", currency);
		
		var port = environment.getProperty("local.server.port");
		Book newBook = book.get();
		
		newBook.setEnviroment(port + " FEIGN");
		newBook.setPrice(cambio.getConvertedValue());
		
		return newBook;
	}
}
