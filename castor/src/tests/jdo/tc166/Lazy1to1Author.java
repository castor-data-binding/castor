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
