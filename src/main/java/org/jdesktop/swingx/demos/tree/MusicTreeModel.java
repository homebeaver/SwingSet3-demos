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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.TreeTableModel;

// interface TreeTableModel extends TreeModel
public class MusicTreeModel extends AbstractTableModel implements TreeTableModel {

    static public class Album {
    	static char SEPARATOR = ';';
        private String record; // title of the album
        private String pixUrl; // wikimedia image url of the album
        // f.i. "https://upload.wikimedia.org/wikipedia/en/a/ac/My_Name_Is_Albert_Ayler.jpg"
        
		Album(String line) {
			String recordAlbumpix = line.substring(2);
			int separator = recordAlbumpix.indexOf(SEPARATOR);
			pixUrl = null;
			if (separator == -1) {
				record = recordAlbumpix;
			} else {
				record = recordAlbumpix.substring(0, separator);
				pixUrl = recordAlbumpix.substring(separator+1);
			}
		}

		public URL getURL() {
			try {
				return pixUrl==null ? null : new URL(pixUrl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;		
		}
		
		public String getHtmlSrc() {
			if(pixUrl==null) return null;
			return "<html>" + "<img src=\"" + pixUrl + "\">"+ "</html>";
		}

		public String toString() {
			return record;
		}
    }
    
    static public class Song { // or Composition
    	static char SEPARATOR = ';';
        private String title; // title of the song
        private String url; // wikimedia audio url of the song/composition
        // f.i. "https://commons.wikimedia.org/wiki/File:Ludwig_van_Beethoven_-_Symphonie_5_c-moll_-_1._Allegro_con_brio.ogg"
        
        Song(String line) {
			String titlePlusUrl = line.substring(2);
			int separator = titlePlusUrl.indexOf(SEPARATOR);
			url = null;
			if (separator == -1) {
				title = titlePlusUrl;
			} else {
				title = titlePlusUrl.substring(0, separator);
				url = titlePlusUrl.substring(separator+1);
			}
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

		public String toString() {
			return title;
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
        top = new DefaultMutableTreeNode(topName==null ? url.getPath() : topName);
        rowCount++;
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
                     catagory = new DefaultMutableTreeNode(line.substring(2));
                     top.add(catagory);
                     rowCount++;
                     break;
                   case 'A':
                     if(catagory != null) {
                         catagory.add(artist = new DefaultMutableTreeNode(line.substring(2)));
                         rowCount++;
                     }
                     break;
                   case 'R':
                     if(artist != null) {
                    	 artist.add(record = new DefaultMutableTreeNode(new Album(line)));
                         rowCount++;
                     }
                     break;
                   case 'S':
                     if(record != null) {
//                         record.add(new DefaultMutableTreeNode(line.substring(2)));
                         record.add(song = new DefaultMutableTreeNode(new Song(line)));
                         rowCount++;
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
		// TODO Auto-generated method stub
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
		if(columnIndex==0) return String.class;
		if(columnIndex==1) return URL.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int column) {
		if(column==0) return "name";
		if(column==1) return "url";
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
