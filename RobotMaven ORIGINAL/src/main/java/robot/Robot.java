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
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    public int dureeReference = 200;
    private Orientation[] tOrientation = {new Orientation(0, 0, -1), new Orientation(1, 1, 0), new Orientation(2, 0, 1), new Orientation(3, -1, 0)};
    private Orientation vers = tOrientation[0];
    private Color couleur;
    private Terrain terrain;
    private int x, y;
    private Instruction programme;
    private Image[] image = new Image[1];
    private Image[] robotN = new Image[4];
    private Image[] robotS = new Image[4];
    private Image[] robotE = new Image[4];
    private Image[] robotO = new Image[4];

    private Image[] robotNActif = new Image[4];
    private Image[] robotSActif = new Image[4];
    private Image[] robotEActif = new Image[4];
    private Image[] robotOActif = new Image[4];

    private Image[] robotCasse = new Image[4];
    private Image[] robotNeM = new Image[4];
    private Image[] robotSeM = new Image[4];
    private Image[] robotEeM = new Image[4];
    private Image[] robotOeM = new Image[4];
    private Image[] robotNprem = new Image[4];
    private Image[] robotSprem = new Image[4];
    private Image[] robotEprem = new Image[4];
    private Image[] robotOprem = new Image[4];

    private Image[] robotNpremActif = new Image[4];
    private Image[] robotSpremActif = new Image[4];
    private Image[] robotEpremActif = new Image[4];
    private Image[] robotOpremActif = new Image[4];

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

    public int getNombrePas() {
        return this.nombrePas;
    }

    public void setNombrePas(int nombrePas) {
        this.nombrePas = nombrePas;
    }

    public void setNombreDepPas(int nombreDepPas) {
        this.nombreDepPas = nombreDepPas;
    }

    private Image[][] robotCouleurE = new Image[5][4];
    private Image[][] robotCouleurEprem = new Image[5][4];

    private Image[][] robotCouleurS = new Image[5][4];
    private Image[][] robotCouleurSprem = new Image[5][4];

    private Image[][] robotCouleurO = new Image[5][4];
    private Image[][] robotCouleurOprem = new Image[5][4];

    private Image[][] robotCouleurN = new Image[5][4];
    private Image[][] robotCouleurNprem = new Image[5][4];

    private boolean pasInitialise = false;
    private boolean casser = false;
    private int echelon=0;

    public boolean isCasser() {
        return this.casser;
    }

    public void setCasser(boolean casser) {
        this.casser = casser;
    }

    public boolean isPasInitialise() {
        return this.pasInitialise;
    }

    public void setPasInitialise(boolean pasInitialise) {
        this.pasInitialise = pasInitialise;
    }
    public int getEchelon(){
        return echelon;
    }
    //fin ajout

    private static Integer cpt = 0;
    private static Robot[] robots = new Robot[5];
    private final int ID;
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
        robotE[1] = robotEprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotE[2] = robotEprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotE[3] = robotEprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);


        robotS[0] = robotSprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotS[1] = robotSprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotS[2] = robotSprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotS[3] = robotSprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotO[0] = robotOprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotO[1] = robotOprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotO[2] = robotOprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotO[3] = robotOprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotN[0] = robotNprem[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotN[1] = robotNprem[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotN[2] = robotNprem[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotN[3] = robotNprem[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);


        robotEActif[0] = robotEpremActif[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotEActif[1] = robotEpremActif[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotEActif[2] = robotEpremActif[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotEActif[3] = robotEpremActif[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);


        robotSActif[0] = robotSpremActif[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotSActif[1] = robotSpremActif[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotSActif[2] = robotSpremActif[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotSActif[3] = robotSpremActif[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotOActif[0] = robotOpremActif[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotOActif[1] = robotOpremActif[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotOActif[2] = robotOpremActif[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotOActif[3] = robotOpremActif[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotNActif[0] = robotNpremActif[0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotNActif[1] = robotNpremActif[1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotNActif[2] = robotNpremActif[2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotNActif[3] = robotNpremActif[3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

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


        robotCouleurE[0][0] = robotCouleurEprem[0][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[1][0] = robotCouleurEprem[1][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[2][0] = robotCouleurEprem[2][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[3][0] = robotCouleurEprem[3][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[4][0] = robotCouleurEprem[4][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurE[0][1] = robotCouleurEprem[0][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[1][1] = robotCouleurEprem[1][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[2][1] = robotCouleurEprem[2][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[3][1] = robotCouleurEprem[3][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[4][1] = robotCouleurEprem[4][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurE[0][2] = robotCouleurEprem[0][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[1][2] = robotCouleurEprem[1][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[2][2] = robotCouleurEprem[2][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[3][2] = robotCouleurEprem[3][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[4][2] = robotCouleurEprem[4][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurE[0][3] = robotCouleurEprem[0][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[1][3] = robotCouleurEprem[1][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[2][3] = robotCouleurEprem[2][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[3][3] = robotCouleurEprem[3][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurE[4][3] = robotCouleurEprem[4][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);


        robotCouleurS[0][0] = robotCouleurSprem[0][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[1][0] = robotCouleurSprem[1][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[2][0] = robotCouleurSprem[2][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[3][0] = robotCouleurSprem[3][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[4][0] = robotCouleurSprem[4][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurS[0][1] = robotCouleurSprem[0][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[1][1] = robotCouleurSprem[1][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[2][1] = robotCouleurSprem[2][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[3][1] = robotCouleurSprem[3][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[4][1] = robotCouleurSprem[4][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurS[0][2] = robotCouleurSprem[0][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[1][2] = robotCouleurSprem[1][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[2][2] = robotCouleurSprem[2][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[3][2] = robotCouleurSprem[3][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[4][2] = robotCouleurSprem[4][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurS[0][3] = robotCouleurSprem[0][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[1][3] = robotCouleurSprem[1][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[2][3] = robotCouleurSprem[2][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[3][3] = robotCouleurSprem[3][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurS[4][3] = robotCouleurSprem[4][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);


        robotCouleurO[0][0] = robotCouleurOprem[0][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[1][0] = robotCouleurOprem[1][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[2][0] = robotCouleurOprem[2][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[3][0] = robotCouleurOprem[3][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[4][0] = robotCouleurOprem[4][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurO[0][1] = robotCouleurOprem[0][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[1][1] = robotCouleurOprem[1][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[2][1] = robotCouleurOprem[2][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[3][1] = robotCouleurOprem[3][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[4][1] = robotCouleurOprem[4][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurO[0][2] = robotCouleurOprem[0][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[1][2] = robotCouleurOprem[1][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[2][2] = robotCouleurOprem[2][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[3][2] = robotCouleurOprem[3][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[4][2] = robotCouleurOprem[4][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurO[0][3] = robotCouleurOprem[0][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[1][3] = robotCouleurOprem[1][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[2][3] = robotCouleurOprem[2][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[3][3] = robotCouleurOprem[3][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurO[4][3] = robotCouleurOprem[4][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);


        robotCouleurN[0][0] = robotCouleurNprem[0][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[1][0] = robotCouleurNprem[1][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[2][0] = robotCouleurNprem[2][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[3][0] = robotCouleurNprem[3][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[4][0] = robotCouleurNprem[4][0].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurN[0][1] = robotCouleurNprem[0][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[1][1] = robotCouleurNprem[1][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[2][1] = robotCouleurNprem[2][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[3][1] = robotCouleurNprem[3][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[4][1] = robotCouleurNprem[4][1].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurN[0][2] = robotCouleurNprem[0][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[1][2] = robotCouleurNprem[1][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[2][2] = robotCouleurNprem[2][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[3][2] = robotCouleurNprem[3][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[4][2] = robotCouleurNprem[4][2].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);

        robotCouleurN[0][3] = robotCouleurNprem[0][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[1][3] = robotCouleurNprem[1][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[2][3] = robotCouleurNprem[2][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[3][3] = robotCouleurNprem[3][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        robotCouleurN[4][3] = robotCouleurNprem[4][3].getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
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


    private void init(Terrain terrain, int x, int y, int direction, Color couleur) {
        this.terrain = terrain;
        this.couleur = couleur;
        passage = terrain.get(x, y);
        this.x = x;
        this.y = y;
        vers = tOrientation[direction];
        terrain.set(x, y, this);

        try {

            robotEprem[0] = ImageIO.read(Robot.class.getResource("/images/robotBlancEstUn.png"));
            robotEprem[1] = ImageIO.read(Robot.class.getResource("/images/robotBlancEstDeux.png"));
            robotEprem[2] = ImageIO.read(Robot.class.getResource("/images/robotBlancEstTrois.png"));
            robotEprem[3] = ImageIO.read(Robot.class.getResource("/images/robotBlancEstQuatre.png"));

            robotE[0] = robotEprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotE[1] = robotEprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotE[2] = robotEprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotE[3] = robotEprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


            robotSprem[0] = ImageIO.read(Robot.class.getResource("/images/robotBlancSudUn.png"));
            robotSprem[1] = ImageIO.read(Robot.class.getResource("/images/robotBlancSudDeux.png"));
            robotSprem[2] = ImageIO.read(Robot.class.getResource("/images/robotBlancSudTrois.png"));
            robotSprem[3] = ImageIO.read(Robot.class.getResource("/images/robotBlancSudQuatre.png"));

            robotS[0] = robotSprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotS[1] = robotSprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotS[2] = robotSprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotS[3] = robotSprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotOprem[0] = ImageIO.read(Robot.class.getResource("/images/robotBlancOuestUn.png"));
            robotOprem[1] = ImageIO.read(Robot.class.getResource("/images/robotBlancOuestDeux.png"));
            robotOprem[2] = ImageIO.read(Robot.class.getResource("/images/robotBlancOuestTrois.png"));
            robotOprem[3] = ImageIO.read(Robot.class.getResource("/images/robotBlancOuestQuatre.png"));

            robotO[0] = robotOprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotO[1] = robotOprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotO[2] = robotOprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotO[3] = robotOprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


            robotNprem[0] = ImageIO.read(Robot.class.getResource("/images/robotBlancNordUn.png"));
            robotNprem[1] = ImageIO.read(Robot.class.getResource("/images/robotBlancNordDeux.png"));
            robotNprem[2] = ImageIO.read(Robot.class.getResource("/images/robotBlancNordTrois.png"));
            robotNprem[3] = ImageIO.read(Robot.class.getResource("/images/robotBlancNordQuatre.png"));

            robotN[0] = robotNprem[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotN[1] = robotNprem[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotN[2] = robotNprem[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotN[3] = robotNprem[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


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


            robotEpremActif[0] = ImageIO.read(Robot.class.getResource("/images/robotBlancEstUnActif.png"));
            robotEpremActif[1] = ImageIO.read(Robot.class.getResource("/images/robotBlancEstDeuxActif.png"));
            robotEpremActif[2] = ImageIO.read(Robot.class.getResource("/images/robotBlancEstTroisActif.png"));
            robotEpremActif[3] = ImageIO.read(Robot.class.getResource("/images/robotBlancEstQuatreActif.png"));

            robotEActif[0] = robotEpremActif[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotEActif[1] = robotEpremActif[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotEActif[2] = robotEpremActif[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotEActif[3] = robotEpremActif[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


            robotSpremActif[0] = ImageIO.read(Robot.class.getResource("/images/robotBlancSudUnActif.png"));
            robotSpremActif[1] = ImageIO.read(Robot.class.getResource("/images/robotBlancSudDeuxActif.png"));
            robotSpremActif[2] = ImageIO.read(Robot.class.getResource("/images/robotBlancSudTroisActif.png"));
            robotSpremActif[3] = ImageIO.read(Robot.class.getResource("/images/robotBlancSudQuatreActif.png"));

            robotSActif[0] = robotSpremActif[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotSActif[1] = robotSpremActif[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotSActif[2] = robotSpremActif[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotSActif[3] = robotSpremActif[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotOpremActif[0] = ImageIO.read(Robot.class.getResource("/images/robotBlancOuestUnActif.png"));
            robotOpremActif[1] = ImageIO.read(Robot.class.getResource("/images/robotBlancOuestDeuxActif.png"));
            robotOpremActif[2] = ImageIO.read(Robot.class.getResource("/images/robotBlancOuestTroisActif.png"));
            robotOpremActif[3] = ImageIO.read(Robot.class.getResource("/images/robotBlancOuestQuatreActif.png"));

            robotOActif[0] = robotOpremActif[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotOActif[1] = robotOpremActif[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotOActif[2] = robotOpremActif[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotOActif[3] = robotOpremActif[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


            robotNpremActif[0] = ImageIO.read(Robot.class.getResource("/images/robotBlancNordUnActif.png"));
            robotNpremActif[1] = ImageIO.read(Robot.class.getResource("/images/robotBlancNordDeuxActif.png"));
            robotNpremActif[2] = ImageIO.read(Robot.class.getResource("/images/robotBlancNordTroisActif.png"));
            robotNpremActif[3] = ImageIO.read(Robot.class.getResource("/images/robotBlancNordQuatreActif.png"));

            robotNActif[0] = robotNpremActif[0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotNActif[1] = robotNpremActif[1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotNActif[2] = robotNpremActif[2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotNActif[3] = robotNpremActif[3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


            robotCouleurEprem[0][0] = ImageIO.read(Robot.class.getResource("/images/robotVertEstUn.png"));
            robotCouleurEprem[1][0] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireEstUn.png"));
            robotCouleurEprem[2][0] = ImageIO.read(Robot.class.getResource("/images/robotJauneEstUn.png"));
            robotCouleurEprem[3][0] = ImageIO.read(Robot.class.getResource("/images/robotOrangeEstUn.png"));
            robotCouleurEprem[4][0] = ImageIO.read(Robot.class.getResource("/images/robotRougeEstUn.png"));

            robotCouleurEprem[0][1] = ImageIO.read(Robot.class.getResource("/images/robotVertEstDeux.png"));
            robotCouleurEprem[1][1] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireEstDeux.png"));
            robotCouleurEprem[2][1] = ImageIO.read(Robot.class.getResource("/images/robotJauneEstDeux.png"));
            robotCouleurEprem[3][1] = ImageIO.read(Robot.class.getResource("/images/robotOrangeEstDeux.png"));
            robotCouleurEprem[4][1] = ImageIO.read(Robot.class.getResource("/images/robotRougeEstDeux.png"));

            robotCouleurEprem[0][2] = ImageIO.read(Robot.class.getResource("/images/robotVertEstTrois.png"));
            robotCouleurEprem[1][2] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireEstTrois.png"));
            robotCouleurEprem[2][2] = ImageIO.read(Robot.class.getResource("/images/robotJauneEstTrois.png"));
            robotCouleurEprem[3][2] = ImageIO.read(Robot.class.getResource("/images/robotOrangeEstTrois.png"));
            robotCouleurEprem[4][2] = ImageIO.read(Robot.class.getResource("/images/robotRougeEstTrois.png"));

            robotCouleurEprem[0][3] = ImageIO.read(Robot.class.getResource("/images/robotVertEstQuatre.png"));
            robotCouleurEprem[1][3] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireEstQuatre.png"));
            robotCouleurEprem[2][3] = ImageIO.read(Robot.class.getResource("/images/robotJauneEstQuatre.png"));
            robotCouleurEprem[3][3] = ImageIO.read(Robot.class.getResource("/images/robotOrangeEstQuatre.png"));
            robotCouleurEprem[4][3] = ImageIO.read(Robot.class.getResource("/images/robotRougeEstQuatre.png"));

            robotCouleurE[0][0] = robotCouleurEprem[0][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[1][0] = robotCouleurEprem[1][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[2][0] = robotCouleurEprem[2][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[3][0] = robotCouleurEprem[3][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[4][0] = robotCouleurEprem[4][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurE[0][1] = robotCouleurEprem[0][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[1][1] = robotCouleurEprem[1][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[2][1] = robotCouleurEprem[2][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[3][1] = robotCouleurEprem[3][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[4][1] = robotCouleurEprem[4][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurE[0][2] = robotCouleurEprem[0][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[1][2] = robotCouleurEprem[1][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[2][2] = robotCouleurEprem[2][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[3][2] = robotCouleurEprem[3][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[4][2] = robotCouleurEprem[4][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurE[0][3] = robotCouleurEprem[0][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[1][3] = robotCouleurEprem[1][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[2][3] = robotCouleurEprem[2][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[3][3] = robotCouleurEprem[3][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurE[4][3] = robotCouleurEprem[4][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


            robotCouleurSprem[0][0] = ImageIO.read(Robot.class.getResource("/images/robotVertSudUn.png"));
            robotCouleurSprem[1][0] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireSudUn.png"));
            robotCouleurSprem[2][0] = ImageIO.read(Robot.class.getResource("/images/robotJauneSudUn.png"));
            robotCouleurSprem[3][0] = ImageIO.read(Robot.class.getResource("/images/robotOrangeSudUn.png"));
            robotCouleurSprem[4][0] = ImageIO.read(Robot.class.getResource("/images/robotRougeSudUn.png"));

            robotCouleurSprem[0][1] = ImageIO.read(Robot.class.getResource("/images/robotVertSudDeux.png"));
            robotCouleurSprem[1][1] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireSudDeux.png"));
            robotCouleurSprem[2][1] = ImageIO.read(Robot.class.getResource("/images/robotJauneSudDeux.png"));
            robotCouleurSprem[3][1] = ImageIO.read(Robot.class.getResource("/images/robotOrangeSudDeux.png"));
            robotCouleurSprem[4][1] = ImageIO.read(Robot.class.getResource("/images/robotRougeSudDeux.png"));

            robotCouleurSprem[0][2] = ImageIO.read(Robot.class.getResource("/images/robotVertSudTrois.png"));
            robotCouleurSprem[1][2] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireSudTrois.png"));
            robotCouleurSprem[2][2] = ImageIO.read(Robot.class.getResource("/images/robotJauneSudTrois.png"));
            robotCouleurSprem[3][2] = ImageIO.read(Robot.class.getResource("/images/robotOrangeSudTrois.png"));
            robotCouleurSprem[4][2] = ImageIO.read(Robot.class.getResource("/images/robotRougeSudTrois.png"));

            robotCouleurSprem[0][3] = ImageIO.read(Robot.class.getResource("/images/robotVertSudQuatre.png"));
            robotCouleurSprem[1][3] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireSudQuatre.png"));
            robotCouleurSprem[2][3] = ImageIO.read(Robot.class.getResource("/images/robotJauneSudQuatre.png"));
            robotCouleurSprem[3][3] = ImageIO.read(Robot.class.getResource("/images/robotOrangeSudQuatre.png"));
            robotCouleurSprem[4][3] = ImageIO.read(Robot.class.getResource("/images/robotRougeSudQuatre.png"));

            robotCouleurS[0][0] = robotCouleurSprem[0][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[1][0] = robotCouleurSprem[1][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[2][0] = robotCouleurSprem[2][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[3][0] = robotCouleurSprem[3][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[4][0] = robotCouleurSprem[4][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurS[0][1] = robotCouleurSprem[0][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[1][1] = robotCouleurSprem[1][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[2][1] = robotCouleurSprem[2][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[3][1] = robotCouleurSprem[3][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[4][1] = robotCouleurSprem[4][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurS[0][2] = robotCouleurSprem[0][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[1][2] = robotCouleurSprem[1][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[2][2] = robotCouleurSprem[2][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[3][2] = robotCouleurSprem[3][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[4][2] = robotCouleurSprem[4][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurS[0][3] = robotCouleurSprem[0][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[1][3] = robotCouleurSprem[1][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[2][3] = robotCouleurSprem[2][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[3][3] = robotCouleurSprem[3][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurS[4][3] = robotCouleurSprem[4][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


            robotCouleurOprem[0][0] = ImageIO.read(Robot.class.getResource("/images/robotVertOuestUn.png"));
            robotCouleurOprem[1][0] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireOuestUn.png"));
            robotCouleurOprem[2][0] = ImageIO.read(Robot.class.getResource("/images/robotJauneOuestUn.png"));
            robotCouleurOprem[3][0] = ImageIO.read(Robot.class.getResource("/images/robotOrangeOuestUn.png"));
            robotCouleurOprem[4][0] = ImageIO.read(Robot.class.getResource("/images/robotRougeOuestUn.png"));

            robotCouleurOprem[0][1] = ImageIO.read(Robot.class.getResource("/images/robotVertOuestDeux.png"));
            robotCouleurOprem[1][1] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireOuestDeux.png"));
            robotCouleurOprem[2][1] = ImageIO.read(Robot.class.getResource("/images/robotJauneOuestDeux.png"));
            robotCouleurOprem[3][1] = ImageIO.read(Robot.class.getResource("/images/robotOrangeOuestDeux.png"));
            robotCouleurOprem[4][1] = ImageIO.read(Robot.class.getResource("/images/robotRougeOuestDeux.png"));

            robotCouleurOprem[0][2] = ImageIO.read(Robot.class.getResource("/images/robotVertOuestTrois.png"));
            robotCouleurOprem[1][2] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireOuestTrois.png"));
            robotCouleurOprem[2][2] = ImageIO.read(Robot.class.getResource("/images/robotJauneOuestTrois.png"));
            robotCouleurOprem[3][2] = ImageIO.read(Robot.class.getResource("/images/robotOrangeOuestTrois.png"));
            robotCouleurOprem[4][2] = ImageIO.read(Robot.class.getResource("/images/robotRougeOuestTrois.png"));

            robotCouleurOprem[0][3] = ImageIO.read(Robot.class.getResource("/images/robotVertOuestQuatre.png"));
            robotCouleurOprem[1][3] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireOuestQuatre.png"));
            robotCouleurOprem[2][3] = ImageIO.read(Robot.class.getResource("/images/robotJauneOuestQuatre.png"));
            robotCouleurOprem[3][3] = ImageIO.read(Robot.class.getResource("/images/robotOrangeOuestQuatre.png"));
            robotCouleurOprem[4][3] = ImageIO.read(Robot.class.getResource("/images/robotRougeOuestQuatre.png"));

            robotCouleurO[0][0] = robotCouleurOprem[0][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[1][0] = robotCouleurOprem[1][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[2][0] = robotCouleurOprem[2][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[3][0] = robotCouleurOprem[3][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[4][0] = robotCouleurOprem[4][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurO[0][1] = robotCouleurOprem[0][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[1][1] = robotCouleurOprem[1][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[2][1] = robotCouleurOprem[2][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[3][1] = robotCouleurOprem[3][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[4][1] = robotCouleurOprem[4][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurO[0][2] = robotCouleurOprem[0][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[1][2] = robotCouleurOprem[1][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[2][2] = robotCouleurOprem[2][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[3][2] = robotCouleurOprem[3][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[4][2] = robotCouleurOprem[4][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurO[0][3] = robotCouleurOprem[0][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[1][3] = robotCouleurOprem[1][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[2][3] = robotCouleurOprem[2][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[3][3] = robotCouleurOprem[3][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurO[4][3] = robotCouleurOprem[4][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);


            robotCouleurNprem[0][0] = ImageIO.read(Robot.class.getResource("/images/robotVertNordUn.png"));
            robotCouleurNprem[1][0] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireNordUn.png"));
            robotCouleurNprem[2][0] = ImageIO.read(Robot.class.getResource("/images/robotJauneNordUn.png"));
            robotCouleurNprem[3][0] = ImageIO.read(Robot.class.getResource("/images/robotOrangeNordUn.png"));
            robotCouleurNprem[4][0] = ImageIO.read(Robot.class.getResource("/images/robotRougeNordUn.png"));

            robotCouleurNprem[0][1] = ImageIO.read(Robot.class.getResource("/images/robotVertNordDeux.png"));
            robotCouleurNprem[1][1] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireNordDeux.png"));
            robotCouleurNprem[2][1] = ImageIO.read(Robot.class.getResource("/images/robotJauneNordDeux.png"));
            robotCouleurNprem[3][1] = ImageIO.read(Robot.class.getResource("/images/robotOrangeNordDeux.png"));
            robotCouleurNprem[4][1] = ImageIO.read(Robot.class.getResource("/images/robotRougeNordDeux.png"));

            robotCouleurNprem[0][2] = ImageIO.read(Robot.class.getResource("/images/robotVertNordTrois.png"));
            robotCouleurNprem[1][2] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireNordTrois.png"));
            robotCouleurNprem[2][2] = ImageIO.read(Robot.class.getResource("/images/robotJauneNordTrois.png"));
            robotCouleurNprem[3][2] = ImageIO.read(Robot.class.getResource("/images/robotOrangeNordTrois.png"));
            robotCouleurNprem[4][2] = ImageIO.read(Robot.class.getResource("/images/robotRougeNordTrois.png"));

            robotCouleurNprem[0][3] = ImageIO.read(Robot.class.getResource("/images/robotVertNordQuatre.png"));
            robotCouleurNprem[1][3] = ImageIO.read(Robot.class.getResource("/images/robotVertClaireNordQuatre.png"));
            robotCouleurNprem[2][3] = ImageIO.read(Robot.class.getResource("/images/robotJauneNordQuatre.png"));
            robotCouleurNprem[3][3] = ImageIO.read(Robot.class.getResource("/images/robotOrangeNordQuatre.png"));
            robotCouleurNprem[4][3] = ImageIO.read(Robot.class.getResource("/images/robotRougeNordQuatre.png"));

            robotCouleurN[0][0] = robotCouleurNprem[0][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[1][0] = robotCouleurNprem[1][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[2][0] = robotCouleurNprem[2][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[3][0] = robotCouleurNprem[3][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[4][0] = robotCouleurNprem[4][0].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurN[0][1] = robotCouleurNprem[0][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[1][1] = robotCouleurNprem[1][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[2][1] = robotCouleurNprem[2][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[3][1] = robotCouleurNprem[3][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[4][1] = robotCouleurNprem[4][1].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurN[0][2] = robotCouleurNprem[0][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[1][2] = robotCouleurNprem[1][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[2][2] = robotCouleurNprem[2][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[3][2] = robotCouleurNprem[3][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[4][2] = robotCouleurNprem[4][2].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

            robotCouleurN[0][3] = robotCouleurNprem[0][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[1][3] = robotCouleurNprem[1][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[2][3] = robotCouleurNprem[2][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[3][3] = robotCouleurNprem[3][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);
            robotCouleurN[4][3] = robotCouleurNprem[4][3].getScaledInstance(terrain.getTailleCelluleX(), terrain.getTailleCelluleY(), Image.SCALE_SMOOTH);

        } catch (IOException ex) {
            Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("ID : " + ID);
        gestionImage(0);
        terrain.repaint();


    }

    /**
     * Créer un robot "quelque part" sur le terrain.
     *
     * @param terrain : terrain sur lequel circule le robot
     * @see public Robot(Terrain terrain, int x, int y)
     */
    public Robot(Terrain terrain) {

        if (cpt < FramePrincipale.getNbRobots())
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim
        init(terrain, random.nextInt(terrain.getNx()), random.nextInt(terrain.getNy()), random.nextInt(4), new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lancementAnimation();

    }

    public Robot(Terrain terrain, int x, int y) {

        if (cpt < FramePrincipale.getNbRobots())
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim
        init(terrain, x, y, random.nextInt(4), new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lancementAnimation();
    }

    public Robot(Terrain terrain, int x, int y, Color couleur) {

        if (cpt < FramePrincipale.getNbRobots())
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim
        init(terrain, x, y, random.nextInt(4), new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lancementAnimation();
    }

    public Robot(Terrain terrain, Color couleur) {

        if (cpt < FramePrincipale.getNbRobots())
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
            init(terrain, random.nextInt(terrain.getNx()), random.nextInt(terrain.getNy()), random.nextInt(4), couleur);
            lancementAnimation();
        } //Ajouté par Sélim
    }

    public Robot(Terrain terrain, int x, int y, int dir) {

        if (cpt < FramePrincipale.getNbRobots())
            ID = ++cpt;
        else {
            cpt = 1;
            ID = cpt;
        } //Ajouté par Sélim
        init(terrain, x, y, dir, new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        lancementAnimation();
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
                image[0] = robotCasse[0];
                terrain.repaint();
                enMarche = false;
                casser = true;
                throw new DansLeMur();
            } else {
                terrain.set(xa, ya, passage);
                passage = terrain.get(x, y);
                terrain.set(x, y, this);
            }
        }
        //debut ajout
        System.out.println("pasInitialiser avance : " + pasInitialise);

        if (pasInitialise) {
            decrementerPas();
        }
        else{
            gestionImage(echelon);
        }
        //fin ajout
        /*
        emplacement du robot actuelle
         */
        terrain.repaint(x * terrain.getTailleCelluleX(), y * terrain.getTailleCelluleY(), terrain.getTailleCelluleX(), terrain.getTailleCelluleY());

        /*
        emplacement du robot avant d'avancer
         */
        terrain.repaint(xa * terrain.getTailleCelluleX(), ya * terrain.getTailleCelluleY(), terrain.getTailleCelluleX(), terrain.getTailleCelluleY());

        try {

            Thread.sleep(dureeReference);
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
        gestionImage(0);

        //debut ajout
        System.out.println("pasInitialiser tourne : " + pasInitialise);
        if (pasInitialise) {
            decrementerPas();
        }
        //fin ajout

        terrain.repaint(x * terrain.getTailleCelluleX(), y * terrain.getTailleCelluleY(), terrain.getTailleCelluleX(), terrain.getTailleCelluleY());

        Thread.sleep(dureeReference);


    }

    //debut ajout
    public void decrementerPas() throws TropDePas {


        if (this.nombrePas * 1.0 > this.nombreDepPas * 0.75) {
            echelon=0;
            gestionImage(echelon);


        } else if (this.nombrePas * 1.0 <= this.nombreDepPas * 0.75 && this.nombrePas * 1.0 > this.nombreDepPas * 0.50) {
            echelon=1;
           gestionImage(echelon);


        } else if (this.nombrePas * 1.0 <= this.nombreDepPas * 0.50 && this.nombrePas * 1.0 > this.nombreDepPas * 0.25) {
            echelon=2;
            gestionImage(echelon);


        } else if (nombrePas > 0) {
            echelon=3;
            gestionImage(echelon);


        }

        this.nombrePas--;

        if (this.nombrePas == 0) {
            nombrePas = -1;
            gestionImage(4);
            echelon=0;
            enMarche = false;
            terrain.repaint();
            pasInitialise = false;

            throw new TropDePas();

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
            gestionImage(0);
            terrain.repaint();
            Thread.sleep(500);

            programme.go(this);
            enMarche = false;
            Thread.sleep(500);
            gestionImage(0);
            terrain.repaint();
        } catch (DansLeMur ex) {
            //Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "<html><p>Erreur de programmation</p></html>",
                    "Bouuuum !!! Dans le mur",
                    JOptionPane.ERROR_MESSAGE);
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

    public static void setRobot(int i, Robot r) {
        robots[i] = r;
    }

    public static Robot[] getRobots() {
        return robots;
    }

    public static void setRobots(Robot[] r) {
        robots = r;
    }

    public int getID() {
        return ID;
    }

    public Instruction getInstruction()
    {
        return programme;
    }

    public static int getCpt()
    {
        return cpt;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setCpt(int nb) {
        cpt = nb;
    }

    public void gestionImage(int numero) {

        if (!casser) {
            if (!pasInitialise) {


                if (this.ID != FramePrincipale.getROBOTACTIF()) {
                    if (vers.direction == Terrain.EST) {
                        image[0] = robotE[ID - 1];

                    } else if (vers.direction == Terrain.OUEST) {
                        image[0] = robotO[ID - 1];
                    } else if (vers.direction == Terrain.SUD) {
                        image[0] = robotS[ID - 1];
                    } else {
                        image[0] = robotN[ID - 1];
                    }
                } else {
                    if (vers.direction == Terrain.EST) {
                        image[0] = robotEActif[ID - 1];

                    } else if (vers.direction == Terrain.OUEST) {
                        image[0] = robotOActif[ID - 1];
                    } else if (vers.direction == Terrain.SUD) {
                        image[0] = robotSActif[ID - 1];
                    } else {
                        image[0] = robotNActif[ID - 1];
                    }
                }
            } else {
                if (vers.direction == Terrain.NORD) {
                    //image Nord
                    image[0] = this.robotCouleurN[numero][ID - 1];

                } else if (vers.direction == Terrain.EST) {
                    //image Est
                    image[0] = this.robotCouleurE[numero][ID - 1];

                } else if (vers.direction == Terrain.SUD) {
                    //image Sud
                    image[0] = this.robotCouleurS[numero][ID - 1];

                } else {
                    //image Ouest
                    image[0] = this.robotCouleurO[numero][ID - 1];

                }

            }
            terrain.repaint(x * terrain.getTailleCelluleX(), y * terrain.getTailleCelluleY(), terrain.getTailleCelluleX(), terrain.getTailleCelluleY());

        } else {
            casser = false;
        }
    }
}
