package org.example.util.ASAP.NRP.Core;

public class Skill implements Comparable<Object> {
  public String ID;
  
  public String Label;
  
  public Skill(String ID, String Label) {
    this.ID = "";
    this.Label = "";
    this.ID = ID;
    this.Label = Label;
  }
  
  public String toString() {
    return this.Label;
  }
  
  public int compareTo(Object obj) {
    if (obj == null || getClass() != obj.getClass())
      return -1; 
    Skill s2 = (Skill)obj;
    return this.Label.compareTo(s2.Label);
  }
  
  public boolean equals(Object obj) {
    if (obj == null || getClass() != obj.getClass())
      return false; 
    Skill skill2 = (Skill)obj;
    return skill2.ID.equals(this.ID);
  }
  
  public int hashCode() {
    return this.ID.hashCode();
  }
}
