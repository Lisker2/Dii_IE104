package org.example.util.ASAP.NRP.Solvers.VariableDepthSearch;

import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.Shift;

public class SingleMoves {
  public static void VerticalSwap(Roster roster, int day, int blockSize, int employee1Index, int employee2Index) {
    boolean cacheViolationPens = true;
    if (employee1Index == employee2Index)
      return; 
    Employee employee1 = roster.Employees[employee1Index];
    while (day + blockSize >= roster.SchedulingPeriod.NumDaysInPeriod)
      blockSize--; 
    if (blockSize <= 0)
      return; 
    Shift[] shifts1 = new Shift[blockSize];
    roster.CacheEmployeePenalties(0, cacheViolationPens);
    for (int x = 0; x < blockSize; x++) {
      shifts1[x] = employee1.GetShift(day + x);
      roster.UnAssignShift(shifts1[x]);
    } 
    roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
    Employee employee2 = roster.Employees[employee2Index];
    Shift[] shifts2 = new Shift[blockSize];
    int i;
    for (i = 0; i < blockSize; i++)
      shifts2[i] = employee2.GetShift(day + i); 
    for (i = 0; i < blockSize; i++)
      roster.UnAssignShift(shifts2[i]); 
    roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
    boolean valid = true;
    int j;
    for (j = 0; j < blockSize; j++) {
      if (employee2.ViolationsForAssigningShift(shifts1[j]) == -1 || 
        employee1.ViolationsForAssigningShift(shifts2[j]) == -1) {
        valid = false;
        break;
      } 
    } 
    if (valid) {
      roster.CacheEmployeePenalties(1, cacheViolationPens);
      for (j = 0; j < blockSize; j++) {
        roster.AssignShift(employee2, shifts1[j]);
        roster.AssignShift(employee1, shifts2[j]);
      } 
      employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
      employee2.Roster.CoverPenalty += employee2.Roster.UpdateCoverPens(shifts1);
      employee1.RecalculatePenalty();
      employee2.RecalculatePenalty();
    } else {
      for (j = 0; j < blockSize; j++)
        roster.UnAssignShift(shifts1[j]); 
      roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
      for (j = 0; j < blockSize; j++)
        roster.AssignShift(employee2, shifts2[j]); 
      roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
      if (employee2.EmployeeDescription.Contract != null && 
        employee2.EmployeeDescription.Contract.BadPatternsIsOn && 
        employee2.EmployeeDescription.BadPatternConstraint != null)
        employee2.EmployeeDescription.BadPatternConstraint.Calculate(employee2); 
      for (j = 0; j < blockSize; j++)
        roster.AssignShift(employee1, shifts1[j]); 
      roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
      roster.RestoreEmployeePenalties(0, cacheViolationPens);
      if (employee1.EmployeeDescription.Contract != null && 
        employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
        employee1.EmployeeDescription.BadPatternConstraint != null)
        employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1); 
    } 
  }
}
