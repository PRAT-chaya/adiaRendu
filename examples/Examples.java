/**
 *
 * @author 21600639 : DENOUAL Axel
 * @author 21910036 : ROUSSEAU Alexy
 * @author 21907858 : SABATIER Brian
 * 
 */
package examples;

import representation.RestrictedDomain;
import representation.IncompatibilityConstraint;
import representation.Rule;
import representation.Variable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ppc.BacktrackSearch;

public class Examples {

    private List<Rule> rules;

    public Examples() {
        this.rules = new ArrayList();
        buildRules();
    }

    private void buildRules() {
        Variable angine = Variable.makeBooleanVariable("angine");
        Variable fievre = new Variable("fievre", "bas", "moyen", "haut");

        Variable vaccin = Variable.makeBooleanVariable("vaccin");
        Variable toux = Variable.makeBooleanVariable("toux");
        Variable grippe = Variable.makeBooleanVariable("grippe");
        Variable virus = Variable.makeBooleanVariable("virus");
        Variable hypothermie = Variable.makeBooleanVariable("hypothermie");
        Variable fatigue = Variable.makeBooleanVariable("fatigue");
        Variable priseSirop = Variable.makeBooleanVariable("priseSirop");
        Variable boutons = Variable.makeBooleanVariable("boutons");
        Variable oedeme = Variable.makeBooleanVariable("oedeme");
        Variable allergieSucre = new Variable("allergieSucre", "bas", "moyen", "haut");

        //Règle 1
        List<RestrictedDomain> premise1 = new ArrayList();
        premise1.add(new RestrictedDomain(angine, Variable.TRUE));
        List<RestrictedDomain> conclusion1 = new ArrayList();
        conclusion1.add(new RestrictedDomain(fievre, "moyen", "haut"));
        Rule rule1 = new Rule(premise1, conclusion1);
        System.out.println("Règle1: \'L'angine provoque une fièvre haute ou moyenne\'");

        //Règle 2
        List<RestrictedDomain> premise2 = new ArrayList();
        premise1.add(new RestrictedDomain(angine, Variable.TRUE));
        List<RestrictedDomain> conclusion2 = new ArrayList();
        conclusion2.add(new RestrictedDomain(toux, Variable.TRUE));
        Rule rule2 = new Rule(premise2, conclusion2);
        System.out.println("Règle2: \'L'angine provoque une toux\'");

        //Règle 3
        List<RestrictedDomain> premise3 = new ArrayList();
        premise3.add(new RestrictedDomain(grippe, Variable.TRUE));
        premise3.add(new RestrictedDomain(vaccin, Variable.FALSE));
        List<RestrictedDomain> conclusion3 = new ArrayList();
        conclusion3.add(new RestrictedDomain(fievre, "haut"));
        Rule rule3 = new Rule(premise3, conclusion3);
        System.out.println("Règle3: \'Une grippe, en l'absence de vaccination,"
                + " provoque une fièvre haute\'");

        //Règle 4
        List<RestrictedDomain> premise4 = new ArrayList();
        premise4.add(new RestrictedDomain(grippe, Variable.TRUE));
        premise4.add(new RestrictedDomain(vaccin, Variable.FALSE));
        List<RestrictedDomain> conclusion4 = new ArrayList();
        conclusion4.add(new RestrictedDomain(fatigue, Variable.TRUE));
        Rule rule4 = new Rule(premise4, conclusion4);
        System.out.println("Règle4: \'Une grippe, en l'absence de vaccination,"
                + " provoque la fatigue\'");

        //Règle 5
        List<RestrictedDomain> premise5 = new ArrayList();
        premise5.add(new RestrictedDomain(angine, Variable.TRUE));
        List<RestrictedDomain> conclusion5 = new ArrayList();
        conclusion5.add(new RestrictedDomain(virus, Variable.TRUE));
        conclusion5.add(new RestrictedDomain(virus, Variable.FALSE));
        Rule rule5 = new Rule(premise5, conclusion5);
        System.out.println("Règle5: \'L'angine peut ou non être provoquée par un virus\'");

        //Règle 6
        List<RestrictedDomain> premise6 = new ArrayList();
        premise6.add(new RestrictedDomain(grippe, Variable.TRUE));
        List<RestrictedDomain> conclusion6 = new ArrayList();
        conclusion6.add(new RestrictedDomain(virus, Variable.TRUE));
        Rule rule6 = new Rule(premise6, conclusion6);
        System.out.println("Règle6: \'Une grippe est toujours provoquée par un virus\'");

        //Règle 7
        List<RestrictedDomain> premise7 = new ArrayList();
        premise7.add(new RestrictedDomain(priseSirop, Variable.TRUE));
        premise7.add(new RestrictedDomain(allergieSucre, "moyen"));
        List<RestrictedDomain> conclusion7 = new ArrayList();
        conclusion7.add(new RestrictedDomain(boutons, Variable.TRUE));
        Rule rule7 = new Rule(premise7, conclusion7);
        System.out.println("Règle7: \'La prise de sirop avec une allergie moyenne"
                + " au sucre provoque des boutons\'");

        //Règle 8
        List<RestrictedDomain> premise8 = new ArrayList();
        premise8.add(new RestrictedDomain(priseSirop, Variable.TRUE));
        premise8.add(new RestrictedDomain(allergieSucre, "haut"));
        List<RestrictedDomain> conclusion8 = new ArrayList();
        conclusion8.add(new RestrictedDomain(oedeme, Variable.TRUE));
        Rule rule8 = new Rule(premise8, conclusion8);
        System.out.println("Règle8: \'La prise de sirop avec une allergie haute"
                + " au sucre provoque un œdème\'");

        //Règle 9
        List<RestrictedDomain> premise9 = new ArrayList();
        premise9.add(new RestrictedDomain(fievre, "moyen", "haut"));
        List<RestrictedDomain> conclusion9 = new ArrayList();
        conclusion9.add(new RestrictedDomain(hypothermie, Variable.TRUE));
        List<RestrictedDomain> terms9 = new ArrayList();
        terms9.addAll(premise9);
        terms9.addAll(conclusion9);
        IncompatibilityConstraint rule9 = new IncompatibilityConstraint(terms9);
        System.out.println("Règle9: \'On ne peut pas à la fois avoir une fièvre haute"
                + " ou moyenne et être en hypothermie\'");

        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        rules.add(rule4);
        rules.add(rule5);
        rules.add(rule6);
        rules.add(rule7);
        rules.add(rule8);
        rules.add(rule9);

    }

    public List<Rule> getRules() {
        return rules;
    }

    public static void main(String[] args) {
        Examples ex = new Examples();
        BacktrackSearch bts = new BacktrackSearch(new HashSet<>(ex.getRules()));
        Set<List<RestrictedDomain>> allSolutions = bts.allSolutions();
        BacktrackSearch.printSolutions(allSolutions);
        System.out.println(allSolutions.size());
    }

}
