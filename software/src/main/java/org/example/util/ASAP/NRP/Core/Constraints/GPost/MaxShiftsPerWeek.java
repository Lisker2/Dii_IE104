package org.example.util.ASAP.NRP.Core.Constraints.GPost;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxShiftsPerWeek implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxShiftsPerWeek(int Weight) {
    this.Title = "Max shifts per week";
    this.LongTitle = "Maximum number of shifts per week";
    this.Weight = 0;
    this.ID = "GPost.MaxShiftsPerWeek";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxShiftsPerWeekIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxShiftsPerWeek Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxShiftsPerWeek + 
      "</MaxShiftsPerWeek>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxShiftsPerWeek + 
      " shifts per week.";
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
    int max = employee.EmployeeDescription.Contract.MaxShiftsPerWeek;
    for (int i = 0; i < schedulingPeriod.NumDaysInPeriod; i += 7) {
      int tot = 0;
      for (int j = 0; j < 7 && j + i < schedulingPeriod.NumDaysInPeriod; j++)
        tot += employee.ShiftCountPerDay[j + i]; 
      if (tot > max) {
        int pen = this.Weight * (tot - max) * (tot - max);
        penalty += pen;
        for (int k = 0; k < 7 && k + i < schedulingPeriod.NumDaysInPeriod; k++) {
          if (employee.ShiftCountPerDay[k + i] > 0) {
            employee.ConstraintViolationPenalties[k + i] = employee.ConstraintViolationPenalties[k + i] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[k + i] = String.valueOf(employee.ViolationDescriptions[k + i]) + "Too many shifts this week. Requests max " + max + " shifts per week (works " + tot + " shifts this week). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
