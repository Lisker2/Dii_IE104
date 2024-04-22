package org.example.util.PersonnelScheduling;

import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.Shift;
import org.example.util.ASAP.NRP.Core.ShiftType;
import org.example.util.ASAP.NRP.Solvers.VariableDepthSearch.TestShiftDB;

class TestShiftDB2 implements TestShiftDB {
  Shift[][] TestShifts;
  
  Shift[][][] AllShifts;
  
  public boolean SchedulingPeriodContainsNonAutoAssignShifts = false;
  
  public boolean[] EmployeeDescriptionHasFrozenDays;
  
  public TestShiftDB2(Roster roster) {
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
      for (int j = 0; j < emp.FrozenDay.length; j++) {
        if (emp.FrozenDay[j]) {
          this.EmployeeDescriptionHasFrozenDays[emp.IndexID] = true;
          break;
        } 
      } 
    } 
    SchedulingPeriod schedulingPeriod = roster.SchedulingPeriod;
    this.AllShifts = new Shift[schedulingPeriod.NumDaysInPeriod][schedulingPeriod.ShiftTypesCount][roster.Employees.length];
    this.TestShifts = new Shift[schedulingPeriod.NumDaysInPeriod][schedulingPeriod.ShiftTypesCount];
    for (int day = 0; day < schedulingPeriod.NumDaysInPeriod; day++) {
      DateTime date = schedulingPeriod.ConvertRosterDayToDate(day);
      for (int j = 0; j < schedulingPeriod.ShiftTypesCount; j++) {
        ShiftType st = schedulingPeriod.GetShiftType(j);
        this.TestShifts[day][j] = new Shift(st, date, schedulingPeriod);
        for (int k = 0; k < roster.Employees.length; k++)
          this.AllShifts[day][j][k] = new Shift(st, date, schedulingPeriod); 
      } 
    } 
  }
  
  public Shift GetTestShift(int stIndex, int day) {
    return this.TestShifts[day][stIndex];
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
  
  public boolean getSchedulingPeriodContainsNonAutoAssignShifts() {
    return this.SchedulingPeriodContainsNonAutoAssignShifts;
  }
  
  public boolean[] getEmployeeDescriptionHasFrozenDays() {
    return this.EmployeeDescriptionHasFrozenDays;
  }
}
