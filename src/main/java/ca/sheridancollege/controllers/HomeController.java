/*
 * Home Controller
 * 
 * @author: Mark Zarak, Oct 2020
 */

package ca.sheridancollege.controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ca.sheridancollege.beans.Matrix;

@Controller
public class HomeController {
	
	ArrayList<Double> sparseMatrix = new ArrayList<>();

	// Home page
	@GetMapping("/") //localhost:8080
	public String goHome(Model model) {
		model.addAttribute("matrix", new Matrix());
		return "home.html";
	}
	
	// Create and display sparse matrix
	@GetMapping("/view") 
	public String doAddSparseMatrix(Model model) {
		model.addAttribute("matrix", new Matrix());
		
		return "view.html";
	}
}
