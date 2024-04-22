package org.example.util.ASAP.NRP.Core;

public class SchedulingHistory {
  public boolean Exists;
  
  public boolean[] PreviousShiftAssignments;
  
  public int[] PreviousDayType;
  
  public boolean[] PreviousDayOffRequestWasWork;
  
  public int LastDayType;
  
  public boolean[] LastDayAssignments;
  
  public int PreviousConsecutiveWorkingDays;
  
  public int PreviousConsecutiveWorkingDaysAndHoliday;
  
  public int PreviousConsecutiveFreeDays;
  
  public int PreviousConsecutiveFreeDaysAndHoliday;
  
  public boolean PreviousConsecutiveFreeDaysAndHolidayIncludesAtLeastOneNonWorkingDay;
  
  public int PreviousConsecutiveWorkingWeekends;
  
  public boolean WeekendWorkedThreeWeeksAgo;
  
  public boolean WeekendWorkedTwoWeeksAgo;
  
  public boolean WeekendWorkedOneWeekAgo;
  
  public boolean PreviousSaturdayWorked;
  
  public boolean PreviousSundayWorked;
  
  public boolean PreviousSaturdayRequestedHoliday;
  
  public boolean PreviousSundayRequestedHoliday;
  
  public boolean NightShiftThursday;
  
  public boolean NightShiftFriday;
  
  public boolean PreviousFridayWorked;
  
  public boolean PreviousNightShift;
  
  public int PreviousFreeDaysAfterNightShift;
  
  public int PreviousConsecutiveHolidayDaysOff;
  
  public int[] PreviousConsecutiveShiftGroups;
  
  public int[] PreviousConsecutiveShifts;
  
  public int[] DaysSinceShiftType;
  
  public int DaysOfHistoryProvided;
  
  public int PreviousWorkingBankHolidays;
  
  public double PreviousOvertime;
  
  public SchedulingHistory(SchedulingPeriod schedulingPeriod) {
    this.Exists = false;
    this.PreviousShiftAssignments = new boolean[0];
    this.PreviousDayType = new int[0];
    this.PreviousDayOffRequestWasWork = new boolean[0];
    this.LastDayType = 0;
    this.PreviousConsecutiveWorkingDays = 0;
    this.PreviousConsecutiveWorkingDaysAndHoliday = 0;
    this.PreviousConsecutiveFreeDays = 0;
    this.PreviousConsecutiveFreeDaysAndHoliday = 0;
    this.PreviousConsecutiveFreeDaysAndHolidayIncludesAtLeastOneNonWorkingDay = false;
    this.PreviousConsecutiveWorkingWeekends = 0;
    this.WeekendWorkedThreeWeeksAgo = false;
    this.WeekendWorkedTwoWeeksAgo = false;
    this.WeekendWorkedOneWeekAgo = false;
    this.PreviousSaturdayWorked = false;
    this.PreviousSundayWorked = false;
    this.PreviousSaturdayRequestedHoliday = false;
    this.PreviousSundayRequestedHoliday = false;
    this.NightShiftThursday = false;
    this.NightShiftFriday = false;
    this.PreviousFridayWorked = false;
    this.PreviousNightShift = false;
    this.PreviousFreeDaysAfterNightShift = 0;
    this.PreviousConsecutiveHolidayDaysOff = 0;
    this.DaysOfHistoryProvided = 0;
    this.PreviousWorkingBankHolidays = 0;
    this.PreviousOvertime = 0.0D;
    this.LastDayAssignments = new boolean[schedulingPeriod.ShiftTypesCount];
    this.PreviousConsecutiveShifts = new int[schedulingPeriod.ShiftTypesCount];
    this.PreviousConsecutiveShiftGroups = new int[schedulingPeriod.ShiftGroupsCount];
    this.DaysSinceShiftType = new int[schedulingPeriod.ShiftTypesCount];
    for (int i = 0; i < schedulingPeriod.ShiftTypesCount; i++)
      this.DaysSinceShiftType[i] = -1; 
  }
  
  public void UpdatePreviousDayType(int daysCount, int shiftTypesCount) {
    this.PreviousDayType = new int[daysCount];
    try {
      for (int i = 0; i < daysCount; i++) {
        boolean dayWorked = this.PreviousDayOffRequestWasWork[i];
        if (!dayWorked)
          for (int j = 0; j < shiftTypesCount; j++) {
            if (this.PreviousShiftAssignments[i * shiftTypesCount + j]) {
              dayWorked = true;
              break;
            } 
          }  
        if (dayWorked) {
          this.PreviousDayType[i] = 1;
        } else {
          this.PreviousDayType[i] = 0;
        } 
      } 
    } catch (RuntimeException e) {
      e.printStackTrace();
    } 
  }
}
