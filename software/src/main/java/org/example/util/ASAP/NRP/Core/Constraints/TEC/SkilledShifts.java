package org.example.util.ASAP.NRP.Core.Constraints.TEC;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class SkilledShifts implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public SkilledShifts(int Weight) {
    this.Title = "Skilled shifts";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = Weight;
  }
  
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
    if (this.Weight == 0)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    int shiftCount = schedulingPeriod.ShiftTypesCount;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int i = 0; i < numDaysInPeriod; i++) {
      if (employee.DayType[i] == 1) {
        int index = shiftCount * i;
        for (int j = 0; j < shiftCount; j++) {
          int shift1 = index + j;
          if (employee.ShiftAssignments[shift1])
            if (!schedulingPeriod.EmployeeHasSkillsForShiftType(employee.EmployeeDescription, j)) {
              penalty += this.Weight;
              employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + this.Weight;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Employee does not have required skills for shift '" + (schedulingPeriod.GetShiftType(j)).Label + "'. " + System.getProperty("line.separator"); 
            }  
        } 
      } 
    } 
    return penalty;
  }
}
