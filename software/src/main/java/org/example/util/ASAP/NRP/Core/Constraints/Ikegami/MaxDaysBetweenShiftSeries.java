package org.example.util.ASAP.NRP.Core.Constraints.Ikegami;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftType;

public class MaxDaysBetweenShiftSeries implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxDaysBetweenShiftSeries(int Weight) {
    this.Title = "Max days between shift series";
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
    String info = "Max days between series of shifts";
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    for (int x = 0; x < schedulingPeriod.ShiftTypesCount; x++) {
      ShiftType shiftType = schedulingPeriod.GetShiftType(x);
      String ID = shiftType.ID;
      int i = shiftType.Index;
      int max = employee.EmployeeDescription.Contract.MaxDaysBetweenShiftSeries[i];
      if (max > 0)
        info = String.valueOf(info) + "<br/>Requests max " + max + " days between two series of '" + ID + "' shifts"; 
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
      int max = employee.EmployeeDescription.Contract.MaxDaysBetweenShiftSeries[shiftIndex];
      if (max > 0) {
        int gap = employee.EmployeeDescription.SchedulingHistory.DaysSinceShiftType[shiftIndex];
        if (gap < 0 || gap > max)
          gap = 0; 
        int startDay = 0;
        for (int i = 0; i < numDaysInPeriod; i++) {
          boolean shiftWorked = employee.ShiftAssignments[i * shiftTypeCount + shiftIndex];
          boolean lastDay = (i == numDaysInPeriod - 1);
          if (lastDay && !shiftWorked)
            gap++; 
          if (shiftWorked || lastDay) {
            if (gap > 0 && gap > max) {
              int pen = (gap - max) * this.Weight;
              penalty += pen;
              int end = i;
              if (i == numDaysInPeriod - 1)
                end = i + 1; 
              for (int day = startDay; day < end; day++) {
                employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pen;
                if (SoftConstraints.UpdateViolationDescriptions) {
                  String label = (schedulingPeriod.GetShiftType(shiftIndex)).Label;
                  employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Max " + max + " days between '" + label + "' shifts required." + System.getProperty("line.separator");
                } 
              } 
            } 
            gap = 0;
            startDay = i + 1;
          } else {
            gap++;
          } 
        } 
      } 
    } 
    return penalty;
  }
}
