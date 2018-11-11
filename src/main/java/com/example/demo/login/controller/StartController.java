package com.example.demo.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StartController {
	@GetMapping("/")
	public String getLogin(Model model) {
		return "login/start";
	}
	@PostMapping("/")
	public String postLogin(Model model) {
		return "login/start";
	}

}
