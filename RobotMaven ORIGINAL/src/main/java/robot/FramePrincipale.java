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

    private static Robot robot=null;

    //Ajouté par Sélim

    private static final long serialVersionUID = 1L;

    private static Random random = new Random();
    private PanneauPrincipal panneauPrincipal;
    private PanneauTerrain panneauTerrain = new PanneauTerrain();
    private Terrain terrain;

    private PanneauCommande panneauCommande;
    private Programme programme;
    private JSplitPane splitPane;
    private JTreeRobot arbre;
    private JFileChooser chooser;
    private BoiteDeDialogueInit dialogueInitialisation;
    //private File oldDir;
    private PanneauDExecution panneauDExecution;


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

        programme = new Programme();
        arbre = new JTreeRobot(programme.getArbreProgramme());

        miseEnPlaceDesMenus();

        panneauPrincipal = new PanneauPrincipal(this);

        Dimension tailleMini = new Dimension(0, 0);

        // TEST SPLIT PANE
        //JScrollPane scropane1 = new JScrollPane(panneauPrincipal);
        //scropane1.setMinimumSize(tailleMini);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                panneauPrincipal, panneauTerrain);

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(700);

        getContentPane().add(splitPane, "Center");
        panneauCommande = new PanneauCommande(this);
        getContentPane().add(panneauCommande, "South");

        Initialisation.initialiser(this, false);

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

    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
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
}
