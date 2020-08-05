package expressions;

import robot.Robot;
import terrain.Cellule;
import terrain.Mur;

public class DevantRobot extends ExprBoolElt {

    @Override
    public String toString() {
        return "Devant robot";
    }

    @Override
    public String getAbr() {
        return "drt";
    }

    @Override
    public boolean evalue(Robot robot) {
        Cellule devant = robot.quoiDevant();
        return devant != null && devant instanceof Robot;
    }
}
