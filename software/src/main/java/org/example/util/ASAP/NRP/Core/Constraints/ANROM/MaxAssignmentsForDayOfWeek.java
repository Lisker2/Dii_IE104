package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxAssignmentsForDayOfWeek implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxAssignmentsForDayOfWeek(int Weight) {
    this.Title = "Max shifts for days of the week";
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
    boolean required = false;
    String info = "";
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllMondaysIsOn) {
      required = true;
      info = "Max total assignments for all Mondays = " + employee.EmployeeDescription.Contract.MaxAssignmentsForAllMondays;
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllTuesdaysIsOn) {
      if (required) {
        info = String.valueOf(info) + "<br/>";
      } else {
        required = true;
      } 
      info = String.valueOf(info) + "Max total assignments for all Tuesdays = " + employee.EmployeeDescription.Contract.MaxAssignmentsForAllTuesdays;
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllWednesdaysIsOn) {
      if (required) {
        info = String.valueOf(info) + "<br/>";
      } else {
        required = true;
      } 
      info = String.valueOf(info) + "Max total assignments for all Wednesdays = " + employee.EmployeeDescription.Contract.MaxAssignmentsForAllWednesdays;
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllThursdaysIsOn) {
      if (required) {
        info = String.valueOf(info) + "<br/>";
      } else {
        required = true;
      } 
      info = String.valueOf(info) + "Max total assignments for all Thursdays = " + employee.EmployeeDescription.Contract.MaxAssignmentsForAllThursdays;
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllFridaysIsOn) {
      if (required) {
        info = String.valueOf(info) + "<br/>";
      } else {
        required = true;
      } 
      info = String.valueOf(info) + "Max total assignments for all Fridays = " + employee.EmployeeDescription.Contract.MaxAssignmentsForAllFridays;
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllSaturdaysIsOn) {
      if (required) {
        info = String.valueOf(info) + "<br/>";
      } else {
        required = true;
      } 
      info = String.valueOf(info) + "Max total assignments for all Saturdays = " + employee.EmployeeDescription.Contract.MaxAssignmentsForAllSaturdays;
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllSundaysIsOn) {
      if (required) {
        info = String.valueOf(info) + "<br/>";
      } else {
        required = true;
      } 
      info = String.valueOf(info) + "Max total assignments for all Sundays = " + employee.EmployeeDescription.Contract.MaxAssignmentsForAllSundays;
    } 
    return info;
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
    int mon, tue, wed, thu, fri, sat, sun;
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
        mon = day1;
        tue = day2;
        wed = day3;
        thu = day4;
        fri = day5;
        sat = day6;
        sun = day7;
        break;
      case 3:
        mon = day7;
        tue = day1;
        wed = day2;
        thu = day3;
        fri = day4;
        sat = day5;
        sun = day6;
        break;
      case 4:
        mon = day6;
        tue = day7;
        wed = day1;
        thu = day2;
        fri = day3;
        sat = day4;
        sun = day5;
        break;
      case 5:
        mon = day5;
        tue = day6;
        wed = day7;
        thu = day1;
        fri = day2;
        sat = day3;
        sun = day4;
        break;
      case 6:
        mon = day4;
        tue = day5;
        wed = day6;
        thu = day7;
        fri = day1;
        sat = day2;
        sun = day3;
        break;
      case 7:
        mon = day3;
        tue = day4;
        wed = day5;
        thu = day6;
        fri = day7;
        sat = day1;
        sun = day2;
        break;
      case 1:
        mon = day2;
        tue = day3;
        wed = day4;
        thu = day5;
        fri = day6;
        sat = day7;
        sun = day1;
        break;
      default:
        mon = day1;
        tue = day2;
        wed = day3;
        thu = day4;
        fri = day5;
        sat = day6;
        sun = day7;
        break;
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllMondaysIsOn) {
      int max = employee.EmployeeDescription.Contract.MaxAssignmentsForAllMondays;
      if (mon > max) {
        int pen = this.Weight * (mon - max);
        penalty += pen;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        for (int day = schedulingPeriod.FirstMonday; day > 0 && day < numDaysInPeriod; day += 7) {
          if (employee.DayType[day] == 1) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments for all Mondays (requests max " + max + ", receives " + mon + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllTuesdaysIsOn) {
      int max = employee.EmployeeDescription.Contract.MaxAssignmentsForAllTuesdays;
      if (tue > max) {
        int pen = this.Weight * (tue - max);
        penalty += pen;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        for (int day = schedulingPeriod.FirstTuesday; day > 0 && day < numDaysInPeriod; day += 7) {
          if (employee.DayType[day] == 1) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments for all Tuesdays (requests max " + max + ", receives " + tue + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllWednesdaysIsOn) {
      int max = employee.EmployeeDescription.Contract.MaxAssignmentsForAllWednesdays;
      if (wed > max) {
        int pen = this.Weight * (wed - max);
        penalty += pen;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        for (int day = schedulingPeriod.FirstWednesday; day > 0 && day < numDaysInPeriod; day += 7) {
          if (employee.DayType[day] == 1) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments for all Wednesdays (requests max " + max + ", receives " + wed + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllThursdaysIsOn) {
      int max = employee.EmployeeDescription.Contract.MaxAssignmentsForAllThursdays;
      if (thu > max) {
        int pen = this.Weight * (thu - max);
        penalty += pen;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        for (int day = schedulingPeriod.FirstThursday; day > 0 && day < numDaysInPeriod; day += 7) {
          if (employee.DayType[day] == 1) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments for all Thursdays (requests max " + max + ", receives " + thu + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllFridaysIsOn) {
      int max = employee.EmployeeDescription.Contract.MaxAssignmentsForAllFridays;
      if (fri > max) {
        int pen = this.Weight * (fri - max);
        penalty += pen;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        for (int day = schedulingPeriod.FirstFriday; day > 0 && day < numDaysInPeriod; day += 7) {
          if (employee.DayType[day] == 1) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments for all Fridays (requests max " + max + ", receives " + fri + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllSaturdaysIsOn) {
      int max = employee.EmployeeDescription.Contract.MaxAssignmentsForAllSaturdays;
      if (sat > max) {
        int pen = this.Weight * (sat - max);
        penalty += pen;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        for (int day = schedulingPeriod.FirstSaturday; day > 0 && day < numDaysInPeriod; day += 7) {
          if (employee.DayType[day] == 1) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments for all Saturdays (requests max " + max + ", receives " + sat + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    if (employee.EmployeeDescription.Contract.MaxAssignmentsForAllSundaysIsOn) {
      int max = employee.EmployeeDescription.Contract.MaxAssignmentsForAllSundays;
      if (sun > max) {
        int pen = this.Weight * (sun - max);
        penalty += pen;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        for (int day = schedulingPeriod.FirstSunday; day > 0 && day < numDaysInPeriod; day += 7) {
          if (employee.DayType[day] == 1) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many assignments for all Sundays (requests max " + max + ", receives " + sun + "). " + System.getProperty("line.separator"); 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
