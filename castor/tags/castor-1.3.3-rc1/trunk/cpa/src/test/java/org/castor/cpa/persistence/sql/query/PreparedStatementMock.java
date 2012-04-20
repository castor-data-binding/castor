/*
 * Copyright 2010 Ralf Joachim, Ahmad Hassan
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
package org.castor.cpa.persistence.sql.query;

import java.sql.PreparedStatement;

/**
 * PreparedStatementMock object to test QueryContext.
 * <br/>
 * We need to implement this mock with an invocation handler as the implemented
 * interface java.sql.PreparedStatement has changed from Java 5 to Java 6. With
 * this change not only new methods have been added but also new classes has been
 * introduced which are referenced by PreparedStatement.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public interface PreparedStatementMock extends PreparedStatement {
    int getLastParameterIndex();
}
