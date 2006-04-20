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

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/**
 * A utility class for generating command line options
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class CommandLineOptions {

    Vector flags = null;
    Hashtable optionInfo = null;
    
    PrintWriter errorWriter = null;
    
    public CommandLineOptions() {
        flags = new Vector();
        optionInfo = new Hashtable();
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
        flags.addElement(flag);
        
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
	                        errorWriter.println(Messages.format("castor.misc.invalidCLIOption",
								    "-" + flag));
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
        pw.print(Messages.message( "castor.misc.CLIUsage" ));
        for (int i = 0; i < flags.size(); i++) {
            String flag = (String) flags.elementAt(i);
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

