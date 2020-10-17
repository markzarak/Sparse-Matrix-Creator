/*
 * A Spring Boot and Thymeleaf enabled application that takes a user's input to
 * create, print, and display a sparse matrix array. On the server-side, the 
 * Compressed Sparse Row scheme is used to save space and speed up computations.
 * 
 * @author: Mark Zarak, Oct 2020
 */

package ca.sheridancollege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SparseMatrixCreatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparseMatrixCreatorApplication.class, args);
	}

}
