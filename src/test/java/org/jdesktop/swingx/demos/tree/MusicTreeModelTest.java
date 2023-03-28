package org.jdesktop.swingx.demos.tree;

import static org.junit.Assert.*;

import org.junit.Test;

public class MusicTreeModelTest {

	@Test
	public void nullRoot() {
		MusicTreeModel model = new MusicTreeModel(null, getClass().getResource("resources/tree.txt"));
		assertNotNull(model.getRoot());
		assertEquals(3, model.getChildCount(model.getRoot()));
		assertEquals(model.catagory.getUserObject(), model.getChild(model.getRoot(), 2));
		assertEquals(616, model.getRowCount());
	}
	
	@Test
	public void test() {
		MusicTreeModel model = new MusicTreeModel("Root", getClass().getResource("resources/tree.txt"));
		Object o = model.getRoot();
		assertEquals("Root", ((MusicTreeModel.MusicEntry)o).toString());
		assertEquals(3, model.getChildCount(model.getRoot()));
		assertEquals(model.catagory.getUserObject(), model.getChild(model.getRoot(), 2));
		
		// there is no node with String "XXX"
		assertEquals(-1, model.getChildCount("XXX"));
		
		// Rock has 6 childs, the last is "Steve Miller Band"
		assertEquals(6, model.getChildCount(model.catagory.getUserObject()));
		assertEquals(model.artist.getUserObject(), model.getChild(model.catagory.getUserObject(), 5));
		assertSame(model.artist.getUserObject(), model.getChild(model.catagory.getUserObject(), 5));
		
		// there is no node with String "XXX"
		assertFalse(model.isLeaf("XXX"));
		assertFalse(model.isLeaf(model.artist.getUserObject()));
		assertEquals("The Joker", model.record.getUserObject().toString());	
		assertTrue(model.isLeaf(model.getChild(model.record.getUserObject(), 0)));
		
		// the last record/album of "Steve Miller Band" has index 3
		assertEquals(3, model.getIndexOfChild(model.artist.getUserObject(), model.record.getUserObject()));
		// record "The Joker" is not child of root/top
		assertEquals(-1, model.getIndexOfChild(model.top.getUserObject(), model.record.getUserObject()));
		assertEquals(-1, model.getIndexOfChild(model.artist.getUserObject(), "XXX"));
		
		// TreeTableModel tests:
		assertNull(model.getValueAt(model.top.getUserObject(), model.getColumnCount())); // column not exists!
		Object top = model.top.getUserObject();
		Object top0 = model.getValueAt(top, 1);
		assertEquals("Root", top0.toString());
		Object artist = model.artist.getUserObject();
		Object artist0 = model.getValueAt(artist, 1);
		assertEquals("Steve Miller Band", artist0.toString());
		assertEquals("The Joker", model.getValueAt(model.record.getUserObject(), 1));
	}

}
