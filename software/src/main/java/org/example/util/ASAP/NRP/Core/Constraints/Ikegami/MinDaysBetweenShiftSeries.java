package org.example.util.ASAP.NRP.Core.Constraints.Ikegami;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class MinDaysBetweenShiftSeries implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinDaysBetweenShiftSeries(int Weight) {
    this.Title = "Min days between shift series";
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
    String info = "Min days between series of shifts";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
      ShiftType shiftType = schedulingPeriod.GetShiftType(x);
      String ID = shiftType.ID;
      int i = shiftType.Index;
      int min = employee.EmployeeDescription.Contract.MinDaysBetweenShiftSeries[i];
      if (min > 1)
        info = String.valueOf(info) + "<br/>Requests min " + min + " days between two series of '" + ID + "' shifts"; 
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
    for (int shiftIndex = 0; shiftIndex < shiftTypeCount; shiftIndex++) {
      int min = employee.EmployeeDescription.Contract.MinDaysBetweenShiftSeries[shiftIndex];
      if (min > 1) {
        int gap = employee.EmployeeDescription.SchedulingHistory.DaysSinceShiftType[shiftIndex];
        boolean firstFound = false;
        if (gap >= 0)
          firstFound = true; 
        int startDay = -1;
        for (int i = 0; i < numDaysInPeriod; i++) {
          if (employee.ShiftAssignments[i * shiftTypeCount + shiftIndex]) {
            firstFound = true;
            if (gap > 0 && gap < min) {
              int pen = (min - gap) * this.Weight;
              penalty += pen;
              if (startDay >= 0) {
                employee.ConstraintViolationPenalties[startDay] = employee.ConstraintViolationPenalties[startDay] + pen;
                if (SoftConstraints.UpdateViolationDescriptions) {
                  String label = (schedulingPeriod.GetShiftType(shiftIndex)).Label;
                  employee.ViolationDescriptions[startDay] = String.valueOf(employee.ViolationDescriptions[startDay]) + "Min " + min + " days between '" + label + "' shifts required." + System.getProperty("line.separator");
                } 
              } 
              employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + pen;
              if (SoftConstraints.UpdateViolationDescriptions) {
                String label = (schedulingPeriod.GetShiftType(shiftIndex)).Label;
                employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Min " + min + " days between '" + label + "' shifts required." + System.getProperty("line.separator");
              } 
            } 
            gap = 0;
            startDay = i;
          } else if (firstFound) {
            gap++;
          } 
        } 
      } 
    } 
    return penalty;
  }
}
