/*
 * (C) Copyright Keith Visco 1998, 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */


package org.exolab.castor.util;


import java.util.Vector;

/**
 * A simple String to Object mapping which preserves order
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class OrderedMap {
    private Vector names = null;
    private Vector objects = null;
    
    /**
     * Creates a new OrderedMap
    **/
    public OrderedMap() {
        names = new Vector();
        objects = new Vector();
    } //-- OrderedMap

    /**
     * Creates a new OrderedMap with the given size
    **/
    public OrderedMap(int size) {
        names = new Vector(size);
        objects = new Vector(size);
    } //-- OrderedMap
    
    
    
    public Object get(String name) {
        int i = indexOf(name);
        if (i >= 0) return objects.elementAt(i);
        return null;
    } //-- get
    
    public Object get(int i) throws IndexOutOfBoundsException {
        return objects.elementAt(i);
    } //-- get
    
    public String getNameByObject(Object obj) {
        int i = objects.indexOf(obj);
        if (i >= 0) return (String)names.elementAt(i);
        return null;
    } //-- getNameByObject
    
    public Vector getNames() {
        return names;
    }
    
    public Vector getObjects() {
        return objects;
    }
    
    /**
     * Returns the index of the object with mapped to the given name
    **/
    public int indexOf(String name) {
        
        for (int i = 0; i < names.size(); i++) {
            String iName = (String)names.elementAt(i);
            if (iName.equals(name)) return i;
        }
        return -1;
        
    } //-- indexOf
    
    public void put(String name, Object obj) {
        int idx = indexOf(name);
        if (idx >= 0) objects.setElementAt(obj, idx);
        else {
            //-- we may need some synchronization here
            //-- if we are in a multithreaded environment
            names.addElement(name);
            objects.addElement(obj);
        }
    } //-- put
    
    public int size() {
        return names.size();
    }
    
    
} //-- OrderedMap
