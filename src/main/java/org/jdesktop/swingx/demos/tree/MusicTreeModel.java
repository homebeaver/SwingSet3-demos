package org.jdesktop.swingx.demos.tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.TreeTableModel;

// interface TreeTableModel extends TreeModel
// interface javax.swing.table.TableModel
public class MusicTreeModel extends AbstractTableModel implements TreeTableModel, TableModel {

	/*
# Key:                                                                         #
#     Popular   / Classical                                                    #
# ----------------------------                                                 #
# A = Artist    / Composer                                                     #
# R = Record    / Style                                                        #
# S = Song Name / Composition                                                  #
# C = Catagory                                                                 #
	 */
    static public class MusicEntry {
    	static char SEPARATOR = ';';
    	private Integer id; // root == 0 or Null
    	String nameOrTitle; // Category: Classical, Jazz, ... ; Artist: Name; Album/Record: Title or Style
        String url; // wikimedia audio url of the song/composition or wikimedia image url of the album
        // f.i. "https://upload.wikimedia.org/wikipedia/en/a/ac/My_Name_Is_Albert_Ayler.jpg"
        
    	MusicEntry(String nameTitle) {
    		nameOrTitle = nameTitle;
    	}
    	MusicEntry(int id, String line) {
    		this.id = id;
			String titlePlusUrl = line.substring(2);
			int separator = titlePlusUrl.indexOf(SEPARATOR);
			url = null;
			if (separator == -1) {
				nameOrTitle = titlePlusUrl;
			} else {
				nameOrTitle = titlePlusUrl.substring(0, separator);
				url = titlePlusUrl.substring(separator+1);
			}
    	}
    	
		public String toString() {
			return nameOrTitle;
		}
    }
    static public class Album extends MusicEntry {
        
		Album(int id, String line) {
			super(id, line);
		}

		public URL getURL() {
			try {
				return url==null ? null : new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;		
		}
		
		public String getHtmlSrc() {
			if(url==null) return null;
			return "<html>" + "<img src=\"" + url + "\">"+ "</html>";
		}

    }
    
    static public class Song extends MusicEntry { // or Composition
        
        Song(int id, String line) {
        	super(id, line);
		}

		public URL getURL() throws MalformedURLException {
			return new URL(url);
		}
		
/*
    <audio
        controls
        src="/media/cc0-audio/t-rex-roar.mp3">
            <a href="/media/cc0-audio/t-rex-roar.mp3">
                Download audio
            </a>
    </audio>
or
	<audio src="AudioTest.ogg" autoplay>
	  <a href="AudioTest.ogg">Download OGG audio</a>.
	</audio>
	
    setToolTipText("<html><center><font color=blue size=+2>" + utl + "</font></center></html>");
	
	
 */
		public String getHtmlSrc() {
			if(url==null) return null;
			return "<html>" + "<center><font color=blue size=+1>"+url+"</font></center>" + "</html>";
		}

    }

    int rowCount = 0;
    DefaultMutableTreeNode top;
    DefaultMutableTreeNode catagory;
    DefaultMutableTreeNode artist;
    DefaultMutableTreeNode record;
    DefaultMutableTreeNode song;

	private MusicTreeModel() {
		super();
	}
	
	MusicTreeModel(String topName, URL url) {
		this();
//        LOG.info(top + " tree data url="+url);
		// use url.getProtocol() or url.getPath()
        top = new DefaultMutableTreeNode(new MusicEntry(topName==null ? url.getPath() : topName));

        try {
            // convert url to buffered string
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);

            // read one line at a time, put into tree
            String line = reader.readLine();
            while(line != null) {
//            	LOG.fine("reading in: ->" + line + "<-");
                char linetype = line.charAt(0);
                switch(linetype) {
                   case 'C':
                     rowCount++;
                     catagory = new DefaultMutableTreeNode(new MusicEntry(rowCount, line));
                     top.add(catagory);
                     break;
                   case 'A':
                     if(catagory != null) {
                         rowCount++;
                         catagory.add(artist = new DefaultMutableTreeNode(new MusicEntry(rowCount, line)));
                     }
                     break;
                   case 'R':
                     if(artist != null) {
                         rowCount++;
                    	 artist.add(record = new DefaultMutableTreeNode(new Album(rowCount, line)));
                     }
                     break;
                   case 'S':
                     if(record != null) {
                         rowCount++;
                         record.add(song = new DefaultMutableTreeNode(new Song(rowCount, line)));
                     }
                     break;
                   default:
                     break;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
        }
	}

	// implements abstract methods of AbstractTableModel ----------------------------
	
	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex==0) {
			Object o = getRoot();
			MusicEntry me = (MusicEntry)o;
			switch(columnIndex) {
            case 0:
            	return me.id;
                //break;
            case 1:
            	return me.toString();
                //break;
            case 2:
            	return me.url;
                //break;
            default:
                break;
			}
		}
//		System.out.println("rowIndex="+rowIndex + ", columnIndex="+columnIndex);
		Enumeration<TreeNode> nodes = top.breadthFirstEnumeration();
		while(nodes.hasMoreElements()) {
			TreeNode next = nodes.nextElement();
			if(next instanceof DefaultMutableTreeNode dmtn) {
				Object o = dmtn.getUserObject();
				MusicEntry me = (MusicEntry)o;
				if(me.id!=null && rowIndex==me.id.intValue()) {
					switch(columnIndex) {
		            case 0:
		            	return me.id;
		                //break;
		            case 1:
		            	return me.toString();
		                //break;
		            case 2:
		            	return me.url;
		                //break;
		            default:
		                break;
					}
				}
			}
		}
		return null;
	}

// implements interface TreeModel and TreeTableModel ---------------------------------

	@Override
	public Object getRoot() {
		return top.getUserObject();
	}

	@Override
	public Object getChild(Object parent, int index) {
		Enumeration<TreeNode> nodes = top.breadthFirstEnumeration();
		while(nodes.hasMoreElements()) {
			TreeNode next = nodes.nextElement();
			if(next instanceof DefaultMutableTreeNode dmtn) {
				if(parent==dmtn.getUserObject()) {
					return ((DefaultMutableTreeNode)dmtn.getChildAt(index)).getUserObject();
				}
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		Enumeration<TreeNode> nodes = top.breadthFirstEnumeration();
		while(nodes.hasMoreElements()) {
			TreeNode next = nodes.nextElement();
			if(next instanceof DefaultMutableTreeNode dmtn) {
				if(parent==dmtn.getUserObject()) {
					return dmtn.getChildCount();
				}
			}
		}
		return -1;
	}

	@Override
	public boolean isLeaf(Object node) {
		return getChildCount(node) == 0; // liefert bei node nicht im Baum false!
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		Enumeration<TreeNode> nodes = top.breadthFirstEnumeration();
		while(nodes.hasMoreElements()) {
			TreeNode next = nodes.nextElement();
			if(next instanceof DefaultMutableTreeNode dmtn) {
				if(parent==dmtn.getUserObject()) {
					Enumeration<TreeNode> children = dmtn.children();
					int ret = 0;
					while(children.hasMoreElements()) {
						if(child==((DefaultMutableTreeNode)children.nextElement()).getUserObject()) return ret;
						ret++;
					}
				}
			}
		}
		return -1;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
	}

//	P-S. TableModelListener management provided by AbstractTableModel superclass:
//    public void addTableModelListener(TableModelListener l);
//    public void removeTableModelListener(TableModelListener l);

	// ab hier TreeTableModel >>>>>>>>>>>>>>>>>>>>>>
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex==0) return Integer.class;
		if(columnIndex==1) return String.class;
		if(columnIndex==2) return URL.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int column) {
		if(column==0) return "id";
		if(column==1) return "name";
		if(column==2) return "url";
		return super.getColumnName(column);
	}

	@Override
	public int getHierarchicalColumn() {
		return 0;
	}

	@Override
	public Object getValueAt(Object node, int column) {
		if(column<0 || column>=getColumnCount()) return null;
		Enumeration<TreeNode> nodes = top.breadthFirstEnumeration();
		while(nodes.hasMoreElements()) {
			TreeNode next = nodes.nextElement();
			if(next instanceof DefaultMutableTreeNode dmtn) {
				if(node==dmtn.getUserObject()) {
					if(3==dmtn.getLevel()) {
						// Album
						return column==getHierarchicalColumn() ? node.toString() : ((Album)node).getURL();
					} else {
						// alle anderen sind String
						return node;
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean isCellEditable(Object node, int column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValueAt(Object value, Object node, int column) {
		// TODO Auto-generated method stub
		
	}

}
