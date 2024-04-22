package org.example.util.PersonnelScheduling;

import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.Shift;
import org.example.util.ASAP.NRP.Core.Tools.RosterLoader;
import org.example.util.ASAP.NRP.Solvers.Crossover.Crossover;
import org.example.util.ASAP.NRP.Solvers.VariableDepthSearch.Hrz1Emp1Swp_M;
import org.example.util.ASAP.NRP.Solvers.VariableDepthSearch.New1Emp1Swp_M;
import org.example.util.ASAP.NRP.Solvers.VariableDepthSearch.PatternTester;
import org.example.util.ASAP.NRP.Solvers.VariableDepthSearch.VDS_B;
import org.example.util.ASAP.NRP.Solvers.VariableDepthSearch.Vrt2Emp1Swp_M;
import org.example.util.AbstractClasses.ProblemDomain;


import java.io.File;
import java.io.InputStream;

public class PersonnelScheduling extends ProblemDomain {
  int MAX_VDS_TIME = 10000;
  
  int MAX_BLOCK_SIZE_AT_DEPTH_ZERO = 3;
  
  int VDS_MAX_DEPTH = 250;
  
  final boolean SHOW_ERROR_MSGS = false;
  
  int bestPenalty = -1;
  
  Roster bestRoster;
  
  TestShiftDB2 shiftsDB;
  
  public Roster[] solutions = new Roster[2];
  
  public String schedulingPeriodID = null;

  String name = "";
  
  final Instance[] dataSets = new Instance[] { 
      new Instance(this.name, 3280, false),
    };

  public int num_instances = this.dataSets.length;
  
  final int[] localSearches;
  
  final int[] ruinRecreates;
  
  final int[] crossovers;
  
  final int[] mutators;
  
  final int[] heuristicsWhichUseDepthOfSearch;
  
  final int[] heuristicsWhichUseIntensityOfMut;


  
  public PersonnelScheduling(long seed, String name) {
    super(seed);
    this.name = name;
    this.localSearches = new int[] { 0, 1, 2, 3, 4 };
    this.ruinRecreates = new int[] { 5, 6, 7 };
    this.crossovers = new int[] { 8, 9, 10 };
    this.mutators = new int[] { 11 };
    this.heuristicsWhichUseDepthOfSearch = new int[] { 3, 4 };
    this.heuristicsWhichUseIntensityOfMut = new int[] { 6, 7, 8, 11 };
  }
  
  public int getNumberOfHeuristics() {
    return 12;
  }
  
  public int[] getHeuristicsOfType(ProblemDomain.HeuristicType hType) {
    switch (hType) {
      case LOCAL_SEARCH:
        return this.localSearches;
      case RUIN_RECREATE:
        return this.ruinRecreates;
      case CROSSOVER:
        return this.crossovers;
      case MUTATION:
        return this.mutators;
    } 
    return null;
  }
  
  public int[] getHeuristicsThatUseDepthOfSearch() {
    return this.heuristicsWhichUseDepthOfSearch;
  }
  
  public int[] getHeuristicsThatUseIntensityOfMutation() {
    return this.heuristicsWhichUseIntensityOfMut;
  }
  
  private boolean isCrossover(int hid) {
    if (this.crossovers == null || this.crossovers.length == 0)
      return false; 
    for (int i = 0; i < this.crossovers.length; i++) {
      if (this.crossovers[i] == hid)
        return true; 
    } 
    return false;
  }
  
  private boolean isLocalSearch(int hid) {
    if (this.localSearches == null || this.localSearches.length == 0)
      return false; 
    for (int i = 0; i < this.localSearches.length; i++) {
      if (this.localSearches[i] == hid)
        return true; 
    } 
    return false;
  }
  
  public double applyHeuristic(int heuristicID, int solIn1, int solIn2, int solOut1) {
    long startTime = System.currentTimeMillis();
    if (!isCrossover(heuristicID)) {
      double s1 = applyHeuristic(heuristicID, solIn1, solOut1);
      if (heuristicID > 0 && heuristicID < this.heuristicCallTimeRecord.length)
        this.heuristicCallTimeRecord[heuristicID] = this.heuristicCallTimeRecord[heuristicID] + (int)(System.currentTimeMillis() - startTime); 
      return s1;
    } 
    Roster r1 = new Roster(this.bestRoster.SchedulingPeriod);
    Roster r2 = new Roster(this.bestRoster.SchedulingPeriod);
    if (heuristicID == 8) {
      int n = (int)Math.round((1.0D - getIntensityOfMutation()) * 16.0D);
      int MAX_BEST_ASSIGNMENTS = 4 + n;
      Crossover.MEH_Crossover(this.solutions[solIn1], this.solutions[solIn2], 
          r1, r2, MAX_BEST_ASSIGNMENTS);
    } else if (heuristicID == 9) {
      Crossover.SS_Crossover(this.solutions[solIn1], this.solutions[solIn2], r1, r2);
    } else if (heuristicID == 10) {
      Crossover.Crossover_A(this.solutions[solIn1], this.solutions[solIn2], r1, r2);
    } else {
      System.out.println("Unknown heuristicID: " + heuristicID);
      return 0.0D;
    } 
    if (heuristicID > 0 && heuristicID < this.heuristicCallRecord.length) {
      this.heuristicCallRecord[heuristicID] = this.heuristicCallRecord[heuristicID] + 1;
      this.heuristicCallTimeRecord[heuristicID] = this.heuristicCallTimeRecord[heuristicID] + (int)(System.currentTimeMillis() - startTime);
    } 
    if (r1.getTotalPenalty() < r2.getTotalPenalty()) {
      int i = r1.getTotalPenalty();
      this.solutions[solOut1] = r1;
      if (i < this.bestPenalty) {
        this.bestPenalty = i;
        this.bestRoster = (Roster)r1.Clone();
      } 
      return i;
    } 
    int pen = r2.getTotalPenalty();
    this.solutions[solOut1] = r2;
    if (pen < this.bestPenalty) {
      this.bestPenalty = pen;
      this.bestRoster = (Roster)r2.Clone();
    } 
    return pen;
  }
  
  public double applyHeuristic(int heuristicID, int solutionIn, int solutionOut) {
    long startTime = System.currentTimeMillis();
    Roster tempRoster = (Roster)this.solutions[solutionIn].Clone();
    int origPen = tempRoster.getTotalPenalty();
    if (heuristicID > 0 && heuristicID < this.heuristicCallRecord.length)
      this.heuristicCallRecord[heuristicID] = this.heuristicCallRecord[heuristicID] + 1; 
    if (isCrossover(heuristicID)) {
      copySolution(solutionIn, solutionOut);
      if (heuristicID > 0 && heuristicID < this.heuristicCallTimeRecord.length)
        this.heuristicCallTimeRecord[heuristicID] = this.heuristicCallTimeRecord[heuristicID] + (int)(System.currentTimeMillis() - startTime); 
      return this.solutions[solutionIn].getTotalPenalty();
    } 
    if (heuristicID == 0) {
      int MAX_BLOCK_SIZE = 5;
      boolean[] N_TRY_BLOCK_SIZE = new boolean[MAX_BLOCK_SIZE];
      for (int i = 0; i < N_TRY_BLOCK_SIZE.length; i++)
        N_TRY_BLOCK_SIZE[i] = true; 
      New1Emp1Swp_M n1 = new New1Emp1Swp_M(this.shiftsDB);
      n1.TIME_LIMIT = false;
      n1.CALCULATE_COVER_PENALTY = true;
      n1.SKIP_ZERO_PENS = false;
      n1.MAX_BLOCK_SIZE = MAX_BLOCK_SIZE;
      n1.VIOLATION_HEURISTIC_THRESHOLD = 0;
      n1.setRNG(this.rng);
      n1.VERBOSE = false;
      n1.TRY_BLOCK_SIZE = N_TRY_BLOCK_SIZE;
      n1.Solve(tempRoster);
    } else if (heuristicID == 1) {
      int MAX_BLOCK_SIZE = 5;
      boolean[] H_TRY_BLOCK_SIZE = new boolean[MAX_BLOCK_SIZE];
      for (int i = 0; i < H_TRY_BLOCK_SIZE.length; i++)
        H_TRY_BLOCK_SIZE[i] = true; 
      Hrz1Emp1Swp_M h1 = new Hrz1Emp1Swp_M(this.shiftsDB);
      h1.TIME_LIMIT = false;
      h1.CALCULATE_COVER_PENALTY = true;
      h1.SKIP_ZERO_PEN = false;
      h1.MAX_BLOCK_SIZE = MAX_BLOCK_SIZE;
      h1.VIOLATION_HEURISTIC_THRESHOLD = 0;
      h1.setRNG(this.rng);
      h1.VERBOSE = false;
      h1.TRY_BLOCK_SIZE = H_TRY_BLOCK_SIZE;
      h1.Solve(tempRoster);
    } else if (heuristicID == 2) {
      int MAX_BLOCK_SIZE = 5;
      boolean[] V_TRY_BLOCK_SIZE = new boolean[MAX_BLOCK_SIZE];
      for (int i = 0; i < V_TRY_BLOCK_SIZE.length; i++)
        V_TRY_BLOCK_SIZE[i] = true; 
      Vrt2Emp1Swp_M v1 = new Vrt2Emp1Swp_M(this.shiftsDB);
      v1.TIME_LIMIT = false;
      v1.MAX_BLOCK_SIZE = MAX_BLOCK_SIZE;
      v1.VIOLATION_HEURISTIC_THRESHOLD = 0;
      v1.setRNG(this.rng);
      v1.VERBOSE = false;
      v1.TRY_BLOCK_SIZE = V_TRY_BLOCK_SIZE;
      v1.Solve(tempRoster);
    } else if (heuristicID == 3) {
      int maxRunTime = (int)(getDepthOfSearch() * this.MAX_VDS_TIME);
      VDS_B search = new VDS_B();
      search.USE_QUICKSEARCH = false;
      search.setRNG(this.rng);
      search.VERBOSE = false;
      search.SYSTEM_TIME_RANDOM_SEED = false;
      search.TIME_LIMIT = true;
      search.PreferredRunTime = maxRunTime;
      search.POSITIVE_GAIN_HEURISTIC = true;
      search.VIOLATION_FLAG_HEURISTIC = false;
      search.MAX_DEPTH = this.VDS_MAX_DEPTH;
      search.NEXT_MOVE_MAX_BLOCK_SIZE = 5;
      search.MAX_BLOCK_SIZE_AT_DEPTH_ZERO = this.MAX_BLOCK_SIZE_AT_DEPTH_ZERO;
      search.TEST_PATTERNS_MAX_ATTEMPTS = 2;
      search.TEST_PATTERNS_AT_START = false;
      search.SATISFY_WEEKENDS = false;
      search.TEST_PATTERNS_DURING_IMPROVE_EMPLOYEE = false;
      search.Solve(tempRoster);
    } else if (heuristicID == 4) {
      int maxRunTime = (int)(getDepthOfSearch() * this.MAX_VDS_TIME);
      VDS_B search = new VDS_B();
      search.USE_QUICKSEARCH = false;
      search.setRNG(this.rng);
      search.VERBOSE = false;
      search.SYSTEM_TIME_RANDOM_SEED = false;
      search.TIME_LIMIT = true;
      search.PreferredRunTime = maxRunTime;
      search.POSITIVE_GAIN_HEURISTIC = true;
      search.VIOLATION_FLAG_HEURISTIC = false;
      search.MAX_DEPTH = this.VDS_MAX_DEPTH;
      search.NEXT_MOVE_MAX_BLOCK_SIZE = 5;
      search.MAX_BLOCK_SIZE_AT_DEPTH_ZERO = this.MAX_BLOCK_SIZE_AT_DEPTH_ZERO;
      search.TEST_PATTERNS_MAX_ATTEMPTS = 2;
      search.TEST_PATTERNS_AT_START = true;
      search.SATISFY_WEEKENDS = true;
      search.TEST_PATTERNS_DURING_IMPROVE_EMPLOYEE = true;
      search.Solve(tempRoster);
    } else if (heuristicID == 5) {
      int employeeIndex = this.rng.nextInt(tempRoster.Employees.length);
      PatternTester pt = new PatternTester();
      pt.ImprovePattern(tempRoster, this.shiftsDB, employeeIndex, this.rng);
    } else if (heuristicID == 6 || heuristicID == 7) {
      long numToTryUnassign;
      boolean[] pats = new boolean[tempRoster.Employees.length];
      if (heuristicID == 6) {
        numToTryUnassign = Math.round(getIntensityOfMutation() * 4.0D) + 2L;
      } else {
        numToTryUnassign = Math.round(getIntensityOfMutation() * tempRoster.Employees.length);
      } 
      int x;
      for (x = 0; x < numToTryUnassign; x++) {
        int index = this.rng.nextInt(tempRoster.Employees.length);
        if (!pats[index]) {
          tempRoster.Employees[index].UnAssignAllShifts();
          pats[index] = true;
        } 
      } 
      for (x = 0; x < pats.length; x++) {
        if (pats[x]) {
          PatternTester pt = new PatternTester();
          pt.ImprovePattern(tempRoster, this.shiftsDB, x, this.rng);
        } 
      } 
    } else if (heuristicID == 11) {
      long attempts = Math.round(getIntensityOfMutation() * 80.0D);
      for (int x = 0; x < attempts; x++) {
        int employeeIndex = this.rng.nextInt(tempRoster.Employees.length);
        int dayIndex = this.rng.nextInt(tempRoster.SchedulingPeriod.NumDaysInPeriod);
        Employee employee1 = tempRoster.Employees[employeeIndex];
        for (int j = 0; j < tempRoster.SchedulingPeriod.ShiftTypesCount; ) {
          Shift shift = employee1.ShiftsOnDay[dayIndex][j];
          if (shift == null) {
            j++;
            continue;
          } 
          Shift[] shifts1 = new Shift[1];
          shifts1[0] = shift;
          tempRoster.UnAssignShift(shift);
          tempRoster.CoverPenalty += tempRoster.UpdateCoverPens(shifts1);
          employee1.RecalculatePenalty();
          break;
        } 
      } 
    } else {
      System.out.println("Unknown heuristicID: " + heuristicID);
    } 
    int pen = tempRoster.getTotalPenalty();
    if (pen < this.bestPenalty) {
      this.bestPenalty = pen;
      this.bestRoster = (Roster)tempRoster.Clone();
    } 
    if (solutionOut >= 0 && solutionOut < this.solutions.length)
      if (isLocalSearch(heuristicID) && 
        tempRoster.getTotalPenalty() > origPen) {
        copySolution(solutionIn, solutionOut);
      } else {
        if (this.solutions[solutionOut] == null)
          this.solutions[solutionOut] = (Roster)tempRoster.Clone(); 
        SetRoster(this.solutions[solutionOut], tempRoster);
      }  
    if (heuristicID > 0 && heuristicID < this.heuristicCallTimeRecord.length)
      this.heuristicCallTimeRecord[heuristicID] = this.heuristicCallTimeRecord[heuristicID] + (int)(System.currentTimeMillis() - startTime); 
    return pen;
  }
  
  public int getNumberOfInstances() {
    return this.dataSets.length;
  }
  
  public void setMemorySize(int solutionCount) {
    if (this.solutions == null)
      this.solutions = new Roster[2]; 
    this.solutions = ArrayResize(this.solutions, solutionCount);
  }
  
  public double getBestSolutionValue() {
    return this.bestRoster.getTotalPenalty();
  }
  
  public void loadInstance(int instanceID) {
    emptySolutions();
    if (instanceID < 0 || instanceID >= this.dataSets.length) {
      System.out.println("Invalid instanceID: " + instanceID);
      return;
    } 
    this.schedulingPeriodID = this.dataSets[instanceID].getName();
    RosterLoader rosterLoader = new RosterLoader();
    rosterLoader.VERBOSE = false;
    try {
      InputStream fis = getClass().getClassLoader()
        .getResourceAsStream(this.name);
      if (fis != null) {
        this.bestRoster = rosterLoader.CreateEmptyRoster(fis);
      } else {
        String fName = this.name;
        File file = new File(fName);
        if (!file.exists()) {
          fName = this.name;
          file = new File(fName);
          if (!file.exists())
            System.out.println("Unable to find data file."); 
        } 
        this.bestRoster = rosterLoader.CreateEmptyRoster(fName);
      } 
    } catch (Exception ex) {
      System.out.println("Error: " + ex.getMessage());
    } 
    
    if (this.bestRoster == null) {
      System.out.println("Problem loading instanceID: " + instanceID + " (" + this.schedulingPeriodID + ")");
      return;
    } 
    this.bestRoster.RecalculateAllPenalties();
    this.shiftsDB = new TestShiftDB2(this.bestRoster);
    MakeInitialAssignments(this.bestRoster);
    this.bestPenalty = this.bestRoster.getTotalPenalty();
  }
  
  public void initialiseSolution(int index) {
    if (index < 0 || index >= this.solutions.length)
      return; 
    if (this.solutions[index] == null)
      this.solutions[index] = (Roster)this.bestRoster.Clone(); 
    this.solutions[index].Empty();
    MakeInitialAssignments(this.solutions[index]);
    int pen = this.solutions[index].getTotalPenalty();
    if (pen < this.bestPenalty) {
      this.bestPenalty = pen;
      this.bestRoster = (Roster)this.solutions[index].Clone();
    } 
  }
  
  public double getFunctionValue(int solutionIndex) {
    return this.solutions[solutionIndex].getTotalPenalty();
  }
  
  public String bestSolutionToString() {
    if (this.bestRoster == null)
      return ""; 
    return this.bestRoster.toString();
  }
  
  public String solutionToString(int solIndex) {
    if (solIndex >= 0 && solIndex < this.solutions.length)
      if (this.solutions[solIndex] != null)
        return this.solutions[solIndex].toString();  
    return "";
  }
  
  public String toString() {
    return "Personnel Scheduling";
  }
  
  public void copySolution(int src, int dest) {
    if (this.solutions[src] == null)
      return; 
    if (this.solutions[dest] == null)
      this.solutions[dest] = (Roster)this.solutions[src].Clone(); 
    SetRoster(this.solutions[dest], this.solutions[src]);
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
            Shift shift1 = (Shift)shift2.Clone();
            if (shift1 != null)
              roster1.AssignShift(employee1, shift1); 
          } 
        } 
      } 
    } 
    roster1.RecalculateAllPenalties();
    if (roster1.getTotalPenalty() != roster2.getTotalPenalty());
  }
  
  private void emptySolutions() {
    for (int i = 0; i < this.solutions.length; i++)
      this.solutions[i] = null; 
  }
  
  private void MakeInitialAssignments(Roster roster) {
    int MAX_BLOCK_SIZE = 5;
    boolean[] N_TRY_BLOCK_SIZE = new boolean[MAX_BLOCK_SIZE];
    N_TRY_BLOCK_SIZE[0] = true;
    N_TRY_BLOCK_SIZE[1] = true;
    N_TRY_BLOCK_SIZE[2] = true;
    N_TRY_BLOCK_SIZE[3] = true;
    N_TRY_BLOCK_SIZE[4] = true;
    New1Emp1Swp_M n1 = new New1Emp1Swp_M(this.shiftsDB);
    n1.TIME_LIMIT = false;
    n1.CALCULATE_COVER_PENALTY = true;
    n1.SKIP_ZERO_PENS = false;
    n1.MAX_BLOCK_SIZE = MAX_BLOCK_SIZE;
    n1.VIOLATION_HEURISTIC_THRESHOLD = 0;
    n1.setRNG(this.rng);
    n1.VERBOSE = false;
    n1.TRY_BLOCK_SIZE = N_TRY_BLOCK_SIZE;
    n1.Solve(roster);
  }
  
  private static Roster[] ArrayResize(Roster[] input, int newSize) {
    Roster[] newArray = new Roster[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public boolean compareSolutions(int solutionIndex1, int solutionIndex2) {
    if (this.solutions[solutionIndex1] == null || this.solutions[solutionIndex2] == null)
      return false; 
    Roster r1 = this.solutions[solutionIndex1];
    Roster r2 = this.solutions[solutionIndex2];
    if (r1.getTotalPenalty() != r2.getTotalPenalty())
      return false; 
    for (int i = 0; i < r1.Employees.length; i++) {
      Employee e1 = r1.Employees[i];
      Employee e2 = r2.GetEmployee(e1.EmployeeDescription.ID);
      for (int j = 0; j < e1.ShiftAssignments.length; j++) {
        if (e1.ShiftAssignments[j] != e2.ShiftAssignments[j])
          return false; 
      } 
    } 
    return true;
  }
}
