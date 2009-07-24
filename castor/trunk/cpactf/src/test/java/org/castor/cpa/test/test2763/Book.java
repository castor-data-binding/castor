/*
 * Copyright 2009 Lukas Lang
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
package org.castor.cpa.test.test2763;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

/**
 * A simple Book class.
 * 
 * @author lukas.lang
 * 
 */
@Ignore
public final class Book implements TimeStampable {

    /**
     * The unique identifier.
     */
    private long _id;
    /**
     * The book title.
     */
    private String _title;
    /**
     * The time stamp.
     */
    private long _timeStamp;

    /**
     * @return the id.
     */
    public long getId() {
        return _id;
    }

    /**
     * @param id
     *            an identifier.
     */
    public void setId(final long id) {
        this._id = id;
    }

    /**
     * @return the title.
     */
    public String getTitle() {
        return _title;
    }

    /**
     * @param title
     *            a title.
     */
    public void setTitle(final String title) {
        this._title = title;
    }

    /**
     * @return the time stamp.
     * @see org.exolab.castor.jdo.TimeStampable#jdoGetTimeStamp()
     */
    public long jdoGetTimeStamp() {
        return _timeStamp;
    }

    /**
     * @param timeStamp
     *            the time stamp.
     * @see org.exolab.castor.jdo.TimeStampable#jdoSetTimeStamp(long)
     */
    public void jdoSetTimeStamp(final long timeStamp) {
        this._timeStamp = timeStamp;
    }

    /**
     * @return the time stamp.
     */
    public long getTimeStamp() {
        return _timeStamp;
    }

    /**
     * @param timeStamp
     *            the timestamp.
     */
    public void setTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }

}