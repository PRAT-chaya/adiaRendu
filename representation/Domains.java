/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ordinaute
 */
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
