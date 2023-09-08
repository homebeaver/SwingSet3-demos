package org.jdesktop.swingx.demos.tree;

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.renderer.WrappingProvider;
import org.jdesktop.swingx.rollover.RolloverRenderer;
import org.jdesktop.swingx.tree.DefaultXTreeCellRenderer;

@Deprecated
public class JXMaintananceTree extends JXTree {

	private static final long serialVersionUID = -4118213619140974890L;
	private static final Logger LOG = Logger.getLogger(JXMaintananceTree.class.getName());

	public JXMaintananceTree(TreeModel newModel) {
		super(newModel);
	}

	@Override
    public TreeCellRenderer getCellRenderer() {
    	StringValue sv = (Object value) -> {
    		if(value==null) return StringValues.TO_STRING.getString(value);
            if(value instanceof MTreeNode mTreeNode) return StringValues.TO_STRING.getString(mTreeNode.getName());
            String simpleName = value.getClass().getSimpleName();
            return simpleName + "(" + value + ")";
    	};
        return new TreeDelegatingRenderer(null, null, sv);
    }

//  class DelegatingRenderer extends DefaultTreeRenderer implements TreeCellRenderer, RolloverRenderer
	@SuppressWarnings("serial")
	class TreeDelegatingRenderer extends DefaultTreeRenderer implements TreeCellRenderer, RolloverRenderer {

		private Icon closedIcon = null;
		private Icon openIcon = null;
		private Icon leafIcon = null;
        private TreeCellRenderer delegate = null;

		public TreeDelegatingRenderer(TreeCellRenderer delegate, IconValue iv, StringValue delegateStringValue) {
//			super(new WrappingProvider(iv, delegateStringValue));
// ==
			super(new WrappingProvider(iv, delegateStringValue, false));
			/* class WrappingProvider extends ComponentProvider<WrappingIconPanel>
			 *    ctor WrappingProvider(iv, sv) ==> this(iconValue, delegateStringValue, true)
			 * == ctor WrappingProvider(IconValue iv, ComponentProvider<?> delegate, boolean unwrapUserObject)
			 * this(iv, (ComponentProvider<?>) null, unwrapUserObject);
			 * getWrappee().setStringValue(delegateStringValue);
			 */
			ComponentProvider<?> cp = getComponentProvider(); // member componentController in AbstractRenderer
			WrappingProvider wp = (WrappingProvider)cp;
			LOG.info("--- .ComponentProvider.Wrappee:"+wp.getWrappee());
			if (delegate instanceof DefaultTreeCellRenderer javaxDTCR) {
				initIcons(javaxDTCR);
				setDelegateRenderer(delegate);
			} else {
//        		initIcons(new DefaultTreeCellRenderer());
				// EUG better DefaultXTreeCellRenderer extends DefaultTreeCellRenderer ?
				initIcons(new DefaultXTreeCellRenderer());
			}
		}

		private void initIcons(DefaultTreeCellRenderer renderer) {
			closedIcon = renderer.getDefaultClosedIcon();
			openIcon = renderer.getDefaultOpenIcon();
			leafIcon = renderer.getDefaultLeafIcon();
		}
        private void updateIcons() {
            if (!isOverwriteRendererIcons()) return;
            setClosedIcon(closedIcon);
            setOpenIcon(openIcon);
            setLeafIcon(leafIcon);
        }

        public void setDelegateRenderer(TreeCellRenderer delegate) {
            if (delegate == null) {
                delegate = createDefaultCellRenderer();
            }
            this.delegate = delegate;
            updateIcons();
        }

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, 
			boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        	Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        	LOG.info("getTreeCellRendererComponent for "+(value==null?"":value.getClass())+" value "+value
        			+ " componentController/Provider:"+getComponentProvider()
        			+ "\n returns "+c);
        	if(c instanceof WrappingIconPanel wip) {
        		LOG.info("WrappingIconPanel JComponent delegate:"+wip.getComponent());
        	}
        	return c;
		}

//		@Override // interface RolloverRenderer
		public boolean isEnabled() {
			if(delegate instanceof RolloverRenderer rolloverRenderer) {
				return rolloverRenderer.isEnabled();
			}
            return false;
		}

		@Override // interface RolloverRenderer
		public void doClick() {
            if (isEnabled()) {
                ((RolloverRenderer) delegate).doClick();
            }
		}

	}
}
