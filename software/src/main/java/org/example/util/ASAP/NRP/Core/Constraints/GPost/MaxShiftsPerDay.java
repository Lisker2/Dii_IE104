package org.example.util.ASAP.NRP.Core.Constraints.GPost;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxShiftsPerDay implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxShiftsPerDay(int Weight) {
    this.Title = "Max shifts per day";
    this.LongTitle = "Maximum number of shifts per day";
    this.Weight = 0;
    this.ID = "GPost.MaxShiftsPerDay";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxShiftsPerDayIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxShiftsPerDay Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxShiftsPerDay + 
      "</MaxShiftsPerDay>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxShiftsPerDay + 
      " shifts a day.";
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
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    int max = employee.EmployeeDescription.Contract.MaxShiftsPerDay;
    for (int i = 0; i < numDaysInPeriod; i++) {
      if (employee.ShiftCountPerDay[i] > max) {
        int pen = this.Weight * (employee.ShiftCountPerDay[i] - max);
        penalty += pen;
        employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + pen;
        if (SoftConstraints.UpdateViolationDescriptions)
          employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Too many shifts on this day (max " + max + "). " + System.getProperty("line.separator"); 
      } 
    } 
    return penalty;
  }
}
