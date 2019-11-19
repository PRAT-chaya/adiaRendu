package examples;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ppc.*;
import representation.Constraint;
import representation.RestrictedDomain;
import representation.Rule;
import representation.Variable;


public class Test {

	public static void main(String[] args) {

		// Variables ===================================================

		Variable x0 = Variable.makeBooleanVariable("x0");
		Variable x1 = new Variable("x1", "0", "1");
		Variable x2 = new Variable("x2", "0", "1", "2");
		Variable x3 = new Variable("x3", "0", "1", "2", "3", "6");

		Set<Variable> allVariables = new HashSet<>(Arrays.asList(x0, x1, x2, x3));

		// Rule : (!x0 && x1=0) -> (new RestrictedDomain(x2=1 || x3=2)
		// ======================

		List<RestrictedDomain> premise = new ArrayList<>();
		premise.add(new RestrictedDomain(x0, Variable.FALSE));
		premise.add(new RestrictedDomain(x1, "0"));

		List<RestrictedDomain> conclusion = new ArrayList<>();
		conclusion.add(new RestrictedDomain(x2, "1"));
		conclusion.add(new RestrictedDomain(x3, "2", "6"));

		Rule rule = new Rule(premise, conclusion);

		// Assignments =================================================

		List<RestrictedDomain> satisfying1 = new ArrayList<>();
		satisfying1.add(new RestrictedDomain(x0, Variable.FALSE));
		satisfying1.add(new RestrictedDomain(x1, "0"));
		satisfying1.add(new RestrictedDomain(x2, "1"));
		satisfying1.add(new RestrictedDomain(x3, "2"));

		List<RestrictedDomain> satisfying2 = new ArrayList<>();
		satisfying2.add(new RestrictedDomain(x0, Variable.FALSE));
		satisfying2.add(new RestrictedDomain(x1, "1"));
		satisfying2.add(new RestrictedDomain(x2, "0"));
		satisfying2.add(new RestrictedDomain(x3, "0"));

		List<RestrictedDomain> satisfying3 = new ArrayList<>();
		satisfying3.add(new RestrictedDomain(x0, Variable.TRUE));
		satisfying3.add(new RestrictedDomain(x1, "1"));
		satisfying3.add(new RestrictedDomain(x2, "1"));
		satisfying3.add(new RestrictedDomain(x3, "2"));

		List<RestrictedDomain> falsifying = new ArrayList<>();
		falsifying.add(new RestrictedDomain(x0, Variable.FALSE));
		falsifying.add(new RestrictedDomain(x1, "0"));
		falsifying.add(new RestrictedDomain(x2, "2"));
		falsifying.add(new RestrictedDomain(x3, "1"));
		// Tests =======================================================

		System.out.print("Testing scope: ");
		System.out.println(rule.getScope().equals(allVariables) ? "OK" : "KO");
		System.out.println(rule);

		System.out.print("Testing satisfying assignment: ");
		System.out.println(satisfying1);
		System.out.println(rule.isSatisfiedBy(satisfying1) ? "OK" : "KO");

		System.out.print("Testing satisfying assignment: ");
		System.out.println(satisfying2);
		System.out.println(rule.isSatisfiedBy(satisfying2) ? "OK" : "KO");

		System.out.print("Testing satisfying assignment: ");
		System.out.println(satisfying3);
		System.out.println(rule.isSatisfiedBy(satisfying3) ? "OK" : "KO");

		System.out.print("Testing falsifying assignment: ");
		System.out.println(falsifying);
		System.out.println(!rule.isSatisfiedBy(falsifying) ? "OK" : "KO");

		// BACKTRACK
		Set<Constraint> constraints;
		BacktrackSearch search;
		List<RestrictedDomain> solution;
		Set<List<RestrictedDomain>> allSolutions;

		constraints = new HashSet<>(Arrays.asList(rule));
		System.out.print("Testing search: ");
		search = new BacktrackSearch(constraints);
		solution = search.solution();
		System.out.println(solution);

		allSolutions = search.allSolutions();
		System.out.println("Nombre de solutions = " + allSolutions.size());
		for (List<RestrictedDomain> sol : allSolutions)
			System.out.println(sol);
	}

}
