package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.Shift;

public class Tutorship implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public Tutorship(int Weight) {
    this.Title = "Tutorship";
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
    String info = "Tutors: ";
    if (employee.EmployeeDescription.TutorCount == 0)
      info = "No tutors "; 
    for (int i = 0; i < employee.EmployeeDescription.TutorCount; i++) {
      EmployeeDescription tutorDesc = employee.EmployeeDescription.GetTutor(i);
      info = String.valueOf(info) + tutorDesc.getName() + "; ";
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
          for (int i = 0; i < employee.EmployeeDescription.TutorCount; i++) {
            EmployeeDescription tutorDesc = employee.EmployeeDescription.GetTutor(i);
            Employee tutor = employee.Roster.GetEmployee(tutorDesc.ID);
            if (tutor == null || !tutor.WorksDuringEntirePeriodOfShift(shift)) {
              penalty += this.Weight;
              employee.ConstraintViolationPenalties[shift.RosterDay] = employee.ConstraintViolationPenalties[shift.RosterDay] + this.Weight;
              if (SoftConstraints.UpdateViolationDescriptions)
                employee.ViolationDescriptions[shift.RosterDay] = String.valueOf(employee.ViolationDescriptions[shift.RosterDay]) + "Requests tutorship from '" + tutorDesc.getName() + "', penalty=" + this.Weight + ". " + System.getProperty("line.separator"); 
            } 
          }  
      } 
    } 
    return penalty;
  }
}
