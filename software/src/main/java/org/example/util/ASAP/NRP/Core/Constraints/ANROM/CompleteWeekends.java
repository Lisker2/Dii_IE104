package org.example.util.ASAP.NRP.Core.Constraints.ANROM;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Contract;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;

public class CompleteWeekends implements SoftConstraint {
  public String Title;
  
  public String LongTitle;
  
  public int Weight;
  
  public String ID;
  
  public CompleteWeekends(int Weight) {
    this.Title = "Complete weekends";
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
    return "";
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
    boolean previousSaturdayWorked = employee.EmployeeDescription.SchedulingHistory.PreviousSaturdayWorked;
    boolean previousSundayWorked = employee.EmployeeDescription.SchedulingHistory.PreviousSundayWorked;
    boolean previousSaturdayRequestedAbsence = employee.EmployeeDescription.SchedulingHistory.PreviousSaturdayRequestedHoliday;
    boolean previousSundayRequestedAbsence = employee.EmployeeDescription.SchedulingHistory.PreviousSundayRequestedHoliday;
    boolean fri = false;
    boolean sat = false;
    boolean sun = false;
    boolean mon = false;
    boolean absenceFri = false;
    boolean absenceSat = false;
    boolean absenceSun = false;
    boolean absenceMon = false;
    int saturdayIndex = schedulingPeriod.FirstSaturday;
    if (saturdayIndex == 0) {
      fri = true;
      if (employee.DayType[0] == 1) {
        sat = true;
      } else if (employee.DayType[0] != 0) {
        absenceSat = true;
      } 
      if (1 < employee.DayType.length)
        if (employee.DayType[1] == 1) {
          sun = true;
        } else if (employee.DayType[1] != 0) {
          absenceSun = true;
        }  
      if (2 < employee.DayType.length)
        if (employee.DayType[2] == 1) {
          mon = true;
        } else if (employee.DayType[2] != 0) {
          absenceMon = true;
        }  
      penalty += 

        
        CalculateCompleteWeekendsPenalty(employee, fri, sat, sun, mon, absenceFri, absenceSat, absenceSun, absenceMon, -1, 0, 1, 2);
      saturdayIndex += 7;
    } else if (saturdayIndex == 6) {
      fri = true;
      sat = previousSaturdayWorked;
      if (!sat && previousSaturdayRequestedAbsence)
        absenceSat = true; 
      if (employee.DayType[0] == 1) {
        sun = true;
      } else if (employee.DayType[0] != 0) {
        absenceSun = true;
      } 
      if (employee.DayType[1] == 1) {
        mon = true;
      } else if (employee.DayType[1] != 0) {
        absenceMon = true;
      } 
      penalty += 

        
        CalculateCompleteWeekendsPenalty(employee, fri, sat, sun, mon, absenceFri, absenceSat, absenceSun, absenceMon, -2, -1, 0, 1);
    } else if (saturdayIndex == 5) {
      fri = true;
      sat = previousSaturdayWorked;
      if (!sat && previousSaturdayRequestedAbsence)
        absenceSat = true; 
      sun = previousSundayWorked;
      if (!sun && previousSundayRequestedAbsence)
        absenceSun = true; 
      if (employee.DayType[0] == 1) {
        mon = true;
      } else if (employee.DayType[0] != 0) {
        absenceMon = true;
      } 
      penalty += 

        
        CalculateCompleteWeekendsPenalty(employee, fri, sat, sun, mon, absenceFri, absenceSat, absenceSun, absenceMon, -3, -2, -1, 0);
    } else if (saturdayIndex < 0) {
      if (schedulingPeriod.StartDate.getDayOfWeek() == 1) {
        fri = true;
        sat = previousSaturdayWorked;
        if (!sat && previousSaturdayRequestedAbsence)
          absenceSat = true; 
        if (employee.DayType[0] == 1) {
          sun = true;
        } else if (employee.DayType[0] != 0) {
          absenceSun = true;
        } 
        if (1 < employee.DayType.length)
          if (employee.DayType[1] == 1) {
            mon = true;
          } else if (employee.DayType[1] != 0) {
            absenceMon = true;
          }  
        penalty += 

          
          CalculateCompleteWeekendsPenalty(employee, fri, sat, sun, mon, absenceFri, absenceSat, absenceSun, absenceMon, -2, -1, 0, 1);
      } else if (schedulingPeriod.StartDate.getDayOfWeek() == 2) {
        fri = true;
        sat = previousSaturdayWorked;
        if (!sat && previousSaturdayRequestedAbsence)
          absenceSat = true; 
        sun = previousSundayWorked;
        if (!sun && previousSundayRequestedAbsence)
          absenceSun = true; 
        if (employee.DayType[0] == 1) {
          mon = true;
        } else if (employee.DayType[0] != 0) {
          absenceMon = true;
        } 
        penalty += 

          
          CalculateCompleteWeekendsPenalty(employee, fri, sat, sun, mon, absenceFri, absenceSat, absenceSun, absenceMon, -3, -2, -1, 0);
      } 
      return penalty;
    } 
    int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
    while (saturdayIndex + 1 < numDaysInPeriod) {
      fri = false;
      sat = false;
      sun = false;
      mon = false;
      absenceFri = false;
      absenceSat = false;
      absenceSun = false;
      absenceMon = false;
      if (employee.DayType[saturdayIndex - 1] == 1) {
        fri = true;
      } else if (employee.DayType[saturdayIndex - 1] != 0) {
        absenceMon = true;
      } 
      if (employee.DayType[saturdayIndex] == 1) {
        sat = true;
      } else if (employee.DayType[saturdayIndex] != 0) {
        absenceSat = true;
      } 
      if (employee.DayType[saturdayIndex + 1] == 1) {
        sun = true;
      } else if (employee.DayType[saturdayIndex + 1] != 0) {
        absenceSun = true;
      } 
      if (saturdayIndex + 2 < employee.DayType.length)
        if (employee.DayType[saturdayIndex + 2] == 1) {
          mon = true;
        } else if (employee.DayType[saturdayIndex + 2] != 0) {
          absenceMon = true;
        }  
      penalty += 

        
        CalculateCompleteWeekendsPenalty(employee, fri, sat, sun, mon, absenceFri, absenceSat, absenceSun, absenceMon, saturdayIndex - 1, saturdayIndex, saturdayIndex + 1, saturdayIndex + 2);
      saturdayIndex += 7;
    } 
    return penalty;
  }
  
  private int CalculateCompleteWeekendsPenalty(Employee employee, boolean fri, boolean sat, boolean sun, boolean mon, boolean absenceFri, boolean absenceSat, boolean absenceSun, boolean absenceMon, int friIndex, int satIndex, int sunIndex, int monIndex) {
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    int penalty = 0;
    Contract.WeekendDefinitions weekendDefinition = employee.EmployeeDescription.Contract.WeekendDefinition;
    int penaltyValue = this.Weight;
    boolean absence = !(!absenceSat && !absenceSun);
    if (weekendDefinition == Contract.WeekendDefinitions.SaturdaySunday) {
      if (sat != sun && !absence) {
        penalty = penaltyValue;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        if (satIndex >= 0 && satIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[satIndex] = employee.ConstraintViolationPenalties[satIndex] + penaltyValue; 
        if (sunIndex >= 0 && sunIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[sunIndex] = employee.ConstraintViolationPenalties[sunIndex] + penaltyValue; 
        if (SoftConstraints.UpdateViolationDescriptions) {
          if (satIndex >= 0 && satIndex < numDaysInPeriod)
            employee.ViolationDescriptions[satIndex] = String.valueOf(employee.ViolationDescriptions[satIndex]) + "Complete weekend required (weekend=Sat-Sun). " + System.getProperty("line.separator"); 
          if (sunIndex >= 0 && sunIndex < numDaysInPeriod)
            employee.ViolationDescriptions[sunIndex] = String.valueOf(employee.ViolationDescriptions[sunIndex]) + "Complete weekend required (weekend=Sat-Sun). " + System.getProperty("line.separator"); 
        } 
      } 
    } else if (weekendDefinition == Contract.WeekendDefinitions.FridaySaturdaySunday) {
      if ((sat != sun && !absence) || (
        !fri && !absenceFri && sat && sun)) {
        penalty = penaltyValue;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        if (friIndex >= 0 && friIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[friIndex] = employee.ConstraintViolationPenalties[friIndex] + penaltyValue; 
        if (satIndex >= 0 && satIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[satIndex] = employee.ConstraintViolationPenalties[satIndex] + penaltyValue; 
        if (sunIndex >= 0 && sunIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[sunIndex] = employee.ConstraintViolationPenalties[sunIndex] + penaltyValue; 
        if (SoftConstraints.UpdateViolationDescriptions) {
          if (friIndex >= 0 && friIndex < numDaysInPeriod)
            employee.ViolationDescriptions[friIndex] = String.valueOf(employee.ViolationDescriptions[friIndex]) + "Complete weekend required (weekend=Fri-Sun). " + System.getProperty("line.separator"); 
          if (satIndex >= 0 && satIndex < numDaysInPeriod)
            employee.ViolationDescriptions[satIndex] = String.valueOf(employee.ViolationDescriptions[satIndex]) + "Complete weekend required (weekend=Fri-Sun). " + System.getProperty("line.separator"); 
          if (sunIndex >= 0 && sunIndex < numDaysInPeriod)
            employee.ViolationDescriptions[sunIndex] = String.valueOf(employee.ViolationDescriptions[sunIndex]) + "Complete weekend required (weekend=Fri-Sun). " + System.getProperty("line.separator"); 
        } 
      } 
    } else if (weekendDefinition == Contract.WeekendDefinitions.FridaySaturdaySundayMonday) {
      if ((sat != sun && !absence) || (((!fri && !absenceFri) || (
        !mon && !absenceMon)) && sat && sun)) {
        penalty = penaltyValue;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        if (friIndex >= 0 && friIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[friIndex] = employee.ConstraintViolationPenalties[friIndex] + penaltyValue; 
        if (satIndex >= 0 && satIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[satIndex] = employee.ConstraintViolationPenalties[satIndex] + penaltyValue; 
        if (sunIndex >= 0 && sunIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[sunIndex] = employee.ConstraintViolationPenalties[sunIndex] + penaltyValue; 
        if (monIndex >= 0 && monIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[monIndex] = employee.ConstraintViolationPenalties[monIndex] + penaltyValue; 
        if (SoftConstraints.UpdateViolationDescriptions) {
          if (friIndex >= 0 && friIndex < numDaysInPeriod)
            employee.ViolationDescriptions[friIndex] = String.valueOf(employee.ViolationDescriptions[friIndex]) + "Complete weekend required (weekend=Fri-Mon). " + System.getProperty("line.separator"); 
          if (satIndex >= 0 && satIndex < numDaysInPeriod)
            employee.ViolationDescriptions[satIndex] = String.valueOf(employee.ViolationDescriptions[satIndex]) + "Complete weekend required (weekend=Fri-Mon). " + System.getProperty("line.separator"); 
          if (sunIndex >= 0 && sunIndex < numDaysInPeriod)
            employee.ViolationDescriptions[sunIndex] = String.valueOf(employee.ViolationDescriptions[sunIndex]) + "Complete weekend required (weekend=Fri-Mon). " + System.getProperty("line.separator"); 
          if (monIndex >= 0 && monIndex < numDaysInPeriod)
            employee.ViolationDescriptions[monIndex] = String.valueOf(employee.ViolationDescriptions[monIndex]) + "Complete weekend required (weekend=Fri-Mon). " + System.getProperty("line.separator"); 
        } 
      } 
    } else if (weekendDefinition == Contract.WeekendDefinitions.SaturdaySundayMonday) {
      if ((sat != sun && !absence) || (!mon && !absenceMon && sat && sun)) {
        penalty = penaltyValue;
        int numDaysInPeriod = schedulingPeriod.NumDaysInPeriod;
        if (satIndex >= 0 && satIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[satIndex] = employee.ConstraintViolationPenalties[satIndex] + penaltyValue; 
        if (sunIndex >= 0 && sunIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[sunIndex] = employee.ConstraintViolationPenalties[sunIndex] + penaltyValue; 
        if (monIndex >= 0 && monIndex < numDaysInPeriod)
          employee.ConstraintViolationPenalties[monIndex] = employee.ConstraintViolationPenalties[monIndex] + penaltyValue; 
        if (SoftConstraints.UpdateViolationDescriptions) {
          if (satIndex >= 0 && satIndex < numDaysInPeriod)
            employee.ViolationDescriptions[satIndex] = String.valueOf(employee.ViolationDescriptions[satIndex]) + "Complete weekend required (weekend=Sat-Mon). " + System.getProperty("line.separator"); 
          if (sunIndex >= 0 && sunIndex < numDaysInPeriod)
            employee.ViolationDescriptions[sunIndex] = String.valueOf(employee.ViolationDescriptions[sunIndex]) + "Complete weekend required (weekend=Sat-Mon). " + System.getProperty("line.separator"); 
          if (monIndex >= 0 && monIndex < numDaysInPeriod)
            employee.ViolationDescriptions[monIndex] = String.valueOf(employee.ViolationDescriptions[monIndex]) + "Complete weekend required (weekend=Sat-Mon). " + System.getProperty("line.separator"); 
        } 
      } 
    } 
    return penalty;
  }
}
