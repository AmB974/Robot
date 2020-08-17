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

    //Ajouté par Sélim

    private static final long serialVersionUID = 1L;

    private final PanneauDExecution panneauDExecution;
    private PanneauCommande panneauCommande;
    private final PanneauPrincipal panneauPrincipal;
    private PanneauTerrain panneauTerrain = new PanneauTerrain();

    private Terrain terrain;

    private final JSplitPane splitPane;
    private final JFileChooser chooser;
    private final BoiteDeDialogueInit dialogueInitialisation;


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
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    @Override
    public void montreDialInit() {
        dialogueInitialisation.setVisible(true);
        if (dialogueInitialisation.getOk()) {
            terrain.getRobotSelectionne().getProgramme().setInitialisation(dialogueInitialisation.getInitialisation());
            terrain.getRobotSelectionne().getProgramme().getInitialisation().initialiser(this, true);
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
        terrain = new Terrain(this);
        setSize(800, 600);

        dialogueInitialisation = new BoiteDeDialogueInit(this);
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

        terrain.getRobotSelectionne().getProgramme().getInitialisation().initialiser(this, true);

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

                        xstream.toXML(terrain.getRobotSelectionne().getProgramme(), new FileOutputStream(f));
                        os = new ObjectOutputStream(new FileOutputStream(new File(f.toString() + ".obj")));
                        os.writeObject(terrain.getRobotSelectionne().getProgramme());
                        FramePrincipale.this.setTitle(f.getAbsolutePath());

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

    @Override
    public PanneauPrincipal getPanneauPrincipal()
    {
        return panneauPrincipal;
    }
}
