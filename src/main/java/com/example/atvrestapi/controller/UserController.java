package com.example.atvrestapi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.atvrestapi.model.User;
import com.example.atvrestapi.repository.UserRepository;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public List<User> getUsers(@RequestBody LinkedHashMap<String, String[]> request) {
		List<User> res = null;
		
		// Caso não tenha parametros no request ou a lista de usuárias esteja vazia: Retorna todos os usuários cadastrados por ordem
		if(request.isEmpty()) {
			res = userRepository.findAll();
		}
		else if(request.containsKey("users")) {
			
			if(request.get("users").length == 0)
				res = userRepository.findAll();
			
			else if(request.get("users").length > 0) {
				res = new ArrayList<User>(request.get("users").length);
				for(String user : request.get("users")) {
					User target = userRepository.findByName(user);
					if(target != null)
						res.add(target);
				}
			}
		}
		res.sort((User u1, User u2) -> u1.getName().compareTo(u2.getName()));
		
		return res;
		
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User user) {
		// Validação da requisição
		if(user == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		else if(user.getName().isBlank() || user.getName().isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		// Procura o nome do usuário no banco de dados
		User target = userRepository.findByName(user.getName());
		if(target != null)
			return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
		
		// Retorna o usuário caso esteja tudo certo
		return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
	}

	@RequestMapping(value="/iou", method=RequestMethod.POST)
	public ResponseEntity<Object> createIOU(@RequestBody LinkedHashMap<String, Object> request){
		if(request.isEmpty()) {
			return new ResponseEntity<>("Request is empty", HttpStatus.BAD_REQUEST);
		}
		
		// Valida os campos lender, barrower e amount
		if(!request.containsKey("lender")) {
			return new ResponseEntity<>("No lender found in request", HttpStatus.BAD_REQUEST);
		}
		else if(!(request.get("lender") instanceof String)) {
			return new ResponseEntity<>("Lender must be a string", HttpStatus.BAD_REQUEST);
		}
		
		if(!request.containsKey("borrower")) {
			return new ResponseEntity<>("No borrower found in request", HttpStatus.BAD_REQUEST);
		}
		else if(!(request.get("borrower") instanceof String)) {
			return new ResponseEntity<>("Borrower must be a string", HttpStatus.BAD_REQUEST);
		}
		
		if(!request.containsKey("amount")) {
			return new ResponseEntity<>("No amount found in request", HttpStatus.BAD_REQUEST);
		}
		else if(!(request.get("amount") instanceof Double)) {
			return new ResponseEntity<>("Amount must be a double", HttpStatus.BAD_REQUEST);
		}
		
		User lender = userRepository.findByName((String)request.get("lender"));
		User borrower = userRepository.findByName((String)request.get("borrower"));
		Double amount = (Double)request.get("amount");
		
		lender.updateValue(true, borrower.getName(), amount);
		borrower.updateValue(false, lender.getName(), amount);
		
		List<User> res = new ArrayList<User>(Arrays.asList(lender, borrower));
		userRepository.saveAll(res);
		res.sort((User u1, User u2) -> u1.getName().compareTo(u2.getName()));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
