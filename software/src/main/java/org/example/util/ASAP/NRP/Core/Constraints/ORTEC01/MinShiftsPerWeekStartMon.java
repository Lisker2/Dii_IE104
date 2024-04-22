package org.example.util.ASAP.NRP.Core.Constraints.ORTEC01;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;

public class MinShiftsPerWeekStartMon implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinShiftsPerWeekStartMon(int Weight) {
    this.Title = "Min shifts per week";
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
    return "(Quadratic, weeks start on Monday)";
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
    int penalty = 0;
    int minShifts = employee.EmployeeDescription.Contract.MinShiftsPerWeekStartMon;
    int shiftCount = 0;
    int i;
    for (i = 0; i < 5; i++)
      shiftCount += employee.ShiftCountPerDay[i]; 
    if (shiftCount < minShifts) {
      int pen = this.Weight * (minShifts - shiftCount) * (minShifts - shiftCount);
      penalty += pen;
      for (int j = 0; j < 5; j++) {
        if (employee.DayType[j] != 1) {
          employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too few shifts this week (requires " + minShifts + "). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    shiftCount = 0;
    for (i = 5; i < 12; i++)
      shiftCount += employee.ShiftCountPerDay[i]; 
    if (shiftCount < minShifts) {
      int pen = this.Weight * (minShifts - shiftCount) * (minShifts - shiftCount);
      penalty += pen;
      for (int j = 5; j < 12; j++) {
        if (employee.DayType[j] != 1) {
          employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too few shifts this week (requires " + minShifts + "). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    shiftCount = 0;
    for (i = 12; i < 19; i++)
      shiftCount += employee.ShiftCountPerDay[i]; 
    if (shiftCount < minShifts) {
      int pen = this.Weight * (minShifts - shiftCount) * (minShifts - shiftCount);
      penalty += pen;
      for (int j = 12; j < 19; j++) {
        if (employee.DayType[j] != 1) {
          employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too few shifts this week (requires " + minShifts + "). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    shiftCount = 0;
    for (i = 19; i < 26; i++)
      shiftCount += employee.ShiftCountPerDay[i]; 
    if (shiftCount < minShifts) {
      int pen = this.Weight * (minShifts - shiftCount) * (minShifts - shiftCount);
      penalty += pen;
      for (int j = 19; j < 26; j++) {
        if (employee.DayType[j] != 1) {
          employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too few shifts this week (requires " + minShifts + "). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    shiftCount = 0;
    for (i = 26; i < 31; i++)
      shiftCount += employee.ShiftCountPerDay[i]; 
    if (shiftCount + 2 < minShifts) {
      int pen = this.Weight * (minShifts - shiftCount) * (minShifts - shiftCount);
      penalty += pen;
      for (int j = 26; j < 31; j++) {
        if (employee.DayType[j] != 1) {
          employee.ConstraintViolationPenalties[j] = employee.ConstraintViolationPenalties[j] + pen;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[j] = String.valueOf(employee.ViolationDescriptions[j]) + "Too few shifts this week (requires " + minShifts + "). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    return penalty;
  }
}
