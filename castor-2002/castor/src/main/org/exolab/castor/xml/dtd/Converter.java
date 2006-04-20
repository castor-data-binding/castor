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
 * Copyright 2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.dtd;

import org.xml.sax.InputSource;

import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;

import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.SimpleTypesFactory;
import org.exolab.castor.xml.dtd.parser.*;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;


/**
 * Class containing static top-level methods to parse and convert
 * XML DTD documents to corresponding XML Schema documents.
 * Also contains simple command line interface to read
 * an XML DTD file and create corresponding XML Schema object.
 * @author <a href="mailto:totok@intalio.com">Alexander Totok</a>
 * @version $Revision$ $Date$
 */
public class Converter {

    /**
     * Simple command line interface to read an XML DTD file and create
     * corresponding XML Schema file. Usage: <pre>
     * java org.exolab.castor.xml.dtd.Converter  dtd_file xsd_file [character_encoding]
     *
     * dtd_file: name of the input DTD file
     * xsd_file: name of the output Schema file
     * character_encoding: name of the character encoding,
     *            if not specified, ASCII is chosen</pre>
     * Help message is provided.
     * @throws DTDException if the input DTD document is malformed.
     * @throws SchemaException if Schema object can not be created.
     * @throws MarshalException if an error occured during marshalling.
     * @throws ValidationException if error occured during marshalling.
     */
    public static void main (String args[]) throws IOException,
                                                   DTDException,
                                                   SchemaException,
                                                   MarshalException,
                                                   ValidationException,
                                                   MappingException {
        if ((args.length < 2) || (args.length > 3)) {

            String s = "\nUsage:\n";
            s += "java org.exolab.castor.xml.dtd.Converter dtd_file xsd_file [character_encoding]\n\n";
            s += "dtd_file: name of the input DTD file\n";
            s += "xsd_file: name of the output Schema file\n";
            s += "character_encoding: name of the character encoding,\n";
            s += "                    if not specified, ASCII is chosen\n";
            System.out.println(s);

        } else {

            String encoding;

            //-- choose encoding
            if (args.length == 2) encoding = "US-ASCII";
            else {
               if (args[2].equals("ascii") || args[2].equals("ASCII")
                                           || args[2].equals("us-ascii"))
                   encoding = "US-ASCII";
               else if (args[2].equals("utf-8"))
                   encoding = "UTF-8";
               else if (args[2].equals("utf-16"))
                   encoding = "UTF-16";
               else encoding = args[2];
            } //-- choose encoding

            //-- instantiate input byte stream, associated with the input file
            FileInputStream inputStream = new FileInputStream(args[0]);

            //-- instantiate char reader from input file byte stream
            InputStreamReader reader = new InputStreamReader(inputStream, encoding);

            //-- instantiate output byte stream, associated with the output file
            FileOutputStream outputStream = new FileOutputStream(args[1]);

            //-- instantiate char writer from output file byte stream
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, encoding);

            //-- convert DTD to Schema
            convertDTDtoSchema(reader, writer);

            reader.close();
            writer.close();
        }
    }

    /**
     * Convert DTD document to corresponding XML Schema document.
     * @param reader reader of the input DTD document.
     * @param writer writer to the output Schema document.
     * @throws DTDException if the DTD document is syntactically or semanticly
     * not correct.
     * @throws SchemaException if Schema object can not be created.
     * @throws IOException if Castor's Marshaller object can not be created
     * with the given <tt>writer</tt>.
     * @throws MarshalException if an error occured during marshalling.
     * @throws ValidationException if an error occured during marshalling.
     */
    public static void convertDTDtoSchema(Reader reader, Writer writer)
                                          throws DTDException,
                                                 SchemaException,
                                                 IOException,
                                                 MarshalException,
                                                 ValidationException,
                                                 MappingException {

       //-- parse text of DTD document
       DTDdocument dtd = parseDTD(reader);

       //-- convert DTD document object into its corresponding Schema object
       Schema schema = convertDTDObjectToSchemaObject(dtd);

       //-- marshal Schema object into its corresponding XML document
       marshalSchema(schema, writer);

    } //-- convertDTDtoSchema

    /**
     * Parses text of a DTD document and returns corresponding DTD document object.
     * It is left to constructor of the <tt>reader</tt> to set up
     * character encoding correctly. This means that method
     * <u><font color="blue">read</font></u> of
     * the <tt>reader</tt> is used to get next character, assuming it returns
     * appropriate values.
     * @param reader input char stream reader. It is recommended
     * to use class {@link java.io.InputStreamReader java.io.InputStreamReader}
     * as a <tt>reader</tt>, which allows to set desired character encoding.
     * @return DTD document object corresponding to the input text
     * of a DTD document.
     * @throws DTDException if the DTD document is syntactically or semanticly
     * not correct.
     */
    public static DTDdocument parseDTD (Reader reader) throws
                                               DTDException {
        try {

           InputCharStream charStream;

           //-- instantiate char stream for initial parser from the input reader
           charStream = new InputCharStream(reader);

           //-- instantiate initial parser
           DTDInitialParser initialParser = new DTDInitialParser(charStream);

           //-- get result of initial parsing - DTD document with parameter
           //-- entity references expanded
           String intermedResult = initialParser.Input();

           //-- construct StringReader from the intermediate result of parsing
           StringReader strReader= new StringReader(intermedResult);

           //-- instantiate char stream for main parser
           charStream = new InputCharStream(strReader);

           //-- instantiate main parser
           DTDParser parser = new DTDParser(charStream);

           //-- parse intermediate result by the main parser
           //-- and get corresponding DTD document oblect
           DTDdocument dtd = parser.Input();

           strReader.close();

           //-- return DTD document object
           return dtd;
        }
        catch (TokenMgrError tme) {
           String msg = tme.getMessage();
           throw new DTDException("TokenMgrError" + (msg == null ? "" : ": " + msg));
        }
        catch (ParseException pe) {
           String msg = pe.getMessage();
           throw new DTDException("ParseException" + (msg == null ? "" : ": " + msg));
        }
    } //-- parseDTD

    /**
     * Convert DTD document object to corresponding Schema object.
     * @param dtd input XML DTD document object.
     * @return corresponding XML Schema object.
     * @throws DTDException if the input DTD document is malformed.
     * @throws SchemaException if Schema object can not be created.
     */
    public static Schema convertDTDObjectToSchemaObject (DTDdocument dtd)
                         throws DTDException, SchemaException {

       Schema schema = new Schema();

       String name = dtd.getName();
       if (name != null && !name.equals("")) schema.setName(name);

       //-- convert Notation declarations
       Enumeration dtdNotations = dtd.getNotations();
       Notation notation;

       while (dtdNotations.hasMoreElements()) {
          notation = (Notation)dtdNotations.nextElement();
          //-- do nothing for now as the Castor Schema object model does not
          //-- contain notations set
       } //-- convert Notations declarations

       //-- convert General Entity declarations.
       //-- XML Schema does not provide facilities analogous to General Entity
       //-- declarations in XML DTD, so we convert each General Entity declaration
       //-- to Documentation subelement of XML Schema document annotaion.
       Enumeration dtdGeneralEntities = dtd.getGeneralEntities();
       GeneralEntity ge;
       Annotation annotation = new Annotation();
       Documentation documentation;
       String text;

       while (dtdGeneralEntities.hasMoreElements()) {
          ge = (GeneralEntity)dtdGeneralEntities.nextElement();
          documentation = new Documentation();

          text = "General Entity Declaration";
          documentation.setContent(text);
          documentation.add(ge);
          annotation.addDocumentation(documentation);
       }

       schema.addAnnotation(annotation);
       //-- convert General Entity declarations

       //-- convert Element declarations
       Enumeration dtdElements = dtd.getElements();
       Element dtdElement; //-- DTD Element declaration
       ElementDecl schemaElement; //-- Schema Element declaration

       while (dtdElements.hasMoreElements()) {
          dtdElement = (Element)dtdElements.nextElement();
          schemaElement = convertDTDElementToSchemaElement(dtdElement, schema);
          schema.addElementDecl(schemaElement);
       } //-- convert Element declarations

       return schema;

    } //-- convertDTDObjectToSchemaObject

    /**
     * Convert DTD Element declaration to Schema Element Declaration.
     * @param dtdElement DTD Element declaration.
     * @param schema Schema owning Element declaration.
     * @throws DTDException if the input DTD Element Declaration is malformed.
     * @throws SchemaException if unable to construct return
     * {@link org.exolab.castor.xml.schema.ElementDecl ElementDecl} object from
     * the input DTD {@link org.exolab.castor.xml.dtd.Element Element} object.
     * @return corresponding Schema Element declaration.
     */
    public static ElementDecl convertDTDElementToSchemaElement(Element dtdElement,
                                                               Schema schema)
                                                               throws DTDException,
                                                               SchemaException {

       String name = dtdElement.getName();
       if (name == null || name.equals("")) {
          String err = "Converter: a DTD element has no name.";
          throw new DTDException(err);
       }
       ElementDecl schemaElement = new ElementDecl(schema, name);

       //-- start converting content of the element
       ComplexType complexType = new ComplexType(schema);
       ContentType contentType = null;
       Iterator mixedChildrenIterator = null;
       String elementRef = null; //-- auxiliary
       ElementDecl elem = null;  //-- auxiliary

       if (dtdElement.isEmptyContent()) {

          contentType = ContentType.empty;

       } else if (dtdElement.isAnyContent()) {

          contentType = ContentType.any;

       } else if (dtdElement.isElemOnlyContent()) {

          contentType = ContentType.elemOnly;

          ContentParticle dtdContent = dtdElement.getContent();
          if (dtdContent == null) { //-- content is not specified
             String err = "Converter: element \"" + dtdElement.getName();
             err += "\" has unspecified content.";
             throw new DTDException(err);
          }

          Object content = null;
          try {
             content = convertContentParticle(dtdContent, schema);
          }
          catch (DTDException e) {
             String err = "Converter: content of element \"" + dtdElement.getName();
             err += "\", represented by a Content Particle, is malformed.";
             throw new DTDException(err);
          }

          if (content instanceof ElementDecl) {
             complexType.addElementDecl((ElementDecl)content);
          } else {
             complexType.addGroup((Group)content);
          }

       } else if (dtdElement.isMixedContent()) { //-- dtdElement is of "Mixed" type

          mixedChildrenIterator = dtdElement.getMixedContentChildren();
          if (mixedChildrenIterator.hasNext()) {

             contentType = ContentType.mixed;

             //-- add children to content model of the complexType
             while (mixedChildrenIterator.hasNext()) {
                elementRef = (String)mixedChildrenIterator.next();
                elem = new ElementDecl(schema);
                elem.setReference(elementRef);
                complexType.addElementDecl(elem);
             }

          } else {

             contentType = ContentType.textOnly;

          }
       } else { //-- the type of the element has not been specified
          String err = "Converter: type of element \"" + dtdElement.getName();
          err += "\" has not been specified.";
          throw new DTDException(err);
       }

       complexType.setContentType(contentType);
       //-- finish converting content of the element

       //-- start attributes convertion
       Enumeration dtdAttributes = dtdElement.getAttributes();
       Attribute dtdAttribute;
       AttributeDecl schemaAttribute;

       while (dtdAttributes.hasMoreElements()) {
          dtdAttribute = (Attribute)dtdAttributes.nextElement();
          schemaAttribute = convertAttribute(dtdAttribute, schema);
          complexType.addAttributeDecl(schemaAttribute);
       }
       //-- end attributes convertion

       schemaElement.setType(complexType);
       return schemaElement;

    } //-- convertDTDElementToSchemaElement

    /**
     * Method to convert
     * {@link org.exolab.castor.xml.dtd.ContentParticle ContentParticle} object,
     * used to implement element content in the DTD object model, to the corresponding
     * object in the Schema object model: either
     * {@link org.exolab.castor.xml.schema.Group Group} or
     * {@link org.exolab.castor.xml.schema.ElementDecl ElementDecl}.
     * @param dtdContent input
     * {@link org.exolab.castor.xml.dtd.ContentParticle ContentParticle} object.
     * @return object returned is an instance of either
     * {@link org.exolab.castor.xml.schema.Group Group} class or
     * {@link org.exolab.castor.xml.schema.ElementDecl ElementDecl} class.
     * @throws DTDException if the input ContentParticle is malformed.
     * @throws SchemaException if unable to construct return content object
     * from a given ContentParticle
     */
    public static Object convertContentParticle(ContentParticle dtdContent,
                                                Schema schema)
                         throws DTDException, SchemaException {

       Object returnValue;

       if (dtdContent.isReferenceType()) {

          ElementDecl elem = new ElementDecl(schema);
          elem.setReference(dtdContent.getReference());

          if (dtdContent.isOneOccurance()) {
             elem.setMinOccurs(1);
             elem.setMaxOccurs(1);
          } else if (dtdContent.isOneOrMoreOccurances()) {
             elem.setMinOccurs(1);
             elem.setMaxOccurs(-1);
          } else if (dtdContent.isZeroOrMoreOccurances()) {
             elem.setMinOccurs(0);
             elem.setMaxOccurs(-1);
          } else if (dtdContent.isZeroOrOneOccurance()) {
             elem.setMinOccurs(0);
             elem.setMaxOccurs(1);
          } else {
             //-- a content particle always has "one occurance" default
             //-- occurance specification
          }

          returnValue = elem;

       } else if (dtdContent.isSeqType() || dtdContent.isChoiceType()) {

          Group group = new Group();
          if (dtdContent.isSeqType()) group.setOrder(Order.seq);
          else group.setOrder(Order.choice);

          Enumeration children = dtdContent.getChildren();
          ContentParticle child;
          Object contentObject;

          while(children.hasMoreElements()) {
             child = (ContentParticle)children.nextElement();
             contentObject = convertContentParticle(child, schema);

             if (contentObject instanceof ElementDecl) {
                group.addElementDecl((ElementDecl)contentObject);
             } else {
                group.addGroup((Group)contentObject);
             }
          }

          if (dtdContent.isOneOccurance()) {
             group.setMinOccurs(1);
             group.setMaxOccurs(1);
          } else if (dtdContent.isOneOrMoreOccurances()) {
             group.setMinOccurs(1);
             group.setMaxOccurs(-1);
          } else if (dtdContent.isZeroOrMoreOccurances()) {
             group.setMinOccurs(0);
             group.setMaxOccurs(-1);
          } else if (dtdContent.isZeroOrOneOccurance()) {
             group.setMinOccurs(0);
             group.setMaxOccurs(1);
          } else {
             //-- a content particle always has "one occurance" default
             //-- occurance specification
          }

          returnValue = group;

       } else { //-- type of input DTD Content Particle is not specified
          throw new DTDException();
       }

       return returnValue;

    } //-- convertContentParticle

    /**
     * Convert DTD Attribute declaration to Schema Attribute Declaration.
     * @param dtdAttribute DTD Attribute declaration.
     * @param schema Schema owning Element of this Attribute.
     * @throws DTDException if the input DTD Attribute Declaration is malformed.
     * @return corresponding Schema Attribute declaration.
     */
    public static AttributeDecl convertAttribute(Attribute dtdAttribute,
                                                 Schema schema)
                                                 throws DTDException {

       AttributeDecl schemaAttribute = new AttributeDecl(schema,
                                                         dtdAttribute.getName());

       SimpleType type= null;

       if (dtdAttribute.isStringType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.STRING_TYPE) );
       }
       else if (dtdAttribute.isIDType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.ID_TYPE) );
       }
       else if (dtdAttribute.isIDREFType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.IDREF_TYPE) );
       }
       else if (dtdAttribute.isIDREFSType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.IDREFS_TYPE) );
       }
       else if (dtdAttribute.isENTITYType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.ENTITY_TYPE) );
       }
       else if (dtdAttribute.isENTITIESType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.ENTITIES_TYPE) );
       }
       else if (dtdAttribute.isNMTOKENType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.NMTOKEN_TYPE) );
       }
       else if (dtdAttribute.isNMTOKENSType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.NMTOKENS_TYPE) );
       }
       else if (dtdAttribute.isNOTATIONType())
       {
           type= schema.getSimpleType( schema.getBuiltInTypeName(SimpleTypesFactory.NOTATION_TYPE) );
       }
       else if (dtdAttribute.isEnumerationType())
       {
          //-- current (08/22/2000) implementation of Schema attribute declaration
          //-- does not provide facility for inline type definition of an attribute
          //-- (it has only 'String typeRef' field for reference to the existing
          //-- top-level datatype).
          //-- do nothing for now
       }
       else
       {
          String err = "Converter: attribute \"" + dtdAttribute.getName();
          err += "\" has unspecified type.";
          throw new DTDException(err);
       }

       if (type != null)
           schemaAttribute.setSimpleType(type);

       if (dtdAttribute.isREQUIRED()) {

          schemaAttribute.setMinOccurs(1);

       } else if (dtdAttribute.isIMPLIED()) {

          schemaAttribute.setMinOccurs(0);

       } else if (dtdAttribute.isFIXED()) {

          schemaAttribute.setDefaultValue(dtdAttribute.getDefaultValue());
          schemaAttribute.setUseValue("fixed");
          schemaAttribute.setMinOccurs(0);

       } else { //-- DTD attribute is of "DEFAULT" type

          schemaAttribute.setDefaultValue(dtdAttribute.getDefaultValue());
          schemaAttribute.setMinOccurs(0);

       }

       return schemaAttribute;
    } //-- convertAttribute

    /**
     * Marshals XML Schema to output char stream.
     * @param schema XML Schema object to marshal.
     * @param writer output char stream to marshal Schema to.
     * @throws IOException if Castor's Marshaller object can not be created
     * with the given <tt>writer</tt>.
     * @throws MarshalException if an error occured during marshalling.
     * @throws ValidationException if an error occured during marshalling.
     */
    public static void marshalSchema(Schema schema, Writer writer)
                                     throws IOException,
                                     MarshalException,
                                     ValidationException,
                                     MappingException {

       Marshaller marshaller = new Marshaller(writer);
       marshaller.setMarshalAsDocument(true);

       Mapping map = new Mapping();
       InputStream is = map.getClass().getResourceAsStream("/org/exolab/castor/xml/dtd/SchemaMapping.xml");

       map.loadMapping(new InputSource(is));
       marshaller.setMapping(map);

       marshaller.marshal(schema);
    } //-- marshalSchema

} //-- Converter
