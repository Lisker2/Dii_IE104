package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Contract;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class NoNightShiftBeforeFreeWeekend implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public NoNightShiftBeforeFreeWeekend(int Weight) {
    this.Title = "Night shift before free weekend";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = Weight;
  }
  
  public void Delete(EmployeeDescription employee) {}
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "";
  }
  
  public String GetDescription(Employee employee) {
    return "";
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
    boolean nightThu = employee.EmployeeDescription.SchedulingHistory.NightShiftThursday;
    boolean nightFri = employee.EmployeeDescription.SchedulingHistory.NightShiftFriday;
    boolean fri = employee.EmployeeDescription.SchedulingHistory.PreviousFridayWorked;
    boolean sat = employee.EmployeeDescription.SchedulingHistory.PreviousSaturdayWorked;
    boolean sun = false;
    int sundayIndex = schedulingPeriod.FirstSunday;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    Contract.WeekendDefinitions weekendDefinition = employee.EmployeeDescription.Contract.WeekendDefinition;
    if (sundayIndex < 0)
      return penalty; 
    while (sundayIndex < numDaysInPeriod) {
      int thuIndex = sundayIndex - 3;
      int friIndex = sundayIndex - 2;
      int satIndex = sundayIndex - 1;
      if (thuIndex >= 0)
        nightThu = employee.NightShifts[thuIndex]; 
      if (friIndex >= 0) {
        nightFri = employee.NightShifts[friIndex];
        fri = (employee.DayType[friIndex] == 1);
      } 
      if (satIndex >= 0)
        sat = (employee.DayType[satIndex] == 1); 
      sun = (employee.DayType[sundayIndex] == 1);
      if (weekendDefinition == Contract.WeekendDefinitions.SaturdaySunday || 
        weekendDefinition == Contract.WeekendDefinitions.SaturdaySundayMonday) {
        if (!sat && !sun && nightFri) {
          penalty += this.Weight;
          if (friIndex >= 0) {
            employee.ConstraintViolationPenalties[friIndex] = employee.ConstraintViolationPenalties[friIndex] + this.Weight;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[friIndex] = String.valueOf(employee.ViolationDescriptions[friIndex]) + "Night shift before a free weekend. " + System.getProperty("line.separator"); 
          } 
          if (satIndex >= 0) {
            employee.ConstraintViolationPenalties[satIndex] = employee.ConstraintViolationPenalties[satIndex] + this.Weight;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[satIndex] = String.valueOf(employee.ViolationDescriptions[satIndex]) + "Night shift before a free weekend. " + System.getProperty("line.separator"); 
          } 
          employee.ConstraintViolationPenalties[sundayIndex] = employee.ConstraintViolationPenalties[sundayIndex] + this.Weight;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[sundayIndex] = String.valueOf(employee.ViolationDescriptions[sundayIndex]) + "Night shift before a free weekend. " + System.getProperty("line.separator"); 
        } 
      } else if (!fri && !sat && !sun && nightThu) {
        penalty += this.Weight;
        if (thuIndex >= 0) {
          employee.ConstraintViolationPenalties[thuIndex] = employee.ConstraintViolationPenalties[thuIndex] + this.Weight;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[thuIndex] = String.valueOf(employee.ViolationDescriptions[thuIndex]) + "Night shift before a free weekend. " + System.getProperty("line.separator"); 
        } 
        if (friIndex >= 0) {
          employee.ConstraintViolationPenalties[friIndex] = employee.ConstraintViolationPenalties[friIndex] + this.Weight;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[friIndex] = String.valueOf(employee.ViolationDescriptions[friIndex]) + "Night shift before a free weekend. " + System.getProperty("line.separator"); 
        } 
        if (satIndex >= 0) {
          employee.ConstraintViolationPenalties[satIndex] = employee.ConstraintViolationPenalties[satIndex] + this.Weight;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[satIndex] = String.valueOf(employee.ViolationDescriptions[satIndex]) + "Night shift before a free weekend. " + System.getProperty("line.separator"); 
        } 
        employee.ConstraintViolationPenalties[sundayIndex] = employee.ConstraintViolationPenalties[sundayIndex] + this.Weight;
        if (SoftConstraints.UpdateViolationDescriptions)
          employee.ViolationDescriptions[sundayIndex] = String.valueOf(employee.ViolationDescriptions[sundayIndex]) + "Night shift before a free weekend. " + System.getProperty("line.separator"); 
      } 
      sundayIndex += 7;
    } 
    return penalty;
  }
}
