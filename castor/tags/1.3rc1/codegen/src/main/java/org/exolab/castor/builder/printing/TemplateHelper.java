/*
 * Copyright 2008 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.builder.printing;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.exolab.javasource.JAnnotation;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JEnum;

/**
 * Helper class that provides convenience methods used
 * by Velocity templates during {@link JClass} printing.
 */
public class TemplateHelper {
    
    /**
     * Removes all line breaks from a given string.
     * 
     * @param string The string containing line breaks.
     * @return A string without line breaks.
     */
    public String removeLineBreaks(String string) {
        return string.replaceAll("\n", "");
    }
    
    /**
     * Takes a comment string and splits it into lines
     * that have the maximum length of 70 chars,
     * 
     * @param comment The comment as string.
     * @return The comment string splitted into a list.
     */
    public ArrayList getLines(String comment) {
        comment = removeLineBreaks(comment);
        ArrayList list = new ArrayList();
        do {
            comment = computeLine(comment, list);
        } while (comment.length() > 0);
        return list;
    }
    
    /**
     * Helper methods that computes the next line for
     * {@link #getLines(String)}.
     * 
     * @param comment The current comment string.
     * @param list The current list.
     * @return The comment without the next line.
     */
    private String computeLine(String comment, ArrayList list) {
        if (comment == null) {
            return ""; 
        }
        
        if (comment.length() <=70) {
            list.add(comment);
            return "";
        }
        
        int pos = comment.indexOf(' ', 70);
        if (pos == -1) {
            list.add(comment);
            return "";
        }
        
        String line = comment.substring(0,pos);
        list.add(line);
        return comment.substring(pos + 1);
        
    }
    
    /**
     * Returns true if the {@link JClass} instance is instance of JEnum.
     * 
     * @param jClass The {@link JClass} instance to check.
     * @return true if instance of JEnum.
     */
    public boolean isEnum(final JClass jClass) {
        return (jClass instanceof JEnum);
    }
    
    /**
     * Converts the given {@link JAnnotation} to a string representation.
     * 
     * @param annotation The annotation to translate.
     * @param shift The intent.
     * @return A string representation of the annotation.
     */
    public String printAnnotation(final JAnnotation annotation, String shift) {
        StringBuilder stringBuffer = new StringBuilder(32);
        stringBuffer.append(shift);
        stringBuffer.append("@");
        stringBuffer.append(annotation.getAnnotationType().getLocalName());
        stringBuffer.append("(");
        // Single element annotation?
        String[] elementNames = annotation.getElementNames();
        if (elementNames.length == 1 && elementNames[0].equals(JAnnotation.VALUE)) {
            // Just output value
            stringBuffer.append(printAnnotationValue(annotation.getElementValueObject(JAnnotation.VALUE),shift));
        } else if (elementNames.length > 0) {
            // Max element name length?
            int maxLength = 0;
            for (int i = 0; i < elementNames.length; i++) {
                int elementNameLength = elementNames[i].length();
                if (elementNameLength > maxLength) { maxLength = elementNameLength; }
            }
            // Output element name and values
            stringBuffer.append("\n");
            stringBuffer.append(shift + "    ");
            for (int i = 0; i < elementNames.length; i++) {
                int elementNameLength = elementNames[i].length();
                // Output element name with padding
                stringBuffer.append(elementNames[i]);
                for (int p = 0; p < maxLength - elementNameLength; p++) {
                    stringBuffer.append(" ");
                }
                // Assignment operator
                stringBuffer.append(" = ");
                // Value
                stringBuffer.append(printAnnotationValue(
                        annotation.getElementValueObject(elementNames[i]), shift));
                if (i < elementNames.length - 1) {
                    stringBuffer.append(",");
                    stringBuffer.append("\n");
                    stringBuffer.append(shift + "    ");
                }
            }
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
    
    /**
     * Helper method that translates an annotation value.
     * 
     * @param elementValue The value to translate.
     * @param shift The intent,
     * @return The string representation of the value.
     */
    private String printAnnotationValue(final Object elementValue, final String shift) {
        // String?
        if (elementValue instanceof String) {
            return (String) elementValue;
        } else if (elementValue instanceof JAnnotation) {
            JAnnotation annotation = (JAnnotation) elementValue;
            return printAnnotation(annotation, shift);
        } else if (elementValue.getClass().isArray()) {
            // Short hand for single item list
            int listLength = Array.getLength(elementValue);
            if (listLength == 1) {
                return printAnnotationValue(Array.get(elementValue, 0), shift);
            }
            // Output list items
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("\n");
            stringBuffer.append("{");
            stringBuffer.append("\n");
            stringBuffer.append(shift);
            for (int i = 0; i < listLength; i++) {
                stringBuffer.append(shift);
                stringBuffer.append(printAnnotationValue(Array.get(elementValue, i), shift));
                if (i < listLength - 1) { stringBuffer.append(","); }
                stringBuffer.append("\n");
            }
            stringBuffer.append("}");
            return stringBuffer.toString();
        }
        throw new IllegalArgumentException("'" + elementValue + "' was not expected.");
    }
    
}
