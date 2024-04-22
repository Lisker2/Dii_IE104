package org.example.util.ASAP.NRP.Core;

import java.util.ArrayList;

public class ShiftType implements Comparable<Object> {
  public String HtmlColor = "#FFFFFF";
  
  public double HoursWorked = 0.0D;
  
  public double FreeTimeBefore = 0.0D;
  
  public double FreeTimeAfter = 0.0D;
  
  ArrayList<String> Skills = new ArrayList<String>();
  
  public int[] Periods = new int[0];
  
  public int[] NextDayPeriods = new int[0];
  
  public boolean CoversPeriods = false;
  
  public int ShiftGroupCount = 0;
  
  public ShiftGroup[] ShiftGroups = new ShiftGroup[0];
  
  Period period;
  
  public boolean AutoAllocate = true;
  
  public String ID;
  
  public String Label;
  
  public String Name;
  
  public String Description;
  
  public int Index;
  
  public boolean RequiresSkills;
  
  public int compareTo(Object obj) {
    if (getStartTime().getTicks() != ((ShiftType)obj).getStartTime().getTicks()) {
      long diff = DateTime.compare(getStartTime(), ((ShiftType)obj).getStartTime());
      if (diff > 0L)
        return 1; 
      if (diff < 0L)
        return -1; 
      return 0;
    } 
    return this.ID.compareTo(((ShiftType)obj).ID);
  }
  
  public ShiftType(String shiftTypeID, DateTime startTime, DateTime endTime) {
    this.ID = "";
    this.Label = "";
    this.Name = "";
    this.Description = "";
    this.Index = -1;
    this.RequiresSkills = false;
    this.ID = shiftTypeID;
    this.Label = shiftTypeID;
    this.Description = shiftTypeID;
    this.period = new Period(startTime, endTime);
    this.HoursWorked = this.period.Duration;
  }
  
  public String getHtmlLabel() {
    return "<span class=\"shiftType\" style=\"background-color:" + this.HtmlColor + "\">" + this.Label + "</span>";
  }
  
  public DateTime getStartTime() {
    return this.period.getStartTime();
  }
  
  public void setStartTime(DateTime value) {
    this.period.setStartTime(value);
  }
  
  public DateTime getEndTime() {
    return this.period.getEndTime();
  }
  
  public void setEndTime(DateTime value) {
    this.period.setEndTime(value);
  }
  
  public boolean AddSkill(String skillID) {
    if (this.Skills.contains(skillID)) {
      System.out.println("Unable to Add Skill : " + skillID + " to Shift : " + this.ID + " as this shift already has this skill.");
      return false;
    } 
    this.Skills.add(skillID);
    this.RequiresSkills = true;
    return true;
  }
  
  public int DeleteSkill(Skill skill) {
    this.Skills.remove(skill.ID);
    if (this.Skills.size() == 0)
      this.RequiresSkills = false; 
    return this.Skills.size();
  }
  
  public ArrayList<String> GetSkills() {
    return this.Skills;
  }
  
  public boolean getSpansMidnight() {
    return this.period.SpansMidnight;
  }
  
  public double getDuration() {
    return this.period.Duration;
  }
  
  public String getDurationLabel() {
    return this.period.DurationLabel;
  }
  
  public void AddPeriod(int index) {
    this.Periods = CSharpConversionHelper.ArrayResize(this.Periods, this.Periods.length + 1);
    this.Periods[this.Periods.length - 1] = index;
    this.CoversPeriods = true;
  }
  
  public void AddNextDayPeriod(int index) {
    this.NextDayPeriods = CSharpConversionHelper.ArrayResize(this.NextDayPeriods, this.NextDayPeriods.length + 1);
    this.NextDayPeriods[this.NextDayPeriods.length - 1] = index;
    this.CoversPeriods = true;
  }
  
  public void AddShiftGroup(ShiftGroup shiftGroup) {
    if (ShiftGroupsContains(shiftGroup))
      return; 
    this.ShiftGroups = CSharpConversionHelper.ArrayResize(this.ShiftGroups, this.ShiftGroups.length + 1);
    this.ShiftGroups[this.ShiftGroups.length - 1] = shiftGroup;
    this.ShiftGroupCount = this.ShiftGroups.length;
  }
  
  private boolean ShiftGroupsContains(ShiftGroup shiftGroup) {
    for (int i = 0; i < this.ShiftGroups.length; i++) {
      if (this.ShiftGroups[i].equals(shiftGroup))
        return true; 
    } 
    return false;
  }
  
  public static int OverlappingMinutes(ShiftType sh1, ShiftType sh2, boolean sameDay) {
    int overlappingMinutes = 0;
    DateTime sh2_StartTime = sh2.getStartTime();
    if (!sameDay)
      sh2_StartTime = sh2_StartTime.AddHours(24); 
    if (sh2_StartTime.isLessThan(sh1.getEndTime().AddMinutes(sh1.FreeTimeAfter))) {
      TimeSpan diffx = sh1.getEndTime().AddMinutes(sh1.FreeTimeAfter).subtract(sh2_StartTime);
      overlappingMinutes += (int)Math.round(diffx.TotalMinutes);
    } 
    if (sh2_StartTime.AddMinutes(-1.0D * sh2.FreeTimeBefore).isLessThan(sh1.getEndTime())) {
      TimeSpan diffy = sh1.getEndTime().subtract(sh2_StartTime.AddMinutes(-1.0D * sh2.FreeTimeBefore));
      overlappingMinutes += (int)Math.round(diffy.TotalMinutes);
    } 
    return overlappingMinutes;
  }
  
  public String toString() {
    if (!this.Name.equalsIgnoreCase("")) {
      if (!this.Label.equalsIgnoreCase(""))
        return String.valueOf(this.Name) + " (" + this.Label + ")"; 
      return this.Name;
    } 
    if (!this.Label.equalsIgnoreCase(""))
      return this.Label; 
    if (this.ID != null)
      return this.ID; 
    return "";
  }
}
