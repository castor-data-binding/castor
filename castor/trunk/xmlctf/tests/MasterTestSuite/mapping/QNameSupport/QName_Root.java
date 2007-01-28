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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 */


public class QName_Root implements org.exolab.castor.tests.framework.CastorTestable {

    private String _name  = "{http://www.castor.org/Mapping/QName}default value";
    private String _item = "{http://www.fooshop.com/Items}penguins";
    private String _value = "{http://blank.namespace.com}blank";

    //--
    public QName_Root() {}

    //--
    public QName_Root(String name, String value) {
        _name  = name;
        _value = value;
    }

    //--
    public String getItem() {
        return _item;
    }


    public void setItem(String item) {
        _item = item;
    }

    //--
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    //--
    public String getValue() {
        return _value;
    }
    
    public void setValue(String value) {
       _value = value;
    }
    
    //Castor Testable
    public String dumpFields() {
    	String result = "";
    	result += "\nField name:"+_name;
    	result += "\nField item:"+_item;
    	result += "\nField value:"+_value;
    	return result;
    }
    public void randomizeFields()
         throws InstantiationException, IllegalAccessException     
    {}
    
    public boolean equals(Object obj) {
       if (!(obj instanceof QName_Root))
           return false;
       QName_Root temp = (QName_Root)obj;
       return (temp.getName().equals(this.getName())) && 
              (temp.getItem().equals(this.getItem())) && 
              (temp.getValue().equals(this.getValue())); 
    }

}
