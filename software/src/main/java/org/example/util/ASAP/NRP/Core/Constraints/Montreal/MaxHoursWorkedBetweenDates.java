package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxHoursWorkedBetweenDates implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxHoursWorkedBetweenDates(int Weight) {
    this.Title = "Max hours worked between two dates";
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
    if (employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates == null)
      return ""; 
    String str = "";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    for (int i = 0; i < employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates.length; i++) {
      int start = (employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates[i]).StartDay;
      int end = (employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates[i]).EndDay;
      int max = (employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates[i]).Hours;
      str = String.valueOf(str) + "Requests max " + max + " hours between " + schedulingPeriod.ConvertRosterDayToDate(start).ToString("yyyy-MM-dd") + " and " + schedulingPeriod.ConvertRosterDayToDate(end).ToString("yyyy-MM-dd") + "<br/> " + System.getProperty("line.separator");
    } 
    return str;
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
    if (employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates == null)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int shiftTypesCount = schedulingPeriod.ShiftTypesCount;
    int penalty = 0;
    for (int i = 0; i < employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates.length; i++) {
      int start = (employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates[i]).StartDay;
      int end = (employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates[i]).EndDay;
      int max = (employee.EmployeeDescription.Contract.MaxHoursWorkedBetweenDates[i]).Hours;
      double totHours = 0.0D;
      for (int j = start; j <= end; j++) {
        for (int k = 0; k < shiftTypesCount; k++) {
          if (employee.ShiftAssignments[j * shiftTypesCount + k])
            totHours += (schedulingPeriod.GetShiftType(k)).HoursWorked; 
        } 
      } 
      int tot = (int)totHours;
      if (tot > max) {
        int pen = (tot - max) * this.Weight;
        penalty += pen;
        for (int k = start; k <= end; k++) {
          if (employee.DayType[k] == 1) {
            employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Too many hours worked between days " + schedulingPeriod.ConvertRosterDayToDate(start).ToString("yyyy-MM-dd") + " and " + schedulingPeriod.ConvertRosterDayToDate(end).ToString("yyyy-MM-dd") + ". Requests max " + max + " hours (works " + tot + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
