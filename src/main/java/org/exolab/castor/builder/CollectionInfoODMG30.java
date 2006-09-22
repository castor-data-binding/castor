package org.exolab.castor.builder;

import org.exolab.castor.builder.types.XSListODMG30;
import org.exolab.castor.builder.types.XSType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JSourceCode;

/**
 * A helper used for generating source that deals with Collections.
 * @author <a href="mailto:frank.thelen@poet.de">Frank Thelen</a>
 * @author <a href="mailto:bernd.deichmann@poet.de">Bernd Deichmann</a>
 * @version $Revision$ $Date: 2006-02-23 01:08:24 -0700 (Thu, 23 Feb 2006) $
**/
public class CollectionInfoODMG30 extends CollectionInfo {

    /**
     * Creates a new CollectionInfoODMG30
     * 
     * @param contentType
     *            the content type of the collection, ie. the type of objects
     *            that the collection will contain
     * @param name
     *            the name of the Collection
     * @param elementName
     *            the element name for each element in collection
     */
    public CollectionInfoODMG30(XSType contentType, String name, String elementName,
            final boolean useJava50) {
        super(contentType, name, elementName, useJava50);
        this.setSchemaType(new XSListODMG30(contentType, useJava50));
    } // -- CollectionInfoODMG30

    /**
     * Creates code for initialization of this Member
     * 
     * @param jsc
     *            the JSourceCode in which to add the source to
     */
    public void generateInitializerCode(JSourceCode jsc) {
        jsc.add("this.");
        jsc.append(this.getName());
        jsc.append(" = ODMG.getImplementation().newDArray();");
    } // -- generateConstructorCode

    protected void createEnumerateMethod(JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod(SGTypes.createEnumeration(this.getContentType().getJType(), useJava50), "enumerate" + this.getMethodSuffix());

        if (!jClass.hasImport("java.util.Vector")) {
            jClass.addImport("java.util.Vector");
        }

        if (!jClass.hasImport("java.util.Iterator")) {
            jClass.addImport("java.util.Iterator");
        }

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("Vector v = new Vector();"); // ODMG 3.0
        sourceCode.add("Iterator i = ");
        sourceCode.append(getName());
        sourceCode.append(".iterator();");
        sourceCode.add("");
        sourceCode.add("while (i.hasNext()) {");
        sourceCode.indent();
        sourceCode.add("v.add(i.next());");
        sourceCode.unindent();
        sourceCode.add("");
        sourceCode.add("return v.elements();");

        jClass.addMethod(method);
    }
} // -- CollectionInfoODMG30
