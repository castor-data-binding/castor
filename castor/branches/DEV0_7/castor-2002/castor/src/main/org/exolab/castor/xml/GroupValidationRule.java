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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class which encapsulates validation rules for groups
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class GroupValidationRule extends ValidationRule {
    
    
    /**
     * The choice type
    **/
    public static final short CHOICE   = 0;
    
    /**
     * The sequence type
    **/
    public static final short SEQUENCE = 1;
    
    /** 
     * The type of grouping to be validated
    **/
    private short type = SEQUENCE;
    
    /**
     * The set of "grouped" validation rules
    **/
    private Vector rules = null;
    
    /**
     * Creates a new Group ValidationRule, using the default type of
     * GroupValidator#SEQUENCE.
    **/
    public GroupValidationRule() {
        this(GroupValidationRule.SEQUENCE);
    } //-- GroupValidator
    
    /**
     * Creates a new GroupValidationRule using the given type
     * @param type the type of grouping to be validated
    **/
    public GroupValidationRule(short type) {
        super("#group");
        this.type = type;
        rules = new Vector(3);
    } //-- GroupValidationRule(short)
    
    /**
     * Adds the given ValidationRule to this GroupValidationRule
     * @param validationRule the validationRule to add
    **/ 
    public void addValidationRule(ValidationRule validationRule) {
        rules.addElement(validationRule);
    } //-- addValidationRule
    
    /**
     * Returns the grouping type for this GroupValidationRule.
     * The group types are SEQUENCE, CHOICE, etc.
     * @return the grouping type for this GroupValidationRule
    **/
    public short getGroupingType() {
        return type;
    } //-- getGroupingType
    
    /**
     * Returns the type of this ValidationRule
     * @return the type of this ValidationRule 
    **/
    public short getType() {
        return ValidationRule.GROUP;
    } //-- getType
    
    /**
     * Returns an enumeration of the ValidationRules contained within
     * this GroupValidationRule
     * @return an enumeration of the ValidationRules contained within
     * this GroupValidationRule
    **/
    public Enumeration rules() {
        return rules.elements();
    } //-- rules
    
    /**
     * Removes the given ValidationRule from this GroupValidationRule
     * @param validationRule the ValidationRule to remove
     * @return true if the ValidationRule was sucessfully removed, 
     * otherwise false.
    **/
    public boolean removeValidationRule(ValidationRule validationRule) {
        return rules.removeElement(validationRule);
    } //-- removeValidationRule
    
} //-- GroupValidationRule
    
