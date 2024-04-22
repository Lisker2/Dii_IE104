package org.example.util.ASAP.NRP.Core;

import org.example.util.ASAP.NRP.Core.Constraints.TEC.Pattern;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.PatternGroup;
import java.util.ArrayList;

public class Contract implements Cloneable {
  public enum WeekendDefinitions {
    SaturdaySunday, FridaySaturdaySunday, FridaySaturdaySundayMonday, SaturdaySundayMonday;
  }
  
  public String ContractID = "";
  
  public String Label = "";
  
  SchedulingPeriod schedulingPeriod;
  
  public int EmployeeCount = 0;
  
  public int MaxNumAssignments = 0;
  
  public boolean MaxNumAssignmentsIsOn = false;
  
  public int MaxNumAssignmentsWeight = -1;
  
  public int MinNumAssignments = 0;
  
  public boolean MinNumAssignmentsIsOn = false;
  
  public int MinNumAssignmentsWeight = -1;
  
  public int MaxDaysOff = 0;
  
  public boolean MaxDaysOffIsOn = false;
  
  public int MaxDaysOffWeight = -1;
  
  public int MinDaysOff = 0;
  
  public boolean MinDaysOffIsOn = false;
  
  public int MinDaysOffWeight = -1;
  
  public int MaxConsecutiveWorkingDays = 0;
  
  public boolean MaxConsecutiveWorkingDaysIsOn = false;
  
  public int MaxConsecutiveWorkingDaysWeight = -1;
  
  public int MinConsecutiveWorkingDays = 0;
  
  public boolean MinConsecutiveWorkingDaysIsOn = false;
  
  public int MinConsecutiveWorkingDaysWeight = -1;
  
  public int MaxWorkingBankHolidays = 0;
  
  public boolean MaxWorkingBankHolidaysIsOn = false;
  
  public int MaxWorkingBankHolidaysWeight = -1;
  
  public int MaxConsecutiveFreeDays = 0;
  
  public boolean MaxConsecutiveFreeDaysIsOn = false;
  
  public int MaxConsecutiveFreeDaysWeight = -1;
  
  public int MinConsecutiveFreeDays = 0;
  
  public boolean MinConsecutiveFreeDaysIsOn = false;
  
  public int MinConsecutiveFreeDaysWeight = -1;
  
  public int MaxConsecutiveWorkingWeekends = 0;
  
  public boolean MaxConsecutiveWorkingWeekendsIsOn = false;
  
  public int MaxConsecutiveWorkingWeekendsWeight = -1;
  
  public int MinConsecutiveWorkingWeekends = 0;
  
  public boolean MinConsecutiveWorkingWeekendsIsOn = false;
  
  public int MinConsecutiveWorkingWeekendsWeight = -1;
  
  public int MaxWorkingWeekendsInFourWeeks = 0;
  
  public boolean MaxWorkingWeekendsInFourWeeksIsOn = false;
  
  public int MaxWorkingWeekendsInFourWeeksWeight = -1;
  
  public int MaxWorkingWeekendsIncFriNight = 0;
  
  public boolean MaxWorkingWeekendsIncFriNightIsOn = false;
  
  public int MaxWorkingWeekendsIncFriNightWeight = -1;
  
  public int MaxWorkingWeekends = 0;
  
  public boolean MaxWorkingWeekendsIsOn = false;
  
  public int MaxWorkingWeekendsWeight = -1;
  
  public int MaxWeekendsOff = 0;
  
  public boolean MaxWeekendsOffIsOn = false;
  
  public int MaxWeekendsOffWeight = -1;
  
  public int MaxConsecutiveFreeWeekends = 0;
  
  public boolean MaxConsecutiveFreeWeekendsIsOn = false;
  
  public int MaxConsecutiveFreeWeekendsWeight = -1;
  
  public int MinConsecutiveFreeWeekends = 0;
  
  public boolean MinConsecutiveFreeWeekendsIsOn = false;
  
  public int MinConsecutiveFreeWeekendsWeight = -1;
  
  public int MaxWorkingDaysPerWeek = 0;
  
  public boolean MaxWorkingDaysPerWeekIsOn = false;
  
  public int MaxWorkingDaysPerWeekWeight = -1;
  
  public int MaxShiftsPerWeekStartMon = 0;
  
  public boolean MaxShiftsPerWeekStartMonIsOn = false;
  
  public int MaxShiftsPerWeekStartMonWeight = -1;
  
  public int MinShiftsPerWeekStartMon = 0;
  
  public boolean MinShiftsPerWeekStartMonIsOn = false;
  
  public int MinShiftsPerWeekStartMonWeight = -1;
  
  public int MaxWeekendDays = 0;
  
  public boolean MaxWeekendDaysIsOn = false;
  
  public int MaxWeekendDaysWeight = -1;
  
  public WeekendDefinitions WeekendDefinition = WeekendDefinitions.SaturdaySunday;
  
  public boolean CompleteWeekends = false;
  
  public int CompleteWeekendsWeight = -1;
  
  public boolean BrokenWeekends = false;
  
  public int BrokenWeekendsWeight = -1;
  
  public boolean IdenticalShiftTypesDuringWeekend = false;
  
  public int IdenticalShiftTypesDuringWeekendWeight = -1;
  
  public boolean NoNightShiftBeforeFreeWeekend = false;
  
  public int NoNightShiftBeforeFreeWeekendWeight = -1;
  
  public boolean TwoFreeDaysAfterNightShifts = false;
  
  public int TwoFreeDaysAfterNightShiftsWeight = -1;
  
  public boolean AlternativeSkillCategory = false;
  
  public int AlternativeSkillCategoryWeight = -1;
  
  public boolean ValidNumConsecutiveShiftTypesIsOn = false;
  
  public int ValidNumConsecutiveShiftTypesWeight = -1;
  
  public boolean ValidNumConsecutiveShiftGroupsIsOn = false;
  
  public int ValidNumConsecutiveShiftGroupsWeight = -1;
  
  public boolean MaxShiftTypesUsed = false;
  
  public boolean MaxShiftGroupsUsed = false;
  
  public boolean MaxShiftTypesIsOn = false;
  
  public int MaxShiftTypesWeight = -1;
  
  public boolean MinShiftTypesUsed = false;
  
  public boolean MinShiftGroupsUsed = false;
  
  public boolean MinShiftTypesIsOn = false;
  
  public int MinShiftTypesWeight = -1;
  
  public boolean MaxShiftTypesPerWeekIsOn = false;
  
  public int MaxShiftTypesPerWeekWeight = -1;
  
  public boolean ValidShiftTypeSuccessionsIsOn = false;
  
  public int ValidShiftTypeSuccessionsWeight = -1;
  
  public boolean MaxAssignmentsForDayOfWeekIsOn = false;
  
  public int MaxAssignmentsForDayOfWeekWeight = -1;
  
  public int MaxAssignmentsForAllMondays = 0;
  
  public boolean MaxAssignmentsForAllMondaysIsOn = false;
  
  public int MaxAssignmentsForAllTuesdays = 0;
  
  public boolean MaxAssignmentsForAllTuesdaysIsOn = false;
  
  public int MaxAssignmentsForAllWednesdays = 0;
  
  public boolean MaxAssignmentsForAllWednesdaysIsOn = false;
  
  public int MaxAssignmentsForAllThursdays = 0;
  
  public boolean MaxAssignmentsForAllThursdaysIsOn = false;
  
  public int MaxAssignmentsForAllFridays = 0;
  
  public boolean MaxAssignmentsForAllFridaysIsOn = false;
  
  public int MaxAssignmentsForAllSaturdays = 0;
  
  public boolean MaxAssignmentsForAllSaturdaysIsOn = false;
  
  public int MaxAssignmentsForAllSundays = 0;
  
  public boolean MaxAssignmentsForAllSundaysIsOn = false;
  
  public double MaxHoursWorked = 0.0D;
  
  public boolean MaxHoursWorkedIsOn = false;
  
  public double MaxHoursWorkedThreshold = 0.0D;
  
  public int MaxHoursWorkedWeight = -1;
  
  public double MinHoursWorked = 0.0D;
  
  public boolean MinHoursWorkedIsOn = false;
  
  public double MinHoursWorkedThreshold = 0.0D;
  
  public int MinHoursWorkedWeight = -1;
  
  public double MaxHoursPerWeek = 0.0D;
  
  public boolean MaxHoursPerWeekIsOn = false;
  
  public int MaxHoursPerWeekWeight = -1;
  
  public double MaxHoursPerFortnight = 0.0D;
  
  public boolean MaxHoursPerFortnightIsOn = false;
  
  public int MaxHoursPerFortnightWeight = -1;
  
  public boolean BadPatternsIsOn = false;
  
  public boolean GoodPatternsIsOn = false;
  
  public int MinShiftsPerWeek = 0;
  
  public boolean MinShiftsPerWeekIsOn = false;
  
  public int MinShiftsPerWeekWeight = -1;
  
  public int MaxShiftsPerWeek = 0;
  
  public boolean MaxShiftsPerWeekIsOn = false;
  
  public int MaxShiftsPerWeekWeight = -1;
  
  public boolean MinConsecutiveShiftTypesIsOn = false;
  
  public int MinConsecutiveShiftTypesWeight = -1;
  
  public boolean MaxConsecutiveShiftTypesIsOn = false;
  
  public int MaxConsecutiveShiftTypesWeight = -1;
  
  public boolean MinDaysBetweenShiftSeriesIsOn = false;
  
  public int MinDaysBetweenShiftSeriesWeight = -1;
  
  public boolean MaxDaysBetweenShiftSeriesIsOn = false;
  
  public int MaxDaysBetweenShiftSeriesWeight = -1;
  
  public boolean MinConsecutiveGeneralShiftTypesIsOn = false;
  
  public int MinConsecutiveGeneralShiftTypesWeight = -1;
  
  public boolean MaxConsecutiveGeneralShiftTypesIsOn = false;
  
  public int MaxConsecutiveGeneralShiftTypesWeight = -1;
  
  public boolean MaxShiftTypeRatiosIsOn = false;
  
  public int MaxShiftTypeRatiosWeight = -1;
  
  public boolean MinShiftTypeRatiosIsOn = false;
  
  public int MinShiftTypeRatiosWeight = -1;
  
  public boolean MaxGeneralShiftTypeRatiosIsOn = false;
  
  public int MaxGeneralShiftTypeRatiosWeight = -1;
  
  public boolean MinGeneralShiftTypeRatiosIsOn = false;
  
  public int MinGeneralShiftTypeRatiosWeight = -1;
  
  public int MaxShiftsPerDay = 0;
  
  public boolean MaxShiftsPerDayIsOn = false;
  
  public int MaxShiftsPerDayWeight = -1;
  
  public boolean MinHoursWorkedBetweenDatesIsOn = false;
  
  public int MinHoursWorkedBetweenDatesWeight = -1;
  
  public boolean MaxHoursWorkedBetweenDatesIsOn = false;
  
  public int MaxHoursWorkedBetweenDatesWeight = -1;
  
  public double StandardPerformance = 0.0D;
  
  public boolean[][] ValidNumConsecutiveShiftTypes;
  
  public boolean[][] ValidNumConsecutiveShiftGroups;
  
  public int[] MaxShiftTypes;
  
  public int[] MaxShiftGroups;
  
  public int[] MinShiftTypes;
  
  public int[] MinShiftGroups;
  
  public int[] MaxConsecutiveShiftTypes;
  
  public int[] MinConsecutiveShiftTypes;
  
  public int[] MaxConsecutiveShiftGroups;
  
  public int[] MinConsecutiveShiftGroups;
  
  public int[] MaxDaysBetweenShiftSeries;
  
  public int[] MinDaysBetweenShiftSeries;
  
  public int[] MaxShiftTypeRatios;
  
  public int[] MinShiftTypeRatios;
  
  public int[] MaxShiftGroupRatios;
  
  public int[] MinShiftGroupRatios;
  
  public int[][] MaxShiftTypesPerWeek;
  
  public boolean MaxShiftTypeRatiosUsed = false;
  
  public boolean MinShiftTypeRatiosUsed = false;
  
  public boolean MaxShiftGroupRatiosUsed = false;
  
  public boolean MinShiftGroupRatiosUsed = false;
  
  public boolean MinConsecutiveShiftTypesUsed = false;
  
  public boolean MinConsecutiveShiftGroupsUsed = false;
  
  public boolean MaxConsecutiveShiftTypesUsed = false;
  
  public boolean MaxConsecutiveShiftGroupsUsed = false;
  
  public boolean[][] ShiftTypeSuccessions;
  
  public Pattern[] BadPatterns = new Pattern[0];
  
  public Pattern[] WeekDayBadPatterns = new Pattern[0];
  
  public Pattern[] DateBadPatterns = new Pattern[0];
  
  public PatternGroup[] GoodPatterns = new PatternGroup[0];
  
  public HoursBetweenDates[] MinHoursWorkedBetweenDates = new HoursBetweenDates[0];
  
  public HoursBetweenDates[] MaxHoursWorkedBetweenDates = new HoursBetweenDates[0];
  
  public ArrayList<ShiftTypeAndValue> MaxShiftTypesAL = new ArrayList<ShiftTypeAndValue>();
  
  public ArrayList<ShiftTypeAndValue> MinShiftTypesAL = new ArrayList<ShiftTypeAndValue>();
  
  public ArrayList<ShiftTypeAndValue> MinConsecutiveShiftTypesAL = new ArrayList<ShiftTypeAndValue>();
  
  public Contract(String contractID, SchedulingPeriod schedulingPeriod) {
    this.ContractID = contractID;
    this.schedulingPeriod = schedulingPeriod;
    int shiftTypesCount = schedulingPeriod.ShiftTypesCount;
    this.ValidNumConsecutiveShiftTypes = new boolean[schedulingPeriod.ShiftTypesCount][12];
    this.ValidNumConsecutiveShiftGroups = new boolean[schedulingPeriod.ShiftGroupsCount][12];
    this.MaxShiftTypes = new int[shiftTypesCount];
    int i;
    for (i = 0; i < this.MaxShiftTypes.length; i++)
      this.MaxShiftTypes[i] = -1; 
    this.MaxShiftGroups = new int[this.schedulingPeriod.ShiftGroupsCount];
    for (i = 0; i < this.MaxShiftGroups.length; i++)
      this.MaxShiftGroups[i] = -1; 
    this.MinShiftTypes = new int[shiftTypesCount];
    for (i = 0; i < this.MinShiftTypes.length; i++)
      this.MinShiftTypes[i] = -1; 
    this.MinShiftGroups = new int[this.schedulingPeriod.ShiftGroupsCount];
    for (i = 0; i < this.MinShiftGroups.length; i++)
      this.MinShiftGroups[i] = -1; 
    this.MaxConsecutiveShiftTypes = new int[shiftTypesCount];
    for (i = 0; i < this.MaxConsecutiveShiftTypes.length; i++)
      this.MaxConsecutiveShiftTypes[i] = -1; 
    this.MinConsecutiveShiftTypes = new int[shiftTypesCount];
    for (i = 0; i < this.MinConsecutiveShiftTypes.length; i++)
      this.MinConsecutiveShiftTypes[i] = -1; 
    this.MaxConsecutiveShiftGroups = new int[schedulingPeriod.ShiftGroupsCount];
    for (i = 0; i < this.MaxConsecutiveShiftGroups.length; i++)
      this.MaxConsecutiveShiftGroups[i] = -1; 
    this.MinConsecutiveShiftGroups = new int[schedulingPeriod.ShiftGroupsCount];
    for (i = 0; i < this.MinConsecutiveShiftGroups.length; i++)
      this.MinConsecutiveShiftGroups[i] = -1; 
    this.MaxDaysBetweenShiftSeries = new int[shiftTypesCount];
    for (i = 0; i < this.MaxDaysBetweenShiftSeries.length; i++)
      this.MaxDaysBetweenShiftSeries[i] = -1; 
    this.MinDaysBetweenShiftSeries = new int[shiftTypesCount];
    for (i = 0; i < this.MinDaysBetweenShiftSeries.length; i++)
      this.MinDaysBetweenShiftSeries[i] = -1; 
    this.MaxShiftTypeRatios = new int[schedulingPeriod.ShiftTypesCount];
    for (i = 0; i < this.MaxShiftTypeRatios.length; i++)
      this.MaxShiftTypeRatios[i] = -1; 
    this.MinShiftTypeRatios = new int[schedulingPeriod.ShiftTypesCount];
    for (i = 0; i < this.MinShiftTypeRatios.length; i++)
      this.MinShiftTypeRatios[i] = -1; 
    this.MaxShiftGroupRatios = new int[schedulingPeriod.ShiftGroupsCount];
    for (i = 0; i < this.MaxShiftGroupRatios.length; i++)
      this.MaxShiftGroupRatios[i] = -1; 
    this.MinShiftGroupRatios = new int[schedulingPeriod.ShiftGroupsCount];
    for (i = 0; i < this.MinShiftGroupRatios.length; i++)
      this.MinShiftGroupRatios[i] = -1; 
    this.MaxShiftTypesPerWeek = new int[schedulingPeriod.NumWeeksInPeriod][shiftTypesCount];
    for (i = 0; i < this.MaxShiftTypesPerWeek.length; i++) {
      for (int j = 0; j < (this.MaxShiftTypesPerWeek[i]).length; j++)
        this.MaxShiftTypesPerWeek[i][j] = -1; 
    } 
    this.ShiftTypeSuccessions = new boolean[shiftTypesCount + 1][shiftTypesCount + 1];
    for (i = 0; i < this.ShiftTypeSuccessions.length; i++) {
      for (int j = 0; j < (this.ShiftTypeSuccessions[i]).length; j++)
        this.ShiftTypeSuccessions[i][j] = false; 
    } 
  }
  
  public void SetWeekendDefinition(String definition) {
    if (definition.equalsIgnoreCase("SaturdaySunday")) {
      this.WeekendDefinition = WeekendDefinitions.SaturdaySunday;
    } else if (definition.equalsIgnoreCase("FridaySaturdaySunday")) {
      this.WeekendDefinition = WeekendDefinitions.FridaySaturdaySunday;
    } else if (definition.equalsIgnoreCase("FridaySaturdaySundayMonday")) {
      this.WeekendDefinition = WeekendDefinitions.FridaySaturdaySundayMonday;
    } else if (definition.equalsIgnoreCase("SaturdaySundayMonday")) {
      this.WeekendDefinition = WeekendDefinitions.SaturdaySundayMonday;
    } else {
      System.out.println("Warning: Unknown weekend definition: " + definition);
      this.WeekendDefinition = WeekendDefinitions.SaturdaySunday;
    } 
  }
  
  public void SetMaxShiftType(ShiftType shiftType, int value) {
    if (shiftType.Index < this.MaxShiftTypes.length)
      this.MaxShiftTypes[shiftType.Index] = value; 
    boolean found = false;
    for (ShiftTypeAndValue stv : this.MaxShiftTypesAL) {
      if (stv.ShiftType.ID.equals(shiftType.ID)) {
        stv.Value = value;
        found = true;
        break;
      } 
    } 
    if (!found)
      this.MaxShiftTypesAL.add(new ShiftTypeAndValue(shiftType, value)); 
    this.MaxShiftTypesUsed = false;
    for (int i = 0; i < this.MaxShiftTypes.length; i++) {
      if (this.MaxShiftTypes[i] >= 0) {
        this.MaxShiftTypesUsed = true;
        break;
      } 
    } 
  }
  
  public void SetMaxShiftGroup(ShiftGroup shiftGroup, int value) {
    this.MaxShiftGroups[shiftGroup.Index] = value;
    this.MaxShiftGroupsUsed = false;
    for (int i = 0; i < this.MaxShiftGroups.length; i++) {
      if (this.MaxShiftGroups[i] >= 0) {
        this.MaxShiftGroupsUsed = true;
        break;
      } 
    } 
  }
  
  public void SetMinShiftType(ShiftType shiftType, int value) {
    if (shiftType.Index < this.MinShiftTypes.length)
      this.MinShiftTypes[shiftType.Index] = value; 
    boolean found = false;
    for (ShiftTypeAndValue stv : this.MinShiftTypesAL) {
      if (stv.ShiftType.ID.equals(shiftType.ID)) {
        stv.Value = value;
        found = true;
        break;
      } 
    } 
    if (!found)
      this.MinShiftTypesAL.add(new ShiftTypeAndValue(shiftType, value)); 
    this.MinShiftTypesUsed = false;
    for (int i = 0; i < this.MinShiftTypes.length; i++) {
      if (this.MinShiftTypes[i] > 0) {
        this.MinShiftTypesUsed = true;
        break;
      } 
    } 
  }
  
  public void SetMinShiftGroup(ShiftGroup shiftGroup, int value) {
    this.MinShiftGroups[shiftGroup.Index] = value;
    this.MinShiftGroupsUsed = false;
    for (int i = 0; i < this.MinShiftGroups.length; i++) {
      if (this.MinShiftGroups[i] > 0) {
        this.MinShiftGroupsUsed = true;
        break;
      } 
    } 
  }
  
  public void SetMaxShiftTypeRatio(ShiftType shiftType, int value) {
    this.MaxShiftTypeRatios[shiftType.Index] = value;
    this.MaxShiftTypeRatiosUsed = true;
  }
  
  public void SetMinShiftTypeRatio(ShiftType shiftType, int value) {
    this.MinShiftTypeRatios[shiftType.Index] = value;
    this.MinShiftTypeRatiosUsed = true;
  }
  
  public void SetMaxShiftGroupRatio(ShiftGroup shiftGroup, int value) {
    this.MaxShiftGroupRatios[shiftGroup.Index] = value;
    this.MaxShiftGroupRatiosUsed = true;
  }
  
  public void SetMinShiftGroupRatio(ShiftGroup shiftGroup, int value) {
    this.MinShiftGroupRatios[shiftGroup.Index] = value;
    this.MinShiftGroupRatiosUsed = true;
  }
  
  public void SetMinDaysBetweenShiftSeries(ShiftType shiftType, int value) {
    this.MinDaysBetweenShiftSeries[shiftType.Index] = value;
  }
  
  public void SetMaxDaysBetweenShiftSeries(ShiftType shiftType, int value) {
    this.MaxDaysBetweenShiftSeries[shiftType.Index] = value;
  }
  
  public void SetMinConsecutiveShiftType(ShiftType shiftType, int value) {
    if (shiftType.Index < this.MinConsecutiveShiftTypes.length)
      this.MinConsecutiveShiftTypes[shiftType.Index] = value; 
    boolean found = false;
    for (ShiftTypeAndValue stv : this.MinConsecutiveShiftTypesAL) {
      if (stv.ShiftType.ID.equals(shiftType.ID)) {
        stv.Value = value;
        found = true;
        break;
      } 
    } 
    if (!found)
      this.MinConsecutiveShiftTypesAL.add(new ShiftTypeAndValue(shiftType, value)); 
    this.MinConsecutiveShiftTypesUsed = false;
    for (int i = 0; i < this.MinConsecutiveShiftTypes.length; i++) {
      if (this.MinConsecutiveShiftTypes[i] > 1) {
        this.MinConsecutiveShiftTypesUsed = true;
        break;
      } 
    } 
  }
  
  public void SetMaxConsecutiveShiftType(ShiftType shiftType, int value) {
    this.MaxConsecutiveShiftTypes[shiftType.Index] = value;
    this.MaxConsecutiveShiftTypesUsed = false;
    for (int i = 0; i < this.MaxConsecutiveShiftTypes.length; i++) {
      if (this.MaxConsecutiveShiftTypes[i] > 0) {
        this.MaxConsecutiveShiftTypesUsed = true;
        break;
      } 
    } 
  }
  
  public void SetMinConsecutiveShiftGroup(ShiftGroup shiftGroup, int value) {
    this.MinConsecutiveShiftGroups[shiftGroup.Index] = value;
    this.MinConsecutiveShiftGroupsUsed = false;
    for (int i = 0; i < this.MinConsecutiveShiftGroups.length; i++) {
      if (this.MinConsecutiveShiftGroups[i] > 1) {
        this.MinConsecutiveShiftGroupsUsed = true;
        break;
      } 
    } 
  }
  
  public void SetMaxConsecutiveShiftGroup(ShiftGroup shiftGroup, int value) {
    this.MaxConsecutiveShiftGroups[shiftGroup.Index] = value;
    this.MaxConsecutiveShiftGroupsUsed = false;
    for (int i = 0; i < this.MaxConsecutiveShiftGroups.length; i++) {
      if (this.MaxConsecutiveShiftGroups[i] > 0) {
        this.MaxConsecutiveShiftGroupsUsed = true;
        break;
      } 
    } 
  }
  
  public void SetMaxShiftTypePerWeek(ShiftType shiftType, int value, int week) {
    if (week > this.schedulingPeriod.NumWeeksInPeriod) {
      System.out.println("Warning: Unable to set MaxShiftTypePerWeek for ShiftType '" + shiftType.ID + "' as week value '" + week + "' is greater than the number of weeks in the period.");
      return;
    } 
    this.MaxShiftTypesPerWeek[week - 1][shiftType.Index] = value;
  }
  
  public void SetShiftTypeSuccession(String shiftTypeID1, String shiftTypeID2, boolean permissible) {
    int index1 = 0;
    int index2 = 0;
    if (!shiftTypeID1.equalsIgnoreCase("")) {
      ShiftType sh = this.schedulingPeriod.GetShiftType(shiftTypeID1);
      if (sh == null) {
        System.out.println("Warning. Unable to SetShiftTypeSuccession : No definition of ShiftType: " + shiftTypeID1 + " found.");
        return;
      } 
      index1 = sh.Index + 1;
    } 
    if (!shiftTypeID2.equalsIgnoreCase("")) {
      ShiftType sh = this.schedulingPeriod.GetShiftType(shiftTypeID2);
      if (sh == null) {
        System.out.println("Warning. Unable to SetShiftTypeSuccession : No definition of ShiftType: " + shiftTypeID2 + " found.");
        return;
      } 
      index2 = sh.Index + 1;
    } 
    this.ShiftTypeSuccessions[index1][index2] = permissible;
  }
  
  public boolean IsPermissibleSuccession(int shiftType1, int shiftType2) {
    return this.ShiftTypeSuccessions[shiftType1 + 1][shiftType2 + 1];
  }
  
  public boolean AddMinHoursWorkedBetweenDates(DateTime StartDate, DateTime EndDate, int hours) {
    if (StartDate.isGreaterThan(EndDate)) {
      System.out.println("Warning. Unable to add MinHoursWorkedBetweenDates request. StartDate " + StartDate + " is after EndDate " + EndDate);
      return false;
    } 
    int startDay = this.schedulingPeriod.ConvertDateToRosterDay(StartDate);
    int endDay = this.schedulingPeriod.ConvertDateToRosterDay(EndDate);
    if (startDay < 0 || startDay >= this.schedulingPeriod.NumDaysInPeriod) {
      System.out.println("Warning. Unable to add MinHoursWorkedBetweenDates request. StartDate " + StartDate + " out of scheduling range.");
      return false;
    } 
    if (endDay < 0 || endDay >= this.schedulingPeriod.NumDaysInPeriod) {
      System.out.println("Warning. Unable to add MinHoursWorkedBetweenDates request. EndDate " + EndDate + " out of scheduling range.");
      return false;
    } 
    HoursBetweenDates hbd = new HoursBetweenDates(startDay, endDay, hours);
    this.MinHoursWorkedBetweenDates = 
      CSharpConversionHelper.ArrayResize(this.MinHoursWorkedBetweenDates, this.MinHoursWorkedBetweenDates.length + 1);
    this.MinHoursWorkedBetweenDates[this.MinHoursWorkedBetweenDates.length - 1] = hbd;
    return true;
  }
  
  public boolean AddMaxHoursWorkedBetweenDates(DateTime StartDate, DateTime EndDate, int hours) {
    if (StartDate.isGreaterThan(EndDate)) {
      System.out.println("Warning. Unable to add MaxHoursWorkedBetweenDates request. StartDate " + StartDate + " is after EndDate " + EndDate);
      return false;
    } 
    int startDay = this.schedulingPeriod.ConvertDateToRosterDay(StartDate);
    int endDay = this.schedulingPeriod.ConvertDateToRosterDay(EndDate);
    if (startDay < 0 || startDay >= this.schedulingPeriod.NumDaysInPeriod) {
      System.out.println("Warning. Unable to add MaxHoursWorkedBetweenDates request. StartDate " + StartDate + " out of scheduling range.");
      return false;
    } 
    if (endDay < 0 || endDay >= this.schedulingPeriod.NumDaysInPeriod) {
      System.out.println("Warning. Unable to add MaxHoursWorkedBetweenDates request. EndDate " + EndDate + " out of scheduling range.");
      return false;
    } 
    HoursBetweenDates hbd = new HoursBetweenDates(startDay, endDay, hours);
    this.MaxHoursWorkedBetweenDates = 
      CSharpConversionHelper.ArrayResize(this.MaxHoursWorkedBetweenDates, this.MaxHoursWorkedBetweenDates.length + 1);
    this.MaxHoursWorkedBetweenDates[this.MaxHoursWorkedBetweenDates.length - 1] = hbd;
    return true;
  }
  
  public void AddBadPattern(Pattern pattern) {
    if (pattern.StartDayType == Pattern.StartType.Day) {
      this.WeekDayBadPatterns = 
        CSharpConversionHelper.ArrayResize(this.WeekDayBadPatterns, this.WeekDayBadPatterns.length + 1);
      this.WeekDayBadPatterns[this.WeekDayBadPatterns.length - 1] = pattern;
    } else if (pattern.StartDayType == Pattern.StartType.Date) {
      this.DateBadPatterns = 
        CSharpConversionHelper.ArrayResize(this.DateBadPatterns, this.DateBadPatterns.length + 1);
      this.DateBadPatterns[this.DateBadPatterns.length - 1] = pattern;
    } 
    try {
      this.BadPatterns = 
        CSharpConversionHelper.ArrayResize(this.BadPatterns, this.BadPatterns.length + 1);
    } catch (Exception ex) {
      System.out.print(ex.getMessage());
    } 
    this.BadPatterns[this.BadPatterns.length - 1] = pattern;
  }
  
  public void AddGoodPattern(PatternGroup patternGroup) {
    this.GoodPatterns = CSharpConversionHelper.ArrayResize(this.GoodPatterns, this.GoodPatterns.length + 1);
    this.GoodPatterns[this.GoodPatterns.length - 1] = patternGroup;
  }
  
  public String toString() {
    return this.Label;
  }
}
