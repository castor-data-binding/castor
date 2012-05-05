package org.castor.xmlctf.fieldhandler;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.junit.Test;
import org.xml.sax.InputSource;

public class ClonableFieldHandlerTest {

   @Test
   public void unmarshalWithClonableFieldHandler() throws MarshalException, ValidationException, IOException, MappingException {
      Mapping mapping = new Mapping();
      mapping.loadMapping(new InputSource(this.getClass().getResource("mapping.xml").openStream()));

      XMLContext context = new XMLContext();
      context.addMapping(mapping);

      Unmarshaller unmarshaller = context.createUnmarshaller();
      unmarshaller.setClass(Root.class);
      Root root = (Root) unmarshaller.unmarshal(new StringReader("<?xml version=\"1.0\"?><root><date>20040510</date></root>"));

      assertNotNull(root);
      assertNotNull(root.getDate());
      
   }

   @Test
   public void unmarshalWithClonableFieldHandlerOldMethod() throws MarshalException, ValidationException, IOException, MappingException {
      Mapping mapping = new Mapping();
      mapping.loadMapping(new InputSource(this.getClass().getResource("mapping-old.xml").openStream()));

      XMLContext context = new XMLContext();
      context.addMapping(mapping);

      Unmarshaller unmarshaller = context.createUnmarshaller();
      unmarshaller.setClass(Root.class);
      Root root = (Root) unmarshaller.unmarshal(new StringReader("<?xml version=\"1.0\"?><root><date>20040510</date></root>"));

      assertNotNull(root);
      assertNotNull(root.getDate());
      
   }

}
