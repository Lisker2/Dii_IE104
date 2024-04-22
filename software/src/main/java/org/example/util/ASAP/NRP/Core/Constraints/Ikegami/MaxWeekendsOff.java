package org.example.util.ASAP.NRP.Core.Constraints.Ikegami;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxWeekendsOff implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxWeekendsOff(int Weight) {
    this.Title = "Max non-working weekends";
    this.LongTitle = "Maximum number of non-working weekends";
    this.Weight = 0;
    this.ID = "Ikegami.MaxWeekendsOff";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxWeekendsOffIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "<MaxWeekendsOff Weight=\"" + this.Weight + "\">" + 
      employee.Contract.MaxWeekendsOff + 
      "</MaxWeekendsOff>" + System.getProperty("line.separator");
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxWeekendsOff + 
      "  weekends off.";
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
    int maxWeekendsOff = employee.EmployeeDescription.Contract.MaxWeekendsOff;
    int numWeekendsInPeriod = schedulingPeriod.NumWeekendsInPeriod;
    int weekendsOff = 0;
    int saturdayIndex = schedulingPeriod.FirstSaturday;
    if (saturdayIndex == 6 && 
      employee.DayType[0] != 1 && 
      !employee.EmployeeDescription.DayOffRequestIsWork[0] && 
      !employee.EmployeeDescription.SchedulingHistory.PreviousSaturdayWorked && 
      !employee.EmployeeDescription.SchedulingHistory.NightShiftFriday)
      weekendsOff++; 
    int week;
    for (week = 0; week < numWeekendsInPeriod; week++) {
      int sat = schedulingPeriod.FirstSaturday + week * 7;
      if (sat + 1 >= schedulingPeriod.NumDaysInPeriod)
        break; 
      if (sat == 0) {
        if (employee.DayType[sat] != 1 && 
          !employee.EmployeeDescription.DayOffRequestIsWork[sat] && 
          employee.DayType[sat + 1] != 1 && 
          !employee.EmployeeDescription.DayOffRequestIsWork[sat + 1] && 
          !employee.EmployeeDescription.SchedulingHistory.NightShiftFriday)
          weekendsOff++; 
      } else if (employee.DayType[sat] != 1 && 
        !employee.EmployeeDescription.DayOffRequestIsWork[sat] && 
        employee.DayType[sat + 1] != 1 && 
        !employee.EmployeeDescription.DayOffRequestIsWork[sat + 1] && 
        !employee.NightShifts[sat - 1]) {
        weekendsOff++;
      } 
    } 
    if (weekendsOff > maxWeekendsOff) {
      penalty = this.Weight * (weekendsOff - maxWeekendsOff);
      if (schedulingPeriod.FirstSaturday == 6 && 
        employee.DayType[0] != 1 && 
        !employee.EmployeeDescription.DayOffRequestIsWork[0] && 
        !employee.EmployeeDescription.SchedulingHistory.PreviousSaturdayWorked && 
        !employee.EmployeeDescription.SchedulingHistory.NightShiftFriday) {
        employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + penalty;
        if (SoftConstraints.UpdateViolationDescriptions)
          employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "Too many weekends off, (max " + maxWeekendsOff + "). " + System.getProperty("line.separator"); 
      } 
      for (week = 0; week < numWeekendsInPeriod; week++) {
        int sat = schedulingPeriod.FirstSaturday + week * 7;
        if (sat + 1 >= schedulingPeriod.NumDaysInPeriod)
          break; 
        if (sat == 0) {
          if (employee.DayType[sat] != 1 && 
            !employee.EmployeeDescription.DayOffRequestIsWork[sat] && 
            employee.DayType[sat + 1] != 1 && 
            !employee.EmployeeDescription.DayOffRequestIsWork[sat + 1] && 
            !employee.EmployeeDescription.SchedulingHistory.NightShiftFriday) {
            employee.ConstraintViolationPenalties[sat] = employee.ConstraintViolationPenalties[sat] + penalty;
            employee.ConstraintViolationPenalties[sat + 1] = employee.ConstraintViolationPenalties[sat + 1] + penalty;
            if (SoftConstraints.UpdateViolationDescriptions) {
              employee.ViolationDescriptions[sat] = String.valueOf(employee.ViolationDescriptions[sat]) + "Too many weekends off, (max " + maxWeekendsOff + "). " + System.getProperty("line.separator");
              employee.ViolationDescriptions[sat + 1] = String.valueOf(employee.ViolationDescriptions[sat + 1]) + "Too many weekends off, (max " + maxWeekendsOff + "). " + System.getProperty("line.separator");
            } 
          } 
        } else if (employee.DayType[sat] != 1 && 
          !employee.EmployeeDescription.DayOffRequestIsWork[sat] && 
          employee.DayType[sat + 1] != 1 && 
          !employee.EmployeeDescription.DayOffRequestIsWork[sat + 1] && 
          !employee.NightShifts[sat - 1]) {
          employee.ConstraintViolationPenalties[sat] = employee.ConstraintViolationPenalties[sat] + penalty;
          employee.ConstraintViolationPenalties[sat + 1] = employee.ConstraintViolationPenalties[sat + 1] + penalty;
          if (SoftConstraints.UpdateViolationDescriptions) {
            employee.ViolationDescriptions[sat] = String.valueOf(employee.ViolationDescriptions[sat]) + "Too many weekends off, (max " + maxWeekendsOff + "). " + System.getProperty("line.separator");
            employee.ViolationDescriptions[sat + 1] = String.valueOf(employee.ViolationDescriptions[sat + 1]) + "Too many weekends off, (max " + maxWeekendsOff + "). " + System.getProperty("line.separator");
          } 
        } 
      } 
    } 
    return penalty;
  }
}
