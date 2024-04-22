package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class TwoFreeDaysAfterNightShifts implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public TwoFreeDaysAfterNightShifts(int Weight) {
    this.Title = "Two free days after night shifts";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = Weight;
  }
  
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
    if (this.Weight == 0)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    boolean previousNightShiftFound = employee.EmployeeDescription.SchedulingHistory.PreviousNightShift;
    int previousFreeDays = employee.EmployeeDescription.SchedulingHistory.PreviousFreeDaysAfterNightShift;
    if (!previousNightShiftFound)
      previousFreeDays = 0; 
    boolean nightShiftFound = previousNightShiftFound;
    int freeDays = previousFreeDays;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    for (int i = 0; i < numDaysInPeriod; i++) {
      boolean nightShift = false;
      if (employee.NightShifts[i])
        nightShift = true; 
      if (nightShiftFound) {
        if (employee.DayType[i] != 1) {
          freeDays++;
        } else if (!nightShift || freeDays != 0) {
          if (freeDays < 2) {
            int pen = this.Weight * (2 - freeDays);
            penalty += pen;
            int lastNightshift = i - freeDays - 1;
            for (int day = lastNightshift + 2; day >= 0 && day < numDaysInPeriod && day >= lastNightshift; day--) {
              employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Minimum two free days required after night shifts. " + System.getProperty("line.separator"); 
            } 
          } 
          freeDays = 0;
          if (!nightShift)
            nightShiftFound = false; 
        } 
      } else if (nightShift) {
        nightShiftFound = true;
        freeDays = 0;
      } 
    } 
    return penalty;
  }
}
