package expressions;

import robot.Robot;
import terrain.Cellule;
import terrain.Mur;

public class PasDevantRobot extends ExprBoolElt {
    private static final long serialVersionUID = 1L;

    @Override
    public String getAbr() {
        return "pdr";
    }
    @Override
    public String toString() {
        return "Pas devant robot";
    }

    @Override
    public boolean evalue(Robot robot) {
        Cellule devant = robot.quoiDevant();
        return devant == null || !(devant instanceof Robot);
    }
}
