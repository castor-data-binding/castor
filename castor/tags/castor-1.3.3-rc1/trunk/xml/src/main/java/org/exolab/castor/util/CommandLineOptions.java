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

import org.castor.core.util.Messages;

/**
 * A utility class for generating command line options.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public class CommandLineOptions {
    private Vector _flags = null;
    private Hashtable _optionInfo = null;
    private PrintWriter _errorWriter = null;
    
    public CommandLineOptions() {
        _flags = new Vector();
        _optionInfo = new Hashtable();
        _errorWriter = new PrintWriter(System.out);
    }
    
    /**
     * Adds the flag to list of available command line options.
     * 
     * @param flag the flag to add as an available command line option.
     */
    public void addFlag(final String flag) {
        addFlag(flag, null, null);
    }
    
    /**
     * Adds the flag to list of available command line options.
     * 
     * @param flag the flag to add as an available command line option.
     * @param comment a comment for the flag.
     */
    public void addFlag(final String flag, final String comment) {
        addFlag(flag, null, comment, false);
    }
    
    /**
     * Adds the flag to list of available command line options.
     * 
     * @param flag the flag to add as an available command line option.
     * @param comment a comment for the flag.
     * @param usageText the text that appears after the flag in the
     *        usage string.
     */
    public void addFlag(final String flag, final String usageText, final String comment) {
        addFlag(flag, usageText, comment, false);
    } //-- addFlag

    /**
     * Adds the flag to list of available command line options.
     * 
     * @param flag The flag to add as an available command line option.
     * @param comment A comment for the flag.
     * @param usageText The text that appears after the flag in the usage string.
     * @param optional When true, indicates that this flag is optional.
     */
    public void addFlag(final String flag, final String usageText, final String comment,
            final boolean optional) {
        if (flag == null) { return; }
        _flags.addElement(flag);
        
        CmdLineOption opt = new CmdLineOption(flag);
        opt.setComment(comment);
        opt.setUsageText(usageText);
        opt.setOptional(optional);
        _optionInfo.put(flag, opt);
    }
    
    /**
     * Parses the arguments into a hashtable with the proper flag as the key.
     */
    public Properties getOptions(final String[] args) {
        Properties options = new Properties();
        String flag = null;
        for (int i = 0; i < args.length; i++) {
            
            if (args[i].startsWith("-")) {
                    
                // clean up previous flag
                if (flag != null) {
                    options.put(flag, args[i]);
                    options.put(new Integer(i), args[i]);
                }
                // get next flag
                flag = args[i].substring(1);
                
                //-- check full flag, otherwise try to find
                //-- flag within string
                if (!_flags.contains(flag)) {
                    int idx = 1;
                    while (idx <= flag.length()) {
                        if (_flags.contains(flag.substring(0, idx))) {
                            if (idx < flag.length()) {
                                options.put(flag.substring(0, idx),
                                    flag.substring(idx));
                                break;
                            }
                        }
                        else if (idx == flag.length()) {
                            _errorWriter.println(Messages.format("misc.invalidCLIOption",
                                    "-" + flag));
                            printUsage(_errorWriter);
                        }
                        ++idx;
                    }
                }
            } else {
                // Store both flag key and number key
                if (flag != null) { options.put(flag, args[i]); }
                options.put(new Integer(i), args[i]);
                flag = null;
            }
            
        }
        if (flag != null) { options.put(flag, "no value"); }
        return options;
    }

    /**
     * Sets a comment for the flag.
     * 
     * @param flag the flag to set the comment for.
     * @param comment the comment to use when printing help for the given flag.
     */
    public void setComment(final String flag, final String comment) {
        if (flag == null) { return; }
        CmdLineOption opt = (CmdLineOption) _optionInfo.get(flag);
        if (opt != null) { opt.setComment(comment); }
    }
    
    /**
     * Sets whether or not a given flag is optional.
     * 
     * @param flag the flag to set optionality for.
     * @param optional the boolean indicating the optionality for the given flag.
     */
    public void setOptional(final String flag, final boolean optional) {
        if (flag == null) { return; }
        CmdLineOption opt = (CmdLineOption) _optionInfo.get(flag);
        if (opt != null) { opt.setOptional(optional); }
    }
    
    /**
     * Sets the text to print after the flag when printing the usage line.
     * 
     * @param flag the flag to set the usage info for.
     * @param usage the usage text.
     */
    public void setUsageInfo(final String flag, final String usage) {
        if (flag == null) { return; }
        CmdLineOption opt = (CmdLineOption) _optionInfo.get(flag);
        if (opt != null) { opt.setUsageText(usage); }
    }

    public void printUsage(final PrintWriter pw) {
        pw.println();
        pw.print(Messages.message("misc.CLIUsage"));
        for (int i = 0; i < _flags.size(); i++) {
            String flag = (String) _flags.elementAt(i);
            CmdLineOption opt = (CmdLineOption) _optionInfo.get(flag);
            if (opt.getOptional()) {
                pw.print(" [-");
            } else {
                pw.print(" -");
            }
            pw.print(flag);
            String usage = opt.getUsageText();
            if (usage != null) {
                pw.print(' ');
                pw.print(usage);
            }
            if (opt.getOptional()) {
                pw.print(']');
            }
        }
        pw.println();
        pw.flush();
    }
    
    public void printHelp(final PrintWriter pw) {
        printUsage(pw);
        pw.println();
        
        if (_flags.size() > 0) {
            pw.println("Flag               Description");
            pw.println("----------------------------------------------");
        }
        for (int i = 0; i < _flags.size(); i++) {
            String flag = (String) _flags.elementAt(i);
            CmdLineOption opt = (CmdLineOption) _optionInfo.get(flag);
            
            pw.print('-');
            pw.print(flag);
            
            pw.print(' ');
            //-- adjust spacing
            int spaces = 17 - flag.length();
            while (spaces > 0) { 
                pw.print(' ');
                --spaces;
            }
            
            pw.print(opt.getComment());
            
            //String usage = opt.getUsageText();
            //if (usage != null) {
            //    pw.print(' ');
            //    pw.print(usage);
            //}
            //if (opt.getOptional()) pw.print(']');
            pw.println();
        }
        pw.println();
        pw.flush();
    }
}

class CmdLineOption {
    private boolean _optional = false;
    private String _usageText = null;
    private String _comment = null;
    private String _flag = null;
    
    /**
     * Creates a new CmdLineOption.
     * 
     * @param flag The flag associated with this command line option.
     */
    CmdLineOption(final String flag) {
        super();
        _flag = flag;
    }
    
    /**
     * Returns the flag associated with this command line option.
     * 
     * @return the flag associated with this command line option.
     */
    public String getFlag() {
        return _flag;
    }
    
    /**
     * Returns whether or not this CmdLineOption is optional or not.
     * 
     * @return true if this CmdLineOption is optional, otherwise false.
    */
    public boolean getOptional() {
        return _optional;
    }
    
    /**
     * Returns the comment for this option.
     * 
     * @return the comment for this command line option.
     */
    public String getComment() {
        return _comment;
    }
    
    /**
     * Returns the text to print after the flag when printing the usage line.
     * 
     * @return the text to print after the flag when printing the usage line.
     */
    public String getUsageText() {
        return _usageText;
    }
    
    /**
     * Sets whether or not this CmdLineOption is optional or not.
     * 
     * @param optional the flag indicating whether or not this CmdLineOption
     *        is optional.
     */
    public void setOptional(final boolean optional) {
        _optional = optional;
    }
    
    /**
     * Sets a comment for the flag.
     * 
     * @param comment the comment to use when printing help for the given flag.
     */
    public void setComment(final String comment) {
        _comment = comment;
    }
    
    /**
     * Sets the text to print after the flag when printing the usage line.
     * 
     * @param usageText the usage text.
     */
    public void setUsageText(final String usageText) {
        _usageText = usageText;
    }
}

