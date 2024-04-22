package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class ShiftTypeSuccessions implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public ShiftTypeSuccessions(int Weight) {
    this.Title = "Shift type successions";
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
    String info = "Which shifts may follow a shift the next day. (\"_\" = empty day).</br>" + System.getProperty("line.separator");
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    for (int i = -1; i < shiftTypeCount; i++) {
      String shift1Label = "_";
      if (i >= 0)
        shift1Label = (schedulingPeriod.GetShiftType(i)).Label; 
      info = String.valueOf(info) + shift1Label + " : ";
      for (int j = -1; j < shiftTypeCount; j++) {
        if (employee.EmployeeDescription.Contract.IsPermissibleSuccession(i, j)) {
          String shift2Label = "_";
          if (j >= 0)
            shift2Label = (schedulingPeriod.GetShiftType(j)).Label; 
          info = String.valueOf(info) + shift2Label + ",";
        } 
      } 
      info = String.valueOf(info) + "</br>" + System.getProperty("line.separator");
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
    if (this.Weight == 0)
      return 0; 
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    int shiftTypeCount = schedulingPeriod.ShiftTypesCount;
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    if (employee.EmployeeDescription.SchedulingHistory.Exists)
      if (employee.DayType[0] != 1) {
        if (employee.EmployeeDescription.SchedulingHistory.LastDayType != 1) {
          if (!employee.EmployeeDescription.Contract.IsPermissibleSuccession(-1, -1)) {
            penalty += this.Weight;
            employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + this.Weight;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "No shift followed by no shift is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator"); 
          } 
        } else {
          for (int j = 0; j < shiftTypeCount; j++) {
            if (employee.EmployeeDescription.SchedulingHistory.LastDayAssignments[j])
              if (!employee.EmployeeDescription.Contract.IsPermissibleSuccession(j, -1)) {
                penalty += this.Weight;
                employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + this.Weight;
                if (SoftConstraints.UpdateViolationDescriptions)
                  employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "'" + (schedulingPeriod.GetShiftType(j)).Label + "' followed by no shift is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator"); 
              }  
          } 
        } 
      } else if (employee.EmployeeDescription.SchedulingHistory.LastDayType != 1) {
        for (int j = 0; j < shiftTypeCount; j++) {
          if (employee.ShiftAssignments[j])
            if (!employee.EmployeeDescription.Contract.IsPermissibleSuccession(-1, j)) {
              penalty += this.Weight;
              employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + this.Weight;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "No shift followed by '" + (schedulingPeriod.GetShiftType(j)).Label + "' is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator"); 
            }  
        } 
      } else {
        for (int j = 0; j < shiftTypeCount; j++) {
          if (employee.ShiftAssignments[j])
            for (int k = 0; k < shiftTypeCount; k++) {
              if (employee.EmployeeDescription.SchedulingHistory.LastDayAssignments[k])
                if (!employee.EmployeeDescription.Contract.IsPermissibleSuccession(k, j)) {
                  penalty += this.Weight;
                  employee.ConstraintViolationPenalties[0] = employee.ConstraintViolationPenalties[0] + this.Weight;
                  if (SoftConstraints.UpdateViolationDescriptions)
                    employee.ViolationDescriptions[0] = String.valueOf(employee.ViolationDescriptions[0]) + "'" + (schedulingPeriod.GetShiftType(k)).Label + "' followed by '" + (schedulingPeriod.GetShiftType(j)).Label + "' is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator"); 
                }  
            }  
        } 
      }  
    for (int i = 1; i < numDaysInPeriod; i++) {
      if (employee.DayType[i] != 1) {
        if (employee.DayType[i - 1] != 1) {
          if (!employee.EmployeeDescription.Contract.IsPermissibleSuccession(-1, -1)) {
            penalty += this.Weight;
            employee.ConstraintViolationPenalties[i - 1] = employee.ConstraintViolationPenalties[i - 1] + this.Weight;
            employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + this.Weight;
            if (SoftConstraints.UpdateViolationDescriptions) {
              employee.ViolationDescriptions[i - 1] = String.valueOf(employee.ViolationDescriptions[i - 1]) + "No shift followed by no shift is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator");
              employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "No shift followed by no shift is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator");
            } 
          } 
        } else {
          for (int j = 0; j < shiftTypeCount; j++) {
            if (employee.ShiftAssignments[(i - 1) * shiftTypeCount + j])
              if (!employee.EmployeeDescription.Contract.IsPermissibleSuccession(j, -1)) {
                penalty += this.Weight;
                employee.ConstraintViolationPenalties[i - 1] = employee.ConstraintViolationPenalties[i - 1] + this.Weight;
                employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + this.Weight;
                if (SoftConstraints.UpdateViolationDescriptions) {
                  employee.ViolationDescriptions[i - 1] = String.valueOf(employee.ViolationDescriptions[i - 1]) + "'" + (schedulingPeriod.GetShiftType(j)).Label + "' followed by no shift is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator");
                  employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "'" + (schedulingPeriod.GetShiftType(j)).Label + "' followed by no shift is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator");
                } 
              }  
          } 
        } 
      } else if (employee.DayType[i - 1] != 1) {
        for (int j = 0; j < shiftTypeCount; j++) {
          if (employee.ShiftAssignments[i * shiftTypeCount + j])
            if (!employee.EmployeeDescription.Contract.IsPermissibleSuccession(-1, j)) {
              penalty += this.Weight;
              employee.ConstraintViolationPenalties[i - 1] = employee.ConstraintViolationPenalties[i - 1] + this.Weight;
              employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + this.Weight;
              if (SoftConstraints.UpdateViolationDescriptions) {
                employee.ViolationDescriptions[i - 1] = String.valueOf(employee.ViolationDescriptions[i - 1]) + "No shift followed by '" + (schedulingPeriod.GetShiftType(j)).Label + "' is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator");
                employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "No shift followed by '" + (schedulingPeriod.GetShiftType(j)).Label + "' is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator");
              } 
            }  
        } 
      } else {
        for (int j = 0; j < shiftTypeCount; j++) {
          if (employee.ShiftAssignments[i * shiftTypeCount + j])
            for (int k = 0; k < shiftTypeCount; k++) {
              if (employee.ShiftAssignments[(i - 1) * shiftTypeCount + k])
                if (!employee.EmployeeDescription.Contract.IsPermissibleSuccession(k, j)) {
                  penalty += this.Weight;
                  employee.ConstraintViolationPenalties[i - 1] = employee.ConstraintViolationPenalties[i - 1] + this.Weight;
                  employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + this.Weight;
                  if (SoftConstraints.UpdateViolationDescriptions) {
                    employee.ViolationDescriptions[i - 1] = String.valueOf(employee.ViolationDescriptions[i - 1]) + "'" + (schedulingPeriod.GetShiftType(k)).Label + "' followed by '" + (schedulingPeriod.GetShiftType(j)).Label + "' is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator");
                    employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "'" + (schedulingPeriod.GetShiftType(k)).Label + "' followed by '" + (schedulingPeriod.GetShiftType(j)).Label + "' is not permitted, penalty=" + this.Weight + ". " + System.getProperty("line.separator");
                  } 
                }  
            }  
        } 
      } 
    } 
    return penalty;
  }
}
