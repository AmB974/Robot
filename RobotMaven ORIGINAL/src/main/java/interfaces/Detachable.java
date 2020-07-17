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
package interfaces;

import java.awt.Dimension;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import robot.Programme;
import robot.Robot;
import robot.panneaux.BoiteDeDialogueInit;
import robot.panneaux.PanneauCommande;
import robot.panneaux.PanneauTerrain;
import terrain.Terrain;

/**
 *
 * @author yvan.maillot
 */
public interface Detachable {


    public void idSuivant(); // Ajouté par Sélim

    public void switchRobot(); // Ajouté par Sélim

    public Programme getProgramme();
    
    public Terrain getTerrain();
    
    public void setTerrain(Terrain terrain);
    
    public JSplitPane getSplitPane();    

    public void redessine();

    public Robot getRobot();

    public void setRobot(Robot robot);

    public Dimension getSize();

    public JTree getArbre();

    public void montreDialInit();
    
    public JMenuBar getJMenuBar();
    
    public BoiteDeDialogueInit getDialogueInitialisation();
    
    public void viderTerrain();
    
    public PanneauTerrain getPanneauTerrain();
    
    public void setPanneauTerrain(PanneauTerrain panneau);
    
    public PanneauCommande getPanneauCommande();
    
    public void setPanneauCommande(PanneauCommande panneau);
    
    public void executeProgramme();
    
    public void executeSelection();
    
    public void setTitle(String nom);

}
