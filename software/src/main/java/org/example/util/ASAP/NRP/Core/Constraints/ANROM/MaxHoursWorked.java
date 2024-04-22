package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxHoursWorked implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxHoursWorked(int Weight) {
    this.Title = "Max hours worked";
    this.LongTitle = "Maximum number of hours the employee can work";
    this.Weight = 0;
    this.ID = "ANROM.MaxHoursWorked";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MaxHoursWorkedIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "";
  }
  
  public String GetDescription(Employee employee) {
    return "Requests max " + employee.EmployeeDescription.Contract.MaxHoursWorked + 
      " hours.";
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
    double standardPeformance = employee.EmployeeDescription.Contract.StandardPerformance;
    int numDaysOff = employee.HolidayDaysOffGranted;
    double previousHoursWorked = employee.EmployeeDescription.SchedulingHistory.PreviousOvertime;
    double x = employee.EmployeeDescription.Contract.MaxHoursWorked - standardPeformance * numDaysOff;
    double y = previousHoursWorked + employee.HoursWorked;
    double diff = y - x;
    diff = roundDouble(diff, 4);
    if (diff > employee.EmployeeDescription.Contract.MaxHoursWorkedThreshold) {
      penalty = this.Weight * (int)Math.floor(diff);
      if (penalty > 0)
        for (int day = 0; day < schedulingPeriod.NumDaysInPeriod; day++) {
          if (employee.ShiftCountPerDay[day] > 0) {
            employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + penalty;
            if (SoftConstraints.UpdateViolationDescriptions)
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Too many hours worked (max " + employee.EmployeeDescription.Contract.MaxHoursWorked + "). " + System.getProperty("line.separator"); 
          } 
        }  
    } 
    return penalty;
  }
  
  public static final double roundDouble(double d, int places) {
    return Math.round(d * Math.pow(10.0D, places)) / Math.pow(10.0D, 
        places);
  }
}
