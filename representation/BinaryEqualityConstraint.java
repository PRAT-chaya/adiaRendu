/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ordinaute
 */
public class BinaryEqualityConstraint extends BinaryConstraint {

    public BinaryEqualityConstraint(RestrictedDomain term1, RestrictedDomain term2) {
        super(term1, term2);
    }

    @Override
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment) throws IllegalArgumentException {
        Variable var1 = this.term1.getVariable();
        Variable var2 = this.term2.getVariable();
        for (RestrictedDomain rd1 : assessment) {
            if (rd1.getVariable() == var1) {
                if (rd1.getSubdomain().size() > 1) {
                    throw new IllegalArgumentException();
                }
                for (RestrictedDomain rd2 : assessment) {
                    if (rd2.getVariable() == var2) {
                        if (rd2.getSubdomain().size() > 1) {
                            throw new IllegalArgumentException();
                        }
                        return (rd1.getSubdomain().iterator().next()
                                .equals(rd2.getSubdomain().iterator().next()));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, String> assessment) {
        Variable var1 = this.term1.getVariable();
        Variable var2 = this.term2.getVariable();
        if (assessment.containsKey(var1) && assessment.containsKey(var2)) {
            return (assessment.get(var1).equals(assessment.get(var2)));
        } else {
            return true;
        }
    }

    @Override
    public boolean filter(List<RestrictedDomain> variables, Map<Variable, Set<String>> assessment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
