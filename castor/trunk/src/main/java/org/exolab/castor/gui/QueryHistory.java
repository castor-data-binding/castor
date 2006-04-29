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
 * $$
 */


package org.exolab.castor.gui;

import java.util.Vector;


public class QueryHistory implements java.io.Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 235997211123063614L;

    private  Vector querys = new Vector();
    private int currentPos=0;
    private  int maxsize=20;

    public Vector getQuerys(){
        return querys;

    }

    public void setQuerys(Vector _querys){
        querys=_querys;
        currentPos = querys.size()-1;
    }

    public int getMaxHistorySize(){
        return maxsize;
    }

    public void setMaxHistorySize(int newSize){
         maxsize=newSize;
    }

    public void moveback(){
        if(currentPos >0)
            currentPos--;
    }

    public void moveforward(){
        if(currentPos < querys.size()-1)
            currentPos++;
    }

    public String GetCurrentQuery(){
      if(querys.isEmpty()) return "";
      return (String)querys.elementAt(currentPos);
    }

    public void addQuery(String newQuery){
        if(querys.contains(newQuery)){
            System.out.println("schon drin");
            return;
        }

        querys.add(newQuery);


        if(querys.size() > maxsize)
            querys.removeElementAt(0);

        currentPos=querys.size()-1;
    }

    // Get to avoid mashaling
    public String getNextQuery(){
        this.moveforward();
        return this.GetCurrentQuery();
    }

    public String getPreviousQuery(){
        this.moveback();
        return this.GetCurrentQuery();
    }
}