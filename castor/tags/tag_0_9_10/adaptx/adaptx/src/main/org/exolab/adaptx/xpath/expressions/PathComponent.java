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
 * A basic interface for LocationStep or FilterExpr
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public interface PathComponent 
    extends XPathExpression, MatchExpression
{

    /**
     * Returns true if this PathComponent is a FilterExpr.
     * Note that if this method returns true, then a call to 
     * #isLocationStep must return false.
     *
     * @return true if this PathComponent is a FilterExpr
     * @see isLocationStep
     */
    public boolean isFilterExpr();
    
    /**
     * Returns true if this PathComponent is a LocationStep.
     * Note that if this method returns true, then a call to 
     * #isFilterExpr must return false.
     *
     * @return true if this PathComponent is a LocationStep
     * @see isFilterExpr
     */
    public boolean isLocationStep();
    

    /**
     * Determines the priority of a PatternExpr as follows:
     * <PRE>
     *  From the 19991116 XSLT 1.0 Recommendation:
     *  + If the pattern has the form of a QName preceded by a
     *    ChildOrAttributeAxisSpecifier or has the form 
     *    processing-instruction(Literal) then the priority is 0.
     *  + If the pattern has the form NCName:* preceded by a 
     *    ChildOrAttributeAxisSpecifier, then the priority is -0.25
     *  + Otherwise if the pattern consists of just a NodeTest 
     *    preceded by a ChildOrAttributeAxisSpecifier then the
     *    priority is -0.5
     *  + Otherwise the priority is 0.5
     * </PRE>
     * @return the priority for this PatternExpr
     */
    public abstract double getDefaultPriority();
    

    /**
     * Returns the sub-expression encapsulated by this PathComponent.
     * the sub-expression will either be a PrimaryExpr if this
     * PathComponent is a FilterExpr, or a NodeExpression if this
     * Pathcomponent is a LocationStep. This method may return
     * null if no such sub-expression exists for the PathComponent.
     *
     * @return the sub-expression encapsulated by this PathComponent.
     */
    public XPathExpression getSubExpression();
    
    /**
     * Evaluates the PredicateExpr of this PathComponent against the given 
     * NodeSet and XPathContext.
     *
     * @param nodes the current NodeSet
     * @param context the XPathContext for use during evaluation.
     */
    public void evaluatePredicates( NodeSet nodes, XPathContext context )
        throws XPathException;
    
    /**
     * Returns the PredicateExpr of this PathComponent
     *
     * @return the PredicateExpr of this PathComponent
     */
    public PredicateExpr getPredicate();
    
    /**
     * Returns true if this PathComponent has predicates expressions.
     *
     * @return true if this PathComponent has predicates expressions.
     */
    public boolean hasPredicates();
    

    
} //-- PathComponent
