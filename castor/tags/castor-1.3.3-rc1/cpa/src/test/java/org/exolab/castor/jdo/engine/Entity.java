package org.exolab.castor.jdo.engine;


public class Entity {

    private int _id;
    
    private Item _item;

    /**
     * @return the id
     */
    public final int getId() {
        return _id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(final int newId) {
        this._id = newId;
    }

    /**
     * @return the items
     */
    public final Item getItem() {
        return _item;
    }

    /**
     * @param items the items to set
     */
    public final void setItem(final Item item) {
        this._item = item;
    }
    
    
    
}
