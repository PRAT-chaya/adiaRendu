/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */
package representation;
import java.util.List;

public class Disjunction extends Rule implements Constraint {
    
    public Disjunction(List<RestrictedDomain> premise, List<RestrictedDomain> conclusion) {
        super(premise, conclusion);
    }
    
    @Override
    public boolean isSatisfiedBy(List<RestrictedDomain> assessment) {
        // Pour chaque variable de l'instance testée
        for (int i = 0; i < assessment.size(); i++) {
            Variable assessmentPremVar = assessment.get(i).getVariable();
            // Si la prémisse est concernée par la variable
            for (int x = 0; x < premise.size(); x++) {
                if (premise.get(x).getVariable() == assessmentPremVar) {
                    /* Si le sous-domaine de la variable tel qu'incluse dans la
                    (pseudo) prémisse contient la valeur associée à cette variable
                    dans l'instance testée alors on retourne vrai. car nous sommes dans le cas d'une disjonction */
                    if (premise.get(x).subDomainContains(assessment.get(i).getSubdomain())) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
}
