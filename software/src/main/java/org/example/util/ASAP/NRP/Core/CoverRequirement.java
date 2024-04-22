package org.example.util.ASAP.NRP.Core;

public class CoverRequirement implements Comparable<Object> {
  public int[][] MinCoverPerShift;
  
  public int[][] MaxCoverPerShift;
  
  public int[][] PrefCoverPerShift;
  
  public int[][] MinCoverPerShiftGroup;
  
  public int[][] MaxCoverPerShiftGroup;
  
  public int[][] PrefCoverPerShiftGroup;
  
  public int[][] MinCoverPerPeriod;
  
  public int[][] MaxCoverPerPeriod;
  
  public int[][] PrefCoverPerPeriod;
  
  public enum SkillTypes {
    AnySkill, SingleSkill, SkillGroup;
  }
  
  public boolean CoverPerPeriodUsed = false;
  
  public boolean CoverPerShiftUsed = false;
  
  public boolean CoverPerShiftGroupUsed = false;
  
  public String SkillID;
  
  public SkillTypes SkillType;
  
  public int Index;
  
  public int compareTo(Object obj) {
    CoverRequirement req = (CoverRequirement)obj;
    if (req == null || this.SkillID == null || req.SkillID == null)
      return 0; 
    return this.SkillID.compareTo(req.SkillID);
  }
  
  public CoverRequirement(String skillID, SkillTypes skillType, SchedulingPeriod schedulingPeriod) {
    this.SkillID = "";
    this.SkillID = skillID;
    this.SkillType = skillType;
    this.MinCoverPerShift = new int[schedulingPeriod.ShiftTypesCount][schedulingPeriod.NumDaysInPeriod];
    this.MaxCoverPerShift = new int[schedulingPeriod.ShiftTypesCount][schedulingPeriod.NumDaysInPeriod];
    this.PrefCoverPerShift = new int[schedulingPeriod.ShiftTypesCount][schedulingPeriod.NumDaysInPeriod];
    this.MinCoverPerShiftGroup = new int[schedulingPeriod.ShiftGroupsCount][schedulingPeriod.NumDaysInPeriod];
    this.MaxCoverPerShiftGroup = new int[schedulingPeriod.ShiftGroupsCount][schedulingPeriod.NumDaysInPeriod];
    this.PrefCoverPerShiftGroup = new int[schedulingPeriod.ShiftGroupsCount][schedulingPeriod.NumDaysInPeriod];
    this.MinCoverPerPeriod = new int[schedulingPeriod.getDayPeriodsCount()][schedulingPeriod.NumDaysInPeriod];
    this.MaxCoverPerPeriod = new int[schedulingPeriod.getDayPeriodsCount()][schedulingPeriod.NumDaysInPeriod];
    this.PrefCoverPerPeriod = new int[schedulingPeriod.getDayPeriodsCount()][schedulingPeriod.NumDaysInPeriod];
    int i;
    for (i = 0; i < schedulingPeriod.ShiftTypesCount; i++) {
      for (int j = 0; j < schedulingPeriod.NumDaysInPeriod; j++) {
        this.MinCoverPerShift[i][j] = -1;
        this.MaxCoverPerShift[i][j] = -1;
        this.PrefCoverPerShift[i][j] = -1;
      } 
    } 
    for (i = 0; i < schedulingPeriod.ShiftGroupsCount; i++) {
      for (int j = 0; j < schedulingPeriod.NumDaysInPeriod; j++) {
        this.MinCoverPerShiftGroup[i][j] = -1;
        this.MaxCoverPerShiftGroup[i][j] = -1;
        this.PrefCoverPerShiftGroup[i][j] = -1;
      } 
    } 
    for (i = 0; i < schedulingPeriod.getDayPeriodsCount(); i++) {
      for (int j = 0; j < schedulingPeriod.NumDaysInPeriod; j++) {
        this.MinCoverPerPeriod[i][j] = -1;
        this.MaxCoverPerPeriod[i][j] = -1;
        this.PrefCoverPerPeriod[i][j] = -1;
      } 
    } 
  }
}
