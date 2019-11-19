/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import extractionConnaissance.*;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import representation.Rule;
import representation.Variable;

/**
 *
 * @author axel
 */
public class ExtractionConnaissanceTest {
    
        private URL url;

    public ExtractionConnaissanceTest(String filename) {
        url = getClass().getResource(filename);
        
    }

    public URL getUrl() {
        return url;
    }
    
    
    
    
    
    public static void main(String[] args) {
        
        //Création de variables utilisées par le DBReader
        ExtractionConnaissanceTest ect = new ExtractionConnaissanceTest("test.csv");
        Set<Variable> variables = new HashSet();
        String variableLine = "angine;prise_sirop;fièvre;œdème;fatigué(e);toux;vacciné(e);hypothermie;allergie_sucre;boutons;grippe;virus;";
        for (String variableName: variableLine.split(";")) {
            variables.add(Variable.makeBooleanVariable(variableName));
        }
        
        //
        DBReader dbr = new DBReader(variables);
        
        Database db = dbr.readCSV(ect.getUrl().getPath());
        System.out.println("file opening");
        BooleanDatabase boolDB = Database.booleanConvert(db);
        System.out.println("conversion success");
        
        System.out.println(boolDB);
        
        //
        FrequentItemsetMiner frequentItemsetM = new FrequentItemsetMiner(boolDB);
        Map<Set<Variable>, Integer> frequentItemset = frequentItemsetM.frequentItemsets(500);
        Set<Variable> minedScope = frequentItemsetM.getMinedScope();
        
        
        AssociationRuleMiner assocRuleM = new AssociationRuleMiner(boolDB, frequentItemset, minedScope);
        Set<Rule> strictRuleSet = assocRuleM.strictRuleMiner(500, 0.8);
        
        
        for(Rule rule: strictRuleSet){
            AssociationRuleMiner.printBooleanRule(rule);        
        }
        
        for(Set<Variable> itemSet: frequentItemset.keySet()){
            for(Variable var: itemSet){
                System.out.print(var.getName() + " ");
            }
            System.out.print(frequentItemset.get(itemSet) + "\n");
        }
    }
    
}
