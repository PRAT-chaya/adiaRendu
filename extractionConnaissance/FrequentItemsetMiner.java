/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractionConnaissance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import representation.Variable;

/**
 *
 * @author ordinaute
 */
public class FrequentItemsetMiner {

    private BooleanDatabase db;
    private Set<Variable> minedScope;

    public FrequentItemsetMiner(BooleanDatabase db) {
        this.db = db;
        this.minedScope = new HashSet();
        minedScope.addAll(db.getVariables());
    }

    public Map<Set<Variable>, Integer> frequentItemsets(int minFreq) {
        Map<Set<Variable>, Integer> frequentItemsets = new HashMap();
        Map<Variable, Integer> frequentSingletons = frequentSingletons(minFreq);
        for (Variable singleton : frequentSingletons.keySet()) {
            Set<Variable> varset = new HashSet();
            varset.add(singleton);
            frequentItemsets.put(varset, frequentSingletons.get(singleton));
        }

        boolean freqEnough = true;

        for (int r = 2; r <= frequentSingletons.keySet().size() && freqEnough; r++) {
            Variable vars[] = new Variable[frequentSingletons.keySet().size()];
            int i = 0;
            for (Variable var : frequentSingletons.keySet()) {
                vars[i] = var;
                i++;
            }
            List<Variable[]> toCheck = new ArrayList();
            buildCombination(vars, vars.length, r, toCheck);
            Set<Set<Variable>> tuples = new HashSet();
            for (Variable[] tuple : toCheck) {
                tuples.add(new HashSet<>(Arrays.asList(tuple)));
            }

            Map<Set<Variable>, Integer> temp = new HashMap();
            temp = tuplesFrequence(tuples);
            temp = freqFilter(temp, minFreq);
            if (!temp.isEmpty()) {
                frequentItemsets.putAll(temp);
            } else {
                freqEnough = false;
            }
        }
        Set<Variable> tempScope = new HashSet();
        for (Set<Variable> itemset : frequentItemsets.keySet()) {
            for (Variable var : this.minedScope) {
                if (itemset.contains(var)) {
                    if (!tempScope.contains(var)) {
                        tempScope.add(var);
                    }
                }
            }
        }
        minedScope = tempScope;
        return frequentItemsets;
    }

    public Set<Variable> getMinedScope() {
        return minedScope;
    }

    public static Map<Set<Variable>, Integer> freqFilter(Map<Set<Variable>, Integer> tuplesFrequence, int minFreq) {
        Set<Set<Variable>> tuples = new HashSet();
        tuples.addAll(tuplesFrequence.keySet());
        for (Set<Variable> tuple : tuples) {
            if (tuplesFrequence.get(tuple) < minFreq) {
                tuplesFrequence.remove(tuple);
            }
        }
        return tuplesFrequence;
    }

    private Map<Set<Variable>, Integer> tuplesFrequence(Set<Set<Variable>> tuples) {
        return tuplesFrequence(tuples, this.db.getTransactions());
    }

    /**
     * Retourne en Map les n-tuplets passant le seuil de fréquence
     *
     * @param tuples Les n-tuplets qu'on veut évaluer
     * @param transactions Les données quel'on a
     * @return Map : en clé un n-tuplet, en valeur sa fréquence
     */
    public static Map<Set<Variable>, Integer> tuplesFrequence(Set<Set<Variable>> tuples, List<Map<Variable, Boolean>> transactions) {
        Map<Set<Variable>, Integer> tuplesFreq = new HashMap();
        for (Map<Variable, Boolean> transaction : transactions) {
            for (Set<Variable> tuple : tuples) {
                if (FrequentItemsetMiner.isTupleInTransaction(tuple, transaction)) {
                    if (tuplesFreq.keySet().contains(tuple)) {
                        int val = tuplesFreq.get(tuple);
                        val++;
                        tuplesFreq.replace(tuple, val);
                    } else {
                        tuplesFreq.put(tuple, 1);
                    }
                }
            }
        }
        return tuplesFreq;
    }

    private Map<Variable, Integer> frequentSingletons(int minFreq) {
        return frequentSingletons(minFreq, this.db.getVariables(), this.db.getTransactions());
    }

    /**
     * Retourne en Map les singletons passant le seuil de fréquence.
     *
     * @param minFreq La fréquence minimum des singletons retournés
     * @param vars Les variables dont on veut évaluer la fréquence
     * @param transactions Les données dont on dispose
     * @return Map avec en clé une variable et en valeur sa fréquence
     */
    public static Map<Variable, Integer> frequentSingletons(int minFreq, List<Variable> vars, List<Map<Variable, Boolean>> transactions) {
        Map<Variable, Integer> singletonCount = new HashMap();
        for (Variable var : vars) {
            singletonCount.put(var, 0);
        }
        for (Map<Variable, Boolean> transaction : transactions) {
            for (Variable var : transaction.keySet()) {
                if (transaction.get(var)) {
                    int temp = (singletonCount.get(var)) + 1;
                    singletonCount.replace(var, temp);
                }
            }
        }
        Set<Variable> varset = new HashSet();
        varset.addAll(singletonCount.keySet());
        for (Variable var : varset) {
            if (singletonCount.get(var) < minFreq) {
                singletonCount.remove(var);
            }
        }
        return singletonCount;
    }

    /**
     * @param arr[] Input Array
     * @param data[] Temporary array to store current combination
     * @param start Staring index in arr[]
     * @param end Ending index in arr[]
     * @param index Current index in data[]
     * @param r Size of a combination to be printed
     */
    private static void combinationUtil(Variable arr[], Variable data[], int start,
            int end, int index, int r, List<Variable[]> output) {

        // Current combination is ready to be printed, print it 
        if (index == r) {
            Variable[] temp = new Variable[r];
            temp = data.clone();
            output.add(temp);
            return;
        }

        // replace index with all possible elements. The condition 
        // "end-i+1 >= r-index" makes sure that including one element 
        // at index will make a combination with remaining elements 
        // at remaining positions 
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i + 1, end, index + 1, r, output);
        }
    }

    // Builds all combinations of size r in arr[] of size n.
    public static void buildCombination(Variable arr[], int n, int r, List<Variable[]> output) {
        // A temporary array to store all combination one by one 
        Variable data[] = new Variable[r];

        // Print all combination using temprary array 'data[]' 
        combinationUtil(arr, data, 0, n - 1, 0, r, output);
    }

    public static boolean isTupleInTransaction(Set<Variable> tuple, Map<Variable, Boolean> transaction) {
        boolean inTransaction = true;
        for (Variable var : tuple) {
            if (!transaction.keySet().contains(var)) {
                inTransaction = false;
            }
        }
        return inTransaction;
    }

}
