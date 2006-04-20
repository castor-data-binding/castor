/*
 * Copyright 2005 Werner Guttmann, Nick Stuart
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
 *
 */
package ctf.jdo.tc9x;

import org.exolab.castor.jdo.TimeStampable;

/**
 * @author nstuart
 */
public class DependentObject implements TimeStampable {
	
	private int _id;

	private String _description;

	private ExtendedObject _parent;

	private long _timestamp;

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public ExtendedObject getParent() {
		return _parent;
	}

	public void setParent(ExtendedObject parent) {
		_parent = parent;
	}

	public long getTimestamp() {
		return _timestamp;
	}

	public void setTimestamp(long timestamp) {
		_timestamp = timestamp;
	}

	public void jdoSetTimeStamp(long timestamp) {
		setTimestamp(timestamp);
	}

	public long jdoGetTimeStamp() {
		return getTimestamp();
	}
}
