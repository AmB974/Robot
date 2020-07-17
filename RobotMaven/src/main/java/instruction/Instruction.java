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

import robot.DansLeMur;
import robot.NoeudProgramme;

/**
 *
 * @author Yvan
 */
public abstract class Instruction extends NoeudProgramme {

    public static final long serialVersionUID = 0L;
    protected String nom;

    public abstract void go(robot.Robot robot) throws DansLeMur, InterruptedException;

    @Override
    public String toString() {
        if (getParent() != null) {
            if (getParent() instanceof Si) {
                if (getParent().getIndex(this) == 0) {
                    return "alors " + nom;
                } else {
                    return "sinon " + nom;
                }
            } else if (getParent() instanceof TantQue || getParent() instanceof Pour) {
                return "faire " + nom;
            }
        }
        return nom;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Permet de changer éventuellement l'objet sur lequel s'applique
     * l'instruction
     *
     * @param o
     */
    //public abstract void set(Object o);

    public abstract boolean autorisationAjout();

    public abstract String deepToString(String decalage);
}
