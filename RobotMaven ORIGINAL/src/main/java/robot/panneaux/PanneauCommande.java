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

import interfaces.Detachable;
import robot.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Maillot
 */
public class PanneauCommande extends JPanel {

    private static final long serialVersionUID = 1L;

    private Detachable frameParente;

    private JButton avance;
    private JButton efface;
    private JButton marque;
    private JSlider sliderDuréeAction;
    private JButton stopLeRobot;
    private JButton tourne;
    private ImageIcon image;

    private JLabel labelSelectionDuRobot;
    private JComboBox comboRobotSelectionne;


    public PanneauCommande(Detachable frameParente) {
        initComponents();
        this.frameParente = frameParente;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        sliderDuréeAction = new JSlider();
        avance = new JButton();
        tourne = new JButton();
        stopLeRobot = new JButton();
        marque = new JButton();
        efface = new JButton();
        image = new ImageIcon();

        sliderDuréeAction.setMajorTickSpacing(50);
        sliderDuréeAction.setMaximum(300);
        sliderDuréeAction.setMinorTickSpacing(10);
        sliderDuréeAction.setPaintLabels(true);
        sliderDuréeAction.setPaintTicks(true);
        sliderDuréeAction.setSnapToTicks(true);
        sliderDuréeAction.setToolTipText("<html>\nDéfinit la durée de référence pour les déplacements du robot.<br>\nEn l'occurence, il s'agit du temps en millisecondes nécessaire au robot pour se déplacer d'une case.\n</html>");
        sliderDuréeAction.setValue(200);
        sliderDuréeAction.setBorder(BorderFactory.createTitledBorder("Vitesse du robot"));
        sliderDuréeAction.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                sliderDuréeActionStateChanged(evt);
            }
        });

        avance.setText("Avance");
        avance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                avanceActionPerformed(evt);
            }
        });

        tourne.setText("Tourne");
        tourne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tourneActionPerformed(evt);
            }
        });

        stopLeRobot.setText("Stoppe le robot");
        stopLeRobot.setToolTipText("<html>\nPour arrêter le robot<br>\nEn case d'erreur.\n</html>");
        stopLeRobot.setVisible(false);
        stopLeRobot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                stopLeRobotActionPerformed(evt);
            }
        });

        marque.setText("Marque");
        marque.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                marqueActionPerformed(evt);
            }
        });

        efface.setText("Efface");
        efface.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                effaceActionPerformed(evt);
            }
        });

        initialiseSelectionRobot();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(stopLeRobot)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(tourne)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(efface))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(avance)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(marque)))
                                .addGap(44, 44, 44)
                                .addComponent(sliderDuréeAction, GroupLayout.PREFERRED_SIZE, 528, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()
                                .addGap(44, 44, 44)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(labelSelectionDuRobot)
                                        .addComponent(comboRobotSelectionne,GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))//Ajouté par Sélim
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(stopLeRobot)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(avance, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(marque, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(tourne, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(efface, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(sliderDuréeAction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(labelSelectionDuRobot)
                                            .addComponent(comboRobotSelectionne,GroupLayout.PREFERRED_SIZE, 30,GroupLayout.PREFERRED_SIZE)))//Ajouté par Sélim
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void tourneActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            frameParente.getTerrain().getRobotSelectionne().tourne();
        } catch (InterruptedException | TropDePas ex) {
            Logger.getLogger(PanneauCommande.class.getName()).log(Level.SEVERE, null, ex);
            frameParente.getTerrain().getRobotSelectionne().stop();
        }
    }

    private void sliderDuréeActionStateChanged(javax.swing.event.ChangeEvent evt) {
        frameParente.getTerrain().getRobotSelectionne().dureeReference = ((JSlider) evt.getSource()).getValue();
    }

    private void stopLeRobotActionPerformed(java.awt.event.ActionEvent evt) {
        frameParente.getTerrain().getRobotSelectionne().stop();
    }

    private void marqueActionPerformed(java.awt.event.ActionEvent evt) {
        frameParente.getTerrain().getRobotSelectionne().poserUneMarque();
    }

    private void effaceActionPerformed(java.awt.event.ActionEvent evt) {
        frameParente.getTerrain().getRobotSelectionne().enleverUneMarque();
    }

    private void avanceActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            frameParente.getTerrain().getRobotSelectionne().avance();
        } catch (DansLeMur | TropDePas ex) {
            Logger.getLogger(PanneauCommande.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void comboRobotSelectionneDefinieActionPerformed(ActionEvent evt) {
        frameParente.getTerrain().changeDeRobot(comboRobotSelectionne.getSelectedIndex() + 1);
    }// Ajouté par Sélim

    private void initialiseSelectionRobot()
    {
        labelSelectionDuRobot = new JLabel();
        labelSelectionDuRobot.setText("Sélection du robot");

        String s[] = new String[frameParente.getTerrain().getNBROBOTS()];

        for (int i = 0; i < frameParente.getTerrain().getNBROBOTS(); ++i)
            s[i] = "Robot " + (i + 1);

        comboRobotSelectionne = new JComboBox();
        comboRobotSelectionne.setModel(new DefaultComboBoxModel(s));
        comboRobotSelectionne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboRobotSelectionneDefinieActionPerformed(e);
            }
        });
    }

    public JComboBox getComboRobotSelectionne()
    {
        return comboRobotSelectionne;
    }
}
