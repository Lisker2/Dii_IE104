package org.example.util.ASAP.NRP.Solvers.VariableDepthSearch;

import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.Shift;
import java.util.Random;

public class Hrz1Emp1Swp_M {
  public boolean Stopped = true;
  
  public String Author = "T";
  
  public String Title = "Horiz_1E_1SwapMax ";
  
  public long TotalEvaluations = 0L;
  
  public int RandomSeed = 1;
  
  Random rand;
  
  boolean finished;
  
  DateTime FinishTime;
  
  TestShiftDB shiftsDB;
  
  int iterationCount = 0;
  
  int[] employeeLastChanged;
  
  int[] dayLastChanged;
  
  int lastImprovementEmployee1;
  
  int lastImprovementDay2;
  
  int lastImprovementDay;
  
  int lastImprovementBlockSize;
  
  public int[] BlockSizeMovesCount;
  
  public int cachePosition = 0;
  
  public boolean VERBOSE = true;
  
  public boolean SKIP_ZERO_PEN = false;
  
  public boolean CALCULATE_COVER_PENALTY = true;
  
  public int MAX_BLOCK_SIZE = 2;
  
  public boolean TIME_LIMIT = false;
  
  public double MAX_RUN_TIME = 10.0D;
  
  public boolean[] TRY_BLOCK_SIZE;
  
  public int VIOLATION_HEURISTIC_THRESHOLD = 0;
  
  boolean cacheViolationPens = false;
  
  boolean randSet;
  
  public Hrz1Emp1Swp_M(TestShiftDB shiftsDB) {
    this.randSet = false;
    this.shiftsDB = shiftsDB;
    this.TRY_BLOCK_SIZE = new boolean[this.MAX_BLOCK_SIZE];
    for (int i = 0; i < this.TRY_BLOCK_SIZE.length; i++)
      this.TRY_BLOCK_SIZE[i] = true; 
  }
  
  public void setRNG(Random rand) {
    this.rand = rand;
    this.randSet = true;
  }
  
  public void Solve(Roster roster) {
    this.FinishTime = DateTime.getNow().AddSeconds(this.MAX_RUN_TIME);
    if (!this.randSet)
      this.rand = new Random(this.RandomSeed); 
    ShuffleEmployees(roster);
    Search(roster);
    if (this.VERBOSE)
      System.out.println("Search finished. Total moves tested=" + this.TotalEvaluations); 
  }
  
  private void Search(Roster roster) {
    if (this.MAX_BLOCK_SIZE > roster.SchedulingPeriod.NumDaysInPeriod)
      this.MAX_BLOCK_SIZE = roster.SchedulingPeriod.NumDaysInPeriod; 
    if (this.VIOLATION_HEURISTIC_THRESHOLD > 0) {
      this.cacheViolationPens = true;
    } else {
      this.cacheViolationPens = false;
    } 
    int totalImprovingMoves = 0;
    int noImprovement = 0;
    boolean repeat = true;
    this.Stopped = false;
    this.finished = false;
    this.iterationCount = 0;
    this.employeeLastChanged = new int[roster.Employees.length];
    this.dayLastChanged = new int[roster.SchedulingPeriod.NumDaysInPeriod];
    this.lastImprovementEmployee1 = -1;
    this.lastImprovementDay2 = -1;
    this.lastImprovementDay = -1;
    this.lastImprovementBlockSize = -1;
    this.BlockSizeMovesCount = new int[this.MAX_BLOCK_SIZE];
    boolean DO_REPEAT = true;
    if (this.MAX_BLOCK_SIZE > this.TRY_BLOCK_SIZE.length) {
      boolean[] tempArr = new boolean[this.MAX_BLOCK_SIZE];
      int j;
      for (j = 0; j < this.TRY_BLOCK_SIZE.length; j++)
        tempArr[j] = this.TRY_BLOCK_SIZE[j]; 
      for (j = this.TRY_BLOCK_SIZE.length; j < this.MAX_BLOCK_SIZE; j++)
        tempArr[j] = true; 
      this.TRY_BLOCK_SIZE = tempArr;
    } 
    int blockSizesToTry = 0;
    for (int i = 0; i < this.TRY_BLOCK_SIZE.length; i++) {
      if (this.TRY_BLOCK_SIZE[i])
        blockSizesToTry++; 
    } 
    while (blockSizesToTry > 0 && repeat) {
      int movesCount = 0;
      if (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.FinishTime))
        break; 
      for (int j = 1; j <= this.MAX_BLOCK_SIZE; j++) {
        this.iterationCount++;
        if (this.TRY_BLOCK_SIZE[j - 1]) {
          for (int k = 0; k < roster.Employees.length; k++)
            movesCount += SwapShiftsDepthOne(roster, roster.Employees[k], j); 
          totalImprovingMoves += movesCount;
          if (this.VERBOSE)
            System.out.println("After SwapShiftsDepthOne (blockSize=" + j + ") (" + movesCount + " moves), roster.Penalty = " + roster.getTotalPenalty()); 
          if (movesCount == 0) {
            noImprovement++;
          } else {
            noImprovement = 0;
          } 
          if (this.finished || noImprovement == blockSizesToTry) {
            repeat = false;
            break;
          } 
        } 
      } 
      if (this.VERBOSE)
        System.out.println("Total moves tested=" + this.TotalEvaluations + ", totalImprovingMoves=" + totalImprovingMoves); 
      if (!DO_REPEAT)
        break; 
    } 
    if (this.VERBOSE)
      System.out.println("Swaps finished. Total moves tested=" + this.TotalEvaluations + ", totalImprovingMoves=" + totalImprovingMoves); 
    if (!this.CALCULATE_COVER_PENALTY)
      roster.RecalculateAllPenalties(); 
  }
  
  public void SearchSingleEmployee(Employee employee) {
    Roster roster = employee.Roster;
    if (this.TIME_LIMIT)
      this.FinishTime = DateTime.getNow().AddSeconds(this.MAX_RUN_TIME); 
    if (this.MAX_BLOCK_SIZE > roster.SchedulingPeriod.NumDaysInPeriod)
      this.MAX_BLOCK_SIZE = roster.SchedulingPeriod.NumDaysInPeriod; 
    if (this.VIOLATION_HEURISTIC_THRESHOLD > 0) {
      this.cacheViolationPens = true;
    } else {
      this.cacheViolationPens = false;
    } 
    int totalImprovingMoves = 0;
    int noImprovement = 0;
    boolean repeat = true;
    this.Stopped = false;
    this.finished = false;
    this.iterationCount = 0;
    this.employeeLastChanged = new int[roster.Employees.length];
    this.dayLastChanged = new int[roster.SchedulingPeriod.NumDaysInPeriod];
    this.lastImprovementEmployee1 = -1;
    this.lastImprovementDay2 = -1;
    this.lastImprovementDay = -1;
    this.lastImprovementBlockSize = -1;
    this.BlockSizeMovesCount = new int[this.MAX_BLOCK_SIZE];
    boolean DO_REPEAT = true;
    if (this.MAX_BLOCK_SIZE > this.TRY_BLOCK_SIZE.length) {
      boolean[] tempArr = new boolean[this.MAX_BLOCK_SIZE];
      int j;
      for (j = 0; j < this.TRY_BLOCK_SIZE.length; j++)
        tempArr[j] = this.TRY_BLOCK_SIZE[j]; 
      for (j = this.TRY_BLOCK_SIZE.length; j < this.MAX_BLOCK_SIZE; j++)
        tempArr[j] = true; 
      this.TRY_BLOCK_SIZE = tempArr;
    } 
    int blockSizesToTry = 0;
    for (int i = 0; i < this.TRY_BLOCK_SIZE.length; i++) {
      if (this.TRY_BLOCK_SIZE[i])
        blockSizesToTry++; 
    } 
    while (blockSizesToTry > 0 && repeat) {
      int movesCount = 0;
      if (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.FinishTime))
        break; 
      for (int j = 1; j <= this.MAX_BLOCK_SIZE; j++) {
        this.iterationCount++;
        if (this.TRY_BLOCK_SIZE[j - 1]) {
          movesCount = SwapShiftsDepthOne(roster, employee, j);
          totalImprovingMoves += movesCount;
          if (this.VERBOSE)
            System.out.println("After SwapShiftsDepthOne (blockSize=" + j + ") (" + movesCount + " moves), roster.Penalty = " + roster.getTotalPenalty()); 
          if (movesCount == 0) {
            noImprovement++;
          } else {
            noImprovement = 0;
          } 
          if (this.finished || noImprovement == blockSizesToTry) {
            repeat = false;
            break;
          } 
        } 
      } 
      if (this.VERBOSE)
        System.out.println("Total moves tested=" + this.TotalEvaluations + ", totalImprovingMoves=" + totalImprovingMoves); 
      if (!DO_REPEAT)
        break; 
    } 
    if (this.VERBOSE)
      System.out.println("Swaps finished. Total moves tested=" + this.TotalEvaluations + ", totalImprovingMoves=" + totalImprovingMoves); 
  }
  
  private int SwapShiftsDepthOne(Roster roster, Employee employee1, int blockSize) {
    int movesCount = 0;
    if (this.SKIP_ZERO_PEN && employee1.Penalty == 0)
      return 0; 
    for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod - blockSize + 1; day++) {
      if (this.VERBOSE)
        System.out.println("H/1E/1S: Swap..D1: BlockSize=" + blockSize + 
            " E1=" + employee1.EmployeeDescription.getName() + 
            " Day=" + day + 
            " Evals=" + this.TotalEvaluations + 
            " Pen=" + roster.getTotalPenalty()); 
      if (this.shiftsDB.getEmployeeDescriptionHasFrozenDays()[employee1.EmployeeDescription.IndexID]) {
        boolean frozen = false;
        for (int i = 0; i < blockSize; i++) {
          if (employee1.EmployeeDescription.FrozenDay[day + i]) {
            frozen = true;
            break;
          } 
        } 
        if (frozen)
          continue; 
      } 
      boolean moveMade = false;
      boolean employee1Violations = false;
      if (this.VIOLATION_HEURISTIC_THRESHOLD > 0)
        for (int i = 0; i < blockSize; i++) {
          if (employee1.ConstraintViolationPenalties[day + i] >= this.VIOLATION_HEURISTIC_THRESHOLD)
            employee1Violations = true; 
        }  
      int originalPenalty = roster.getTotalPenalty();
      Shift[] shifts1 = new Shift[blockSize];
      roster.CacheEmployeePenalties(this.cachePosition, this.cacheViolationPens);
      boolean fixedShift = false;
      boolean dayChanged = false;
      int x;
      for (x = 0; x < blockSize; x++) {
        shifts1[x] = employee1.GetShift(day + x);
        if (shifts1[x] != null && shifts1[x].isFixed()) {
          fixedShift = true;
          break;
        } 
        if (this.iterationCount - this.dayLastChanged[day + x] <= this.MAX_BLOCK_SIZE)
          dayChanged = true; 
      } 
      if (!fixedShift) {
        for (x = 0; x < blockSize; x++)
          roster.UnAssignShift(shifts1[x]); 
        if (this.CALCULATE_COVER_PENALTY)
          roster.CoverPenalty += roster.UpdateCoverPens(shifts1); 
        for (int day2 = day + blockSize; day2 < roster.SchedulingPeriod.NumDaysInPeriod - blockSize + 1; day2++) {
          if (this.shiftsDB.getEmployeeDescriptionHasFrozenDays()[employee1.EmployeeDescription.IndexID]) {
            boolean frozen = false;
            for (int j = 0; j < blockSize; j++) {
              if (employee1.EmployeeDescription.FrozenDay[day2 + j]) {
                frozen = true;
                break;
              } 
            } 
            if (frozen)
              continue; 
          } 
          if (this.VIOLATION_HEURISTIC_THRESHOLD > 0 && !employee1Violations) {
            boolean employee1Violations2 = false;
            for (int j = 0; j < blockSize; j++) {
              if (employee1.ConstraintViolationPenalties[day2 + j] >= this.VIOLATION_HEURISTIC_THRESHOLD)
                employee1Violations2 = true; 
            } 
            if (!employee1Violations2)
              continue; 
          } 
          Shift[] shifts2 = new Shift[blockSize];
          fixedShift = false;
          boolean day2Changed = false;
          for (int i = 0; i < blockSize; i++) {
            shifts2[i] = employee1.GetShift(day2 + i);
            if (shifts2[i] != null && shifts2[i].isFixed()) {
              fixedShift = true;
              break;
            } 
            if (this.iterationCount - this.dayLastChanged[day2 + i] <= this.MAX_BLOCK_SIZE)
              day2Changed = true; 
          } 
          if (!fixedShift)
            if (dayChanged || day2Changed || 
              this.iterationCount - this.employeeLastChanged[employee1.Index] <= this.MAX_BLOCK_SIZE)
              if ((shifts1[0] != null || shifts2[0] != null) && (
                shifts1[blockSize - 1] != null || shifts2[blockSize - 1] != null)) {
                boolean different = false;
                int j;
                for (j = 0; j < blockSize; j++) {
                  if (shifts1[j] != null && shifts2[j] != null) {
                    if ((shifts1[j]).ShiftType != (shifts2[j]).ShiftType) {
                      different = true;
                      break;
                    } 
                  } else if (shifts1[j] != null || shifts2[j] != null) {
                    different = true;
                    break;
                  } 
                } 
                if (different) {
                  for (j = 0; j < blockSize; j++)
                    roster.UnAssignShift(shifts2[j]); 
                  if (this.CALCULATE_COVER_PENALTY)
                    roster.CoverPenalty += roster.UpdateCoverPens(shifts2); 
                  boolean valid = true;
                  Shift[] shifts1Real = new Shift[blockSize];
                  Shift[] shifts2Real = new Shift[blockSize];
                  int k;
                  for (k = 0; k < blockSize; k++) {
                    Shift tShift1 = null;
                    if (shifts2[k] != null)
                      tShift1 = this.shiftsDB.GetTestShift((shifts2[k]).ShiftType.Index, day + k); 
                    shifts2Real[k] = tShift1;
                    if (tShift1 != null && employee1.ViolationsForAssigningShift(tShift1) == -1) {
                      valid = false;
                      break;
                    } 
                    Shift tShift2 = null;
                    if (shifts1[k] != null)
                      tShift2 = this.shiftsDB.GetTestShift((shifts1[k]).ShiftType.Index, day2 + k); 
                    shifts1Real[k] = tShift2;
                    if (tShift2 != null && employee1.ViolationsForAssigningShift(tShift2) == -1) {
                      valid = false;
                      break;
                    } 
                  } 
                  if (valid) {
                    roster.CacheEmployeePenalties(this.cachePosition + 1, this.cacheViolationPens);
                    for (k = 0; k < blockSize; k++) {
                      roster.AssignShift(employee1, shifts1Real[k]);
                      roster.AssignShift(employee1, shifts2Real[k]);
                    } 
                    Shift[] allShifts = new Shift[blockSize * 2];
                    int m;
                    for (m = 0; m < blockSize; m++) {
                      allShifts[m] = shifts1Real[m];
                      allShifts[m + blockSize] = shifts2Real[m];
                    } 
                    if (this.CALCULATE_COVER_PENALTY) {
                      employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(allShifts);
                      employee1.RecalculatePenalty(originalPenalty);
                    } else {
                      employee1.RecalculatePenalty();
                    } 
                    this.TotalEvaluations++;
                    if (roster.getTotalPenalty() < originalPenalty) {
                      moveMade = true;
                    } else {
                      moveMade = false;
                    } 
                    if (moveMade) {
                      this.BlockSizeMovesCount[blockSize - 1] = this.BlockSizeMovesCount[blockSize - 1] + 1;
                      for (m = 0; m < blockSize; m++) {
                        roster.UnAssignShift(shifts1Real[m]);
                        roster.UnAssignShift(shifts2Real[m]);
                        if (shifts1Real[m] != null)
                          roster.AssignShift(employee1, (Shift)shifts1Real[m].Clone()); 
                        if (shifts2Real[m] != null)
                          roster.AssignShift(employee1, (Shift)shifts2Real[m].Clone()); 
                        if (shifts1Real[m] != null && shifts2[m] != null) {
                          if ((shifts1Real[m]).ShiftType != (shifts2[m]).ShiftType)
                            this.dayLastChanged[day2 + m] = this.iterationCount; 
                        } else if (shifts1Real[m] != null || shifts2[m] != null) {
                          this.dayLastChanged[day2 + m] = this.iterationCount;
                        } 
                        if (shifts1[m] != null && shifts2Real[m] != null) {
                          if ((shifts1[m]).ShiftType != (shifts2Real[m]).ShiftType)
                            this.dayLastChanged[day + m] = this.iterationCount; 
                        } else if (shifts1[m] != null || shifts2Real[m] != null) {
                          this.dayLastChanged[day + m] = this.iterationCount;
                        } 
                      } 
                      this.employeeLastChanged[employee1.Index] = this.iterationCount;
                      this.lastImprovementEmployee1 = employee1.Index;
                      this.lastImprovementBlockSize = blockSize;
                      this.lastImprovementDay = day;
                      this.lastImprovementDay2 = day2;
                      break;
                    } 
                    for (m = 0; m < blockSize; m++) {
                      roster.UnAssignShift(shifts1Real[m]);
                      roster.UnAssignShift(shifts2Real[m]);
                    } 
                    roster.RestoreEmployeePenalties(this.cachePosition + 1, this.cacheViolationPens);
                    if (this.CALCULATE_COVER_PENALTY)
                      roster.CoverPenalty += roster.UpdateCoverPens(allShifts); 
                  } 
                  for (k = 0; k < blockSize; k++)
                    roster.AssignShift(employee1, shifts2[k]); 
                  if (this.CALCULATE_COVER_PENALTY)
                    roster.CoverPenalty += roster.UpdateCoverPens(shifts2); 
                  if (this.lastImprovementDay == day && 
                    this.lastImprovementDay2 == day2 && 
                    this.lastImprovementEmployee1 == employee1.Index && 
                    this.lastImprovementBlockSize == blockSize) {
                    this.finished = true;
                    break;
                  } 
                } 
              }   
          continue;
        } 
        if (moveMade) {
          movesCount++;
        } else {
          for (int i = 0; i < blockSize; i++)
            roster.AssignShift(employee1, shifts1[i]); 
          roster.RestoreEmployeePenalties(this.cachePosition, this.cacheViolationPens);
          if (this.CALCULATE_COVER_PENALTY)
            roster.CoverPenalty += roster.UpdateCoverPens(shifts1); 
        } 
        if (this.Stopped || this.finished || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.FinishTime)))
          return movesCount; 
      } 
      continue;
    } 
    return movesCount;
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
}
