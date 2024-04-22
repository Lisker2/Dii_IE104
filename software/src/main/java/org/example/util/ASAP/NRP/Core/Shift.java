package org.example.util.ASAP.NRP.Core;

public class Shift implements Cloneable {
  SchedulingPeriod schedulingPeriod;
  
  public Employee Employee;
  
  public DateTime StartTime;
  
  public DateTime EndTime;
  
  public ShiftType ShiftType;
  
  public int RosterDay;
  
  public int WeekNumber;
  
  public Shift(ShiftType shiftType, DateTime date, SchedulingPeriod schedulingPeriod) {
    this.RosterDay = 0;
    this.WeekNumber = 0;
    this.ShiftType = shiftType;
    this.schedulingPeriod = schedulingPeriod;
    this.StartTime = new DateTime(date.getYear(), date.getMonth(), date.getDay(), shiftType.getStartTime().getHour(), shiftType.getStartTime().getMinute(), shiftType.getStartTime().getSecond());
    this.EndTime = this.StartTime.AddHours(shiftType.getDuration());
    this.RosterDay = schedulingPeriod.ConvertDateToRosterDay(date);
    this.WeekNumber = schedulingPeriod.ConvertDateToWeek(date);
  }
  
  public Shift(ShiftType shiftType, DateTime startTime, DateTime endTime, int day, int week, SchedulingPeriod schedulingPeriod) {
    this.RosterDay = 0;
    this.WeekNumber = 0;
    this.ShiftType = shiftType;
    this.schedulingPeriod = schedulingPeriod;
    this.StartTime = startTime;
    this.EndTime = endTime;
    this.RosterDay = day;
    this.WeekNumber = week;
  }
  
  public Object Clone() {
    Shift shift = new Shift(this.ShiftType, this.StartTime, this.EndTime, this.RosterDay, this.WeekNumber, this.schedulingPeriod);
    return shift;
  }
  
  public boolean isAssigned() {
    return (this.Employee != null);
  }
  
  public boolean isFixed() {
    return !this.ShiftType.AutoAllocate;
  }
}
