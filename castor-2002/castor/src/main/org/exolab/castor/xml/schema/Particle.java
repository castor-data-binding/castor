/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
*/
 
package org.exolab.castor.xml.schema;


/**
 * An abstract class that represents an XML Schema Particle
 * This is not an entirely true representation of how
 * XML Schema depicts a "particle" since this representation
 * of a particle does not hold the "term" component (element, all,
 * choice, sequence, group, any) but rather the "term"
 * extends this class.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a> 
**/
public abstract class Particle extends Annotated {
    
    /**
     * The maximum occurance
    **/
    private int _maxOccurs = -1;
    
    /**
     * The minimum occurance
    **/
    private int _minOccurs  = 1;
    
    /**
     * Default Constructor, uses a default minimum occurance
     * of 1, and a default unbounded maximum occurance
    **/
    protected Particle() {
        super();
    } //-- Particle

    /**
     * Constructor for Particle which allows setting the values for
     * maximum and minimum occurances
     *
     * @param minOccurs the minimum occurance
     * @param maxOccurs the maximum occurance
    **/
    protected Particle(int minOccurs, int maxOccurs) {
        super();
        setMinOccurs(minOccurs);
        setMaxOccurs(maxOccurs);
    } //-- Particle
    
    /**
     * Returns the maximum number of occurances that this CMParticle 
     * may appear
     * @return the maximum number of occurances that this CMParticle
     * may appear.
     * A non positive (n < 1) value indicates that the 
     * value is unspecified (ie. unbounded).
    **/
    public final int getMaxOccurs() {
        return _maxOccurs;
    } //-- getMaxOccurs

    /**
     * Returns the minimum number of occurances that this CMParticle
     * must appear
     * @return the minimum number of occurances that this CMParticle
     * must appear
     * A negative (n < 0) value indicates that the value is unspecified.
    **/
    public final int getMinOccurs() {
        return _minOccurs;
    } //-- getMinOccurs
    
    /**
     * Sets the maximum number of occurances that this CMParticle must
     * appear within it's parent context
     * @param maxOccurs the maximum number of occurances that this
     * CMParticle may appear within it's parent context
    **/
    public final void setMaxOccurs(int maxOccurs) {
        _maxOccurs = maxOccurs;
    } //-- setMaxOccurs

    /**
     * Sets the minimum number of occurances that this CMParticle must
     * appear within it's parent context
     * @param minOccurs the number of occurances that this 
     * CMParticle must
     * appeae within it's parent context
    **/
    public final void setMinOccurs(int minOccurs) {
        _minOccurs = minOccurs;
    } //-- setMinOccurs
    
} //-- CMParticle