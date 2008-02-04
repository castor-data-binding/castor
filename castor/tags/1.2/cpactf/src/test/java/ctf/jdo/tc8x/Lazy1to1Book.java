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

public final class Lazy1to1Book { 
    private Long _id; 
    private String _name; 
    private Lazy1to1Author _author;
    
    public Long getId() { return _id; } 
    public void setId(final Long id) { _id = id; } 

    public String getName() { return _name; } 
    public void setName(final String name) { _name = name; } 

    public Lazy1to1Author getAuthor() { return _author; } 
    public void setAuthor(final Lazy1to1Author author) { _author = author; } 
}
