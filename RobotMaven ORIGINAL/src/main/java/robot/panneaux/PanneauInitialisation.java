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
import terrain.Terrain;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
        nombrePasDefinie.setSelected(initialisation.isPresenceJauge());
        jSliderNombrePas.setEnabled(initialisation.isPresenceJauge());
        jSliderNombrePas.setValue(initialisation.getJauge());
        textNombrePasExact.setEnabled(initialisation.isPresenceTextArea());
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

        textNombrePasExact = new JTextArea();
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
        initialiseJauge();
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
                                                                .addComponent(textNombrePasExact, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE))
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
        FramePrincipale.setPositionRobot(comboPositionRobot.getSelectedIndex() - 1, FramePrincipale.getROBOTACTIF());
        FramePrincipale.setOrientationRobot(comboOrientationRobot.getSelectedIndex() - 1, FramePrincipale.getROBOTACTIF());
    }// Ajouté par Sélim



    public static void selectionneRobot(int id) {
        FramePrincipale.setROBOTACTIF(id);
    }//Ajouté par Sélim

    private void initialiseSelectionRobot()
    {
        labelSelectionDuRobot.setText("Sélection du robot");

        String s[] = new String[FramePrincipale.getNbRobots()];

        for (int i = 0; i < FramePrincipale.getNbRobots(); ++i)
            s[i] = "Robot " + (i + 1);


        comboRobotSelectionne.setModel(new DefaultComboBoxModel(s));
        comboRobotSelectionne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboRobotSelectionneDefinieActionPerformed(e);
            }
        });
    }// Modifié par Sélim

    private void initialiseOrientationRobot()
    {
        labelOrientationRobot.setText("Orientation du robot");

        comboOrientationRobot.setModel(new DefaultComboBoxModel(new String[]{"Quelconque", "Vers le nord", "Vers l'est", "Vers le  sud", "Vers l'ouest"}));
        comboOrientationRobot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboOrientationRobotItemStateChanged(e);
            }
        });
    }// Modifié par Sélim

    private void initialisePositionRobot()
    {
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

    private void initialisePresenceMinerai()
    {
        jCheckBoxMinerai.setText("Présence de minerai");
        jCheckBoxMinerai.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jCheckBoxMineraiStateChanged(evt);
            }
        });
    }// Modifié par Sélim

    private void initialisePositionMinerai()
    {
        labelPositionMinerai.setText("Position du minerai");

        comboPositionMinerai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N'importe où", "Contre un mur", "Dans un coin", "Pas contre un mur", "Pas dans un coin", "Contre le mur nord", "Contre le mur est", "Contre le mur sud", "Contre le mur ouest", "Dans le coin nord-est", "Dans le coin sud-est", "Dans le coin sud-ouest", "Dans le coin nord-ouest" }));
        comboPositionMinerai.setEnabled(false);
        comboPositionMinerai.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                comboPositionMineraiItemStateChanged(evt);
            }
        });
    }// Modifié par Sélim

    private void initialiseSliderHauteur()
    {
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

    private void initialiseSliderLargeur()
    {
        jSliderLargeur.setMajorTickSpacing(5);
        jSliderLargeur.setMinimum(Terrain.minX);
        jSliderLargeur.setMinorTickSpacing(1);
        jSliderLargeur.setPaintLabels(true);
        jSliderLargeur.setPaintTicks(true);
        jSliderLargeur.setSnapToTicks(true);
        jSliderLargeur.setToolTipText(jSliderLargeur.getValue() + "");
        jSliderLargeur.setValue(10);
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

    private void initialiseJauge() {
        //debut ajout
        jSliderNombrePas.setMajorTickSpacing(5);
        jSliderNombrePas.setMinimum(5);
        jSliderNombrePas.setMinorTickSpacing(1);
        jSliderNombrePas.setPaintLabels(true);
        jSliderNombrePas.setPaintTicks(true);
        jSliderNombrePas.setSnapToTicks(true);
        jSliderNombrePas.setToolTipText(jSliderNombrePas.getValue() + "");
        jSliderNombrePas.setValue(10);
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
        textNombrePasExact.setRows(1);
        textNombrePasExact.setEditable(false);
        textNombrePasExact.setBorder(javax.swing.BorderFactory.createBevelBorder(1));
        textNombrePasExact.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textNombrePasExactStateChanged(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textNombrePasExactStateChanged(e);

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textNombrePasExactStateChanged(e);
            }
        });
        //fin ajout
    }




    //debut ajout
    private void jSliderNombrePasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderJaugeStateChanged
        if (changementInterne) return;
        if(!testChiffre) {
            initialisation.setJauge(jSliderNombrePas.getValue());
            textNombrePasExact.setText(jSliderNombrePas.getValue() + "");
        }else if(testChiffre){
            testChiffre= false;
        }
    }//GEN-LAST:event_jSliderJaugeStateChanged

    private void nombrePasDefinieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jaugeDefinieActionPerformed
        if (changementInterne) return;
        jSliderNombrePas.setEnabled(nombrePasDefinie.isSelected());
        textNombrePasExact.setEnabled(nombrePasDefinie.isSelected());
        textNombrePasExact.setEditable(nombrePasDefinie.isSelected());
        initialisation.setPresenceJauge(nombrePasDefinie.isSelected());
        initialisation.setJauge(jSliderNombrePas.getValue());


    }//GEN-LAST:event_jaugeDefinieActionPerformed


    private void textNombrePasExactStateChanged(DocumentEvent evt) {//GEN-FIRST:event_jSliderJaugeStateChanged
        if (changementInterne) return;
        int nombrePas = Integer.parseInt(textNombrePasExact.getText());

        if (!testChiffre) {

            if (nombrePas > 0 && nombrePas <= 100) {
                initialisation.setJauge(nombrePas);
                initialisation.setTextArea(textNombrePasExact.getText());
                jSliderNombrePas.setValue(nombrePas);

            } else if (nombrePas > 100) {
                initialisation.setJauge(nombrePas);
                initialisation.setTextArea(textNombrePasExact.getText());
                jSliderNombrePas.setValue(100);
            }
            testChiffre= true;
        }else if (testChiffre){
            testChiffre= false;
        }

    }//GEN-LAST:event_jSliderJaugeStateChanged

    // fin ajout

    //debut ajout
    private JSlider jSliderNombrePas;
    private JCheckBox nombrePasDefinie;
    private JTextArea textNombrePasExact = new JTextArea();
    private boolean testChiffre = false;

    // End of variables declaration//GEN-END:variables

    public Initialisation getInitialisation() {
        return initialisation;
    }
}
