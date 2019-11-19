/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Domains {

    private List<RestrictedDomain> domains;

    public Domains(List<RestrictedDomain> domains) {
        this.domains = domains;
    }
    
    public Domains(Map<Variable, Set<String>> domains){
        this.domains = new ArrayList();
        for(Variable var : domains.keySet()){
            this.domains.add(new RestrictedDomain(var, domains.get(var)));
        }
    }

    public List<RestrictedDomain> getDomains() {
        return domains;
    }

    public RestrictedDomain getDomain(Variable var) throws IllegalArgumentException {
        if (this.containsVar(var)) {
            for (RestrictedDomain domain : this.domains) {
                if (domain.getVariable() == var) {
                    return domain;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    public boolean containsVar(Variable var) {
        for (RestrictedDomain domain : this.domains) {
            if (domain.getVariable() == var) {
                return true;
            }
        }
        return false;
    }
}
