package org.example.util.ASAP.NRP.Core.Constraints.Montreal;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.DaysOffRequestBetweenDates;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;

public class DaysOffRequestsBetweenDates implements SoftConstraint {
  public String Title = "Min/Max days off between two dates";
  
  public String LongTitle = "";
  
  public int Weight = 0;
  
  public String GetDescription(Employee employee) {
    return "";
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public String ID = "";
  
  public void Delete(EmployeeDescription employee) {}
  
  public String ToXml(EmployeeDescription employee) {
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
    if (employee.EmployeeDescription.DaysOffRequestBetweenDatesCount <= 0)
      return 0; 
    int penalty = 0;
    for (int i = 0; i < employee.EmployeeDescription.DaysOffRequestsBetweenDates.length; i++) {
      DaysOffRequestBetweenDates req = employee.EmployeeDescription.DaysOffRequestsBetweenDates[i];
      int count = 0;
      for (int j = req.StartDay; j <= req.EndDay; j++) {
        if (employee.DayType[j] != 1)
          count++; 
      } 
      if (count < req.Min) {
        int pen = req.Weight * (req.Min - count);
        penalty += pen;
        for (int k = req.StartDay; k <= req.EndDay; k++) {
          if (employee.DayType[k] == 1) {
            employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Too few days off days between " + employee.EmployeeDescription.SchedulingPeriod.ConvertRosterDayToDate(req.StartDay).ToString("yyyy-MM-dd") + " and " + employee.EmployeeDescription.SchedulingPeriod.ConvertRosterDayToDate(req.EndDay).ToString("yyyy-MM-dd") + ". Requests min " + req.Min + " days off (receives " + count + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } else if (count > req.Max) {
        int pen = req.Weight * (count - req.Max);
        penalty += pen;
        for (int k = req.StartDay; k <= req.EndDay; k++) {
          if (employee.DayType[k] != 1) {
            employee.ConstraintViolationPenalties[k] = employee.ConstraintViolationPenalties[k] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[k] = String.valueOf(employee.ViolationDescriptions[k]) + "Too many days off days between " + employee.EmployeeDescription.SchedulingPeriod.ConvertRosterDayToDate(req.StartDay).ToString("yyyy-MM-dd") + " and " + employee.EmployeeDescription.SchedulingPeriod.ConvertRosterDayToDate(req.EndDay).ToString("yyyy-MM-dd") + ". Requests max " + req.Max + " days off (receives " + count + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
