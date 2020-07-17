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
package terrain;

import interfaces.Detachable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Le terrain de jeu du robot. Il peut contenir au départ 80x50 cellules. La
 * dimension du terrain en X et en Y Une cellule y sera représentée par un
 * rectangle plein dont la dimension est modifiable : (tailleCelluleX x
 * tailleCelluleY).
 *
 * @author maillot
 */
public class Terrain extends JPanel {

    private static final long serialVersionUID = 1L;

    private static Random random = new Random();
    public static final int NORD = 0;
    public static final int EST = 1;
    public static final int SUD = 2;
    public static final int OUEST = 3;
    public static final int maxX = 30;
    public static final int maxY = 20;
    public static final int minX = 5;
    public static final int minY = 5;
    public static final int NORD_EST = 0;
    public static final int SUD_EST = 1;
    public static final int SUD_OUEST = 2;
    public static final int NORD_OUEST = 3;
    private Cellule[][] terrain;
    private int nX;
    private int nY;
    private int tailleCelluleX = 30;
    private int tailleCelluleY = 30;

    public static class Position {

        public final int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private Detachable frameParente;

    public Terrain(Detachable frameParente) {
        this(frameParente, -1, -1);
        //repaint();
    }

    public Terrain(Detachable frameParente, int hauteur, int largeur) {
        this.frameParente = frameParente;
        if (largeur < 3) {
            nX = random.nextInt(maxX) + minX + 2;
        } else {
            nX = largeur;
        }
        if (hauteur < 3) {
            nY = random.nextInt(maxY) + minY + 2;
        } else {
            nY = hauteur;
        }

        terrain = new Cellule[nX][nY];

        Dimension dimension = frameParente.getSize();

        int height = dimension.height - 200;
        int width = dimension.width - 100;

        int rapportX = width / nX < 2 ? 1 : width / nX;
        int rapportY = height / nY < 2 ? 1 : height / nY;
        int rapport = rapportX < rapportY ? rapportX : rapportY;
        rapport = rapport > 50 ? 50 : rapport;

        tailleCelluleX = tailleCelluleY = rapport;

        placementDesMurs();

        setPreferredSize(new Dimension(nX * tailleCelluleX, nY * tailleCelluleY));
        setBackground(new Color(0xf0f1e4));

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {

                int x = me.getX() / getTailleCelluleX();
                int y = me.getY() / getTailleCelluleY();
                try {
                    if (terrain[x][y] instanceof Marque) {
                        terrain[x][y] = null;
                    } else {
                        terrain[x][y] = new Marque(getTailleCelluleX(), getTailleCelluleY(), true);
                    }
                    updateUI();
                } catch (ArrayIndexOutOfBoundsException ai) {
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                Dimension dimension = Terrain.this.frameParente.getSize();

                int height = dimension.height - 200;
                int width = dimension.width - 50;

                int rapportX = width / nX < 2 ? 1 : width / nX;
                int rapportY = height / nY < 2 ? 1 : height / nY;
                int rapport = rapportX < rapportY ? rapportX : rapportY;
                rapport = rapport > 50 ? 50 : rapport;

                tailleCelluleX = tailleCelluleY = rapport;

                setPreferredSize(new Dimension(nX * tailleCelluleX, nY * tailleCelluleY));
                for (int i = 0; i < nX; i++) {
                    for (int j = 0; j < nY; j++) {
                        if (terrain[i][j] != null) {
                            terrain[i][j].setTaille(tailleCelluleX, tailleCelluleY);
                        }
                    }
                }
            }
        });

    }

    private void placementDesMurs() {
        Mur mur = new Mur(getTailleCelluleX(), getTailleCelluleY());
        for (int x = 0; x < getNx(); x++) {
            set(x, 0, mur);
            set(x, getNy() - 1, mur);
        }
        for (int y = 0; y < getNy(); y++) {
            set(0, y, mur);
            set(getNx() - 1, y, mur);
        }

    }

    /**
     * Permet de tenir compte des redimensionnement de la fenêtre. Le calcul du
     * nombre de cellules en X et Y se fait en fonction de la dimension de la
     * fenêtre et de la taille des fourmis en X et en Y.
     *
     */
    private void changementDeDimension() {
        Dimension dimension = frameParente.getSize();

        int height = dimension.height - 200;
        int width = dimension.width - 100;

        int rapportX = width / nX < 2 ? 1 : width / nX;
        int rapportY = height / nY < 2 ? 1 : height / nY;
        int rapport = rapportX < rapportY ? rapportX : rapportY;
        rapport = rapport > 50 ? 50 : rapport;

        tailleCelluleX = tailleCelluleY = rapport;

        setPreferredSize(new Dimension(nX * tailleCelluleX, nY * tailleCelluleY));
        setBackground(new Color(0xf0f1e4));

    }

    /**
     * Permet de modifier le nombre de cellules en Y
     *
     * @param maxY nombre de cellules en Y
     */
    public void setNy(int maxY) {
        if (maxY < 0) {
            this.nY = 0;
        } else {
            this.nY = maxY;
        }
        changementDeDimension();
    }

    /**
     * Permet de modifier le nombre de cellules en X
     *
     * @param maxX nombre de cellules en X
     */
    public void setNx(int maxX) {
        if (maxX < 0) {
            this.nX = 0;
        } else {
            this.nX = maxX;
        }
        changementDeDimension();
    }

    /**
     * Retourne le nombre de cellules en Y
     *
     * @return le nombre de cellules en Y
     */
    public int getNy() {
        return nY;
    }

    /**
     * Retourne le nombre de cellules en X
     *
     * @return le nombre de cellules en X
     */
    public int getNx() {
        return nX;
    }

    /**
     *
     * @return
     */
    public int getTailleCelluleX() {
        return tailleCelluleX;
    }

    public void setTailleCelluleX(int tailleCelluleX) {
        this.tailleCelluleX = tailleCelluleX;
        changementDeDimension();
    }

    public int getTailleCelluleY() {
        return tailleCelluleY;
    }

    public void setTailleCelluleY(int tailleCelluleY) {
        this.tailleCelluleY = tailleCelluleY;
        changementDeDimension();
    }

    public void set(int x, int y, Cellule cellule) {
        //if (terrain[x][y] == null)
        //cellule.prend(terrain[x][y]);
        terrain[x][y] = cellule;
        //else
        //    terrain[x][y].prend(cellule);

    }

    public Cellule get(int x, int y) {
        return terrain[x][y];
    }

    @Override
    public void paint(java.awt.Graphics g) {
        super.paint(g);
        //int n = 0;
        if (terrain == null) {
            return;
        }

        for (int x = 0; x < nX; x++) {
            for (int y = 0; y < nY; y++) {
                //if (terrain[x][y] != null) {
                try {
                if (terrain[x][y].getImage() == null) {
                    //g.setColor(Color.GREEN/*terrain[x][y].getCouleur()*/);
                    g.fillRect(x * tailleCelluleX, y * tailleCelluleY, tailleCelluleX, tailleCelluleY);
                } else {
                    g.drawImage(terrain[x][y].getImage(), x * tailleCelluleX, y * tailleCelluleY, this);
                }
                } catch(NullPointerException npe) {
                    
                }
                //n++;
                //}
            }
        }
        int derY = tailleCelluleY * nY;
        int derX = tailleCelluleY * nX;
        g.setColor(Color.GRAY);
        for (int y = tailleCelluleY; y < derY; y += tailleCelluleY) {
            g.drawLine(0, y, derX, y);
        }
        for (int x = tailleCelluleX; x < derX; x += tailleCelluleX) {
            g.drawLine(x, 0, x, derY);
        }

    }

    public void ajouterMur(int x, int y) {
        terrain[x][y] = new Mur(tailleCelluleX, tailleCelluleY);
    }
}
