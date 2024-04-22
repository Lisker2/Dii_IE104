package org.example.util.Examples;
import org.example.util.AbstractClasses.HyperHeuristic;
import org.example.util.AbstractClasses.ProblemDomain;
import org.example.util.HHs.adaphh.acceptance.AcceptanceCriterionType;
import org.example.util.HHs.adaphh.hyperheuristic.GIHH;
import org.example.util.HHs.adaphh.selection.SelectionMethodType;
import org.example.util.HHs.adaphh.util.Vars;
import org.example.util.PersonnelScheduling.PersonnelScheduling;
import org.example.util.ASAP.NRP.Core.Tools.RosterPrinter;
import org.example.util.travelingSalesmanProblem.TSP;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * This class shows how to run a selected hyper-heuristic on a selected problem domain.
 * It shows the minimum that must be done to test a hyper heuristic on a problem domain, and it is 
 * intended to be read before the ExampleRun2 class, which provides an example of a more complex set-up
 */
public class ExampleRun1 {

	public static void main(String[] args) {
		TSP problem = new TSP(1234,"china");

		HyperHeuristic hyper_heuristic_object = new GIHH(5678,problem.getNumberOfHeuristics(),1000,"", SelectionMethodType.AdaptiveLimitedLAassistedDHSMentorSTD,
				AcceptanceCriterionType.AdaptiveIterationLimitedListBasedTA);
		problem.loadInstance(0);
		hyper_heuristic_object.setTimeLimit(1000);
		hyper_heuristic_object.loadProblemDomain(problem);
		hyper_heuristic_object.run();
		System.out.println(hyper_heuristic_object.getBestSolutionValue());
		int[] vertexLocations = problem.bestSoFar.permutation;
		double[][] adjacencyMatrix = problem.instance.coordinates;
		String fileName = "data.txt";
		try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
			// Write vertexLocations
			for (int location : vertexLocations) {
				writer.print(location + " ");
			}
			writer.println();

			// Write adjacencyMatrix
			for (double[] row : adjacencyMatrix) {
				for (double value : row) {
					writer.print(value + " ");
				}
				writer.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
