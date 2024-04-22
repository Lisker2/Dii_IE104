package org.example.util.ASAP.NRP.Core.Constraints.Ikegami;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxWorkingDaysPerWeek implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxWorkingDaysPerWeek(int Weight) {
    this.Title = "Max working days per week";
    this.LongTitle = "Maximum number of working days per week";
    this.Weight = 0;
    this.ID = "Ikegami.MaxWorkingDaysPerWeek";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxWorkingDaysPerWeekIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxWorkingDaysPerWeek Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxWorkingDaysPerWeek + 
      "</MaxWorkingDaysPerWeek>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxWorkingDaysPerWeek + " working days per week.";
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
    int max = employee.EmployeeDescription.Contract.MaxWorkingDaysPerWeek;
    for (int i = 0; i < schedulingPeriod.NumDaysInPeriod; i += 7) {
      int tot = 0;
      for (int j = 0; j < 7 && j + i < schedulingPeriod.NumDaysInPeriod; j++) {
        int day = j + i;
        if (employee.DayType[day] == 1 || 
          employee.EmployeeDescription.DayOffRequestIsWork[day])
          tot++; 
      } 
      if (tot > max) {
        int pen = this.Weight * (tot - max);
        penalty += pen;
        for (int k = 0; k < 7 && k + i < schedulingPeriod.NumDaysInPeriod; k++) {
          if (employee.ShiftCountPerDay[k + i] > 0) {
            employee.ConstraintViolationPenalties[k + i] = employee.ConstraintViolationPenalties[k + i] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[k + i] = String.valueOf(employee.ViolationDescriptions[k + i]) + "Too many working days this week. Requests max " + max + " working days per week (works " + tot + " days this week). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
