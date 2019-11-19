/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package representation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RestrictedDomain {
    private Variable variable;
    private Set<String> subdomain;
    private boolean isBoolean;

    public RestrictedDomain(Variable variable, String... subdomain){
        this.variable = variable;
        this.subdomain = new HashSet();
        this.isBoolean = variable.isBoolean();
        for(String val: subdomain){
            this.subdomain.add(val);
        }
    }

    public RestrictedDomain(Variable variable, Set<String> subdomain){
        this.variable = variable;
        this.subdomain = subdomain;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public Set<String> getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(Set<String> subdomain) {
        this.subdomain = subdomain;
    }
    
    public boolean subDomainContains(Set<String> subsubdomain){
        boolean subdomainContains = true;
                for (Iterator ssdIt = subsubdomain.iterator(); ssdIt.hasNext();){
                    if (!subdomain.contains(ssdIt.next())){
                        subdomainContains = false;
                }                    
            }
        return subdomainContains;
    }
    
    public boolean isBoolean(){
        return this.isBoolean;
    }
    
    @Override
    public String toString(){
        String domainStr = this.variable.getName() + " : ";
        for (String val : this.getSubdomain()){
            domainStr += val + ", ";
        }
        return domainStr;
    }
}
