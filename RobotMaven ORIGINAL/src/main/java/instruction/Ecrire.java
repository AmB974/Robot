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
package instruction;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.*;
import robot.DansLeMur;

/**
 *
 * @author Yvan
 */
public class Ecrire extends InstructionElementaire {
    
    private static final long serialVersionUID = 1L;
    
    String message;
    private transient JDialog boite;
    private transient robot.Robot robot;

    /*
     * private JDialog creerDialogue(String message) {
     *
     * }
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        créerDialogue();
    }
    
    private void créerDialogue() {
        
        boite = new JDialog((Frame) null, "Nono vous informe");
        boite.setModal(false);
        boite.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        boite.setLayout(new BorderLayout());
        boite.add(new JLabel(message));
        JButton ok = new JButton("Ok");
        JPanel p = new JPanel();
        p.add(ok);
        
        boite.add(p, "South");
        if (Toolkit.getDefaultToolkit().isAlwaysOnTopSupported()) {
            boite.setAlwaysOnTop(true);
        }
        boite.pack();
        ok.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (robot != null) {
                    robot.deBloquer();
                }
                boite.setVisible(false);
            }
        });
    }
    
    /**
     * Méthode pour afficher un message
     * @param message le message à afficher.
     */
    public Ecrire(String message) {
        this.message = message;
        nom = "écrire, " + message;
        créerDialogue();
    }
    
    @Override
    public void go(robot.Robot robot) throws DansLeMur, InterruptedException {
        this.robot = robot;
        if (boite != null) {
            robot.bloquer();
            boite.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, message, "Nono vous informe", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    @Override
    public ImageIcon getIcon() {
        return null;
    }
}
