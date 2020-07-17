/*
 * Creative commons CC BY-NC-SA 2020 Yvan Maillot <yvan.maillot@uha.fr>
 *
 *     Share - You can copy and redistribute the material in any medium or format
 * 
 *     Adapt - You can remix, transform, and build upon the material 
 * 
 * Under the following terms :
 * 
 *     Attribution - You must give appropriate credit, provide a link to the license, 
 *     and indicate if changes were made. You may do so in any reasonable manner, 
 *     but not in any way that suggests the licensor endorses you or your use. 
 * 
 *     NonCommercial — You may not use the material for commercial purposes. 
 * 
 *     ShareAlike — If you remix, transform, or build upon the material, 
 *     you must distribute your contributions under the same license as the original. 
 * 
 * Notices:    You do not have to comply with the license for elements of 
 *             the material in the public domain or where your use is permitted 
 *             by an applicable exception or limitation. 
 * 
 * No warranties are given. The license may not give you all of the permissions 
 * necessary for your intended use. For example, other rights such as publicity, 
 * privacy, or moral rights may limit how you use the material. 
 * 
 * See <https://creativecommons.org/licenses/by-nc-sa/4.0/>.
 */

package robot;

import instruction.Bloc;
import instruction.Instruction;
import instruction.Racine;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Yvan
 */
public class Programme implements Serializable {
    private static final long serialVersionUID = 1L;

    private Initialisation initialisation;
    private Map<String, Instruction> procedure;
    private DefaultTreeModel arbreProgramme;
    public Programme() {

        initialisation = new Initialisation();
        arbreProgramme = new DefaultTreeModel(new Racine());
        procedure  = new HashMap<String, Instruction>();
        Bloc principal = new Bloc("procédure principale");
        procedure.put("procédure principale", principal);
        arbreProgramme.insertNodeInto(principal, (Instruction)arbreProgramme.getRoot(), 0);
        
    }

    @Override
    public String toString() {
        String retour = initialisation.toString() +  "\n" + 

        ((Instruction)arbreProgramme.getRoot()).deepToString("");
        return retour;
    }

/*
    public static void changerDeRobot(Instruction instruction, Robot robot) {
        instruction.set(robot);
        for(int i = 0; i < instruction.getChildCount(); i++)
            changerDeRobot((Instruction) instruction.getChildAt(i), robot);
    }*/
    public DefaultTreeModel getArbreProgramme() {
        return arbreProgramme;
    }

    public Instruction getProcedurePrincipal() {
        return procedure.get("procédure principale");
    }
    public Instruction getProcedure(String nom) {
        return procedure.get(nom);
    }
    public Collection<Instruction> getProcedures() {
        Collection<Instruction> c = procedure.values();
        c.remove(getProcedurePrincipal());
        return c;
    }
    public void ajoutProcedure(Bloc bloc) {
        procedure.put(bloc.toString(), bloc);       
    }
    public void retraitProcedure(String nom) {
        Bloc as = (Bloc) procedure.remove(nom);
        if (as != null) {
            arbreProgramme.removeNodeFromParent(as);
        }
    }
    public Bloc ajoutProcedure(String nom) {
        Bloc bloc = new Bloc(nom);
        procedure.put(nom, bloc);
        arbreProgramme.insertNodeInto(bloc, (Instruction)arbreProgramme.getRoot(), 0);
        return bloc;

    }
    public void setInitialisation(Initialisation initialisation) {
        this.initialisation = initialisation;
    }

    public Initialisation getInitialisation() {
        return initialisation;
    }

    public Iterable<Instruction> procedures() {
        return procedure.values();
    }
    
    public void supprimerProcedure() {
        procedure.clear();
    }

}
