package org.example.util.ASAP.NRP.Solvers.VariableDepthSearch;

import org.example.util.ASAP.NRP.Core.Constraints.ANROM.CompleteWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.IdenticalShiftTypesDuringWeekend;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxConsecutiveWorkingWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxNumAssignments;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxShiftTypes;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MinTimeBetweenShifts;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.NoNightShiftBeforeFreeWeekend;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.NumConsecutiveShiftTypes;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.RequestedDaysOff;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.RequestedDaysOn;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.RequestedShiftsOff;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.RequestedShiftsOn;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.ShiftTypeSuccessions;
import org.example.util.ASAP.NRP.Core.Constraints.GPost.MaxShiftsPerDay;
import org.example.util.ASAP.NRP.Core.Constraints.Ikegami.MaxWeekendsOff;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MaxConsecutiveFreeWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinConsecutiveFreeWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinConsecutiveWorkingWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.QMC.RequestedShiftGroupsOn;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.MaxShiftGroups;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.Shift;
import org.example.util.ASAP.NRP.Core.ShiftGroup;
import org.example.util.ASAP.NRP.Core.ShiftType;
import java.util.ArrayList;
import java.util.Random;

public class PatternTester {
  New1Emp1Swp_M n1;
  
  Hrz1Emp1Swp_M h1;
  
  boolean cacheViolationPens = true;
  
  int TEST_PATTERNS_MAX_ATTEMPTS = 5;
  
  boolean SATISFY_WEEKENDS = true;
  
  SoftConstraint[][] EmployeesWeekendConstraints;
  
  int[][] EmployeeOKshifts;
  
  final boolean SHOW_ERROR_MSGS = false;
  
  Random rand;
  
  TestShiftDB testShiftDB;
  
  public void ImprovePattern(Roster roster, TestShiftDB testShiftDB, int employeeIndex, Random rand) {
    this.testShiftDB = testShiftDB;
    if (roster.Employees.length == 0 || roster.SchedulingPeriod.ShiftTypesCount == 0)
      return; 
    roster.RecalculateAllPenalties();
    if (roster.getTotalPenalty() == 0)
      return; 
    this.rand = rand;
    this.n1 = new New1Emp1Swp_M(testShiftDB);
    this.n1.TIME_LIMIT = false;
    this.n1.SKIP_ZERO_PENS = false;
    this.n1.MAX_BLOCK_SIZE = 5;
    this.n1.VIOLATION_HEURISTIC_THRESHOLD = 0;
    this.n1.VERBOSE = false;
    this.h1 = new Hrz1Emp1Swp_M(testShiftDB);
    this.h1.MAX_BLOCK_SIZE = 5;
    this.h1.TIME_LIMIT = false;
    this.h1.VIOLATION_HEURISTIC_THRESHOLD = 0;
    this.h1.SKIP_ZERO_PEN = false;
    this.h1.VERBOSE = false;
    this.n1.setRNG(rand);
    this.h1.setRNG(rand);
    this.EmployeesWeekendConstraints = new SoftConstraint[roster.SchedulingPeriod.EmployeesCount][];
    for (int i = 0; i < roster.SchedulingPeriod.EmployeesCount; i++) {
      EmployeeDescription emp = roster.SchedulingPeriod.GetEmployeeDescription(i);
      CreateWeekendSoftConstraints(emp);
    } 
    SchedulingPeriod schedulingPeriod = roster.SchedulingPeriod;
    int shiftTypesCount = schedulingPeriod.ShiftTypesCount;
    this.EmployeeOKshifts = new int[roster.SchedulingPeriod.EmployeesCount][];
    for (int e = 0; e < roster.Employees.length; e++) {
      Employee employee1 = roster.Employees[e];
      ArrayList<Integer> okShifts = new ArrayList<Integer>();
      int j;
      for (j = 0; j < shiftTypesCount; j++) {
        if (employee1.EmployeeDescription.Contract != null) {
          int max = employee1.EmployeeDescription.Contract.MaxShiftTypes[j];
          if (max == 0)
            continue; 
        } 
        ShiftType st = schedulingPeriod.GetShiftType(j);
        if (st.AutoAllocate)
          if (!st.RequiresSkills || 
            schedulingPeriod.EmployeeHasSkillsForShiftType(employee1.EmployeeDescription, j)) {
            int[] groups = roster.SchedulingPeriod.GetShiftGroupsContainingShift(j);
            boolean permit = true;
            if (employee1.EmployeeDescription.Contract != null)
              for (int k = 0; k < groups.length; k++) {
                int max = employee1.EmployeeDescription.Contract.MaxShiftGroups[groups[k]];
                if (max == 0) {
                  permit = false;
                  break;
                } 
              }  
            if (permit)
              okShifts.add(new Integer(j)); 
          }  
        continue;
      } 
      this.EmployeeOKshifts[employee1.EmployeeDescription.IndexID] = new int[okShifts.size()];
      for (j = 0; j < okShifts.size(); j++)
        this.EmployeeOKshifts[employee1.EmployeeDescription.IndexID][j] = ((Integer)okShifts.get(j)).intValue(); 
    } 
    Employee employee = roster.Employees[employeeIndex];
    boolean[] bestPattern = TestPatternsHeuristically(employee, roster.getTotalPenalty(), 
        false, roster.getTotalPenalty(), 0);
    if (bestPattern != null) {
      int d;
      for (d = 0; d < roster.SchedulingPeriod.NumDaysInPeriod; d++) {
        for (int j = 0; j < roster.SchedulingPeriod.ShiftTypesCount; j++) {
          Shift shift = employee.ShiftsOnDay[d][j];
          if (shift != null)
            roster.UnAssignShift(shift); 
        } 
      } 
      for (d = 0; d < roster.SchedulingPeriod.NumDaysInPeriod; d++) {
        for (int j = 0; j < roster.SchedulingPeriod.ShiftTypesCount; j++) {
          int index = d * roster.SchedulingPeriod.ShiftTypesCount + j;
          if (bestPattern[index]) {
            Shift shift = (Shift)testShiftDB.GetTestShift(j, d).Clone();
            if (shift == null);
            roster.AssignShift(employee, shift);
          } 
        } 
      } 
      employee.RecalculatePenalty();
      roster.CoverPenalty = roster.RecalculateCoverPenalty();
    } 
  }
  
  private boolean[] TestPatternsHeuristically(Employee employee, int bestPenalty, boolean moveFound, int originalPenalty, int penaltyCachePos) {
    Roster roster = employee.Roster;
    int currentTotalPenalty = roster.getTotalPenalty();
    int currentEmployeesPenalty = roster.EmployeesPenalty;
    boolean[] bestPattern = (boolean[])null;
    boolean[] requestsPattern = (boolean[])null;
    Shift[] initialShifts = new Shift[employee.ShiftsCount];
    int pos = 0;
    for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod; day++) {
      for (int index = 0; index < roster.SchedulingPeriod.ShiftTypesCount; index++) {
        Shift shift = employee.ShiftsOnDay[day][index];
        if (shift != null) {
          if (shift.ShiftType.AutoAllocate && 
            !employee.EmployeeDescription.FrozenDay[day])
            roster.UnAssignShift(shift); 
          initialShifts[pos++] = shift;
        } 
      } 
    } 
    roster.CoverPenalty = roster.RecalculateCoverPenalty();
    SatisfyRequests(employee);
    if (this.SATISFY_WEEKENDS)
      SatisfyWeekends(employee, true, penaltyCachePos + 1); 
    employee.RecalculatePenalty();
    roster.CacheEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
    int i;
    for (i = 1; i <= this.TEST_PATTERNS_MAX_ATTEMPTS; i++) {
      if (requestsPattern == null) {
        requestsPattern = new boolean[employee.ShiftAssignments.length];
        System.arraycopy(employee.ShiftAssignments, 0, 
            requestsPattern, 0, 
            employee.ShiftAssignments.length);
      } else {
        for (int j = 0; j < roster.SchedulingPeriod.NumDaysInPeriod; j++) {
          for (int x = 0; x < roster.SchedulingPeriod.ShiftTypesCount; x++) {
            int index = j * roster.SchedulingPeriod.ShiftTypesCount + x;
            if (requestsPattern[index]) {
              Shift shift = (Shift)this.testShiftDB.GetTestShift(x, j).Clone();
              roster.AssignShift(employee, shift);
            } 
          } 
        } 
        roster.RestoreEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
        roster.CoverPenalty = roster.RecalculateCoverPenalty();
      } 
      ConstructPattern(employee, i, true, penaltyCachePos + 2);
      boolean valid = false;
      int e1Change = roster.EmployeesPenalty - currentEmployeesPenalty;
      if (roster.getTotalPenalty() < originalPenalty || 
        currentTotalPenalty + e1Change < originalPenalty)
        valid = true; 
      if (valid && (!moveFound || roster.getTotalPenalty() < bestPenalty)) {
        moveFound = true;
        bestPenalty = roster.getTotalPenalty();
        bestPattern = new boolean[employee.ShiftAssignments.length];
        System.arraycopy(employee.ShiftAssignments, 0, 
            bestPattern, 0, 
            employee.ShiftAssignments.length);
      } 
      employee.UnAssignAllShifts();
      roster.CoverPenalty = roster.RecalculateCoverPenalty();
    } 
    for (i = 0; i < initialShifts.length; i++)
      roster.AssignShift(employee, initialShifts[i]); 
    roster.CoverPenalty = roster.RecalculateCoverPenalty();
    employee.RecalculatePenalty();
    return bestPattern;
  }
  
  private void ConstructPattern(Employee employee, int maxLength, boolean calculateCoverPenalty, int cachePosition) {
    boolean cacheViolationPens = true;
    Roster roster = employee.Roster;
    int failedAttempts = 0;
    int[] shiftTypes = this.EmployeeOKshifts[employee.EmployeeDescription.IndexID];
    if (shiftTypes.length == 0)
      return; 
    boolean moveFound = true;
    while (moveFound) {
      moveFound = false;
      int bestPenalty = employee.Roster.getTotalPenalty();
      Shift[] bestShifts = (Shift[])null;
      for (int i = 0; i < shiftTypes.length; i++) {
        int shiftIndex = shiftTypes[i];
        for (int blockSize = 1; blockSize <= maxLength; blockSize++) {
          for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod - blockSize + 1; day++) {
            Shift[] shifts = new Shift[blockSize];
            for (int x = 0; x < blockSize; x++)
              shifts[x] = this.testShiftDB.GetTestShift(shiftIndex, day + x); 
            boolean valid = true;
            int j;
            for (j = 0; j < shifts.length; j++) {
              if (employee.ViolationsForAssigningShift(shifts[j]) == -1 || 
                employee.EmployeeDescription.FrozenDay[day + j]) {
                valid = false;
                break;
              } 
            } 
            if (valid) {
              roster.CacheEmployeePenalties(cachePosition, cacheViolationPens);
              for (j = 0; j < shifts.length; j++)
                roster.AssignShift(employee, shifts[j]); 
              if (calculateCoverPenalty)
                employee.Roster.CoverPenalty += employee.Roster.UpdateCoverPens(shifts); 
              employee.RecalculatePenalty(bestPenalty + 1, day, day + blockSize - 1, false);
              if (roster.getTotalPenalty() < bestPenalty || (
                roster.getTotalPenalty() == bestPenalty && this.rand.nextDouble() > 0.5D)) {
                bestShifts = shifts;
                bestPenalty = roster.getTotalPenalty();
              } 
              for (j = 0; j < shifts.length; j++)
                roster.UnAssignShift(shifts[j]); 
              roster.RestoreEmployeePenalties(cachePosition, cacheViolationPens);
              if (calculateCoverPenalty)
                roster.CoverPenalty += roster.UpdateCoverPens(shifts); 
            } 
          } 
        } 
      } 
      if (bestShifts != null) {
        Shift[] shifts = new Shift[bestShifts.length];
        for (int x = 0; x < bestShifts.length; x++) {
          shifts[x] = (Shift)bestShifts[x].Clone();
          //shifts[x];
          roster.AssignShift(employee, shifts[x]);
        } 
        if (calculateCoverPenalty)
          employee.Roster.CoverPenalty += employee.Roster.UpdateCoverPens(shifts); 
        employee.RecalculatePenalty();
        moveFound = true;
        continue;
      } 
      failedAttempts++;
    } 
    this.n1.TotalEvaluations = 0L;
    this.n1.cachePosition = cachePosition;
    this.n1.CALCULATE_COVER_PENALTY = calculateCoverPenalty;
    this.n1.SearchSingleEmployee(employee);
    this.h1.TotalEvaluations = 0L;
    this.h1.cachePosition = cachePosition;
    this.h1.CALCULATE_COVER_PENALTY = calculateCoverPenalty;
    this.h1.SearchSingleEmployee(employee);
  }
  
  private void SatisfyRequests(Employee employee) {
    if (employee.EmployeeDescription.ShiftOnRequestCount != 0) {
      Roster roster = employee.Roster;
      int stc = roster.SchedulingPeriod.ShiftTypesCount;
      for (int day = 0; day < employee.Roster.SchedulingPeriod.NumDaysInPeriod; day++) {
        if (!employee.EmployeeDescription.FrozenDay[day])
          for (int i = 0; i < stc; i++) {
            if (employee.EmployeeDescription.ShiftOnRequests[day * stc + i] != 0) {
              ShiftType st = roster.SchedulingPeriod.GetShiftType(i);
              if (st.AutoAllocate) {
                Shift shift = (Shift)this.testShiftDB.GetTestShift(st.Index, day).Clone();
                if (employee.ViolationsForAssigningShift(shift) != -1) {
                  roster.AssignShift(employee, shift);
                  roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
                } 
              } 
            } 
          }  
      } 
    } 
    if (employee.EmployeeDescription.ShiftGroupOnRequests != null) {
      Roster roster = employee.Roster;
      for (int i = 0; i < employee.EmployeeDescription.ShiftGroupOnRequests.length; i++) {
        int day = (employee.EmployeeDescription.ShiftGroupOnRequests[i]).Day;
        if (!employee.EmployeeDescription.FrozenDay[day]) {
          ShiftGroup group = (employee.EmployeeDescription.ShiftGroupOnRequests[i]).ShiftGroup;
          Shift bestShift = null;
          int bestCoverPenalty = 0;
          for (int j = 0; j < group.Group.length; j++) {
            Shift shift = this.testShiftDB.GetTestShift((group.Group[j]).Index, day);
            if (shift.ShiftType.AutoAllocate)
              if (employee.ViolationsForAssigningShift(shift) != -1) {
                roster.AssignShift(employee, shift);
                roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
                if (bestShift == null || roster.CoverPenalty < bestCoverPenalty) {
                  bestShift = shift;
                  bestCoverPenalty = roster.CoverPenalty;
                } 
                roster.UnAssignShift(shift);
                roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
              }  
          } 
          if (bestShift != null) {
            Shift shift = (Shift)bestShift.Clone();
            roster.AssignShift(employee, shift);
            roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
          } 
        } 
      } 
    } 
    if (employee.EmployeeDescription.DayOnRequestCount != 0) {
      Roster roster = employee.Roster;
      int stc = roster.SchedulingPeriod.ShiftTypesCount;
      for (int day = 0; day < employee.Roster.SchedulingPeriod.NumDaysInPeriod; day++) {
        if (!employee.EmployeeDescription.FrozenDay[day])
          if (employee.EmployeeDescription.DayOnRequests[day] != 0 && 
            employee.DayType[day] != 1) {
            Shift bestShift = null;
            int bestCoverPenalty = 0;
            for (int i = 0; i < stc; i++) {
              Shift shift = this.testShiftDB.GetTestShift(i, day);
              if (shift.ShiftType.AutoAllocate)
                if (employee.ViolationsForAssigningShift(shift) != -1) {
                  roster.AssignShift(employee, shift);
                  roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
                  if (bestShift == null || roster.CoverPenalty < bestCoverPenalty) {
                    bestShift = shift;
                    bestCoverPenalty = roster.CoverPenalty;
                  } 
                  roster.UnAssignShift(shift);
                  roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
                }  
            } 
            if (bestShift != null) {
              Shift shift = (Shift)bestShift.Clone();
              roster.AssignShift(employee, shift);
              roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
            } 
          }  
      } 
    } 
  }
  
  private void SatisfyWeekends(Employee employee, boolean calculateCoverPenalty, int cachePosition) {
    CalculateWeekendsPenalty(employee, -1);
    Roster roster = employee.Roster;
    int day;
    for (day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod; day++) {
      if (!employee.EmployeeDescription.FrozenDay[day]) {
        Shift bestShift1 = null;
        Shift bestShift2 = null;
        int bestPenalty = roster.EmployeesPenalty;
        for (int i = 0; i < roster.SchedulingPeriod.ShiftTypesCount; i++) {
          Shift shift1 = this.testShiftDB.GetTestShift(i, day);
          if (shift1.ShiftType.AutoAllocate)
            if (employee.ViolationsForAssigningShift(shift1) != -1) {
              roster.CacheEmployeePenalties(cachePosition, this.cacheViolationPens);
              roster.AssignShift(employee, shift1);
              if (calculateCoverPenalty)
                roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift1 }); 
              CalculateWeekendsPenalty(employee, -1);
              if (roster.EmployeesPenalty < bestPenalty) {
                bestShift1 = shift1;
                bestPenalty = roster.EmployeesPenalty;
              } 
              if (day + 1 < roster.SchedulingPeriod.NumDaysInPeriod && 
                !employee.EmployeeDescription.FrozenDay[day + 1]) {
                Shift shift2 = this.testShiftDB.GetTestShift(i, day + 1);
                if (employee.ViolationsForAssigningShift(shift2) != -1 && 
                  shift2.ShiftType.AutoAllocate) {
                  roster.AssignShift(employee, shift2);
                  if (calculateCoverPenalty)
                    roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift2 }); 
                  CalculateWeekendsPenalty(employee, bestPenalty);
                  if (roster.EmployeesPenalty < bestPenalty) {
                    bestShift1 = shift1;
                    bestShift2 = shift2;
                    bestPenalty = roster.EmployeesPenalty;
                  } 
                  roster.UnAssignShift(shift2);
                  if (calculateCoverPenalty)
                    roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift2 }); 
                } 
              } 
              roster.UnAssignShift(shift1);
              if (calculateCoverPenalty)
                roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift1 }); 
              roster.RestoreEmployeePenalties(cachePosition, this.cacheViolationPens);
            }  
        } 
        if (bestShift1 != null) {
          Shift shift1 = (Shift)bestShift1.Clone();
          roster.AssignShift(employee, shift1);
          if (calculateCoverPenalty)
            roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift1 }); 
          if (bestShift2 != null) {
            Shift shift2 = (Shift)bestShift2.Clone();
            roster.AssignShift(employee, shift2);
            if (calculateCoverPenalty)
              roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift2 }); 
          } 
          CalculateWeekendsPenalty(employee, -1);
        } 
      } 
    } 
    for (day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod; day++) {
      if (!employee.EmployeeDescription.FrozenDay[day] && 
        employee.DayType[day] == 1) {
        Shift shift = employee.GetShift(day);
        if (shift.ShiftType.AutoAllocate) {
          int origPen = roster.EmployeesPenalty;
          roster.CacheEmployeePenalties(cachePosition, this.cacheViolationPens);
          roster.UnAssignShift(shift);
          if (calculateCoverPenalty)
            roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift }); 
          CalculateWeekendsPenalty(employee, origPen + 1);
          if (roster.EmployeesPenalty > origPen) {
            roster.AssignShift(employee, shift);
            if (calculateCoverPenalty)
              roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift }); 
            roster.RestoreEmployeePenalties(cachePosition, this.cacheViolationPens);
          } 
        } 
      } 
    } 
  }
  
  public int CalculateWeekendsPenalty(Employee employee, int bestPenalty) {
    int originalPenalty = employee.Penalty;
    int newPenalty = 0;
    if (bestPenalty >= 0) {
      int diff = bestPenalty - employee.Roster.EmployeesPenalty - originalPenalty;
      for (int i = 0; i < (this.EmployeesWeekendConstraints[employee.EmployeeDescription.IndexID]).length; i++) {
        newPenalty += this.EmployeesWeekendConstraints[employee.EmployeeDescription.IndexID][i].Calculate(employee);
        if (newPenalty >= diff)
          break; 
      } 
    } else {
      for (int i = 0; i < (this.EmployeesWeekendConstraints[employee.EmployeeDescription.IndexID]).length; i++)
        newPenalty += this.EmployeesWeekendConstraints[employee.EmployeeDescription.IndexID][i].Calculate(employee); 
    } 
    employee.Roster.EmployeesPenalty = employee.Roster.EmployeesPenalty - originalPenalty + newPenalty;
    employee.Penalty = newPenalty;
    return employee.Roster.EmployeesPenalty;
  }
  
  private void CreateWeekendSoftConstraints(EmployeeDescription employee) {
    if (employee.Contract == null) {
      this.EmployeesWeekendConstraints[employee.IndexID] = new SoftConstraint[0];
      return;
    } 
    SchedulingPeriod schedulingPeriod = employee.SchedulingPeriod;
    ArrayList<SoftConstraint> array = new ArrayList<SoftConstraint>();
    if (employee.Contract.MaxShiftTypesIsOn) {
      int weight = schedulingPeriod.MasterWeights.MaxShiftTypes;
      if (employee.Contract.MaxShiftTypesWeight > -1)
        weight = employee.Contract.MaxShiftTypesWeight; 
      if (employee.Contract.MaxShiftTypesUsed)
        array.add(new MaxShiftTypes(weight)); 
      if (employee.Contract.MaxShiftGroupsUsed)
        array.add(new MaxShiftGroups(weight)); 
    } 
    if (schedulingPeriod.MasterWeights.MinTimeBetweenShifts > 0) {
      int weight = schedulingPeriod.MasterWeights.MinTimeBetweenShifts;
      array.add(new MinTimeBetweenShifts(weight));
    } 
    if (employee.Contract.MaxNumAssignmentsIsOn) {
      int weight = schedulingPeriod.MasterWeights.MaxNumAssignments;
      if (employee.Contract.MaxNumAssignmentsWeight > -1)
        weight = employee.Contract.MaxNumAssignmentsWeight; 
      array.add(new MaxNumAssignments(weight));
    } 
    if (employee.Contract.MaxShiftsPerDayIsOn) {
      int weight = schedulingPeriod.MasterWeights.MaxShiftsPerDay;
      if (employee.Contract.MaxShiftsPerDayWeight > -1)
        weight = employee.Contract.MaxShiftsPerDayWeight; 
      array.add(new MaxShiftsPerDay(weight));
    } 
    if (employee.Contract.MaxConsecutiveWorkingWeekendsIsOn) {
      int weight = schedulingPeriod.MasterWeights.MaxConsecutiveWorkingWeekends;
      if (employee.Contract.MaxConsecutiveWorkingWeekendsWeight > -1)
        weight = employee.Contract.MaxConsecutiveWorkingWeekendsWeight; 
      array.add(new MaxConsecutiveWorkingWeekends(weight));
    } 
    if (employee.Contract.MinConsecutiveWorkingWeekendsIsOn) {
      int weight = schedulingPeriod.MasterWeights.MinConsecutiveWorkingWeekends;
      if (employee.Contract.MinConsecutiveWorkingWeekendsWeight > -1)
        weight = employee.Contract.MinConsecutiveWorkingWeekendsWeight; 
      array.add(new MinConsecutiveWorkingWeekends(weight));
    } 
    if (employee.Contract.MaxConsecutiveFreeWeekendsIsOn) {
      int weight = schedulingPeriod.MasterWeights.MaxConsecutiveFreeWeekends;
      if (employee.Contract.MaxConsecutiveFreeWeekendsWeight > -1)
        weight = employee.Contract.MaxConsecutiveFreeWeekendsWeight; 
      array.add(new MaxConsecutiveFreeWeekends(weight));
    } 
    if (employee.Contract.MinConsecutiveFreeWeekendsIsOn) {
      int weight = schedulingPeriod.MasterWeights.MinConsecutiveFreeWeekends;
      if (employee.Contract.MinConsecutiveFreeWeekendsWeight > -1)
        weight = employee.Contract.MinConsecutiveFreeWeekendsWeight; 
      array.add(new MinConsecutiveFreeWeekends(weight));
    } 
    if (employee.Contract.CompleteWeekends) {
      int weight = schedulingPeriod.MasterWeights.CompleteWeekends;
      if (employee.Contract.CompleteWeekendsWeight > -1)
        weight = employee.Contract.CompleteWeekendsWeight; 
      array.add(new CompleteWeekends(weight));
    } 
    if (employee.Contract.IdenticalShiftTypesDuringWeekend) {
      int weight = schedulingPeriod.MasterWeights.IdenticalShiftTypesDuringWeekend;
      if (employee.Contract.IdenticalShiftTypesDuringWeekendWeight > -1)
        weight = employee.Contract.IdenticalShiftTypesDuringWeekendWeight; 
      array.add(new IdenticalShiftTypesDuringWeekend(weight));
    } 
    if (employee.Contract.NoNightShiftBeforeFreeWeekend) {
      int weight = schedulingPeriod.MasterWeights.NoNightShiftBeforeFreeWeekend;
      if (employee.Contract.NoNightShiftBeforeFreeWeekendWeight > -1)
        weight = employee.Contract.NoNightShiftBeforeFreeWeekendWeight; 
      array.add(new NoNightShiftBeforeFreeWeekend(weight));
    } 
    if (employee.Contract.ValidNumConsecutiveShiftTypesIsOn) {
      int weight = schedulingPeriod.MasterWeights.ValidNumConsecutiveShiftTypes;
      if (employee.Contract.ValidNumConsecutiveShiftTypesWeight > -1)
        weight = employee.Contract.ValidNumConsecutiveShiftTypesWeight; 
      array.add(new NumConsecutiveShiftTypes(weight));
    } 
    if (employee.Contract.ValidShiftTypeSuccessionsIsOn) {
      int weight = schedulingPeriod.MasterWeights.ValidShiftTypeSuccessions;
      if (employee.Contract.ValidShiftTypeSuccessionsWeight > -1)
        weight = employee.Contract.ValidShiftTypeSuccessionsWeight; 
      array.add(new ShiftTypeSuccessions(weight));
    } 
    if (employee.Contract.MaxWeekendsOffIsOn) {
      int weight;
      if (employee.Contract.MaxWeekendsOffWeight > -1) {
        weight = employee.Contract.MaxWeekendsOffWeight;
      } else {
        weight = schedulingPeriod.MasterWeights.MaxWeekendsOff;
      } 
      array.add(new MaxWeekendsOff(weight));
    } 
    if (employee.DayOffRequestCount > 0)
      array.add(new RequestedDaysOff()); 
    if (employee.DayOnRequestCount > 0)
      array.add(new RequestedDaysOn()); 
    if (employee.ShiftOffRequestCount > 0)
      array.add(new RequestedShiftsOff()); 
    if (employee.ShiftOnRequestCount > 0)
      array.add(new RequestedShiftsOn()); 
    if (employee.ShiftGroupOnRequests != null && 
      employee.ShiftGroupOnRequests.length > 0)
      array.add(new RequestedShiftGroupsOn()); 
    this.EmployeesWeekendConstraints[employee.IndexID] = 
      array.<SoftConstraint>toArray(new SoftConstraint[0]);
  }
}
