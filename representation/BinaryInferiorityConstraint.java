/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ordinaute
 */
public class BinaryInferiorityConstraint extends BinaryConstraint {

    public BinaryInferiorityConstraint(RestrictedDomain term1, RestrictedDomain term2) {
        super(term1, term2);
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, String> assessment) {
        boolean isSatisfied = true;
        Variable var1 = term1.getVariable();
        Variable var2 = term2.getVariable();
        if (assessment.containsKey(var1) && assessment.containsKey(var2)) {
            int val1 = Integer.valueOf(assessment.get(var1));
            int val2 = Integer.valueOf(assessment.get(var2));
            isSatisfied = (val1 < val2);
        }
        return isSatisfied;
    }

    @Override
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment) throws IllegalArgumentException {
        boolean isSatisfied = true;
        Variable var1 = term1.getVariable();
        Variable var2 = term2.getVariable();
        for (RestrictedDomain domain1 : assessment) {
            if (domain1.getVariable() == var1) {
                if (domain1.getSubdomain().size() == 1) {
                    for (RestrictedDomain domain2 : assessment) {
                        if (domain2.getVariable() == var2) {
                            if(domain2.getSubdomain().size() == 1){
                                int val1 = Integer.valueOf(domain1.getSubdomain().iterator().next());
                                int val2 = Integer.valueOf(domain2.getSubdomain().iterator().next());
                                isSatisfied = (val1 < val2);                              
                            } else {
                                throw new IllegalArgumentException();
                            }
                        }
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
        return isSatisfied;
    }
}
