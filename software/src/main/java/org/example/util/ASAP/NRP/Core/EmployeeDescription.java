package org.example.util.ASAP.NRP.Core;

import org.example.util.ASAP.NRP.Core.Constraints.ANROM.AlternativeSkillCategory;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.CompleteWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.IdenticalShiftTypesDuringWeekend;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxAssignmentsForDayOfWeek;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxConsecutiveFreeDays;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxConsecutiveWorkingDays;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxConsecutiveWorkingWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxHoursWorked;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxNumAssignments;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxShiftTypes;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxShiftTypesPerWeek;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxWorkingBankHolidays;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MaxWorkingWeekendsInFourWeeks;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MinConsecutiveFreeDays;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MinConsecutiveWorkingDays;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MinHoursWorked;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.MinTimeBetweenShifts;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.NoNightShiftBeforeFreeWeekend;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.NumConsecutiveShiftGroups;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.NumConsecutiveShiftTypes;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.RequestedDaysOff;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.RequestedDaysOn;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.RequestedShiftsOff;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.RequestedShiftsOn;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.ShiftTypeSuccessions;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.Tutorship;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.TwoFreeDaysAfterNightShifts;
import org.example.util.ASAP.NRP.Core.Constraints.ANROM.WorkSeparately;
import org.example.util.ASAP.NRP.Core.Constraints.Azaiez.MaxWeekendDays;
import org.example.util.ASAP.NRP.Core.Constraints.GPost.MaxShiftsPerDay;
import org.example.util.ASAP.NRP.Core.Constraints.GPost.MaxShiftsPerWeek;
import org.example.util.ASAP.NRP.Core.Constraints.GPost.MinShiftsPerWeek;
import org.example.util.ASAP.NRP.Core.Constraints.Ikegami.MaxDaysBetweenShiftSeries;
import org.example.util.ASAP.NRP.Core.Constraints.Ikegami.MaxDaysOff;
import org.example.util.ASAP.NRP.Core.Constraints.Ikegami.MaxWeekendsOff;
import org.example.util.ASAP.NRP.Core.Constraints.Ikegami.MaxWorkingDaysPerWeek;
import org.example.util.ASAP.NRP.Core.Constraints.Ikegami.MinDaysBetweenShiftSeries;
import org.example.util.ASAP.NRP.Core.Constraints.Ikegami.MinDaysOff;
import org.example.util.ASAP.NRP.Core.Constraints.Millar.MaxWorkingWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.DaysOffRequestsBetweenDates;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MaxConsecutiveFreeWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MaxConsecutiveShiftGroups;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MaxConsecutiveShiftTypes;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MaxHoursWorkedBetweenDates;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MaxShiftGroupRatios;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MaxShiftTypeRatios;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinConsecutiveFreeWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinConsecutiveShiftGroups;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinConsecutiveShiftTypes;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinConsecutiveWorkingWeekends;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinHoursWorkedBetweenDates;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinShiftGroupRatios;
import org.example.util.ASAP.NRP.Core.Constraints.Montreal.MinShiftTypeRatios;
import org.example.util.ASAP.NRP.Core.Constraints.ORTEC01.MaxShiftsPerWeekStartMon;
import org.example.util.ASAP.NRP.Core.Constraints.ORTEC01.MaxWorkingWeekendsIncFriNight;
import org.example.util.ASAP.NRP.Core.Constraints.ORTEC01.MinShiftsPerWeekStartMon;
import org.example.util.ASAP.NRP.Core.Constraints.QMC.MaxHoursPerFortnight;
import org.example.util.ASAP.NRP.Core.Constraints.QMC.RequestedShiftGroupsOn;
import org.example.util.ASAP.NRP.Core.Constraints.QMC.ShiftGroupRequest;
import org.example.util.ASAP.NRP.Core.Constraints.SINTEF.MaxHoursPerWeek;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.BadPatterns;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.EmployeeAvailability;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.GoodPatterns;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.MaxShiftGroups;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.MinShiftGroups;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.MinShiftTypes;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.SkilledShifts;
import java.util.ArrayList;
import java.util.Hashtable;

public class EmployeeDescription {
  public enum SkillType {
    Primary, Secondary;
  }
  
  public boolean AvailableDuringEntirePeriod = true;
  
  public boolean[] AvailableOn;
  
  public String ID = "";
  
  public String FirstName = "";
  
  public String LastName = "";
  
  public String EmailAddress = "";
  
  public String PhoneNumber = "";
  
  public int IndexID = 0;
  
  public int TutorCount = 0;
  
  public int AvoidPartnershipsCount = 0;
  
  public int VerticallyRelatedEmployeesCount = 0;
  
  public String SkillsDescription = "";
  
  public Contract Contract = null;
  
  DateTime employmentStartDate = new DateTime();
  
  DateTime employmentEndDate = new DateTime();
  
  public Hashtable<String, SkillType> Skills = new Hashtable<String, SkillType>();
  
  public ArrayList<EmployeeDescription> Tutors = new ArrayList<EmployeeDescription>();
  
  public ArrayList<EmployeeDescription> AvoidPartnership = new ArrayList<EmployeeDescription>();
  
  public ArrayList<EmployeeDescription> VerticallyRelatedEmployees = new ArrayList<EmployeeDescription>();
  
  public SchedulingPeriod SchedulingPeriod;
  
  public SchedulingHistory SchedulingHistory;
  
  public int[] DayOffRequests;
  
  public boolean[] DayOffRequestIsHoliday;
  
  public boolean[] DayOffRequestIsWork;
  
  public int[] DayOnRequests;
  
  public int[] ShiftOffRequests;
  
  public int[] ShiftOnRequests;
  
  public boolean[] PreAssignments;
  
  public ArrayList<DayOffRequest> DayOffRequestObjs = new ArrayList<DayOffRequest>();
  
  public ArrayList<DayOnRequest> DayOnRequestObjs = new ArrayList<DayOnRequest>();
  
  public ArrayList<ShiftRequest> ShiftOffRequestObjs = new ArrayList<ShiftRequest>();
  
  public ArrayList<ShiftRequest> ShiftOnRequestObjs = new ArrayList<ShiftRequest>();
  
  public int DayOffRequestCount = 0;
  
  public int DayOnRequestCount = 0;
  
  public int ShiftOffRequestCount = 0;
  
  public int ShiftOnRequestCount = 0;
  
  public int DaysOffRequestBetweenDatesCount = 0;
  
  public SoftConstraint[] AllSoftConstraints = new SoftConstraint[0];
  
  public ShiftGroupRequest[] ShiftGroupOnRequests = new ShiftGroupRequest[0];
  
  public DaysOffRequestBetweenDates[] DaysOffRequestsBetweenDates = new DaysOffRequestBetweenDates[0];
  
  public BadPatterns BadPatternConstraint = null;
  
  public boolean[] FrozenDay;
  
  public boolean UsesConstraintANROMRequestedShiftsOff = false;
  
  public boolean UsesConstraintANROMRequestedDaysOff = false;
  
  public boolean UsesConstraintANROMRequestedDaysOn = false;
  
  public boolean UsesConstraintANROMRequestedShiftsOn = false;
  
  public boolean UsesConstraintQMCRequestedShiftGroupsOn = false;
  
  public boolean InRoster;
  
  public EmployeeDescription(String employeeID, SchedulingPeriod schedulingPeriod) {
    this.InRoster = true;
    this.ID = employeeID;
    this.SchedulingPeriod = schedulingPeriod;
    this.SchedulingHistory = new SchedulingHistory(schedulingPeriod);
    this.DayOffRequests = new int[schedulingPeriod.NumDaysInPeriod];
    int i;
    for (i = 0; i < this.DayOffRequests.length; i++)
      this.DayOffRequests[i] = 0; 
    this.DayOffRequestIsHoliday = new boolean[schedulingPeriod.NumDaysInPeriod];
    this.DayOffRequestIsWork = new boolean[schedulingPeriod.NumDaysInPeriod];
    this.DayOnRequests = new int[schedulingPeriod.NumDaysInPeriod];
    for (i = 0; i < this.DayOnRequests.length; i++)
      this.DayOnRequests[i] = 0; 
    this.ShiftOffRequests = new int[schedulingPeriod.NumDaysInPeriod * schedulingPeriod.ShiftTypesCount];
    for (i = 0; i < this.ShiftOffRequests.length; i++)
      this.ShiftOffRequests[i] = 0; 
    this.ShiftOnRequests = new int[schedulingPeriod.NumDaysInPeriod * schedulingPeriod.ShiftTypesCount];
    for (i = 0; i < this.ShiftOnRequests.length; i++)
      this.ShiftOnRequests[i] = 0; 
    this.AvailableOn = new boolean[schedulingPeriod.NumDaysInPeriod];
    for (i = 0; i < this.AvailableOn.length; i++)
      this.AvailableOn[i] = true; 
    this.PreAssignments = new boolean[schedulingPeriod.NumDaysInPeriod * schedulingPeriod.ShiftTypesCount];
    this.FrozenDay = new boolean[schedulingPeriod.NumDaysInPeriod];
  }
  
  public String getName() {
    String name = this.LastName;
    if (!this.FirstName.equalsIgnoreCase(""))
      if (name.equalsIgnoreCase("")) {
        name = this.FirstName;
      } else {
        name = String.valueOf(name) + ", " + this.FirstName;
      }  
    return name;
  }
  
  public String getSkillsLabel() {
    String skillsLabel = "";
    return skillsLabel;
  }
  
  public DateTime getEmploymentStartDate() {
    return this.employmentStartDate;
  }
  
  public void setEmploymentStartDate(DateTime value) {
    this.employmentStartDate = value;
    if (this.employmentEndDate != null && 
      this.employmentEndDate.isLessThan(this.SchedulingPeriod.EndDate)) {
      this.AvailableDuringEntirePeriod = false;
    } else if (this.employmentStartDate.isGreaterThan(this.SchedulingPeriod.StartDate)) {
      this.AvailableDuringEntirePeriod = false;
    } else {
      this.AvailableDuringEntirePeriod = true;
    } 
    if (this.employmentStartDate.isGreaterThan(this.SchedulingPeriod.StartDate)) {
      DateTime tempDate = this.SchedulingPeriod.StartDate;
      for (int i = 0; i < this.SchedulingPeriod.NumDaysInPeriod; i++) {
        if (tempDate.isLessThan(this.employmentStartDate))
          this.AvailableOn[i] = false; 
        tempDate = tempDate.AddDays(1);
      } 
    } 
  }
  
  public DateTime getEmploymentEndDate() {
    return this.employmentEndDate;
  }
  
  public void setEmploymentEndDate(DateTime value) {
    this.employmentEndDate = value;
    if (this.employmentStartDate != null && 
      this.employmentStartDate.isGreaterThan(this.SchedulingPeriod.StartDate)) {
      this.AvailableDuringEntirePeriod = false;
    } else if (this.employmentEndDate.isLessThan(this.SchedulingPeriod.EndDate)) {
      this.AvailableDuringEntirePeriod = false;
    } else {
      this.AvailableDuringEntirePeriod = true;
    } 
    if (this.employmentEndDate.isLessThan(this.SchedulingPeriod.EndDate)) {
      DateTime tempDate = this.SchedulingPeriod.EndDate;
      for (int i = this.SchedulingPeriod.NumDaysInPeriod - 1; i >= 0; i--) {
        if (tempDate.isGreaterThan(this.employmentEndDate))
          this.AvailableOn[i] = false; 
        tempDate = tempDate.AddDays(-1);
      } 
    } 
  }
  
  public boolean AddSkill(String skillID, String type) {
    if (this.Skills.containsKey(skillID)) {
      System.out.println("Unable to Add Skill : " + skillID + " to Employee : " + getName() + " as already has this skill.");
      return false;
    } 
    if (type.equalsIgnoreCase("Primary")) {
      this.Skills.put(skillID, SkillType.Primary);
    } else if (type.equalsIgnoreCase("Secondary")) {
      this.Skills.put(skillID, SkillType.Secondary);
    } else {
      System.out.println("Unable to Add Skill : " + skillID + " to Employee : " + getName() + " as skill type " + type + " is an unknown skill type");
      return false;
    } 
    UpdateSkillsDescription();
    return true;
  }
  
  public void DeleteSkill(Skill skill) {
    this.Skills.remove(skill.ID);
    UpdateSkillsDescription();
  }
  
  public void ClearSkills() {
    this.Skills.clear();
    UpdateSkillsDescription();
  }
  
  private void UpdateSkillsDescription() {
    this.SkillsDescription = "";
  }
  
  public boolean HasSkillType(String skillID, SkillType type) {
    if (this.Skills.containsKey(skillID)) {
      if ((SkillType)this.Skills.get(skillID) == type)
        return true; 
      return false;
    } 
    return false;
  }
  
  public boolean HasSkillType(Skill skill) {
    if (this.Skills.containsKey(skill.ID))
      return true; 
    return false;
  }
  
  public String GetSkillsDescription() {
    return this.SkillsDescription;
  }
  
  public boolean AddTutor(EmployeeDescription employee) {
    if (this.Tutors.contains(employee))
      return false; 
    this.Tutors.add(employee);
    this.TutorCount++;
    employee.AddVerticallyRelatedEmployee(this);
    return true;
  }
  
  public EmployeeDescription GetTutor(int index) {
    return this.Tutors.get(index);
  }
  
  public boolean AvoidPartnership(EmployeeDescription employee) {
    if (this.AvoidPartnership.contains(employee))
      return false; 
    this.AvoidPartnership.add(employee);
    this.AvoidPartnershipsCount++;
    employee.AddVerticallyRelatedEmployee(this);
    return true;
  }
  
  public EmployeeDescription GetAvoidPartnership(int index) {
    return this.AvoidPartnership.get(index);
  }
  
  public boolean AddVerticallyRelatedEmployee(EmployeeDescription employee) {
    if (this.VerticallyRelatedEmployees.contains(employee))
      return false; 
    this.VerticallyRelatedEmployees.add(employee);
    this.VerticallyRelatedEmployeesCount++;
    return true;
  }
  
  public EmployeeDescription GetVerticallyRelatedEmployee(int index) {
    return this.VerticallyRelatedEmployees.get(index);
  }
  
  public boolean AvailableOnDay(int rosterDay) {
    return this.AvailableOn[rosterDay];
  }
  
  public void AddShiftGroupOnRequest(ShiftGroupRequest request) {
    this.ShiftGroupOnRequests = CSharpConversionHelper.ArrayResize(this.ShiftGroupOnRequests, this.ShiftGroupOnRequests.length + 1);
    this.ShiftGroupOnRequests[this.ShiftGroupOnRequests.length - 1] = request;
  }
  
  public void AddDaysOffRequestBetweenDates(DaysOffRequestBetweenDates request) {
    this.DaysOffRequestsBetweenDates = CSharpConversionHelper.ArrayResize(this.DaysOffRequestsBetweenDates, this.DaysOffRequestsBetweenDates.length + 1);
    this.DaysOffRequestsBetweenDates[this.DaysOffRequestsBetweenDates.length - 1] = request;
    this.DaysOffRequestBetweenDatesCount++;
  }
  
  public void CreateSoftConstraints() {
    if (this.Contract == null)
      return; 
    ArrayList<SoftConstraint> array = new ArrayList<SoftConstraint>();
    if (this.Contract.MaxShiftTypesIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxShiftTypes;
      if (this.Contract.MaxShiftTypesWeight > -1)
        weight = this.Contract.MaxShiftTypesWeight; 
      if (this.Contract.MaxShiftTypesUsed)
        array.add(new MaxShiftTypes(weight)); 
      if (this.Contract.MaxShiftGroupsUsed)
        array.add(new MaxShiftGroups(weight)); 
    } 
    if (this.Contract.MinShiftTypesIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinShiftTypes;
      if (this.Contract.MinShiftTypesWeight > -1)
        weight = this.Contract.MinShiftTypesWeight; 
      if (this.Contract.MinShiftTypesUsed)
        array.add(new MinShiftTypes(weight)); 
      if (this.Contract.MinShiftGroupsUsed)
        array.add(new MinShiftGroups(weight)); 
    } 
    if (this.Contract.MaxNumAssignmentsIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxNumAssignments;
      if (this.Contract.MaxNumAssignmentsWeight > -1)
        weight = this.Contract.MaxNumAssignmentsWeight; 
      array.add(new MaxNumAssignments(weight));
    } 
    if (this.Contract.MaxConsecutiveWorkingWeekendsIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxConsecutiveWorkingWeekends;
      if (this.Contract.MaxConsecutiveWorkingWeekendsWeight > -1)
        weight = this.Contract.MaxConsecutiveWorkingWeekendsWeight; 
      array.add(new MaxConsecutiveWorkingWeekends(weight));
    } 
    if (this.Contract.MinConsecutiveWorkingWeekendsIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinConsecutiveWorkingWeekends;
      if (this.Contract.MinConsecutiveWorkingWeekendsWeight > -1)
        weight = this.Contract.MinConsecutiveWorkingWeekendsWeight; 
      array.add(new MinConsecutiveWorkingWeekends(weight));
    } 
    if (this.Contract.MaxConsecutiveFreeWeekendsIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxConsecutiveFreeWeekends;
      if (this.Contract.MaxConsecutiveFreeWeekendsWeight > -1)
        weight = this.Contract.MaxConsecutiveFreeWeekendsWeight; 
      array.add(new MaxConsecutiveFreeWeekends(weight));
    } 
    if (this.Contract.MinConsecutiveFreeWeekendsIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinConsecutiveFreeWeekends;
      if (this.Contract.MinConsecutiveFreeWeekendsWeight > -1)
        weight = this.Contract.MinConsecutiveFreeWeekendsWeight; 
      array.add(new MinConsecutiveFreeWeekends(weight));
    } 
    if (this.Contract.MaxWorkingWeekendsInFourWeeksIsOn) {
      int weight;
      if (this.Contract.MaxWorkingWeekendsInFourWeeksWeight > -1) {
        weight = this.Contract.MaxWorkingWeekendsInFourWeeksWeight;
      } else {
        weight = this.SchedulingPeriod.MasterWeights.MaxWorkingWeekendsInFourWeeks;
      } 
      array.add(new MaxWorkingWeekendsInFourWeeks(weight));
    } 
    if (this.Contract.MaxWorkingWeekendsIncFriNightIsOn) {
      int weight;
      if (this.Contract.MaxWorkingWeekendsIncFriNightWeight > -1) {
        weight = this.Contract.MaxWorkingWeekendsIncFriNightWeight;
      } else {
        weight = this.SchedulingPeriod.MasterWeights.MaxWorkingWeekendsIncFriNight;
      } 
      array.add(new MaxWorkingWeekendsIncFriNight(weight));
    } 
    if (this.Contract.MaxWeekendsOffIsOn) {
      int weight;
      if (this.Contract.MaxWeekendsOffWeight > -1) {
        weight = this.Contract.MaxWeekendsOffWeight;
      } else {
        weight = this.SchedulingPeriod.MasterWeights.MaxWeekendsOff;
      } 
      array.add(new MaxWeekendsOff(weight));
    } 
    if (this.Contract.MaxWorkingWeekendsIsOn) {
      int weight;
      if (this.Contract.MaxWorkingWeekendsWeight > -1) {
        weight = this.Contract.MaxWorkingWeekendsWeight;
      } else {
        weight = this.SchedulingPeriod.MasterWeights.MaxWorkingWeekends;
      } 
      array.add(new MaxWorkingWeekends(weight));
    } 
    if (this.SchedulingPeriod.MasterWeights.MinTimeBetweenShifts > 0) {
      int weight = this.SchedulingPeriod.MasterWeights.MinTimeBetweenShifts;
      array.add(new MinTimeBetweenShifts(weight));
    } 
    if (this.Contract.MaxDaysOffIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxDaysOff;
      if (this.Contract.MaxDaysOffWeight > -1)
        weight = this.Contract.MaxDaysOffWeight; 
      array.add(new MaxDaysOff(weight));
    } 
    if (this.Contract.MinDaysOffIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinDaysOff;
      if (this.Contract.MinDaysOffWeight > -1)
        weight = this.Contract.MinDaysOffWeight; 
      array.add(new MinDaysOff(weight));
    } 
    if (this.Contract.MaxShiftsPerDayIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxShiftsPerDay;
      if (this.Contract.MaxShiftsPerDayWeight > -1)
        weight = this.Contract.MaxShiftsPerDayWeight; 
      array.add(new MaxShiftsPerDay(weight));
    } 
    if (this.Contract.MinShiftsPerWeekIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinShiftsPerWeek;
      if (this.Contract.MinShiftsPerWeekWeight > -1)
        weight = this.Contract.MinShiftsPerWeekWeight; 
      array.add(new MinShiftsPerWeek(weight));
    } 
    if (this.Contract.MaxShiftsPerWeekIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxShiftsPerWeek;
      if (this.Contract.MaxShiftsPerWeekWeight > -1)
        weight = this.Contract.MaxShiftsPerWeekWeight; 
      array.add(new MaxShiftsPerWeek(weight));
    } 
    if (this.Contract.MaxWorkingDaysPerWeekIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxWorkingDaysPerWeek;
      if (this.Contract.MaxWorkingDaysPerWeekWeight > -1)
        weight = this.Contract.MaxWorkingDaysPerWeekWeight; 
      array.add(new MaxWorkingDaysPerWeek(weight));
    } 
    if (this.Contract.MaxConsecutiveWorkingDaysIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxConsecutiveWorkingDays;
      if (this.Contract.MaxConsecutiveWorkingDaysWeight > -1)
        weight = this.Contract.MaxConsecutiveWorkingDaysWeight; 
      array.add(new MaxConsecutiveWorkingDays(weight));
    } 
    if (this.Contract.MinConsecutiveWorkingDaysIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinConsecutiveWorkingDays;
      if (this.Contract.MinConsecutiveWorkingDaysWeight > -1)
        weight = this.Contract.MinConsecutiveWorkingDaysWeight; 
      array.add(new MinConsecutiveWorkingDays(weight));
    } 
    if (this.Contract.MinDaysBetweenShiftSeriesIsOn) {
      int weight;
      if (this.Contract.MinDaysBetweenShiftSeriesWeight > -1) {
        weight = this.Contract.MinDaysBetweenShiftSeriesWeight;
      } else {
        weight = this.SchedulingPeriod.MasterWeights.MinDaysBetweenShiftSeries;
      } 
      array.add(new MinDaysBetweenShiftSeries(weight));
    } 
    if (this.Contract.MaxDaysBetweenShiftSeriesIsOn) {
      int weight;
      if (this.Contract.MaxDaysBetweenShiftSeriesWeight > -1) {
        weight = this.Contract.MaxDaysBetweenShiftSeriesWeight;
      } else {
        weight = this.SchedulingPeriod.MasterWeights.MaxDaysBetweenShiftSeries;
      } 
      array.add(new MaxDaysBetweenShiftSeries(weight));
    } 
    if (this.Contract.MinConsecutiveShiftTypesIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinConsecutiveShiftTypes;
      if (this.Contract.MinConsecutiveShiftTypesWeight > -1)
        weight = this.Contract.MinConsecutiveShiftTypesWeight; 
      if (this.Contract.MinConsecutiveShiftTypesUsed)
        array.add(new MinConsecutiveShiftTypes(weight)); 
      if (this.Contract.MinConsecutiveShiftGroupsUsed)
        array.add(new MinConsecutiveShiftGroups(weight)); 
    } 
    if (this.Contract.MaxConsecutiveShiftTypesIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxConsecutiveShiftTypes;
      if (this.Contract.MaxConsecutiveShiftTypesWeight > -1)
        weight = this.Contract.MaxConsecutiveShiftTypesWeight; 
      if (this.Contract.MaxConsecutiveShiftTypesUsed)
        array.add(new MaxConsecutiveShiftTypes(weight)); 
      if (this.Contract.MaxConsecutiveShiftGroupsUsed)
        array.add(new MaxConsecutiveShiftGroups(weight)); 
    } 
    if (this.Contract.MinHoursWorkedBetweenDatesIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinHoursWorkedBetweenDates;
      if (this.Contract.MinHoursWorkedBetweenDatesWeight > -1)
        weight = this.Contract.MinHoursWorkedBetweenDatesWeight; 
      array.add(new MinHoursWorkedBetweenDates(weight));
    } 
    if (this.Contract.MaxHoursWorkedBetweenDatesIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxHoursWorkedBetweenDates;
      if (this.Contract.MaxHoursWorkedBetweenDatesWeight > -1)
        weight = this.Contract.MaxHoursWorkedBetweenDatesWeight; 
      array.add(new MaxHoursWorkedBetweenDates(weight));
    } 
    if (this.Contract.ValidShiftTypeSuccessionsIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.ValidShiftTypeSuccessions;
      if (this.Contract.ValidShiftTypeSuccessionsWeight > -1)
        weight = this.Contract.ValidShiftTypeSuccessionsWeight; 
      array.add(new ShiftTypeSuccessions(weight));
    } 
    if (this.Contract.MaxShiftTypeRatiosIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxShiftTypeRatios;
      if (this.Contract.MaxShiftTypeRatiosWeight > -1)
        weight = this.Contract.MaxShiftTypeRatiosWeight; 
      if (this.Contract.MaxShiftTypeRatiosUsed)
        array.add(new MaxShiftTypeRatios(weight)); 
      if (this.Contract.MaxShiftGroupRatiosUsed)
        array.add(new MaxShiftGroupRatios(weight)); 
    } 
    if (this.Contract.MinShiftTypeRatiosIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinShiftTypeRatios;
      if (this.Contract.MinShiftTypeRatiosWeight > -1)
        weight = this.Contract.MinShiftTypeRatiosWeight; 
      if (this.Contract.MinShiftTypeRatiosUsed)
        array.add(new MinShiftTypeRatios(weight)); 
      if (this.Contract.MinShiftGroupRatiosUsed)
        array.add(new MinShiftGroupRatios(weight)); 
    } 
    if (this.Contract.MaxWorkingBankHolidaysIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxWorkingBankHolidays;
      if (this.Contract.MaxWorkingBankHolidaysWeight > -1)
        weight = this.Contract.MaxWorkingBankHolidaysWeight; 
      array.add(new MaxWorkingBankHolidays(weight));
    } 
    if (this.Contract.MaxConsecutiveFreeDaysIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxConsecutiveFreeDays;
      if (this.Contract.MaxConsecutiveFreeDaysWeight > -1)
        weight = this.Contract.MaxConsecutiveFreeDaysWeight; 
      array.add(new MaxConsecutiveFreeDays(weight));
    } 
    if (this.Contract.MinConsecutiveFreeDaysIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinConsecutiveFreeDays;
      if (this.Contract.MinConsecutiveFreeDaysWeight > -1)
        weight = this.Contract.MinConsecutiveFreeDaysWeight; 
      array.add(new MinConsecutiveFreeDays(weight));
    } 
    if (this.Contract.CompleteWeekends) {
      int weight = this.SchedulingPeriod.MasterWeights.CompleteWeekends;
      if (this.Contract.CompleteWeekendsWeight > -1)
        weight = this.Contract.CompleteWeekendsWeight; 
      array.add(new CompleteWeekends(weight));
    } 
    if (this.Contract.IdenticalShiftTypesDuringWeekend) {
      int weight = this.SchedulingPeriod.MasterWeights.IdenticalShiftTypesDuringWeekend;
      if (this.Contract.IdenticalShiftTypesDuringWeekendWeight > -1)
        weight = this.Contract.IdenticalShiftTypesDuringWeekendWeight; 
      array.add(new IdenticalShiftTypesDuringWeekend(weight));
    } 
    if (this.Contract.NoNightShiftBeforeFreeWeekend) {
      int weight = this.SchedulingPeriod.MasterWeights.NoNightShiftBeforeFreeWeekend;
      if (this.Contract.NoNightShiftBeforeFreeWeekendWeight > -1)
        weight = this.Contract.NoNightShiftBeforeFreeWeekendWeight; 
      array.add(new NoNightShiftBeforeFreeWeekend(weight));
    } 
    if (this.Contract.TwoFreeDaysAfterNightShifts) {
      int weight = this.SchedulingPeriod.MasterWeights.TwoFreeDaysAfterNightShifts;
      if (this.Contract.TwoFreeDaysAfterNightShiftsWeight > -1)
        weight = this.Contract.TwoFreeDaysAfterNightShiftsWeight; 
      array.add(new TwoFreeDaysAfterNightShifts(weight));
    } 
    if (this.Contract.MaxAssignmentsForDayOfWeekIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxAssignmentsForDayOfWeek;
      if (this.Contract.MaxAssignmentsForDayOfWeekWeight > -1)
        weight = this.Contract.MaxAssignmentsForDayOfWeekWeight; 
      array.add(new MaxAssignmentsForDayOfWeek(weight));
    } 
    if (this.Contract.ValidNumConsecutiveShiftTypesIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.ValidNumConsecutiveShiftTypes;
      if (this.Contract.ValidNumConsecutiveShiftTypesWeight > -1)
        weight = this.Contract.ValidNumConsecutiveShiftTypesWeight; 
      array.add(new NumConsecutiveShiftTypes(weight));
    } 
    if (this.Contract.ValidNumConsecutiveShiftGroupsIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.ValidNumConsecutiveShiftGroups;
      if (this.Contract.ValidNumConsecutiveShiftGroupsWeight > -1)
        weight = this.Contract.ValidNumConsecutiveShiftGroupsWeight; 
      array.add(new NumConsecutiveShiftGroups(weight));
    } 
    if (this.Contract.MaxShiftTypesPerWeekIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxShiftTypesPerWeek;
      if (this.Contract.MaxShiftTypesPerWeekWeight > -1)
        weight = this.Contract.MaxShiftTypesPerWeekWeight; 
      array.add(new MaxShiftTypesPerWeek(weight));
    } 
    if (this.Contract.MaxHoursWorkedIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxHoursWorked;
      if (this.Contract.MaxHoursWorkedWeight > -1)
        weight = this.Contract.MaxHoursWorkedWeight; 
      array.add(new MaxHoursWorked(weight));
    } 
    if (this.Contract.MinHoursWorkedIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinHoursWorked;
      if (this.Contract.MinHoursWorkedWeight > -1)
        weight = this.Contract.MinHoursWorkedWeight; 
      array.add(new MinHoursWorked(weight));
    } 
    if (this.Contract.AlternativeSkillCategory) {
      int weight = this.SchedulingPeriod.MasterWeights.AlternativeSkillCategory;
      if (this.Contract.AlternativeSkillCategoryWeight > -1)
        weight = this.Contract.AlternativeSkillCategoryWeight; 
      array.add(new AlternativeSkillCategory(weight));
    } 
    if (this.TutorCount > 0 && 
      this.SchedulingPeriod.MasterWeights.Tutorship > 0) {
      int weight = this.SchedulingPeriod.MasterWeights.Tutorship;
      array.add(new Tutorship(weight));
    } 
    if (this.AvoidPartnershipsCount > 0 && 
      this.SchedulingPeriod.MasterWeights.WorkSeparately > 0) {
      int weight = this.SchedulingPeriod.MasterWeights.WorkSeparately;
      array.add(new WorkSeparately(weight));
    } 
    if (this.DaysOffRequestBetweenDatesCount > 0)
      array.add(new DaysOffRequestsBetweenDates()); 
    if (this.SchedulingPeriod.ShiftsRequireSkills && this.SchedulingPeriod.MasterWeights.SkilledShifts > 0)
      array.add(new SkilledShifts(this.SchedulingPeriod.MasterWeights.SkilledShifts)); 
    if (!this.AvailableDuringEntirePeriod && this.SchedulingPeriod.MasterWeights.EmployeeAvailability > 0)
      array.add(new EmployeeAvailability(this.SchedulingPeriod.MasterWeights.EmployeeAvailability)); 
    if (this.Contract.MaxShiftsPerWeekStartMonIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxShiftsPerWeekStartMon;
      if (this.Contract.MaxShiftsPerWeekStartMonWeight > -1)
        weight = this.Contract.MaxShiftsPerWeekStartMonWeight; 
      array.add(new MaxShiftsPerWeekStartMon(weight));
    } 
    if (this.Contract.MinShiftsPerWeekStartMonIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MinShiftsPerWeekStartMon;
      if (this.Contract.MinShiftsPerWeekStartMonWeight > -1)
        weight = this.Contract.MinShiftsPerWeekStartMonWeight; 
      array.add(new MinShiftsPerWeekStartMon(weight));
    } 
    if (this.Contract.MaxHoursPerWeekIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxHoursPerWeek;
      if (this.Contract.MaxHoursPerWeekWeight > -1)
        weight = this.Contract.MaxHoursPerWeekWeight; 
      array.add(new MaxHoursPerWeek(weight));
    } 
    if (this.Contract.MaxHoursPerFortnightIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxHoursPerFortnight;
      if (this.Contract.MaxHoursPerFortnightWeight > -1)
        weight = this.Contract.MaxHoursPerFortnightWeight; 
      array.add(new MaxHoursPerFortnight(weight));
    } 
    if (this.Contract.MaxWeekendDaysIsOn) {
      int weight = this.SchedulingPeriod.MasterWeights.MaxWeekendDays;
      if (this.Contract.MaxWeekendDaysWeight > -1)
        weight = this.Contract.MaxWeekendDaysWeight; 
      array.add(new MaxWeekendDays(weight));
    } 
    if (this.Contract.BadPatternsIsOn) {
      this.BadPatternConstraint = new BadPatterns(this);
      array.add(this.BadPatternConstraint);
    } 
    if (this.Contract.GoodPatternsIsOn)
      array.add(new GoodPatterns()); 
    if (this.DayOffRequestCount > 0) {
      array.add(new RequestedDaysOff());
      this.UsesConstraintANROMRequestedDaysOff = true;
    } 
    if (this.DayOnRequestCount > 0) {
      array.add(new RequestedDaysOn());
      this.UsesConstraintANROMRequestedDaysOn = true;
    } 
    if (this.ShiftOffRequestCount > 0) {
      array.add(new RequestedShiftsOff());
      this.UsesConstraintANROMRequestedShiftsOff = true;
    } 
    if (this.ShiftOnRequestCount > 0) {
      array.add(new RequestedShiftsOn());
      this.UsesConstraintANROMRequestedShiftsOn = true;
    } 
    if (this.ShiftGroupOnRequests != null && this.ShiftGroupOnRequests.length > 0) {
      array.add(new RequestedShiftGroupsOn());
      this.UsesConstraintQMCRequestedShiftGroupsOn = true;
    } 
    this.AllSoftConstraints = array.<SoftConstraint>toArray(this.AllSoftConstraints);
  }
  
  public String toString() {
    return getName();
  }
}
