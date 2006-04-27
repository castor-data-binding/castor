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
package jdo.tc166; 

import java.util.Collection; 

public class Lazy1to1Author { 
	private Long id; 
	private String firstName; 
	private String lastName;
	private Collection books; 
    
	public Long getId() { return id; } 
	public void setId(Long id) { this.id = id; } 
    
    public String getFirstName() { return firstName; } 
    public void setFirstName(String firstName) { this.firstName = firstName; } 
    
    public String getLastName() { return lastName; } 
    public void setLastName(String lastName) { this.lastName = lastName; } 

    public Collection getBooks() { return books; } 
	public void setBooks(Collection books) { this.books = books; } 
}
