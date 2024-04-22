package org.example.util.ASAP.NRP.Core.Constraints.Ikegami;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxDaysOff implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxDaysOff(int Weight) {
    this.Title = "Max non-working days";
    this.LongTitle = "Maximum number of non-working days in the scheduling period";
    this.Weight = 0;
    this.ID = "Ikegami.MaxDaysOff";
    this.Weight = Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxDaysOffIsOn = false;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxDaysOff Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxDaysOff + 
      "</MaxDaysOff>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max. " + employee.EmployeeDescription.Contract.MaxDaysOff + 
      " days off, receives " + employee.DaysOffCount;
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
    int penalty = 0;
    int max = employee.EmployeeDescription.Contract.MaxDaysOff;
    if (employee.DaysOffCount > max) {
      penalty = (employee.DaysOffCount - max) * this.Weight;
      SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
      for (int day = 0; day < schedulingPeriod.NumDaysInPeriod; day++) {
        if (employee.DayType[day] != 1 && 
          !employee.EmployeeDescription.DayOffRequestIsWork[day]) {
          employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + penalty;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Requests max " + employee.EmployeeDescription.Contract.MaxDaysOff + " days off. " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    return penalty;
  }
}
