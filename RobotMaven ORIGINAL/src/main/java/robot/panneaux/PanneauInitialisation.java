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

/*
 * NewJPanel.java
 *
 * Created on 2 mai 2009, 19:20:49
 */


package robot.panneaux;

import robot.*;
import robot.Robot;
import terrain.Terrain;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Yvan
 */
public class PanneauInitialisation extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Initialisation initialisation;
    private boolean changementInterne = false;

    private JComboBox comboOrientationRobot;
    private JComboBox comboPositionMinerai;
    private JComboBox comboPositionRobot;
    private JCheckBox hauteurDefinie;
    private JCheckBox jCheckBoxMinerai;
    private JSlider jSliderHauteur;
    private JSlider jSliderLargeur;
    private JLabel labelOrientationRobot;
    private JLabel labelPositionMinerai;
    private JLabel labelPositionRobot;
    private JCheckBox largeurDefinie;

    //debut ajout
    private JSlider jSliderNombrePas;
    private static JCheckBox nombrePasDefinie;
    private JTextField textNombrePasExact = new JTextField();
    private boolean synchroJaugeTexte = false;
    private JLabel textErreur = new JLabel();
    // fin ajout

    private JComboBox comboRobotSelectionne;
    private JLabel labelSelectionDuRobot;
    //Ajouté par Sélim

    /**
     * Creates new form NewJPanel
     */
    public PanneauInitialisation() {
        initialisation = new Initialisation();
        initComponents();
    }

    public PanneauInitialisation(Initialisation initialisation) {
        this.initialisation = initialisation;
        initComponents();

        changementInterne = true;

        jCheckBoxMinerai.setSelected(initialisation.isPresenceMinerai());
        comboOrientationRobot.setSelectedIndex(FramePrincipale.getOrientationRobotActif() + 1);
        comboPositionMinerai.setSelectedIndex(initialisation.getPositionMinerai() + 1);
        comboPositionRobot.setSelectedIndex(FramePrincipale.getPositionRobotActif() + 1);
        largeurDefinie.setSelected(initialisation.isPresenceLargeur());
        jSliderLargeur.setEnabled(initialisation.isPresenceLargeur());
        jSliderLargeur.setValue(initialisation.getLargeur());
        hauteurDefinie.setSelected(initialisation.isPresenceHauteur());
        jSliderHauteur.setEnabled(initialisation.isPresenceHauteur());
        jSliderHauteur.setValue(initialisation.getHauteur());

        //debut ajout
        textNombrePasExact.setEnabled(false);
        nombrePasDefinie.setSelected(false);
        jSliderNombrePas.setEnabled(false);
        jSliderNombrePas.setValue(5);
        //fin ajout

        changementInterne = false;
    }

    private void initComponents() {

        comboPositionRobot = new JComboBox();
        labelPositionRobot = new JLabel();
        comboOrientationRobot = new JComboBox();
        labelOrientationRobot = new JLabel();
        jCheckBoxMinerai = new JCheckBox();
        labelPositionMinerai = new JLabel();
        comboPositionMinerai = new JComboBox();
        jSliderHauteur = new JSlider();
        jSliderLargeur = new JSlider();
        hauteurDefinie = new JCheckBox();
        largeurDefinie = new JCheckBox();

        //debut ajout
        jSliderNombrePas = new JSlider();
        nombrePasDefinie = new JCheckBox();

        textNombrePasExact = new JTextField();
        // fin ajout

        labelSelectionDuRobot = new JLabel();
        comboRobotSelectionne = new JComboBox();
        //Ajouté par Sélim

        setBorder(BorderFactory.createTitledBorder("Initialisation"));

        initialisePositionRobot();

        initialiseOrientationRobot();

        initialisePresenceMinerai();

        initialisePositionMinerai();

        initialiseSelectionRobot();

        initialiseSliderHauteur();

        initialiseSliderLargeur();

        //debut ajout
        initialiseNombrePas();
        //fin ajout

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelPositionRobot)
                                                        .addComponent(labelOrientationRobot)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                                        .addComponent(comboOrientationRobot, GroupLayout.Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(comboPositionRobot, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(labelPositionMinerai)
                                                                        .addComponent(jCheckBoxMinerai)
                                                                        .addComponent(comboPositionMinerai, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(labelSelectionDuRobot)// Ajouté par Sélim
                                                                        .addComponent(comboRobotSelectionne))// Ajouté par Sélim
                                                                .addContainerGap())
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(largeurDefinie)
                                                                        .addComponent(hauteurDefinie)
                                                                        //debut ajout
                                                                        .addComponent(nombrePasDefinie))
                                                                //fin ajout
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jSliderHauteur, GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                                                                        .addComponent(jSliderLargeur, GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)

                                                                        .addComponent(jSliderNombrePas, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE) //ajout Ambre
                                                                        .addComponent(textNombrePasExact, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)//ajout Ambre
                                                                        .addComponent(textErreur))//ajout Ambre
                                                                .addContainerGap()))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)))
                                )));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(labelOrientationRobot)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboOrientationRobot, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBoxMinerai))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelPositionRobot)
                                        .addComponent(labelPositionMinerai)
                                        .addComponent(labelSelectionDuRobot))// Ajouté par Sélim
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(comboPositionRobot, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboPositionMinerai, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboRobotSelectionne, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))// Ajouté par Sélim
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(hauteurDefinie)
                                        .addComponent(jSliderHauteur, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jSliderLargeur, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(largeurDefinie))
                                //debut ajout
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSliderNombrePas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nombrePasDefinie))

                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textNombrePasExact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                )
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textErreur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                )

                                //fin ajout
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void jCheckBoxMineraiStateChanged(ChangeEvent evt) {
        if (changementInterne) return;
        if (jCheckBoxMinerai.isSelected())
            comboPositionMinerai.setEnabled(true);
        else
            comboPositionMinerai.setEnabled(false);
        initialisation.setPrésenceMinerai(jCheckBoxMinerai.isSelected());
    }

    private void comboOrientationRobotItemStateChanged(ActionEvent evt) {
        if (changementInterne) return;
        FramePrincipale.setOrientationRobot(comboOrientationRobot.getSelectedIndex() - 1, FramePrincipale.getROBOTACTIF());
    }

    private void comboPositionRobotItemStateChanged(ActionEvent evt) {
        if (changementInterne) return;
        FramePrincipale.setPositionRobot(comboPositionRobot.getSelectedIndex() - 1, FramePrincipale.getROBOTACTIF());
    }

    private void comboPositionMineraiItemStateChanged(ItemEvent evt) {
        if (changementInterne) return;
        initialisation.setPositionMinerai(comboPositionMinerai.getSelectedIndex() - 1);
    }

    private void largeurDefinieActionPerformed(ActionEvent evt) {
        if (changementInterne) return;
        jSliderLargeur.setEnabled(largeurDefinie.isSelected());
        initialisation.setPresenceLargeur(largeurDefinie.isSelected());
        initialisation.setLargeur(jSliderLargeur.getValue());
    }

    private void hauteurDefinieStateChanged(ChangeEvent evt) {
        if (changementInterne) return;
        jSliderHauteur.setEnabled(hauteurDefinie.isSelected());
        initialisation.setPresenceHauteur(hauteurDefinie.isSelected());
        initialisation.setHauteur(jSliderHauteur.getValue());
    }

    private void jSliderHauteurStateChanged(ChangeEvent evt) {
        if (changementInterne) return;
        initialisation.setHauteur(jSliderHauteur.getValue());
    }

    private void jSliderLargeurStateChanged(ChangeEvent evt) {
        if (changementInterne) return;
        initialisation.setLargeur(jSliderLargeur.getValue());
    }

    private void comboRobotSelectionneDefinieActionPerformed(ActionEvent evt) {
        if (changementInterne) return;
        selectionneRobot(comboRobotSelectionne.getSelectedIndex() + 1);
        FramePrincipale.setOrientationRobot(comboOrientationRobot.getSelectedIndex() - 1, comboRobotSelectionne.getSelectedIndex() + 1);
        FramePrincipale.setPositionRobot(comboPositionRobot.getSelectedIndex() - 1, comboRobotSelectionne.getSelectedIndex() + 1);
        if (nombrePasDefinie.isSelected())
            FramePrincipale.setNombreDePas(FramePrincipale.getROBOTACTIF(), Integer.parseInt(textNombrePasExact.getText()));
        else FramePrincipale.setNombreDePas(FramePrincipale.getROBOTACTIF(), -2);
    }// Ajouté par Sélim

    public static void selectionneRobot(int id) {
        FramePrincipale.setRobotActif(id);
    }//Ajouté par Sélim

    private void initialiseSelectionRobot() {
        labelSelectionDuRobot.setText("Sélection du robot");

        String s[] = new String[FramePrincipale.getNbRobotSurTerrain()];

        for (int i = 0; i < FramePrincipale.getNbRobotSurTerrain(); ++i)
            s[i] = "Robot " + (i + 1);


        comboRobotSelectionne.setModel(new DefaultComboBoxModel(s));
        comboRobotSelectionne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboRobotSelectionneDefinieActionPerformed(e);
            }
        });
    }// Modifié par Sélim

    private void initialiseOrientationRobot() {
        labelOrientationRobot.setText("Orientation du robot");

        comboOrientationRobot.setModel(new DefaultComboBoxModel(new String[]{"Quelconque", "Vers le nord", "Vers l'est", "Vers le  sud", "Vers l'ouest"}));
        comboOrientationRobot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboOrientationRobotItemStateChanged(e);
            }
        });
    }// Modifié par Sélim

    private void initialisePositionRobot() {
        labelPositionRobot.setText("Position du robot");

        if (FramePrincipale.getNbRobots() == 1) {
            comboPositionRobot.setModel(new DefaultComboBoxModel(new String[]{"N'importe où", "Contre un mur", "Dans un coin", "Pas contre un mur", "Pas dans un coin", "Contre le mur nord", "Contre le mur est", "Contre le mur sud", "Contre le mur ouest", "Dans le coin nord-est", "Dans le coin sud-est", "Dans le coin sud-ouest", "Dans le coin nord-ouest"}));
        } else {
            comboPositionRobot.setModel(new DefaultComboBoxModel(new String[]{"N'importe où", "Contre un mur", "Dans un coin", "Pas contre un mur", "Pas dans un coin", "Contre le mur nord", "Contre le mur est", "Contre le mur sud", "Contre le mur ouest"}));
        } //Ajouté par Sélim

        comboPositionRobot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboPositionRobotItemStateChanged(e);
            }
        });
    }// Modifié par Sélim

    private void initialisePresenceMinerai() {
        jCheckBoxMinerai.setText("Présence de minerai");
        jCheckBoxMinerai.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jCheckBoxMineraiStateChanged(evt);
            }
        });
    }// Modifié par Sélim

    private void initialisePositionMinerai() {
        labelPositionMinerai.setText("Position du minerai");

        comboPositionMinerai.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"N'importe où", "Contre un mur", "Dans un coin", "Pas contre un mur", "Pas dans un coin", "Contre le mur nord", "Contre le mur est", "Contre le mur sud", "Contre le mur ouest", "Dans le coin nord-est", "Dans le coin sud-est", "Dans le coin sud-ouest", "Dans le coin nord-ouest"}));
        comboPositionMinerai.setEnabled(false);
        comboPositionMinerai.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                comboPositionMineraiItemStateChanged(evt);
            }
        });
    }// Modifié par Sélim

    private void initialiseSliderHauteur() {
        jSliderHauteur.setMajorTickSpacing(5);
        jSliderHauteur.setMinimum(Terrain.minY);
        jSliderHauteur.setMinorTickSpacing(1);
        jSliderHauteur.setPaintLabels(true);
        jSliderHauteur.setPaintTicks(true);
        jSliderHauteur.setSnapToTicks(true);
        jSliderHauteur.setToolTipText(jSliderHauteur.getValue() + "");
        jSliderHauteur.setValue(10);
        jSliderHauteur.setBorder(BorderFactory.createTitledBorder("Hauteur"));
        jSliderHauteur.setEnabled(false);
        jSliderHauteur.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSliderHauteurStateChanged(evt);
            }
        });

        hauteurDefinie.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                hauteurDefinieStateChanged(evt);
            }
        });
    }// Modifié par Sélim

    private void initialiseSliderLargeur() {
        jSliderLargeur.setMajorTickSpacing(5);
        jSliderLargeur.setMinimum(Terrain.minX);
        jSliderLargeur.setMinorTickSpacing(1);
        jSliderLargeur.setPaintLabels(true);
        jSliderLargeur.setPaintTicks(true);
        jSliderLargeur.setSnapToTicks(true);
        jSliderLargeur.setToolTipText(jSliderLargeur.getValue() + "");
        jSliderLargeur.setValue(-1);
        jSliderLargeur.setBorder(BorderFactory.createTitledBorder("Largeur"));
        jSliderLargeur.setEnabled(false);
        jSliderLargeur.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSliderLargeurStateChanged(evt);
            }
        });

        largeurDefinie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                largeurDefinieActionPerformed(evt);
            }
        });
    }// Modifié par Sélim

    private void initialiseNombrePas() {
        //debut ajout
        jSliderNombrePas.setMajorTickSpacing(5);
        jSliderNombrePas.setMinimum(5);
        jSliderNombrePas.setMinorTickSpacing(1);
        jSliderNombrePas.setPaintLabels(true);
        jSliderNombrePas.setPaintTicks(true);
        jSliderNombrePas.setSnapToTicks(true);
        jSliderNombrePas.setToolTipText(jSliderNombrePas.getValue() + "");
        jSliderNombrePas.setValue(5);
        jSliderNombrePas.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre de Pas"));
        jSliderNombrePas.setEnabled(false);
        jSliderNombrePas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderNombrePasStateChanged(evt);
            }
        });

        nombrePasDefinie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombrePasDefinieActionPerformed(evt);
            }
        });

        textNombrePasExact.setColumns(1);
        textNombrePasExact.setEditable(false);
        //textNombrePasExact.setText("5");
        //textNombrePasExact.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        textNombrePasExact.setBorder(javax.swing.BorderFactory.createBevelBorder(2,Color.BLUE,Color.DARK_GRAY));
        textErreur.setForeground(Color.RED);
        textNombrePasExact.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(Character.isAlphabetic(c)){
                    textNombrePasExact.setEditable(false);
                    textErreur.setText("Veuillez rentrer que des chiffres !");

                }else{
                    textNombrePasExact.setEditable(true);
                    textErreur.setText("");
                }
            }
        });
        textNombrePasExact.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {


                    textNombrePasStateChanged(e);

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //textNombrePasExactStateChanged(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                    textNombrePasStateChanged(e);

            }
        });
        //fin ajout
    }

    //debut ajout
    private void jSliderNombrePasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderJaugeStateChanged
        if (changementInterne) return;
        if (synchroJaugeTexte) {
            synchroJaugeTexte = false;

        }else {
            textNombrePasExact.setText(jSliderNombrePas.getValue() + "");
            FramePrincipale.setNombreDePas(FramePrincipale.getROBOTACTIF(), Integer.parseInt(textNombrePasExact.getText()));
            synchroJaugeTexte=true;
        }
    }//GEN-LAST:event_jSliderJaugeStateChanged

    private void nombrePasDefinieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jaugeDefinieActionPerformed
        if (changementInterne) return;
        jSliderNombrePas.setEnabled(nombrePasDefinie.isSelected());
        textNombrePasExact.setEnabled(nombrePasDefinie.isSelected());
        textNombrePasExact.setEditable(nombrePasDefinie.isSelected());
        synchroJaugeTexte = nombrePasDefinie.isSelected();


        if(!synchroJaugeTexte) {
            FramePrincipale.setNombreDePas(FramePrincipale.getROBOTACTIF(), -2);
        }

    }//GEN-LAST:event_jaugeDefinieActionPerformed

    private void textNombrePasStateChanged(DocumentEvent evt) {//GEN-FIRST:event_jSliderJaugeStateChanged

        if (changementInterne) return;
        Runnable doTextNombrePas = new Runnable() {
            @Override
            public void run() {
                int nombrePas;

                nombrePas = Integer.parseInt(textNombrePasExact.getText());

                if (nombrePas > 5 && nombrePas < 100) {
                    jSliderNombrePas.setValue(nombrePas);
                } else if (nombrePas >= 100) {
                    jSliderNombrePas.setValue(100);
                } else {
                    jSliderNombrePas.setValue(5);
                }

                FramePrincipale.setNombreDePas(FramePrincipale.getROBOTACTIF(), Integer.parseInt(textNombrePasExact.getText()));
            }
        };
       SwingUtilities.invokeLater(doTextNombrePas);



    }//GEN-LAST:event_jSliderJaugeStateChanged

    private void jTextFieldKeyPressed(KeyEvent evt){
        char c = evt.getKeyChar();
        if(Character.isLetter(c)){
            textNombrePasExact.setEditable(false);

        }else{
            textNombrePasExact.setEditable(true);
        }
    }
    // fin ajout

    public Initialisation getInitialisation() {
        return initialisation;
    }
}
