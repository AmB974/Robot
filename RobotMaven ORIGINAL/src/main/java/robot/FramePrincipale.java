package robot;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import interfaces.Detachable;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import robot.panneaux.*;
import terrain.Terrain;

/**
 * @author Yvan
 */
public class FramePrincipale extends JFrame implements Detachable {

    private static Robot robot = null;
    private static int NBROBOTS = 1;
    //private static int NBRobotsSurTerrain=1;


    //Ajouté par Sélim

    private static final long serialVersionUID = 1L;

    private static Random random = new Random();
    private PanneauPrincipal panneauPrincipal;
    private PanneauTerrain panneauTerrain = new PanneauTerrain();
    private Terrain terrain;

    private static PanneauCommande panneauCommande;
    private Programme[] programme = new Programme[5];
    private JSplitPane splitPane;
    private JTreeRobot[] arbre = new JTreeRobot[5];
    private JFileChooser chooser;
    private BoiteDeDialogueInit dialogueInitialisation;
    //private File oldDir;
    private PanneauDExecution panneauDExecution;
    private static int ROBOTACTIF = 1;
    private static int VIDE = -2;
    private static int QUELCONQUE = -1;
    private static int[] orientationsRobots = {VIDE, QUELCONQUE, QUELCONQUE, QUELCONQUE, QUELCONQUE};
    private static int[] positionsRobots = {VIDE, QUELCONQUE, QUELCONQUE, QUELCONQUE, QUELCONQUE};
    private static int[] nombrePas = {VIDE,VIDE,VIDE,VIDE,VIDE};


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

    /*
     * private class Position {
     *
     * int x, y; }
     */

    @Override
    public JTree getArbre() {
        return arbre[ROBOTACTIF];
    }

    @Override
    public Programme getProgramme() {
        return programme[ROBOTACTIF];
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
            programme[ROBOTACTIF].setInitialisation(dialogueInitialisation.getInitialisation());
            Initialisation.initialiser(dialogueInitialisation.getInitialisation(), this, true, FramePrincipale.getNbRobots());
        }
    }

    @Override
    public void redessine() {
        if (terrain != null) {
            terrain.revalidate();
        }
    }

    public FramePrincipale(int niveau) {
        this();
        panneauPrincipal.setNiveau(niveau);
        panneauPrincipal.figerNiveau(niveau);
    }

    public FramePrincipale() {
        super("Le monde de Nono");
        setJMenuBar(new JMenuBar());

        setSize(800, 600);

        dialogueInitialisation = new BoiteDeDialogueInit(this);

        for(int i=1; i<5; ++i)
        {
            programme[i] = new Programme();
            arbre[i] = new JTreeRobot(programme[i].getArbreProgramme());
        }

        miseEnPlaceDesMenus();

        panneauPrincipal = new PanneauPrincipal(this);

        Dimension tailleMini = new Dimension(0, 0);

        // TEST SPLIT PANE
        JScrollPane scropane1 = new JScrollPane(panneauPrincipal);
        scropane1.setMinimumSize(tailleMini);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                panneauPrincipal, panneauTerrain);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(700);

        getContentPane().add(splitPane, "Center");
        panneauCommande = new PanneauCommande(this);
        getContentPane().add(panneauCommande, "South");

        Initialisation.initialiser(this, false, NBROBOTS);

        panneauDExecution = new PanneauDExecution(this);

        panneauTerrain.add(panneauDExecution, "South");

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chooser = new JFileChooser();

        FileNameExtensionFilter filtre = new FileNameExtensionFilter("Fichiers robot", "rob");
        chooser.setFileFilter(filtre);

        pack();
    }

    private void miseEnPlaceDesMenus() {
        JMenuBar barreMenu = new JMenuBar();
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem ouvrir = new JMenuItem("Ouvrir...");
        JMenuItem enregistrer = new JMenuItem("Enregistrer...");

        setJMenuBar(barreMenu);

        barreMenu.add(menuFichier);
        menuFichier.add(ouvrir);
        menuFichier.add(enregistrer);
        enregistrer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();

                    if (!Pattern.matches(".*\\.rob$", f.toString())) {
                        f = new File(f.toString() + ".rob");
                    }
                    XStream xstream = new XStream(new DomDriver());
                    ObjectOutputStream os = null;
                    try {

                        xstream.toXML(programme, new FileOutputStream(f));
                        os = new ObjectOutputStream(new FileOutputStream(new File(f.toString() + ".obj")));
                        os.writeObject(programme);
                        FramePrincipale.this.setTitle(f.getAbsolutePath());

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(FramePrincipale.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FramePrincipale.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            os.close();
                        } catch (IOException ex) {
                            Logger.getLogger(FramePrincipale.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        ouvrir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        URL f = chooser.getSelectedFile().toURI().toURL();

                        panneauPrincipal.ouvrir(f);
                    } catch (MalformedURLException ex) {
                        //Logger.getLogger(FramePrincipale.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

    }

    public static void setRobotActif(int id) {
        robot = Robot.getRobots()[id];
        ROBOTACTIF = id;
    }

    @Override
    public BoiteDeDialogueInit getDialogueInitialisation() {
        return dialogueInitialisation;
    }

    @Override
    public void viderTerrain() {
        panneauTerrain.remove(terrain);
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

    public static int getNbRobots() {
        return NBROBOTS;
    }
    public static void setNbRobots( int nb) {
        NBROBOTS= nb;
    }

    public static int getOrientationRobotActif() {
        return orientationsRobots[ROBOTACTIF];
    }

    public static int getPositionRobotActif() {
        return positionsRobots[ROBOTACTIF];
    }

    public static void setOrientationRobot(int orientation, int id) {
        orientationsRobots[id] = orientation;
    }

    public static void setPositionRobot(int position, int id) {
        positionsRobots[id] = position;
    }

    public static int getROBOTACTIF() {
        return ROBOTACTIF;
    }

    public static int getOrientationRobot(int i) {
        return orientationsRobots[i];
    }

    public static int getPositionRobot(int i) {
        return positionsRobots[i];
    }

    public static void setRobot(int i, Robot r) {
        Robot.getRobots()[i] = r;
    }

    public static void resetComboBoxEtROBOTACTIF()
    {
        setRobotActif(1);
        panneauCommande.getComboRobotSelectionne().setSelectedIndex(FramePrincipale.getROBOTACTIF()-1);
    }

    public static void setNombreDePas(int i, int nbPas) {
        nombrePas[i] = nbPas;
    }
    public static int getNombreDePas(int i)
    {
        return nombrePas[i];
    }


    
}
