package org.example.util.ASAP.NRP.Core.Constraints.Azaiez;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxWeekendDays implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxWeekendDays(int Weight) {
    this.Title = "Max weekend days on";
    this.LongTitle = "Max weekend days on";
    this.Weight = 0;
    this.ID = "Azaiez.MaxWeekendDays";
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
    return "Requests max " + employee.EmployeeDescription.Contract.MaxWeekendDays + " weekend days (Sat and Sun) on";
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
    int sat, sun;
    if (this.Weight == 0)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    int day1 = 0;
    int day2 = 0;
    int day3 = 0;
    int day4 = 0;
    int day5 = 0;
    int day6 = 0;
    int day7 = 0;
    int numDays = employee.ShiftCountPerDay.length;
    for (int i = 0; i < numDays; i += 7) {
      day1 += employee.ShiftCountPerDay[i];
      if (i + 1 < numDays)
        day2 += employee.ShiftCountPerDay[i + 1]; 
      if (i + 2 < numDays)
        day3 += employee.ShiftCountPerDay[i + 2]; 
      if (i + 3 < numDays)
        day4 += employee.ShiftCountPerDay[i + 3]; 
      if (i + 4 < numDays)
        day5 += employee.ShiftCountPerDay[i + 4]; 
      if (i + 5 < numDays)
        day6 += employee.ShiftCountPerDay[i + 5]; 
      if (i + 6 < numDays)
        day7 += employee.ShiftCountPerDay[i + 6]; 
    } 
    switch (schedulingPeriod.StartDate.getDayOfWeek()) {
      case 2:
        sat = day6;
        sun = day7;
        break;
      case 3:
        sat = day5;
        sun = day6;
        break;
      case 4:
        sat = day4;
        sun = day5;
        break;
      case 5:
        sat = day3;
        sun = day4;
        break;
      case 6:
        sat = day2;
        sun = day3;
        break;
      case 7:
        sat = day1;
        sun = day2;
        break;
      case 1:
        sat = day7;
        sun = day1;
        break;
      default:
        sat = day6;
        sun = day7;
        break;
    } 
    int max = employee.EmployeeDescription.Contract.MaxWeekendDays;
    int tot = sat + sun;
    if (tot > max) {
      int pen = this.Weight * (tot - max);
      penalty += pen;
      int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
      int day;
      for (day = schedulingPeriod.FirstSaturday; day > 0 && day < numDaysInPeriod; day += 7) {
        if (employee.DayType[day] == 1) {
          employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments on weekend days (requests max " + max + ", receives " + tot + "). \n"; 
        } 
      } 
      for (day = schedulingPeriod.FirstSunday; day > 0 && day < numDaysInPeriod; day += 7) {
        if (employee.DayType[day] == 1) {
          employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments on weekend days (requests max " + max + ", receives " + tot + "). \n"; 
        } 
      } 
    } 
    return penalty;
  }
}
