package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class RequestedDaysOff implements SoftConstraint {
  public String Title = "Requested days off";
  
  public String LongTitle = "";
  
  public int Weight = 0;
  
  public String ID = "";
  
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
    if (employee.EmployeeDescription.DayOffRequestCount == 0)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int i = 0; i < numDaysInPeriod; i++) {
      if (employee.EmployeeDescription.DayOffRequests[i] != 0 && 
        employee.DayType[i] == 1) {
        int penaltyValue = employee.EmployeeDescription.DayOffRequests[i];
        penalty += penaltyValue;
        employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + penaltyValue;
        if (SoftConstraints.UpdateViolationDescriptions)
          employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Requested day off not granted (penalty=" + penaltyValue + "). " + System.getProperty("line.separator"); 
      } 
    } 
    return penalty;
  }
}
