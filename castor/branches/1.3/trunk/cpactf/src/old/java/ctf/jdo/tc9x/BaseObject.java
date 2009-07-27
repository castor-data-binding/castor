/*
 * Copyright 2005 Werner Guttmann
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
package ctf.jdo.tc9x;

import org.exolab.castor.jdo.TimeStampable;

/**
 * @author nstuart
 */
public class BaseObject implements TimeStampable {
    private int _id;
    private String _description;
    private boolean _saved;
    private long _timestamp;

    public final int getId() {
        return _id;
    }

    public final void setId(final int id) {
        _id = id;
    }

    public final String getDescription() {
        return _description;
    }

    public final void setDescription(final String description) {
        _description = description;
    }

    public final boolean isSaved() {
        return _saved;
    }

    public final void setSaved(final boolean saved) {
        _saved = saved;
    }

    public final long getTimestamp() {
        return _timestamp;
    }

    public final void setTimestamp(final long timestamp) {
        _timestamp = timestamp;
    }

    public final void jdoSetTimeStamp(final long timestamp) {
        setTimestamp(timestamp);
    }

    public final long jdoGetTimeStamp() {
        return getTimestamp();
    }
}
