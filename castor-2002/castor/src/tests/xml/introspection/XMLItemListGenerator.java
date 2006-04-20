/**
 * Copyright (C) 2000, Intalio Inc.
 *
 * The program(s) herein may be used and/or copied only with the
 * written permission of Intalio Inc. or in accordance with the terms
 * and conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *
 * $Id$
 */


//package xml.introspection;

import java.io.*;

/**
 *  This class is used by the tests writen in {@link IntroTest}.
 *  These tests needs several times the same xml file where
 *  only the name of the root element changes. The method
 *  {@link #generator} takes in argument a string, the name of the
 *  root element, and a boolean, to know if xsi attributes 
 *  are wanted or not. The content of the generated file is
 *  always the same. The method {@link #generatorinv} just flips
 *  the two items element of the file, otherwise, it does
 *  the same that {@link #generator}.
 *  <br>
 * @author <a href="mailto:victoor@intalio.com">Alexandre Victoor</a>
 * @version $Revision$ $Date$ 
 */
public class XMLItemListGenerator {
    FileWriter file;  
    
    
    public static void main(String[] args  ) 
    {
        try {
            XMLItemListGenerator gen_obj = new XMLItemListGenerator();
            
            gen_obj.generator("bidonroot",true);
        }
        catch ( Exception e ){ System.out.println("file error");  }
    }
    
     /**
     *  This method generate an xml file, input.xml, containing two items.
     * @param rootname the name of the root element in the generated xml file
     * @param xsi   a boolean to specifie if xsi attribures will be present in the file
     */
    public void generator( String rootname, boolean xsi )
    {
        try {
            
            file = new FileWriter("input.xml");
            file.write("<?xml version='1.0'?>\n");
            file.write("<"+rootname+">\n");
            file.write("    <item value=\"34\"");
            if (xsi) 
                file.write(" xmlns:xsi=\"http://www.w3.org/2000/10/XMLSchema-instance\" xsi:type=\"java:Item\"");
            file.write(">\n");
            file.write("        <name>hello</name>\n");
            file.write("    </item>\n");
            
            file.write("    <item value=\"366\"");
            if (xsi) 
                file.write(" xmlns:xsi=\"http://www.w3.org/2000/10/XMLSchema-instance\" xsi:type=\"java:Item\"");
            file.write(">\n");
            file.write("        <name>test</name>\n");
            file.write("    </item>\n");
            
            file.write("</"+rootname+">\n");
            
            file.flush();
        }
        catch ( Exception e ){ System.out.println("file error");  }
    
   }

     /**
     *  This method generate an xml file, input.xml, containing two items.
     *  Same thing as {@link #generator} expect that the two items are switched.
     * @param rootname the name of the root element in the generated xml file
     * @param xsi   a boolean to specifie if xsi attribures will be present in the file
     */

   public void generatorinv( String rootname, boolean xsi )
    {
        try {
            
            file = new FileWriter("input.xml");
            file.write("<?xml version='1.0'?>\n");
            file.write("<"+rootname+">\n");
            
            
            file.write("    <item value=\"366\"");
            if (xsi) 
                file.write(" xmlns:xsi=\"http://www.w3.org/2000/10/XMLSchema-instance\" xsi:type=\"java:Item\"");
            file.write(">\n");
            file.write("        <name>test</name>\n");
            file.write("    </item>\n");
            
            file.write("    <item value=\"34\"");
            if (xsi) 
                file.write(" xmlns:xsi=\"http://www.w3.org/2000/10/XMLSchema-instance\" xsi:type=\"java:Item\"");
            file.write(">\n");
            file.write("        <name>hello</name>\n");
            file.write("    </item>\n");
            
            
            file.write("</"+rootname+">\n");
            
            file.flush();
        }
        catch ( Exception e ){ System.out.println("file error");  }
    
   }


}