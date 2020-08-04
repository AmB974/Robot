package expressions;

import robot.Robot;
import terrain.Cellule;
import terrain.Mur;

public class DevantRobotOuMur extends ExprBoolElt {
    @Override
    public String toString() {
        return "Devant robot ou mur";
    }

    @Override
    public String getAbr() {
        return "drom";
    }

    @Override
    public boolean evalue(Robot robot) {
        Cellule devant = robot.quoiDevant();
        return devant != null && (devant instanceof Robot || devant instanceof Mur);
    }
}
