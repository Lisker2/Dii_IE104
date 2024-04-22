package org.example.util.ASAP.NRP.Core.Constraints.QMC;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxHoursPerFortnight implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxHoursPerFortnight(int Weight) {
    this.Title = "Max hours per fortnight";
    this.LongTitle = "Maximum number of hours worked each two week period";
    this.Weight = 0;
    this.ID = "QMC.MaxHoursPerFortnight";
    this.Weight = Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxHoursPerFortnightIsOn = false;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxHoursPerFortnight Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxHoursPerFortnight + 
      "</MaxHoursPerFortnight>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxHoursPerFortnight + " hours per fortnight";
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
    double max = employee.EmployeeDescription.Contract.MaxHoursPerFortnight;
    for (int i = 0; i < schedulingPeriod.NumWeeksInPeriod; i++) {
      double hours = employee.HoursWorkedPerWeek[i];
      if (i + 1 < schedulingPeriod.NumWeeksInPeriod)
        hours += employee.HoursWorkedPerWeek[i + 1]; 
      if (hours > max) {
        int pen = (int)(this.Weight * (hours - max));
        penalty += pen;
        for (int k = i * 7; k < (i + 2) * 7 && k < schedulingPeriod.NumDaysInPeriod; k++) {
          if (employee.DayType[k] == 1) {
            employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Too many hours worked this fortnight. Requests max " + max + " hours per fortnight (works " + hours + " hours this fortnight). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
