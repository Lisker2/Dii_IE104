package org.example.util.ASAP.NRP.Solvers.VariableDepthSearch;

import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.Shift;
import java.util.Random;

public class Vrt2Emp1Swp_M {
  public boolean Stopped = true;
  
  public String Author = "T";
  
  public String Title = "Vrt2Emp1Swp";
  
  public long TotalEvaluations = 0L;
  
  public int RandomSeed = 1;
  
  boolean finished;
  
  int lastImprovementEmployee1;
  
  int lastImprovementEmployee2;
  
  int lastImprovementDay;
  
  int lastImprovementBlockSize;
  
  public int[] BlockSizeMovesCount;
  
  int iterationCount = 0;
  
  int[] employeeLastChanged;
  
  int[] dayLastChanged;
  
  TestShiftDB shiftsDB;
  
  public boolean TIME_LIMIT = false;
  
  public boolean VERBOSE = true;
  
  public int MAX_BLOCK_SIZE = 2;
  
  public double MAX_RUN_TIME = 10.0D;
  
  public boolean[] TRY_BLOCK_SIZE;
  
  public int VIOLATION_HEURISTIC_THRESHOLD = 0;
  
  boolean cacheViolationPens = false;
  
  DateTime FinishTime;
  
  Random rand;
  
  boolean randSet;
  
  public Vrt2Emp1Swp_M(TestShiftDB shiftsDB) {
    this.randSet = false;
    this.shiftsDB = shiftsDB;
    UpdateTitle();
    this.TRY_BLOCK_SIZE = new boolean[this.MAX_BLOCK_SIZE];
    for (int i = 0; i < this.TRY_BLOCK_SIZE.length; i++)
      this.TRY_BLOCK_SIZE[i] = true; 
  }
  
  public void setRNG(Random rand) {
    this.rand = rand;
    this.randSet = true;
  }
  
  public void Solve(Roster roster) {
    if (this.TIME_LIMIT)
      this.FinishTime = DateTime.getNow().AddSeconds(this.MAX_RUN_TIME); 
    if (!this.randSet)
      this.rand = new Random(this.RandomSeed); 
    ShuffleEmployees(roster);
    UpdateTitle();
    this.Stopped = false;
    this.TotalEvaluations = 0L;
    Search(roster);
    if (this.VERBOSE)
      System.out.println("Search finished. Total moves tested=" + this.TotalEvaluations); 
  }
  
  private void Search(Roster roster) {
    if (this.VIOLATION_HEURISTIC_THRESHOLD > 0) {
      this.cacheViolationPens = true;
    } else {
      this.cacheViolationPens = false;
    } 
    this.finished = false;
    this.lastImprovementEmployee1 = -1;
    this.lastImprovementEmployee2 = -1;
    this.lastImprovementDay = -1;
    this.lastImprovementBlockSize = -1;
    int totalImprovingMoves = 0;
    int noImprovement = 0;
    boolean repeat = true;
    this.iterationCount = 0;
    this.employeeLastChanged = new int[roster.Employees.length];
    this.dayLastChanged = new int[roster.SchedulingPeriod.NumDaysInPeriod];
    this.BlockSizeMovesCount = new int[this.MAX_BLOCK_SIZE];
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
          movesCount = SwapShiftsDepthOne(roster, j);
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
    } 
    if (this.VERBOSE)
      System.out.println("Variable depth search finished. Total moves tested=" + this.TotalEvaluations + ", totalImprovingMoves=" + totalImprovingMoves); 
  }
  
  private int SwapShiftsDepthOne(Roster roster, int blockSize) {
    int movesCount = 0;
    if (blockSize < 1 || blockSize > roster.SchedulingPeriod.NumDaysInPeriod)
      return 0; 
    for (int i = 0; i < roster.Employees.length; i++) {
      Employee employee1 = roster.Employees[i];
      for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod - blockSize + 1; day++) {
        if (this.VERBOSE)
          System.out.println("V/2E/1S: Swap..D1: BlockSize=" + blockSize + 
              " E1=" + employee1.EmployeeDescription.getName() + 
              " Day=" + day + 
              " Evals=" + this.TotalEvaluations + 
              " Pen=" + roster.getTotalPenalty()); 
        if (this.shiftsDB.getSchedulingPeriodContainsNonAutoAssignShifts()) {
          boolean fixedShift = false;
          for (int k = 0; k < blockSize; k++) {
            Shift s = employee1.GetShift(day + k);
            if (s != null && s.isFixed()) {
              fixedShift = true;
              break;
            } 
          } 
          if (fixedShift)
            continue; 
        } 
        if (this.shiftsDB.getEmployeeDescriptionHasFrozenDays()[employee1.EmployeeDescription.IndexID]) {
          boolean frozen = false;
          for (int k = 0; k < blockSize; k++) {
            if (employee1.EmployeeDescription.FrozenDay[day + k]) {
              frozen = true;
              break;
            } 
          } 
          if (frozen)
            continue; 
        } 
        boolean moveMade = false;
        int originalPenalty = roster.getTotalPenalty();
        Shift[] shifts1 = new Shift[blockSize];
        roster.CacheEmployeePenalties(0, this.cacheViolationPens);
        boolean employee1Violations = false;
        boolean dayChanged = false;
        for (int x = 0; x < blockSize; x++) {
          shifts1[x] = employee1.GetShift(day + x);
          roster.UnAssignShift(shifts1[x]);
          if (employee1.ConstraintViolationPenalties[day + x] >= this.VIOLATION_HEURISTIC_THRESHOLD)
            employee1Violations = true; 
          if (this.iterationCount - this.dayLastChanged[day + x] <= this.MAX_BLOCK_SIZE)
            dayChanged = true; 
        } 
        roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
        for (int j = 0; j < roster.Employees.length; j++) {
          if (i == j)
            continue; 
          Employee employee2 = roster.Employees[j];
          if (!dayChanged && 
            this.iterationCount - this.employeeLastChanged[employee1.Index] > this.MAX_BLOCK_SIZE && 
            this.iterationCount - this.employeeLastChanged[employee2.Index] > this.MAX_BLOCK_SIZE)
            continue; 
          if (this.shiftsDB.getEmployeeDescriptionHasFrozenDays()[employee2.EmployeeDescription.IndexID]) {
            boolean frozen = false;
            for (int m = 0; m < blockSize; m++) {
              if (employee2.EmployeeDescription.FrozenDay[day + m]) {
                frozen = true;
                break;
              } 
            } 
            if (frozen)
              continue; 
          } 
          Shift[] shifts2 = new Shift[blockSize];
          boolean employee2Violations = false;
          for (int k = 0; k < blockSize; k++) {
            shifts2[k] = employee2.GetShift(day + k);
            if (employee2.ConstraintViolationPenalties[day + k] >= this.VIOLATION_HEURISTIC_THRESHOLD)
              employee2Violations = true; 
          } 
          if (!employee1Violations && !employee2Violations)
            continue; 
          if (this.shiftsDB.getSchedulingPeriodContainsNonAutoAssignShifts()) {
            boolean fixedShifts = false;
            for (int m = 0; m < blockSize; m++) {
              if (shifts2[m] != null && shifts2[m].isFixed()) {
                fixedShifts = true;
                break;
              } 
            } 
            if (fixedShifts)
              continue; 
          } 
          if ((shifts1[0] != null || shifts2[0] != null) && (
            shifts1[blockSize - 1] != null || shifts2[blockSize - 1] != null)) {
            boolean different = false;
            int m;
            for (m = 0; m < blockSize; m++) {
              if (shifts1[m] != null && shifts2[m] != null) {
                if ((shifts1[m]).ShiftType != (shifts2[m]).ShiftType) {
                  different = true;
                  break;
                } 
              } else if (shifts1[m] != null || shifts2[m] != null) {
                different = true;
                break;
              } 
            } 
            if (different) {
              for (m = 0; m < blockSize; m++)
                roster.UnAssignShift(shifts2[m]); 
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
                roster.CacheEmployeePenalties(1, this.cacheViolationPens);
                for (n = 0; n < blockSize; n++) {
                  roster.AssignShift(employee2, shifts1[n]);
                  roster.AssignShift(employee1, shifts2[n]);
                } 
                employee1.Roster.CoverPenalty += employee1.Roster.UpdateCoverPens(shifts2);
                employee2.Roster.CoverPenalty += employee2.Roster.UpdateCoverPens(shifts1);
                employee1.RecalculatePenalty(originalPenalty + employee2.Penalty, day, day + blockSize - 1, true);
                employee2.RecalculatePenalty(originalPenalty, day, day + blockSize - 1, true);
                this.TotalEvaluations++;
                if (roster.getTotalPenalty() < originalPenalty) {
                  moveMade = true;
                } else {
                  moveMade = false;
                } 
                if (moveMade) {
                  this.BlockSizeMovesCount[blockSize - 1] = this.BlockSizeMovesCount[blockSize - 1] + 1;
                  this.lastImprovementEmployee1 = employee1.Index;
                  this.lastImprovementEmployee2 = employee2.Index;
                  this.lastImprovementBlockSize = blockSize;
                  this.lastImprovementDay = day;
                  this.employeeLastChanged[employee1.Index] = this.iterationCount;
                  this.employeeLastChanged[employee2.Index] = this.iterationCount;
                  for (n = 0; n < blockSize; n++) {
                    if (shifts1[n] != null && shifts2[n] != null) {
                      if ((shifts1[n]).ShiftType != (shifts2[n]).ShiftType)
                        this.dayLastChanged[day + n] = this.iterationCount; 
                    } else if (shifts1[n] != null || shifts2[n] != null) {
                      this.dayLastChanged[day + n] = this.iterationCount;
                    } 
                  } 
                  break;
                } 
                for (n = 0; n < blockSize; n++) {
                  roster.UnAssignShift(shifts1[n]);
                  roster.UnAssignShift(shifts2[n]);
                } 
                roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
                roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
                roster.RestoreEmployeePenalties(1, this.cacheViolationPens);
              } 
              for (n = 0; n < blockSize; n++)
                roster.AssignShift(employee2, shifts2[n]); 
              roster.CoverPenalty += roster.UpdateCoverPens(shifts2);
              if (employee2.EmployeeDescription.Contract != null && 
                employee2.EmployeeDescription.Contract.BadPatternsIsOn && 
                employee2.EmployeeDescription.BadPatternConstraint != null)
                employee2.EmployeeDescription.BadPatternConstraint.Calculate(employee2, day, day + blockSize - 1); 
              if (this.lastImprovementEmployee1 == employee1.Index && 
                this.lastImprovementEmployee2 == employee2.Index && 
                this.lastImprovementDay == day && 
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
          for (int k = 0; k < blockSize; k++)
            roster.AssignShift(employee1, shifts1[k]); 
          roster.CoverPenalty += roster.UpdateCoverPens(shifts1);
          roster.RestoreEmployeePenalties(0, this.cacheViolationPens);
          if (employee1.EmployeeDescription.Contract != null && 
            employee1.EmployeeDescription.Contract.BadPatternsIsOn && 
            employee1.EmployeeDescription.BadPatternConstraint != null)
            employee1.EmployeeDescription.BadPatternConstraint.Calculate(employee1, day, day + blockSize - 1); 
        } 
        if (this.Stopped || this.finished || (this.TIME_LIMIT && DateTime.getNow().isGreaterThan(this.FinishTime)))
          return movesCount; 
        continue;
      } 
    } 
    return movesCount;
  }
  
  private void UpdateTitle() {
    this.Title = "Vrt2Emp1Swp ";
    this.Title = String.valueOf(this.Title) + ", MAX_BLOCK_SIZE=" + this.MAX_BLOCK_SIZE;
    if (this.TIME_LIMIT)
      this.Title = String.valueOf(this.Title) + ", Max time limit "; 
    this.Title = String.valueOf(this.Title) + ", VIOLATION_HEURISTIC_THRESHOLD=" + this.VIOLATION_HEURISTIC_THRESHOLD;
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
