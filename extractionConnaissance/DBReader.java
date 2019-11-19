package extractionConnaissance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Variable;

public class DBReader {

    protected Set<Variable> variables;
    
    public DBReader(Set<Variable> variables) {
        this.variables = variables;
    
    }
    /**
     * Reads a database, that is, a list of instantiations, from a CSV
     * file.The expected format is the ';'-separated list of variable names
     * as the first line, then one ';'-separated list of values per instance
     * each on its own line
     * @param filename
     * @return 
     */
    public Database readCSV(String filename){ 
        try (BufferedReader reader = new BufferedReader (new FileReader (filename))) {
            Database res = this.readDB(reader);
            reader.close();
            return res;
        }catch(IOException e){
            System.out.println("Pas content :{");
            return null;
        }
    }
    
    public Database readDB(BufferedReader in) throws IOException {
        // Reading variables
        List<Variable> orderedVariables = new ArrayList<>();
        String variableLine = in.readLine();
        for (String variableName: variableLine.split(";")) {
            boolean found = false;
            for (Variable variable: this.variables) {
                if (variable.getName().equals(variableName)) {
                    orderedVariables.add(variable);
                    found = true;
                    break;
                }
            }
            if ( ! found ) {
                throw new IOException("Unknown variable name: " + variableName);
            }
        }
        // Reading instances
        List<Map<Variable, String>> instances = new ArrayList<>();
        String line;
        int lineNb = 1;
        while ( (line = in.readLine()) != null ) {
            String [] parts = line.split(";");
            if (parts.length != orderedVariables.size()) {
                throw new IOException("Wrong number of fields on line " + lineNb);
            }
            Map<Variable, String> instance = new HashMap<> ();
            for (int i = 0; i < parts.length; i++) {
                instance.put(orderedVariables.get(i), parts[i]);
            }
            instances.add(instance);
            lineNb++;
        }
        return new Database(orderedVariables, instances);
    }
    
    public static void main(String[] args) {
        Set<Variable> variables = new HashSet();
        String variableLine = "angine;prise_sirop;fièvre;œdème;fatigué(e);toux;vacciné(e);hypothermie;allergie_sucre;boutons;grippe;virus;";
        for (String variableName: variableLine.split(";")) {
            variables.add(new Variable(variableName,""));
        }
        DBReader dbr = new DBReader(variables);
        Database db = dbr.readCSV("/home/axel/Downloads/bases/db_b1_n1000_p01.csv");
        System.out.println("file opening");
        System.out.println(db);
        BooleanDatabase boolDB = Database.booleanConvert(db);
        System.out.println("conversion success");
        System.out.print(boolDB);
    }
}
