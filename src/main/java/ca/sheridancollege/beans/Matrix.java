/*
 * Model
 * 
 * @author: Mark Zarak, Oct 2020
 */

package ca.sheridancollege.beans;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // Bean Law NoArgsConstructor
@AllArgsConstructor
@Data // Bean Law Getter & Setter
public class Matrix implements Serializable {

	private static final long serialVersionUID = -2663242228311901508L;
	private double rangeMin;
	private double rangeMax;
	private int mRows;
	private int nColumns;
	double precisionThreshold;

}
