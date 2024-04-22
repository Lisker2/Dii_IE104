package org.example.util.ASAP.NRP.Solvers.Crossover;

import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.Shift;
import java.util.ArrayList;
import java.util.Collections;

public class Crossover {
  public static void MEH_Crossover(Roster schedule1, Roster schedule2, Roster offspring1, Roster offspring2, int MAX_BEST_ASSIGNMENTS) {
    Assignment[] s1Shifts = MEH_GetBestShifts(schedule1, MAX_BEST_ASSIGNMENTS);
    Assignment[] s2Shifts = MEH_GetBestShifts(schedule2, MAX_BEST_ASSIGNMENTS);
    int i;
    for (i = 0; i < s1Shifts.length; i++) {
      Assignment a = s1Shifts[i];
      Employee offspring1Employee = offspring1.GetEmployee(a.Shift.Employee.EmployeeDescription.ID);
      Shift shift1 = (Shift)a.Shift.Clone();
      if (offspring1Employee.ViolationsForAssigningShift(shift1) != -1)
        offspring1.AssignShift(offspring1Employee, shift1); 
      Employee offspring2Employee = offspring2.GetEmployee(a.Shift.Employee.EmployeeDescription.ID);
      Shift shift2 = (Shift)a.Shift.Clone();
      if (offspring2Employee.ViolationsForAssigningShift(shift2) != -1)
        offspring2.AssignShift(offspring2Employee, shift2); 
    } 
    for (i = 0; i < s2Shifts.length; i++) {
      Assignment a = s2Shifts[i];
      Employee offspring1Employee = offspring1.GetEmployee(a.Shift.Employee.EmployeeDescription.ID);
      Shift shift1 = (Shift)a.Shift.Clone();
      if (offspring1Employee.ViolationsForAssigningShift(shift1) != -1)
        offspring1.AssignShift(offspring1Employee, shift1); 
      Employee offspring2Employee = offspring2.GetEmployee(a.Shift.Employee.EmployeeDescription.ID);
      Shift shift2 = (Shift)a.Shift.Clone();
      if (offspring2Employee.ViolationsForAssigningShift(shift2) != -1)
        offspring2.AssignShift(offspring2Employee, shift2); 
    } 
    offspring1.RecalculateAllPenalties();
    offspring2.RecalculateAllPenalties();
  }
  
  private static Assignment[] MEH_GetBestShifts(Roster schedule, int MAX_BEST_ASSIGNMENTS) {
    ArrayList<Assignment> aList = new ArrayList<Assignment>();
    int origPen = schedule.getTotalPenalty();
    for (int i = 0; i < schedule.Employees.length; i++) {
      for (int day = 0; day < schedule.SchedulingPeriod.NumDaysInPeriod; day++) {
        for (int j = 0; j < schedule.SchedulingPeriod.ShiftTypesCount; j++) {
          Shift shift = (schedule.Employees[i]).ShiftsOnDay[day][j];
          if (shift != null) {
            Employee employee1 = shift.Employee;
            Shift[] shifts1 = new Shift[1];
            shifts1[0] = shift;
            schedule.CacheEmployeePenalties(0, false);
            schedule.UnAssignShift(shift);
            schedule.CoverPenalty += schedule.UpdateCoverPens(shifts1);
            employee1.RecalculatePenalty();
            int diff = schedule.getTotalPenalty() - origPen;
            schedule.AssignShift(employee1, shift);
            schedule.RestoreEmployeePenalties(0, false);
            schedule.CoverPenalty += schedule.UpdateCoverPens(shifts1);
            aList.add(new Assignment(shift, diff));
            Collections.sort(aList);
            while (aList.size() > MAX_BEST_ASSIGNMENTS)
              aList.remove(aList.size() - 1); 
          } 
        } 
      } 
    } 
    Assignment[] a = new Assignment[aList.size()];
    return aList.<Assignment>toArray(a);
  }
  
  public static void SS_Crossover(Roster schedule1, Roster schedule2, Roster newSchedule, Roster newSchedule2) {
    boolean matchFound;
    newSchedule.Empty();
    ArrayList<Vote> votes = new ArrayList<Vote>();
    Roster[] referenceSet = { schedule1, schedule2 };
    for (int z = 0; z < referenceSet.length; z++) {
      Roster schedule = referenceSet[z];
      Voter voter = new Voter(schedule);
      for (int i = 0; i < schedule.Employees.length; i++) {
        for (int day = 0; day < schedule.SchedulingPeriod.NumDaysInPeriod; day++) {
          for (int j = 0; j < schedule.SchedulingPeriod.ShiftTypesCount; j++) {
            Shift shift = (schedule.Employees[i]).ShiftsOnDay[day][j];
            if (shift != null) {
              boolean found = false;
              for (int x = 0; x < votes.size(); x++) {
                Vote vote = votes.get(x);
                if (vote.ScheduleDay == shift.RosterDay && 
                  vote.ShiftTypeID == shift.ShiftType.ID && 
                  vote.EmployeeID == shift.Employee.EmployeeDescription.ID) {
                  vote.Voters.add(voter);
                  found = true;
                  break;
                } 
              } 
              if (!found) {
                Vote vote = new Vote(shift.RosterDay, shift.ShiftType.ID, 
                    shift.Employee.EmployeeDescription.ID, shift);
                vote.Voters.add(voter);
                votes.add(vote);
              } 
            } 
          } 
        } 
      } 
    } 
    do {
      matchFound = false;
      Collections.sort(votes, Collections.reverseOrder());
      int index = 0;
      for (int i = 0; i < votes.size(); i++) {
        Vote vote = votes.get(i);
        Shift shift = (Shift)vote.Shift.Clone();
        Employee employee = newSchedule.GetEmployee(vote.EmployeeID);
        if (employee.ViolationsForAssigningShift(shift) != -1) {
          newSchedule.AssignShift(employee, shift);
          for (int j = 0; j < vote.Voters.size(); j++) {
            Voter voter = vote.Voters.get(j);
            voter.VotesSuccessful++;
          } 
          matchFound = true;
          break;
        } 
        index++;
      } 
      if (!matchFound)
        continue; 
      for (int x = 0; x <= index; x++)
        votes.remove(0); 
    } while (votes.size() != 0 && matchFound);
    newSchedule.RecalculateAllPenalties();
    SetRoster(newSchedule2, newSchedule);
  }
  
  public static void Crossover_A(Roster parent1, Roster parent2, Roster child1, Roster child2) {
    child1.Empty();
    for (int i = 0; i < parent1.Employees.length; i++) {
      Employee p1employee = parent1.Employees[i];
      Employee p2employee = parent2.GetEmployee(p1employee.EmployeeDescription.ID);
      for (int day = 0; day < parent1.SchedulingPeriod.NumDaysInPeriod; day++) {
        for (int j = 0; j < parent1.SchedulingPeriod.ShiftTypesCount; j++) {
          Shift shift = p1employee.ShiftsOnDay[day][j];
          if (shift != null)
            if (p2employee.ShiftsOnDay[day][j] != null) {
              Shift s = (Shift)shift.Clone();
              Employee c1employee = child1.GetEmployee(p1employee.EmployeeDescription.ID);
              if (c1employee.ViolationsForAssigningShift(s) != -1)
                child1.AssignShift(c1employee, s); 
            }  
        } 
      } 
    } 
    child1.RecalculateAllPenalties();
    SetRoster(child2, child1);
  }
  
  public static void SetRoster(Roster roster1, Roster roster2) {
    roster1.Empty();
    for (int i = 0; i < roster2.Employees.length; i++) {
      Employee employee2 = roster2.Employees[i];
      Employee employee1 = roster1.GetEmployee(employee2.EmployeeDescription.ID);
      for (int day = 0; day < roster2.SchedulingPeriod.NumDaysInPeriod; day++) {
        for (int j = 0; j < roster2.SchedulingPeriod.ShiftTypesCount; j++) {
          Shift shift2 = employee2.ShiftsOnDay[day][j];
          if (shift2 != null) {
            Shift shift1 = (Shift)shift2.Clone();
            if (shift1 != null)
              roster1.AssignShift(employee1, shift1); 
          } 
        } 
      } 
    } 
    roster1.RecalculateAllPenalties();
    roster1.getTotalPenalty();
    roster2.getTotalPenalty();
  }
}
