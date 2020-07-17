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

package expressions;

import robot.Robot;
import terrain.Cellule;
import terrain.Mur;

/**
 *
 * @author Yvan
 */
public class DevantMur extends ExprBoolElt {
    private static final long serialVersionUID = 1L;
    //private transient Robot robot;
    //public DevantMur(Robot robot) {
    //    this.robot = robot;
    //}

    /*@Override
    public boolean evalue(robot.Robot robot) {
        Cellule devant = robot.quoiDevant();
        return devant != null && devant instanceof Mur;
    }*/
    @Override
    public String toString() {
        return "devant mur";
    }


    //@Override
    //public void set(Object o) {
    //    robot = (Robot) o;
    //}

    @Override
    public String getAbr() {
        return "dmr";
    }

    @Override
    public boolean evalue(Robot robot) {
        Cellule devant = robot.quoiDevant();
        return devant != null && devant instanceof Mur;
    }

}
