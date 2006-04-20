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
 * The Original Code is XSL:P XSLT processor.
 * 
 * The Initial Developer of the Original Code is Keith Visco.
 * Portions created by Keith Visco (C) 1998-2001 Keith Visco.
 * All Rights Reserved..
 *
 * Contributor(s): 
 * Keith Visco, kvisco@ziplink.net
 *    -- original author. 
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

 
package org.exolab.adaptx.util;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A utility class for generating command line options
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class CommandLineOptions {

    List flags = null;
    Hashtable optionInfo = null;
    
    PrintWriter errorWriter = null;
    
    /**
     * The command used to invoke the application
    **/
    private String _invokeCmd = "";
    
    public CommandLineOptions() {
        flags       = new List();
        optionInfo  = new Hashtable();
        errorWriter = new PrintWriter(System.out);
    } //-- CommandLineOptions
    
    /**
     * Adds the flag to list of available command line options
     * @param flag the flag to add as an available command line option
    **/
    public void addFlag(String flag) {
        addFlag(flag, null, null);
    } //-- addFlag
    
    /**
     * Adds the flag to list of available command line options
     * @param flag the flag to add as an available command line option
     * @param comment a comment for the flag
    **/
    public void addFlag(String flag, String comment) {
        addFlag(flag, null, comment);
    } //-- addFlag
    
    /**
     * Adds the flag to list of available command line options
     * @param flag the flag to add as an available command line option
     * @param comment a comment for the flag
     * @param usageText the text that appears after the flag in the
     * usage string
    **/
    public void addFlag(String flag, String usageText, String comment) {
        if (flag == null) return;
        flags.add(flag);
        
        CmdLineOption opt = new CmdLineOption(flag);
        opt.setComment(comment);
        opt.setUsageText(usageText);
        optionInfo.put(flag, opt);
    } //-- addFlag
    
	/**
	 * parses the arguments into a hashtable with the proper flag
	 * as the key
	**/
	public Properties getOptions(String[] args) {
	    Properties options = new Properties();
	    String flag = null;
	    for (int i = 0; i < args.length; i++) {
	        
	        if (args[i].startsWith("-")) {
	            	
	            // clean up previous flag
	            if (flag != null) {
	                options.put(flag,args[i]);
	                options.put(new Integer(i),args[i]);
	            }
	            // get next flag
	            flag = args[i].substring(1);
	            
	            //-- check full flag, otherwise try to find
	            //-- flag within string
	            if (!flags.contains(flag)) {
	                int idx = 1;
	                while(idx <= flag.length()) {
	                    if (flags.contains(flag.substring(0,idx))) {
	                        if (idx < flag.length()) {
	                            options.put(flag.substring(0,idx),
	                                flag.substring(idx));
	                            break;
	                        }
	                    }
	                    else if (idx == flag.length()) {
	                        errorWriter.print("invalid option -");
	                        errorWriter.println(flag);
	                        printUsage(errorWriter);
	                    }
	                    ++idx;
	                }// end while
	            }
	            
	        }// if flag
	        else {
	            // Store both flag key and number key
	            if (flag != null) options.put(flag,args[i]);
	            options.put(new Integer(i),args[i]);
	            flag = null;
	        }
	        
	    }// end for
	    if (flag != null) options.put(flag, "no value");
	    return options;
	} //-- getOptions
	
	/**
	 * Sets a comment for the flag
	 * @param flag the flag to set the comment for
	 * @param comment the comment to use when printing help for the given flag
	**/
	public void setComment(String flag, String comment) {
	    if (flag == null) return;
	    CmdLineOption opt = (CmdLineOption)optionInfo.get(flag);
	    if (opt != null) opt.setComment(comment);
	} //-- setComment
	
	/**
	 * Sets whether or not a given flag is optional
	 * @param flag the flag to set optionality for 
	 * @param optional the boolean indicating the optionality for the given flag
	**/
	public void setOptional(String flag, boolean optional) {
	    if (flag == null) return;
	    CmdLineOption opt = (CmdLineOption)optionInfo.get(flag);
	    if (opt != null) opt.setOptional(optional);
	} //-- setOptional
	
	/**
	 * Sets the command used to invoke the application
	 * @param invokeCommand the command used to invoke the application
	**/
	public void setInvokeCommand(String invokeCommand) {
	    if (invokeCommand == null) 
	        this._invokeCmd = "";
	    else 
    	    this._invokeCmd = invokeCommand;
	} //-- setInvokeCommand
	
	/**
	 * Sets the text to print after the flag when printing the usage line
	 * @param flag the flag to set the usage info for
	 * @param usage the usage text
	**/
	public void setUsageInfo(String flag, String usage) {
	    if (flag == null) return;
	    CmdLineOption opt = (CmdLineOption)optionInfo.get(flag);
	    if (opt != null) opt.setUsageText(usage);
	} //-- setUsageInfo

    public void printUsage(PrintWriter pw) {
        pw.println();
        pw.print("usage: " + _invokeCmd + " ");
        
        for (int i = 0; i < flags.size(); i++) {
            String flag = (String) flags.get(i);
            CmdLineOption opt = (CmdLineOption)optionInfo.get(flag);
            if (opt.getOptional()) pw.print(" [-");
            else pw.print(" -");
            pw.print(flag);
            String usage = opt.getUsageText();
            if (usage != null) {
                pw.print(' ');
                pw.print(usage);
            }
            if (opt.getOptional()) pw.print(']');
            
        }
        pw.println();
        pw.flush();
    } //-- printUsage
    
    public void printHelp(PrintWriter pw) {
        //-- add later
    }
    
} //-- CommandLineOptions

class CmdLineOption {
    
    boolean optional = false;
    String usageText = null;
    String comment = null;
    String flag = null;
    
    /**
     * Creates a new CmdLineOption
     * @param the flag associated with this command line option
    **/
    CmdLineOption(String flag) {
        super();
        this.flag = flag;
    } //-- CmdLineOption
    
    /**
     * Returns the flag associated with this command line option
     * @return the flag associated with this command line option
    **/
    public String getFlag() {
        return this.flag;
    } //-- getFlag
    
    /**
     * Returns whether or not this CmdLineOption is optional or not
     * @return true if this CmdLineOption is optional, otherwise false
    **/
    public boolean getOptional() {
        return this.optional;
    } //-- getOptional
    
	/**
	 * Returns the comment for this option
	 * @return the comment for this command line option
	**/
	public String getComment() {
	    return this.comment;
	} //-- getComment
	
	/**
	 * Returns the text to print after the flag when printing the usage line
	 * @return the text to print after the flag when printing the usage line
	**/
	public String getUsageText() {
	    return this.usageText;
	} //-- getUsageText
	
    
    /**
     * Sets whether or not this CmdLineOption is optional or not
     * @param optional the flag indicating whether or not this CmdLineOption
     * is optional
    **/
    public void setOptional(boolean optional) {
        this.optional = optional;
    } //-- setOptional
    
	/**
	 * Sets a comment for the flag
	 * @param comment the comment to use when printing help for the given flag
	**/
	public void setComment(String comment) {
	    this.comment = comment;
	} //-- setComment
	
	/**
	 * Sets the text to print after the flag when printing the usage line
	 * @param usageText the usage text
	**/
	public void setUsageText(String usageText) {
	    this.usageText = usageText;
	} //-- setUsageText
    
} //-- CmdLineOption

