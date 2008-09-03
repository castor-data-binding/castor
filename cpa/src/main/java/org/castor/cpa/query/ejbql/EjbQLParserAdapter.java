/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.query.ejbql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.castor.cpa.query.AbstractParser;
import org.castor.cpa.query.ParseException;
import org.castor.cpa.query.TokenManagerError;
import org.castor.cpa.query.QueryObject;

/**
 * Class that implements abstract parser generator.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class EjbQLParserAdapter extends AbstractParser {
    // --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * 
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    public QueryObject parse(final String oql)
            throws UnsupportedEncodingException, ParseException {
        EjbQLParser parser = null;
        EjbQLParserTokenManager tkmgr = null;
        try {
            tkmgr = createTkmgr(oql);
            parser = new EjbQLParser(tkmgr);
            SimpleNode root = parser.ejbQL();
            EjbQLTreeWalker tw = new EjbQLTreeWalker(root);
            return tw.getSelect();
        } catch (org.castor.cpa.query.ejbql.ParseException e) {
            parser.ReInit(tkmgr);
            throw new ParseException(e);
        } catch (org.castor.cpa.query.castorql.TokenMgrError e) {
            // parser.ReInit(tkmgr);
            throw new TokenManagerError(e);
        }
    }

    // --------------------------------------------------------------------------

    /**
     * Gets the root SimpleNode instance of JJTREE.
     * 
     * @param oql the query string
     * @return the root SimpleNode of JJTREE 
     * @throws UnsupportedEncodingException
     *                 the unsupported encoding exception
     * @throws ParseException
     *                 the QL parse exception
     */
    public SimpleNode getSimpleNode(final String oql)
            throws UnsupportedEncodingException, ParseException {
        EjbQLParser parser = null;
        EjbQLParserTokenManager tkmgr = null;
        try {
            tkmgr = createTkmgr(oql);
            parser = new EjbQLParser(tkmgr);
            return parser.ejbQL();
        } catch (org.castor.cpa.query.ejbql.ParseException e) {
            parser.ReInit(tkmgr);
            throw new ParseException(e);
        } catch (org.castor.cpa.query.castorql.TokenMgrError e) {
            // parser.ReInit(tkmgr);
            throw new TokenManagerError(e);
        }
    }
    
    // --------------------------------------------------------------------------

    /**
     * Creates the TokenManager instance.
     * 
     * @param oql the query string
     * @return the castor ql parser token manager
     * @throws UnsupportedEncodingException
     *                 the unsupported encoding exception
     */
    public EjbQLParserTokenManager createTkmgr(final String oql)
            throws UnsupportedEncodingException {
        StringBuffer stringBuffer = new StringBuffer(oql);
        InputStream bis = new ByteArrayInputStream(stringBuffer.toString()
                .getBytes("UTF-8"));
        InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
        SimpleCharStream jSt = new SimpleCharStream(isr);
        return new EjbQLParserTokenManager(jSt);
    }

    // --------------------------------------------------------------------------
}
