package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.Shift;

public class WorkSeparately implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public WorkSeparately(int weight) {
    this.Title = "Work separately";
    this.LongTitle = "";
    this.Weight = 0;
    this.ID = "";
    this.Weight = weight;
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
    String info = "Do not work at same time as: ";
    if (employee.EmployeeDescription.AvoidPartnershipsCount == 0)
      info = "No requirements"; 
    for (int i = 0; i < employee.EmployeeDescription.AvoidPartnershipsCount; i++) {
      EmployeeDescription desc = employee.EmployeeDescription.GetAvoidPartnership(i);
      info = String.valueOf(info) + desc.getName() + "; ";
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
    for (int day = 0; day < schedulingPeriod.NumDaysInPeriod; day++) {
      for (int index = 0; index < schedulingPeriod.ShiftTypesCount; index++) {
        Shift shift = employee.ShiftsOnDay[day][index];
        if (shift != null)
          for (int i = 0; i < employee.EmployeeDescription.AvoidPartnershipsCount; i++) {
            EmployeeDescription employee2Desc = employee.EmployeeDescription.GetAvoidPartnership(i);
            Employee employee2 = employee.Roster.GetEmployee(employee2Desc.ID);
            if (employee2 != null && employee2.WorksDuringPeriod(shift)) {
              penalty += this.Weight;
              employee.ConstraintViolationPenalties[shift.RosterDay] = employee.ConstraintViolationPenalties[shift.RosterDay] + this.Weight;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[shift.RosterDay] = String.valueOf(employee.ViolationDescriptions[shift.RosterDay]) + "Requests not to work at same time as employee '" + employee2Desc.getName() + "', penalty=" + this.Weight + ". " + System.getProperty("line.separator"); 
            } 
          }  
      } 
    } 
    return penalty;
  }
}
