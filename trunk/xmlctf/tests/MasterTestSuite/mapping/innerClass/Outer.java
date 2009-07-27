public class Outer {

   private Inner member;
   
   public Inner getMember() { return member; }
   public void setMember (Inner member) { this.member = member; }
   
   public static class Inner {
   
      private String innerMember;
      
      public String getInnerMember() { return innerMember; }
      public void setInnerMember(String innerMember) { this.innerMember = innerMember; }
      
   }
}