/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation.test;

import java.util.ArrayList;
import java.util.List;
import representation.Constraint;
import representation.BinaryInferiorityConstraint;
import representation.RestrictedDomain;
import representation.Variable;

/**
 *
 * @author ordinaute
 */
public class InferirorityConstraintTest {

    public static void main(String[] args) {
        Variable x1 = new Variable("x1", "1", "2", "3");
        Variable x2 = new Variable("x2", "1", "2", "3");
        Variable x3 = new Variable("x2", "1", "2", "3");
        RestrictedDomain term1 = new RestrictedDomain(x1, "2");
        RestrictedDomain term2 = new RestrictedDomain(x2, "3");
        List<RestrictedDomain> assessment = new ArrayList();
        assessment.add(term1);
        assessment.add(term2);
        assessment.add(new RestrictedDomain(x3, "oui"));
        testIsSatisfiedBy(term1, term2, assessment);
    }

    static void testIsSatisfiedBy(RestrictedDomain term1, RestrictedDomain term2, List<RestrictedDomain> assessment) {
        Constraint inferior = new BinaryInferiorityConstraint(term1, term2);
        System.out.println(inferior.isSatisfiedBy(assessment));
    }
}
