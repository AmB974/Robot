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
import java.awt.Dimension;
import java.util.Random;
import javax.swing.*;
import robot.panneaux.*;
import terrain.Terrain;


/**
 *
 * @author Yvan
 */
public class AppletPrincipale extends JApplet implements Detachable {

    private static final long serialVersionUID = 1L;

    public static final int VIDE = -2;
    public static final int QUELCONQUE = -1;
    public static final int CONTRE_UN_MUR = 0;
    public static final int DANS_UN_COIN = 1;
    public static final int PAS_CONTRE_UN_MUR = 2;
    private static int NBROBOTS = 4;
    private static int[] positionsRobots = {VIDE, QUELCONQUE, QUELCONQUE, QUELCONQUE, QUELCONQUE};
    private static int[] orientationsRobots = {VIDE, QUELCONQUE, QUELCONQUE, QUELCONQUE, QUELCONQUE};
    private static int ROBOTACTIF = 1;

    private static Random random = new Random();

    private PanneauPrincipal panneauPrincipal;
    private PanneauTerrain panneauTerrain = new PanneauTerrain();
    private Terrain terrain;
    private static Robot robot = null;
    private PanneauCommande panneauCommande;
    private Programme programme;
    private JSplitPane splitPane;
    private PanneauDExecution panneauDExecution;
    private JTree arbre;

    @Override
    public BoiteDeDialogueInit getDialogueInitialisation() {
        return dialogueInitialisation;
    }
    private BoiteDeDialogueInit dialogueInitialisation;
    //private File oldDir;
    
    @Override
    public JMenuBar getJMenuBar() {
        return super.getJMenuBar();
    }


    @Override
    public Terrain getTerrain() {
        return terrain;
    }

    @Override
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    @Override
    public JTree getArbre() {
        return arbre;
    }

    @Override
    public Programme getProgramme() {
        return programme;
    }

    @Override
    public Robot getRobot() {
        return robot;
    }

    @Override
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    @Override
    public void montreDialInit() {
        dialogueInitialisation.setVisible(true);
        if (dialogueInitialisation.getOk()) {
            programme.setInitialisation(dialogueInitialisation.getInitialisation());
            Initialisation.initialiser(dialogueInitialisation.getInitialisation(), this, true);
        }
    }

    @Override
    public void redessine() {
        if (terrain != null) {
            terrain.revalidate();
        }
    }

    @Override
    public void init() {
        //super("Le monde de nono");


        //initialisation = new Initialisation(this);
        //programme = new DefaultTreeModel(new Bloc("programme principal"));
        //programme.put("programme principal", new DefaultTreeModel(new Bloc("programme principal")));
        //setSize(800, 600);

        dialogueInitialisation = new BoiteDeDialogueInit(this);

        programme = new Programme();
        arbre = new JTreeRobot(programme.getArbreProgramme());
        
        setJMenuBar(new JMenuBar());
        
        //#########################################
        panneauPrincipal = new PanneauPrincipal(this);

        Dimension tailleMini = new Dimension(0, 0);

        // TEST SPLIT PANE
        JScrollPane scropane1 = new JScrollPane(panneauPrincipal);
        scropane1.setMinimumSize(tailleMini);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scropane1, panneauTerrain);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(scropane1.getPreferredSize().width);

        getContentPane().add(splitPane, "Center");
        panneauCommande = new PanneauCommande(this);
        getContentPane().add(panneauCommande, "South");

        Initialisation.initialiser(this, false);
        
        panneauDExecution = new PanneauDExecution(this);


        panneauTerrain.add(panneauDExecution, "South");

//#########################################


        //setVisible(true);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);

        //chooser = new JFileChooser();

        //FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers robot", "rob");
        //chooser.setFileFilter(filtre);

    }

    @Override
    public void viderTerrain() {
        panneauTerrain.remove(terrain);
    }

    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public PanneauTerrain getPanneauTerrain() {
        return panneauTerrain;
    }

    @Override
    public void setPanneauTerrain(PanneauTerrain panneau) {
        this.panneauTerrain = panneau;
    }

    @Override
    public PanneauCommande getPanneauCommande() {
        return panneauCommande;
    }

    @Override
    public void setPanneauCommande(PanneauCommande panneau) {
        this.panneauCommande = panneau;
    }

    @Override
    public void executeProgramme() {
        panneauDExecution.executeProgramme();
    }

    @Override
    public void executeSelection() {
        panneauDExecution.executeSelection();
    }

    @Override
    public void setTitle(String nom) {
        
    }

    public static int getNbRobots()
    {
        return NBROBOTS;
    }

    public static int getPositionRobotActif()
    {
        return positionsRobots[ROBOTACTIF];
    }

    public static int getPositionRobot(int i)
    {
        return positionsRobots[i];
    }

    public static void setPositionRobot(int positionRobot, int robot) {
        positionsRobots[robot] = positionRobot;
    }

    public static void setOrientationRobot(int orientationRobot, int robot)
    {
        orientationsRobots[robot] = orientationRobot;
    }

    public static int getOrientationRobotActif(){
        return orientationsRobots[AppletPrincipale.getROBOTACTIF()];
    }

    public static int getOrientationRobot(int i)
    {
        return orientationsRobots[i];
    }

    public static void setRobot(int i, Robot r)
    {
        robot.getRobots()[i] = r;
    }

    public static void setROBOTACTIF(int id)
    {
        ROBOTACTIF = id;
        robot = Robot.getRobots()[ROBOTACTIF];
    }

    public static int getROBOTACTIF()
    {
        return ROBOTACTIF;
    }
}

