package org.example.util.ASAP.NRP.Core.Constraints.SINTEF;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxHoursPerWeek implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxHoursPerWeek(int Weight) {
    this.Title = "Max hours per week";
    this.LongTitle = "Maximum number of hours worked each week";
    this.Weight = 0;
    this.ID = "SINTEF.MaxHoursPerWeek";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxHoursPerWeekIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxHoursPerWeek Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxHoursPerWeek + 
      "</MaxHoursPerWeek>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxHoursPerWeek + " hours per week";
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
    double max = employee.EmployeeDescription.Contract.MaxHoursPerWeek;
    for (int i = 0; i < schedulingPeriod.NumWeeksInPeriod; i++) {
      double hours = employee.HoursWorkedPerWeek[i];
      if (hours > max) {
        int pen = (int)(this.Weight * (hours - max));
        penalty += pen;
        for (int k = i * 7; k < (i + 1) * 7 && k < schedulingPeriod.NumDaysInPeriod; k++) {
          if (employee.DayType[k] == 1) {
            employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Too many hours worked this week. Requests max " + max + " hours per week (works " + hours + " hours this week). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
