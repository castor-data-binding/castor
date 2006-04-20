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


package org.exolab.adaptx.xpath.engine;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.expressions.MatchExpression;
import org.exolab.adaptx.xpath.expressions.PathComponent;
import org.exolab.adaptx.xpath.expressions.PredicateExpr;

/**
 * The base class for paths (now basically called paths)
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public abstract class AbstractPathComponent implements PathComponent
{


    PredicateExprImpl _predicate = null;
    
    
    /**
     * Creates a new AbstractPathComponent
     */
    public AbstractPathComponent()
    {
        super();
        
    } //-- AbstractPathComponent

    
    /**
     * Adds the given Expression to this PathComponent's predicate
     * List.
     *
     * @param expr the Expr to add to the predicate list
     */
    public void addPredicate( XPathExpression expr )
        throws XPathException
    {
        addPredicate( new PredicateExprImpl( expr ) );
    } //-- addPredicate
    

    /**
     * Adds the given Expression to this PathComponent's predicate
     * List.
     *
     * @param expr the Expr to add to the predicate list
     */
    public void addPredicate( PredicateExprImpl predicate )
        throws XPathException
    {
        if ( _predicate == null )
            _predicate = predicate;
        else {
            PredicateExprImpl last = _predicate;
            while ( last.hasNext() )
                last = (PredicateExprImpl)last.getNext();
            last.setNext( predicate );
        }
    } //-- addPredicate
    
    
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
    public abstract XPathExpression getSubExpression();
    

    public abstract short getExprType();


    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
     */
    public abstract XPathResult evaluate( XPathContext context )
        throws XPathException;


    /**
     * Returns the PredicateExpr of this PathComponent
     *
     * @return the PredicateExpr of this PathComponent
     */
    public PredicateExpr getPredicate()
    {
        return _predicate;
    } //-- getPredicate
    

    /**
     * Returns true if this FilterBase has predicates expressions.
     *
     * @return true if this FilterBase has predicates expressions.
     */
    public boolean hasPredicates()
    {
        return _predicate != null;
    } //-- hasPredicates
    

    /**
     * Evaluates the PredicateExpr of this PathComponent against the 
     * given Node.
     *
     * @param nodes the current NodeSet
     * @param contextInfo provides a way to retrieve additional context 
     * information needed to evaluate some expressions
     */
    public void evaluatePredicates( NodeSet nodes, XPathContext context )
        throws XPathException 
    {
        if ( _predicate == null || nodes == null )
            return;
        
        if (nodes.size() == 0) return;
        
        PredicateExpr expr = _predicate;
        
        NodeSet nodesToRemove = context.newNodeSet(nodes.size());
        
        while ( expr != null ) {
            
            XPathContext tmpContext = context.newContext(nodes, 0); 
            
            int nIdx = 0;
            for ( ; nIdx < nodes.size() ; nIdx++ ) {

                if (nIdx > 0) tmpContext.setPosition(nIdx);
                
                XPathResult result = expr.evaluate( tmpContext );
                //-- check for NumberResult in case we need to
                //-- check the position of the node
                switch( result.getResultType() ) {
                case XPathResult.NUMBER:
                    double val = result.numberValue();
                    if ( val != (double) ( nIdx + 1  ) ) {
                        nodesToRemove.add( tmpContext.getNode() );
                    }
                    break;
                default: //-- convert to boolean
                    if ( !result.booleanValue() ) {
                        nodesToRemove.add( tmpContext.getNode() );
                    }
                    break;
                }
            }
            
            for (nIdx = 0; nIdx < nodesToRemove.size() ; nIdx++) {
                nodes.remove (nodesToRemove.item(nIdx));
            }
            nodesToRemove.clear();
            
            
            expr = expr.getNext();
        }
    } // evaluateTestExpr
    
    /**
     * Returns true if this PathComponent is a FilterExpr.
     * Note that if this method returns true, then a call to 
     * #isLocationStep must return false.
     *
     * @return true if this PathComponent is a FilterExpr
     * @see isLocationStep
     */
    public abstract boolean isFilterExpr();
    
    /**
     * Returns true if this PathComponent is a LocationStep.
     * Note that if this method returns true, then a call to 
     * #isFilterExpr must return false.
     *
     * @return true if this PathComponent is a LocationStep
     * @see isFilterExpr
     */
    public abstract boolean isLocationStep();
    
    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context.
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this MatchExpr
     * @exception XPathException when an error occurs during
     * evaluation
     */
    public abstract boolean matches( XPathNode node, XPathContext context )
        throws XPathException;

    
    /**
     * Removes the given predicate from this PathComponent.
     *
     * @param expr the Expr to remove from the predicate list
     * @return the removed PredicateExpr or null if the Expr did not exist
     * in the list of predicates for this PathComponent
     */
    private PredicateExpr removePredicate( PredicateExpr expr )
        throws XPathException
    {
        if ( _predicate == null )
            return null;
        if ( _predicate == expr )
            _predicate = (PredicateExprImpl)_predicate.getNext();
        else {
            PredicateExprImpl next = (PredicateExprImpl)_predicate.getNext();
            PredicateExprImpl prev = _predicate;
            while ( next != null ) {
                if ( next == expr ) {
                    prev.setNext( (PredicateExprImpl) next.getNext() );
                    return next;
                }
                prev = next;
                next = (PredicateExprImpl) next.getNext();
            }
        }
        return null;
    } //-- removePredicate
    
    /**
     * Returns the String representation of this PathComponent
     *
     * @return the String representation of this PathComponent
     */
    public String toString()
    {
        if ( _predicate == null )
            return "";
        else
            return _predicate.toString();
    } //-- toString


} //-- FilterBase
