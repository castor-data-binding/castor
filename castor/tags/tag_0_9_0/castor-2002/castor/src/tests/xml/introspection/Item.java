public class Item {
    public String name;
    public int    value;

    public Item() {}

    public Item(String n, int v) {
        name  = n;
        value = v;
    }

    public boolean equals ( Item _item )
    {
        
        if (name.equals(_item.name)&&(value==_item.value))
        {
            
            return true;
        }
        else
        {
            
            print();
            _item.print();
            return false;
        }
        
    }
    
    public void print() {
        System.out.println("name=" + name + "; value=" + value);
    }
}
