package org.example.util.ASAP.NRP.Core;

public class SkillGroup {
  public Skill[] Skills = new Skill[0];
  
  public String ID;
  
  public int Index;
  
  public String Label;
  
  public SkillGroup(String id) {
    this.ID = "";
    this.Index = -1;
    this.Label = "";
    this.ID = id;
  }
  
  public void AddSkill(Skill skill) {
    if (Contains(skill)) {
      System.out.println("Warning: SkillGroup: '" + skill.ID + "' already contains skill " + skill + ".");
      return;
    } 
    try {
      this.Skills = CSharpConversionHelper.ArrayResize(this.Skills, this.Skills.length + 1);
      this.Skills[this.Skills.length - 1] = skill;
      this.Label = (this.Skills[0]).Label;
      for (int i = 1; i < this.Skills.length; i++)
        this.Label = String.valueOf(this.Label) + ", " + (this.Skills[i]).Label; 
    } catch (RuntimeException e) {
      e.printStackTrace();
    } 
  }
  
  private boolean Contains(Skill skill) {
    for (int i = 0; i < this.Skills.length; i++) {
      if ((this.Skills[i]).ID.equals(skill.ID))
        return true; 
    } 
    return false;
  }
}
