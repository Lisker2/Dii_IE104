package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxWorkingWeekendsInFourWeeks implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxWorkingWeekendsInFourWeeks(int Weight) {
    this.Title = "Max working weekends in four weeks";
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
    return "Requests max " + employee.EmployeeDescription.Contract.MaxWorkingWeekendsInFourWeeks + 
      "  working weekends in four weeks.";
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
    int maxWorkingWeekendsInFourWeeks = employee.EmployeeDescription.Contract.MaxWorkingWeekendsInFourWeeks;
    boolean weekendWorkedThreeWeeksAgo = employee.EmployeeDescription.SchedulingHistory.WeekendWorkedThreeWeeksAgo;
    boolean weekendWorkedTwoWeeksAgo = employee.EmployeeDescription.SchedulingHistory.WeekendWorkedTwoWeeksAgo;
    boolean weekendWorkedOneWeekAgo = employee.EmployeeDescription.SchedulingHistory.WeekendWorkedOneWeekAgo;
    int numWeekendsInPeriod = schedulingPeriod.NumWeekendsInPeriod;
    boolean[] weekendWorked = new boolean[3 + numWeekendsInPeriod];
    weekendWorked[0] = weekendWorkedThreeWeeksAgo;
    weekendWorked[1] = weekendWorkedTwoWeeksAgo;
    weekendWorked[2] = weekendWorkedOneWeekAgo;
    int saturdayIndex = schedulingPeriod.FirstSaturday;
    if (saturdayIndex == 6 && 
      employee.DayType[0] == 1)
      weekendWorked[2] = true; 
    int i;
    for (i = 3; i < weekendWorked.length; i++) {
      if (employee.DayType[saturdayIndex] == 1) {
        weekendWorked[i] = true;
      } else if (saturdayIndex + 1 < employee.DayType.length && 
        employee.DayType[saturdayIndex + 1] == 1) {
        weekendWorked[i] = true;
      } 
      saturdayIndex += 7;
    } 
    for (i = 0; i < weekendWorked.length; i++) {
      int weekendsWorked = 0;
      for (int j = i; j < weekendWorked.length && j < i + 4; j++) {
        if (weekendWorked[j])
          weekendsWorked++; 
      } 
      if (weekendsWorked > maxWorkingWeekendsInFourWeeks) {
        int pen = this.Weight * (weekendsWorked - maxWorkingWeekendsInFourWeeks);
        penalty += pen;
        if (i <= 2 && 
          schedulingPeriod.FirstSaturday == 6 && 
          employee.DayType[0] == 1) {
          employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + pen;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "Too many working weekends in a four week period, (max " + maxWorkingWeekendsInFourWeeks + "). " + System.getProperty("line.separator"); 
        } 
        int start = (i < 3) ? 3 : i;
        for (int week = start; week < i + 4 && week < weekendWorked.length; week++) {
          int sat = schedulingPeriod.FirstSaturday + (week - 3) * 7;
          if (employee.DayType[sat] == 1) {
            employee.ConstraintViolationPenalties[sat] = employee.ConstraintViolationPenalties[sat] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[sat] = String.valueOf(employee.ViolationDescriptions[sat]) + "Too many working weekends in a four week period, (max " + maxWorkingWeekendsInFourWeeks + "). " + System.getProperty("line.separator"); 
          } 
          if (sat + 1 < employee.DayType.length && 
            employee.DayType[sat + 1] == 1) {
            employee.ConstraintViolationPenalties[sat + 1] = employee.ConstraintViolationPenalties[sat + 1] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[sat + 1] = String.valueOf(employee.ViolationDescriptions[sat + 1]) + "Too many working weekends in a four week period, (max " + maxWorkingWeekendsInFourWeeks + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
