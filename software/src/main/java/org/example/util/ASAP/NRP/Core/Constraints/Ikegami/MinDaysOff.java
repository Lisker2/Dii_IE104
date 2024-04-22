package org.example.util.ASAP.NRP.Core.Constraints.Ikegami;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MinDaysOff implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinDaysOff(int Weight) {
    this.Title = "Min non-working days";
    this.LongTitle = "Minimum number of non-working days in the scheduling period";
    this.Weight = 0;
    this.ID = "Ikegami.MinDaysOff";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MinDaysOffIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MinDaysOff Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MinDaysOff + 
      "</MinDaysOff>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests min. " + employee.EmployeeDescription.Contract.MinDaysOff + 
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
    int min = employee.EmployeeDescription.Contract.MinDaysOff;
    if (employee.DaysOffCount < min) {
      penalty = (min - employee.DaysOffCount) * this.Weight;
      SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
      for (int day = 0; day < schedulingPeriod.NumDaysInPeriod; day++) {
        if (employee.DayType[day] == 1) {
          employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + penalty;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Requests min " + employee.EmployeeDescription.Contract.MinDaysOff + " days off. " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    return penalty;
  }
}
