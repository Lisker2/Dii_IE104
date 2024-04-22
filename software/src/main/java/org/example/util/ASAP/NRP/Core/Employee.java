package org.example.util.ASAP.NRP.Core;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;

public class Employee implements Comparable<Object> {
  public EmployeeDescription EmployeeDescription;
  
  public Roster Roster;
  
  public int Index = 0;
  
  public int ShiftsCount = 0;
  
  public Shift[][] ShiftsOnDay;
  
  public int[] DayType;
  
  public boolean[] ShiftAssignments;
  
  public boolean[] NightShifts;
  
  public int[] ShiftCountPerDay;
  
  public int[] ShiftTypeCount;
  
  public int[] ShiftGroupCount;
  
  public int[][] ShiftGroupPerDayCount;
  
  public int[][] ShiftTypePerWeekCount;
  
  public double[] HoursWorkedPerWeek;
  
  public int DaysOffCount = 0;
  
  public int[] ConstraintViolationPenalties;
  
  public String[] ViolationDescriptions;
  
  public int Penalty = 0;
  
  public double HoursWorked = 0.0D;
  
  public int HolidayDaysOffGranted = 0;
  
  public int[] PenaltyCache;
  
  public int[][] ConstraintViolationPenaltiesCache;
  
  public Employee(Roster roster, EmployeeDescription employeeDescription) {
    this.Roster = roster;
    this.EmployeeDescription = employeeDescription;
    SchedulingPeriod schedulingPeriod = roster.SchedulingPeriod;
    this.DaysOffCount = schedulingPeriod.NumDaysInPeriod;
    this.DayType = new int[schedulingPeriod.NumDaysInPeriod];
    int i;
    for (i = 0; i < this.DayType.length; i++) {
      this.DayType[i] = 0;
      if (employeeDescription.DayOffRequestIsWork[i])
        this.DaysOffCount--; 
    } 
    this.ShiftAssignments = new boolean[schedulingPeriod.NumDaysInPeriod * schedulingPeriod.ShiftTypesCount];
    this.NightShifts = new boolean[schedulingPeriod.NumDaysInPeriod];
    this.ShiftCountPerDay = new int[schedulingPeriod.NumDaysInPeriod];
    this.ShiftTypeCount = new int[schedulingPeriod.ShiftTypesCount];
    this.ShiftGroupCount = new int[schedulingPeriod.ShiftGroupsCount];
    this.ShiftGroupPerDayCount = new int[schedulingPeriod.NumDaysInPeriod][schedulingPeriod.ShiftGroupsCount];
    this.ShiftTypePerWeekCount = new int[schedulingPeriod.NumWeeksInPeriod][schedulingPeriod.ShiftTypesCount];
    this.ShiftsOnDay = new Shift[schedulingPeriod.NumDaysInPeriod][schedulingPeriod.ShiftTypesCount];
    this.HoursWorkedPerWeek = new double[schedulingPeriod.NumWeeksInPeriod];
    this.ConstraintViolationPenalties = new int[schedulingPeriod.NumDaysInPeriod];
    this.ViolationDescriptions = new String[schedulingPeriod.NumDaysInPeriod];
    this.Roster.getClass();
    this.PenaltyCache = new int[500];
    this.Roster.getClass();
    this.ConstraintViolationPenaltiesCache = new int[500][];
    i = 0;
    this.Roster.getClass();
    for (; i < 500; i++)
      this.ConstraintViolationPenaltiesCache[i] = new int[schedulingPeriod.NumDaysInPeriod]; 
    for (i = 0; i < schedulingPeriod.NumDaysInPeriod; i++) {
      if (employeeDescription.DayOffRequestIsHoliday[i] && 
        this.DayType[i] == 0) {
        this.DayType[i] = 2;
        this.HolidayDaysOffGranted++;
      } 
      this.ViolationDescriptions[i] = "";
    } 
  }
  
  public int compareTo(Object obj) {
    int p1 = this.Penalty;
    int p2 = ((Employee)obj).Penalty;
    return p2 - p1;
  }
  
  public String toString() {
    return this.EmployeeDescription.getName();
  }
  
  public boolean WorksDuringEntirePeriodOfShift(Shift shift) {
    for (int day = 0; day < this.Roster.SchedulingPeriod.NumDaysInPeriod; day++) {
      for (int index = 0; index < this.Roster.SchedulingPeriod.ShiftTypesCount; index++) {
        Shift shift2 = this.ShiftsOnDay[day][index];
        if (shift2 != null)
          if (shift2.StartTime.isLessThanOrEqual(shift.StartTime) && 
            shift2.EndTime.isGreaterThanOrEqual(shift.EndTime))
            return true;  
      } 
    } 
    return false;
  }
  
  public boolean WorksDuringPeriod(Shift shift) {
    for (int day = 0; day < this.Roster.SchedulingPeriod.NumDaysInPeriod; day++) {
      for (int index = 0; index < this.Roster.SchedulingPeriod.ShiftTypesCount; index++) {
        Shift shift2 = this.ShiftsOnDay[day][index];
        if (shift2 != null)
          if ((shift.StartTime.isGreaterThanOrEqual(shift2.StartTime) && shift.StartTime.isLessThan(shift2.EndTime)) || (
            shift2.StartTime.isGreaterThanOrEqual(shift.StartTime) && shift2.StartTime.isLessThan(shift.EndTime)))
            return true;  
      } 
    } 
    return false;
  }
  
  public Shift GetShift(int rosterDay) {
    if (this.DayType[rosterDay] != 1)
      return null; 
    for (int i = 0; i < this.Roster.SchedulingPeriod.ShiftTypesCount; i++) {
      if (this.ShiftsOnDay[rosterDay][i] != null)
        return this.ShiftsOnDay[rosterDay][i]; 
    } 
    return null;
  }
  
  public void RecalculatePenalty() {
    int originalPenalty = this.Penalty;
    int newPenalty = SoftConstraints.CalculatePenalty(this);
    this.Roster.EmployeesPenalty = this.Roster.EmployeesPenalty - originalPenalty + newPenalty;
    this.Penalty = newPenalty;
    for (int i = 0; i < this.EmployeeDescription.VerticallyRelatedEmployeesCount; i++) {
      String employee2ID = (this.EmployeeDescription.GetVerticallyRelatedEmployee(i)).ID;
      Employee employee2 = this.Roster.GetEmployee(employee2ID);
      if (employee2 != null) {
        originalPenalty = employee2.Penalty;
        newPenalty = SoftConstraints.CalculatePenalty(employee2);
        this.Roster.EmployeesPenalty = this.Roster.EmployeesPenalty - originalPenalty + newPenalty;
        employee2.Penalty = newPenalty;
      } 
    } 
  }
  
  public void RecalculatePenalty(int startDay, int endDay, boolean updateStructure) {
    int originalPenalty = this.Penalty;
    int newPenalty = SoftConstraints.CalculatePenalty(this, startDay, endDay, updateStructure);
    this.Roster.EmployeesPenalty = this.Roster.EmployeesPenalty - originalPenalty + newPenalty;
    this.Penalty = newPenalty;
    for (int i = 0; i < this.EmployeeDescription.VerticallyRelatedEmployeesCount; i++) {
      String employee2ID = (this.EmployeeDescription.GetVerticallyRelatedEmployee(i)).ID;
      Employee employee2 = this.Roster.GetEmployee(employee2ID);
      if (employee2 != null) {
        originalPenalty = employee2.Penalty;
        newPenalty = SoftConstraints.CalculatePenalty(employee2, startDay, endDay, updateStructure);
        this.Roster.EmployeesPenalty = this.Roster.EmployeesPenalty - originalPenalty + newPenalty;
        employee2.Penalty = newPenalty;
      } 
    } 
  }
  
  public void RecalculatePenalty(int maxPenalty, int startDay, int endDay, boolean updateStructure) {
    for (int i = 0; i < this.EmployeeDescription.VerticallyRelatedEmployeesCount; i++) {
      String employee2ID = (this.EmployeeDescription.GetVerticallyRelatedEmployee(i)).ID;
      Employee employee2 = this.Roster.GetEmployee(employee2ID);
      if (employee2 != null) {
        int j = employee2.Penalty;
        int k = SoftConstraints.CalculatePenalty(employee2, startDay, endDay, updateStructure);
        this.Roster.EmployeesPenalty = this.Roster.EmployeesPenalty - j + k;
        employee2.Penalty = k;
      } 
    } 
    int originalPenalty = this.Penalty;
    int maxEmployeePen = maxPenalty - this.Roster.getTotalPenalty() - originalPenalty;
    int newPenalty = 0;
    if (maxEmployeePen > 0)
      newPenalty = SoftConstraints.CalculatePenalty(this, maxEmployeePen, startDay, endDay, updateStructure); 
    this.Roster.EmployeesPenalty = this.Roster.EmployeesPenalty - originalPenalty + newPenalty;
    this.Penalty = newPenalty;
  }
  
  public void RecalculatePenalty(int maxPenalty) {
    for (int i = 0; i < this.EmployeeDescription.VerticallyRelatedEmployeesCount; i++) {
      String employee2ID = (this.EmployeeDescription.GetVerticallyRelatedEmployee(i)).ID;
      Employee employee2 = this.Roster.GetEmployee(employee2ID);
      if (employee2 != null) {
        int j = employee2.Penalty;
        int k = SoftConstraints.CalculatePenalty(employee2);
        this.Roster.EmployeesPenalty = this.Roster.EmployeesPenalty - j + k;
        employee2.Penalty = k;
      } 
    } 
    int originalPenalty = this.Penalty;
    int maxEmployeePen = maxPenalty - this.Roster.getTotalPenalty() - originalPenalty;
    int newPenalty = 0;
    if (maxEmployeePen > 0)
      newPenalty = SoftConstraints.CalculatePenalty(this, maxEmployeePen); 
    this.Roster.EmployeesPenalty = this.Roster.EmployeesPenalty - originalPenalty + newPenalty;
    this.Penalty = newPenalty;
  }
  
  public int ViolationsForAssigningShift(Shift shift) {
    int violationCount = 0;
    if (shift == null)
      return 0; 
    if (shift.Employee != null)
      return -1; 
    int rosterDay = shift.RosterDay;
    int shiftAssignmentIndex = rosterDay * this.Roster.SchedulingPeriod.ShiftTypesCount + shift.ShiftType.Index;
    if (this.ShiftAssignments[shiftAssignmentIndex])
      return -1; 
    return violationCount;
  }
  
  public void UnAssignAllShifts() {
    for (int day = 0; day < this.Roster.SchedulingPeriod.NumDaysInPeriod; day++) {
      for (int j = 0; j < this.Roster.SchedulingPeriod.ShiftTypesCount; j++) {
        if (this.ShiftsOnDay[day][j] != null)
          this.Roster.UnAssignShift(this.ShiftsOnDay[day][j]); 
      } 
    } 
  }
  
  public int UnAssignAllShifts(int day) {
    int count = 0;
    for (int j = 0; j < this.Roster.SchedulingPeriod.ShiftTypesCount; j++) {
      if (this.ShiftsOnDay[day][j] != null)
        if (this.Roster.UnAssignShift(this.ShiftsOnDay[day][j]))
          count++;  
    } 
    return count;
  }
}
