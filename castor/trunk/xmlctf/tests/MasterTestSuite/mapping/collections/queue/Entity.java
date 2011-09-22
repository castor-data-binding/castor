import java.util.Queue;
import java.util.PriorityQueue;

public final class Entity {
    private Integer _id;
    private String _name;
    private Queue<String> entries; // = new PriorityQueue<String>();
    
    public Queue<String> getEntries() {
      return entries;
   }

   public void setEntries(Queue<String> queue) {
      this.entries = queue;
   }

   public Integer getId() {
        return _id;
    }
    
    public void setId(final Integer id) {
        _id = id;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }
}
