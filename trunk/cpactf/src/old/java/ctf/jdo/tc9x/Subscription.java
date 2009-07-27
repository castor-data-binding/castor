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
package ctf.jdo.tc9x;

import java.util.Date;

public class Subscription {
    private int _id;
    private Date _createdDate;
    private String _description;
    private Customer _customer;

    public final int getID() { return _id; }
    public final void setID(final int id) { _id = id; }
    
    public final Date getCreatedDate() { return _createdDate; }
    public final void setCreatedDate(final Date createdDate) {
        _createdDate = createdDate;
    }

    public final String getDescription() { return _description; }
    public final void setDescription(final String description) {
        _description = description;
    }

    public final Customer getCustomer() { return _customer; }
    public final void setCustomer(final Customer customer) { _customer = customer; }
}
