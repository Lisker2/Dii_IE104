package org.example.util.ASAP.NRP.Core.Constraints.Millar;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxWorkingWeekends implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxWorkingWeekends(int Weight) {
    this.Title = "Max working weekends";
    this.LongTitle = "Maximum number of working weekends";
    this.Weight = 0;
    this.ID = "Millar.MaxWorkingWeekends";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxWorkingWeekendsIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxWorkingWeekends Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxWorkingWeekends + 
      "</MaxWorkingWeekends>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxWorkingWeekends + 
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
    if (this.Weight == 0)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    int maxWorkingWeekends = employee.EmployeeDescription.Contract.MaxWorkingWeekends;
    int numWeekendsInPeriod = schedulingPeriod.NumWeekendsInPeriod;
    int weekendsWorked = 0;
    int saturdayIndex = schedulingPeriod.FirstSaturday;
    if (saturdayIndex == 6 && (
      employee.DayType[0] == 1 || 
      employee.EmployeeDescription.DayOffRequestIsWork[0]))
      weekendsWorked++; 
    int week;
    for (week = 0; week < numWeekendsInPeriod; week++) {
      int sat = schedulingPeriod.FirstSaturday + week * 7;
      if (sat >= schedulingPeriod.NumDaysInPeriod)
        break; 
      if (employee.DayType[sat] == 1 || 
        employee.EmployeeDescription.DayOffRequestIsWork[sat]) {
        weekendsWorked++;
      } else if (sat + 1 < schedulingPeriod.NumDaysInPeriod && (
        employee.DayType[sat + 1] == 1 || 
        employee.EmployeeDescription.DayOffRequestIsWork[sat + 1])) {
        weekendsWorked++;
      } 
    } 
    if (weekendsWorked > maxWorkingWeekends) {
      penalty = this.Weight * (weekendsWorked - maxWorkingWeekends);
      if (schedulingPeriod.FirstSaturday == 6 && (
        employee.DayType[0] == 1 || 
        employee.EmployeeDescription.DayOffRequestIsWork[0])) {
        employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + penalty;
        if (SoftConstraints.UpdateViolationDescriptions)
          employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "Too many working weekends, (max " + maxWorkingWeekends + "). " + System.getProperty("line.separator"); 
      } 
      for (week = 0; week < numWeekendsInPeriod; week++) {
        int sat = schedulingPeriod.FirstSaturday + week * 7;
        if (sat >= schedulingPeriod.NumDaysInPeriod)
          break; 
        if (employee.DayType[sat] == 1 || 
          employee.EmployeeDescription.DayOffRequestIsWork[sat]) {
          employee.ConstraintViolationPenalties[sat] = employee.ConstraintViolationPenalties[sat] + penalty;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[sat] = String.valueOf(employee.ViolationDescriptions[sat]) + "Too many working weekends, (max " + maxWorkingWeekends + "). " + System.getProperty("line.separator"); 
        } 
        if (sat + 1 < schedulingPeriod.NumDaysInPeriod && (
          employee.DayType[sat + 1] == 1 || 
          employee.EmployeeDescription.DayOffRequestIsWork[sat + 1])) {
          employee.ConstraintViolationPenalties[sat + 1] = employee.ConstraintViolationPenalties[sat + 1] + penalty;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[sat + 1] = String.valueOf(employee.ViolationDescriptions[sat + 1]) + "Too many working weekends, (max " + maxWorkingWeekends + "). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    return penalty;
  }
}
