/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satisfiability.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import representation.BinaryEqualityConstraint;
import representation.Constraint;
import representation.Domains;
import representation.BinaryInferiorityConstraint;
import representation.RestrictedDomain;
import representation.Rule;
import representation.Variable;
import satisfiability.GeneralizedArcConsistency;

/**
 *
 * @author ordinaute
 */
public class GACTest {

    public static void main(String[] args) {
        Variable x1 = new Variable("x1", "1", "2", "3");
        Variable x2 = new Variable("x2", "1", "2", "3");
        Variable x3 = new Variable("x3", "1", "2", "3");
        RestrictedDomain term1 = new RestrictedDomain(x1, "2");
        RestrictedDomain term2 = new RestrictedDomain(x2, "3");
        Constraint inferior = new BinaryInferiorityConstraint(term1, new RestrictedDomain(x2, x2.getDomain()));
        Constraint equal = new BinaryEqualityConstraint(
                new RestrictedDomain(x2, x2.getDomain()),
                new RestrictedDomain(x3, x3.getDomain()));
        List<RestrictedDomain> domains = new ArrayList();
        domains.add(new RestrictedDomain(x1, x1.getDomain()));
        domains.add(new RestrictedDomain(x2, x2.getDomain()));
        domains.add(new RestrictedDomain(x3, x3.getDomain()));
        List<RestrictedDomain> premise = new ArrayList();
        premise.add(new RestrictedDomain(x1, "2"));
        premise.add(new RestrictedDomain(x2, "1"));
        List<RestrictedDomain> conclusion = new ArrayList();
        conclusion.add(new RestrictedDomain(x3, "3"));
        Constraint rule = new Rule(premise, conclusion);

        //testIsConsistent(inferior, x2, "2", domains);
        //testIsConsistent(rule, x1, "2", domains);
        Set<Constraint> constraints = new HashSet();
        constraints.add(inferior);
        constraints.add(equal);
        testEnforceArcConsistency(constraints, domains);
    }

    static void testIsConsistent(Constraint constraint, Variable var, String value,
            List<RestrictedDomain> domainsList) {
        Domains domains = new Domains(domainsList);
        System.out.println(
                GeneralizedArcConsistency.isConsistent(var, value, constraint, domains)
        );
    }

    static void testEnforceArcConsistency(Set<Constraint> constraints, List<RestrictedDomain> domains) {
        GeneralizedArcConsistency gac = new GeneralizedArcConsistency(constraints);
        displayDomains(domains);
        gac.enforceArcConsistency(domains);
        displayDomains(domains);
    }
    
    static void displayDomains(List<RestrictedDomain> domains){
        for (RestrictedDomain domain : domains){
            System.out.print(domain.getVariable().getName() + " [");
            for (String value : domain.getSubdomain()){
                System.out.print(value + " ");
            }
            System.out.print("]");
            System.out.println();
        }
    }
}
