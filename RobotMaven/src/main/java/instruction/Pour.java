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

import javax.swing.ImageIcon;
import robot.DansLeMur;

/**
 *
 * @author Yvan
 */
public class Pour extends InstructionComposee {
    
    private static final long serialVersionUID = 1L;
    
    private String debut;
    private String fin;
    private String pas;
    private String nomCompteur;
    
    public Pour(InstructionComposee parent, String debut, String fin, String pas) {
        this.nom = "pour";
        this.debut = debut;
        this.fin = fin;
        this.pas = pas;
        char i = 'i';
        nomCompteur = ""+i;
        while(parent.get(nomCompteur) != null) {
            i++;
            nomCompteur = ""+i;
        }
        set(nomCompteur, 0);
    }
    public Pour(InstructionComposee parent, String debut, String fin) {
        this(parent, debut, fin, "1");
    }
    public Pour(InstructionComposee parent, String fin) {
        this(parent, "1", fin, "1");
    }

    @Override
    public void go(robot.Robot robot) throws DansLeMur, InterruptedException {
        
        int p = get(pas);
        if (p > 0)
            for (int compteur = get(debut); compteur <= get(fin); compteur+=p)  {
                set(nomCompteur, compteur);
                ((Instruction) getFirstChild()).go(robot);
            }
        else if (p < 0)
            for (int compteur = get(debut); compteur >= get(fin); compteur+=p)  {
                set(nomCompteur, compteur);
                ((Instruction) getFirstChild()).go(robot);
            }
    }

    @Override
    public String toString() {
        return super.toString()+" "+ nomCompteur+ " de " + debut + " à " + fin + " par pas de " + pas;
    }


    @Override
    public boolean autorisationAjout() {
        return getChildCount() < 1;
    }

    /*@Override
    public void set(Object o) {
    }*/

    @Override
    public ImageIcon getIcon() {
        return null;
    }

}
