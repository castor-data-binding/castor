/*
 * Copyright 2005 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
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
 *
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.engine.SQLTypeInfos;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.persist.spi.Identity;

/**
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 * @since 1.0
 */
public final class SQLHelper {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLQuery.class);

    public static Object[] calculateNumberOfFields(Collection extendingClassDescriptors,
            int numberOfIdentityColumns, int numberOfFields, int numberOfExtendLevels, 
            ResultSet rs) throws SQLException {
        
        JDOClassDescriptor potentialLeafDescriptor = null;
        int suggestedNumberOfFields = numberOfFields;
        Collection potentialActualClassDescriptor = new LinkedList();
        int numberOfIdentitiesToAnalyze = 0;
        addExtendingClassDescriptors(potentialActualClassDescriptor, extendingClassDescriptors);
        
        JDOClassDescriptor potentialClassDescriptor = null;
        JDOClassDescriptor potentialClassDescriptorPrevious = null;
        int initialColumnIndex = numberOfFields + numberOfIdentityColumns * numberOfExtendLevels + 1;
        int columnIndex = initialColumnIndex;
        int numberOfExtendingClassDescriptors = 0;
        for (Iterator iter = potentialActualClassDescriptor.iterator(); iter.hasNext(); ) {
            potentialClassDescriptor = (JDOClassDescriptor) iter.next();
            numberOfExtendingClassDescriptors += 1;
            LOG.debug ("Potential extending class descriptor: " + potentialClassDescriptor.getJavaClass().getName());
            FieldDescriptor[] identityDescriptors = potentialClassDescriptor.getIdentities();
            boolean isNull = true;
            
            for (int i = 0; i < identityDescriptors.length; i++) {
                Object temp;
                JDOFieldDescriptor jdoFieldDescriptor = (JDOFieldDescriptor) identityDescriptors[i];
                if (jdoFieldDescriptor.getSQLName().length == 1 ) {
                    temp = SQLTypeInfos.getValue( rs, columnIndex++, java.sql.Types.JAVA_OBJECT);
                } else {
                    Object[] temps = new Object[jdoFieldDescriptor.getSQLName().length];
                    for ( int j=0; j<jdoFieldDescriptor.getSQLName().length; j++ ) {
                        temps[j] = SQLTypeInfos.getValue( rs, columnIndex++, java.sql.Types.JAVA_OBJECT);
                    }
                    temp = new Identity(temps);
                }
                
                LOG.debug ("Obtained value " + temp + " for additional (extending) identity " + 
                        potentialClassDescriptor.getJavaClass().getName() + "/" + 
                        identityDescriptors[i].getFieldName() + " at position " + 
                        columnIndex);
                isNull = (temp == null);
                if (!isNull) {
                    numberOfIdentitiesToAnalyze += 1;
                    potentialClassDescriptorPrevious = potentialClassDescriptor;
                }
                
            }
            
            if (!iter.hasNext() && !isNull && numberOfIdentitiesToAnalyze > 0) {
                potentialLeafDescriptor = potentialClassDescriptor;
                suggestedNumberOfFields += potentialClassDescriptor.getFields().length;
            } else if (!iter.hasNext() && isNull && numberOfIdentitiesToAnalyze > 0){
                potentialLeafDescriptor = potentialClassDescriptorPrevious; 
                // suggestedNumberOfFields += potentialClassDescriptor.getFields().length;
            } else {
                FieldDescriptor[] potentialFields = 
                    potentialClassDescriptor.getFields();
                for (int i = 0; i < potentialFields.length; i++) {
                    JDOFieldDescriptor jdoFieldDescriptor = (JDOFieldDescriptor) potentialFields[i];
                    String[] columnNames = jdoFieldDescriptor.getSQLName();
                    if (columnNames != null) {
                        columnIndex = columnIndex + columnNames.length;
                    }
                }
                
                // the JDOClassDescriptor we just looked at is definitely part of the extends hierarchy,
                // and as such we need to increase the number of potential fields
                if (!isNull) {
                    suggestedNumberOfFields += potentialClassDescriptor.getFields().length;
                }
            }
        }
        
        LOG.debug ("In total " + numberOfIdentitiesToAnalyze + " (extending) identities analyzed.");
        
        if ((potentialLeafDescriptor != null) && LOG.isDebugEnabled()) {
            LOG.debug ("Most likely of type " + potentialLeafDescriptor.getJavaClass().getName());
            LOG.debug ("After analysis, " + suggestedNumberOfFields + " fields need to be loaded.");
        }
        
        return new Object[] {potentialLeafDescriptor, new Integer (suggestedNumberOfFields) };
    }

    public static int numberOfExtendingClassDescriptors(JDOClassDescriptor classDescriptor) {
        int numberOfExtendLevels = 1;
        JDOClassDescriptor currentClassDescriptor = classDescriptor;
        while (currentClassDescriptor.getExtends() != null) {
            currentClassDescriptor = (JDOClassDescriptor) currentClassDescriptor.getExtends();
            numberOfExtendLevels++;
        }
        return numberOfExtendLevels;
    }

    public static void addExtendingClassDescriptors(
            Collection classDescriptorsToAdd, Collection extendingClassDescriptors) {

        JDOClassDescriptor classDescriptor = null; 
        for (Iterator iter = extendingClassDescriptors.iterator(); iter.hasNext(); ) {
            classDescriptor = (JDOClassDescriptor) iter.next(); 
            classDescriptorsToAdd.add (classDescriptor);
            addExtendingClassDescriptors(classDescriptorsToAdd, classDescriptor.getExtendedBy());
        }
    }

    private SQLHelper() { }
}
