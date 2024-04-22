package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxNumAssignments implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxNumAssignments(int Weight) {
    this.Title = "Max number of shifts";
    this.LongTitle = "Maximum number of shifts that may be assigned in this scheduling period";
    this.Weight = 0;
    this.ID = "ANROM.MaxNumAssignments";
    this.Weight = Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxNumAssignmentsIsOn = false;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxNumAssignments Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxNumAssignments + 
      "</MaxNumAssignments>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxNumAssignments + 
      " shifts, receives " + employee.ShiftsCount;
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
    int maxPermitted = employee.EmployeeDescription.Contract.MaxNumAssignments;
    int numAssignments = employee.ShiftsCount;
    int numDaysInPlanningPeriod = schedulingPeriod.NumDaysInPeriod;
    int numAbsenceDays = 0;
    maxPermitted *= (numDaysInPlanningPeriod - numAbsenceDays) / numDaysInPlanningPeriod;
    if (numAssignments > maxPermitted) {
      penalty = (numAssignments - maxPermitted) * this.Weight;
      for (int day = 0; day < numDaysInPlanningPeriod; day++) {
        if (employee.ShiftCountPerDay[day] > 0) {
          employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + penalty;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many shifts (max " + maxPermitted + "). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    return penalty;
  }
}
