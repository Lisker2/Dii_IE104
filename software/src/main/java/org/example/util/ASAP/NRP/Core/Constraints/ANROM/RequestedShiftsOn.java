package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class RequestedShiftsOn implements SoftConstraint {
  public String Title = "Requested shifts on";
  
  public String LongTitle = "";
  
  public int Weight = 0;
  
  public String ID = "";
  
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
    if (employee.EmployeeDescription.ShiftOnRequestCount == 0)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    int length = schedulingPeriod.NumDaysInPeriod * schedulingPeriod.ShiftTypesCount;
    for (int i = 0; i < length; i++) {
      if (employee.EmployeeDescription.ShiftOnRequests[i] != 0 && 
        !employee.ShiftAssignments[i]) {
        int penaltyValue = employee.EmployeeDescription.ShiftOnRequests[i];
        penalty += penaltyValue;
        employee.ConstraintViolationPenalties[i / schedulingPeriod.ShiftTypesCount] = employee.ConstraintViolationPenalties[i / schedulingPeriod.ShiftTypesCount] + penaltyValue;
        if (SoftConstraints.UpdateViolationDescriptions)
          employee.ViolationDescriptions[i / schedulingPeriod.ShiftTypesCount] = String.valueOf(employee.ViolationDescriptions[i / schedulingPeriod.ShiftTypesCount]) + "Requested shift on not granted (penalty=" + penaltyValue + "). " + System.getProperty("line.separator"); 
      } 
    } 
    return penalty;
  }
}
