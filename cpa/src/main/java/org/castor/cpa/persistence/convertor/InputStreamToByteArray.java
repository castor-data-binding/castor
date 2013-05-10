/*
 * Copyright 2007 Ralf Joachim
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
package org.castor.cpa.persistence.convertor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Convert <code>InputStream</code> to <code>byte[]</code>.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public final class InputStreamToByteArray extends AbstractLobTypeConvertor {
    //-----------------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public InputStreamToByteArray() {
        super(InputStream.class, byte[].class);
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object object) {
        try {
            InputStream is = (InputStream) object;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[getLobBufferSize()];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex.toString());
        }
    }

    //-----------------------------------------------------------------------------------
}
