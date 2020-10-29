/*
 * Home Controller
 * 
 * @author: Mark Zarak, Oct 2020
 */

package ca.sheridancollege.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	// Create sparse matrix and display associated arrays
	@GetMapping("/view")
	public String doAddSparseMatrix(Model model, @ModelAttribute Matrix matrix, @RequestParam double rangeMin,
			@RequestParam double rangeMax, @RequestParam int mRows, @RequestParam int nColumns,
			@RequestParam double precisionThreshold, @RequestParam String compressedSparseRow) {

		// Assign default row and column values for array if none given by user
		boolean displayDefault = false;
		if (mRows == 0 && nColumns == 0 && rangeMin == 0 && rangeMax == 0) {
			mRows = 4;
			nColumns = 5;
			displayDefault = true;
		}

		// Create empty array for sparse matrix
		double[][] sparseMatrix = new double[mRows][nColumns];

		// Create default sparse matrix from text file if user does not enter any values
		if (displayDefault) {
			try {
				File myObj = new File("SparseMatrixInput.txt");
				Scanner myReader = new Scanner(myObj);
				int[] fileData = new int[25];
				int i = 0;
				while (myReader.hasNextInt()) {
					fileData[i] = myReader.nextInt();
					i++;
				}
				// Default sparse matrix data in SparseMatrixInput.txt begins on second index
				i = 2;
				for (int floor = 0; floor < mRows; ++floor) {
					for (int room = 0; room < nColumns; ++room) {
						sparseMatrix[floor][room] = fileData[i];
						i++;
					}
				}
				myReader.close();
				System.out.println("Successfully read data from SparseMatrixInput.txt");
			} catch (FileNotFoundException e) {
				System.out.println("An error reading data from SparseMatrixInput.txt occurred.");
				e.printStackTrace();
			}
		} else {
			// Fill sparse matrix based on user input
			for (int floor = 0; floor < mRows; ++floor) {
				for (int room = 0; room < nColumns; ++room) {
					// Probability of adding 0 based on user threshold value
					Random rand = new Random();
					double probability = rand.nextDouble();
					if (probability <= precisionThreshold) {
						sparseMatrix[floor][room] = 0;
					} else {
						// If 0 was not added, then add randomly generated number
						double randomNumber = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
						sparseMatrix[floor][room] = randomNumber;
					}
				}
			}
		}
		// Add sparseMatrix array to model to save data
		model.addAttribute("generatedElements", sparseMatrix);

		// Print sparse matrix to text file
		try {
			FileWriter file = new FileWriter("SparseMatrixOutput.txt", true);
			BufferedWriter output = new BufferedWriter(file);
			output.write("***Sparse Matrix A***");
			output.newLine();
			for (int floor = 0; floor < sparseMatrix.length; ++floor) {
				for (int room = 0; room < sparseMatrix[0].length; ++room) {
					output.write(String.format("%.0f. ", +sparseMatrix[floor][room]));
				}
				output.newLine();
			}
			output.newLine();
			output.flush();
			output.close();
			System.out.println("Successfully wrote data to SparseMatrixOutput.txt");
		} catch (IOException e) {
			System.out.println("An error outputting data to SparseMatrixOutput.txt occurred.");
			e.printStackTrace();
		}

		// Create array based on checkbox selection:
		// Create array V
		if (compressedSparseRow.contains("v")) {
			String arrayVName = "Array V";
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
			// Add array V to model
			model.addAttribute("arrayVName", arrayVName);
			model.addAttribute("arrayV", arrayV);
		}

		// Create array J
		if (compressedSparseRow.contains("j")) {
			String arrayJName = "Array J";
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
			// Add array J to model
			model.addAttribute("arrayJName", arrayJName);
			model.addAttribute("arrayJAttribute", arrayJ);
		}

		// Create array I
		if (compressedSparseRow.contains("i")) {
			String arrayIName = "Array I";
			ArrayList<Integer> arrayI = new ArrayList<>();
			// indexOfArrayV is used to keep track of the non zero-numbers added to Array V
			int indexOfArrayV = 0;
			for (int floor = 0; floor < sparseMatrix.length; ++floor) {
				boolean found = false;
				for (int room = 0; room < sparseMatrix[floor].length; ++room) {
					if (sparseMatrix[floor][room] != 0) {
						// If a zero-number has been found, allow only first entry per row from
						// sparseMatrix
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
			// Add array I to model
			model.addAttribute("arrayIName", arrayIName);
			model.addAttribute("arrayIAttribute", arrayI);
		}
		return "home.html";
	}
}
