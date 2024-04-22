package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxConsecutiveWorkingDays implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxConsecutiveWorkingDays(int Weight) {
    this.Title = "Max consecutive working days";
    this.LongTitle = "Maximum number of consecutive working days";
    this.Weight = 0;
    this.ID = "ANROM.MaxConsecutiveWorkingDays";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxConsecutiveWorkingDaysIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxConsecutiveWorkingDays Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxConsecutiveWorkingDays + 
      "</MaxConsecutiveWorkingDays>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxConsecutiveWorkingDays + 
      " consecutive working days.";
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
    int maxConsecutiveWorkingDays = employee.EmployeeDescription.Contract.MaxConsecutiveWorkingDays;
    int consecutiveWorkingDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveWorkingDays;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int i = 0; i < numDaysInPeriod; i++) {
      if (employee.DayType[i] == 1 || employee.EmployeeDescription.DayOffRequestIsWork[i])
        consecutiveWorkingDays++; 
      if ((employee.DayType[i] != 1 && !employee.EmployeeDescription.DayOffRequestIsWork[i]) || 
        i + 1 == numDaysInPeriod) {
        if (consecutiveWorkingDays > maxConsecutiveWorkingDays) {
          int pen = this.Weight * (consecutiveWorkingDays - maxConsecutiveWorkingDays);
          penalty += pen;
          int end = i - 1;
          if (employee.DayType[i] == 1)
            end = i; 
          for (int j = end; j >= 0 && j > end - consecutiveWorkingDays; j--) {
            employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too many consecutive working days (max " + maxConsecutiveWorkingDays + "). " + System.getProperty("line.separator"); 
          } 
        } 
        consecutiveWorkingDays = 0;
      } 
    } 
    return penalty;
  }
}
