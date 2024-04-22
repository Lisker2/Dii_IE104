package org.example.util.ASAP.NRP.Core.Parsers;

import org.example.util.ASAP.NRP.Core.CoverRequirement;
import org.example.util.ASAP.NRP.Core.Period;

public class Cover implements Cloneable {
  public String ID;
  
  public String SkillID;
  
  public CoverType Type;
  
  public CoverRequirement.SkillTypes SkillType;
  
  public int Min;
  
  public int Max;
  
  public int Preferred;
  
  public Period Period;
  
  public enum CoverType {
    Period, Shift, ShiftGroup;
  }
  
  public Cover(String ID, CoverType coverType, String skillID, CoverRequirement.SkillTypes skillType, Period period) {
    this.ID = null;
    this.SkillID = "";
    this.Min = -1;
    this.Max = -1;
    this.Preferred = -1;
    this.ID = ID;
    this.Type = coverType;
    this.SkillID = skillID;
    this.SkillType = skillType;
    this.Period = period;
  }
  
  public Object Clone() {
    Cover cover = new Cover(this.ID, this.Type, this.SkillID, this.SkillType, this.Period);
    cover.Min = this.Min;
    cover.Max = this.Max;
    cover.Preferred = this.Preferred;
    return cover;
  }
}
