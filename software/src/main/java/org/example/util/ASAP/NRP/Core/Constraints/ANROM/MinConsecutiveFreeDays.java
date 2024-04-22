package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MinConsecutiveFreeDays implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinConsecutiveFreeDays(int Weight) {
    this.Title = "Min consecutive free days";
    this.LongTitle = "Minimum number of consecutive free days";
    this.Weight = 0;
    this.ID = "ANROM.MinConsecutiveFreeDays";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MinConsecutiveFreeDaysIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MinConsecutiveFreeDays Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MinConsecutiveFreeDays + 
      "</MinConsecutiveFreeDays>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests min " + employee.EmployeeDescription.Contract.MinConsecutiveFreeDays + 
      " consecutive free days.";
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
    int minConsecutiveFreeDays = employee.EmployeeDescription.Contract.MinConsecutiveFreeDays;
    int consecutiveFreeDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveFreeDaysAndHoliday;
    boolean atLeastOneNonWorkingDay = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveFreeDaysAndHolidayIncludesAtLeastOneNonWorkingDay;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int i = 0; i < numDaysInPeriod; i++) {
      if (employee.DayType[i] != 1) {
        consecutiveFreeDays++;
        if (employee.DayType[i] == 0)
          atLeastOneNonWorkingDay = true; 
      } else {
        if (consecutiveFreeDays != 0 && 
          atLeastOneNonWorkingDay && 
          consecutiveFreeDays < minConsecutiveFreeDays) {
          int extra = minConsecutiveFreeDays - consecutiveFreeDays;
          int pen = this.Weight * extra;
          penalty += pen;
          int end = i - 1 + extra;
          for (int j = end; j >= 0 && j > end - consecutiveFreeDays + extra + extra; j--) {
            if (j < numDaysInPeriod) {
              employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too few consecutive free days (min " + minConsecutiveFreeDays + "). " + System.getProperty("line.separator"); 
            } 
          } 
        } 
        consecutiveFreeDays = 0;
        atLeastOneNonWorkingDay = false;
      } 
    } 
    return penalty;
  }
}
