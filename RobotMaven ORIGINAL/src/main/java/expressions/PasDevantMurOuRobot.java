package expressions;

import robot.Robot;
import terrain.Cellule;
import terrain.Marque;
import terrain.Minerai;
//Ajouté par Sélim
public class PasDevantMurOuRobot extends ExprBoolElt{

    private static final long serialVersionUID = 1L;
    //private transient Robot robot;
    //public PasDevantMur(Robot robot) {
    //    this.robot = robot;
    //}

    @Override
    public boolean evalue(Robot robot) {
        Cellule devant = robot.quoiDevant();
        return devant == null || devant instanceof Minerai || devant instanceof Marque;
    }

    @Override
    public String getAbr() {
        return "pdmor";
    }

    @Override
    public String toString() {
        return "pas devant mur ou robot";
    }

}
