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
package robot;

import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author maillot
 */
public abstract class NoeudProgramme extends DefaultMutableTreeNode {
    private static final long serialVersionUID = 1L;

    @Override
    public void add(MutableTreeNode newChild) {
        super.add(newChild);
    }

    @Override
    public Enumeration<?> breadthFirstEnumeration() {
        return super.breadthFirstEnumeration();
    }

    @Override
    public Enumeration<?> children() {
        return super.children();
    }
    
    public abstract ImageIcon getIcon();
    
    

    @Override
    public NoeudProgramme clone() {
        NoeudProgramme n = (NoeudProgramme) super.clone();
        for(int i = 0; i < getChildCount(); i++) {
            n.add(((NoeudProgramme) getChildAt(i)).clone());
        }
        return n;
    }

    @Override
    public Enumeration<?> depthFirstEnumeration() {
        return super.depthFirstEnumeration();
    }

    @Override
    public boolean getAllowsChildren() {
        return super.getAllowsChildren();
    }

    @Override
    public TreeNode getChildAfter(TreeNode aChild) {
        return super.getChildAfter(aChild);
    }

    @Override
    public TreeNode getChildAt(int index) {
        return super.getChildAt(index);
    }

    @Override
    public TreeNode getChildBefore(TreeNode aChild) {
        return super.getChildBefore(aChild);
    }

    @Override
    public int getChildCount() {
        return super.getChildCount();
    }

    @Override
    public int getDepth() {
        return super.getDepth();
    }

    @Override
    public TreeNode getFirstChild() {
        return super.getFirstChild();
    }

    @Override
    public NoeudProgramme getFirstLeaf() {
        return (NoeudProgramme) super.getFirstLeaf();
    }

    @Override
    public int getIndex(TreeNode aChild) {
        return super.getIndex(aChild);
    }

    @Override
    public TreeNode getLastChild() {
        return super.getLastChild();
    }

    @Override
    public NoeudProgramme getLastLeaf() {
        return (NoeudProgramme) super.getLastLeaf();
    }

    @Override
    public int getLeafCount() {
        return super.getLeafCount();
    }

    @Override
    public int getLevel() {
        return super.getLevel();
    }

    @Override
    public NoeudProgramme getNextLeaf() {
        return (NoeudProgramme) super.getNextLeaf();
    }

    @Override
    public NoeudProgramme getNextNode() {
        return (NoeudProgramme) super.getNextNode();
    }

    @Override
    public NoeudProgramme getNextSibling() {
        return (NoeudProgramme) super.getNextSibling();
    }

    @Override
    public TreeNode getParent() {
        return super.getParent();
    }

    @Override
    public TreeNode[] getPath() {
        return super.getPath();
    }

    @Override
    protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
        return super.getPathToRoot(aNode, depth);
    }

    @Override
    public NoeudProgramme getPreviousLeaf() {
        return (NoeudProgramme) super.getPreviousLeaf();
    }

    @Override
    public NoeudProgramme getPreviousNode() {
        return (NoeudProgramme) super.getPreviousNode();
    }

    @Override
    public NoeudProgramme getPreviousSibling() {
        return (NoeudProgramme) super.getPreviousSibling();
    }

    @Override
    public TreeNode getRoot() {
        return super.getRoot();
    }

    @Override
    public TreeNode getSharedAncestor(DefaultMutableTreeNode aNode) {
        return super.getSharedAncestor(aNode);
    }

    @Override
    public int getSiblingCount() {
        return super.getSiblingCount();
    }

    @Override
    public Object getUserObject() {
        return super.getUserObject();
    }

    @Override
    public Object[] getUserObjectPath() {
        return super.getUserObjectPath();
    }

    @Override
    public void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
    }

    @Override
    public boolean isLeaf() {
        return super.isLeaf();
    }

    @Override
    public boolean isNodeAncestor(TreeNode anotherNode) {
        return super.isNodeAncestor(anotherNode);
    }

    @Override
    public boolean isNodeChild(TreeNode aNode) {
        return super.isNodeChild(aNode);
    }

    @Override
    public boolean isNodeDescendant(DefaultMutableTreeNode anotherNode) {
        return super.isNodeDescendant(anotherNode);
    }

    @Override
    public boolean isNodeRelated(DefaultMutableTreeNode aNode) {
        return super.isNodeRelated(aNode);
    }

    @Override
    public boolean isNodeSibling(TreeNode anotherNode) {
        return super.isNodeSibling(anotherNode);
    }

    @Override
    public boolean isRoot() {
        return super.isRoot();
    }

    @Override
    public Enumeration<?> pathFromAncestorEnumeration(TreeNode ancestor) {
        return super.pathFromAncestorEnumeration(ancestor);
    }

    @Override
    public Enumeration<?> postorderEnumeration() {
        return super.postorderEnumeration();
    }

    @Override
    public Enumeration<?> preorderEnumeration() {
        return super.preorderEnumeration();
    }

    @Override
    public void remove(int childIndex) {
        super.remove(childIndex);
    }

    @Override
    public void remove(MutableTreeNode aChild) {
        super.remove(aChild);
    }

    @Override
    public void removeAllChildren() {
        super.removeAllChildren();
    }

    @Override
    public void removeFromParent() {
        super.removeFromParent();
    }

    @Override
    public void setAllowsChildren(boolean allows) {
        super.setAllowsChildren(allows);
    }
/*
    @Override
    public void setParent(MutableTreeNode newParent) {
        super.setParent(newParent);
    }*/

    @Override
    public void setUserObject(Object userObject) {
        super.setUserObject(userObject);
    }

    @Override
    public String toString() {
        return super.toString();
    }
    
}
