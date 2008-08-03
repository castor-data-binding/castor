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
package ctf.jdo.tc8x; 

import java.util.Collection;

public class Lazy1to1Author { 
    private Long _id; 
    private String _firstName; 
    private String _lastName;
    private Collection _books; 
    
    public Long getId() { return _id; } 
    public void setId(final Long id) { _id = id; } 
    
    public String getFirstName() { return _firstName; } 
    public void setFirstName(final String firstName) { _firstName = firstName; } 
    
    public String getLastName() { return _lastName; } 
    public void setLastName(final String lastName) { _lastName = lastName; } 

    public Collection getBooks() { return _books; } 
    public void setBooks(final Collection books) { _books = books; } 
}
