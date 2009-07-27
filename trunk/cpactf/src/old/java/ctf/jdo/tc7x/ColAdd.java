 /*
 * $Id$
 */
package ctf.jdo.tc7x;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Test object for different collection types.
 */
public final class ColAdd extends Col {
    private ArrayList _item;

    public boolean containsItem(final Item item) {
        if ((_item == null) || (_item.size() == 0)) { return false; }
        return _item.contains(item);
    }

    public Iterator itemIterator() {
        if ((_item == null) || (_item.size() == 0)) { return EMPTY_ITORATOR; }
        return _item.iterator();
    }

    public void removeItem(final Item item) {
        if (_item != null) {
            _item.remove(item);
            item.setTestCol(null);
        }
    }

    public int itemSize() {
        if (_item == null) { return 0; }
        return _item.size();
    }

    public ArrayList getItems() { return _item; }

    public void addItem(final Item item) {
        if (_item == null) { _item = new ArrayList(); }
        _item.add(item);
        item.setTestCol(this);
    }
}
