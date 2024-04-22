package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;

public class MinHoursWorked implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MinHoursWorked(int Weight) {
    this.Title = "Min hours worked";
    this.LongTitle = "Minimum number of hours the employee can work";
    this.Weight = 0;
    this.ID = "ANROM.MinHoursWorked";
    this.Weight = Weight;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.MinHoursWorkedIsOn = false;
  }
  
  public String ToXml(EmployeeDescription employee) {
    return "";
  }
  
  public String GetDescription(Employee employee) {
    return "Requests min " + employee.EmployeeDescription.Contract.MinHoursWorked + 
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
    int penalty = 0;
    double standardPeformance = employee.EmployeeDescription.Contract.StandardPerformance;
    int numDaysOff = employee.HolidayDaysOffGranted;
    double previousHoursWorked = employee.EmployeeDescription.SchedulingHistory.PreviousOvertime;
    double x = employee.EmployeeDescription.Contract.MinHoursWorked - standardPeformance * numDaysOff;
    double y = previousHoursWorked + employee.HoursWorked;
    double diff = x - y;
    if (diff > employee.EmployeeDescription.Contract.MinHoursWorkedThreshold)
      penalty = this.Weight * (int)diff; 
    return penalty;
  }
}
