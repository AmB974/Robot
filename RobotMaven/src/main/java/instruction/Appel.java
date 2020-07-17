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

import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import robot.DansLeMur;

/**
 *
 * @author Yvan
 */
public class Appel extends InstructionElementaire {
    private static final long serialVersionUID = 1L;
    
    private Bloc bloc;

    
    public Appel(Bloc bloc) {
        this.bloc = bloc;
        Scanner sc = new Scanner(bloc.toString());
        sc.useDelimiter(", ");
        this.nom = sc.next();
        while(sc.hasNext()) {
            String var = sc.next();
            String n = JOptionPane.showInputDialog(
                    "<html>" +
                    "<p>"+bloc+"</p>"+
                    "<p>Donnez une valeur à " + var + "</p>"+
                    "</html>");
            nom += ", " + var + "=" + n;
        }
    }
    
    @Override
    public void go(robot.Robot robot) throws DansLeMur, InterruptedException {
        Scanner sc = new Scanner(toString());
        sc.useDelimiter(", ");
        sc.next();
        while(sc.hasNext()) {
            String varValeur = sc.next();
            Scanner psc = new Scanner(varValeur);
            psc.useDelimiter("=");
            String var = psc.next();
            //int valeur = psc.nextInt();
            bloc.set(var, ((InstructionComposee)getParent()).get(psc.next()));

        }
        bloc.go(robot);
    }
    
    
    @Override
    public String toString() {
        return nom;
    }

    /*@Override
    public void set(Object o) {
    }*/

    @Override
    public ImageIcon getIcon() {
        return null;
    }



}
