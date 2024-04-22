package org.example.util.ASAP.NRP.Core;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Tools.RosterPrinter;
import java.io.StringWriter;
import java.util.Hashtable;

public class Roster implements Cloneable, Comparable<Object> {
  public static final int NON_WORKING_DAY = 0;
  
  public static final int WORKING_DAY = 1;
  
  public static final int HOLIDAY_DAY_OFF = 2;
  
  public CoverProvided[] CoverProvided;
  
  private Hashtable<String, Employee> EmployeeHash = new Hashtable<String, Employee>();
  
  public Employee[] Employees;
  
  public int[] CoverPenOnDay;
  
  public final int CacheSize = 500;
  
  private int[] EmployeesPenaltyCache = new int[500];
  
  public SchedulingPeriod SchedulingPeriod;
  
  public int EmployeesPenalty;
  
  public int CoverPenalty;
  
  public Roster(SchedulingPeriod schedulingPeriod) {
    this.SchedulingPeriod = schedulingPeriod;
    this.CoverPenOnDay = new int[schedulingPeriod.NumDaysInPeriod];
    this.CoverProvided = new CoverProvided[schedulingPeriod.CoverRequirements.Requirements.length];
    for (int k = 0; k < this.CoverProvided.length; k++) {
      this.CoverProvided[k] = new CoverProvided(schedulingPeriod);
      (this.CoverProvided[k]).Index = k;
    } 
    int employeesCount = 0;
    for (int i = 0; i < schedulingPeriod.EmployeesCount; i++) {
      if ((schedulingPeriod.GetEmployeeDescription(i)).InRoster)
        employeesCount++; 
    } 
    this.Employees = new Employee[employeesCount];
    int ind = 0;
    for (int j = 0; j < schedulingPeriod.EmployeesCount; j++) {
      EmployeeDescription employeeDescription = schedulingPeriod.GetEmployeeDescription(j);
      if (employeeDescription.InRoster) {
        Employee employee = new Employee(this, employeeDescription);
        this.EmployeeHash.put(employeeDescription.ID, employee);
        this.Employees[ind] = employee;
        employee.Index = ind;
        ind++;
      } 
    } 
    MakeInitialAssignments();
    RecalculateAllPenalties();
  }
  
  public Object Clone() {
    Roster roster = new Roster(this.SchedulingPeriod);
    for (int i = 0; i < this.Employees.length; i++) {
      Employee newEmployee = roster.GetEmployee((this.Employees[i]).EmployeeDescription.ID);
      for (int day = 0; day < this.SchedulingPeriod.NumDaysInPeriod; day++) {
        for (int j = 0; j < this.SchedulingPeriod.ShiftTypesCount; j++) {
          if ((this.Employees[i]).ShiftsOnDay[day][j] != null) {
            Shift newShift = (Shift)(this.Employees[i]).ShiftsOnDay[day][j].Clone();
            int violationCount = newEmployee.ViolationsForAssigningShift(newShift);
            if (violationCount != -1)
              roster.AssignShift(newEmployee, newShift); 
          } 
        } 
      } 
    } 
    roster.RecalculateAllPenalties();
    return roster;
  }
  
  public int compareTo(Object obj) {
    int p1 = getTotalPenalty();
    int p2 = ((Roster)obj).getTotalPenalty();
    int diff = p1 - p2;
    if (diff > 0)
      return 1; 
    if (diff < 0)
      return -1; 
    return 0;
  }
  
  public int getTotalPenalty() {
    return this.CoverPenalty + this.EmployeesPenalty;
  }
  
  public boolean AssignShift(Employee employee, Shift shift) {
    if (shift == null)
      return false; 
    if (shift.isAssigned())
      return false; 
    if (employee == null)
      return false; 
    shift.Employee = employee;
    int rosterDay = shift.RosterDay;
    int shiftIndex = shift.ShiftType.Index;
    employee.ShiftsCount++;
    employee.ShiftsOnDay[rosterDay][shiftIndex] = shift;
    if (employee.DayType[rosterDay] == 2)
      employee.HolidayDaysOffGranted--; 
    employee.DayType[rosterDay] = 1;
    int shiftAssignmentIndex = rosterDay * this.SchedulingPeriod.ShiftTypesCount + shift.ShiftType.Index;
    employee.ShiftAssignments[shiftAssignmentIndex] = true;
    if (shift.ShiftType.getSpansMidnight())
      employee.NightShifts[rosterDay] = true; 
    if (employee.ShiftCountPerDay[rosterDay] == 0 && 
      !employee.EmployeeDescription.DayOffRequestIsWork[rosterDay])
      employee.DaysOffCount--; 
    employee.ShiftCountPerDay[rosterDay] = employee.ShiftCountPerDay[rosterDay] + 1;
    employee.ShiftTypeCount[shiftIndex] = employee.ShiftTypeCount[shiftIndex] + 1;
    for (int i = 0; i < shift.ShiftType.ShiftGroupCount; i++) {
      ShiftGroup grp = shift.ShiftType.ShiftGroups[i];
      employee.ShiftGroupCount[grp.Index] = employee.ShiftGroupCount[grp.Index] + 1;
      employee.ShiftGroupPerDayCount[rosterDay][grp.Index] = employee.ShiftGroupPerDayCount[rosterDay][grp.Index] + 1;
    } 
    int week = shift.WeekNumber;
    employee.ShiftTypePerWeekCount[week - 1][shiftIndex] = employee.ShiftTypePerWeekCount[week - 1][shiftIndex] + 1;
    employee.HoursWorked += shift.ShiftType.HoursWorked;
    employee.HoursWorkedPerWeek[week - 1] = employee.HoursWorkedPerWeek[week - 1] + shift.ShiftType.HoursWorked;
    for (int j = 0; j < this.SchedulingPeriod.CoverRequirements.Requirements.length; j++) {
      CoverRequirement req = this.SchedulingPeriod.CoverRequirements.Requirements[j];
      CoverProvided prov = this.CoverProvided[j];
      if (this.SchedulingPeriod.EmployeeHasSkillForCover(employee.EmployeeDescription, req)) {
        if (req.CoverPerShiftUsed)
          prov.ProvidedCoverPerShift[shiftIndex][rosterDay] = prov.ProvidedCoverPerShift[shiftIndex][rosterDay] + 1; 
        if (req.CoverPerShiftGroupUsed) {
          int[] groups = this.SchedulingPeriod.GetShiftGroupsContainingShift(shiftIndex);
          for (int k = 0; k < groups.length; k++)
            prov.ProvidedCoverPerShiftGroup[groups[k]][rosterDay] = prov.ProvidedCoverPerShiftGroup[groups[k]][rosterDay] + 1; 
        } 
        if (req.CoverPerPeriodUsed && shift.ShiftType.CoversPeriods) {
          int k;
          for (k = 0; k < shift.ShiftType.Periods.length; k++)
            prov.ProvidedCoverPerPeriod[shift.ShiftType.Periods[k]][rosterDay] = prov.ProvidedCoverPerPeriod[shift.ShiftType.Periods[k]][rosterDay] + 1; 
          if (rosterDay + 1 < this.SchedulingPeriod.NumDaysInPeriod)
            for (k = 0; k < shift.ShiftType.NextDayPeriods.length; k++)
              prov.ProvidedCoverPerPeriod[shift.ShiftType.NextDayPeriods[k]][rosterDay + 1] = prov.ProvidedCoverPerPeriod[shift.ShiftType.NextDayPeriods[k]][rosterDay + 1] + 1;  
        } 
      } 
    } 
    return true;
  }
  
  public boolean UnAssignShift(Shift shift) {
    if (shift == null || !shift.isAssigned())
      return false; 
    Employee employee = shift.Employee;
    shift.Employee = null;
    int rosterDay = shift.RosterDay;
    int shiftIndex = shift.ShiftType.Index;
    employee.ShiftsCount--;
    employee.ShiftsOnDay[rosterDay][shiftIndex] = null;
    int shiftAssignmentIndex = rosterDay * this.SchedulingPeriod.ShiftTypesCount + shift.ShiftType.Index;
    employee.ShiftAssignments[shiftAssignmentIndex] = false;
    if (shift.ShiftType.getSpansMidnight())
      employee.NightShifts[rosterDay] = false; 
    employee.ShiftCountPerDay[rosterDay] = employee.ShiftCountPerDay[rosterDay] - 1;
    if (employee.ShiftCountPerDay[rosterDay] == 0) {
      if (employee.EmployeeDescription.DayOffRequestIsHoliday[rosterDay]) {
        employee.DayType[rosterDay] = 2;
        employee.HolidayDaysOffGranted++;
      } else {
        employee.DayType[rosterDay] = 0;
      } 
      if (!employee.EmployeeDescription.DayOffRequestIsWork[rosterDay])
        employee.DaysOffCount++; 
    } 
    employee.ShiftTypeCount[shiftIndex] = employee.ShiftTypeCount[shiftIndex] - 1;
    for (int i = 0; i < shift.ShiftType.ShiftGroupCount; i++) {
      ShiftGroup grp = shift.ShiftType.ShiftGroups[i];
      employee.ShiftGroupCount[grp.Index] = employee.ShiftGroupCount[grp.Index] - 1;
      employee.ShiftGroupPerDayCount[rosterDay][grp.Index] = employee.ShiftGroupPerDayCount[rosterDay][grp.Index] - 1;
    } 
    int week = shift.WeekNumber;
    employee.ShiftTypePerWeekCount[week - 1][shiftIndex] = employee.ShiftTypePerWeekCount[week - 1][shiftIndex] - 1;
    employee.HoursWorked -= shift.ShiftType.HoursWorked;
    employee.HoursWorkedPerWeek[week - 1] = employee.HoursWorkedPerWeek[week - 1] - shift.ShiftType.HoursWorked;
    for (int j = 0; j < this.SchedulingPeriod.CoverRequirements.Requirements.length; j++) {
      CoverRequirement req = this.SchedulingPeriod.CoverRequirements.Requirements[j];
      CoverProvided prov = this.CoverProvided[j];
      if (this.SchedulingPeriod.EmployeeHasSkillForCover(employee.EmployeeDescription, req)) {
        if (req.CoverPerShiftUsed)
          prov.ProvidedCoverPerShift[shiftIndex][rosterDay] = prov.ProvidedCoverPerShift[shiftIndex][rosterDay] - 1; 
        if (req.CoverPerShiftGroupUsed) {
          int[] groups = this.SchedulingPeriod.GetShiftGroupsContainingShift(shiftIndex);
          for (int k = 0; k < groups.length; k++)
            prov.ProvidedCoverPerShiftGroup[groups[k]][rosterDay] = prov.ProvidedCoverPerShiftGroup[groups[k]][rosterDay] - 1; 
        } 
        if (req.CoverPerPeriodUsed && shift.ShiftType.CoversPeriods) {
          int k;
          for (k = 0; k < shift.ShiftType.Periods.length; k++)
            prov.ProvidedCoverPerPeriod[shift.ShiftType.Periods[k]][rosterDay] = prov.ProvidedCoverPerPeriod[shift.ShiftType.Periods[k]][rosterDay] - 1; 
          if (rosterDay + 1 < this.SchedulingPeriod.NumDaysInPeriod)
            for (k = 0; k < shift.ShiftType.NextDayPeriods.length; k++)
              prov.ProvidedCoverPerPeriod[shift.ShiftType.NextDayPeriods[k]][rosterDay + 1] = prov.ProvidedCoverPerPeriod[shift.ShiftType.NextDayPeriods[k]][rosterDay + 1] - 1;  
        } 
      } 
    } 
    return true;
  }
  
  public int RecalculateAllPenalties() {
    int totPenalty = 0;
    for (int i = 0; i < this.Employees.length; i++) {
      Employee employee = this.Employees[i];
      employee.Penalty = SoftConstraints.CalculatePenalty(employee);
      totPenalty += employee.Penalty;
    } 
    this.EmployeesPenalty = totPenalty;
    int covPenalty = RecalculateCoverPenalty();
    totPenalty += covPenalty;
    this.CoverPenalty = covPenalty;
    return totPenalty;
  }
  
  public int RecalculateCoverPenalty() {
    int covPenalty = 0;
    for (int i = 0; i < this.CoverPenOnDay.length; i++)
      this.CoverPenOnDay[i] = 0; 
    for (int x = 0; x < this.SchedulingPeriod.CoverRequirements.Requirements.length; x++) {
      CoverRequirement req = this.SchedulingPeriod.CoverRequirements.Requirements[x];
      CoverProvided prov = this.CoverProvided[x];
      if (req.CoverPerPeriodUsed)
        for (int day = 0; day < this.SchedulingPeriod.NumDaysInPeriod; day++) {
          for (int p = 0; p < this.SchedulingPeriod.getDayPeriodsCount(); p++) {
            int newPen = 0;
            if (req.MinCoverPerPeriod[p][day] > 0 && 
              prov.ProvidedCoverPerPeriod[p][day] < req.MinCoverPerPeriod[p][day]) {
              int diff = req.MinCoverPerPeriod[p][day] - prov.ProvidedCoverPerPeriod[p][day];
              int pen = diff * this.SchedulingPeriod.MasterWeights.MinUnderStaffing;
              newPen += pen;
            } 
            if (req.MaxCoverPerPeriod[p][day] >= 0 && 
              prov.ProvidedCoverPerPeriod[p][day] > req.MaxCoverPerPeriod[p][day]) {
              int diff = prov.ProvidedCoverPerPeriod[p][day] - req.MaxCoverPerPeriod[p][day];
              int pen = diff * this.SchedulingPeriod.MasterWeights.MaxOverStaffing;
              newPen += pen;
            } 
            if (req.PrefCoverPerPeriod[p][day] >= 0 && 
              req.PrefCoverPerPeriod[p][day] != prov.ProvidedCoverPerPeriod[p][day]) {
              int diff = req.PrefCoverPerPeriod[p][day] - prov.ProvidedCoverPerPeriod[p][day];
              int pen = 0;
              if (diff < 0) {
                pen = Math.abs(diff * this.SchedulingPeriod.MasterWeights.PrefOverStaffing);
              } else {
                pen = diff * this.SchedulingPeriod.MasterWeights.PrefUnderStaffing;
              } 
              newPen += pen;
            } 
            prov.CoverPerPeriodPenalty[p][day] = newPen;
            this.CoverPenOnDay[day] = this.CoverPenOnDay[day] + newPen;
            covPenalty += newPen;
          } 
        }  
      if (req.CoverPerShiftUsed)
        for (int day = 0; day < this.SchedulingPeriod.NumDaysInPeriod; day++) {
          for (int j = 0; j < this.SchedulingPeriod.ShiftTypesCount; j++) {
            int newPen = 0;
            if (req.MinCoverPerShift[j][day] > 0 && 
              req.MinCoverPerShift[j][day] > prov.ProvidedCoverPerShift[j][day]) {
              int diff = req.MinCoverPerShift[j][day] - prov.ProvidedCoverPerShift[j][day];
              int pen = diff * this.SchedulingPeriod.MasterWeights.MinUnderStaffing;
              newPen += pen;
            } 
            if (req.MaxCoverPerShift[j][day] >= 0 && 
              req.MaxCoverPerShift[j][day] < prov.ProvidedCoverPerShift[j][day]) {
              int diff = prov.ProvidedCoverPerShift[j][day] - req.MaxCoverPerShift[j][day];
              int pen = diff * this.SchedulingPeriod.MasterWeights.MaxOverStaffing;
              newPen += pen;
            } 
            if (req.PrefCoverPerShift[j][day] >= 0 && 
              req.PrefCoverPerShift[j][day] != prov.ProvidedCoverPerShift[j][day]) {
              int diff = req.PrefCoverPerShift[j][day] - prov.ProvidedCoverPerShift[j][day];
              int pen = 0;
              if (diff < 0) {
                pen = Math.abs(diff * this.SchedulingPeriod.MasterWeights.PrefOverStaffing);
              } else {
                pen = diff * this.SchedulingPeriod.MasterWeights.PrefUnderStaffing;
              } 
              newPen += pen;
            } 
            prov.CoverPerShiftPenalty[j][day] = newPen;
            this.CoverPenOnDay[day] = this.CoverPenOnDay[day] + newPen;
            covPenalty += newPen;
          } 
        }  
      if (req.CoverPerShiftGroupUsed)
        for (int day = 0; day < this.SchedulingPeriod.NumDaysInPeriod; day++) {
          for (int j = 0; j < this.SchedulingPeriod.ShiftGroupsCount; j++) {
            int newPen = 0;
            if (req.MinCoverPerShiftGroup[j][day] > 0 && 
              req.MinCoverPerShiftGroup[j][day] > prov.ProvidedCoverPerShiftGroup[j][day]) {
              int diff = req.MinCoverPerShiftGroup[j][day] - prov.ProvidedCoverPerShiftGroup[j][day];
              int pen = diff * this.SchedulingPeriod.MasterWeights.MinUnderStaffing;
              newPen += pen;
            } 
            if (req.MaxCoverPerShiftGroup[j][day] >= 0 && 
              req.MaxCoverPerShiftGroup[j][day] < prov.ProvidedCoverPerShiftGroup[j][day]) {
              int diff = prov.ProvidedCoverPerShiftGroup[j][day] - req.MaxCoverPerShiftGroup[j][day];
              int pen = diff * this.SchedulingPeriod.MasterWeights.MaxOverStaffing;
              newPen += pen;
            } 
            if (req.PrefCoverPerShiftGroup[j][day] >= 0 && 
              req.PrefCoverPerShiftGroup[j][day] != prov.ProvidedCoverPerShiftGroup[j][day]) {
              int diff = req.PrefCoverPerShiftGroup[j][day] - prov.ProvidedCoverPerShiftGroup[j][day];
              int pen = 0;
              if (diff < 0) {
                pen = Math.abs(diff * this.SchedulingPeriod.MasterWeights.PrefOverStaffing);
              } else {
                pen = diff * this.SchedulingPeriod.MasterWeights.PrefUnderStaffing;
              } 
              newPen += pen;
            } 
            prov.CoverPerShiftGroupPenalty[j][day] = newPen;
            this.CoverPenOnDay[day] = this.CoverPenOnDay[day] + newPen;
            covPenalty += newPen;
          } 
        }  
    } 
    return covPenalty;
  }
  
  public Employee GetEmployee(String employeeID) {
    Object obj = this.EmployeeHash.get(employeeID);
    if (obj == null)
      return null; 
    return (Employee)obj;
  }
  
  public void CacheEmployeePenalties(int cacheIndex, boolean violationPenalties) {
    if (violationPenalties) {
      for (int x = 0; x < this.Employees.length; x++) {
        Employee employee = this.Employees[x];
        employee.PenaltyCache[cacheIndex] = employee.Penalty;
        System.arraycopy(employee.ConstraintViolationPenalties, 0, 
            employee.ConstraintViolationPenaltiesCache[cacheIndex], 0, 
            employee.ConstraintViolationPenalties.length);
      } 
    } else {
      for (int x = 0; x < this.Employees.length; x++) {
        Employee employee = this.Employees[x];
        employee.PenaltyCache[cacheIndex] = employee.Penalty;
      } 
    } 
    this.EmployeesPenaltyCache[cacheIndex] = this.EmployeesPenalty;
  }
  
  public void CacheEmployeePenalties(int cacheIndex) {
    CacheEmployeePenalties(cacheIndex, true);
  }
  
  public void RestoreEmployeePenalties(int cacheIndex, boolean violationPenalties) {
    if (violationPenalties) {
      for (int x = 0; x < this.Employees.length; x++) {
        Employee employee = this.Employees[x];
        employee.Penalty = employee.PenaltyCache[cacheIndex];
        System.arraycopy(employee.ConstraintViolationPenaltiesCache[cacheIndex], 0, 
            employee.ConstraintViolationPenalties, 0, 
            employee.ConstraintViolationPenalties.length);
      } 
    } else {
      for (int x = 0; x < this.Employees.length; x++) {
        Employee employee = this.Employees[x];
        employee.Penalty = employee.PenaltyCache[cacheIndex];
      } 
    } 
    this.EmployeesPenalty = this.EmployeesPenaltyCache[cacheIndex];
  }
  
  public void RestoreEmployeePenalties(int cacheIndex) {
    RestoreEmployeePenalties(cacheIndex, true);
  }
  
  public int UpdateCoverPens(Shift[] shifts) {
    SchedulingPeriod sp = this.SchedulingPeriod;
    int coverChange = 0;
    for (int x = 0; x < sp.CoverRequirements.Requirements.length; x++) {
      CoverRequirement req = sp.CoverRequirements.Requirements[x];
      CoverProvided prov = this.CoverProvided[x];
      for (int z = 0; z < shifts.length; z++) {
        Shift shift = shifts[z];
        if (shift != null) {
          int day = shift.RosterDay;
          if (req.CoverPerPeriodUsed && shift.ShiftType.CoversPeriods) {
            for (int i = 0; i < shift.ShiftType.Periods.length; i++) {
              int p = shift.ShiftType.Periods[i];
              int newPen = 0;
              if (req.MinCoverPerPeriod[p][day] > 0 && 
                prov.ProvidedCoverPerPeriod[p][day] < req.MinCoverPerPeriod[p][day]) {
                int diff = req.MinCoverPerPeriod[p][day] - prov.ProvidedCoverPerPeriod[p][day];
                int pen = diff * sp.MasterWeights.MinUnderStaffing;
                newPen += pen;
              } 
              if (req.MaxCoverPerPeriod[p][day] >= 0 && 
                prov.ProvidedCoverPerPeriod[p][day] > req.MaxCoverPerPeriod[p][day]) {
                int diff = prov.ProvidedCoverPerPeriod[p][day] - req.MaxCoverPerPeriod[p][day];
                int pen = diff * sp.MasterWeights.MaxOverStaffing;
                newPen += pen;
              } 
              if (req.PrefCoverPerPeriod[p][day] >= 0 && 
                req.PrefCoverPerPeriod[p][day] != prov.ProvidedCoverPerPeriod[p][day]) {
                int diff = req.PrefCoverPerPeriod[p][day] - prov.ProvidedCoverPerPeriod[p][day];
                int pen = 0;
                if (diff < 0) {
                  pen = Math.abs(diff * sp.MasterWeights.PrefOverStaffing);
                } else {
                  pen = diff * sp.MasterWeights.PrefUnderStaffing;
                } 
                newPen += pen;
              } 
              if (newPen != prov.CoverPerPeriodPenalty[p][day]) {
                this.CoverPenOnDay[day] = this.CoverPenOnDay[day] - prov.CoverPerPeriodPenalty[p][day];
                this.CoverPenOnDay[day] = this.CoverPenOnDay[day] + newPen;
                coverChange += newPen - prov.CoverPerPeriodPenalty[p][day];
                prov.CoverPerPeriodPenalty[p][day] = newPen;
              } 
            } 
            int nextDay = day + 1;
            if (nextDay < this.SchedulingPeriod.NumDaysInPeriod)
              for (int j = 0; j < shift.ShiftType.NextDayPeriods.length; j++) {
                int p = shift.ShiftType.NextDayPeriods[j];
                int newPen = 0;
                if (req.MinCoverPerPeriod[p][nextDay] > 0 && 
                  prov.ProvidedCoverPerPeriod[p][nextDay] < req.MinCoverPerPeriod[p][nextDay]) {
                  int diff = req.MinCoverPerPeriod[p][nextDay] - prov.ProvidedCoverPerPeriod[p][nextDay];
                  int pen = diff * sp.MasterWeights.MinUnderStaffing;
                  newPen += pen;
                } 
                if (req.MaxCoverPerPeriod[p][nextDay] >= 0 && 
                  prov.ProvidedCoverPerPeriod[p][nextDay] > req.MaxCoverPerPeriod[p][nextDay]) {
                  int diff = prov.ProvidedCoverPerPeriod[p][nextDay] - req.MaxCoverPerPeriod[p][nextDay];
                  int pen = diff * sp.MasterWeights.MaxOverStaffing;
                  newPen += pen;
                } 
                if (req.PrefCoverPerPeriod[p][nextDay] >= 0 && 
                  req.PrefCoverPerPeriod[p][nextDay] != prov.ProvidedCoverPerPeriod[p][nextDay]) {
                  int diff = req.PrefCoverPerPeriod[p][nextDay] - prov.ProvidedCoverPerPeriod[p][nextDay];
                  int pen = 0;
                  if (diff < 0) {
                    pen = Math.abs(diff * sp.MasterWeights.PrefOverStaffing);
                  } else {
                    pen = diff * sp.MasterWeights.PrefUnderStaffing;
                  } 
                  newPen += pen;
                } 
                if (newPen != prov.CoverPerPeriodPenalty[p][nextDay]) {
                  this.CoverPenOnDay[nextDay] = this.CoverPenOnDay[nextDay] - prov.CoverPerPeriodPenalty[p][nextDay];
                  this.CoverPenOnDay[nextDay] = this.CoverPenOnDay[nextDay] + newPen;
                  coverChange += newPen - prov.CoverPerPeriodPenalty[p][nextDay];
                  prov.CoverPerPeriodPenalty[p][nextDay] = newPen;
                } 
              }  
          } 
          if (req.CoverPerShiftUsed) {
            int i = shift.ShiftType.Index;
            int newPen = 0;
            if (req.MinCoverPerShift[i][day] > 0 && 
              req.MinCoverPerShift[i][day] > prov.ProvidedCoverPerShift[i][day]) {
              int diff = req.MinCoverPerShift[i][day] - prov.ProvidedCoverPerShift[i][day];
              int pen = diff * sp.MasterWeights.MinUnderStaffing;
              newPen += pen;
            } 
            if (req.MaxCoverPerShift[i][day] >= 0 && 
              req.MaxCoverPerShift[i][day] < prov.ProvidedCoverPerShift[i][day]) {
              int diff = prov.ProvidedCoverPerShift[i][day] - req.MaxCoverPerShift[i][day];
              int pen = diff * sp.MasterWeights.MaxOverStaffing;
              newPen += pen;
            } 
            if (req.PrefCoverPerShift[i][day] >= 0 && 
              req.PrefCoverPerShift[i][day] != prov.ProvidedCoverPerShift[i][day]) {
              int diff = req.PrefCoverPerShift[i][day] - prov.ProvidedCoverPerShift[i][day];
              int pen = 0;
              if (diff < 0) {
                pen = Math.abs(diff * sp.MasterWeights.PrefOverStaffing);
              } else {
                pen = diff * sp.MasterWeights.PrefUnderStaffing;
              } 
              newPen += pen;
            } 
            if (newPen != prov.CoverPerShiftPenalty[i][day]) {
              this.CoverPenOnDay[day] = this.CoverPenOnDay[day] - prov.CoverPerShiftPenalty[i][day];
              this.CoverPenOnDay[day] = this.CoverPenOnDay[day] + newPen;
              coverChange += newPen - prov.CoverPerShiftPenalty[i][day];
              prov.CoverPerShiftPenalty[i][day] = newPen;
            } 
          } 
          if (req.CoverPerShiftGroupUsed)
            for (int i = 0; i < sp.ShiftGroupsCount; i++) {
              if (sp.ShiftGroupContainsShiftType(i, shift.ShiftType.Index)) {
                int newPen = 0;
                if (req.MinCoverPerShiftGroup[i][day] > 0 && 
                  req.MinCoverPerShiftGroup[i][day] > prov.ProvidedCoverPerShiftGroup[i][day]) {
                  int diff = req.MinCoverPerShiftGroup[i][day] - prov.ProvidedCoverPerShiftGroup[i][day];
                  int pen = diff * sp.MasterWeights.MinUnderStaffing;
                  newPen += pen;
                } 
                if (req.MaxCoverPerShiftGroup[i][day] >= 0 && 
                  req.MaxCoverPerShiftGroup[i][day] < prov.ProvidedCoverPerShiftGroup[i][day]) {
                  int diff = prov.ProvidedCoverPerShiftGroup[i][day] - req.MaxCoverPerShiftGroup[i][day];
                  int pen = diff * sp.MasterWeights.MaxOverStaffing;
                  newPen += pen;
                } 
                if (req.PrefCoverPerShiftGroup[i][day] >= 0 && 
                  req.PrefCoverPerShiftGroup[i][day] != prov.ProvidedCoverPerShiftGroup[i][day]) {
                  int diff = req.PrefCoverPerShiftGroup[i][day] - prov.ProvidedCoverPerShiftGroup[i][day];
                  int pen = 0;
                  if (diff < 0) {
                    pen = Math.abs(diff * sp.MasterWeights.PrefOverStaffing);
                  } else {
                    pen = diff * sp.MasterWeights.PrefUnderStaffing;
                  } 
                  newPen += pen;
                } 
                if (newPen != prov.CoverPerShiftGroupPenalty[i][day]) {
                  this.CoverPenOnDay[day] = this.CoverPenOnDay[day] - prov.CoverPerShiftGroupPenalty[i][day];
                  this.CoverPenOnDay[day] = this.CoverPenOnDay[day] + newPen;
                  coverChange += newPen - prov.CoverPerShiftGroupPenalty[i][day];
                  prov.CoverPerShiftGroupPenalty[i][day] = newPen;
                } 
              } 
            }  
        } 
      } 
    } 
    return coverChange;
  }
  
  public void Empty() {
    for (int i = 0; i < this.Employees.length; i++) {
      for (int day = 0; day < this.SchedulingPeriod.NumDaysInPeriod; day++) {
        for (int j = 0; j < this.SchedulingPeriod.ShiftTypesCount; j++) {
          if ((this.Employees[i]).ShiftsOnDay[day][j] != null)
            UnAssignShift((this.Employees[i]).ShiftsOnDay[day][j]); 
        } 
      } 
    } 
    RecalculateAllPenalties();
  }
  
  public void MakeInitialAssignments() {
    if (!this.SchedulingPeriod.ContainsInitialAssignments)
      return; 
    byte b;
    int i;
    Employee[] arrayOfEmployee;
    for (i = (arrayOfEmployee = this.Employees).length, b = 0; b < i; ) {
      Employee employee = arrayOfEmployee[b];
      for (int day = 0; day < this.SchedulingPeriod.NumDaysInPeriod; day++) {
        for (int j = 0; j < this.SchedulingPeriod.ShiftTypesCount; j++) {
          int index = day * this.SchedulingPeriod.ShiftTypesCount + j;
          if (employee.EmployeeDescription.PreAssignments[index]) {
            ShiftType st = this.SchedulingPeriod.GetShiftType(j);
            Shift shift = new Shift(st, 
                this.SchedulingPeriod.ConvertRosterDayToDate(day), 
                this.SchedulingPeriod);
            int violationCount = employee.ViolationsForAssigningShift(shift);
            if (violationCount != -1)
              AssignShift(employee, shift); 
          } 
        } 
      } 
      b++;
    } 
    RecalculateAllPenalties();
  }
  
  public String toString() {
    StringWriter sw = new StringWriter();
    RosterPrinter.PrintRosterAsHTML(this, sw, true, 
        "", "", "", DateTime.getNow().ToShortDateString(), 
        "", "");
    return sw.toString();
  }
}
