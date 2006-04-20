/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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


package org.exolab.adaptx.xpath.expressions;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;

/**
 * This interface represents a Location Step as defined by XPath 1.0
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public interface LocationStep extends PathComponent
{


    public static final short ANCESTORS_AXIS           = 0;
    public static final short ANCESTORS_OR_SELF_AXIS   = 1;
    public static final short ATTRIBUTES_AXIS          = 2;
    public static final short CHILDREN_AXIS            = 3;
    public static final short DESCENDANTS_AXIS         = 4;
    public static final short DESCENDANTS_OR_SELF_AXIS = 5;
    public static final short FOLLOWING_AXIS           = 6;
    public static final short FOLLOWING_SIBLINGS_AXIS  = 7;
    public static final short PARENT_AXIS              = 8;
    public static final short PRECEDING_AXIS           = 9;
    public static final short PRECEDING_SIBLINGS_AXIS  = 10;
    public static final short SELF_AXIS                = 11;
    public static final short NAMESPACE_AXIS           = 12;






    /**
     * Returns the axis-identifier for this LocationStep.
     *
     * @return the axis-identifier for this LocationStep.
     */
    public short getAxisIdentifier();
    
    /**
     * Returns the NodeExpression for this LocationStep
     *
     * @return the NodeExpression for this LocationStep
     */
    public NodeExpression getNodeExpr();


} //-- LocationStep
