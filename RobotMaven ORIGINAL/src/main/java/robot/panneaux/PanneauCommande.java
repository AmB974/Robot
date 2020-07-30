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
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
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

    /**
     * Creates new form panneauCommande
     */
    public PanneauCommande(Detachable frameParente) {
        initComponents();
        this.frameParente = frameParente;
    }

    @SuppressWarnings("unchecked")
    //    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
    }// </editor-fold>//GEN-END:initComponents


    private void tourneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tourneActionPerformed
        try {
            frameParente.getRobot().tourne();
        } catch (InterruptedException | TropDePas ex) {
            Logger.getLogger(PanneauCommande.class.getName()).log(Level.SEVERE, null, ex);
            frameParente.getRobot().stop();
        }
    }//GEN-LAST:event_tourneActionPerformed

    private void sliderDuréeActionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderDuréeActionStateChanged
        frameParente.getRobot().dureeReference = ((JSlider) evt.getSource()).getValue();
    }//GEN-LAST:event_sliderDuréeActionStateChanged

    private void stopLeRobotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopLeRobotActionPerformed
        frameParente.getRobot().stop();
    }//GEN-LAST:event_stopLeRobotActionPerformed

    private void marqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marqueActionPerformed
        // TODO add your handling code here:
        frameParente.getRobot().poserUneMarque();
    }//GEN-LAST:event_marqueActionPerformed

    private void effaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_effaceActionPerformed
        // TODO add your handling code here:
        frameParente.getRobot().enleverUneMarque();
    }//GEN-LAST:event_effaceActionPerformed

    private void avanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avanceActionPerformed
        // TODO add your handling code here:
        try {
            PanneauCommande.this.frameParente.getRobot().avance();
        } catch (DansLeMur | TropDePas ex) {
            Logger.getLogger(PanneauCommande.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_avanceActionPerformed

    private void comboRobotSelectionneDefinieActionPerformed(ActionEvent evt) {
        frameParente.getRobot().setActif(false);
        frameParente.getRobot().imageSelonOrientation();

        System.out.println("Robot actif: "+ frameParente.getRobot().getID()+" Actif : "+ frameParente.getRobot().isActif());
        frameParente.setRobotActif(comboRobotSelectionne.getSelectedIndex() + 1);
        frameParente.getRobot().setActif(true);
        frameParente.getRobot().imageSelonOrientation();
        System.out.println("Robot actif: "+ frameParente.getRobot().getID()+" Actif : "+ frameParente.getRobot().isActif());


    }// Ajouté par Sélim

    private void initialiseSelectionRobot()
    {
        labelSelectionDuRobot = new JLabel();
        labelSelectionDuRobot.setText("Sélection du robot");

        String s[] = new String[FramePrincipale.getNbRobots()];

        for (int i = 0; i < FramePrincipale.getNbRobots(); ++i)
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton avance;
    private javax.swing.JButton efface;
    private javax.swing.JButton marque;
    private javax.swing.JSlider sliderDuréeAction;
    private javax.swing.JButton stopLeRobot;
    private javax.swing.JButton tourne;
    private javax.swing.ImageIcon image;
    // End of variables declaration//GEN-END:variables

    private JLabel labelSelectionDuRobot;
    private JComboBox comboRobotSelectionne;



}
