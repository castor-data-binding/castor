package org.exolab.castor.builder.factory;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JParameter;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * This class translates a fieldInfo describing an identity into 
 * the suitable getter<7setter methods.
 */
public class IdentityMemberAndAccessorFactory extends FieldMemberAndAccessorFactory {

    /**
     * Creates the IdentityMemberAndAccessorFactory.
     * @param naming the javaNaming to use
     */
    public IdentityMemberAndAccessorFactory(final JavaNaming naming) {
        super(naming);
    }

    /**
     * Creats the getter/setter and getReferenceId methods.
     * 
     * @param fieldInfo the fieldInfo to translate
     * @return the created methods
     */
    public JMethod[] createAccessMethods(final FieldInfo fieldInfo) {
            String mname = fieldInfo.getMethodSuffix();
            JType jType = fieldInfo.getSchemaType().getJType();

            JMethod[] methods = new JMethod[3];
            methods[0] = makeGetMethod(fieldInfo, mname, jType); // -- create get method
            methods[1] = makeSetMethod(fieldInfo, mname, jType); // -- create set method
            methods[2] = makeGetReferenceIdMethod(fieldInfo); // -- create getReferenceId
                                                     // (from Referable Interface)

            return methods;
        } // -- createAccessMethods

        /**
         * Creates the getter method.
         * 
         * @param fieldInfo the fieldInfo to translate
         * @param mname the name of this field
         * @param jType the type of this field
         * @return the getter method for this identity
         */
        private JMethod makeGetMethod(final FieldInfo fieldInfo, 
                final String mname, final JType jType) {
            JMethod method = new JMethod("get" + mname, jType,
                    "the value of field '" + mname + "'.");
            JSourceCode jsc = method.getSourceCode();
            jsc.add("return this.");
            jsc.append(fieldInfo.getName());
            jsc.append(";");
            return method;
        }

        /**
         * Creates the setter method.
         * 
         * @param fieldInfo the fieldInfo to translate
         * @param mname the name of this field
         * @param jType the type of this field
         * @return the setter method for this identity
         */
        private JMethod makeSetMethod(final FieldInfo fieldInfo, 
                final String mname, final JType jType) {
            JMethod method = new JMethod("set" + mname);
            method.addParameter(new JParameter(jType, fieldInfo.getName()));
            JSourceCode jsc = method.getSourceCode();
            jsc.add("this.");
            jsc.append(fieldInfo.getName());
            jsc.append(" = ");
            jsc.append(fieldInfo.getName());
            jsc.append(";");

            //-- add resolver registration
            //jsc.add("if (idResolver != null) ");
            //jsc.indent();
            //jsc.add("idResolver.addResolvable(");
            //jsc.append(fieldInfo.getName());
            //jsc.append(", this);");
            //jsc.unindent();

            return method;
        }

        /**
         * Creates the getReferenceId method.
         * @param fieldInfo the fieldInfo to translate
         * @return the getReferenceId method.
         */
        private JMethod makeGetReferenceIdMethod(final FieldInfo fieldInfo) {
            JMethod method = new JMethod("getReferenceId", SGTypes.STRING,
                    "the reference ID");
            JSourceCode jsc = method.getSourceCode();
            jsc.add("return this.");
            jsc.append(fieldInfo.getName());
            jsc.append(";");
            return method;
        }


}
