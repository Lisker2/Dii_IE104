package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MinConsecutiveWorkingDays implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinConsecutiveWorkingDays(int Weight) {
    this.Title = "Min consecutive working days";
    this.LongTitle = "Minimum number of consecutive working days";
    this.Weight = 0;
    this.ID = "ANROM.MinConsecutiveWorkingDays";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MinConsecutiveWorkingDaysIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MinConsecutiveWorkingDays Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MinConsecutiveWorkingDays + 
      "</MinConsecutiveWorkingDays>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests min " + employee.EmployeeDescription.Contract.MinConsecutiveWorkingDays + 
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
    int minConsecutiveWorkingDays = employee.EmployeeDescription.Contract.MinConsecutiveWorkingDays;
    int consecutiveWorkingDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveWorkingDays;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int i = 0; i < numDaysInPeriod; i++) {
      if (employee.DayType[i] != 1 && 
        !employee.EmployeeDescription.DayOffRequestIsWork[i]) {
        if (consecutiveWorkingDays != 0 && consecutiveWorkingDays < minConsecutiveWorkingDays) {
          int extra = minConsecutiveWorkingDays - consecutiveWorkingDays;
          int pen = this.Weight * extra;
          penalty += pen;
          int end = i - 1 + extra;
          for (int j = end; j >= 0 && j > end - consecutiveWorkingDays + extra + extra; j--) {
            if (j < numDaysInPeriod) {
              employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too few consecutive working days (min " + minConsecutiveWorkingDays + "). " + System.getProperty("line.separator"); 
            } 
          } 
        } 
        consecutiveWorkingDays = 0;
      } else {
        consecutiveWorkingDays++;
      } 
    } 
    return penalty;
  }
}
