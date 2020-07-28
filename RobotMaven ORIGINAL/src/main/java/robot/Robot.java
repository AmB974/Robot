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

import instruction.Instruction;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import terrain.Cellule;
import terrain.Marque;
import terrain.Mur;
import terrain.Terrain;

public class Robot implements Cellule, Runnable {

    private Cellule passage = null;
    private static Random random = new Random();
    private boolean stop = false;
    // La durée en ms pour avancer d'une case.
    public int duréeReference = 200;
    private Orientation[] tOrientation = {new Orientation(0, 0, -1), new Orientation(1, 1, 0), new Orientation(2, 0, 1), new Orientation(3, -1, 0)};
    private Orientation vers = tOrientation[0];
    private Color couleur;
    private Terrain terrain;
    private int x, y;
    private Instruction programme;
    private Image[] image;
    private Image[] robotN = new Image[1];
    private Image[] robotS = new Image[1];
    private Image[] robotE = new Image[1];
    private Image[] robotO = new Image[1];

    private Image[] robotCasse = new Image[4];
    private Image[] robotNeM = new Image[4];
    private Image[] robotSeM = new Image[4];
    private Image[] robotEeM = new Image[4];
    private Image[] robotOeM = new Image[4];
    private Image[] robotNprem = new Image[1];
    private Image[] robotSprem = new Image[1];
    private Image[] robotEprem = new Image[1];
    private Image[] robotOprem = new Image[1];
    private Image[] robotCasseprem = new Image[4];
    private Image[] robotNeMprem = new Image[4];
    private Image[] robotSeMprem = new Image[4];
    private Image[] robotEeMprem = new Image[4];
    private Image[] robotOeMprem = new Image[4];
    private Thread processus;
    private boolean enMarche = false;
    private int numeroImage = 0;

    //debut ajout
    private int nombrePas = -1;
    private int nombreDepPas;
    public int getNombrePas(){ return this.nombrePas;}
    public void setNombrePas(int nombrePas){ this.nombrePas=nombrePas;}
    public void setNombreDepPas(int nombreDepPas){ this.nombreDepPas = nombreDepPas;}
    private Image[] robotCouleurE = new Image[5];
    private Image[] robotCouleurEprem = new Image[5];

    private Image[] robotCouleurS = new Image[5];
    private Image[] robotCouleurSprem = new Image[5];

    private Image[] robotCouleurO = new Image[5];
    private Image[] robotCouleurOprem = new Image[5];

    private Image[] robotCouleurN = new Image[5];
    private Image[] robotCouleurNprem = new Image[5];
    //fin ajout

    private static Integer cpt = 0;
    private static Robot[] robots = new Robot[FramePrincipale.getNbRobots() + 1];
    private final int ID;
    private static int NBROBOTS = FramePrincipale.getNbRobots();//--------------------------------- A mettre en place
    //Ajouté par Sélim

    public Cellule quoiDessous() {
        return passage;
    }

    private void lancementAnimation() {
        Thread animation = new Thread(new Runnable() {

            @Override
            public void run() {
                while (enMarche) {
                    Robot.this.numeroImage = (Robot.this.numeroImage + 1) % Robot.this.image.length;
                    Robot.this.terrain.repaint(x * Robot.this.terrain.getTailleCelluleX(), y * Robot.this.terrain.getTailleCelluleY(), Robot.this.terrain.getTailleCelluleX(), Robot.this.terrain.getTailleCelluleY());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
        //animation.setDaemon(true);
        animation.start();
    }

    public void bloquer() {
        stop = true;
    }

    public synchronized void deBloquer() {
        stop = false;
        notifyAll();
    }

    public boolean isStopped() {
        return stop;
    }

    @Override
    public void setTaille(int lx, int ly) {

        robotE[0] = robotEprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotS[0] = robotSprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotO[0] = robotOprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotN[0] = robotNprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotEeM[0] = robotEeMprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotEeM[1] = robotEeMprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotEeM[2] = robotEeMprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotEeM[3] = robotEeMprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotOeM[0] = robotOeMprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotOeM[1] = robotOeMprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotOeM[2] = robotOeMprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotOeM[3] = robotOeMprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotSeM[0] = robotSeMprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotSeM[1] = robotSeMprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotSeM[2] = robotSeMprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotSeM[3] = robotSeMprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotNeM[0] = robotNeMprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotNeM[1] = robotNeMprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotNeM[2] = robotNeMprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotNeM[3] = robotNeMprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCasse[0] = robotCasseprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCasse[1] = robotCasseprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCasse[2] = robotCasseprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCasse[3] = robotCasseprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurE[0] = robotCouleurEprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[1] = robotCouleurEprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[2] = robotCouleurEprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[3] = robotCouleurEprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[4] = robotCouleurEprem[4].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurS[0] = robotCouleurSprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[1] = robotCouleurSprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[2] = robotCouleurSprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[3] = robotCouleurSprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[4] = robotCouleurSprem[4].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurO[0] = robotCouleurOprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[1] = robotCouleurOprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[2] = robotCouleurOprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[3] = robotCouleurOprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[4] = robotCouleurOprem[4].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurN[0] = robotCouleurNprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[1] = robotCouleurNprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[2] = robotCouleurNprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[3] = robotCouleurNprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[4] = robotCouleurNprem[4].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
    }

    private class Orientation {

        public final int direction, pasx, pasy;

        Orientation(int ix, int px, int py) {
            pasx = px;
            pasy = py;
            direction = ix;
        }
    }

    ;


    /*
     * public void prend(Cellule c) { if (passage == null) passage = c; else
     * passage.prend(c);
    }
     */
    private Image[] imageSelonOrientation() {

        if (!enMarche) {
            if (vers.direction == Terrain.EST) {
                return robotE;
            } else if (vers.direction == Terrain.OUEST) {
                return robotO;
            } else if (vers.direction == Terrain.SUD) {
                return robotS;
            } else {
                return robotN;
            }
        } else {
            if (vers.direction == Terrain.EST) {
                return robotEeM;
            } else if (vers.direction == Terrain.OUEST) {
                return robotOeM;
            } else if (vers.direction == Terrain.SUD) {
                return robotSeM;
            } else {
                return robotNeM;
            }
        }
    }

    private void init(Terrain terrain, int x, int y, int direction, Color couleur) {
        this.terrain = terrain;
        this.couleur = couleur;
        passage = terrain.get(x, y);
        this.x = x;
        this.y = y;
        vers = tOrientation[direction];
        terrain.set(x, y, this);

        try {

            robotEprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVersEst.png"));
            robotSprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVersSud.png"));
            robotOprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVersOuest.png"));
            robotNprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVersNord.png"));

            robotE[0] = robotEprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotS[0] = robotSprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotO[0] = robotOprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotN[0] = robotNprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotEeMprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVersEst1.png"));
            robotEeMprem[1] = ImageIO.read(Robot.class.getResource("/images/robotVersEst2.png"));
            robotEeMprem[2] = ImageIO.read(Robot.class.getResource("/images/robotVersEst3.png"));
            robotEeMprem[3] = ImageIO.read(Robot.class.getResource("/images/robotVersEst4.png"));

            robotEeM[0] = robotEeMprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotEeM[1] = robotEeMprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotEeM[2] = robotEeMprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotEeM[3] = robotEeMprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotOeMprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVersOuest1.png"));
            robotOeMprem[1] = ImageIO.read(Robot.class.getResource("/images/robotVersOuest2.png"));
            robotOeMprem[2] = ImageIO.read(Robot.class.getResource("/images/robotVersOuest3.png"));
            robotOeMprem[3] = ImageIO.read(Robot.class.getResource("/images/robotVersOuest4.png"));

            robotOeM[0] = robotOeMprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotOeM[1] = robotOeMprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotOeM[2] = robotOeMprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotOeM[3] = robotOeMprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotSeMprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVersSud1.png"));
            robotSeMprem[1] = ImageIO.read(Robot.class.getResource("/images/robotVersSud2.png"));
            robotSeMprem[2] = ImageIO.read(Robot.class.getResource("/images/robotVersSud3.png"));
            robotSeMprem[3] = ImageIO.read(Robot.class.getResource("/images/robotVersSud4.png"));

            robotSeM[0] = robotSeMprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotSeM[1] = robotSeMprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotSeM[2] = robotSeMprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotSeM[3] = robotSeMprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotNeMprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVersNord1.png"));
            robotNeMprem[1] = ImageIO.read(Robot.class.getResource("/images/robotVersNord2.png"));
            robotNeMprem[2] = ImageIO.read(Robot.class.getResource("/images/robotVersNord3.png"));
            robotNeMprem[3] = ImageIO.read(Robot.class.getResource("/images/robotVersNord4.png"));

            robotNeM[0] = robotNeMprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotNeM[1] = robotNeMprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotNeM[2] = robotNeMprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotNeM[3] = robotNeMprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCasseprem[0] = ImageIO.read(Robot.class.getResource("/images/robotCasse1.png"));
            robotCasseprem[1] = ImageIO.read(Robot.class.getResource("/images/robotCasse2.png"));
            robotCasseprem[2] = ImageIO.read(Robot.class.getResource("/images/robotCasse3.png"));
            robotCasseprem[3] = ImageIO.read(Robot.class.getResource("/images/robotCasse4.png"));

            robotCasse[0] = robotCasseprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCasse[1] = robotCasseprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCasse[2] = robotCasseprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCasse[3] = robotCasseprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurEprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVertEst.png"));
            robotCouleurEprem[1] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireEst.png"));
            robotCouleurEprem[2] = ImageIO.read(Robot.class.getResource("/images/robotJauneEst.png"));
            robotCouleurEprem[3] = ImageIO.read(Robot.class.getResource("/images/robotOrangeEst.png"));
            robotCouleurEprem[4] = ImageIO.read(Robot.class.getResource("/images/robotRougeEst.png"));

            robotCouleurE[0] = robotCouleurEprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[1] = robotCouleurEprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[2] = robotCouleurEprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[3] = robotCouleurEprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[4] = robotCouleurEprem[4].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurSprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVertSud.png"));
            robotCouleurSprem[1] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireSud.png"));
            robotCouleurSprem[2] = ImageIO.read(Robot.class.getResource("/images/robotJauneSud.png"));
            robotCouleurSprem[3] = ImageIO.read(Robot.class.getResource("/images/robotOrangeSud.png"));
            robotCouleurSprem[4] = ImageIO.read(Robot.class.getResource("/images/robotRougeSud.png"));

            robotCouleurS[0] = robotCouleurSprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[1] = robotCouleurSprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[2] = robotCouleurSprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[3] = robotCouleurSprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[4] = robotCouleurSprem[4].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurOprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVertOuest.png"));
            robotCouleurOprem[1] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireOuest.png"));
            robotCouleurOprem[2] = ImageIO.read(Robot.class.getResource("/images/robotJauneOuest.png"));
            robotCouleurOprem[3] = ImageIO.read(Robot.class.getResource("/images/robotOrangeOuest.png"));
            robotCouleurOprem[4] = ImageIO.read(Robot.class.getResource("/images/robotRougeOuest.png"));

            robotCouleurO[0] = robotCouleurOprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[1] = robotCouleurOprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[2] = robotCouleurOprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[3] = robotCouleurOprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[4] = robotCouleurOprem[4].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurNprem[0] = ImageIO.read(Robot.class.getResource("/images/robotVertNord.png"));
            robotCouleurNprem[1] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireNord.png"));
            robotCouleurNprem[2] = ImageIO.read(Robot.class.getResource("/images/robotJauneNord.png"));
            robotCouleurNprem[3] = ImageIO.read(Robot.class.getResource("/images/robotOrangeNord.png"));
            robotCouleurNprem[4] = ImageIO.read(Robot.class.getResource("/images/robotRougeNord.png"));

            robotCouleurN[0] = robotCouleurNprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[1] = robotCouleurNprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[2] = robotCouleurNprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[3] = robotCouleurNprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[4] = robotCouleurNprem[4].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

        } catch (IOException ex) {
            Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
        }
        image = imageSelonOrientation();
        terrain.repaint();

    }

    /**
     * Créer un robot "quelque part" sur le terrain.
     *
     * @param terrain : terrain sur lequel circule le robot
     * @see public Robot(Terrain terrain, int x, int y)
     */
    public Robot(Terrain terrain) {
        init(terrain, random.nextInt(terrain.getNx()), random.nextInt(terrain.getNy()), random.nextInt(4), new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lancementAnimation();
        if (cpt < NBROBOTS)
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim

    }

    public Robot(Terrain terrain, int x, int y) {
        init(terrain, x, y, random.nextInt(4), new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lancementAnimation();
        if (cpt < NBROBOTS)
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim
    }

    public Robot(Terrain terrain, int x, int y, Color couleur) {
        init(terrain, x, y, random.nextInt(4), new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lancementAnimation();
        if (cpt < NBROBOTS)
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim
    }

    public Robot(Terrain terrain, Color couleur) {
        init(terrain, random.nextInt(terrain.getNx()), random.nextInt(terrain.getNy()), random.nextInt(4), couleur);
        lancementAnimation();
        if (cpt < NBROBOTS)
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim
    }

    public Robot(Terrain terrain, int x, int y, int dir) {
        init(terrain, x, y, dir, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lancementAnimation();
        if (cpt < NBROBOTS)
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim
    }

    /**
     * Pour connaître la couleur du robot
     *
     * @return la couleur du robot
     */
    @Override
    public Color getCouleur() {
        return couleur;
    }

    public Cellule quoiDevant() {

        int xa = x + vers.pasx;
        int ya = y + vers.pasy;

        if (xa >= terrain.getNx()) {
            xa = 0;
        }
        if (xa < 0) {
            xa = terrain.getNx() - 1;
        }

        if (ya >= terrain.getNy()) {
            ya = 0;
        }
        if (ya < 0) {
            ya = terrain.getNy() - 1;
        }

        return terrain.get(xa, ya);
    }

    /**
     * Avance d'un pas (ou non) sur le terrain dans le sens indiqué par pasx et
     * pasy
     */
    //@Override
    public void avance() throws DansLeMur, TropDePas {
        int xa = x;
        int ya = y;

        x += vers.pasx;
        y += vers.pasy;

        if (x >= terrain.getNx()) {
            x = 0;
        }
        if (x < 0) {
            x = terrain.getNx() - 1;
        }

        if (y >= terrain.getNy()) {
            y = 0;
        }
        if (y < 0) {
            y = terrain.getNy() - 1;
        }



        Cellule cellule = terrain.get(x, y);
        if (cellule != this) {
            if (cellule instanceof Robot || cellule instanceof Mur) {
                x = xa;
                y = ya;
                image = robotCasse;
                terrain.repaint();
                enMarche = false;
                throw new DansLeMur();
            } else {
                terrain.set(xa, ya, passage);
                passage = terrain.get(x, y);
                terrain.set(x, y, this);
            }
        }
        //debut ajout

        if(nombrePas>0) {
            decrementerPas();
        }
        //fin ajout

        terrain.repaint(x * terrain.getTailleCelluleX(), y * terrain.getTailleCelluleY(), terrain.getTailleCelluleX(), terrain.getTailleCelluleY());
        terrain.repaint(xa * terrain.getTailleCelluleX(), ya * terrain.getTailleCelluleY(), terrain.getTailleCelluleX(), terrain.getTailleCelluleY());

        try {

            Thread.sleep(duréeReference);
        } catch (InterruptedException ex) {
            enMarche = false;

            return;
        }


    }

    public Thread getProcessus() {
        return processus;
    }

    //@Override
    public void tourne() throws InterruptedException, TropDePas {
        vers = tOrientation[(vers.direction + 1) % 4];
        image = imageSelonOrientation();

        //debut ajout

        if(nombrePas>0) {
            decrementerPas();
        }
        //fin ajout

        terrain.repaint(x * terrain.getTailleCelluleX(), y * terrain.getTailleCelluleY(), terrain.getTailleCelluleX(), terrain.getTailleCelluleY());

        Thread.sleep(duréeReference);



    }

    //debut ajout
    public void decrementerPas() throws TropDePas{



            if(this.nombrePas*1.0 > this.nombreDepPas * 0.75 ){
                imageOrientation(vers,0);


            } else if(this.nombrePas*1.0 <= this.nombreDepPas * 0.75 && this.nombrePas*1.0 > this.nombreDepPas * 0.50 ){
                imageOrientation(vers, 1);


            } else if(this.nombrePas*1.0 <= this.nombreDepPas * 0.50 && this.nombrePas*1.0 > this.nombreDepPas * 0.25 ){
                imageOrientation(vers, 2);


            } else if (nombrePas>0){
                imageOrientation(vers,3);


            }

            this.nombrePas--;

            if (this.nombrePas == 0) {
                nombrePas=-1;
                imageOrientation(vers, 4);
                enMarche=false;
                terrain.repaint();

                throw new TropDePas();

            }


    }

    public void imageOrientation(Orientation orientation, int numero){
        Image[] couleurEnvoyer = new Image[1];

        if (orientation.direction == Terrain.NORD) {
            //image Nord
            couleurEnvoyer[0] = this.robotCouleurN[numero];
            this.image= couleurEnvoyer;
        } else if(orientation.direction == Terrain.EST){
            //image Est
            couleurEnvoyer[0] = this.robotCouleurE[numero];
            this.image= couleurEnvoyer;
        }else if(orientation.direction == Terrain.SUD){
            //image Sud
            couleurEnvoyer[0] = this.robotCouleurS[numero];
            this.image= couleurEnvoyer;
        }else{
            //image Ouest
            couleurEnvoyer[0] = this.robotCouleurO[numero];
            this.image= couleurEnvoyer;
        }
    }
    //fin ajout

    public synchronized void execute(Instruction i) {
        programme = i;
        stop();
        processus = new Thread(this);

        //processus.setDaemon(true);
        processus.start();
    }

    public void stop() {
        enMarche = false;
        if (processus != null) {
            while (processus.isAlive()) {
                processus.interrupt();
            }
        }

    }

    public void change() {

    }

    @Override
    public synchronized void run() {
        try {

            terrain.repaint();
            Thread.sleep(500);
            enMarche = true;
            image = imageSelonOrientation();
            terrain.repaint();
            Thread.sleep(500);

            programme.go(this);
            enMarche = false;
            Thread.sleep(500);
            image = imageSelonOrientation();
            terrain.repaint();
        } catch (DansLeMur ex) {
            //Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "<html><p>Erreur de programmation</p></html>",
                    "Bouuuum !!! Dans le mur",
                    JOptionPane.ERROR_MESSAGE,
                    new ImageIcon(Robot.class.getResource("resources/images/RobotCasse.png")));
            stop();

        } catch (InterruptedException ex) {
            //Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "<html><p>Arrêt du robot</p>"
                            + "<p>Sans doute s'agit-il d'une erreur de programmation</p></html>",
                    "Arrêt du robot",
                    JOptionPane.ERROR_MESSAGE);
            stop();
        } catch (NullPointerException ex) {
            Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(null, "<html><p>Arrêt du robot</p>" +
            //        "<p>Sans doute s'agit-il d'une erreur de programmation (null)</p></html>",
            //        "Arrêt du robot",
            //        JOptionPane.ERROR_MESSAGE);
            //processus.interrupt();

        } catch (TropDePas tropDePas) {
            tropDePas.printStackTrace();


        }
    }

    @Override
    public Image getImage() {
        //image.flush();
        return image[numeroImage % image.length];
    }

    //@Override
    public void poserUneMarque() {
        // Pour l'instant, le fait de poser une marque supprime tout ce qu'il pourra y avoir ici
        passage = new Marque(terrain.getTailleCelluleX(), terrain.getTailleCelluleY());
    }

    //@Override
    public void enleverUneMarque() {
        if (passage instanceof terrain.Marque) {
            passage = null;
        }
    }

    public static void setRobot(int i, Robot r)
    {
        robots[i] = r;
    }

    public static Robot[] getRobots()
    {
        return robots;
    }

    public static void setRobots(Robot[] r)
    {
        robots = r;
    }

    public int getID()
    {
        return ID;
    }
}
