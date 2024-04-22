package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class MinTimeBetweenShifts implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinTimeBetweenShifts(int Weight) {
    this.Title = "Min time between shifts";
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
    return "(Penalised per minute over)";
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
    int overlappingMinutes = 0;
    int shiftCount = schedulingPeriod.ShiftTypesCount;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    if (employee.EmployeeDescription.SchedulingHistory.LastDayType == 1 && 
      employee.DayType[0] == 1)
      for (int j = 0; j < shiftCount; j++) {
        if (employee.EmployeeDescription.SchedulingHistory.LastDayAssignments[j]) {
          ShiftType sh1 = schedulingPeriod.GetShiftType(j);
          for (int k = 0; k < shiftCount; k++) {
            if (employee.ShiftAssignments[k]) {
              ShiftType sh2 = schedulingPeriod.GetShiftType(k);
              int overlap = schedulingPeriod.OverlappingMinutes(sh1, sh2, false);
              overlappingMinutes += overlap;
              if (overlap > 0) {
                int pen = overlap * this.Weight;
                employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + pen;
                if (SoftConstraints.UpdateViolationDescriptions)
                  employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "Not enough rest time between shift '" + sh1.Label + "' on the previous day and '" + sh2.Label + "' on this day. " + System.getProperty("line.separator"); 
              } 
            } 
          } 
        } 
      }  
    for (int i = 0; i < numDaysInPeriod; i++) {
      if (employee.DayType[i] == 1) {
        int index = shiftCount * i;
        for (int j = 0; j < shiftCount; j++) {
          int shift1 = index + j;
          if (employee.ShiftAssignments[shift1]) {
            ShiftType sh1 = schedulingPeriod.GetShiftType(j);
            int k;
            for (k = j + 1; k < shiftCount; k++) {
              int shift2 = index + k;
              if (employee.ShiftAssignments[shift2]) {
                ShiftType sh2 = schedulingPeriod.GetShiftType(k);
                int overlap = schedulingPeriod.OverlappingMinutes(sh1, sh2, true);
                overlappingMinutes += overlap;
                if (overlap > 0) {
                  int pen = overlap * this.Weight;
                  employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + pen;
                  if (SoftConstraints.UpdateViolationDescriptions)
                    employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Not enough rest time between shifts '" + sh1.Label + "' and '" + sh2.Label + "' on this day. " + System.getProperty("line.separator"); 
                } 
              } 
            } 
            if (i + 1 < numDaysInPeriod && 
              employee.DayType[i + 1] == 1)
              for (k = 0; k < shiftCount; k++) {
                int shift2 = index + shiftCount + k;
                if (employee.ShiftAssignments[shift2]) {
                  ShiftType sh2 = schedulingPeriod.GetShiftType(k);
                  int overlap = schedulingPeriod.OverlappingMinutes(sh1, sh2, false);
                  overlappingMinutes += overlap;
                  if (overlap > 0) {
                    int pen = overlap * this.Weight;
                    employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + pen;
                    employee.ConstraintViolationPenalties[i + 1] = employee.ConstraintViolationPenalties[i + 1] + pen;
                    if (SoftConstraints.UpdateViolationDescriptions) {
                      employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Not enough rest time between shift '" + sh1.Label + "' on this day and '" + sh2.Label + "' on the next day. " + System.getProperty("line.separator");
                      employee.ViolationDescriptions[i + 1] = String.valueOf(employee.ViolationDescriptions[i + 1]) + "Not enough rest time between shift '" + sh1.Label + "' on the previous day and '" + sh2.Label + "' on this day. " + System.getProperty("line.separator");
                    } 
                  } 
                } 
              }  
          } 
        } 
      } 
    } 
    return overlappingMinutes * this.Weight;
  }
}
