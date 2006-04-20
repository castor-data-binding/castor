/*
 * MyObject.java
 *
 * Created on June 23, 2005, 12:58 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package jdo.test1158;

import org.exolab.castor.jdo.TimeStampable;

/**
 * @author nstuart
 */
public class BaseObject implements TimeStampable{
    private int _id;
    private String _description;
    private boolean _saved;
    private long _timestamp;

    public int getId() { return _id; }
    public void setId(int id) { _id = id; }

    public String getDescription() { return _description; }
    public void setDescription(String description) { _description = description; }

    public boolean isSaved() { return _saved; }
    public void setSaved(boolean saved) { _saved = saved; }

    public long getTimestamp() { return _timestamp; }
    public void setTimestamp(long timestamp) { _timestamp = timestamp; }

    public void jdoSetTimeStamp(long timestamp) { setTimestamp(timestamp); }
    public long jdoGetTimeStamp() { return getTimestamp(); }
}
