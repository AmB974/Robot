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

package instruction;

import java.util.HashMap;

/**
 *
 * @author Yvan
 */
public abstract class InstructionComposee extends Instruction {

    public static final long serialVersionUID = 0L;
    
    private HashMap<String, Integer> variable = new HashMap<String, Integer>();

    /**
     * Retourne la valeur d'une variable passée en paramètre
     * @param nomVar
     * @return
     */
    public Integer get(String nomVar) {
        Integer valeur = null;
        try {
            valeur = Integer.parseInt(nomVar);
        } catch(NumberFormatException nfe) {
            valeur = variable.get(nomVar);
            if (valeur == null)
                if (getParent() == null)
                    return null;
                else
                    return ((InstructionComposee)getParent()).get(nomVar);
            else
                return valeur;
        }
        return valeur;
    }

    public Integer set(String nomVar, int valeur) {
        if (get(nomVar) == null)
            return variable.put(nomVar, valeur);
        else
            return affecte(nomVar, valeur);
    }

    private Integer affecte(String nomVar, int valeur) {
        if (variable.containsKey(nomVar))
            return variable.put(nomVar, valeur);
        else
            return affecte(nomVar, valeur);
    }
    public String[] getVars() {
        return variable.keySet().toArray(new String[0]);
    }

    @Override
    public String deepToString(String decalage) {
        //XStream xstream = new XStream();
        int n = getChildCount();
        String s = decalage + nom + "\ndébut\n";
        for(int i = 0; i < n; i++) {
            s += ((Instruction)getChildAt(i)).deepToString(decalage+" ");
        }
        s += "fin\n";
        return s;
    }

}
