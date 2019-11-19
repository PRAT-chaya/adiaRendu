/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */

package representation;

import java.util.HashSet;
import java.util.Set;

public class Variable {

    public static final String FALSE = "non";
    public static final String TRUE = "oui";
    private String name;
    private Set<String> domain;
    private boolean isBoolean;

    public Variable(String name, String... domain) {
        this.name = name;
        this.domain = new HashSet();
        this.isBoolean = false;
        for (String val : domain) {
            this.domain.add(val);
        }
    }
    
    public Variable(String name, boolean isBoolean, String... domain) {
        this.name = name;
        this.domain = new HashSet();
        this.isBoolean = isBoolean;
        for (String val : domain) {
            this.domain.add(val);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getDomain() {
        return domain;
    }

    public void setDomain(Set<String> domain) {
        this.domain = domain;
    }

    public static Variable makeBooleanVariable(String name) {
        Variable variable = new Variable(name, true, "oui", "non");
        return variable;
    }
    
    public boolean isBoolean(){
        return isBoolean;
    }

    @Override
    public String toString(){
        return this.name;
    }
    
}
