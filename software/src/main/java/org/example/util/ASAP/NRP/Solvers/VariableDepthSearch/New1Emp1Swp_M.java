package org.example.util.ASAP.NRP.Solvers.VariableDepthSearch;

import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.Shift;
import java.util.Random;

public class New1Emp1Swp_M {
  public boolean Stopped = true;
  
  public String Author = "T";
  
  public String Title = "NewBlock_1E_1SwapMax ";
  
  public long TotalEvaluations = 0L;
  
  public int RandomSeed = 1;
  
  Random rand;
  
  boolean finished;
  
  TestShiftDB shiftsDB;
  
  DateTime FinishTime;
  
  int iterationCount = 0;
  
  int[] employeeLastChanged;
  
  int[] dayLastChanged;
  
  int lastImprovementEmployee1;
  
  int lastImprovementBlockSize;
  
  int lastImprovementDay;
  
  int lastImprovementShiftType;
  
  public int[] BlockSizeMovesCount;
  
  public int cachePosition = 0;
  
  public boolean VERBOSE = true;
  
  public boolean SKIP_ZERO_PENS = false;
  
  public boolean CALCULATE_COVER_PENALTY = true;
  
  public int MAX_BLOCK_SIZE = 2;
  
  public boolean TIME_LIMIT = false;
  
  public double MAX_RUN_TIME = 10.0D;
  
  public boolean[] TRY_BLOCK_SIZE;
  
  public int VIOLATION_HEURISTIC_THRESHOLD = 0;
  
  boolean cacheViolationPens = false;
  
  boolean randSet;
  
  public New1Emp1Swp_M(TestShiftDB shiftsDB) {
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
    this.lastImprovementBlockSize = -1;
    this.lastImprovementDay = -1;
    this.lastImprovementShiftType = -2;
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
    this.lastImprovementBlockSize = -1;
    this.lastImprovementDay = -1;
    this.lastImprovementShiftType = -2;
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
    if (this.SKIP_ZERO_PENS && employee1.Penalty == 0)
      return 0; 
    for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod - blockSize + 1; day++) {
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
      if (this.VERBOSE)
        System.out.println("N/1E/1S: Swap..D1: BlockSize=" + blockSize + 
            " E1=" + employee1.EmployeeDescription.getName() + 
            " Day=" + day + 
            " Evals=" + this.TotalEvaluations + 
            " Pen=" + roster.getTotalPenalty()); 
      boolean moveMade = false;
      int originalPenalty = roster.getTotalPenalty();
      Shift[] shifts1 = new Shift[blockSize];
      roster.CacheEmployeePenalties(this.cachePosition, this.cacheViolationPens);
      if (this.VIOLATION_HEURISTIC_THRESHOLD > 0) {
        boolean employee1Violations = false;
        for (int i = 0; i < blockSize; i++) {
          if (employee1.ConstraintViolationPenalties[day + i] >= this.VIOLATION_HEURISTIC_THRESHOLD)
            employee1Violations = true; 
        } 
        if (!employee1Violations)
          continue; 
      } 
      boolean dayChanged = false;
      boolean fixedShift = false;
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
      if (!fixedShift)
        if (dayChanged || this.iterationCount - this.employeeLastChanged[employee1.Index] <= this.MAX_BLOCK_SIZE) {
          for (x = 0; x < blockSize; x++)
            roster.UnAssignShift(shifts1[x]); 
          if (this.CALCULATE_COVER_PENALTY)
            roster.CoverPenalty += roster.UpdateCoverPens(shifts1); 
          for (int k = -1; k < roster.SchedulingPeriod.ShiftTypesCount; k++) {
            if (!this.shiftsDB.getSchedulingPeriodContainsNonAutoAssignShifts() || k < 0 || 
              (roster.SchedulingPeriod.GetShiftType(k)).AutoAllocate) {
              Shift[] shifts2 = new Shift[blockSize];
              boolean different = false;
              for (int i = 0; i < blockSize; i++) {
                if (k < 0) {
                  shifts2[i] = null;
                  if (shifts1[i] != null)
                    different = true; 
                } else {
                  shifts2[i] = this.shiftsDB.GetTestShift(k, day + i);
                  if (shifts1[i] == null || (shifts1[i]).ShiftType.Index != k)
                    different = true; 
                } 
              } 
              if (different)
                if ((shifts1[0] != null || shifts2[0] != null) && (
                  shifts1[blockSize - 1] != null || shifts2[blockSize - 1] != null)) {
                  boolean valid = true;
                  int j;
                  for (j = 0; j < blockSize; j++) {
                    if (employee1.ViolationsForAssigningShift(shifts2[j]) == -1) {
                      valid = false;
                      break;
                    } 
                  } 
                  if (valid) {
                    roster.CacheEmployeePenalties(this.cachePosition + 1, this.cacheViolationPens);
                    for (j = 0; j < blockSize; j++)
                      roster.AssignShift(employee1, shifts2[j]); 
                    if (this.CALCULATE_COVER_PENALTY) {
                      employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
                      employee1.RecalculatePenalty(originalPenalty, day, day + blockSize - 1, true);
                    } else {
                      employee1.RecalculatePenalty(day, day + blockSize - 1, true);
                    } 
                    this.TotalEvaluations++;
                    if (roster.getTotalPenalty() < originalPenalty) {
                      moveMade = true;
                      movesCount++;
                      this.BlockSizeMovesCount[blockSize - 1] = this.BlockSizeMovesCount[blockSize - 1] + 1;
                      if (shifts2[0] != null)
                        for (j = 0; j < blockSize; j++) {
                          roster.UnAssignShift(shifts2[j]);
                          if (shifts2[j] != null)
                            roster.AssignShift(employee1, (Shift)shifts2[j].Clone()); 
                        }  
                      if (this.cacheViolationPens)
                        if (employee1.EmployeeDescription.Contract != null && 
                          employee1.EmployeeDescription.Contract.BadPatternsIsOn)
                          employee1.RecalculatePenalty();  
                      this.employeeLastChanged[employee1.Index] = this.iterationCount;
                      for (j = 0; j < blockSize; j++) {
                        if (shifts1[j] != null && shifts2[j] != null) {
                          if ((shifts1[j]).ShiftType != (shifts2[j]).ShiftType)
                            this.dayLastChanged[day + j] = this.iterationCount; 
                        } else if (shifts1[j] != null || shifts2[j] != null) {
                          this.dayLastChanged[day + j] = this.iterationCount;
                        } 
                      } 
                      this.lastImprovementEmployee1 = employee1.Index;
                      this.lastImprovementBlockSize = blockSize;
                      this.lastImprovementDay = day;
                      this.lastImprovementShiftType = k;
                      break;
                    } 
                    for (j = 0; j < blockSize; j++)
                      roster.UnAssignShift(shifts2[j]); 
                    roster.RestoreEmployeePenalties(this.cachePosition + 1, this.cacheViolationPens);
                    if (this.CALCULATE_COVER_PENALTY)
                      roster.CoverPenalty += roster.UpdateCoverPens(shifts2); 
                  } 
                  if (this.lastImprovementDay == day && 
                    this.lastImprovementEmployee1 == employee1.Index && 
                    this.lastImprovementShiftType == k && 
                    this.lastImprovementBlockSize == blockSize) {
                    this.finished = true;
                    break;
                  } 
                }  
            } 
          } 
          if (!moveMade) {
            for (int i = 0; i < blockSize; i++)
              roster.AssignShift(employee1, shifts1[i]); 
            roster.RestoreEmployeePenalties(this.cachePosition, this.cacheViolationPens);
            if (this.CALCULATE_COVER_PENALTY)
              roster.CoverPenalty += roster.UpdateCoverPens(shifts1); 
            if (employee1.EmployeeDescription.Contract != null && 
              employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
              employee1.EmployeeDescription.BadPatternConstraint != null)
              employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1); 
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
