/*
 * Home Controller
 * 
 * @author: Mark Zarak, Oct 2020
 */

package ca.sheridancollege.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.beans.Matrix;

@Controller
public class HomeController {

	// Home page
	@GetMapping("/") // localhost:8080
	public String goHome(Model model) {

		model.addAttribute("matrix", new Matrix());
		return "home.html";
	}

	// Create sparse matrix
	@GetMapping("/view")
	public String doAddSparseMatrix(Model model, @RequestParam double rangeMin, @RequestParam double rangeMax,
			@RequestParam int mRows, @RequestParam int nColumns, @RequestParam double precisionThreshold) {

		model.addAttribute("matrix", new Matrix());
		Random rand = new Random();

		// Create empty array for sparse matrix
		double[][] sparseMatrix = new double[mRows][nColumns];

		System.out.println("***Sparse Matrix A***");
		// Fill sparse matrix based on user input
		for (int floor = 0; floor < mRows; ++floor) {
			for (int room = 0; room < nColumns; ++room) {
				// Probability of adding 0 based on user threshold value
				double probability = rand.nextDouble();
				if (probability <= precisionThreshold) {
					sparseMatrix[floor][room] = 0;
					DecimalFormat decimalFormat=new DecimalFormat("#");
					System.out.print(decimalFormat.format(sparseMatrix[floor][room]) + " ");
				} else {
					// If 0 was not added, then add randomly generated number
					double randomNumber = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
					sparseMatrix[floor][room] = randomNumber;
					System.out.print(String.format("%.0f. ", +sparseMatrix[floor][room]));
				}
			}
			System.out.println("");
		}
		System.out.println("");
		
		// Add sparseMatrix array to model to save data
//		model.addAttribute("generatedElements", sparseMatrix);
		// Print sparse matrix to text file

		// Create array V
		System.out.println("***Array V***");
		ArrayList<Double> arrayV = new ArrayList<>();
		for (int floor = 0; floor < sparseMatrix.length; ++floor) {
			for (int room = 0; room < sparseMatrix[floor].length; ++room) {
				double currentNum = sparseMatrix[floor][room];
				// Add number to arrayV if not zero, above 1, or below -1
				if (currentNum != 0 && (currentNum >= 1 || currentNum <= -1)) {
					arrayV.add(currentNum);
				}
				// Round up if not zero, below 1, and above 0 
				if (currentNum != 0 && (currentNum < 1 && currentNum > 0)) {
					arrayV.add(Math.ceil(currentNum));
				}
				// Round down if not zero, less than 0, and above -1
				if (currentNum != 0 && (currentNum < 0 && currentNum > -1)) {
					arrayV.add(Math.floor(currentNum));
				}
			}
		}

		// Print array V
		for (int i = 0; i < arrayV.size(); ++i) {
			System.out.print(String.format("%.0f. ", +arrayV.get(i)));
		}
		System.out.println("");

		// Create array J
		System.out.println("");
		System.out.println("***Array J***");
		ArrayList<Integer> arrayJ = new ArrayList<>();
		// indexJ is used to keep track of the beginning of each line in sparseMatrix
		int indexJ = 0;
		for (int floor = 0; floor < sparseMatrix.length; ++floor) {
			for (int room = 0; room < sparseMatrix[floor].length; ++room) {
				if (sparseMatrix[floor][room] != 0) {
					arrayJ.add(indexJ);
				}
				indexJ++;
			}
			// Reset indexJ to count from the beginning of the next line
			indexJ = 0;
		}

		// Print array J
		for (int i = 0; i < arrayJ.size(); ++i) {
			System.out.print(arrayJ.get(i) + " ");
		}
		System.out.println("");

		// Create array I
		System.out.println("");
		System.out.println("***Array I***");
		ArrayList<Integer> arrayI = new ArrayList<>();
		// indexOfArrayV is used to keep track of the non zero-numbers added to Array V
		int indexOfArrayV = 0;
		for (int floor = 0; floor < sparseMatrix.length; ++floor) {
			boolean found = false;
			for (int room = 0; room < sparseMatrix[floor].length; ++room) {
				if (sparseMatrix[floor][room] != 0) {
					// If a zero-number has been found, allow only first entry per row from sparseMatrix
					if (found == false) {
					arrayI.add(indexOfArrayV);
					found = true;
					}
					indexOfArrayV++;
				}
			}
		}
		// Point last element in arrayI to the start of a fictitious (m + 1) row
		arrayI.add(indexOfArrayV);

		// Print array I
		for (int i = 0; i < arrayI.size(); ++i) {
			System.out.print(arrayI.get(i) + " ");
		}
		System.out.println("");

		return "home.html";
	}

}
