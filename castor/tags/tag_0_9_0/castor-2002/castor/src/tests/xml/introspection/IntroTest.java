/**
 * Copyright (C) 2000, Intalio Inc.
 *
 * The program(s) herein may be used and/or copied only with the
 * written permission of Intalio Inc. or in accordance with the terms
 * and conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *
 * $Id: IntroTest.java
 */



//package xml.introspection;

/**
 *  This tests do not belong to a package right now because of a problem 
 *  with the Castor introspection feature and objects inside packages...
 *  To compile and run these tests, just use test.bat
 */

import java.io.*;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.*;
import junit.framework.*;
import junit.extensions.*;
import org.xml.sax.InputSource;

import com.intalio.XMLDiff.*;

/**
 * This class is used to test the introspection feature of Castor.
 * Objects representing a collection are unmarshalled, marshalled.
 * All the types supported by Castor are used:
 * Array, Vector, Hashtable, ArrayList and HashSet  
 *
 * 
 */


 public class IntroTest extends TestCase {
   
    XMLItemListGenerator XMLgen = new XMLItemListGenerator();
    XMLDiff _xmldiff = new XMLDiff();
    
    rootArray _rootarray = new rootArray();
    rootArrayD _rootarrayd = new rootArrayD();
    rootVector _rootvector = new rootVector();
    rootVectorD _rootvectord = new rootVectorD();
    rootHashtable _roothashtable = new rootHashtable();
    rootArrayList _rootarraylist = new rootArrayList();
    rootHashSet _roothashset = new rootHashSet();
    
    /**
     *  This method uses the class XMLGen to build
     *  input.xml . The name of the rootname is
     *  given in argument of the method. The boolean
     *  xsi is used to tell if xsi attribut should be 
     *  used or not in the input files. The generated
     *  file input.xml is then compared to output.xml,
     *  result from marshalling process, using XMLDiff.
     */
     
    public void compareResults ( String rootname, boolean xsi )
    {
             
             String errmsg;
             
             XMLgen.generator(rootname,xsi);
             
             if (xsi)
                errmsg="Working with a rootname=\""+rootname+"\" with xsi attributes";
             else
                errmsg="Working with a rootname=\""+rootname+"\" without xsi attributes";
             
             assert(errmsg+", the result of the marshalling process is different from what is expected",_xmldiff.compareURIs("input.xml","output.xml"));  
    }
   
    /**
     *  In specific cases, order of the elements is not
     *  an issue. Here, we always work with files 
     *  containing only a collection of 2 items, so there 
     *  are only 2 possibilities, 2 comparisons to do.
     *  If the first comparison is not succesfull, a second
     *  comparison is made using the method comparerResult
     */
    public void compareResultsInv ( String rootname, boolean xsi )
    {
            
             XMLgen.generatorinv(rootname,xsi);
             
             if (!_xmldiff.compareURIs("input.xml","output.xml"))
                compareResults( rootname, xsi );
    }
        
   public IntroTest(String name) {
        super(name);
    }
    
    
        /**
         *  All the objects which will be marshalled or
         *  which will be useful to check if the results
         *  from the unmarshaller are good, are created
         *  here.
         */
        
        public void setUp()
        {
                Item[] items = new Item[2];
                Item it1 = new Item("hello", 34);
                Item it2 = new Item("test", 366);
                items[0] = it1;
                items[1] = it2;
                
                _rootarray.setItem(items);
                _rootarrayd.item=items;
                
                java.util.Vector _vectoritems = new java.util.Vector();
                _vectoritems.add(it1);
                _vectoritems.add(it2);
             
                _rootvector.setItem(_vectoritems);
                _rootvectord.item=_vectoritems;
                
                java.util.Hashtable _hashtableitems = new java.util.Hashtable();
                _hashtableitems.put(it1,it1);
                _hashtableitems.put(it2,it2);
                _roothashtable.setItem(_hashtableitems);
                
                java.util.ArrayList _arraylistitems = new java.util.ArrayList();
                _arraylistitems.add(it1);
                _arraylistitems.add(it2);
                _rootarraylist.setItem(_arraylistitems);
                
                java.util.HashSet _hashsetitems = new java.util.HashSet();
                _hashsetitems.add(it1);
                _hashsetitems.add(it2);
                _roothashset.setItem(_hashsetitems);
        
        }
        
    /**
     *  This method unmarshall a file and then check 
     *  if the result is valid. The object used to 
     *  represent a collection is a simple array. XSI
     *  attributes are present in the input xml file.
     *  The root object used has get/set methods
     */
    
    public void testIntroTC01ArrayXSIUnmarshal() {
        
        try {

            
            Unmarshaller unmar;
         
            unmar = new Unmarshaller(rootArray.class);
  
            XMLgen.generator("root-array",true);
            rootArray newRoot = (rootArray)unmar.unmarshal( new FileReader( "input.xml"));
            
            assert("these two rootArray objects are not equals", newRoot.equals(_rootarray));
            
            
        } catch  (Exception e) {
            
            fail("An error has occured: "+e);
        }
    }
    
    /**
     *  This method unmarshall a file and then check 
     *  if the result is valid. The object used to 
     *  represent a collection is a simple array. XSI
     *  attributes are not present in the input xml file.
     *  The root object used has get/set methods
     */
    
    public void testIntroTC02ArrayNoXSIUnmarshal() {
        
        try {

            
            Unmarshaller unmar;
         
            unmar = new Unmarshaller(rootArray.class);
  
            XMLgen.generator("root-array",false);
            rootArray newRoot = (rootArray)unmar.unmarshal( new FileReader( "input.xml"));
            
            assert("these two rootArray objects are not equals", newRoot.equals(_rootarray));
            
            
        } catch  (Exception e) {
            
            fail("An error has occured: "+e);
        }
    }
    
    /**
     *  This method marshall an object containing  
     *  an array. The object marshalled has get/set 
     *  methods. The result of the marshalling process
     *  is tested using the method compareresult.
     *
     */
    
    public void testIntroTC03ArrayMarshal() {
        try {
             
             FileWriter writer = new FileWriter("output.xml");
             Marshaller marshaller = new Marshaller(writer);
             marshaller.marshal(_rootarray);
             
             compareResults("root-array",true);
            
        } catch  (Exception e) {
            fail("An error has occured: "+e);
        }
    }
    
    /**
     *  NOT YET SUPPORTED
     *  This method unmarshall a file and then check 
     *  if the result is valid. The object used to 
     *  represent a collection is a simple array. XSI
     *  attributes are present in the input xml file.
     *  The root object used has no get/set methods and
     *  Castor should access directly to the attribute
     *  of the object.
     */
    
      
        public void _testIntroTC04ArrayDUnmarshal() {
        
        try {

            Unmarshaller unmar;
         
            unmar = new Unmarshaller(rootArrayD.class);
  
            XMLgen.generator("root-array-d",false);
            rootArrayD newRoot = (rootArrayD)unmar.unmarshal( new FileReader( "input.xml"));
            
            assert("these two rootArrayD objects are not equals", newRoot.equals(_rootarrayd));
            
        } catch  (Exception e) {
            
            fail("An error has occured: "+e);
        }
    }
    
    /**
     *  This method marshall an object containing  
     *  an array. The object marshalled has no get/set 
     *  methods. Castor is expected to use a direct acces
     *  to the array. The result of the marshalling process
     *  is tested using the method compareresult.
     *
     */  
    
    public void testIntroTC05ArrayDMarshal() {
        try {
             
             FileWriter writer = new FileWriter("output.xml");
             Marshaller marshaller = new Marshaller(writer);
             marshaller.marshal(_rootarrayd);
             
             compareResults("root-array-d",false);
            
        } catch  (Exception e) {
            fail("An error has occured: "+e);
        }
    }

    /**
     *  This method unmarshall a file and then check 
     *  if the result is valid. The object used to 
     *  represent a collection is a vector. XSI
     *  attributes are present in the input xml file.
     *  The root object used has get/set methods
     */

    
        public void testIntroTC06VectorUnmarshal() {
        
        try {
            
                Unmarshaller unmar;
            
            unmar = new Unmarshaller(rootVector.class);
            
            XMLgen.generator("root-vector",true);
            rootVector newRoot = (rootVector)unmar.unmarshal(new InputSource(new FileReader("input.xml")));

            assert("these two rootVector objects are not equals", newRoot.equals(_rootvector));
            
        } catch  (Exception e) {
            
            fail("An error has occured: "+e);
        }
    }


    /**
     *  This method marshall an object containing  
     *  a vector. get/set methods are used to access the
     *  vector. The result of the marshalling process
     *  is tested using the method compareresult.
     *
     */  
    
    public void testIntroTC07VectorMarshal() {
        try {
             
             FileWriter writer = new FileWriter("output.xml");
             Marshaller marshaller = new Marshaller(writer);
             marshaller.marshal(_rootvector);
             
             compareResults("root-vector",true);
             
            
        } catch  (Exception e) {
            fail("An error has occured: "+e);
        }
    }

    /**
     *  This method unmarshall a file and then check 
     *  if the result is valid. The object used to 
     *  represent a collection is a vector. XSI
     *  attributes are present in the input xml file.
     *  The root object used has no get/set methods
     *  and Castor will access directly to the vector
     */
   
    public void testIntroTC08VectorDUnmarshal() {
        
        try {
                Unmarshaller unmar;
            
            unmar = new Unmarshaller(rootVectorD.class);
            
            XMLgen.generator("root-vector-d",true);
            rootVectorD newRoot = (rootVectorD)unmar.unmarshal(new InputSource(new FileReader("input.xml")));

            assert("these two rootVectorD objects are not equals", newRoot.equals(_rootvectord));
           
        } catch  (Exception e) {
            
            fail("An error has occured: "+e);
        }
    }
    
    /**
     *  This method marshall an object containing  
     *  a vector. The object marshalled has no get/set 
     *  methods. Castor is expected to use a direct acces
     *  to the array. The result of the marshalling process
     *  is tested using the method compareresult.
     *
     */  

    public void testIntroTC09VectorDMarshal() {
        try {
             
             FileWriter writer = new FileWriter("output.xml");
             Marshaller marshaller = new Marshaller(writer);
             marshaller.marshal(_rootvectord);
             
             compareResults("root-vector-d",true);
            
        } catch  (Exception e) {
            fail("An error has occured: "+e);
        }
    }

        
        
    /**
     *  NOT YET SUPPORTED
     *  This method unmarshall a file and then check 
     *  if the result is valid. The object used to 
     *  represent a collection is an Hashtable. XSI
     *  attributes are present in the input xml file.
     */
        
        public void _testIntroTC10HashtableUnmarshal() {
        
        try {

                Unmarshaller unmar;
           
            unmar = new Unmarshaller(rootHashtable.class);
            
            XMLgen.generator("root-hashtable",true);
            _roothashtable.print();
            rootHashtable newRoot = (rootHashtable)unmar.unmarshal(new InputSource(new FileReader("input.xml")));
            newRoot.print();
            _roothashtable.print();
            
        } catch  (Exception e) {
            e.printStackTrace();
            fail("An error has occured: "+e);
        }
    }

    /**
     *  This method marshall an object containing  
     *  a hashtable. The object marshalled has get/set 
     *  methods. 
     *  Since there is no order in a Hashtable, the
     *  result of the marshalling is tested with the
     *  method compareResultsInv which performs a double
     *  comparison.
     */  
    
    public void testIntroTC11HashtableMarshal() {
        try {
             
             FileWriter writer = new FileWriter("output.xml");
             Marshaller marshaller = new Marshaller(writer);
             marshaller.marshal(_roothashtable);
             
             
             compareResultsInv("root-hashtable",true);
             
            
        } catch  (Exception e) {
            fail("An error has occured: "+e);
        }
    }
    
    /**
     *  NOT YET SUPPORTED
     *  This method unmarshall a file and then check 
     *  if the result is valid. The object used to 
     *  represent a collection is an Hashtable. XSI
     *  attributes are present in the input xml file.
     */

        public void _testIntroTC12ArrayListUnmarshal() {
        
        try {
                Unmarshaller unmar;

            unmar = new Unmarshaller(rootArrayList.class);
            
            XMLgen.generator("root-array-list",true);
            _rootarraylist.print();
            rootArrayList newRoot = (rootArrayList)unmar.unmarshal(new InputSource(new FileReader("input.xml")));
            newRoot.print();
            _rootarraylist.print();
            
        } catch  (Exception e) {
            e.printStackTrace();
            fail("An error has occured: "+e);
        }
    }
    

    /**
     *  This method marshall an object containing  
     *  an ArrayList. The object marshalled has get/set 
     *  methods. 
     *  Since there is no order in an ArrayList, the
     *  result of the marshalling is tested with the
     *  method compareResultsInv which performs a double
     *  comparison.
     */  


    public void testIntroTC13ArrayListMarshal() {
        try {
             
             
             FileWriter writer = new FileWriter("output.xml");
             Marshaller marshaller = new Marshaller(writer);
             marshaller.marshal(_rootarraylist);
             
             
             compareResults("root-array-list",true);
             
            
        } catch  (Exception e) {
            fail("An error has occured: "+e);
        }
    }

    /**
     *  NOT YET SUPPORTED
     *  This method unmarshall a file and then check 
     *  if the result is valid. The object used to 
     *  represent a collection is an HashSet. XSI
     *  attributes are present in the input xml file.
     */

    public void _testIntroTC14HashSetUnmarshal() {
        
        try {
            
                Unmarshaller unmar;
           
            unmar = new Unmarshaller(rootHashSet.class);
            
            XMLgen.generator("root-hashset",true);
            _roothashset.print();
            rootHashSet newRoot = (rootHashSet)unmar.unmarshal(new InputSource(new FileReader("input.xml")));
            newRoot.print();
            _roothashset.print();
            
            
        } catch  (Exception e) {
            e.printStackTrace();
            fail("An error has occured: "+e);
        }
    }


    /**
     *  This method marshall an object containing  
     *  a HashSet. The object marshalled has get/set 
     *  methods. 
     *  Since there is no order in a HashSet, the
     *  result of the marshalling is tested with the
     *  method compareResultsInv which performs a double
     *  comparison.
     */    
    
    public void testIntroTC15HashSetMarshal() {
        try {
            
             FileWriter writer = new FileWriter("output.xml");
             Marshaller marshaller = new Marshaller(writer);
             marshaller.marshal(_roothashset);
             
             
             compareResultsInv("root-hash-set",true);
             
            
        } catch  (Exception e) {
            fail("An error has occured: "+e);
        }
    }


        
        
 }
 
 