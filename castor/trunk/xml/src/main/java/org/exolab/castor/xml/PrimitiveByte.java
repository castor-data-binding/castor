/*
 * Copyright 2005 Philipp Erlacher
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
package org.exolab.castor.xml;

import org.castor.core.util.StringUtil;

/**
 * This class is part of the command pattern implementation to instantiate an
 * object. It is used as a command by the command invoker PrimitiveObject.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
class PrimitiveByte extends PrimitiveObject {

    @Override
    public Object getObject() {
        if (StringUtil.isEmpty(value)) {
            return new Byte((byte) 0);
        }

        return new Byte(value);
    }

}
