/*
 * Copyright (C) 2005, Intalio Inc.
 *
 * The program(s) herein may be used and/or copied only with the
 * written permission of Intalio Inc. or in accordance with the terms
 * and conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *
 * $Id$
 * 
 * Created on Jan 12, 2005 by kvisco
 *
 */
package org.exolab.castor.util;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * A simple command line utility that parses Castor's change log file and outputs the
 * file in an XML format, a Castor mapping file may be used to change the XML format
 * that is output. Actually I haven't enabled any command line options yet, so the
 * mapping file can't be specified at the moment. 
 * 
 * @author <a href="mailto:kvisco@intalio.com">kvisco</a>
 * @revision $Revision$ $Date$
 */
public class ChangeLog2XML {

    /**
     * The default filename for the CHANGELOG file
     */
    private static final String DEFAULT_FILE = "CHANGELOG";
    
    private static final String DEFAULT_OUTPUT = "changelog.xml";
    
    
    private static final String L_PAREN             = "(";
    private static final String R_PAREN             = ")";
    private static final String DETAILS_TOKEN       = "Details:";
    private static final String VERSION_SEPARATOR   = "---";
    private static final String VERSION_TOKEN       = "Version";
    
    
    /**
     * Creates a new instance of ChangeLog2XML
     *
     */
    public ChangeLog2XML() {
        super();
    }
    
    /**
     * The method which does the parsing of the CHANGELOG file
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ParseException
     */
    private Changelog parse(File file)     
        throws FileNotFoundException, IOException, ParseException
    {
    	if (!file.exists()) {
    		throw new IllegalArgumentException("The argument 'file' must not be null!");
        }
        
        
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = null;
        boolean checkForVersion = false;
        boolean inEntry = false;
        String prevLine = null;
        StringBuffer buffer = null;
        String details = null;
        Changelog changelog = new Changelog();
        Release release = null;
        
        while ((line = reader.readLine()) != null)  {
            
            //-- check for version separator?
            if (checkForVersion) {
                checkForVersion = false;
            	if (line.startsWith(VERSION_SEPARATOR)) {
                   release = new Release();
                   String version = prevLine;
                   //-- strip off 'Version'
                   version = version.substring(VERSION_TOKEN.length()).trim();
                   release.setVersion(version);
                   changelog.addRelease(release);
                   continue;
                }
              inEntry = true;
              buffer = new StringBuffer();
              buffer.append(prevLine.trim());
            }
            
            //-- empty line is either end of entry or ignorable
            //-- whitespace
            if (line.length() == 0) {
                if (inEntry) {
                	Entry entry = new Entry();
                    String value = buffer.toString();
                    
                    /* cleanup of entry values*/
                    
                    //-- strip off old entry designator "-";
                    if (value.startsWith("-")) {
                        value = value.substring(1);
                    }
                    int idx = value.indexOf(':');
                    //-- Strip off component type (ie XML, JDO, ALL, etc);
                    if ((idx >= 0) && (idx < 10)) {
                    	String component = value.substring(0, idx);
                        entry.setComponent(component);
                        value = value.substring(idx+1);
                    }
                    
                    
                    if (details != null) {
                    	entry.setDetails(details);
                    }
                    
                    //-- check for committer and date
                    int lastLength = prevLine.length();
                    if (prevLine.startsWith(L_PAREN) && prevLine.endsWith(R_PAREN)) {
                    	prevLine = prevLine.substring(1, lastLength-1);
                        idx = prevLine.indexOf('-');
                        if (idx >= 0) {
                        	entry.setCommitter(prevLine.substring(0, idx).trim());
                            entry.setDate(prevLine.substring(idx+1).trim());
                            //-- cleanup value
                            value = value.substring(0, value.length()-lastLength);
                        }
                    }
                    
                    entry.setValue(value.trim());
                    
                    release.addEntry(entry);
                    inEntry = false;
                    details = null;
                }
                continue;
            }
            
            if (!inEntry) {
                if (line.startsWith(VERSION_TOKEN)) {
                    checkForVersion = true;
                    prevLine = line;
                    continue;
                }
                inEntry = true;
                buffer = new StringBuffer();
                buffer.append(line.trim());
            }
            else {
                
                line = line.trim();
                
                if (line.startsWith(DETAILS_TOKEN)) {
                	details = line.substring(DETAILS_TOKEN.length() + 1);
                }
                else {
                    if (buffer.length() > 0) {
                    	buffer.append(' ');
                    }
                	buffer.append(line);
                }
                prevLine = line;
            }
            
            //-- if we make it here we have valid text
            
        }
        
        fileReader.close();
        
        
        return changelog;
        
    }
    
    public static void main(String[] args) {
        //TODO: add CommandLineOptions
        // options needed
        // 1. filename/path of CHANGELOG
        // 2. mapping file for customization
        // 3. output file name
        
        
        ChangeLog2XML parser = new ChangeLog2XML();
        
        try {
            File file = new File(DEFAULT_FILE);
        	Changelog changelog = parser.parse(file);
            
            file = new File(DEFAULT_OUTPUT);
            FileWriter writer = new FileWriter(file);
            LocalConfiguration config = LocalConfiguration.getInstance();
            config.getProperties().setProperty(Configuration.Property.Indent, "true");
            Marshaller marshaller = new Marshaller(writer);
            marshaller.setRootElement("changelog");
            marshaller.setSuppressXSIType(true);
            marshaller.marshal(changelog);
            
        }
        catch(Exception ex) {
        	ex.printStackTrace();
        }
    }
    
    /* 
       Inner JavaBean classes, these classes must follow the basic JavaBean
       guidelines so that the Castor Introspector will introspect them
       properly  
       
     */
    
    public static class Changelog {
        
        private ArrayList _releases = new ArrayList();
        
        public Changelog() {
        	super();
        }
        
        public void addRelease(Release release) {
            if (release != null) {
            	_releases.add(release);
            }
        }
        
        public Release[] getRelease() {
            Release[] relArray = new Release[_releases.size()];
            _releases.toArray(relArray);
            return relArray;
        }
    }
    
    public static class Release {
        private String _version = null;
        private ArrayList _entries = new ArrayList();
        
        public Release() {
        	super();
        }
        
        public void addEntry(Entry entry) {
        	if (entry != null) {
               _entries.add(entry);      
            }
        }
        
        public Entry[] getEntry() {
        	Entry[] entryArray = new Entry[_entries.size()];
            _entries.toArray(entryArray);
            return entryArray;
        }
        public String getVersion() {
        	return _version;
        }
        
        public void setVersion(String version) {
        	_version = version;
        }
    }
    
    public static class Entry {
        
        private String _component = null;
        private String _details   = null;
        private String _value     = null;
        private String _committer = null;
        private String _date      = null;
        
        public Entry() {
        	super();
        }
        
        public String getCommitter() {
            return _committer;
        }
        
        public String getComponent() {
        	return _component;
        }
        
        public String getDate() {
        	return _date;
        }
        
        public String getDetails() {
            return _details;
        }
        
        public String getValue() {
           return _value;
        }
        
        public void setCommitter(String committer) {
        	_committer = committer;
        }
        
        public void setComponent(String component) {
        	_component = component;
        }
        
        public void setDate(String date) {
        	_date = date;
        }
        
        public void setDetails(String details) {
        	_details = details;
        }
        
        public void setValue(String value) {
        	_value = value;
        }
    }
}
