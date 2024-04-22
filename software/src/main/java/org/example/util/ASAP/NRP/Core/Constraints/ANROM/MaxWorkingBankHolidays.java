package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class MaxWorkingBankHolidays implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public MaxWorkingBankHolidays(int Weight) {
    this.Title = "Max working bank holidays";
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
    return "Requests max " + employee.EmployeeDescription.Contract.MaxWorkingBankHolidays + 
      " working bank holidays.";
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
    if (schedulingPeriod.getBankHolidayCount() == 0)
      return 0; 
    int penalty = 0;
    int maxWorkingBankHolidays = employee.EmployeeDescription.Contract.MaxWorkingBankHolidays;
    int workingBankHolidays = employee.EmployeeDescription.SchedulingHistory.PreviousWorkingBankHolidays;
    int i;
    for (i = 0; i < schedulingPeriod.BankHolidayArray.length; i++) {
      if (schedulingPeriod.BankHolidayArray[i] && 
        employee.DayType[i] == 1)
        workingBankHolidays++; 
    } 
    if (workingBankHolidays > maxWorkingBankHolidays) {
      penalty = this.Weight * (workingBankHolidays - maxWorkingBankHolidays);
      for (i = 0; i < schedulingPeriod.BankHolidayArray.length; i++) {
        if (schedulingPeriod.BankHolidayArray[i] && 
          employee.DayType[i] == 1) {
          employee.ConstraintViolationPenalties[i] = employee.ConstraintViolationPenalties[i] + penalty;
          if (SoftConstraints.UpdateViolationDescriptions)
            employee.ViolationDescriptions[i] = String.valueOf(employee.ViolationDescriptions[i]) + "Too many working bank holidays (max " + maxWorkingBankHolidays + ", works " + workingBankHolidays + " this year). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    return penalty;
  }
}
