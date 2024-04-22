package org.example.util.ASAP.NRP.Core.Constraints.ORTEC01;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxWorkingWeekendsIncFriNight implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxWorkingWeekendsIncFriNight(int Weight) {
    this.Title = "Max working weekends (includes night shifts on Friday nights).";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {}
  
  public String ToXml(EmployeeDescription employee) {
    return "";
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxWorkingWeekendsIncFriNight + 
      "  working weekends.";
  }
  
  public int Calculate(Employee employee, int startDay, int endDay) {
    return Calculate(employee);
  }
  
  public int Calculate(Employee employee, int maxPenalty, int startDay, int endDay) {
    return Calculate(employee);
  }
  
  public int Calculate(Employee employee, int startDay, int endDay, boolean updateStructure) {
    return Calculate(employee);
  }
  
  public int Calculate(Employee employee) {
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    int i = employee.EmployeeDescription.Contract.MaxWorkingWeekendsIncFriNight;
    int penaltyValue = schedulingPeriod.MasterWeights.MaxWorkingWeekendsIncFriNight;
    if (!employee.EmployeeDescription.Contract.MaxWorkingWeekendsIncFriNightIsOn || 
      penaltyValue == 0)
      return penalty; 
    int weekendsWorked = 0;
    if (employee.DayType[3] == 1 || 
      employee.DayType[4] == 1)
      weekendsWorked++; 
    if (employee.DayType[10] == 1 || 
      employee.DayType[11] == 1)
      weekendsWorked++; 
    if (employee.DayType[17] == 1 || 
      employee.DayType[18] == 1)
      weekendsWorked++; 
    if (employee.DayType[24] == 1 || 
      employee.DayType[25] == 1)
      weekendsWorked++; 
    if (employee.ShiftsOnDay[30][(schedulingPeriod.GetShiftType("N")).Index] != null)
      weekendsWorked++; 
    if (weekendsWorked > i) {
      penalty += penaltyValue;
      int[] weekendDays = { 3, 4, 10, 11, 17, 18, 24, 25 };
      for (int j = 0; j < weekendDays.length; j++) {
        int day = weekendDays[j];
        if (employee.DayType[day] == 1) {
          employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + penaltyValue;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many working weekends. " + System.getProperty("line.separator"); 
        } 
      } 
      if (employee.ShiftsOnDay[30][(schedulingPeriod.GetShiftType("N")).Index] != null) {
        employee.ConstraintViolationPenalties[30] = employee.ConstraintViolationPenalties[30] + penaltyValue;
        if (SoftConstraints.UpdateViolationDescriptions)
          employee.ViolationDescriptions[30] = String.valueOf(employee.ViolationDescriptions[30]) + "Too many working weekends. " + System.getProperty("line.separator"); 
      } 
    } 
    return penalty;
  }
}
