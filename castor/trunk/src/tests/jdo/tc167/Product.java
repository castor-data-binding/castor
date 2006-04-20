package jdo.tc167;

public class Product {
    private int         id;
    private String      name;
    private KindEnum    kind;
    
    public Product() { }
    
    public Product(int id, String name, KindEnum kind) {
        this.id = id;
        this.name = name;
        this.kind = kind;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KindEnum getKind() {
        return this.kind;
    }

    public void setKind(KindEnum kind) {
        this.kind = kind;
    }
    
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (!(other instanceof Product)) return false;
        
        Product prod = (Product) other;
        return (id == prod.id)
            && (((name == null) && (prod.name == null))
            || name.equals(prod.name))
            && (kind == prod.kind);
    }
    
    public String toString() {
        return "Product(" + id + ", '" + name + "', " + kind + ")";
    }
}
