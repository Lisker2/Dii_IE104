package org.example.util.ASAP.NRP.Core;

public class CoverProvided {
  public int[][] ProvidedCoverPerShift;
  
  public int[][] CoverPerShiftPenalty;
  
  public int[][] ProvidedCoverPerShiftGroup;
  
  public int[][] CoverPerShiftGroupPenalty;
  
  public int[][] ProvidedCoverPerPeriod;
  
  public int[][] CoverPerPeriodPenalty;
  
  public int Index;
  
  public CoverProvided(SchedulingPeriod schedulingPeriod) {
    this.ProvidedCoverPerShift = new int[schedulingPeriod.ShiftTypesCount][schedulingPeriod.NumDaysInPeriod];
    this.CoverPerShiftPenalty = new int[schedulingPeriod.ShiftTypesCount][schedulingPeriod.NumDaysInPeriod];
    this.ProvidedCoverPerShiftGroup = new int[schedulingPeriod.ShiftGroupsCount][schedulingPeriod.NumDaysInPeriod];
    this.CoverPerShiftGroupPenalty = new int[schedulingPeriod.ShiftGroupsCount][schedulingPeriod.NumDaysInPeriod];
    this.ProvidedCoverPerPeriod = new int[schedulingPeriod.getDayPeriodsCount()][schedulingPeriod.NumDaysInPeriod];
    this.CoverPerPeriodPenalty = new int[schedulingPeriod.getDayPeriodsCount()][schedulingPeriod.NumDaysInPeriod];
  }
}
