package jdo.tc166; 


public class Lazy1to1Book { 
	private Long id; 
	private String name; 
	private Lazy1to1Author author;
	
	public Long getId() { return id; } 
	public void setId(Long id) { this.id = id; } 

    public String getName() { return name; } 
    public void setName(String name) { this.name = name; } 

    public Lazy1to1Author getAuthor() { return author; } 
	public void setAuthor(Lazy1to1Author author) { this.author = author; } 
}
