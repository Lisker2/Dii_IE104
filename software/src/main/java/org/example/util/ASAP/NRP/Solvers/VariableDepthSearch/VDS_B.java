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
import org.example.util.ASAP.NRP.Core.Constraints.Azaiez.MaxWeekendDays;
import org.example.util.ASAP.NRP.Core.Constraints.GPost.MaxShiftsPerDay;
import org.example.util.ASAP.NRP.Core.Constraints.Ikegami.MaxWeekendsOff;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MaxConsecutiveFreeWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinConsecutiveFreeWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinConsecutiveWorkingWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.QMC.RequestedShiftGroupsOn;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.MaxShiftGroups;
import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.Shift;
import org.example.util.ASAP.NRP.Core.ShiftGroup;
import org.example.util.ASAP.NRP.Core.ShiftType;
import org.example.util.ASAP.NRP.Solvers.Solver;
import java.util.ArrayList;
import java.util.Random;

public class VDS_B implements Solver, TestShiftDB {
  public void setStopped(boolean stopped) {
    this.Stopped = stopped;
  }
  
  public boolean getStopped() {
    return this.Stopped;
  }
  
  public String getAuthor() {
    return this.Author;
  }
  
  public int getRandomSeed() {
    return this.RandomSeed;
  }
  
  public long getTotalEvaluations() {
    return this.TotalEvaluations;
  }
  
  public void setRandomSeed(int seed) {
    this.RandomSeed = seed;
  }
  
  public String getTitle() {
    return this.Title;
  }
  
  public boolean Stopped = true;
  
  public String Author = "Tim";
  
  public String Title = "VDS_B";
  
  public long TotalEvaluations = 0L;
  
  public int RandomSeed = 1;
  
  public int PreferredRunTime = 5000;
  
  public boolean VERBOSE = true;
  
  public boolean SYSTEM_TIME_RANDOM_SEED = true;
  
  public boolean TIME_LIMIT = true;
  
  public boolean USE_QUICKSEARCH = true;
  
  public boolean POSITIVE_GAIN_HEURISTIC = true;
  
  public boolean VIOLATION_FLAG_HEURISTIC = false;
  
  public int MAX_DEPTH = 500;
  
  public int NEXT_MOVE_MAX_BLOCK_SIZE = 5;
  
  public int MAX_BLOCK_SIZE_AT_DEPTH_ZERO = 2;
  
  public int TEST_PATTERNS_MAX_ATTEMPTS = 2;
  
  public boolean TEST_PATTERNS_AT_START = true;
  
  public boolean SATISFY_WEEKENDS = true;
  
  public boolean TEST_PATTERNS_DURING_IMPROVE_EMPLOYEE = false;
  
  final boolean CYC_HEURISTIC = true;
  
  DateTime start;
  
  DateTime end;
  
  Random rand;
  
  boolean finished;
  
  int lastImprovementEmployee1;
  
  int lastImprovementEmployee2;
  
  int lastImprovementDay;
  
  int lastImprovementBlockSize;
  
  int lastImprovementShiftType;
  
  int lastImprovementType;
  
  final int TOTAL_IMPROVEMENT = 0;
  
  final int IMPROVE_EMPLOYEE_NEXT = 1;
  
  final int IMPROVE_COVER_NEXT = 2;
  
  final int NEW_SHIFTS = 3;
  
  final int BETWEEN_EMPLOYEES = 4;
  
  final int NEW_PATTERN = 5;
  
  int[][] movesMade;
  
  final int EMPLOYEE1 = 0;
  
  final int EMPLOYEE2 = 1;
  
  final int START_DAY = 2;
  
  final int BLOCK_SIZE = 3;
  
  final int TYPE = 4;
  
  final int SHIFT_INDEX = 5;
  
  Shift[][] TestShifts;
  
  Shift[][][] AllShifts;
  
  int[][] EmployeeOKshifts;
  
  int currentRosterHash;
  
  int[][][] hashRandomValues;
  
  int[] hashEmployeeRandVals;
  
  int[] hashDaysRandVals;
  
  int[] HashKeyArray;
  
  SoftConstraint[][] EmployeesWeekendConstraints;
  
  boolean cacheViolationPens = false;
  
  boolean updateViolationFlags = false;
  
  New1Emp1Swp_M n1;
  
  Hrz1Emp1Swp_M h1;
  
  Roster bestRoster = null;
  
  int guiBestPenalty;
  
  public boolean SchedulingPeriodContainsNonAutoAssignShifts = false;
  
  public boolean[] EmployeeDescriptionHasFrozenDays;
  
  final boolean SHOW_ERROR_MSGS = false;
  
  boolean randSet = false;
  
  public void setRNG(Random rand) {
    this.rand = rand;
    this.n1.setRNG(rand);
    this.h1.setRNG(rand);
    this.randSet = true;
  }
  
  public VDS_B() {
    UpdateTitle();
    this.n1 = new New1Emp1Swp_M(this);
    this.n1.TIME_LIMIT = false;
    this.n1.SKIP_ZERO_PENS = false;
    this.n1.MAX_BLOCK_SIZE = 5;
    this.n1.VIOLATION_HEURISTIC_THRESHOLD = 0;
    this.n1.RandomSeed = this.RandomSeed;
    this.n1.VERBOSE = false;
    this.h1 = new Hrz1Emp1Swp_M(this);
    this.h1.MAX_BLOCK_SIZE = 5;
    this.h1.RandomSeed = this.RandomSeed;
    this.h1.TIME_LIMIT = false;
    this.h1.VIOLATION_HEURISTIC_THRESHOLD = 0;
    this.h1.SKIP_ZERO_PEN = false;
    this.h1.VERBOSE = false;
  }
  
  public void Solve(Roster roster) {
    if (roster.Employees.length == 0 || roster.SchedulingPeriod.ShiftTypesCount == 0)
      return; 
    roster.RecalculateAllPenalties();
    if (roster.getTotalPenalty() == 0)
      return; 
    if (this.SYSTEM_TIME_RANDOM_SEED)
      this.RandomSeed = (int)DateTime.getNow().getTicks(); 
    if (!this.randSet) {
      this.rand = new Random(this.RandomSeed);
      this.n1.setRNG(this.rand);
      this.h1.setRNG(this.rand);
    } 
    this.Stopped = false;
    this.TotalEvaluations = 0L;
    this.guiBestPenalty = -1;
    this.SchedulingPeriodContainsNonAutoAssignShifts = false;
    int i;
    for (i = 0; i < roster.SchedulingPeriod.ShiftTypesCount; i++) {
      if (!(roster.SchedulingPeriod.GetShiftType(i)).AutoAllocate) {
        this.SchedulingPeriodContainsNonAutoAssignShifts = true;
        break;
      } 
    } 
    this.EmployeeDescriptionHasFrozenDays = new boolean[roster.SchedulingPeriod.EmployeesCount];
    for (i = 0; i < roster.SchedulingPeriod.EmployeesCount; i++) {
      EmployeeDescription emp = roster.SchedulingPeriod.GetEmployeeDescription(i);
      for (int m = 0; m < emp.FrozenDay.length; m++) {
        if (emp.FrozenDay[m]) {
          this.EmployeeDescriptionHasFrozenDays[emp.IndexID] = true;
          break;
        } 
      } 
    } 
    this.EmployeesWeekendConstraints = new SoftConstraint[roster.SchedulingPeriod.EmployeesCount][];
    for (i = 0; i < roster.SchedulingPeriod.EmployeesCount; i++) {
      EmployeeDescription emp = roster.SchedulingPeriod.GetEmployeeDescription(i);
      CreateWeekendSoftConstraints(emp);
    } 
    CheckAlgorithmParameters(roster);
    UpdateTitle();
    SchedulingPeriod schedulingPeriod = roster.SchedulingPeriod;
    this.AllShifts = new Shift[schedulingPeriod.NumDaysInPeriod][schedulingPeriod.ShiftTypesCount][roster.Employees.length];
    this.TestShifts = new Shift[schedulingPeriod.NumDaysInPeriod][schedulingPeriod.ShiftTypesCount];
    for (int j = 0; j < schedulingPeriod.NumDaysInPeriod; j++) {
      DateTime date = schedulingPeriod.ConvertRosterDayToDate(j);
      for (int m = 0; m < schedulingPeriod.ShiftTypesCount; m++) {
        ShiftType st = schedulingPeriod.GetShiftType(m);
        this.TestShifts[j][m] = new Shift(st, date, schedulingPeriod);
        for (int n = 0; n < roster.Employees.length; n++)
          this.AllShifts[j][m][n] = new Shift(st, date, schedulingPeriod); 
      } 
    } 
    this.hashRandomValues = new int[roster.Employees.length][schedulingPeriod.NumDaysInPeriod][schedulingPeriod.ShiftTypesCount];
    this.hashEmployeeRandVals = new int[roster.Employees.length];
    this.hashDaysRandVals = new int[schedulingPeriod.NumDaysInPeriod];
    for (int e = 0; e < roster.Employees.length; e++) {
      this.hashEmployeeRandVals[e] = this.rand.nextInt(2147483647);
      for (int m = 0; m < schedulingPeriod.NumDaysInPeriod; m++) {
        for (int s = 0; s < schedulingPeriod.ShiftTypesCount; s++)
          this.hashRandomValues[e][m][s] = this.rand.nextInt(2147483647); 
      } 
    } 
    for (int day = 0; day < schedulingPeriod.NumDaysInPeriod; day++)
      this.hashDaysRandVals[day] = this.rand.nextInt(2147483647); 
    int shiftTypesCount = schedulingPeriod.ShiftTypesCount;
    this.EmployeeOKshifts = new int[roster.SchedulingPeriod.EmployeesCount][];
    for (int k = 0; k < roster.Employees.length; k++) {
      Employee employee = roster.Employees[k];
      ArrayList<Integer> okShifts = new ArrayList<Integer>();
      int m;
      for (m = 0; m < shiftTypesCount; m++) {
        if (employee.EmployeeDescription.Contract != null) {
          int max = employee.EmployeeDescription.Contract.MaxShiftTypes[m];
          if (max == 0)
            continue; 
        } 
        ShiftType st = schedulingPeriod.GetShiftType(m);
        if (st.AutoAllocate)
          if (!st.RequiresSkills || 
            schedulingPeriod.EmployeeHasSkillsForShiftType(employee.EmployeeDescription, m)) {
            int[] groups = roster.SchedulingPeriod.GetShiftGroupsContainingShift(m);
            boolean permit = true;
            if (employee.EmployeeDescription.Contract != null)
              for (int n = 0; n < groups.length; n++) {
                int max = employee.EmployeeDescription.Contract.MaxShiftGroups[groups[n]];
                if (max == 0) {
                  permit = false;
                  break;
                } 
              }  
            if (permit)
              okShifts.add(new Integer(m)); 
          }  
        continue;
      } 
      this.EmployeeOKshifts[employee.EmployeeDescription.IndexID] = new int[okShifts.size()];
      for (m = 0; m < okShifts.size(); m++)
        this.EmployeeOKshifts[employee.EmployeeDescription.IndexID][m] = ((Integer)okShifts.get(m)).intValue(); 
    } 
    this.movesMade = new int[this.MAX_DEPTH][6];
    this.start = DateTime.getNow();
    this.end = DateTime.getNow().AddMilliseconds(this.PreferredRunTime);
    Roster originalRoster = (Roster)roster.Clone();
    Search(roster);
    if (this.TIME_LIMIT && roster.getTotalPenalty() != 0) {
      this.bestRoster = (Roster)roster.Clone();
      while (!this.Stopped && this.bestRoster.getTotalPenalty() != 0 && 
        DateTime.getNow().isLessThan(this.end)) {
        SetRoster(roster, originalRoster);
        Search(roster);
        if (roster.getTotalPenalty() < this.bestRoster.getTotalPenalty())
          this.bestRoster = (Roster)roster.Clone(); 
      } 
      SetRoster(roster, this.bestRoster);
    } 
    if (this.VERBOSE)
      System.out.println("Search finished. Total moves tested=" + this.TotalEvaluations + " Pen=" + roster.getTotalPenalty()); 
  }
  
  private void Search(Roster roster) {
    this.cacheViolationPens = false;
    this.updateViolationFlags = false;
    if (this.VIOLATION_FLAG_HEURISTIC) {
      this.cacheViolationPens = true;
      this.updateViolationFlags = true;
    } 
    this.lastImprovementEmployee1 = -1;
    this.lastImprovementEmployee2 = -1;
    this.lastImprovementDay = -1;
    this.lastImprovementBlockSize = -1;
    this.lastImprovementType = -1;
    this.lastImprovementShiftType = -2;
    this.finished = false;
    ShuffleEmployees(roster);
    if (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end))
      return; 
    int movesCount = SwapShiftsDepthOne(roster);
    if (this.VERBOSE) {
      System.out.println("\nTotal moves tested=" + this.TotalEvaluations + ", totalImprovingMoves=" + movesCount);
      System.out.println("Variable depth search finished. Total moves tested=" + this.TotalEvaluations + ", totalImprovingMoves=" + movesCount);
    } 
  }
  
  private int SwapShiftsDepthOne(Roster roster) {
    int startMovesCount, maxHashSize = (roster.SchedulingPeriod.ShiftTypesCount + 1) * this.MAX_DEPTH + (roster.Employees.length - 1) * this.MAX_DEPTH * 3 + 1;
    ResetHashTable(maxHashSize);
    if (this.TEST_PATTERNS_AT_START)
      ConstructRoster(roster); 
    if (roster.getTotalPenalty() == 0 || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end)))
      return 0; 
    if (this.USE_QUICKSEARCH)
      QuickSearch(roster); 
    roster.RecalculateAllPenalties();
    if (roster.getTotalPenalty() == 0 || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end)))
      return 0; 
    this.currentRosterHash = CalculateHash(roster);
    AddHashKey(this.currentRosterHash);
    int movesCount = 0;
    do {
      startMovesCount = movesCount;
      for (int blockSize = 1; blockSize <= this.MAX_BLOCK_SIZE_AT_DEPTH_ZERO; blockSize++) {
        for (int i = 0; i < roster.Employees.length; i++) {
          Employee employee1 = roster.Employees[i];
          for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod - blockSize + 1; day++) {
            if (this.VERBOSE)
              System.out.println("VDSB: Swp..D1: BlckSze=" + blockSize + 
                  " E1=" + employee1.EmployeeDescription.getName() + 
                  " Day=" + day + 
                  " Evals=" + this.TotalEvaluations + 
                  " Pen=" + roster.getTotalPenalty() + 
                  " EPen=" + roster.EmployeesPenalty + 
                  " CovPen=" + roster.CoverPenalty); 
            GuiPrint("Stage : VDS", roster);
            if (this.Stopped || roster.getTotalPenalty() == 0)
              return movesCount; 
            if (this.lastImprovementDay == day && 
              this.lastImprovementEmployee1 == employee1.Index && 
              this.lastImprovementBlockSize == blockSize && 
              this.lastImprovementType == 3)
              return movesCount; 
            boolean coverViolations = false;
            for (int x = 0; x < blockSize; x++) {
              if (roster.CoverPenOnDay[day + x] > 0) {
                coverViolations = true;
                break;
              } 
            } 
            if (employee1.Penalty == 0 && !coverViolations)
              continue; 
            if (this.SchedulingPeriodContainsNonAutoAssignShifts) {
              boolean fixedShifts = false;
              for (int m = 0; m < blockSize; m++) {
                Shift s = employee1.GetShift(day + m);
                if (s != null && s.isFixed()) {
                  fixedShifts = true;
                  break;
                } 
              } 
              if (fixedShifts)
                continue; 
            } 
            if (this.EmployeeDescriptionHasFrozenDays[employee1.EmployeeDescription.IndexID]) {
              boolean frozen = false;
              for (int m = 0; m < blockSize; m++) {
                if (employee1.EmployeeDescription.FrozenDay[day + m]) {
                  frozen = true;
                  break;
                } 
              } 
              if (frozen)
                continue; 
            } 
            boolean employee1Violations = false;
            if (this.VIOLATION_FLAG_HEURISTIC) {
              for (int m = 0; m < blockSize; m++) {
                if (employee1.ConstraintViolationPenalties[day + m] > 0) {
                  employee1Violations = true;
                  break;
                } 
              } 
              if (!coverViolations && 
                !employee1Violations)
                continue; 
            } 
            boolean moveMade = false;
            int originalPenalty = roster.getTotalPenalty();
            int originalCoverPenalty = roster.CoverPenalty;
            int originalEmployeesPenalty = roster.EmployeesPenalty;
            int[] originalCoverPenOnDay = new int[roster.SchedulingPeriod.NumDaysInPeriod];
            System.arraycopy(roster.CoverPenOnDay, 0, 
                originalCoverPenOnDay, 0, 
                roster.CoverPenOnDay.length);
            ResetHashTable(maxHashSize);
            AddHashKey(this.currentRosterHash);
            Shift[] shifts1 = new Shift[blockSize];
            roster.CacheEmployeePenalties(0, this.cacheViolationPens);
            for (int j = 0; j < blockSize; j++) {
              shifts1[j] = employee1.GetShift(day + j);
              if (shifts1[j] != null) {
                roster.UnAssignShift(shifts1[j]);
                this.currentRosterHash ^= this.hashRandomValues[i][day + j][(shifts1[j]).ShiftType.Index];
              } 
            } 
            roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
            for (int k = -1; k < roster.SchedulingPeriod.ShiftTypesCount; k++) {
              if (!this.SchedulingPeriodContainsNonAutoAssignShifts || k < 0 || 
                (roster.SchedulingPeriod.GetShiftType(k)).AutoAllocate) {
                Shift[] shifts2 = new Shift[blockSize];
                boolean different = false;
                for (int m = 0; m < blockSize; m++) {
                  if (k < 0) {
                    shifts2[m] = null;
                    if (shifts1[m] != null)
                      different = true; 
                  } else {
                    shifts2[m] = GetTestShift(k, day + m);
                    if (shifts1[m] == null || (shifts1[m]).ShiftType.Index != k)
                      different = true; 
                  } 
                } 
                if (different)
                  if ((shifts1[0] != null || shifts2[0] != null) && (
                    shifts1[blockSize - 1] != null || shifts2[blockSize - 1] != null)) {
                    boolean valid = true;
                    int n;
                    for (n = 0; n < blockSize; n++) {
                      if (employee1.ViolationsForAssigningShift(shifts2[n]) == -1) {
                        valid = false;
                        break;
                      } 
                    } 
                    if (valid) {
                      roster.CacheEmployeePenalties(1, this.cacheViolationPens);
                      if (k >= 0) {
                        for (n = 0; n < blockSize; n++) {
                          roster.AssignShift(employee1, shifts2[n]);
                          this.currentRosterHash ^= this.hashRandomValues[i][day + n][k];
                        } 
                        employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
                      } 
                      employee1.RecalculatePenalty(day, day + blockSize - 1, true);
                      this.TotalEvaluations++;
                      if (roster.getTotalPenalty() < originalPenalty) {
                        moveMade = true;
                        movesCount++;
                        if (shifts2[0] != null)
                          for (n = 0; n < blockSize; n++) {
                            roster.UnAssignShift(shifts2[n]);
                            if (shifts2[n] != null) {
                              Shift s = GetShift((shifts2[n]).ShiftType.Index, (shifts2[n]).RosterDay);
                              roster.AssignShift(employee1, s);
                            } 
                          }  
                        if (this.updateViolationFlags)
                          if (employee1.EmployeeDescription.Contract != null && 
                            employee1.EmployeeDescription.Contract.BadPatternsIsOn)
                            employee1.RecalculatePenalty();  
                      } else if (roster.CoverPenalty < originalCoverPenalty && 
                        this.MAX_DEPTH > 1 && (!this.TIME_LIMIT || DateTime.getNow().isLessThan(this.end))) {
                        if (shifts2[0] != null)
                          for (n = 0; n < blockSize; n++) {
                            roster.UnAssignShift(shifts2[n]);
                            if (shifts2[n] != null) {
                              shifts2[n] = GetShift((shifts2[n]).ShiftType.Index, (shifts2[n]).RosterDay);
                              roster.AssignShift(employee1, shifts2[n]);
                            } 
                          }  
                        if (this.updateViolationFlags)
                          if (employee1.EmployeeDescription.Contract != null && 
                            employee1.EmployeeDescription.Contract.BadPatternsIsOn)
                            employee1.RecalculatePenalty();  
                        int nextHash = this.currentRosterHash ^ this.hashEmployeeRandVals[employee1.Index];
                        if (!PreviouslyTested(nextHash))
                          moveMade = ImproveEmployee(employee1, originalPenalty, 2, originalCoverPenalty); 
                      } else if (roster.EmployeesPenalty < originalEmployeesPenalty && 
                        this.MAX_DEPTH > 1 && (!this.TIME_LIMIT || DateTime.getNow().isLessThan(this.end))) {
                        this.movesMade[0][0] = employee1.Index;
                        this.movesMade[0][2] = day;
                        this.movesMade[0][3] = blockSize;
                        this.movesMade[0][4] = 3;
                        this.movesMade[0][5] = k;
                        if (shifts2[0] != null)
                          for (n = 0; n < blockSize; n++) {
                            roster.UnAssignShift(shifts2[n]);
                            if (shifts2[n] != null) {
                              shifts2[n] = GetShift((shifts2[n]).ShiftType.Index, (shifts2[n]).RosterDay);
                              roster.AssignShift(employee1, shifts2[n]);
                            } 
                          }  
                        int nextHash = this.currentRosterHash;
                        boolean[] badDays = new boolean[roster.SchedulingPeriod.NumDaysInPeriod];
                        for (int d = 0; d < badDays.length; d++) {
                          if (roster.CoverPenOnDay[d] > originalCoverPenOnDay[d]) {
                            badDays[d] = true;
                            nextHash ^= this.hashDaysRandVals[d];
                          } 
                        } 
                        if (this.updateViolationFlags)
                          if (employee1.EmployeeDescription.Contract != null && 
                            employee1.EmployeeDescription.Contract.BadPatternsIsOn)
                            employee1.RecalculatePenalty();  
                        if (!PreviouslyTested(nextHash))
                          moveMade = ImproveCover(roster, badDays, originalPenalty, 2, originalCoverPenalty); 
                      } 
                      if (moveMade) {
                        this.lastImprovementEmployee1 = employee1.Index;
                        this.lastImprovementBlockSize = blockSize;
                        this.lastImprovementDay = day;
                        this.lastImprovementShiftType = k;
                        this.lastImprovementType = 3;
                        movesCount++;
                        break;
                      } 
                      if (k >= 0) {
                        for (n = 0; n < blockSize; n++) {
                          roster.UnAssignShift(shifts2[n]);
                          this.currentRosterHash ^= this.hashRandomValues[i][day + n][k];
                        } 
                        roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
                      } 
                      roster.RestoreEmployeePenalties(1, this.cacheViolationPens);
                    } 
                  }  
              } 
            } 
            if (!moveMade) {
              int n;
              for (n = 0; n < blockSize; n++) {
                if (shifts1[n] != null) {
                  roster.AssignShift(employee1, shifts1[n]);
                  this.currentRosterHash ^= this.hashRandomValues[i][day + n][(shifts1[n]).ShiftType.Index];
                } 
              } 
              roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
              roster.RestoreEmployeePenalties(0, this.cacheViolationPens);
              if (employee1.EmployeeDescription.Contract != null && 
                employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
                employee1.EmployeeDescription.BadPatternConstraint != null)
                employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1, day, day + blockSize - 1); 
              if (this.Stopped || this.finished || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end)) || 
                roster.getTotalPenalty() == 0)
                return movesCount; 
              shifts1 = new Shift[blockSize];
              roster.CacheEmployeePenalties(0, this.cacheViolationPens);
              for (n = 0; n < blockSize; n++) {
                shifts1[n] = employee1.GetShift(day + n);
                if (shifts1[n] != null) {
                  roster.UnAssignShift(shifts1[n]);
                  this.currentRosterHash ^= this.hashRandomValues[i][day + n][(shifts1[n]).ShiftType.Index];
                } 
              } 
              roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
              for (int m = i + 1; m < roster.Employees.length; m++) {
                Employee employee2 = roster.Employees[m];
                if (this.lastImprovementEmployee1 == employee1.Index && 
                  this.lastImprovementEmployee2 == employee2.Index && 
                  this.lastImprovementDay == day && 
                  this.lastImprovementBlockSize == blockSize && 
                  this.lastImprovementType == 4) {
                  this.finished = true;
                  break;
                } 
                if (employee1.Penalty == 0 && employee2.Penalty == 0 && 
                  !coverViolations)
                  continue; 
                if (this.EmployeeDescriptionHasFrozenDays[employee2.EmployeeDescription.IndexID]) {
                  boolean frozen = false;
                  for (int i2 = 0; i2 < blockSize; i2++) {
                    if (employee2.EmployeeDescription.FrozenDay[day + i2]) {
                      frozen = true;
                      break;
                    } 
                  } 
                  if (frozen)
                    continue; 
                } 
                Shift[] shifts2 = new Shift[blockSize];
                boolean employee2Violations = false;
                int i1;
                for (i1 = 0; i1 < blockSize; i1++) {
                  shifts2[i1] = employee2.GetShift(day + i1);
                  if (employee2.ConstraintViolationPenalties[day + i1] > 0)
                    employee2Violations = true; 
                } 
                if (this.VIOLATION_FLAG_HEURISTIC && 
                  !employee1Violations && !employee2Violations)
                  continue; 
                if (this.SchedulingPeriodContainsNonAutoAssignShifts) {
                  boolean fixedShifts = false;
                  for (int i2 = 0; i2 < blockSize; i2++) {
                    if (shifts2[i2] != null && shifts2[i2].isFixed()) {
                      fixedShifts = true;
                      break;
                    } 
                  } 
                  if (fixedShifts)
                    continue; 
                } 
                if ((shifts1[0] != null || shifts2[0] != null) && (
                  shifts1[blockSize - 1] != null || shifts2[blockSize - 1] != null)) {
                  for (i1 = 0; i1 < blockSize; i1++) {
                    if (shifts2[i1] != null) {
                      roster.UnAssignShift(shifts2[i1]);
                      this.currentRosterHash ^= this.hashRandomValues[m][day + i1][(shifts2[i1]).ShiftType.Index];
                    } 
                  } 
                  roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
                  boolean valid = true;
                  int i2;
                  for (i2 = 0; i2 < blockSize; i2++) {
                    if (employee2.ViolationsForAssigningShift(shifts1[i2]) == -1 || 
                      employee1.ViolationsForAssigningShift(shifts2[i2]) == -1) {
                      valid = false;
                      break;
                    } 
                  } 
                  if (valid) {
                    roster.CacheEmployeePenalties(1, this.cacheViolationPens);
                    for (i2 = 0; i2 < blockSize; i2++) {
                      if (shifts1[i2] != null) {
                        roster.AssignShift(employee2, shifts1[i2]);
                        this.currentRosterHash ^= this.hashRandomValues[m][day + i2][(shifts1[i2]).ShiftType.Index];
                      } 
                      if (shifts2[i2] != null) {
                        roster.AssignShift(employee1, shifts2[i2]);
                        this.currentRosterHash ^= this.hashRandomValues[i][day + i2][(shifts2[i2]).ShiftType.Index];
                      } 
                    } 
                    roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
                    roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
                    int tempPen = roster.getTotalPenalty();
                    employee1.RecalculatePenalty(day, day + blockSize - 1, true);
                    int e1Change = roster.getTotalPenalty() - tempPen;
                    employee2.RecalculatePenalty(day, day + blockSize - 1, true);
                    int e2Change = roster.getTotalPenalty() - tempPen + e1Change;
                    int covChange = roster.CoverPenalty - originalCoverPenalty;
                    this.TotalEvaluations++;
                    if (roster.getTotalPenalty() < originalPenalty) {
                      if (this.updateViolationFlags) {
                        if (employee1.EmployeeDescription.Contract != null && 
                          employee1.EmployeeDescription.Contract.BadPatternsIsOn)
                          employee1.RecalculatePenalty(); 
                        if (employee2.EmployeeDescription.Contract != null && 
                          employee2.EmployeeDescription.Contract.BadPatternsIsOn)
                          employee2.RecalculatePenalty(); 
                      } 
                      moveMade = true;
                    } else if (this.MAX_DEPTH > 1 && (!this.TIME_LIMIT || DateTime.getNow().isLessThan(this.end))) {
                      this.movesMade[0][0] = employee1.Index;
                      this.movesMade[0][1] = employee2.Index;
                      this.movesMade[0][2] = day;
                      this.movesMade[0][3] = blockSize;
                      this.movesMade[0][4] = 4;
                      if (this.updateViolationFlags) {
                        if (employee1.EmployeeDescription.Contract != null && 
                          employee1.EmployeeDescription.Contract.BadPatternsIsOn)
                          employee1.RecalculatePenalty(); 
                        if (employee2.EmployeeDescription.Contract != null && 
                          employee2.EmployeeDescription.Contract.BadPatternsIsOn)
                          employee2.RecalculatePenalty(); 
                      } 
                      if (e2Change + covChange < 0) {
                        int nextHash = this.currentRosterHash ^ this.hashEmployeeRandVals[employee1.Index];
                        if (!PreviouslyTested(nextHash))
                          moveMade = ImproveEmployee(employee1, originalPenalty, 2, originalCoverPenalty); 
                      } 
                      if (!moveMade && e1Change + covChange < 0 && (!this.TIME_LIMIT || DateTime.getNow().isLessThan(this.end))) {
                        int nextHash = this.currentRosterHash ^ this.hashEmployeeRandVals[employee2.Index];
                        if (!PreviouslyTested(nextHash))
                          moveMade = ImproveEmployee(employee2, originalPenalty, 2, originalCoverPenalty); 
                      } 
                      if (!moveMade && e1Change + e2Change < 0 && (!this.TIME_LIMIT || DateTime.getNow().isLessThan(this.end))) {
                        int hash = this.currentRosterHash;
                        boolean[] badDays = new boolean[roster.SchedulingPeriod.NumDaysInPeriod];
                        for (int d = 0; d < badDays.length; d++) {
                          if (roster.CoverPenOnDay[d] > originalCoverPenOnDay[d]) {
                            badDays[d] = true;
                            hash ^= this.hashDaysRandVals[d];
                          } 
                        } 
                        if (!PreviouslyTested(hash))
                          moveMade = ImproveCover(roster, badDays, originalPenalty, 2, originalCoverPenalty); 
                      } 
                    } 
                    if (moveMade) {
                      this.lastImprovementEmployee1 = employee1.Index;
                      this.lastImprovementEmployee2 = employee2.Index;
                      this.lastImprovementBlockSize = blockSize;
                      this.lastImprovementDay = day;
                      this.lastImprovementType = 4;
                      movesCount++;
                      break;
                    } 
                    for (int i3 = 0; i3 < blockSize; i3++) {
                      if (shifts1[i3] != null) {
                        roster.UnAssignShift(shifts1[i3]);
                        this.currentRosterHash ^= this.hashRandomValues[m][day + i3][(shifts1[i3]).ShiftType.Index];
                      } 
                      if (shifts2[i3] != null) {
                        roster.UnAssignShift(shifts2[i3]);
                        this.currentRosterHash ^= this.hashRandomValues[i][day + i3][(shifts2[i3]).ShiftType.Index];
                      } 
                    } 
                    roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
                    roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
                    roster.RestoreEmployeePenalties(1, this.cacheViolationPens);
                    if (employee2.EmployeeDescription.Contract != null && 
                      employee2.EmployeeDescription.Contract.BadPatternsIsOn && 
                      employee2.EmployeeDescription.BadPatternConstraint != null)
                      employee2.EmployeeDescription.BadPatternConstraint.Calculate(employee2, day, day + blockSize - 1); 
                  } 
                  for (i2 = 0; i2 < blockSize; i2++) {
                    if (shifts2[i2] != null) {
                      roster.AssignShift(employee2, shifts2[i2]);
                      this.currentRosterHash ^= this.hashRandomValues[m][day + i2][(shifts2[i2]).ShiftType.Index];
                    } 
                  } 
                  roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
                  if (employee2.EmployeeDescription.Contract != null && 
                    employee2.EmployeeDescription.Contract.BadPatternsIsOn && 
                    employee2.EmployeeDescription.BadPatternConstraint != null)
                    employee2.EmployeeDescription.BadPatternConstraint.Calculate(employee2, day, day + blockSize - 1); 
                } 
                continue;
              } 
              if (!moveMade) {
                for (int i1 = 0; i1 < blockSize; i1++) {
                  if (shifts1[i1] != null) {
                    roster.AssignShift(employee1, shifts1[i1]);
                    this.currentRosterHash ^= this.hashRandomValues[i][day + i1][(shifts1[i1]).ShiftType.Index];
                  } 
                } 
                roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
                roster.RestoreEmployeePenalties(0, this.cacheViolationPens);
                if (employee1.EmployeeDescription.Contract != null && 
                  employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
                  employee1.EmployeeDescription.BadPatternConstraint != null)
                  employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1, day, day + blockSize - 1); 
              } 
              if (this.Stopped || this.finished || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end)) || 
                roster.getTotalPenalty() == 0)
                return movesCount; 
            } 
            continue;
          } 
        } 
        if (this.VERBOSE)
          System.out.println("After SwapShiftsDepthOne (blockSize=" + blockSize + ") (" + movesCount + " moves), roster.Penalty = " + roster.getTotalPenalty()); 
      } 
    } while (movesCount != startMovesCount);
    return movesCount;
  }
  
  private boolean ImproveEmployee(Employee employee1, int originalPenalty, int currentDepth, int originalCoverPenalty) {
    AddHashKey(this.currentRosterHash ^ this.hashEmployeeRandVals[employee1.Index]);
    Roster roster = employee1.Roster;
    int maxBlockSize = this.NEXT_MOVE_MAX_BLOCK_SIZE;
    int penaltyCachePos = currentDepth;
    boolean moveFound = false;
    int bestPenalty = 0;
    int bestDay = 0;
    Employee nextEmployee = null;
    Employee bestEmployee2 = null;
    Shift[] bestShifts1 = (Shift[])null;
    Shift[] bestShifts2 = (Shift[])null;
    int bestSwapType = -1;
    int bestNextImprovement = -1;
    int currentTotalPenalty = roster.getTotalPenalty();
    int currentCoverPenalty = roster.CoverPenalty;
    int currentEmployeesPenalty = roster.EmployeesPenalty;
    int[] originalCoverPenOnDay = new int[roster.SchedulingPeriod.NumDaysInPeriod];
    System.arraycopy(roster.CoverPenOnDay, 0, 
        originalCoverPenOnDay, 0, 
        roster.CoverPenOnDay.length);
    for (int blockSize = 1; blockSize <= maxBlockSize; blockSize++) {
      for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod - blockSize + 1; day++) {
        if (this.EmployeeDescriptionHasFrozenDays[employee1.EmployeeDescription.IndexID]) {
          boolean frozen = false;
          for (int m = 0; m < blockSize; m++) {
            if (employee1.EmployeeDescription.FrozenDay[day + m]) {
              frozen = true;
              break;
            } 
          } 
          if (frozen)
            continue; 
        } 
        boolean coverViolations = false;
        for (int x = 0; x < blockSize; x++) {
          if (roster.CoverPenOnDay[day + x] > 0) {
            coverViolations = true;
            break;
          } 
        } 
        boolean employee1Violations = false;
        if (this.VIOLATION_FLAG_HEURISTIC)
          for (int m = 0; m < blockSize; m++) {
            if (employee1.ConstraintViolationPenalties[day + m] > 0) {
              employee1Violations = true;
              break;
            } 
          }  
        Shift[] shifts1 = new Shift[blockSize];
        if (this.SchedulingPeriodContainsNonAutoAssignShifts) {
          boolean fixedShift = false;
          for (int m = 0; m < blockSize; m++) {
            Shift s = employee1.GetShift(day + m);
            if (s != null && s.isFixed()) {
              fixedShift = true;
              break;
            } 
          } 
          if (fixedShift)
            continue; 
        } 
        if ((coverViolations || employee1.Penalty != 0) && (
          !this.VIOLATION_FLAG_HEURISTIC || coverViolations || employee1Violations)) {
          roster.CacheEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
          for (int i1 = 0; i1 < blockSize; i1++) {
            shifts1[i1] = employee1.GetShift(day + i1);
            if (shifts1[i1] != null) {
              roster.UnAssignShift(shifts1[i1]);
              this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + i1][(shifts1[i1]).ShiftType.Index];
            } 
          } 
          roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
          for (int n = -1; n < roster.SchedulingPeriod.ShiftTypesCount; n++) {
            Shift[] shifts2 = new Shift[blockSize];
            if (!this.SchedulingPeriodContainsNonAutoAssignShifts || n < 0 || 
              (roster.SchedulingPeriod.GetShiftType(n)).AutoAllocate) {
              boolean madePreviously = false;
              for (int d = 0; d < currentDepth - 1; d++) {
                if (this.movesMade[d][4] == 3 && 
                  this.movesMade[d][2] == day && 
                  this.movesMade[d][3] == blockSize && 
                  this.movesMade[d][0] == employee1.Index && 
                  this.movesMade[d][5] == n) {
                  madePreviously = true;
                  break;
                } 
              } 
              if (!madePreviously) {
                boolean different = false;
                for (int i2 = 0; i2 < blockSize; i2++) {
                  if (n < 0) {
                    shifts2[i2] = null;
                    if (shifts1[i2] != null)
                      different = true; 
                  } else {
                    shifts2[i2] = GetTestShift(n, day + i2);
                    if (shifts1[i2] == null || (shifts1[i2]).ShiftType.Index != n)
                      different = true; 
                  } 
                } 
                if (different)
                  if ((shifts1[0] != null || shifts2[0] != null) && (
                    shifts1[blockSize - 1] != null || shifts2[blockSize - 1] != null)) {
                    boolean valid = true;
                    int i3;
                    for (i3 = 0; i3 < blockSize; i3++) {
                      if (employee1.ViolationsForAssigningShift(shifts2[i3]) == -1) {
                        valid = false;
                        break;
                      } 
                    } 
                    if (valid) {
                      roster.CacheEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
                      if (n >= 0) {
                        for (i3 = 0; i3 < blockSize; i3++) {
                          roster.AssignShift(employee1, shifts2[i3]);
                          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + i3][n];
                        } 
                        employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
                      } 
                      if (moveFound) {
                        employee1.RecalculatePenalty(bestPenalty, day, day + blockSize - 1, false);
                      } else {
                        employee1.RecalculatePenalty(day, day + blockSize - 1, false);
                      } 
                      this.TotalEvaluations++;
                      boolean validSwap = false;
                      int nextImprovement = -1;
                      if (roster.getTotalPenalty() < originalPenalty) {
                        validSwap = true;
                        nextImprovement = 0;
                      } else {
                        int e1Change = roster.EmployeesPenalty - currentEmployeesPenalty;
                        int covChange = roster.CoverPenalty - currentCoverPenalty;
                        if (currentTotalPenalty + e1Change < originalPenalty) {
                          int hash = this.currentRosterHash;
                          for (int i4 = 0; i4 < roster.SchedulingPeriod.NumDaysInPeriod; i4++) {
                            if (roster.CoverPenOnDay[i4] > originalCoverPenOnDay[i4])
                              hash ^= this.hashDaysRandVals[i4]; 
                          } 
                          if (!PreviouslyTested(hash)) {
                            validSwap = true;
                            nextImprovement = 2;
                          } 
                        } else if (currentTotalPenalty + covChange < originalPenalty) {
                          int nextHash = this.currentRosterHash ^ this.hashEmployeeRandVals[employee1.Index];
                          if (!PreviouslyTested(nextHash)) {
                            validSwap = true;
                            nextImprovement = 1;
                          } 
                        } 
                      } 
                      if (validSwap && (!moveFound || roster.getTotalPenalty() < bestPenalty)) {
                        moveFound = true;
                        bestPenalty = roster.getTotalPenalty();
                        bestDay = day;
                        bestNextImprovement = nextImprovement;
                        bestSwapType = 3;
                        nextEmployee = employee1;
                        bestShifts1 = new Shift[blockSize];
                        bestShifts2 = new Shift[blockSize];
                        for (int i4 = 0; i4 < blockSize; i4++) {
                          bestShifts1[i4] = shifts1[i4];
                          bestShifts2[i4] = shifts2[i4];
                        } 
                      } 
                      if (n >= 0) {
                        for (int i4 = 0; i4 < blockSize; i4++) {
                          roster.UnAssignShift(shifts2[i4]);
                          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + i4][n];
                        } 
                        roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
                      } 
                      roster.RestoreEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
                    } 
                  }  
              } 
            } 
          } 
          for (int m = 0; m < blockSize; m++) {
            if (shifts1[m] != null) {
              roster.AssignShift(employee1, shifts1[m]);
              this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + m][(shifts1[m]).ShiftType.Index];
            } 
          } 
          roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
          roster.RestoreEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        } 
        shifts1 = new Shift[blockSize];
        roster.CacheEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        for (int k = 0; k < blockSize; k++) {
          shifts1[k] = employee1.GetShift(day + k);
          if (shifts1[k] != null) {
            roster.UnAssignShift(shifts1[k]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + k][(shifts1[k]).ShiftType.Index];
          } 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
        for (int j = 0; j < roster.Employees.length; j++) {
          Employee employee2 = roster.Employees[j];
          if (employee1 == employee2)
            continue; 
          if (employee1.Penalty == 0 && employee2.Penalty == 0 && !coverViolations)
            continue; 
          if (this.EmployeeDescriptionHasFrozenDays[employee2.EmployeeDescription.IndexID]) {
            boolean frozen = false;
            for (int n = 0; n < blockSize; n++) {
              if (employee2.EmployeeDescription.FrozenDay[day + n]) {
                frozen = true;
                break;
              } 
            } 
            if (frozen)
              continue; 
          } 
          boolean madePreviously = false;
          for (int d = 0; d < currentDepth - 1; d++) {
            if (this.movesMade[d][4] == 4 && 
              this.movesMade[d][2] == day && 
              this.movesMade[d][3] == blockSize && ((
              this.movesMade[d][0] == employee1.Index && this.movesMade[d][1] == employee2.Index) || (
              this.movesMade[d][0] == employee2.Index && this.movesMade[d][1] == employee1.Index))) {
              madePreviously = true;
              break;
            } 
          } 
          if (madePreviously)
            continue; 
          Shift[] shifts2 = new Shift[blockSize];
          boolean employee2Violations = false;
          int m;
          for (m = 0; m < blockSize; m++) {
            shifts2[m] = employee2.GetShift(day + m);
            if (employee2.ConstraintViolationPenalties[day + m] > 0)
              employee2Violations = true; 
          } 
          if (this.VIOLATION_FLAG_HEURISTIC && !coverViolations && 
            !employee1Violations && !employee2Violations)
            continue; 
          if (this.SchedulingPeriodContainsNonAutoAssignShifts) {
            boolean fixedShifts = false;
            for (int n = 0; n < blockSize; n++) {
              if (shifts2[n] != null && shifts2[n].isFixed()) {
                fixedShifts = true;
                break;
              } 
            } 
            if (fixedShifts)
              continue; 
          } 
          if ((shifts1[0] != null || shifts2[0] != null) && (
            shifts1[blockSize - 1] != null || shifts2[blockSize - 1] != null)) {
            for (m = 0; m < blockSize; m++) {
              if (shifts2[m] != null) {
                roster.UnAssignShift(shifts2[m]);
                this.currentRosterHash ^= this.hashRandomValues[j][day + m][(shifts2[m]).ShiftType.Index];
              } 
            } 
            roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
            boolean valid = true;
            int n;
            for (n = 0; n < blockSize; n++) {
              if (employee2.ViolationsForAssigningShift(shifts1[n]) == -1 || 
                employee1.ViolationsForAssigningShift(shifts2[n]) == -1) {
                valid = false;
                break;
              } 
            } 
            if (valid) {
              roster.CacheEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
              for (n = 0; n < blockSize; n++) {
                if (shifts1[n] != null) {
                  roster.AssignShift(employee2, shifts1[n]);
                  this.currentRosterHash ^= this.hashRandomValues[j][day + n][(shifts1[n]).ShiftType.Index];
                } 
                if (shifts2[n] != null) {
                  roster.AssignShift(employee1, shifts2[n]);
                  this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + n][(shifts2[n]).ShiftType.Index];
                } 
              } 
              roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
              roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
              int covChange = roster.CoverPenalty - currentCoverPenalty;
              int tempPen = roster.getTotalPenalty();
              if (moveFound) {
                employee1.RecalculatePenalty(bestPenalty + employee2.Penalty, day, day + blockSize - 1, false);
              } else {
                employee1.RecalculatePenalty(day, day + blockSize - 1, false);
              } 
              int e1Change = roster.getTotalPenalty() - tempPen;
              int tempPen2 = roster.getTotalPenalty();
              if (moveFound) {
                employee2.RecalculatePenalty(bestPenalty, day, day + blockSize - 1, false);
              } else {
                employee2.RecalculatePenalty(day, day + blockSize - 1, false);
              } 
              int e2Change = roster.getTotalPenalty() - tempPen2;
              this.TotalEvaluations++;
              boolean validMove = false;
              Employee possibleEmployee = null;
              int nextImprovement = -1;
              if (roster.getTotalPenalty() < originalPenalty) {
                validMove = true;
                nextImprovement = -1;
              } else {
                int penIgnoringE1 = currentTotalPenalty + e2Change + covChange;
                int penIgnoringE2 = currentTotalPenalty + e1Change + covChange;
                int penIgnoringCov = currentTotalPenalty + e1Change + e2Change;
                int lowest1 = 0;
                int lowest2 = 0;
                int lowest3 = 0;
                Employee possibleEmployee1 = null;
                int nextImprovement1 = 0;
                Employee possibleEmployee2 = null;
                int nextImprovement2 = 0;
                Employee possibleEmployee3 = null;
                int nextImprovement3 = 0;
                if (penIgnoringCov <= penIgnoringE1 && penIgnoringCov <= penIgnoringE2) {
                  lowest1 = penIgnoringCov;
                  nextImprovement1 = 2;
                  if (penIgnoringE1 <= penIgnoringE2) {
                    lowest2 = penIgnoringE1;
                    nextImprovement2 = 1;
                    possibleEmployee2 = employee1;
                    lowest3 = penIgnoringE2;
                    nextImprovement3 = 1;
                    possibleEmployee3 = employee2;
                  } else {
                    lowest2 = penIgnoringE2;
                    nextImprovement2 = 1;
                    possibleEmployee2 = employee2;
                    lowest3 = penIgnoringE1;
                    nextImprovement3 = 1;
                    possibleEmployee3 = employee1;
                  } 
                } else if (penIgnoringE1 <= penIgnoringCov && penIgnoringE1 <= penIgnoringE2) {
                  lowest1 = penIgnoringE1;
                  nextImprovement1 = 1;
                  possibleEmployee1 = employee1;
                  if (penIgnoringCov <= penIgnoringE2) {
                    lowest2 = penIgnoringCov;
                    nextImprovement2 = 2;
                    lowest3 = penIgnoringE2;
                    nextImprovement3 = 1;
                    possibleEmployee3 = employee2;
                  } else {
                    lowest2 = penIgnoringE2;
                    nextImprovement2 = 1;
                    possibleEmployee2 = employee2;
                    lowest3 = penIgnoringCov;
                    nextImprovement3 = 2;
                  } 
                } else if (penIgnoringE2 <= penIgnoringCov && penIgnoringE2 <= penIgnoringE1) {
                  lowest1 = penIgnoringE2;
                  nextImprovement1 = 1;
                  possibleEmployee1 = employee2;
                  if (penIgnoringCov <= penIgnoringE1) {
                    lowest2 = penIgnoringCov;
                    nextImprovement2 = 2;
                    lowest3 = penIgnoringE1;
                    nextImprovement3 = 1;
                    possibleEmployee3 = employee1;
                  } else {
                    lowest2 = penIgnoringE1;
                    nextImprovement2 = 1;
                    possibleEmployee2 = employee1;
                    lowest3 = penIgnoringCov;
                    nextImprovement3 = 2;
                  } 
                } 
                if (isValidMove(lowest1, nextImprovement1, possibleEmployee1, originalPenalty, roster, originalCoverPenOnDay)) {
                  validMove = true;
                  nextImprovement = nextImprovement1;
                  possibleEmployee = possibleEmployee1;
                } else if (isValidMove(lowest2, nextImprovement2, possibleEmployee2, originalPenalty, roster, originalCoverPenOnDay)) {
                  validMove = true;
                  nextImprovement = nextImprovement2;
                  possibleEmployee = possibleEmployee2;
                } else if (isValidMove(lowest3, nextImprovement3, possibleEmployee3, originalPenalty, roster, originalCoverPenOnDay)) {
                  validMove = true;
                  nextImprovement = nextImprovement3;
                  possibleEmployee = possibleEmployee3;
                } 
              } 
              if (validMove && (!moveFound || roster.getTotalPenalty() < bestPenalty)) {
                moveFound = true;
                bestPenalty = roster.getTotalPenalty();
                bestDay = day;
                nextEmployee = possibleEmployee;
                bestEmployee2 = employee2;
                bestSwapType = 4;
                bestNextImprovement = nextImprovement;
                bestShifts1 = new Shift[blockSize];
                bestShifts2 = new Shift[blockSize];
                for (int i2 = 0; i2 < blockSize; i2++) {
                  bestShifts1[i2] = shifts1[i2];
                  bestShifts2[i2] = shifts2[i2];
                } 
              } 
              for (int i1 = 0; i1 < blockSize; i1++) {
                if (shifts1[i1] != null) {
                  roster.UnAssignShift(shifts1[i1]);
                  this.currentRosterHash ^= this.hashRandomValues[j][day + i1][(shifts1[i1]).ShiftType.Index];
                } 
                if (shifts2[i1] != null) {
                  roster.UnAssignShift(shifts2[i1]);
                  this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + i1][(shifts2[i1]).ShiftType.Index];
                } 
              } 
              roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
              roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
              roster.RestoreEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
            } 
            for (n = 0; n < blockSize; n++) {
              if (shifts2[n] != null) {
                roster.AssignShift(employee2, shifts2[n]);
                this.currentRosterHash ^= this.hashRandomValues[j][day + n][(shifts2[n]).ShiftType.Index];
              } 
            } 
            roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
          } 
          continue;
        } 
        for (int i = 0; i < blockSize; i++) {
          if (shifts1[i] != null) {
            roster.AssignShift(employee1, shifts1[i]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + i][(shifts1[i]).ShiftType.Index];
          } 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
        roster.RestoreEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        if (this.Stopped)
          return false; 
        continue;
      } 
    } 
    boolean[] bestPattern = (boolean[])null;
    if (this.TEST_PATTERNS_DURING_IMPROVE_EMPLOYEE) {
      bestPattern = TestPatternsHeuristically(employee1, bestPenalty, moveFound, originalPenalty, penaltyCachePos);
      if (bestPattern != null) {
        moveFound = true;
        bestSwapType = 5;
        bestNextImprovement = 2;
      } 
    } 
    if (moveFound) {
      boolean moveMade = false;
      Shift[] shifts2 = (Shift[])null;
      int day = bestDay;
      Shift[] initialShifts = (Shift[])null;
      if (bestSwapType == 3) {
        int shiftIndex, i = bestShifts1.length;
        roster.CacheEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        int x;
        for (x = 0; x < i; x++) {
          if (bestShifts1[x] != null) {
            roster.UnAssignShift(bestShifts1[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + x][(bestShifts1[x]).ShiftType.Index];
          } 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts1);
        shifts2 = new Shift[i];
        if (bestShifts2[0] != null) {
          shiftIndex = (bestShifts2[0]).ShiftType.Index;
          for (x = 0; x < i; x++) {
            shifts2[x] = GetShift((bestShifts2[x]).ShiftType.Index, day + x);
            roster.AssignShift(employee1, shifts2[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + x][(shifts2[x]).ShiftType.Index];
          } 
          employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
          employee1.RecalculatePenalty(day, day + i - 1, true);
        } else {
          shiftIndex = -1;
          employee1.RecalculatePenalty(day, day + i - 1, true);
        } 
        if (this.updateViolationFlags)
          if (employee1.EmployeeDescription.Contract != null && 
            employee1.EmployeeDescription.Contract.BadPatternsIsOn)
            employee1.RecalculatePenalty();  
        this.movesMade[currentDepth - 1][4] = 3;
        this.movesMade[currentDepth - 1][0] = employee1.Index;
        this.movesMade[currentDepth - 1][2] = day;
        this.movesMade[currentDepth - 1][3] = i;
        this.movesMade[currentDepth - 1][5] = shiftIndex;
      } else if (bestSwapType == 4) {
        int i = bestShifts1.length;
        Employee employee2 = bestEmployee2;
        roster.CacheEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        int x;
        for (x = 0; x < i; x++) {
          if (bestShifts1[x] != null) {
            roster.UnAssignShift(bestShifts1[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + x][(bestShifts1[x]).ShiftType.Index];
          } 
          if (bestShifts2[x] != null) {
            roster.UnAssignShift(bestShifts2[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee2.Index][day + x][(bestShifts2[x]).ShiftType.Index];
          } 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts1);
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts2);
        for (x = 0; x < i; x++) {
          if (bestShifts1[x] != null) {
            roster.AssignShift(employee2, bestShifts1[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee2.Index][day + x][(bestShifts1[x]).ShiftType.Index];
          } 
          if (bestShifts2[x] != null) {
            roster.AssignShift(employee1, bestShifts2[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + x][(bestShifts2[x]).ShiftType.Index];
          } 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts1);
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts2);
        employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(bestShifts2);
        employee1.RecalculatePenalty(day, day + i - 1, true);
        employee2.Roster.CoverPenalty += employee2.Roster.UpdateCoverPens(bestShifts1);
        employee2.RecalculatePenalty(day, day + i - 1, true);
        if (this.updateViolationFlags) {
          if (employee1.EmployeeDescription.Contract != null && 
            employee1.EmployeeDescription.Contract.BadPatternsIsOn)
            employee1.RecalculatePenalty(); 
          if (employee2.EmployeeDescription.Contract != null && 
            employee2.EmployeeDescription.Contract.BadPatternsIsOn)
            employee2.RecalculatePenalty(); 
        } 
        this.movesMade[currentDepth - 1][4] = 4;
        this.movesMade[currentDepth - 1][0] = employee1.Index;
        this.movesMade[currentDepth - 1][1] = employee2.Index;
        this.movesMade[currentDepth - 1][2] = day;
        this.movesMade[currentDepth - 1][3] = i;
      } else if (bestSwapType == 5) {
        roster.CacheEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        initialShifts = new Shift[employee1.ShiftsCount];
        int pos = 0;
        int d;
        for (d = 0; d < roster.SchedulingPeriod.NumDaysInPeriod; d++) {
          for (int j = 0; j < roster.SchedulingPeriod.ShiftTypesCount; j++) {
            Shift shift = employee1.ShiftsOnDay[d][j];
            if (shift != null) {
              initialShifts[pos++] = shift;
              roster.UnAssignShift(shift);
              this.currentRosterHash ^= this.hashRandomValues[employee1.Index][d][j];
            } 
          } 
        } 
        for (d = 0; d < roster.SchedulingPeriod.NumDaysInPeriod; d++) {
          for (int j = 0; j < roster.SchedulingPeriod.ShiftTypesCount; j++) {
            int index = d * roster.SchedulingPeriod.ShiftTypesCount + j;
            if (bestPattern[index]) {
              Shift shift = GetShift(j, d);
              roster.AssignShift(employee1, shift);
              this.currentRosterHash ^= this.hashRandomValues[employee1.Index][d][j];
            } 
          } 
        } 
        employee1.RecalculatePenalty();
        roster.CoverPenalty = roster.RecalculateCoverPenalty();
        this.movesMade[currentDepth - 1][4] = 5;
        this.movesMade[currentDepth - 1][0] = employee1.Index;
      } else {
        return false;
      } 
      if (roster.getTotalPenalty() < originalPenalty) {
        moveMade = true;
      } else if (currentDepth < this.MAX_DEPTH && (
        !this.TIME_LIMIT || DateTime.getNow().isLessThan(this.end))) {
        if (bestNextImprovement == 2) {
          boolean[] badDays = new boolean[roster.SchedulingPeriod.NumDaysInPeriod];
          if (bestSwapType == 5) {
            for (int d = 0; d < roster.SchedulingPeriod.NumDaysInPeriod; d++)
              badDays[d] = true; 
          } else {
            for (int d = 0; d < badDays.length; d++) {
              if (roster.CoverPenOnDay[d] > originalCoverPenOnDay[d])
                badDays[d] = true; 
            } 
          } 
          moveMade = ImproveCover(roster, badDays, originalPenalty, currentDepth + 1, originalCoverPenalty);
        } else if (bestNextImprovement == 1) {
          moveMade = ImproveEmployee(nextEmployee, originalPenalty, currentDepth + 1, originalCoverPenalty);
        } 
      } 
      if (moveMade)
        return true; 
      if (bestSwapType == 3) {
        int i = bestShifts1.length;
        if (shifts2[0] != null) {
          for (int j = 0; j < i; j++) {
            roster.UnAssignShift(shifts2[j]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + j][(shifts2[j]).ShiftType.Index];
          } 
          roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
        } 
        for (int x = 0; x < i; x++) {
          if (bestShifts1[x] != null) {
            roster.AssignShift(employee1, bestShifts1[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + x][(bestShifts1[x]).ShiftType.Index];
          } 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts1);
        roster.RestoreEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        if (employee1.EmployeeDescription.Contract != null && 
          employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
          employee1.EmployeeDescription.BadPatternConstraint != null)
          employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1, day, day + i - 1); 
      } else if (bestSwapType == 4) {
        int i = bestShifts1.length;
        Employee employee2 = bestEmployee2;
        int x;
        for (x = 0; x < i; x++) {
          if (bestShifts1[x] != null) {
            roster.UnAssignShift(bestShifts1[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee2.Index][day + x][(bestShifts1[x]).ShiftType.Index];
          } 
          if (bestShifts2[x] != null) {
            roster.UnAssignShift(bestShifts2[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + x][(bestShifts2[x]).ShiftType.Index];
          } 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts1);
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts2);
        for (x = 0; x < i; x++) {
          if (bestShifts2[x] != null) {
            roster.AssignShift(employee2, bestShifts2[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee2.Index][day + x][(bestShifts2[x]).ShiftType.Index];
          } 
          if (bestShifts1[x] != null) {
            roster.AssignShift(employee1, bestShifts1[x]);
            this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + x][(bestShifts1[x]).ShiftType.Index];
          } 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts1);
        roster.CoverPenalty += roster.UpdateCoverPens(bestShifts2);
        roster.RestoreEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        if (employee1.EmployeeDescription.Contract != null && 
          employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
          employee1.EmployeeDescription.BadPatternConstraint != null)
          employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1, day, day + i - 1); 
        if (employee2.EmployeeDescription.Contract != null && 
          employee2.EmployeeDescription.Contract.BadPatternsIsOn && 
          employee2.EmployeeDescription.BadPatternConstraint != null)
          employee2.EmployeeDescription.BadPatternConstraint.Calculate(employee2, day, day + i - 1); 
      } else if (bestSwapType == 5) {
        for (int d = 0; d < roster.SchedulingPeriod.NumDaysInPeriod; d++) {
          for (int j = 0; j < roster.SchedulingPeriod.ShiftTypesCount; j++) {
            if (employee1.ShiftsOnDay[d][j] != null) {
              roster.UnAssignShift(employee1.ShiftsOnDay[d][j]);
              this.currentRosterHash ^= this.hashRandomValues[employee1.Index][d][j];
            } 
          } 
        } 
        for (int i = 0; i < initialShifts.length; i++) {
          Shift shift = initialShifts[i];
          roster.AssignShift(employee1, shift);
          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][shift.RosterDay][shift.ShiftType.Index];
        } 
        roster.CoverPenalty = roster.RecalculateCoverPenalty();
        roster.RestoreEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
        if (employee1.EmployeeDescription.Contract != null && 
          employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
          employee1.EmployeeDescription.BadPatternConstraint != null)
          employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1); 
      } 
      return false;
    } 
    return false;
  }
  
  private boolean ImproveCover(Roster roster, boolean[] badDays, int originalPenalty, int currentDepth, int originalCoverPenalty) {
    int maxBlockSize = this.NEXT_MOVE_MAX_BLOCK_SIZE;
    int penaltyCachePos = currentDepth;
    boolean moveFound = false;
    int bestPenalty = 0;
    int bestDay = 0;
    Employee nextEmployee = null;
    Shift[] bestShifts1 = (Shift[])null;
    Shift[] bestShifts2 = (Shift[])null;
    int currentTotalPenalty = roster.getTotalPenalty();
    int currentCoverPenalty = roster.CoverPenalty;
    int hash = this.currentRosterHash;
    for (int i = 0; i < badDays.length; i++) {
      if (badDays[i])
        hash ^= this.hashDaysRandVals[i]; 
    } 
    AddHashKey(hash);
    for (int blockSize = 1; blockSize <= maxBlockSize; blockSize++) {
      for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod - blockSize + 1; day++) {
        boolean validDay = false;
        for (int x = 0; x < blockSize; x++) {
          if (badDays[day + x]) {
            validDay = true;
            break;
          } 
        } 
        if (validDay)
          for (int j = 0; j < roster.Employees.length; j++) {
            Employee employee1 = roster.Employees[j];
            if (this.SchedulingPeriodContainsNonAutoAssignShifts) {
              boolean fixedShift = false;
              for (int i1 = 0; i1 < blockSize; i1++) {
                Shift s = employee1.GetShift(day + i1);
                if (s != null && s.isFixed()) {
                  fixedShift = true;
                  break;
                } 
              } 
              if (fixedShift)
                continue; 
            } 
            if (this.EmployeeDescriptionHasFrozenDays[employee1.EmployeeDescription.IndexID]) {
              boolean frozen = false;
              for (int i1 = 0; i1 < blockSize; i1++) {
                if (employee1.EmployeeDescription.FrozenDay[day + i1]) {
                  frozen = true;
                  break;
                } 
              } 
              if (frozen)
                continue; 
            } 
            Shift[] shifts1 = new Shift[blockSize];
            roster.CacheEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
            for (int n = 0; n < blockSize; n++) {
              shifts1[n] = employee1.GetShift(day + n);
              if (shifts1[n] != null) {
                roster.UnAssignShift(shifts1[n]);
                this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + n][(shifts1[n]).ShiftType.Index];
              } 
            } 
            roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
            for (int k = -1; k < roster.SchedulingPeriod.ShiftTypesCount; k++) {
              if (!this.SchedulingPeriodContainsNonAutoAssignShifts || k < 0 || 
                (roster.SchedulingPeriod.GetShiftType(k)).AutoAllocate) {
                Shift[] shifts2 = new Shift[blockSize];
                boolean madePreviously = false;
                for (int d = 0; d < currentDepth - 1; d++) {
                  if (this.movesMade[d][4] == 3 && 
                    this.movesMade[d][2] == day && 
                    this.movesMade[d][3] == blockSize && 
                    this.movesMade[d][0] == employee1.Index && 
                    this.movesMade[d][5] == k) {
                    madePreviously = true;
                    break;
                  } 
                } 
                if (!madePreviously) {
                  boolean different = false;
                  for (int i1 = 0; i1 < blockSize; i1++) {
                    if (k < 0) {
                      shifts2[i1] = null;
                      if (shifts1[i1] != null)
                        different = true; 
                    } else {
                      shifts2[i1] = GetTestShift(k, day + i1);
                      if (shifts1[i1] == null || (shifts1[i1]).ShiftType.Index != k)
                        different = true; 
                    } 
                  } 
                  if (different) {
                    boolean valid = true;
                    int i2;
                    for (i2 = 0; i2 < blockSize; i2++) {
                      if (employee1.ViolationsForAssigningShift(shifts2[i2]) == -1) {
                        valid = false;
                        break;
                      } 
                    } 
                    if (valid) {
                      roster.CacheEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
                      if (k >= 0) {
                        for (i2 = 0; i2 < blockSize; i2++) {
                          roster.AssignShift(employee1, shifts2[i2]);
                          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + i2][k];
                        } 
                        if (moveFound) {
                          employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
                          employee1.RecalculatePenalty(bestPenalty, day, day + blockSize - 1, false);
                        } else {
                          employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
                          employee1.RecalculatePenalty(day, day + blockSize - 1, false);
                        } 
                      } else if (moveFound) {
                        employee1.RecalculatePenalty(bestPenalty, day, day + blockSize - 1, false);
                      } else {
                        employee1.RecalculatePenalty(day, day + blockSize - 1, false);
                      } 
                      this.TotalEvaluations++;
                      boolean validSwap = false;
                      if (roster.getTotalPenalty() < originalPenalty) {
                        validSwap = true;
                      } else {
                        int covChange = roster.CoverPenalty - currentCoverPenalty;
                        if (currentTotalPenalty + covChange < originalPenalty) {
                          int nextHash = this.currentRosterHash ^ this.hashEmployeeRandVals[employee1.Index];
                          if (!PreviouslyTested(nextHash))
                            validSwap = true; 
                        } 
                      } 
                      if (validSwap && (!moveFound || roster.getTotalPenalty() < bestPenalty)) {
                        moveFound = true;
                        bestPenalty = roster.getTotalPenalty();
                        bestDay = day;
                        nextEmployee = employee1;
                        bestShifts1 = new Shift[blockSize];
                        bestShifts2 = new Shift[blockSize];
                        for (int i3 = 0; i3 < blockSize; i3++) {
                          bestShifts1[i3] = shifts1[i3];
                          bestShifts2[i3] = shifts2[i3];
                        } 
                      } 
                      if (k >= 0) {
                        for (int i3 = 0; i3 < blockSize; i3++) {
                          roster.UnAssignShift(shifts2[i3]);
                          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + i3][k];
                        } 
                        roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
                      } 
                      roster.RestoreEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
                    } 
                  } 
                } 
              } 
            } 
            for (int m = 0; m < blockSize; m++) {
              if (shifts1[m] != null) {
                roster.AssignShift(employee1, shifts1[m]);
                this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + m][(shifts1[m]).ShiftType.Index];
              } 
            } 
            roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
            roster.RestoreEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
            if (this.Stopped)
              return false; 
            continue;
          }  
      } 
    } 
    if (moveFound) {
      int shiftIndex;
      boolean moveMade = false;
      Shift[] shifts2 = (Shift[])null;
      int j = bestShifts1.length;
      Employee employee1 = nextEmployee;
      int day = bestDay;
      roster.CacheEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
      for (int x = 0; x < j; x++) {
        if (bestShifts1[x] != null) {
          roster.UnAssignShift(bestShifts1[x]);
          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + x][(bestShifts1[x]).ShiftType.Index];
        } 
      } 
      roster.CoverPenalty += roster.UpdateCoverPens(bestShifts1);
      shifts2 = new Shift[j];
      if (bestShifts2[0] != null) {
        shiftIndex = (bestShifts2[0]).ShiftType.Index;
        for (int m = 0; m < j; m++) {
          shifts2[m] = GetShift((bestShifts2[m]).ShiftType.Index, day + m);
          roster.AssignShift(employee1, shifts2[m]);
          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + m][(shifts2[m]).ShiftType.Index];
        } 
        employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
        employee1.RecalculatePenalty(day, day + j - 1, true);
      } else {
        shiftIndex = -1;
        employee1.RecalculatePenalty(day, day + j - 1, true);
      } 
      if (this.updateViolationFlags)
        if (employee1.EmployeeDescription.Contract != null && 
          employee1.EmployeeDescription.Contract.BadPatternsIsOn)
          employee1.RecalculatePenalty();  
      this.movesMade[currentDepth - 1][4] = 3;
      this.movesMade[currentDepth - 1][0] = employee1.Index;
      this.movesMade[currentDepth - 1][2] = day;
      this.movesMade[currentDepth - 1][3] = j;
      this.movesMade[currentDepth - 1][5] = shiftIndex;
      if (roster.getTotalPenalty() < originalPenalty) {
        moveMade = true;
        return true;
      } 
      if (currentDepth < this.MAX_DEPTH && (
        !this.TIME_LIMIT || DateTime.getNow().isLessThan(this.end)))
        moveMade = ImproveEmployee(nextEmployee, originalPenalty, currentDepth + 1, originalCoverPenalty); 
      if (moveMade)
        return true; 
      if (shifts2[0] != null) {
        for (int m = 0; m < j; m++) {
          roster.UnAssignShift(shifts2[m]);
          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + m][(shifts2[m]).ShiftType.Index];
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
      } 
      for (int k = 0; k < j; k++) {
        if (bestShifts1[k] != null) {
          roster.AssignShift(employee1, bestShifts1[k]);
          this.currentRosterHash ^= this.hashRandomValues[employee1.Index][day + k][(bestShifts1[k]).ShiftType.Index];
        } 
      } 
      roster.CoverPenalty += roster.UpdateCoverPens(bestShifts1);
      roster.RestoreEmployeePenalties(penaltyCachePos, this.cacheViolationPens);
      if (employee1.EmployeeDescription.Contract != null && 
        employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
        employee1.EmployeeDescription.BadPatternConstraint != null)
        employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1, day, day + j - 1); 
      return false;
    } 
    return false;
  }
  
  private void UpdateTitle() {
    this.Title = "VDS_B ";
    this.Title = String.valueOf(this.Title) + " MAX_DEPTH=" + this.MAX_DEPTH;
    this.Title = String.valueOf(this.Title) + ", MAX_BLOCK_SIZE_AT_DEPTH_ZERO=" + this.MAX_BLOCK_SIZE_AT_DEPTH_ZERO;
    this.Title = String.valueOf(this.Title) + ", NEXT_MOVE_MAX_BLOCK_SIZE=" + this.NEXT_MOVE_MAX_BLOCK_SIZE;
    if (this.TIME_LIMIT)
      this.Title = String.valueOf(this.Title) + ", Max time limit "; 
    if (this.TEST_PATTERNS_AT_START)
      this.Title = String.valueOf(this.Title) + ", TEST_PATTERNS_AT_START "; 
    if (this.POSITIVE_GAIN_HEURISTIC)
      this.Title = String.valueOf(this.Title) + ", POSITIVE_GAIN_HEURISTIC "; 
    if (this.VIOLATION_FLAG_HEURISTIC)
      this.Title = String.valueOf(this.Title) + ", VIOLATION_FLAG_HEURISTIC "; 
    if (this.TEST_PATTERNS_DURING_IMPROVE_EMPLOYEE)
      this.Title = String.valueOf(this.Title) + ", TEST_PATTERNS_DURING_IMPROVE_EMPLOYEE "; 
  }
  
  private void ShuffleEmployees(Roster roster) {
    for (int i = 0; i < roster.Employees.length * 3; i++) {
      int index1 = this.rand.nextInt(roster.Employees.length);
      int index2 = this.rand.nextInt(roster.Employees.length);
      Employee temp = roster.Employees[index1];
      roster.Employees[index1] = roster.Employees[index2];
      (roster.Employees[index2]).Index = index1;
      roster.Employees[index2] = temp;
      temp.Index = index2;
    } 
  }
  
  public Shift GetTestShift(int stIndex, int day) {
    return this.TestShifts[day][stIndex];
  }
  
  private int CalculateHash(Roster roster) {
    int hash = 0;
    for (int e = 0; e < roster.Employees.length; e++) {
      Employee employee = roster.Employees[e];
      for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod; day++) {
        for (int index = 0; index < roster.SchedulingPeriod.ShiftTypesCount; index++) {
          if (employee.ShiftsOnDay[day][index] != null)
            hash ^= this.hashRandomValues[e][day][index]; 
        } 
      } 
    } 
    return hash;
  }
  
  private void ResetHashTable(int maxSize) {
    this.HashKeyArray = new int[maxSize];
  }
  
  private void AddHashKey(int key) {
    int i = FindHashSlot(key);
    if (this.HashKeyArray[i] == 0)
      this.HashKeyArray[i] = key; 
  }
  
  private int FindHashSlot(int key) {
    int i = key % this.HashKeyArray.length;
    while (this.HashKeyArray[i] != 0 && this.HashKeyArray[i] != key)
      i = (i + 1) % this.HashKeyArray.length; 
    return i;
  }
  
  private boolean PreviouslyTested(int key) {
    int index = FindHashSlot(key);
    if (this.HashKeyArray[index] == 0)
      return false; 
    return true;
  }
  
  private boolean[] TestPatternsHeuristically(Employee employee, int bestPenalty, boolean moveFound, int originalPenalty, int penaltyCachePos) {
    Roster roster = employee.Roster;
    int currentTotalPenalty = roster.getTotalPenalty();
    int currentEmployeesPenalty = roster.EmployeesPenalty;
    boolean[] bestPattern = (boolean[])null;
    boolean[] requestsPattern = (boolean[])null;
    Shift[] initialShifts = new Shift[employee.ShiftsCount];
    int emptyPatternHash = this.currentRosterHash;
    int e = employee.Index;
    int pos = 0;
    for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod; day++) {
      for (int index = 0; index < roster.SchedulingPeriod.ShiftTypesCount; index++) {
        Shift shift = employee.ShiftsOnDay[day][index];
        if (shift != null) {
          if (shift.ShiftType.AutoAllocate && 
            !employee.EmployeeDescription.FrozenDay[day])
            roster.UnAssignShift(shift); 
          initialShifts[pos++] = shift;
          emptyPatternHash ^= this.hashRandomValues[e][day][index];
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
              Shift shift = GetShift(x, j);
              roster.AssignShift(employee, shift);
            } 
          } 
        } 
        roster.RestoreEmployeePenalties(penaltyCachePos + 1, this.cacheViolationPens);
        roster.CoverPenalty = roster.RecalculateCoverPenalty();
      } 
      ConstructPattern(employee, i, true, penaltyCachePos + 2);
      if (this.VERBOSE)
        System.out.println("CnstrctPttrn :: Emp: " + employee.EmployeeDescription.LastName + " Attempt: " + i + " EmpPen: " + employee.Penalty + " TotPen=" + roster.getTotalPenalty() + " CovPen=" + roster.CoverPenalty); 
      boolean valid = false;
      int e1Change = roster.EmployeesPenalty - currentEmployeesPenalty;
      if (roster.getTotalPenalty() < originalPenalty || 
        currentTotalPenalty + e1Change < originalPenalty)
        valid = true; 
      if (valid && (!moveFound || roster.getTotalPenalty() < bestPenalty)) {
        int tempHash = emptyPatternHash;
        int j;
        for (j = 0; j < roster.SchedulingPeriod.NumDaysInPeriod; j++) {
          for (int index = 0; index < roster.SchedulingPeriod.ShiftTypesCount; index++) {
            if (employee.ShiftsOnDay[j][index] != null)
              tempHash ^= this.hashRandomValues[e][j][index]; 
          } 
        } 
        for (j = 0; j < roster.SchedulingPeriod.NumDaysInPeriod; j++)
          tempHash ^= this.hashDaysRandVals[j]; 
        if (!PreviouslyTested(tempHash)) {
          moveFound = true;
          bestPenalty = roster.getTotalPenalty();
          bestPattern = new boolean[employee.ShiftAssignments.length];
          System.arraycopy(employee.ShiftAssignments, 0, 
              bestPattern, 0, 
              employee.ShiftAssignments.length);
        } 
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
              shifts[x] = GetTestShift(shiftIndex, day + x); 
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
              roster.CacheEmployeePenalties(cachePosition, this.cacheViolationPens);
              for (j = 0; j < shifts.length; j++)
                roster.AssignShift(employee, shifts[j]); 
              if (calculateCoverPenalty)
                employee.Roster.CoverPenalty += employee.Roster.UpdateCoverPens(shifts); 
              employee.RecalculatePenalty(bestPenalty + 1, day, day + blockSize - 1, false);
              this.TotalEvaluations++;
              if (roster.getTotalPenalty() < bestPenalty || (
                roster.getTotalPenalty() == bestPenalty && this.rand.nextDouble() > 0.5D)) {
                bestShifts = shifts;
                bestPenalty = roster.getTotalPenalty();
              } 
              for (j = 0; j < shifts.length; j++)
                roster.UnAssignShift(shifts[j]); 
              roster.RestoreEmployeePenalties(cachePosition, this.cacheViolationPens);
              if (calculateCoverPenalty)
                roster.CoverPenalty += roster.UpdateCoverPens(shifts); 
            } 
          } 
        } 
      } 
      if (bestShifts != null) {
        Shift[] shifts = new Shift[bestShifts.length];
        for (int x = 0; x < bestShifts.length; x++) {
          shifts[x] = GetShift((bestShifts[x]).ShiftType, (bestShifts[x]).RosterDay);
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
    this.TotalEvaluations += this.n1.TotalEvaluations;
    this.h1.TotalEvaluations = 0L;
    this.h1.cachePosition = cachePosition;
    this.h1.CALCULATE_COVER_PENALTY = calculateCoverPenalty;
    this.h1.SearchSingleEmployee(employee);
    this.TotalEvaluations += this.h1.TotalEvaluations;
  }
  
  public Shift GetShift(ShiftType st, int day) {
    if (st == null)
      return null; 
    return GetShift(st.Index, day);
  }
  
  public Shift GetShift(int stIndex, int day) {
    for (int i = 0; i < (this.AllShifts[day][stIndex]).length; i++) {
      if (!this.AllShifts[day][stIndex][i].isAssigned())
        return this.AllShifts[day][stIndex][i]; 
    } 
    return null;
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
                Shift shift = GetShift(st, day);
                if (employee.ViolationsForAssigningShift(shift) != -1) {
                  roster.AssignShift(employee, shift);
                  roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
                  this.TotalEvaluations++;
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
            Shift shift = GetTestShift((group.Group[j]).Index, day);
            if (shift.ShiftType.AutoAllocate)
              if (employee.ViolationsForAssigningShift(shift) != -1) {
                roster.AssignShift(employee, shift);
                roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
                this.TotalEvaluations++;
                if (bestShift == null || roster.CoverPenalty < bestCoverPenalty) {
                  bestShift = shift;
                  bestCoverPenalty = roster.CoverPenalty;
                } 
                roster.UnAssignShift(shift);
                roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
              }  
          } 
          if (bestShift != null) {
            Shift shift = GetShift(bestShift.ShiftType, bestShift.RosterDay);
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
              Shift shift = GetTestShift(i, day);
              if (shift.ShiftType.AutoAllocate)
                if (employee.ViolationsForAssigningShift(shift) != -1) {
                  roster.AssignShift(employee, shift);
                  roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
                  this.TotalEvaluations++;
                  if (bestShift == null || roster.CoverPenalty < bestCoverPenalty) {
                    bestShift = shift;
                    bestCoverPenalty = roster.CoverPenalty;
                  } 
                  roster.UnAssignShift(shift);
                  roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift });
                }  
            } 
            if (bestShift != null) {
              Shift shift = GetShift(bestShift.ShiftType, bestShift.RosterDay);
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
          Shift shift1 = GetTestShift(i, day);
          if (shift1.ShiftType.AutoAllocate)
            if (employee.ViolationsForAssigningShift(shift1) != -1) {
              roster.CacheEmployeePenalties(cachePosition, this.cacheViolationPens);
              roster.AssignShift(employee, shift1);
              if (calculateCoverPenalty)
                roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift1 }); 
              CalculateWeekendsPenalty(employee, -1);
              this.TotalEvaluations++;
              if (roster.EmployeesPenalty < bestPenalty) {
                bestShift1 = shift1;
                bestPenalty = roster.EmployeesPenalty;
              } 
              if (day + 1 < roster.SchedulingPeriod.NumDaysInPeriod && 
                !employee.EmployeeDescription.FrozenDay[day + 1]) {
                Shift shift2 = GetTestShift(i, day + 1);
                if (employee.ViolationsForAssigningShift(shift2) != -1 && 
                  shift2.ShiftType.AutoAllocate) {
                  roster.AssignShift(employee, shift2);
                  if (calculateCoverPenalty)
                    roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift2 }); 
                  CalculateWeekendsPenalty(employee, bestPenalty);
                  this.TotalEvaluations++;
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
          Shift shift1 = GetShift(bestShift1.ShiftType, bestShift1.RosterDay);
          roster.AssignShift(employee, shift1);
          if (calculateCoverPenalty)
            roster.CoverPenalty += roster.UpdateCoverPens(new Shift[] { shift1 }); 
          if (bestShift2 != null) {
            Shift shift2 = GetShift(bestShift2.ShiftType, bestShift2.RosterDay);
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
          this.TotalEvaluations++;
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
  
  public void DumpShifts(String msg, Shift[] shifts) {
    System.out.print(msg);
    for (int i = 0; i < shifts.length; i++) {
      Shift s = shifts[i];
      if (s == null) {
        System.out.print("_ (_) | ");
      } else {
        System.out.print(String.valueOf(s.ShiftType.ID) + " (" + s.RosterDay + ") | ");
      } 
    } 
    System.out.println(".");
  }
  
  private boolean isValidMove(int lowest, int nextImprovement, Employee possibleEmployee, int originalPenalty, Roster roster, int[] originalCoverPenOnDay) {
    if (lowest >= originalPenalty)
      return false; 
    boolean validMove = false;
    if (nextImprovement == 2) {
      int hash = this.currentRosterHash;
      for (int d = 0; d < roster.SchedulingPeriod.NumDaysInPeriod; d++) {
        if (roster.CoverPenOnDay[d] > originalCoverPenOnDay[d])
          hash ^= this.hashDaysRandVals[d]; 
      } 
      validMove = !PreviouslyTested(hash);
    } else if (nextImprovement == 1) {
      int nextHash = this.currentRosterHash ^ this.hashEmployeeRandVals[possibleEmployee.Index];
      validMove = !PreviouslyTested(nextHash);
    } 
    return validMove;
  }
  
  private void ConstructRoster(Roster roster) {
    GuiPrint("Stage : Testing Patterns", roster);
    int noImprov = 0;
    boolean finish = false;
    while (!finish) {
      for (int i = 0; i < roster.Employees.length; i++) {
        GuiPrint("Stage : Testing Patterns", roster);
        if (this.Stopped || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end)))
          return; 
        Employee employee = roster.Employees[i];
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
                Shift shift = GetShift(j, d);
                roster.AssignShift(employee, shift);
              } 
            } 
          } 
          employee.RecalculatePenalty();
          roster.CoverPenalty = roster.RecalculateCoverPenalty();
          noImprov = 0;
          if (roster.getTotalPenalty() == 0)
            return; 
        } else {
          noImprov++;
          if (noImprov >= roster.Employees.length) {
            finish = true;
            break;
          } 
        } 
      } 
    } 
  }
  
  private void QuickSearch(Roster roster) {
    int startPen;
    GuiPrint("Stage : Quick Search", roster);
    if (this.Stopped)
      return; 
    int MAX_BLOCK_SIZE = 5;
    boolean[] N_TRY_BLOCK_SIZE = new boolean[MAX_BLOCK_SIZE];
    boolean[] H_TRY_BLOCK_SIZE = new boolean[MAX_BLOCK_SIZE];
    boolean[] V_TRY_BLOCK_SIZE = new boolean[MAX_BLOCK_SIZE];
    for (int i = 0; i < MAX_BLOCK_SIZE; 

      
      H_TRY_BLOCK_SIZE[i] = true, N_TRY_BLOCK_SIZE[i] = true, i++)
      V_TRY_BLOCK_SIZE[i] = true; 
    int iteration = 0;
    do {
      startPen = roster.getTotalPenalty();
      iteration++;
      New1Emp1Swp_M n1 = new New1Emp1Swp_M(this);
      n1.TIME_LIMIT = false;
      n1.CALCULATE_COVER_PENALTY = true;
      n1.SKIP_ZERO_PENS = false;
      n1.MAX_BLOCK_SIZE = MAX_BLOCK_SIZE;
      n1.VIOLATION_HEURISTIC_THRESHOLD = 0;
      n1.RandomSeed = this.RandomSeed;
      n1.VERBOSE = false;
      n1.TRY_BLOCK_SIZE = N_TRY_BLOCK_SIZE;
      n1.Solve(roster);
      this.TotalEvaluations += n1.TotalEvaluations;
      for (int x = 0; x < n1.BlockSizeMovesCount.length; x++) {
        if (this.VERBOSE)
          System.out.println("N/1E/1S : BlockSize=" + (x + 1) + " Moves=" + n1.BlockSizeMovesCount[x]); 
        if (n1.BlockSizeMovesCount[x] == 0)
          N_TRY_BLOCK_SIZE[x] = false; 
      } 
      if (this.VERBOSE)
        System.out.println("Pen=" + roster.getTotalPenalty()); 
      GuiPrint("Stage : Quick Search", roster);
      if (this.Stopped || roster.getTotalPenalty() == 0 || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end)))
        return; 
      Hrz1Emp1Swp_M h1 = new Hrz1Emp1Swp_M(this);
      h1.TIME_LIMIT = false;
      h1.CALCULATE_COVER_PENALTY = true;
      h1.SKIP_ZERO_PEN = false;
      h1.MAX_BLOCK_SIZE = MAX_BLOCK_SIZE;
      h1.VIOLATION_HEURISTIC_THRESHOLD = 0;
      h1.RandomSeed = this.RandomSeed;
      h1.VERBOSE = false;
      h1.TRY_BLOCK_SIZE = H_TRY_BLOCK_SIZE;
      h1.Solve(roster);
      this.TotalEvaluations += h1.TotalEvaluations;
      for (int j = 0; j < h1.BlockSizeMovesCount.length; j++) {
        if (this.VERBOSE)
          System.out.println("H/1E/1S : BlockSize=" + (j + 1) + " Moves=" + h1.BlockSizeMovesCount[j]); 
        if (h1.BlockSizeMovesCount[j] == 0)
          H_TRY_BLOCK_SIZE[j] = false; 
      } 
      if (this.VERBOSE)
        System.out.println("Pen=" + roster.getTotalPenalty()); 
      GuiPrint("Stage : Quick Search", roster);
      if (this.Stopped || roster.getTotalPenalty() == 0 || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end)))
        return; 
      Vrt2Emp1Swp_M v1 = new Vrt2Emp1Swp_M(this);
      v1.TIME_LIMIT = false;
      v1.MAX_BLOCK_SIZE = MAX_BLOCK_SIZE;
      v1.VIOLATION_HEURISTIC_THRESHOLD = 0;
      v1.RandomSeed = this.RandomSeed;
      v1.VERBOSE = false;
      v1.TRY_BLOCK_SIZE = V_TRY_BLOCK_SIZE;
      v1.Solve(roster);
      this.TotalEvaluations += v1.TotalEvaluations;
      for (int k = 0; k < v1.BlockSizeMovesCount.length; k++) {
        if (this.VERBOSE)
          System.out.println("V/2E/1S : BlockSize=" + (k + 1) + " Moves=" + v1.BlockSizeMovesCount[k]); 
        if (v1.BlockSizeMovesCount[k] == 0)
          V_TRY_BLOCK_SIZE[k] = false; 
      } 
      if (this.VERBOSE)
        System.out.println("Pen=" + roster.getTotalPenalty()); 
      GuiPrint("Stage : Quick Search", roster);
      if (this.Stopped || roster.getTotalPenalty() == 0 || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.end)))
        return; 
    } while (roster.getTotalPenalty() != startPen);
  }
  
  private void CheckAlgorithmParameters(Roster roster) {
    if (this.NEXT_MOVE_MAX_BLOCK_SIZE < 1) {
      System.out.println("Warning : NEXT_MOVE_MAX_BLOCK_SIZE is a bit small! Changing to 1.");
      this.NEXT_MOVE_MAX_BLOCK_SIZE = 1;
    } 
    if (this.NEXT_MOVE_MAX_BLOCK_SIZE > roster.SchedulingPeriod.NumDaysInPeriod) {
      System.out.println("Warning : NEXT_MOVE_MAX_BLOCK_SIZE is a bit big! Changing to " + roster.SchedulingPeriod.NumDaysInPeriod + ".");
      this.NEXT_MOVE_MAX_BLOCK_SIZE = roster.SchedulingPeriod.NumDaysInPeriod;
    } 
    if (this.MAX_BLOCK_SIZE_AT_DEPTH_ZERO < 1) {
      System.out.println("Warning : MAX_BLOCK_SIZE_AT_DEPTH_ZERO is a bit small! Changing to 1.");
      this.MAX_BLOCK_SIZE_AT_DEPTH_ZERO = 1;
    } 
    if (this.MAX_BLOCK_SIZE_AT_DEPTH_ZERO > roster.SchedulingPeriod.NumDaysInPeriod) {
      System.out.println("Warning : MAX_BLOCK_SIZE_AT_DEPTH_ZERO is a bit big! Changing to " + roster.SchedulingPeriod.NumDaysInPeriod + ".");
      this.MAX_BLOCK_SIZE_AT_DEPTH_ZERO = roster.SchedulingPeriod.NumDaysInPeriod;
    } 
  }
  
  public boolean TestPen(Roster roster, String msg) {
    int p1 = roster.getTotalPenalty();
    int totPenalty = 0;
    for (int i = 0; i < roster.Employees.length; i++) {
      Employee employee = roster.Employees[i];
      totPenalty += SoftConstraints.CalculatePenalty(employee, 0, roster.SchedulingPeriod.NumDaysInPeriod - 1, false);
    } 
    totPenalty += roster.RecalculateCoverPenalty();
    if (p1 != totPenalty) {
      System.out.println("p1 != totPenalty " + p1 + " .equalsIgnoreCase(" + totPenalty + " " + msg);
      return false;
    } 
    return true;
  }
  
  public void DumpPattern(String msg, Employee employee) {
    System.out.print(String.valueOf(msg) + " : ");
    for (int day = 0; day < employee.Roster.SchedulingPeriod.NumDaysInPeriod; day++) {
      for (int x = 0; x < employee.Roster.SchedulingPeriod.ShiftTypesCount; x++) {
        if (employee.ShiftsOnDay[day][x] != null)
          System.out.print((employee.ShiftsOnDay[day][x]).ShiftType.Label); 
      } 
      System.out.print("|");
    } 
    System.out.println();
  }
  
  private void GuiPrint(String message, Roster roster) {
    int penalty = roster.getTotalPenalty();
    if (this.guiBestPenalty < 0 || penalty < this.guiBestPenalty)
      this.guiBestPenalty = penalty; 
    if (this.bestRoster != null && this.bestRoster.getTotalPenalty() < this.guiBestPenalty)
      this.guiBestPenalty = this.bestRoster.getTotalPenalty(); 
  }
  
  private void SetRoster(Roster roster1, Roster roster2) {
    roster1.Empty();
    for (int i = 0; i < roster2.Employees.length; i++) {
      Employee employee2 = roster2.Employees[i];
      Employee employee1 = roster1.GetEmployee(employee2.EmployeeDescription.ID);
      for (int day = 0; day < roster2.SchedulingPeriod.NumDaysInPeriod; day++) {
        for (int j = 0; j < roster2.SchedulingPeriod.ShiftTypesCount; j++) {
          Shift shift2 = employee2.ShiftsOnDay[day][j];
          if (shift2 != null) {
            Shift shift1 = GetShift(shift2.ShiftType.Index, day);
            if (shift1 != null)
              roster1.AssignShift(employee1, shift1); 
          } 
        } 
      } 
    } 
    roster1.RecalculateAllPenalties();
    if (roster1.getTotalPenalty() != roster2.getTotalPenalty());
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
    if (employee.Contract.MaxWeekendDaysIsOn) {
      int weight = schedulingPeriod.MasterWeights.MaxWeekendDays;
      if (employee.Contract.MaxWeekendDaysWeight > -1)
        weight = employee.Contract.MaxWeekendDaysWeight; 
      array.add(new MaxWeekendDays(weight));
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
  
  public boolean getSchedulingPeriodContainsNonAutoAssignShifts() {
    return this.SchedulingPeriodContainsNonAutoAssignShifts;
  }
  
  public boolean[] getEmployeeDescriptionHasFrozenDays() {
    return this.EmployeeDescriptionHasFrozenDays;
  }
}
