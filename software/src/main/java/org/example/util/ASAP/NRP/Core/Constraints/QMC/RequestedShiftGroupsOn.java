package org.example.util.ASAP.NRP.Core.Constraints.QMC;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;

public class RequestedShiftGroupsOn implements SoftConstraint {
  public String Title = "Requested shift group on";
  
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
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    if (employee.EmployeeDescription.ShiftGroupOnRequests == null)
      return 0; 
    for (int i = 0; i < employee.EmployeeDescription.ShiftGroupOnRequests.length; i++) {
      boolean matched = false;
      ShiftGroup group = (employee.EmployeeDescription.ShiftGroupOnRequests[i]).ShiftGroup;
      int day = (employee.EmployeeDescription.ShiftGroupOnRequests[i]).Day;
      for (int j = 0; j < group.Group.length; j++) {
        int shift = (group.Group[j]).Index;
        int shiftIndex = day * schedulingPeriod.ShiftTypesCount + shift;
        if (employee.ShiftAssignments[shiftIndex]) {
          matched = true;
          break;
        } 
      } 
      if (!matched) {
        int Weight = (employee.EmployeeDescription.ShiftGroupOnRequests[i]).Weight;
        penalty += Weight;
        employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + Weight;
        if (SoftConstraints.UpdateViolationDescriptions)
          employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Requested shift group (" + group.Label + ") on not satisfied (penalty=" + Weight + "). " + System.getProperty("line.separator"); 
      } 
    } 
    return penalty;
  }
}
