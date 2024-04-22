package org.example.util.ASAP.NRP.Core.Constraints.TEC;

import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.ShiftGroup;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class Pattern implements Cloneable {
  public int StartDay;
  
  public int[] ShiftIndices;
  
  public DayType[] DayTypes;
  
  public ShiftType[] Shifts;
  
  public ShiftGroup[] ShiftGroups;
  
  public StartType StartDayType;
  
  public int Weight;
  
  public int Length;
  
  public int WeekdayIndex;
  
  public DateTime StartDate;
  
  public enum DayType {
    Any, Off, OtherWork, Shift, NotShift, ShiftGroup, WorkingDay;
  }
  
  public enum StartType {
    All, Day, Date;
  }
  
  public Pattern(int startDay, StartType startType, int[] shiftIndices, DayType[] dayTypes, int weight, int weekdayIndex, DateTime startDate, ShiftType[] Shifts, ShiftGroup[] ShiftGroups) {
    this.StartDay = startDay;
    this.ShiftIndices = shiftIndices;
    this.DayTypes = dayTypes;
    this.Shifts = Shifts;
    this.ShiftGroups = ShiftGroups;
    this.StartDayType = startType;
    this.Weight = weight;
    this.Length = dayTypes.length;
    this.WeekdayIndex = weekdayIndex;
    this.StartDate = startDate;
  }
  
  public String getStartDayOrDate() {
    if (this.StartDayType == StartType.All)
      return ""; 
    if (this.StartDayType == StartType.Date)
      return this.StartDate.ToShortDateString(); 
    if (this.StartDayType == StartType.Day) {
      if (this.WeekdayIndex == 0)
        return "Monday"; 
      if (this.WeekdayIndex == 1)
        return "Tuesday"; 
      if (this.WeekdayIndex == 2)
        return "Wednesday"; 
      if (this.WeekdayIndex == 3)
        return "Thursday"; 
      if (this.WeekdayIndex == 4)
        return "Friday"; 
      if (this.WeekdayIndex == 5)
        return "Saturday"; 
      if (this.WeekdayIndex == 6)
        return "Sunday"; 
    } 
    return "";
  }
  
  public String toString() {
    String str = "";
    for (int k = 0; k < this.ShiftIndices.length; k++) {
      DayType dayType = this.DayTypes[k];
      if (dayType == DayType.Off) {
        str = String.valueOf(str) + "Off";
      } else if (dayType == DayType.OtherWork) {
        str = String.valueOf(str) + "OtherWork";
      } else if (dayType == DayType.Any) {
        str = String.valueOf(str) + "Off or On";
      } else if (dayType == DayType.WorkingDay) {
        str = String.valueOf(str) + "On";
      } else if (dayType == DayType.Shift) {
        ShiftType sh = this.Shifts[k];
        str = String.valueOf(str) + sh.Label;
      } else if (dayType == DayType.NotShift) {
        ShiftType sh = this.Shifts[k];
        str = String.valueOf(str) + "Not " + sh.Label;
      } else if (dayType == DayType.ShiftGroup) {
        ShiftGroup sh = this.ShiftGroups[k];
        str = String.valueOf(str) + "[" + sh.Label + "]";
      } 
      if (k != this.ShiftIndices.length - 1)
        str = String.valueOf(str) + ", "; 
    } 
    return str;
  }
  
  public String ToHtmlTableRow() {
    String str = "";
    for (int k = 0; k < this.ShiftIndices.length; k++) {
      DayType dayType = this.DayTypes[k];
      str = String.valueOf(str) + "<td class=\"ptrnTblCell\" valign=\"top\" style=\"text-align: center\">";
      if (dayType == DayType.Off) {
        str = String.valueOf(str) + "Off";
      } else if (dayType == DayType.OtherWork) {
        str = String.valueOf(str) + "OtherWork";
      } else if (dayType == DayType.Any) {
        str = String.valueOf(str) + "Off<br/><i>or</i><br/>On";
      } else if (dayType == DayType.WorkingDay) {
        str = String.valueOf(str) + "On";
      } else if (dayType == DayType.Shift) {
        ShiftType sh = this.Shifts[k];
        str = String.valueOf(str) + sh.Label;
      } else if (dayType == DayType.NotShift) {
        ShiftType sh = this.Shifts[k];
        str = String.valueOf(str) + "<nobr><i>Not</i> " + sh.Label + "</nobr>";
      } else if (dayType == DayType.ShiftGroup) {
        ShiftGroup sh = this.ShiftGroups[k];
        str = String.valueOf(str) + (sh.Group[0]).Label;
        for (int i = 1; i < sh.Group.length; i++)
          str = String.valueOf(str) + " <i>or</i><br/>" + (sh.Group[i]).Label; 
      } 
      str = String.valueOf(str) + "</td>";
    } 
    return str;
  }
}
