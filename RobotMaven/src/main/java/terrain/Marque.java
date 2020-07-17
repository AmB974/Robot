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

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author maillot
 */
public class Marque implements Cellule {
    private Image imagePrem, image;
    private Cellule cellule;
    private boolean exterieur;

    public boolean isExterieur() {
        return exterieur;
    }

    public Marque(int lx, int ly, boolean exterieur) {
        try {
            this.exterieur = exterieur;
            imagePrem = ImageIO.read(Marque.class.getResource("/images/marque.png"));
            image = imagePrem.getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            Logger.getLogger(Marque.class.getName()).log(Level.SEVERE, null, ex);
        }
        cellule = null;
    }
    
    public Marque(int lx, int ly) {
        this(lx, ly, false);
    }

    @Override
    public Color getCouleur() {
        return Color.ORANGE;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void setTaille(int lx, int ly) {
        image = imagePrem.getScaledInstance(lx, ly, Image.SCALE_SMOOTH);
    }

    /*public void prend(Cellule c) {
        if (cellule == null)
            cellule = c;
        else
            cellule.prend(c);
    }*/

}
