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
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author Yvan
 */
public class BoiteDeDialogueInit extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private PanneauInitialisation panneauInit;
    private Detachable environnement;
    private JButton valider = new JButton("initialisation");
    private JButton annuler = new JButton("annulation");
    
    private boolean reponse = false;
    public BoiteDeDialogueInit(Detachable environnement) {
        super((Dialog)null, "Initialisation");
        this.environnement = environnement;
        setModalityType(ModalityType.APPLICATION_MODAL);
        panneauInit = new PanneauInitialisation();
        setLayout(new BorderLayout());
        add(panneauInit, "Center");
        JPanel boutons = new JPanel();
        boutons.add(valider);
        boutons.add(annuler);
        add(boutons, "South");
         annuler.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                reponse = false;
            }
        });
      
        valider.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                reponse = true;
            }
        });

        setResizable(false);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        pack();
    }
    public void setInitialisation(Initialisation initialisation)  {
        remove(panneauInit);
        panneauInit = new PanneauInitialisation(initialisation);
        add(panneauInit, "Center"); 

        
        if (environnement != null)
            environnement.redessine();
        //environnement.pack();
        //if (environnement.getExtendedState() != JFrame.MAXIMIZED_BOTH)
        //    environnement.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public Initialisation getInitialisation() {
        return panneauInit.getInitialisation();
    }
    public boolean getOk() {
        return reponse;
    }
}
