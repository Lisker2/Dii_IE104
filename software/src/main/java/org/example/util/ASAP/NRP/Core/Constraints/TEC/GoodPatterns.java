package org.example.util.ASAP.NRP.Core.Constraints.TEC;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;

public class GoodPatterns implements SoftConstraint {
  public String Title = "Good Patterns";
  
  public String LongTitle = "";
  
  public int Weight = 0;
  
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
  
  public String GetDescription(Employee employee) {
    String str = "<p>";
    for (int i = 0; i < employee.EmployeeDescription.Contract.GoodPatterns.length; i++) {
      PatternGroup grp = employee.EmployeeDescription.Contract.GoodPatterns[i];
      str = String.valueOf(str) + "At least one pattern required (Weight=" + grp.Weight + ") from :<br/>";
      for (int j = 0; j < grp.Patterns.length; j++) {
        Pattern pattern = grp.Patterns[j];
        if (pattern.StartDayType == Pattern.StartType.Day) {
          str = String.valueOf(str) + "Starting on " + pattern.getStartDayOrDate() + "s : ";
        } else if (pattern.StartDayType == Pattern.StartType.Date) {
          str = String.valueOf(str) + "Starting on " + pattern.getStartDayOrDate() + " : ";
        } else {
          str = String.valueOf(str) + "Starting on any day : ";
        } 
        str = String.valueOf(str) + pattern.toString();
        str = String.valueOf(str) + ". Penalty=" + pattern.Weight + "<br/>";
      } 
    } 
    return String.valueOf(str) + "</p>";
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
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    int penalty = 0;
    for (int grpIndex = 0; grpIndex < employee.EmployeeDescription.Contract.GoodPatterns.length; grpIndex++) {
      PatternGroup patternGrp = employee.EmployeeDescription.Contract.GoodPatterns[grpIndex];
      if (patternGrp.Weight > 0) {
        boolean patternFound = false;
        int j;
        for (j = 0; j < patternGrp.Patterns.length; j++) {
          Pattern pattern = patternGrp.Patterns[j];
          if (pattern.StartDay >= 0 || employee.EmployeeDescription.SchedulingHistory.PreviousDayType.length + pattern.StartDay >= 0) {
            boolean patternMatched = true;
            for (int k = 0; k < pattern.ShiftIndices.length; k++) {
              int index = pattern.ShiftIndices[k];
              int day = pattern.StartDay + k;
              if (day < 0) {
                day = employee.EmployeeDescription.SchedulingHistory.PreviousDayType.length + day;
                if (pattern.DayTypes[k] != Pattern.DayType.Any)
                  if (pattern.DayTypes[k] == Pattern.DayType.Off) {
                    if (employee.EmployeeDescription.SchedulingHistory.PreviousDayType[day] == 1) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.WorkingDay) {
                    if (employee.EmployeeDescription.SchedulingHistory.PreviousDayType[day] != 1) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.Shift) {
                    if (!employee.EmployeeDescription.SchedulingHistory.PreviousShiftAssignments[day * shiftTypeCount + index]) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.NotShift) {
                    if (employee.EmployeeDescription.SchedulingHistory.PreviousShiftAssignments[day * shiftTypeCount + index]) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.ShiftGroup) {
                    ShiftGroup grp = schedulingPeriod.GetShiftGroup(index);
                    boolean grpMatched = false;
                    for (int x = 0; x < grp.Group.length; x++) {
                      if (employee.EmployeeDescription.SchedulingHistory.PreviousShiftAssignments[day * shiftTypeCount + (grp.Group[x]).Index]) {
                        grpMatched = true;
                        break;
                      } 
                    } 
                    if (!grpMatched) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.OtherWork) {
                    if (employee.EmployeeDescription.SchedulingHistory.PreviousDayType[day] != 1) {
                      patternMatched = false;
                      break;
                    } 
                  }  
              } else {
                if (day >= schedulingPeriod.NumDaysInPeriod)
                  break; 
                if (pattern.DayTypes[k] != Pattern.DayType.Any)
                  if (pattern.DayTypes[k] == Pattern.DayType.Off) {
                    if (employee.DayType[day] == 1 || employee.EmployeeDescription.DayOffRequestIsWork[day]) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.WorkingDay) {
                    if (employee.DayType[day] != 1 && !employee.EmployeeDescription.DayOffRequestIsWork[day]) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.Shift) {
                    if (!employee.ShiftAssignments[day * shiftTypeCount + index]) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.NotShift) {
                    if (employee.ShiftAssignments[day * shiftTypeCount + index]) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.ShiftGroup) {
                    ShiftGroup grp = schedulingPeriod.GetShiftGroup(index);
                    boolean grpMatched = false;
                    for (int x = 0; x < grp.Group.length; x++) {
                      if (employee.ShiftAssignments[day * shiftTypeCount + (grp.Group[x]).Index]) {
                        grpMatched = true;
                        break;
                      } 
                    } 
                    if (!grpMatched) {
                      patternMatched = false;
                      break;
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.OtherWork) {
                    if (!employee.EmployeeDescription.DayOffRequestIsWork[day]) {
                      patternMatched = false;
                      break;
                    } 
                  }  
              } 
            } 
            if (patternMatched) {
              penalty += pattern.Weight;
              patternFound = true;
              break;
            } 
          } 
        } 
        if (!patternFound) {
          penalty += patternGrp.Weight;
          for (j = 0; j < patternGrp.Patterns.length; j++) {
            Pattern pattern = patternGrp.Patterns[j];
            for (int k = 0; k < pattern.ShiftIndices.length; k++) {
              int index = pattern.ShiftIndices[k];
              int day = pattern.StartDay + k;
              if (day >= 0) {
                if (day >= schedulingPeriod.NumDaysInPeriod)
                  break; 
                if (pattern.DayTypes[k] != Pattern.DayType.Any)
                  if (pattern.DayTypes[k] == Pattern.DayType.Off) {
                    if (employee.DayType[day] == 1 || employee.EmployeeDescription.DayOffRequestIsWork[day]) {
                      employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + patternGrp.Weight;
                      if (SoftConstraints.UpdateViolationDescriptions)
                        employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Pattern not matched (wants a day off)" + System.getProperty("line.separator"); 
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.WorkingDay) {
                    if (employee.DayType[day] != 1 && !employee.EmployeeDescription.DayOffRequestIsWork[day]) {
                      employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + patternGrp.Weight;
                      if (SoftConstraints.UpdateViolationDescriptions)
                        employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Pattern not matched (wants a day on)" + System.getProperty("line.separator"); 
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.Shift) {
                    if (!employee.ShiftAssignments[day * shiftTypeCount + index]) {
                      employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + patternGrp.Weight;
                      if (SoftConstraints.UpdateViolationDescriptions)
                        employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Pattern not matched (" + (schedulingPeriod.GetShiftType(index)).Label + " shift required)" + System.getProperty("line.separator"); 
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.NotShift) {
                    if (employee.ShiftAssignments[day * shiftTypeCount + index]) {
                      employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + patternGrp.Weight;
                      if (SoftConstraints.UpdateViolationDescriptions)
                        employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Pattern not matched (Not " + (schedulingPeriod.GetShiftType(index)).Label + " shift required)" + System.getProperty("line.separator"); 
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.ShiftGroup) {
                    ShiftGroup grp = schedulingPeriod.GetShiftGroup(index);
                    boolean grpMatched = false;
                    for (int x = 0; x < grp.Group.length; x++) {
                      if (employee.ShiftAssignments[day * shiftTypeCount + (grp.Group[x]).Index]) {
                        grpMatched = true;
                        break;
                      } 
                    } 
                    if (!grpMatched) {
                      employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + patternGrp.Weight;
                      if (SoftConstraints.UpdateViolationDescriptions)
                        employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Pattern not matched (wants a shift from group [" + grp.Label + "])" + System.getProperty("line.separator"); 
                    } 
                  } else if (pattern.DayTypes[k] == Pattern.DayType.OtherWork) {
                    if (!employee.EmployeeDescription.DayOffRequestIsWork[day]) {
                      employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + patternGrp.Weight;
                      if (SoftConstraints.UpdateViolationDescriptions)
                        employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Pattern not matched (wants other work)" + System.getProperty("line.separator"); 
                    } 
                  }  
              } 
            } 
          } 
        } 
      } 
    } 
    return penalty;
  }
}
