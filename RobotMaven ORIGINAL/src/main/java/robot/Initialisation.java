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

import interfaces.Detachable;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import terrain.Cellule;
import terrain.Minerai;
import terrain.Terrain;

/**
 *
 * @author Yvan
 */
public class Initialisation implements Serializable {

    public static int NBROBOTS = 4;//Ajouté par Sélim

    private static final long serialVersionUID = 1L;

    public static final int QUELCONQUE = -1;
    public static final int CONTRE_UN_MUR = 0;
    public static final int DANS_UN_COIN = 1;
    public static final int PAS_CONTRE_UN_MUR = 2;
    public static final int PAS_DANS_UN_COIN = 3;
    public static final int CONTRE_LE_MUR_NORD = 4;
    public static final int CONTRE_LE_MUR_EST = 5;
    public static final int CONTRE_LE_MUR_SUD = 6;
    public static final int CONTRE_LE_MUR_OUEST = 7;
    public static final int DANS_LE_COIN_NE = 8;
    public static final int DANS_LE_COIN_SE = 9;
    public static final int DANS_LE_COIN_SO = 10;
    public static final int DANS_LE_COIN_NO = 11;
    private int positionRobot = QUELCONQUE;
    private int orientationRobot = QUELCONQUE;
    private boolean presenceMinerai = false;
    private int positionMinerai = QUELCONQUE;
    private boolean presenceHauteur = false;
    private boolean presenceLargeur = false;
    private int hauteur;
    private int largeur;

    // debut Ajout
    private boolean presenceJauge;
    private int jauge;
    // fin Ajout

    private static Random random = new Random();

    public static Terrain.Position calculPosition(int position, Terrain terrain) {

        switch (position) {
            case Initialisation.QUELCONQUE:
                return new Terrain.Position(random.nextInt(terrain.getNx() - 2) + 1, random.nextInt(terrain.getNy() - 2) + 1);
            case Initialisation.CONTRE_UN_MUR:
                // Tirage au sort du mur
                int mur = random.nextInt(4);
                switch (mur) {
                    case Terrain.NORD:
                        return new Terrain.Position(random.nextInt(terrain.getNx() - 2) + 1, 1);
                    case Terrain.SUD:
                        return new Terrain.Position(random.nextInt(terrain.getNx() - 2) + 1, terrain.getNy() - 2);
                    case Terrain.OUEST:
                        return new Terrain.Position(1, random.nextInt(terrain.getNy() - 2) + 1);
                    case Terrain.EST:
                        return new Terrain.Position(terrain.getNx() - 2, random.nextInt(terrain.getNy() - 2) + 1);
                }
                break;
            case Initialisation.DANS_UN_COIN:
                // Tirage au sort du coin
                int coin = random.nextInt(4);
                switch (coin) {
                    case Terrain.NORD_EST:
                        return new Terrain.Position(terrain.getNx() - 2, 1);
                    case Terrain.SUD_EST:
                        return new Terrain.Position(terrain.getNx() - 2, terrain.getNy() - 2);
                    case Terrain.SUD_OUEST:
                        return new Terrain.Position(1, terrain.getNy() - 2);
                    case Terrain.NORD_OUEST:
                        return new Terrain.Position(1, 1);
                }
                break;
            case Initialisation.PAS_CONTRE_UN_MUR:
                return new Terrain.Position(random.nextInt(terrain.getNx() - 4) + 2, random.nextInt(terrain.getNy() - 4) + 2);
            case Initialisation.PAS_DANS_UN_COIN:
                int x;
                int y;
                do {
                    x = random.nextInt(terrain.getNx() - 2) + 1;
                    y = random.nextInt(terrain.getNy() - 2) + 1;
                } while (x == 1 && y == 1
                        || x == 1 && y == terrain.getNy() - 2
                        || x == terrain.getNx() - 2 && y == 1
                        || x == terrain.getNx() - 2 && y == terrain.getNy() - 2);
                return new Terrain.Position(x, y);

            case Initialisation.CONTRE_LE_MUR_NORD:
                return new Terrain.Position(random.nextInt(terrain.getNx() - 2) + 1, 1);
            case Initialisation.CONTRE_LE_MUR_EST:
                return new Terrain.Position(terrain.getNx() - 2, random.nextInt(terrain.getNy() - 2) + 1);
            case Initialisation.CONTRE_LE_MUR_SUD:
                return new Terrain.Position(random.nextInt(terrain.getNx() - 2) + 1, terrain.getNy() - 2);
            case Initialisation.CONTRE_LE_MUR_OUEST:
                return new Terrain.Position(1, random.nextInt(terrain.getNy() - 2) + 1);
            case Initialisation.DANS_LE_COIN_NE:
                return new Terrain.Position(terrain.getNx() - 2, 1);
            case Initialisation.DANS_LE_COIN_SE:
                return new Terrain.Position(terrain.getNx() - 2, terrain.getNy() - 2);

            case Initialisation.DANS_LE_COIN_SO:
                return new Terrain.Position(1, terrain.getNy() - 2);
            case Initialisation.DANS_LE_COIN_NO:
                return new Terrain.Position(1, 1);
            default:
                try {
                    throw new Exception("position : " + position + " Ne devrait pas arriver");
                } catch (Exception ex) {
                    Logger.getLogger(AppletPrincipale.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
        }
        return null;
    }

    //debut ajout
    public int getJauge(){ return jauge;}
    public void setJauge(int jauge){ this.jauge = jauge;}
    public boolean isPresenceJauge(){ return presenceJauge;}
    public void setPresenceJauge(boolean presenceJauge){ this.presenceJauge = presenceJauge;}
    //fin ajout

    public static int getNbRobots()
    {
        return NBROBOTS;
    }//Ajouté par Sélim

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }

    public boolean isPresenceHauteur() {
        return presenceHauteur;
    }

    public void setPresenceHauteur(boolean presenceHauteur) {
        this.presenceHauteur = presenceHauteur;
    }

    public boolean isPresenceLargeur() {
        return presenceLargeur;
    }

    public void setPresenceLargeur(boolean presenceLargeur) {
        this.presenceLargeur = presenceLargeur;
    }

    public int getOrientationRobot() {
        return orientationRobot;
    }

    public int getPositionMinerai() {
        return positionMinerai;
    }

    public int getPositionRobot() {
        return positionRobot;
    }

    public boolean isPresenceMinerai() {
        return presenceMinerai;
    }

    public void setOrientationRobot(int orientationRobot) {
        this.orientationRobot = orientationRobot;
    }

    public void setPositionMinerai(int positionMinerai) {
        this.positionMinerai = positionMinerai;
    }

    public void setPositionRobot(int positionRobot) {
        this.positionRobot = positionRobot;
    }

    public void setPrésenceMinerai(boolean presenceMinerai) {
        this.presenceMinerai = presenceMinerai;
    }

    @Override
    public String toString() {
        return "{Position du robot : " + positionRobot
                + ",\\nOrientation du robot : " + orientationRobot
                + ",\\nPrésence du minerai : " + presenceMinerai
                + ",\\nPosition du minerai : " + positionMinerai
                + ",\\nHauteur définie : " + presenceHauteur
                + ",\\nHauteur : " + hauteur
                + ",\\nLargeur définie : " + presenceLargeur
                + ",\\nLargeur : " + largeur
                + "}";
    }

    public Initialisation() {
    }

    public static void initialiser(Detachable frameParente, boolean marque) {
        initialiser(frameParente.getProgramme().getInitialisation(), frameParente, marque);
    }

    public static void initialiser(Initialisation initialisation, Detachable frameParente, boolean marque) {
        frameParente.getProgramme().setInitialisation(initialisation);
        frameParente.getDialogueInitialisation().setInitialisation(initialisation);
        int hauteur = -1;
        int largeur = -1;
        if (frameParente.getProgramme().getInitialisation().isPresenceHauteur()) {
            hauteur = frameParente.getProgramme().getInitialisation().getHauteur();
        }
        if (frameParente.getProgramme().getInitialisation().isPresenceLargeur()) {
            largeur = frameParente.getProgramme().getInitialisation().getLargeur();
        }

        // Récupérer les coordonnées des marques entrées à la souris
        class Coord {

            public final int x, y;

            Coord(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }
        Set<Coord> marquesALaSouris = new HashSet<Coord>();
        try {
            for (int x = 0; x < frameParente.getTerrain().getNx(); x++) {
                for (int y = 0; y < frameParente.getTerrain().getNy(); y++) {
                    Cellule c = frameParente.getTerrain().get(x, y);
                    if (c instanceof terrain.Marque) {
                        terrain.Marque m = (terrain.Marque) c;
                        if (m.isExterieur()) {
                            marquesALaSouris.add(new Coord(x, y));
                        }
                    }
                }
            }
        } catch (NullPointerException np) {
            // Arrive la première fois
        }

        if (frameParente.getTerrain() != null) {
            frameParente.viderTerrain();
        }



        frameParente.setTerrain(new Terrain(frameParente, hauteur, largeur));


        // Remettre les marques (tant que faire se peut, puisque
        // les dimensions du terrain ont pu changées
        if (marque) {
            for (Coord c : marquesALaSouris) {

                if (c.x > 0 && c.x < frameParente.getTerrain().getNx() - 1 && c.y > 0 && c.y < frameParente.getTerrain().getNy() - 1) {
                    frameParente.getTerrain().set(c.x, c.y, 
                            new terrain.Marque(frameParente.getTerrain().getTailleCelluleX(), frameParente.getTerrain().getTailleCelluleY(), true));
                }

            }
        }


        //placementDesMurs();
        // Le minerai doit être placé avant le robot...
        if (frameParente.getProgramme().getInitialisation().isPresenceMinerai()) {
            placementDuMinerai(frameParente.getProgramme().getInitialisation().getPositionMinerai(), frameParente.getTerrain());
        }

        for(int i=0; i<NBROBOTS; ++i) {
            placementDuRobot(frameParente.getProgramme().getInitialisation().getOrientationRobot(),
                    frameParente.getProgramme().getInitialisation().getPositionRobot(),
                    frameParente);
        }//Ajouté par Sélim

        frameParente.getPanneauTerrain().add(frameParente.getTerrain(), "Center");

        //debut ajout
        if(frameParente.getProgramme().getInitialisation().isPresenceJauge()){
            frameParente.getRobot().setNombrePas(frameParente.getProgramme().getInitialisation().getJauge());
            frameParente.getRobot().setNombreDepPas(frameParente.getProgramme().getInitialisation().getJauge());
            System.out.println("ici ta race"+frameParente.getProgramme().getInitialisation().getJauge());
        }
        //fin ajout


        frameParente.getRobot().duréeReference = frameParente.getPanneauCommande().getDuree();
        //Programme.changerDeRobot((Instruction) frameParente.getProgramme().getArbreProgramme().getRoot(), frameParente.getRobot());
    }

    private static void placementDuMinerai(int positionMinerai, Terrain terrain) {
        Terrain.Position p = Initialisation.calculPosition(positionMinerai, terrain);
        terrain.set(p.x, p.y, new Minerai(terrain.getTailleCelluleX(), terrain.getTailleCelluleY()));
    }

    private static void placementDuRobot(int orientationRobot, int positionRobot, Detachable frameParente) {

        if (orientationRobot == QUELCONQUE) {
            orientationRobot = random.nextInt(4);
        }

        Terrain.Position p;

        do{
            p = calculPosition(positionRobot, frameParente.getTerrain());
        }
        while(frameParente.getTerrain().get(p.x,p.y) != null);

        frameParente.setRobot(new Robot(frameParente.getTerrain(), p.x, p.y, orientationRobot));
    }//Ajouté par Sélim
}
