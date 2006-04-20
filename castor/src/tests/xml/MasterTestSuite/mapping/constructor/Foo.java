

public class Foo {
    
    private int size = 20;
    
    public Foo() {
        
    }
    
    public Foo(int size) {
        System.out.println("Foo("  + size + ")"); 
        this.size = size;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
}
