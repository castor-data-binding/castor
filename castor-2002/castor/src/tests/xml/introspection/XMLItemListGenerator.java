//package xml.introspection;

import java.io.*;

/**
 *  This class is used by the tests writen in IntroTest.java.
 *  These tests needs several times the same xml file where
 *  only the name of the root element changes. The method
 *  'generator' takes in argument a string, the name of the
 *  root element, and a boolean, to know if xsi attributes 
 *  are wanted or not. The content of the generated file is
 *  always the same. The method 'generatorinv' just flips
 *  the two items element of the file, otherwise, it does
 *  the same that 'generator'.
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