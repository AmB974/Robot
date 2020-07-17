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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import robot.NoeudProgramme;

/**
 *
 * @author yvan
 */
public class JTreeRobot extends JTree {
    
    private static final long serialVersionUID = 1L;

    private int memX = -1, memY = -1;    
    private NoeudProgramme noeudSelectionné;

    public JTreeRobot(DefaultTreeModel modele) {
        super(modele);
        
        
        DefaultTreeCellRenderer dtcr = new DefaultTreeCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                if (((NoeudProgramme) value).getIcon() != null) {
                    setIcon(((NoeudProgramme) value).getIcon());
                }
                return this;
            }
        };
        
        dtcr.setLeafIcon(null);        
        setCellRenderer(dtcr);

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                try {
                    memX = me.getX();
                    memY = me.getY();
                    noeudSelectionné = (NoeudProgramme) getPathForLocation(memX, memY).getLastPathComponent();
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } catch (Exception ex) {
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {

                try {
                    if (memX != me.getX() && memY != me.getY()) {
                        NoeudProgramme noeud = (NoeudProgramme) getPathForLocation(me.getX(), me.getY()).getLastPathComponent();
                        NoeudProgramme parent = (NoeudProgramme) noeud.getParent();
                        int i = parent.getIndex(noeud);
                        if (me.isControlDown()) {
                            parent.insert(noeudSelectionné.clone(), i);
                        } else {
                            parent.insert(noeudSelectionné, i);
                        }
                        updateUI();
                    }
                } catch (Exception ex) {
                } finally {
                    noeudSelectionné = null;
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    memX = -1;
                    memY = -1;
                }

            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
    }
}
