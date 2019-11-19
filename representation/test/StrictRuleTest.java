/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import representation.BinaryInferiorityConstraint;
import representation.Constraint;
import representation.RestrictedDomain;
import representation.StrictRule;
import representation.Variable;

/**
 *
 * @author ordinaute
 */
public class StrictRuleTest {

    public static void main(String[] args) {
        /*Variable x1 = new Variable("x1", "1", "2", "3");
        Variable x2 = new Variable("x2", "1", "2", "3");
        Variable x3 = new Variable("x3", "1", "2", "3");
        Variable x4 = new Variable("x3", "1", "2", "3");
        RestrictedDomain term1 = new RestrictedDomain(x1, "1");
        RestrictedDomain term2 = new RestrictedDomain(x2, "3");
        RestrictedDomain term3 = new RestrictedDomain(x3, "3");
        List<RestrictedDomain> premise = new ArrayList();
        premise.add(term1);
        List<RestrictedDomain> conclusion = new ArrayList();
        conclusion.add(term2);
        conclusion.add(term3);
        List<RestrictedDomain> assessment = new ArrayList();
        assessment.add(new RestrictedDomain(x1, "1"));
        assessment.add(new RestrictedDomain(x2, "3"));
        assessment.add(new RestrictedDomain(x3, "3"));
        assessment.add(new RestrictedDomain(x4, "2"));
        testIsSatisfiedBy(premise, conclusion, assessment);
*/
        Variable a = Variable.makeBooleanVariable("A");
        Variable b = Variable.makeBooleanVariable("B");
        Variable c = Variable.makeBooleanVariable("C");
        Variable d = Variable.makeBooleanVariable("D");
        Variable e = Variable.makeBooleanVariable("E");

        List<RestrictedDomain> premise = new ArrayList();
        List<RestrictedDomain> conclusion = new ArrayList();
        premise.add(new RestrictedDomain(b, Variable.TRUE));
        conclusion.add(new RestrictedDomain(a, Variable.TRUE));
        //conclusion.add(new RestrictedDomain(c, Variable.TRUE));
        StrictRule rule = new StrictRule(premise, conclusion);
        
        Map<Variable, String> assignment = new HashMap();
        assignment.put(a, Variable.TRUE);
        assignment.put(b, Variable.TRUE);
        assignment.put(c, Variable.TRUE);
        assignment.put(d, Variable.TRUE);
        //assignment.put(e, Variable.TRUE);
        
        System.out.println(rule.isSatisfiedBy(assignment));
    }

    static void testIsSatisfiedBy(List<RestrictedDomain> premise, List<RestrictedDomain> conclusion, List<RestrictedDomain> assessment) {
        Constraint strict = new StrictRule(premise, conclusion);
        System.out.println(strict.isSatisfiedBy(assessment));
    }
}
