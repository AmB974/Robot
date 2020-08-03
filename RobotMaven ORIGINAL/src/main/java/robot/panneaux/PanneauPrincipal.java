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
package robot.panneaux;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;
import expressions.*;
import instruction.*;
import interfaces.Detachable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import robot.*;

class JMenuItemNiveau extends JMenuItem {

    private static final long serialVersionUID = 1L;

    int niveau;

    JMenuItemNiveau(int niveau, String texte) {
        super(texte);
        this.niveau = niveau;
    }

    public int getNiveau() {
        return niveau;
    }
}

/**
 *
 * @author Maillot
 */
public class PanneauPrincipal extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JButton boutonInitialise = new JButton("Initialisation");
    private final JButton boutonAvance = new JButton("Avance");
    private final JButton boutonTourne = new JButton("Tourne");
    private final JButton boutonMarque = new JButton("Marque");
    private final JButton boutonEfface = new JButton("Efface");
    private final JButton boutonSi = new JButton("Si");
    private final JButton boutonTantQue = new JButton("Tant que");
    private final JButton boutonPour = new JButton("Pour");
    private final JLabel de = new JLabel("De", JLabel.CENTER);
    private final JTextField texteDebutPour = new JTextField("1");
    private final JLabel à = new JLabel("À", JLabel.CENTER);
    private final JTextField texteFinPour = new JTextField("10");
    private final JLabel pas = new JLabel("Pas", JLabel.CENTER);
    private final JTextField textePasPour = new JTextField("1");
    private final JButton boutonBloc = new JButton("Bloc");
    private final JTextField texteBloc = new JTextField();
    private final JTextArea texteExprBool = new JTextArea(3, 5);
    private ExprBool exprBoolComplexe = null;
    private ParseurExprBool parseur;
    private final JButton boutonAjoutProcedure = new JButton("Nouvelle procédure");
    private final JButton boutonAppelProcedure = new JButton("Appel de procédure");
    private final JComboBox<NoeudProgramme> comboAppelProcedure = new JComboBox<>();
    private final JTextField texteNouvelleProcedure = new JTextField();
    private final JButton boutonEcrire = new JButton("Écrire");
    private final JButton boutonLire = new JButton("Lire");
    private final JTextField texteLireEcrire = new JTextField();
    private final JButton boutonSupprime = new JButton("Supprimer");
    private final JButton boutonCopie = new JButton("Copier");
    private final JButton boutonColle = new JButton("Coller");
    private final JButton boutonCoupe = new JButton("Couper");
    private final JButton boutonExecutionProgramme = new JButton("Exécution du programme");
    private final JButton boutonExecutionSelection = new JButton("Exécution de la sélection");
    private ByteArrayOutputStream brancheCopiee[] = null;
    private final JComboBox<ExprBool> comboExpression = new JComboBox<>();
    //private JTree arbre;
    private final Detachable frameParente;
    //private PanneauProcedure panneauProcedure;
    //private boolean controleSyntaxique = true;
    private JPanel panneauProg;
    private ActionListener ajoutInstruction;
    private final GridBagConstraints placementProg = new GridBagConstraints();
    private final BoiteDeDialHTML exercicesDialog;
    private final BoiteDeDialHTML manuelDialog;
    private final BoiteDeDialHTML aProposDialog;
    private JMenu menuNiveau;
    private static final String[] nomsFichiersDemo = {
        "Se dérouiller les roulettes sans risque",
        "S'orienter",
        "Avancer jusqu'au mur d'en face",
        "Faire le tour du terrain",
        "S'orienter avec un tant que",
        "Au milieu d'un bord",
        "Il y a du minerai sur un bord; se placer devant",
        /*"Il y a peut-être du minerai sur un bord; se placer devant s'il existe (coince)",
         "Il y a peut-être du minerai sur un bord; se placer devant s'il existe (coince pas mais PB)",*/
        "Il y a peut-être du minerai sur un bord; se placer devant s'il existe",
        "Au milieu d'un bord avec procédures",
        "Au milieu d'un bord avec procédures (fausse bonne idée)",
        "Trouver la plus grande colonne de marques",
        /*"Dessiner un rectangle de x par y",
         "Dessiner un triangle",*/
        "Dessiner un carré de 10 x 10",
        "Dessiner un carré de 10 x 10 en faisant 5 allers et retours",
        "Dessiner un triangle isocèle",
        "Compter",
        "Compter LSB à droite",
        "Tri à bulle",
        "Additionner"
    };

    public PanneauPrincipal(Detachable frameParente) {

        this.exercicesDialog = new BoiteDeDialHTML(PanneauPrincipal.class.getResource("/html/exercices.html")/*new File("resources/html/exercices.html").toURI().toURL()*/);
        this.manuelDialog = new BoiteDeDialHTML(PanneauPrincipal.class.getResource("/html/manuel_utilisateur.html")/*new File("resources/html/manuel_utilisateur.html").toURI().toURL()*/);
        this.aProposDialog = new BoiteDeDialHTML(PanneauPrincipal.class.getResource("/html/aPropos.html")/*new File("resources/html/aPropos.html").toURI().toURL()*/);

        this.frameParente = frameParente;

        creationDesAttributs();

        // Placement
        panneauProg.setLayout(new GridBagLayout());

        int pl = 0;
        placementComposant(boutonInitialise, pl++, 0, 4);
        placementComposant(new JLabel("Programmation", JLabel.CENTER), pl++, 0, 4);
        placementComposant(boutonAvance, pl, 0);
        placementComposant(boutonTourne, pl, 1);
        placementComposant(boutonMarque, pl, 2);
        placementComposant(boutonEfface, pl++, 3);
        placementComposant(boutonBloc, pl++, 0, 4);
        placementComposant(texteExprBool, pl++, 0, 4);
        placementComposant(boutonSi, pl, 0);
        placementComposant(boutonTantQue, pl, 1);
        placementComposant(comboExpression, pl++, 2, 2);
        placementComposant(boutonPour, pl, 0);

        JPanel panneauDebut = new JPanel(new GridLayout(1, 2));
        panneauDebut.add(de);
        panneauDebut.add(texteDebutPour);
        placementComposant(panneauDebut, pl, 1);

        JPanel panneauFin = new JPanel(new GridLayout(1, 2));
        panneauFin.add(à);
        panneauFin.add(texteFinPour);
        placementComposant(panneauFin, pl, 2);

        JPanel panneauPas = new JPanel(new GridLayout(1, 2));
        panneauPas.add(pas);
        panneauPas.add(textePasPour);
        placementComposant(panneauPas, pl++, 3);

        //placementComposant(panneauProcedure, 6, 0, 4);
        placementComposant(texteNouvelleProcedure, pl++, 0, 4);
        placementComposant(boutonAjoutProcedure, pl++, 0, 4);
        placementComposant(comboAppelProcedure, pl++, 0, 4);
        placementComposant(boutonAppelProcedure, pl++, 0, 4);

        placementComposant(boutonLire, pl, 0, 1);
        placementComposant(boutonEcrire, pl, 1, 1);
        placementComposant(texteLireEcrire, pl++, 2, 2);

        placementComposant(new JLabel("Édition", JLabel.CENTER), pl++, 0, 4);
        placementComposant(boutonSupprime, pl, 0);
        placementComposant(boutonCoupe, pl, 1);
        placementComposant(boutonCopie, pl, 2);
        placementComposant(boutonColle, pl++, 3);

        placementComposant(new JLabel(" ", JLabel.CENTER), pl++, 0, 4);

        placementComposant(new JLabel(" ", JLabel.CENTER), pl++, 0, 4);
        placementComposant(new JLabel(" ", JLabel.CENTER), pl++, 0, 4);
        placementComposant(new JLabel("Exécution", JLabel.CENTER), pl++, 0, 4);
        placementComposant(boutonExecutionProgramme, pl, 0, 2);
        placementComposant(boutonExecutionSelection, pl, 2, 2);

        miseEnPlaceDesMenus();

    }

    public String[] getListeTests() {
        ArrayList<String> liste = new ArrayList<>();
        for (int i = 0; i < comboExpression.getItemCount(); i++) {
            ExprBoolElt exp = (ExprBoolElt) comboExpression.getItemAt(i);
            liste.add(exp.toString());
            liste.add(exp.getAbr());
        }
        return liste.toArray(new String[0]);
    }

    public void setNiveau(int n) {

        boutonAvance.setVisible(true);
        boutonTourne.setVisible(true);
        boutonMarque.setVisible(true);
        boutonEfface.setVisible(true);
        texteExprBool.setVisible(true);
        boutonBloc.setVisible(true);
        boutonSi.setVisible(true);
        boutonTantQue.setVisible(true);
        comboExpression.setVisible(true);
        boutonAjoutProcedure.setVisible(true);
        boutonAppelProcedure.setVisible(true);
        texteNouvelleProcedure.setVisible(true);
        comboAppelProcedure.setVisible(true);
        boutonEcrire.setVisible(true);
        boutonLire.setVisible(true);
        texteLireEcrire.setVisible(true);

        boutonPour.setVisible(true);
        texteDebutPour.setVisible(true);
        texteFinPour.setVisible(true);
        textePasPour.setVisible(true);
        de.setVisible(true);
        à.setVisible(true);
        pas.setVisible(true);

        switch (n) {
            case 1:
                boutonSi.setVisible(false);
                boutonBloc.setVisible(false);
                comboExpression.setVisible(false);
            case 2:
                boutonTantQue.setVisible(false);
            case 3:
                texteExprBool.setVisible(false);
            case 4:
                boutonAjoutProcedure.setVisible(false);
                boutonAppelProcedure.setVisible(false);
                texteNouvelleProcedure.setVisible(false);
                comboAppelProcedure.setVisible(false);
            case 5:
                boutonPour.setVisible(false);
                texteDebutPour.setVisible(false);
                texteFinPour.setVisible(false);
                textePasPour.setVisible(false);
                de.setVisible(false);
                à.setVisible(false);
                pas.setVisible(false);
            case 6:
                boutonEcrire.setVisible(false);
                boutonLire.setVisible(false);
                texteLireEcrire.setVisible(false);
        }
    }

    private void miseEnPlaceDesMenus() {

        menuNiveau = new JMenu("Niveaux");

        ArrayList<JMenuItemNiveau> itemNiveau = new ArrayList<>();
        int i = 1;
        itemNiveau.add(new JMenuItemNiveau(i++, "Niveau 1 : Instructions élémentaires seules"));
        itemNiveau.add(new JMenuItemNiveau(i++, "Niveau 2 : Instruction alternative"));
        itemNiveau.add(new JMenuItemNiveau(i++, "Niveau 3 : Instruction itérative"));
        itemNiveau.add(new JMenuItemNiveau(i++, "Niveau 4 : Expressions booléennes complexes"));
        itemNiveau.add(new JMenuItemNiveau(i++, "Niveau 5 : Procédures"));
        itemNiveau.add(new JMenuItemNiveau(i++, "Niveau 6 : Instruction itérative (pour)"));
        itemNiveau.add(new JMenuItemNiveau(i++, "Niveau 7 : Lecture et écriture"));

        ActionListener actionMenuNiveau = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItemNiveau jniveau = (JMenuItemNiveau) e.getSource();
                setNiveau(jniveau.getNiveau());
            }
        };

        for (JMenuItemNiveau item : itemNiveau) {
            menuNiveau.add(item);
            item.addActionListener(actionMenuNiveau);
        }

        JMenu menuAide = new JMenu("Aide");
        JMenuItem aide = new JMenuItem("Aide...");
        JMenuItem exo = new JMenuItem("Exercices...");
        JMenuItem aPropos = new JMenuItem("À propos...");

        frameParente.getJMenuBar().add(menuNiveau);

        frameParente.getJMenuBar().add(menuAide);

        // #### Enlevé temporairement pour cause de bug
        frameParente.getJMenuBar().add(PanneauPrincipal.this.getMenuDemo());

        menuAide.add(aide);
        menuAide.add(exo);
        menuAide.add(aPropos);

        aide.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                manuelDialog.setVisible(true);
            }
        });
        exo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exercicesDialog.setVisible(true);
            }
        });
        aPropos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                aProposDialog.setVisible(true);
            }
        });

        boutonExecutionProgramme.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                frameParente.executeProgramme();
            }
        });

        boutonExecutionSelection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                frameParente.executeSelection();
            }
        });
    }

    /*public void ouvrirOBJ(URL fichier) {
     try {
     ObjectInputStream oi = null;
     try {
     oi = new ObjectInputStream(fichier.openStream());
     } catch (IOException ex) {
     Logger.getLogger(PanneauPrincipal.class.getName()).log(Level.SEVERE, null, ex);
     } catch (NullPointerException ex) {
     System.out.println("Le fichier s'appelle : " + fichier);
     ex.printStackTrace();
     }
     Programme nouveauProgramme = (Programme) oi.readObject();



     frameParente.setTitle(fichier.getFile());


     setAppelProcedure(nouveauProgramme.getProcedures());
     Programme programme = frameParente.getProgramme();

     Instruction racine = (Instruction) frameParente.getProgramme().getArbreProgramme().getRoot();
     while (racine.getChildCount() > 0) {
     programme.getArbreProgramme().removeNodeFromParent((MutableTreeNode) racine.getFirstChild());
     }


     Instruction nouvelleRacine = (Instruction) nouveauProgramme.getArbreProgramme().getRoot();

     programme.supprimerProcedure();
     while (nouvelleRacine.getChildCount() > 0) {
     Instruction instruction = (Instruction) nouvelleRacine.getFirstChild();
     programme.ajoutProcedure((Bloc) instruction);
     programme.getArbreProgramme().insertNodeInto(instruction, racine, racine.getChildCount());
     }

     //Initialisation.initialiser(nouveauProgramme.getInitialisation(), FramePrincipale.this);
     Initialisation.initialiser(nouveauProgramme.getInitialisation(), frameParente, false);
     for (int i = 0; i < frameParente.getArbre().getRowCount(); i++) {
     frameParente.getArbre().expandRow(i);
     }
     } catch (IOException ex) {
     Logger.getLogger(PanneauPrincipal.class.getName()).log(Level.SEVERE, null, ex);
     } catch (ClassNotFoundException ex) {
     Logger.getLogger(PanneauPrincipal.class.getName()).log(Level.SEVERE, null, ex);
     }

     }*/
    public void ouvrir(URL fichier) {
        /*if (Pattern.matches(".*\\.obj$", fichier.getFile())) {
         ouvrirOBJ(fichier);
         } else {*/

        XStream xstream = new XStream(new DomDriver());

        Programme nouveauProgramme = null;

        try {
            nouveauProgramme = (Programme) xstream.fromXML(fichier.openStream());

            frameParente.setTitle(fichier.getFile());

            setAppelProcedure(nouveauProgramme.getProcedures());
            Programme programme = frameParente.getProgramme();
            Instruction racine = (Instruction) frameParente.getProgramme().getArbreProgramme().getRoot();

            while (racine.getChildCount()
                    > 0) {
                programme.getArbreProgramme().removeNodeFromParent((MutableTreeNode) racine.getFirstChild());
            }
            Instruction nouvelleRacine = (Instruction) nouveauProgramme.getArbreProgramme().getRoot();

            programme.supprimerProcedure();

            while (nouvelleRacine.getChildCount()
                    > 0) {
                Instruction instruction = (Instruction) nouvelleRacine.getFirstChild();
                programme.ajoutProcedure((Bloc) instruction);
                programme.getArbreProgramme().insertNodeInto(instruction, racine, racine.getChildCount());
            }

            //Initialisation.initialiser(nouveauProgramme.getInitialisation(), FramePrincipale.this);
            Initialisation.initialiser(nouveauProgramme.getInitialisation(), frameParente, false);
            for (int i = 0;
                    i < frameParente.getArbre().getRowCount(); i++) {
                frameParente.getArbre().expandRow(i);
            }

        } catch (FileNotFoundException | NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Le fichier " + fichier + " semble absent", "Problème d'ouverture de fichier", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Le fichier " + fichier + " est illisible", "Problème d'ouverture de fichier", JOptionPane.ERROR_MESSAGE);
        } catch (StreamException se) {
            JOptionPane.showMessageDialog(this, "Le fichier " + fichier + " semble incompatible", "Problème d'incompatibilité de fichier", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur " + ex/*Arrays.toString(ex.getStackTrace())*/, "Erreur inattendue", JOptionPane.ERROR_MESSAGE);
        }
        //}
    }

    private JMenu getMenuDemo() {

        JMenu demo = new JMenu("Démonstrations");
        for (String nom : nomsFichiersDemo) {
            ajouterNomsAuMenu(demo, nom);
        }

        return demo;
    }

    private JMenuItem getMenuItem(final String nom) {
        JMenuItem item = new JMenuItem(nom);
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                PanneauPrincipal.this.ouvrir(PanneauPrincipal.class.getResource("/sources/" + nom + ".rob"));
            }
        });
        return item;
    }

    private void creationDesAttributs() {

        panneauProg = new JPanel();

        ajoutInstruction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath treePath = PanneauPrincipal.this.frameParente.getArbre().getSelectionPath();
                Instruction select;
                //if (treePath != null) {
                if (treePath == null) {
                    select = (Instruction) PanneauPrincipal.this.frameParente.getArbre().getModel().getRoot();
                } else {
                    select = (Instruction) treePath.getLastPathComponent();
                }
                if (select.getParent() == null) {
                    select = PanneauPrincipal.this.frameParente.getProgramme().getProcedurePrincipal();
                }

                JButton source = (JButton) e.getSource();

                Instruction instruction = null;
                if (source == boutonAvance) {
                    instruction = new Avance(/*
                             * PanneauPrincipal.this.frameParente.getRobot()
                             */);
                }
                else if (source == boutonTourne) {
                    instruction = new Tourne(/*
                             * PanneauPrincipal.this.frameParente.getRobot()
                             */);
                } else if (source == boutonMarque) {
                    instruction = new Marque(/*
                             * PanneauPrincipal.this.frameParente.getRobot()
                             */);
                } else if (source == boutonEfface) {
                    instruction = new Efface(/*
                             * PanneauPrincipal.this.frameParente.getRobot()
                             */);
                } else if (source == boutonTantQue) {
                    ExprBool exp;
                    if (exprBoolComplexe != null) {
                        exp = exprBoolComplexe;
                        texteExprBool.setText("");
                    } else {
                        exp = (ExprBoolElt) PanneauPrincipal.this.comboExpression.getSelectedItem();
                    }
                    //ExpressionBoolenne pasDevantMur = new PasDevantMur(Programme.this.robot);
                    instruction = new TantQue(exp);
                } else if (source == boutonSi) {
                    ExprBool exp;
                    if (exprBoolComplexe == null) {
                        exp = (ExprBool) PanneauPrincipal.this.comboExpression.getSelectedItem();
                    } else {
                        exp = exprBoolComplexe;
                    }
                    instruction = new Si(exp);
                } else if (source == boutonBloc) {
                    if (texteBloc.getText().equalsIgnoreCase("")) {
                        instruction = new Bloc("bloc");
                    } else {
                        instruction = new Bloc(texteBloc.getText());
                    }
                } else if (source == boutonAjoutProcedure) {
                    if (texteNouvelleProcedure.getText().length() > 0
                            && PanneauPrincipal.this.frameParente.getProgramme().getProcedure(texteNouvelleProcedure.getText()) == null) {
                        instruction = PanneauPrincipal.this.frameParente.getProgramme().ajoutProcedure(texteNouvelleProcedure.getText());
                        comboAppelProcedure.addItem(instruction);
                        instruction = null; // Important, ne pas enlever
                    }
                } else if (source == boutonAppelProcedure) {
                    instruction = new Appel((Bloc) comboAppelProcedure.getSelectedItem());
                } else if (source == boutonPour) {
                    if (select instanceof InstructionComposee) {
                        InstructionComposee parent = (InstructionComposee) select;
                        String de, a, pas;
                        de = texteDebutPour.getText();
                        a = texteFinPour.getText();
                        pas = textePasPour.getText();

                        instruction = new Pour((InstructionComposee) select, de, a, pas);

                    } else {
                        //instruction = null; // Important, ne pas enlever
                        return;
                    }
                } else if (source == boutonEcrire) {
                    instruction = new Ecrire(texteLireEcrire.getText());
                } else if (source == boutonLire) {
                    String variable = JOptionPane.showInputDialog("Quelle variable doit être affectée ?", instruction);
                    instruction = new Lire(variable, texteLireEcrire.getText());
                }
                if (instruction != null) {
                    if (select.autorisationAjout()) {
                        PanneauPrincipal.this.frameParente.getProgramme().getArbreProgramme().insertNodeInto(instruction, select, select.getChildCount());
                    } else {
                        Instruction parent = (Instruction) select.getParent();
                        if (parent != null && parent.autorisationAjout()) {
                            PanneauPrincipal.this.frameParente.getProgramme().getArbreProgramme().insertNodeInto(instruction, parent, parent.getIndex(select));
                        }
                    }
                    JTree arbre = PanneauPrincipal.this.frameParente.getArbre();
                    for (int i = 0; i < arbre.getRowCount(); i++) {
                        arbre.expandRow(i);
                    }
                    //instruction.setParent(select);
                }
            }
            //}
        };

        boutonAvance.setText("Avance");

        comboExpression.addItem(new DevantMur());
        comboExpression.addItem(new PasDevantMur());
        comboExpression.addItem(new SurMarque());
        comboExpression.addItem(new PasSurMarque());
        comboExpression.addItem(new DevantMarque());
        comboExpression.addItem(new PasDevantMarque());
        comboExpression.addItem(new SurMinerai());
        comboExpression.addItem(new PasSurMinerai());
        comboExpression.addItem(new PasDevantMurOuRobot());
        comboExpression.addItem(new PasDevantRobot());
        comboExpression.addItem(new DevantRobot());
        comboExpression.addItem(new DevantRobotOuMur());

        parseur = new ParseurExprBool(frameParente.getRobot(), getListeTests());

        texteExprBool.setBorder(BorderFactory.createTitledBorder("Expression booléene complexe"));
        texteExprBool.addCaretListener(new CaretListener() {

            @Override
            public void caretUpdate(CaretEvent e) {
                JTextArea source = (JTextArea) e.getSource();

                exprBoolComplexe = parseur.compile(source.getText(), PanneauPrincipal.this.frameParente.getRobot());
                if (exprBoolComplexe == null) {
                    source.setForeground(Color.RED);
                } else {
                    source.setForeground(Color.GREEN.darker());
                }

            }
        });

        JScrollPane vueDArbre = new JScrollPane(frameParente.getArbre());
        JPanel panneauArbre = new JPanel(new BorderLayout());
        panneauArbre.setPreferredSize(new Dimension(300, 400));
        panneauArbre.add(vueDArbre, "Center");

        JPanel panneauCommande = new JPanel();

        panneauCommande.add(panneauProg);

        setLayout(new BorderLayout());
        Dimension tailleMini = new Dimension(0, 0);
        panneauCommande.setMinimumSize(tailleMini);
        panneauArbre.setMinimumSize(tailleMini);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panneauCommande, panneauArbre);
        splitPane.setDividerLocation(450);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, "Center");

        frameParente.getArbre().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.isControlDown() && ke.getKeyCode() == 'C') {
                    boutonCopie.doClick();
                } else if (ke.isControlDown() && ke.getKeyCode() == 'X') {
                    boutonCoupe.doClick();
                } else if (ke.isControlDown() && ke.getKeyCode() == 'V') {
                    boutonColle.doClick();
                } else if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
                    boutonSupprime.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });

        boutonInitialise.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PanneauPrincipal.this.frameParente.montreDialInit();
                FramePrincipale.setROBOTACTIF(1);
            }
        });

        boutonAvance.addActionListener(ajoutInstruction);
        boutonTourne.addActionListener(ajoutInstruction);
        boutonMarque.addActionListener(ajoutInstruction);
        boutonEfface.addActionListener(ajoutInstruction);
        boutonSi.addActionListener(ajoutInstruction);
        boutonTantQue.addActionListener(ajoutInstruction);
        boutonBloc.addActionListener(ajoutInstruction);
        boutonPour.addActionListener(ajoutInstruction);
        boutonAjoutProcedure.addActionListener(ajoutInstruction);
        boutonAppelProcedure.addActionListener(ajoutInstruction);
        boutonEcrire.addActionListener(ajoutInstruction);
        boutonLire.addActionListener(ajoutInstruction);

        boutonSupprime.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                TreePath selection[] = PanneauPrincipal.this.frameParente.getArbre().getSelectionPaths();
                if (selection != null) {
                    for (TreePath currentSelection : selection) {
                        Instruction currentNode = (Instruction) (currentSelection.getLastPathComponent());
                        Instruction parent = (Instruction) (currentNode.getParent());
                        if (parent == null) {
                            while (currentNode.getChildCount() > 1) {
                                comboAppelProcedure.removeItem((Instruction) currentNode.getFirstChild());
                                //PanneauPrincipal.this.frameParente.getProgramme().getArbreProgramme().removeNodeFromParent((MutableTreeNode) currentNode.getFirstChild());
                                PanneauPrincipal.this.frameParente.getProgramme().retraitProcedure(currentNode.getFirstChild().toString());
                            }
                            currentNode = (Instruction) currentNode.getFirstChild();
                            if (currentNode != null) {
                                while (currentNode.getChildCount() > 0) {
                                    PanneauPrincipal.this.frameParente.getProgramme().getArbreProgramme().removeNodeFromParent((MutableTreeNode) currentNode.getFirstChild());
                                }
                            }

                        } else if (parent.getParent() == null) {
                            if (currentNode.getChildCount() > 0) {
                                while (currentNode.getChildCount() > 0) {
                                    PanneauPrincipal.this.frameParente.getProgramme().getArbreProgramme().removeNodeFromParent((MutableTreeNode) currentNode.getFirstChild());
                                }

                            } else {
                                comboAppelProcedure.removeItem(currentNode);
                                PanneauPrincipal.this.frameParente.getProgramme().retraitProcedure(currentNode.toString());
                            }
                            //if (currentNode.getNom().equalsIgnoreCase("procédure principale"))
                        } else {
                            NoeudProgramme suivant = currentNode.getNextNode();
                            if (suivant == null) {
                                suivant = currentNode.getPreviousNode();
                            }
                            PanneauPrincipal.this.frameParente.getProgramme().getArbreProgramme().removeNodeFromParent(currentNode);
                            TreePath chemin = new TreePath(suivant);
                            PanneauPrincipal.this.frameParente.getArbre().setSelectionPath(chemin);
                            PanneauPrincipal.this.frameParente.getProgramme().getArbreProgramme().nodeChanged(suivant);
                        }
                    }
                }
            }
        });
        boutonCoupe.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boutonCopie.doClick();
                boutonSupprime.doClick();
            }
        });
        boutonCopie.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath[] tp = PanneauPrincipal.this.frameParente.getArbre().getSelectionPaths();

                Deque<TreePath> treePaths = new ArrayDeque<TreePath>();
                for (int i = tp.length - 1; i > 0; i--) {
                    if (!tp[i - 1].isDescendant(tp[i])) {
                        //v.add(tp[i]);
                        treePaths.addFirst(tp[i]);

                    }
                }

                treePaths.addFirst(tp[0]);

                if (treePaths != null) {
                    brancheCopiee = new ByteArrayOutputStream[treePaths.size()];
                    int i = 0;
                    for (TreePath treePath : treePaths) {
                        Instruction select = (Instruction) treePath.getLastPathComponent();
                        brancheCopiee[i] = new ByteArrayOutputStream();
                        try {
                            ObjectOutputStream fout = new ObjectOutputStream(brancheCopiee[i]);
                            fout.writeObject(select);

                        } catch (IOException ex) {
                            Logger.getLogger(PanneauPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        i++;
                    }
                }
            }
        });
        boutonColle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath treePath = PanneauPrincipal.this.frameParente.getArbre().getSelectionPath();
                if (treePath != null && brancheCopiee != null) {
                    Instruction select = (Instruction) treePath.getLastPathComponent();
                    Instruction parent;
                    int positionInsertion;
                    if (select.autorisationAjout()) {
                        parent = select;
                        positionInsertion = parent.getChildCount();
                    } else {
                        parent = (Instruction) select.getParent();
                        positionInsertion = parent.getIndex(select);
                    }
                    // if (select.autorisationAjout()) {
                    Instruction instruction = null;
                    for (ByteArrayOutputStream copiee : brancheCopiee) {
                        try {
                            ObjectInputStream fin = new ObjectInputStream(new ByteArrayInputStream(copiee.toByteArray()));
                            instruction = (Instruction) fin.readObject();

                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(PanneauPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(PanneauPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //Programme.changerDeRobot(instruction, PanneauPrincipal.this.frameParente.getRobot());
                        PanneauPrincipal.this.frameParente.getProgramme().getArbreProgramme().insertNodeInto(instruction, parent, positionInsertion++);
                    }
                    // }
                }
            }
        });

    }

    private void placementComposant(Component c, int x, int y, int w, int h, int f) {
        placementProg.gridx = x;
        placementProg.gridy = y;
        placementProg.gridheight = h;
        placementProg.gridwidth = w;
        placementProg.fill = f;

        panneauProg.add(c, placementProg);

    }

    private void placementComposant(Component k, int l, int c) {
        placementComposant(k, c, l, 1, 1, GridBagConstraints.BOTH);
    }

    private void placementComposant(Component k, int l, int c, int w) {
        placementComposant(k, c, l, w, 1, GridBagConstraints.BOTH);
    }

    /*
     * private void placementComposant(Component k, int l, int c, int w, int h)
     * { placementComposant(k, c, l, w, h, GridBagConstraints.BOTH); }
     */
    public void setAppelProcedure(Collection<Instruction> c) {
        comboAppelProcedure.removeAllItems();
        for (Instruction i : c) {
            comboAppelProcedure.addItem(i);
        }
    }

    public void figerNiveau(int niveau) {

        for (int i = niveau; i < menuNiveau.getItemCount(); i++) {
            menuNiveau.getItem(i).setEnabled(false);
        }
    }

    private void ajouterNomsAuMenu(JMenu demo, String nom) {
        // C'est le mieux que j'ai trouvé pour le moment.
        // 

        if (frameParente instanceof FramePrincipale) {
            try {
                XStream xstream = new XStream(new DomDriver());

                Programme programme = null;
                
                programme = (Programme) xstream.fromXML(PanneauPrincipal.class.getResourceAsStream("/sources/" + nom + ".rob"));

                //programme = (Programme) xstream.fromXML(getClass().getResourceAsStream("sources/" + nom + ".rob"));
                //os = null;
                //File file = new File(getClass().getResource("sources/" + nom + ".rob.obj"));
                // getClass().getr
                try ( //os = new ObjectOutputStream(new FileOutputStream(new File(getClass().getResource("sources/" + nom + ".rob.obj").toURI())));
                        //os = new ObjectOutputStream(getClass().getResourceAsStream("sources/" + nom + ".rob.obj"));
                         ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("resources/sources/" + nom + ".rob.obj"))) {
                    os.writeObject(programme);
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex);
            } catch (IOException ex) {
                System.err.println(ex);
            } catch (NullPointerException ex) {
                System.err.println(ex);
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }

        demo.add(getMenuItem(nom));

    }
}
