package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxConsecutiveFreeDays implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxConsecutiveFreeDays(int Weight) {
    this.Title = "Max consecutive free days";
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
    return "Requests max " + employee.EmployeeDescription.Contract.MaxConsecutiveFreeDays + 
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
    int maxConsecutiveFreeDays = employee.EmployeeDescription.Contract.MaxConsecutiveFreeDays;
    int consecutiveFreeDays = employee.EmployeeDescription.SchedulingHistory.PreviousConsecutiveFreeDays;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int i = 0; i < numDaysInPeriod; i++) {
      if (employee.DayType[i] == 0)
        consecutiveFreeDays++; 
      if (employee.DayType[i] == 1 || 
        employee.EmployeeDescription.DayOffRequestIsWork[i] || 
        i + 1 == numDaysInPeriod) {
        if (consecutiveFreeDays > maxConsecutiveFreeDays) {
          int pen = this.Weight * (consecutiveFreeDays - maxConsecutiveFreeDays);
          penalty += pen;
          int end = i - 1;
          if (employee.DayType[i] == 0)
            end = i; 
          for (int j = end; j >= 0 && j > end - consecutiveFreeDays; j--) {
            employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too many consecutive free days (max " + maxConsecutiveFreeDays + "). " + System.getProperty("line.separator"); 
          } 
        } 
        consecutiveFreeDays = 0;
      } 
    } 
    return penalty;
  }
}
