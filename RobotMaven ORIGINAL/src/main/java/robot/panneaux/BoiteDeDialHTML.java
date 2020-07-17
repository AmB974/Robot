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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author Yvan
 */
public class BoiteDeDialHTML extends javax.swing.JDialog {
    
    private static final long serialVersionUID = 1L;
    
    /** Creates new form exercicesDialog */
    private URL texteURL;
    public BoiteDeDialHTML(URL texteURL) {
        super((JFrame)null, false);
        this.texteURL = texteURL;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        add(textPaneExercices(), "Center");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
    }

    private JScrollPane textPaneExercices() {
        textPaneExercices = new JTextPane();

        JScrollPane paneScrollPane = new JScrollPane(textPaneExercices);
        paneScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        paneScrollPane.setPreferredSize(new Dimension(250, 155));
        paneScrollPane.setMinimumSize(new Dimension(10, 10));
        textPaneExercices.setEditable(false);


        try {
            textPaneExercices.setPage(texteURL);
        } catch (IOException ex) {
            Logger.getLogger(BoiteDeDialHTML.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paneScrollPane;
    }

    private javax.swing.JTextPane textPaneExercices;
}
