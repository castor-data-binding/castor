/*
 * Copyright 2005 Ralf Joachim
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
package ptf.jdo.rel1toN;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-06-24 19:41:08 -0600 (Fri, 24 Jun 2005) $
 */
public final class OID {
    //-------------------------------------------------------------------------
    
    private Integer _id;

    //-------------------------------------------------------------------------

    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    //-------------------------------------------------------------------------

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("<OID identity='");
        sb.append(_id);
        sb.append("'/>\n");
        
        return sb.toString();
    }
    
    //-------------------------------------------------------------------------
}
