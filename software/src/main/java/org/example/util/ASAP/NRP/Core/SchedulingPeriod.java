package org.example.util.ASAP.NRP.Core;

import org.example.util.ASAP.NRP.Core.Constraints.QMC.ShiftGroupRequest;
import org.example.util.ASAP.NRP.Core.Parsers.Cover;
import org.example.util.ASAP.NRP.Core.Parsers.CoverSpecification;
import java.util.ArrayList;
import java.util.Hashtable;

public class SchedulingPeriod {
  ShiftType[] ShiftTypes = new ShiftType[0];
  
  EmployeeDescription[] EmployeeDescriptions = new EmployeeDescription[0];
  
  ShiftGroup[] ShiftGroups = new ShiftGroup[0];
  
  SkillGroup[] SkillGroups = new SkillGroup[0];
  
  DayPeriod[] DayPeriods = new DayPeriod[0];
  
  Hashtable<String, EmployeeDescription> EmployeeDescriptionsHash = new Hashtable<String, EmployeeDescription>();
  
  Hashtable<String, ShiftType> ShiftTypesHash = new Hashtable<String, ShiftType>();
  
  Hashtable<String, ShiftGroup> ShiftGroupsHash = new Hashtable<String, ShiftGroup>();
  
  Hashtable<String, SkillGroup> SkillGroupsHash = new Hashtable<String, SkillGroup>();
  
  Hashtable<String, BankHoliday> BankHolidaysHash = new Hashtable<String, BankHoliday>();
  
  Hashtable<String, Skill> SkillsHash = new Hashtable<String, Skill>();
  
  ArrayList<ShiftType> ShiftTypesArrayList = new ArrayList<ShiftType>();
  
  ArrayList<Skill> SkillsArrayList = new ArrayList<Skill>();
  
  public String OrganisationID = "";
  
  public String DepartmentID = null;
  
  public String FilePath = null;
  
  public DateTime StartDate;
  
  public DateTime EndDate;
  
  public MasterWeights MasterWeights = null;
  
  public int NumDaysInPeriod = 0;
  
  public int FirstSunday = -1;
  
  public int FirstMonday = -1;
  
  public int FirstTuesday = -1;
  
  public int FirstWednesday = -1;
  
  public int FirstThursday = -1;
  
  public int FirstFriday = -1;
  
  public int FirstSaturday = -1;
  
  public int EmployeesCount = 0;
  
  public int NumWeekendsInPeriod = 0;
  
  public int ShiftTypesCount = 0;
  
  public int ShiftGroupsCount = 0;
  
  public int SkillGroupsCount = 0;
  
  public int NumWeeksInPeriod = 0;
  
  public int SkillsCount = 0;
  
  public int MondayPatternIndex;
  
  public int TuesdayPatternIndex;
  
  public int WednesdayPatternIndex;
  
  public int ThursdayPatternIndex;
  
  public int FridayPatternIndex;
  
  public int SaturdayPatternIndex;
  
  public int SundayPatternIndex;
  
  public boolean[] BankHolidayArray;
  
  int[][] sameDayOverlappingMinutes;
  
  int[][] overlappingMinutes;
  
  boolean[][] employeeHasSkillsForShiftType;
  
  boolean[][] employeeUsesSecondarySkillForShiftType;
  
  boolean[][] employeeHasSkillForCover;
  
  public CoverRequirements CoverRequirements;
  
  int[][] shiftGroupsContainingShift;
  
  boolean[][] shiftGroupContainsShiftType;
  
  public boolean ShiftsRequireSkills = false;
  
  public boolean ContainsInitialAssignments = false;
  
  public String SchedulingPeriodID = "";
  
  public boolean HistoryLoadedFromSchedulingPeriodFile = false;
  
  public SchedulingPeriod(DateTime startDate, DateTime endDate) {
    this.StartDate = startDate;
    this.EndDate = endDate;
    this.MasterWeights = new MasterWeights();
    this.CoverRequirements = new CoverRequirements(this);
    CalculateNumDaysInPeriod();
    this.BankHolidayArray = new boolean[this.NumDaysInPeriod];
    CalculateFirstDays();
    CalculateNumWeekendsInPeriod();
    CalculateNumWeeksInPeriod();
    CalculatePatternDays();
  }
  
  public int getBankHolidayCount() {
    return this.BankHolidaysHash.size();
  }
  
  public int getDayPeriodsCount() {
    return this.DayPeriods.length;
  }
  
  public boolean AddBankHoliday(BankHoliday bankHoliday) {
    String ID = bankHoliday.ID;
    if (this.BankHolidaysHash.containsKey(ID)) {
      System.out.println("Unable to add BankHoliday : " + ID + " as BankHoliday with same ID already exists.");
      return false;
    } 
    if (!IsDateWithinSchedulingPeriod(bankHoliday.Date)) {
      System.out.println("Unable to add BankHoliday : " + ID + " as the BankHoliday's date is not within the schedulingPeriod's range.");
      return false;
    } 
    this.BankHolidaysHash.put(ID, bankHoliday);
    int day = ConvertDateToRosterDay(bankHoliday.Date);
    if (this.BankHolidayArray[day]) {
      System.out.println("Warning: There appears to be more than one bank holiday defined for day: " + bankHoliday.Date);
    } else {
      this.BankHolidayArray[day] = true;
    } 
    return true;
  }
  
  public boolean AddEmployeeDescription(EmployeeDescription employeeDescription) {
    String employeeDescriptionID = employeeDescription.ID;
    if (this.EmployeeDescriptionsHash.containsKey(employeeDescriptionID)) {
      System.out.println("Unable to add Employee : " + employeeDescriptionID + " as employeeDescription with same ID already exists.");
      return false;
    } 
    this.EmployeeDescriptionsHash.put(employeeDescriptionID, employeeDescription);
    employeeDescription.IndexID = this.EmployeeDescriptionsHash.size() - 1;
    this.EmployeesCount = this.EmployeeDescriptionsHash.size();
    this.EmployeeDescriptions = CSharpConversionHelper.ArrayResize(this.EmployeeDescriptions, this.EmployeeDescriptions.length + 1);
    this.EmployeeDescriptions[this.EmployeeDescriptions.length - 1] = employeeDescription;
    return true;
  }
  
  public String GetNewSkillID() {
    for (int i = 0; i < 100000; i++) {
      String key = Integer.toString(i);
      if (!this.SkillsHash.containsKey(key))
        return key; 
    } 
    System.out.println("Warning : Unable to generate new Skill ID");
    return "";
  }
  
  public void DeleteShiftType(ShiftType shiftType) {
    if (shiftType == null)
      return; 
    String shiftTypeID = shiftType.ID;
    if (!this.ShiftTypesHash.contains(shiftTypeID))
      return; 
    this.ShiftTypesCount--;
    this.ShiftTypesHash.remove(shiftTypeID);
    this.ShiftTypesArrayList.remove(shiftType);
    ShiftTypesChanged();
  }
  
  public void DeleteSkill(Skill skill) {
    if (skill == null || !this.SkillsHash.contains(skill.ID))
      return; 
    this.SkillsArrayList.remove(skill);
    this.SkillsHash.remove(skill.ID);
    this.SkillsCount--;
    byte b;
    int i;
    EmployeeDescription[] arrayOfEmployeeDescription;
    for (i = (arrayOfEmployeeDescription = this.EmployeeDescriptions).length, b = 0; b < i; ) {
      EmployeeDescription employee = arrayOfEmployeeDescription[b];
      employee.DeleteSkill(skill);
      b++;
    } 
    boolean skillsRequired = false;
    ShiftType[] arrayOfShiftType;
    for (int j = (arrayOfShiftType = this.ShiftTypes).length; i < j; ) {
      ShiftType shiftType = arrayOfShiftType[i];
      int count = shiftType.DeleteSkill(skill);
      if (count > 0)
        skillsRequired = true; 
      i++;
    } 
    this.ShiftsRequireSkills = skillsRequired;
  }
  
  public EmployeeDescription GetEmployeeDescription(String employeeDescriptionID) {
    Object obj = this.EmployeeDescriptionsHash.get(employeeDescriptionID);
    if (obj == null)
      return null; 
    return (EmployeeDescription)obj;
  }
  
  public EmployeeDescription GetEmployeeDescription(int index) {
    return this.EmployeeDescriptions[index];
  }
  
  public boolean AddShiftType(ShiftType shiftType) {
    String shiftTypeID = shiftType.ID;
    if (this.ShiftTypesHash.containsKey(shiftTypeID)) {
      System.out.println("Unable to add ShiftType : " + shiftTypeID + " as shift with same ID already exists.");
      return false;
    } 
    shiftType.Index = this.ShiftTypesCount;
    this.ShiftTypesCount++;
    this.ShiftTypesHash.put(shiftTypeID, shiftType);
    this.ShiftTypesArrayList.add(shiftType);
    ShiftTypesChanged();
    return true;
  }
  
  private void ShiftTypesChanged() {
    SortShiftTypes();
    this.ShiftTypes = this.ShiftTypesArrayList.<ShiftType>toArray(this.ShiftTypes);
    this.sameDayOverlappingMinutes = new int[this.ShiftTypesCount][this.ShiftTypesCount];
    this.overlappingMinutes = new int[this.ShiftTypesCount][this.ShiftTypesCount];
    for (int i = 0; i < this.ShiftTypesCount; i++) {
      for (int j = 0; j < this.ShiftTypesCount; j++) {
        this.sameDayOverlappingMinutes[i][j] = ShiftType.OverlappingMinutes(this.ShiftTypes[i], this.ShiftTypes[j], true);
        this.overlappingMinutes[i][j] = ShiftType.OverlappingMinutes(this.ShiftTypes[i], this.ShiftTypes[j], false);
      } 
    } 
  }
  
  public boolean AddShiftGroup(ShiftGroup shiftGroup) {
    String ID = shiftGroup.ID;
    if (this.ShiftGroupsHash.containsKey(ID)) {
      System.out.println("Unable to add ShiftGroup : " + ID + " as a ShiftGroup with the same ID already exists.");
      return false;
    } 
    shiftGroup.Index = this.ShiftGroupsCount;
    this.ShiftGroupsCount++;
    this.ShiftGroupsHash.put(ID, shiftGroup);
    this.ShiftGroups = CSharpConversionHelper.ArrayResize(this.ShiftGroups, this.ShiftGroups.length + 1);
    this.ShiftGroups[this.ShiftGroups.length - 1] = shiftGroup;
    return true;
  }
  
  public boolean AddSkill(Skill skill) {
    for (Skill skill2 : this.SkillsArrayList) {
      if (skill.ID.equals(skill2.ID)) {
        System.out.println("Unable to add Skill : " + skill.ID + " as a Skill with the same ID already exists.");
        return false;
      } 
    } 
    this.SkillsCount++;
    this.SkillsHash.put(skill.ID, skill);
    this.SkillsArrayList.add(skill);
    return true;
  }
  
  public boolean AddSkillGroup(SkillGroup skillGroup) {
    String ID = skillGroup.ID;
    if (this.SkillGroupsHash.containsKey(ID)) {
      System.out.println("Unable to add SkillGroup : " + ID + " as a SkillGroup with the same ID already exists.");
      return false;
    } 
    skillGroup.Index = this.SkillGroupsCount;
    this.SkillGroupsCount++;
    this.SkillGroupsHash.put(ID, skillGroup);
    this.SkillGroups = CSharpConversionHelper.ArrayResize(this.SkillGroups, this.SkillGroups.length + 1);
    this.SkillGroups[this.SkillGroups.length - 1] = skillGroup;
    return true;
  }
  
  private void SortShiftTypes() {
    int i;
    for (i = 0; i < this.ShiftTypesCount; i++) {
      for (int j = 1; j < this.ShiftTypesCount - i; j++) {
        ShiftType type1 = this.ShiftTypesArrayList.get(j - 1);
        ShiftType type2 = this.ShiftTypesArrayList.get(j);
        if (type2.getStartTime().isLessThan(type1.getStartTime()) || (
          type2.getStartTime().getTicks() == type1.getStartTime().getTicks() && 
          type2.getDuration() < type1.getDuration())) {
          this.ShiftTypesArrayList.set(j, type1);
          this.ShiftTypesArrayList.set(j - 1, type2);
        } 
      } 
    } 
    for (i = 0; i < this.ShiftTypesCount; i++)
      ((ShiftType)this.ShiftTypesArrayList.get(i)).Index = i; 
  }
  
  public ShiftType GetShiftType(String shiftTypeID) {
    Object obj = this.ShiftTypesHash.get(shiftTypeID);
    if (obj == null)
      return null; 
    return (ShiftType)obj;
  }
  
  public ShiftType GetShiftType(int index) {
    return this.ShiftTypes[index];
  }
  
  public ShiftType GetShiftTypeByLabel(String label) {
    byte b;
    int i;
    ShiftType[] arrayOfShiftType;
    for (i = (arrayOfShiftType = this.ShiftTypes).length, b = 0; b < i; ) {
      ShiftType st = arrayOfShiftType[b];
      if (st.Label == label)
        return st; 
      b++;
    } 
    return null;
  }
  
  public ShiftGroup GetShiftGroup(String ID) {
    return this.ShiftGroupsHash.get(ID);
  }
  
  public ShiftGroup GetShiftGroup(int index) {
    return this.ShiftGroups[index];
  }
  
  public ShiftGroup GetShiftGroup(ShiftType[] shifts) {
    for (int i = 0; i < this.ShiftGroups.length; i++) {
      ShiftGroup grp = this.ShiftGroups[i];
      if (grp.Group.length == shifts.length) {
        boolean match = true;
        for (int j = 0; j < shifts.length; j++) {
          if (!grp.Contains((shifts[j]).ID)) {
            match = false;
            break;
          } 
        } 
        if (match)
          return grp; 
      } 
    } 
    return null;
  }
  
  public Skill GetSkill(String ID) {
    return this.SkillsHash.get(ID);
  }
  
  public SkillGroup GetSkillGroup(String ID) {
    return this.SkillGroupsHash.get(ID);
  }
  
  public SkillGroup GetSkillGroup(int index) {
    return this.SkillGroups[index];
  }
  
  public DayPeriod GetDayPeriod(Period period) {
    for (int i = 0; i < this.DayPeriods.length; i++) {
      if ((this.DayPeriods[i]).Period.equals(period))
        return this.DayPeriods[i]; 
    } 
    return null;
  }
  
  public DayPeriod GetDayPeriod(int index) {
    return this.DayPeriods[index];
  }
  
  public Skill GetSkill(int index) {
    return this.SkillsArrayList.get(index);
  }
  
  public int OverlappingMinutes(ShiftType shiftType1, ShiftType shiftType2, boolean sameDay) {
    if (sameDay)
      return this.sameDayOverlappingMinutes[shiftType1.Index][shiftType2.Index]; 
    return this.overlappingMinutes[shiftType1.Index][shiftType2.Index];
  }
  
  private void CalculatePatternDays() {
    switch (this.StartDate.getDayOfWeek()) {
      case 2:
        this.MondayPatternIndex = 0;
        this.TuesdayPatternIndex = 1;
        this.WednesdayPatternIndex = 2;
        this.ThursdayPatternIndex = 3;
        this.FridayPatternIndex = 4;
        this.SaturdayPatternIndex = 5;
        this.SundayPatternIndex = 6;
        return;
      case 3:
        this.MondayPatternIndex = 6;
        this.TuesdayPatternIndex = 0;
        this.WednesdayPatternIndex = 1;
        this.ThursdayPatternIndex = 2;
        this.FridayPatternIndex = 3;
        this.SaturdayPatternIndex = 4;
        this.SundayPatternIndex = 5;
        return;
      case 4:
        this.MondayPatternIndex = 5;
        this.TuesdayPatternIndex = 6;
        this.WednesdayPatternIndex = 0;
        this.ThursdayPatternIndex = 1;
        this.FridayPatternIndex = 2;
        this.SaturdayPatternIndex = 3;
        this.SundayPatternIndex = 4;
        return;
      case 5:
        this.MondayPatternIndex = 4;
        this.TuesdayPatternIndex = 5;
        this.WednesdayPatternIndex = 6;
        this.ThursdayPatternIndex = 0;
        this.FridayPatternIndex = 1;
        this.SaturdayPatternIndex = 2;
        this.SundayPatternIndex = 3;
        return;
      case 6:
        this.MondayPatternIndex = 3;
        this.TuesdayPatternIndex = 4;
        this.WednesdayPatternIndex = 5;
        this.ThursdayPatternIndex = 6;
        this.FridayPatternIndex = 0;
        this.SaturdayPatternIndex = 1;
        this.SundayPatternIndex = 2;
        return;
      case 7:
        this.MondayPatternIndex = 2;
        this.TuesdayPatternIndex = 3;
        this.WednesdayPatternIndex = 4;
        this.ThursdayPatternIndex = 5;
        this.FridayPatternIndex = 6;
        this.SaturdayPatternIndex = 0;
        this.SundayPatternIndex = 1;
        return;
      case 1:
        this.MondayPatternIndex = 1;
        this.TuesdayPatternIndex = 2;
        this.WednesdayPatternIndex = 3;
        this.ThursdayPatternIndex = 4;
        this.FridayPatternIndex = 5;
        this.SaturdayPatternIndex = 6;
        this.SundayPatternIndex = 0;
        return;
    } 
    this.MondayPatternIndex = 0;
    this.TuesdayPatternIndex = 1;
    this.WednesdayPatternIndex = 2;
    this.ThursdayPatternIndex = 3;
    this.FridayPatternIndex = 4;
    this.SaturdayPatternIndex = 5;
    this.SundayPatternIndex = 6;
  }
  
  private void CalculateFirstDays() {
    this.FirstSunday = -1;
    this.FirstMonday = -1;
    this.FirstTuesday = -1;
    this.FirstWednesday = -1;
    this.FirstThursday = -1;
    this.FirstFriday = -1;
    this.FirstSaturday = -1;
    for (int i = 0; i < 7 && i < this.NumDaysInPeriod; i++) {
      switch (this.StartDate.AddDays(i).getDayOfWeek()) {
        case 2:
          this.FirstMonday = i;
          break;
        case 3:
          this.FirstTuesday = i;
          break;
        case 4:
          this.FirstWednesday = i;
          break;
        case 5:
          this.FirstThursday = i;
          break;
        case 6:
          this.FirstFriday = i;
          break;
        case 7:
          this.FirstSaturday = i;
          break;
        case 1:
          this.FirstSunday = i;
          break;
      } 
    } 
  }
  
  private void CalculateNumWeekendsInPeriod() {
    CalculateFirstDays();
    this.NumWeekendsInPeriod = 0;
    int saturdayIndex = this.FirstSaturday;
    if (saturdayIndex < 0)
      return; 
    while (saturdayIndex < this.NumDaysInPeriod) {
      saturdayIndex += 7;
      this.NumWeekendsInPeriod++;
    } 
  }
  
  private void CalculateNumWeeksInPeriod() {
    double num = this.NumDaysInPeriod / 7.0D;
    this.NumWeeksInPeriod = (int)Math.ceil(num);
  }
  
  private void CalculateNumDaysInPeriod() {
    TimeSpan diff = this.EndDate.subtract(this.StartDate);
    this.NumDaysInPeriod = (int)Math.ceil(diff.TotalDays);
  }
  
  public boolean IsDateWithinSchedulingPeriod(DateTime date) {
    if (DateTime.compare(this.StartDate, date) > 0L)
      return false; 
    if (DateTime.compare(date, this.EndDate.AddHours(-24)) > 0L)
      return false; 
    return true;
  }
  
  public boolean IsDayWithinSchedulingPeriod(int day) {
    return (day >= 0 && day < this.NumDaysInPeriod);
  }
  
  public int ConvertDateToRosterDay(DateTime date) {
    TimeSpan diff = date.subtract(this.StartDate);
    int day = (int)Math.floor(diff.TotalDays);
    return day;
  }
  
  public double TestConvertDateToRosterDay(DateTime date) {
    TimeSpan diff = date.subtract(this.StartDate);
    return diff.TotalDays;
  }
  
  public int ConvertDateToWeek(DateTime date) {
    TimeSpan diff = date.subtract(this.StartDate);
    if (diff.TotalDays <= 0.0D)
      return 1; 
    double week = diff.TotalDays / 7.0D;
    int w = (int)Math.floor(week);
    return w + 1;
  }
  
  public DateTime ConvertRosterDayToDate(int day) {
    return this.StartDate.AddDays(day);
  }
  
  public boolean RequestDaysOffBetweenDates(String employeeID, int weight, DateTime StartDate, DateTime EndDate, int MinimumDaysOff, int MaximumDaysOff, boolean holiday) {
    if (weight <= 0)
      return false; 
    EmployeeDescription employee = GetEmployeeDescription(employeeID);
    if (employee == null) {
      System.out.println("Unable to request days off. Unknown employee: " + employeeID);
      return false;
    } 
    if (!IsDateWithinSchedulingPeriod(StartDate)) {
      System.out.println("Unable to request days off. Date : " + StartDate + " out of scheduling range.");
      return false;
    } 
    if (!IsDateWithinSchedulingPeriod(EndDate)) {
      System.out.println("Unable to request days off. Date : " + EndDate + " out of scheduling range.");
      return false;
    } 
    int start = ConvertDateToRosterDay(StartDate);
    int end = ConvertDateToRosterDay(EndDate);
    DaysOffRequestBetweenDates request = new DaysOffRequestBetweenDates(start, end, MinimumDaysOff, MaximumDaysOff, weight);
    employee.AddDaysOffRequestBetweenDates(request);
    return true;
  }
  
  public boolean RequestDayOff(String employeeID, int day, DateTime date, int weight, boolean holiday, boolean working) {
    if (weight <= 0)
      return false; 
    EmployeeDescription employee = GetEmployeeDescription(employeeID);
    if (employee == null) {
      System.out.println("Unable to request day off. Unknown employee: " + employeeID);
      return false;
    } 
    if (employee.DayOffRequests[day] == 0 && weight > 0) {
      employee.DayOffRequestObjs.add(new DayOffRequest(date, weight, working, holiday));
      employee.DayOffRequestCount++;
    } 
    employee.DayOffRequests[day] = weight;
    employee.DayOffRequestIsHoliday[day] = holiday;
    employee.DayOffRequestIsWork[day] = working;
    return true;
  }
  
  public boolean RequestDayOn(String employeeID, int day, DateTime date, int weight) {
    if (weight <= 0)
      return false; 
    EmployeeDescription employee = GetEmployeeDescription(employeeID);
    if (employee == null) {
      System.out.println("Unable to request day on. Unknown employee: " + employeeID);
      return false;
    } 
    if (employee.DayOnRequests[day] == 0 && weight > 0) {
      employee.DayOnRequestCount++;
      employee.DayOnRequestObjs.add(new DayOnRequest(date, weight));
    } 
    employee.DayOnRequests[day] = weight;
    return true;
  }
  
  public boolean RequestShiftOff(String employeeID, String shiftTypeID, int day, DateTime date, int weight) {
    if (weight <= 0)
      return false; 
    EmployeeDescription employee = GetEmployeeDescription(employeeID);
    if (employee == null) {
      System.out.println("Unable to request shift off. Unknown Employee: " + employeeID);
      return false;
    } 
    ShiftType shiftType = GetShiftType(shiftTypeID);
    if (shiftType == null) {
      System.out.println("Unable to request shift off. Unknown ShiftType: " + shiftTypeID);
      return false;
    } 
    int index = day * this.ShiftTypesCount + shiftType.Index;
    if (employee.ShiftOffRequests[index] == 0 && weight > 0) {
      employee.ShiftOffRequestCount++;
      employee.ShiftOffRequestObjs.add(new ShiftRequest(date, shiftType, weight));
    } 
    employee.ShiftOffRequests[index] = weight;
    return true;
  }
  
  public boolean RequestShiftOn(String employeeID, String shiftTypeID, int day, DateTime date, int weight) {
    if (weight <= 0)
      return false; 
    EmployeeDescription employee = GetEmployeeDescription(employeeID);
    if (employee == null) {
      System.out.println("Unable to request shift on. Unknown Employee: " + employeeID);
      return false;
    } 
    ShiftType shiftType = GetShiftType(shiftTypeID);
    if (shiftType == null) {
      System.out.println("Unable to request shift on. Unknown ShiftType: " + shiftTypeID);
      return false;
    } 
    int index = day * this.ShiftTypesCount + shiftType.Index;
    if (employee.ShiftOnRequests[index] == 0 && weight > 0) {
      employee.ShiftOnRequestCount++;
      employee.ShiftOnRequestObjs.add(new ShiftRequest(date, shiftType, weight));
    } 
    employee.ShiftOnRequests[index] = weight;
    return true;
  }
  
  public boolean RequestShiftGroupOn(String employeeID, String shiftGroupID, int day, DateTime date, int weight) {
    EmployeeDescription employee = GetEmployeeDescription(employeeID);
    if (employee == null) {
      System.out.println("Unable to request shift group on. Unknown Employee: " + employeeID);
      return false;
    } 
    ShiftGroup shiftGroup = GetShiftGroup(shiftGroupID);
    if (shiftGroup == null) {
      System.out.println("Unable to request shift group on. Unknown ShiftGroup: " + shiftGroupID);
      return false;
    } 
    ShiftGroupRequest request = new ShiftGroupRequest(shiftGroup, day, date, weight);
    employee.AddShiftGroupOnRequest(request);
    return true;
  }
  
  public boolean RequestShiftGroupOn(String employeeID, ArrayList<ShiftType> shifts, int day, DateTime date, int weight) {
    if (shifts.size() == 0)
      return false; 
    EmployeeDescription employee = GetEmployeeDescription(employeeID);
    if (employee == null) {
      System.out.println("Unable to request shift group on. Unknown Employee: " + employeeID);
      return false;
    } 
    ShiftGroup shiftGroup = new ShiftGroup("");
    for (ShiftType s : shifts)
      shiftGroup.AddShiftType(s, false); 
    ShiftGroupRequest request = new ShiftGroupRequest(shiftGroup, day, date, weight);
    RequestShiftGroupOn(employee, request);
    return true;
  }
  
  public void RequestShiftGroupOn(EmployeeDescription employee, ShiftGroupRequest request) {
    employee.AddShiftGroupOnRequest(request);
  }
  
  public boolean RequestCollaboration(String employee1ID, String employee2ID) {
    EmployeeDescription employee1 = GetEmployeeDescription(employee1ID);
    EmployeeDescription employee2 = GetEmployeeDescription(employee2ID);
    if (employee1 == null) {
      System.out.println("Unable to request collaboration. Unknown Employee: " + employee1ID);
      return false;
    } 
    if (employee2 == null) {
      System.out.println("Unable to request collaboration. Unknown Employee: " + employee2ID);
      return false;
    } 
    employee2.AddTutor(employee1);
    return true;
  }
  
  public boolean RequestSeparation(String employee1ID, String employee2ID) {
    EmployeeDescription employee1 = GetEmployeeDescription(employee1ID);
    EmployeeDescription employee2 = GetEmployeeDescription(employee2ID);
    if (employee1 == null) {
      System.out.println("Unable to request collaboration. Unknown Employee: " + employee1ID);
      return false;
    } 
    if (employee2 == null) {
      System.out.println("Unable to request collaboration. Unknown Employee: " + employee2ID);
      return false;
    } 
    employee1.AvoidPartnership(employee2);
    return true;
  }
  
  public void ParsingFinished() {
    byte b;
    int m;
    ShiftType[] arrayOfShiftType;
    for (m = (arrayOfShiftType = this.ShiftTypes).length, b = 0; b < m; ) {
      ShiftType shiftType = arrayOfShiftType[b];
      if (shiftType.RequiresSkills) {
        this.ShiftsRequireSkills = true;
        break;
      } 
      b++;
    } 
    ArrayList<Period> periods = new ArrayList<Period>();
    for (CoverSpecification spec : this.CoverRequirements.GetCoverSpecifications()) {
      for (Cover cover : spec.CoverRequirements) {
        if (cover.Type == Cover.CoverType.Period)
          if (!periods.contains(cover.Period))
            periods.add(cover.Period);  
      } 
    } 
    boolean splitFound = true;
    while (splitFound) {
      splitFound = false;
      for (int n = 0; n < this.ShiftTypesCount; n++) {
        ShiftType s = GetShiftType(n);
        int removeIndex = -1;
        for (int i1 = 0; i1 < periods.size(); i1++) {
          Period p = periods.get(i1);
          DateTime pStartTime = p.getStartTime();
          DateTime pEndTime = p.getEndTime();
          if (s.getStartTime().isLessThanOrEqual(pStartTime) && s.getEndTime().isGreaterThan(pStartTime) && 
            s.getEndTime().isLessThan(pEndTime)) {
            Period p1 = new Period(pStartTime, s.getEndTime());
            Period p2 = new Period(s.getEndTime(), pEndTime);
            if (!periods.contains(p1))
              periods.add(p1); 
            if (!periods.contains(p2))
              periods.add(p2); 
            removeIndex = i1;
            splitFound = true;
            break;
          } 
          if (s.getStartTime().isGreaterThan(pStartTime) && s.getStartTime().isLessThan(pEndTime) && 
            s.getEndTime().isGreaterThanOrEqual(pEndTime)) {
            Period p1 = new Period(pStartTime, s.getStartTime());
            Period p2 = new Period(s.getStartTime(), pEndTime);
            if (!periods.contains(p1))
              periods.add(p1); 
            if (!periods.contains(p2))
              periods.add(p2); 
            removeIndex = i1;
            splitFound = true;
            break;
          } 
          if (s.getStartTime().isGreaterThan(pStartTime) && s.getStartTime().isLessThan(pEndTime) && 
            s.getEndTime().isLessThan(pEndTime)) {
            Period p1 = new Period(pStartTime, s.getStartTime());
            Period p2 = new Period(s.getStartTime(), s.getEndTime());
            Period p3 = new Period(s.getEndTime(), pEndTime);
            if (!periods.contains(p1))
              periods.add(p1); 
            if (!periods.contains(p2))
              periods.add(p2); 
            if (!periods.contains(p3))
              periods.add(p3); 
            removeIndex = i1;
            splitFound = true;
            break;
          } 
          if (s.getSpansMidnight()) {
            pStartTime = p.getStartTime().AddHours(24);
            pEndTime = p.getEndTime().AddHours(24);
            if (s.getStartTime().isLessThanOrEqual(pStartTime) && s.getEndTime().isGreaterThan(pStartTime) && 
              s.getEndTime().isLessThan(pEndTime)) {
              Period p1 = new Period(pStartTime, s.getEndTime());
              Period p2 = new Period(s.getEndTime(), pEndTime);
              if (!periods.contains(p1))
                periods.add(p1); 
              if (!periods.contains(p2))
                periods.add(p2); 
              removeIndex = i1;
              splitFound = true;
              break;
            } 
            if (s.getStartTime().isGreaterThan(pStartTime) && s.getStartTime().isLessThan(pEndTime) && 
              s.getEndTime().isGreaterThanOrEqual(pEndTime)) {
              Period p1 = new Period(pStartTime, s.getStartTime());
              Period p2 = new Period(s.getStartTime(), pEndTime);
              if (!periods.contains(p1))
                periods.add(p1); 
              if (!periods.contains(p2))
                periods.add(p2); 
              removeIndex = i1;
              splitFound = true;
              break;
            } 
            if (s.getStartTime().isGreaterThan(pStartTime) && s.getStartTime().isLessThan(pEndTime) && 
              s.getEndTime().isLessThan(pEndTime)) {
              Period p1 = new Period(pStartTime, s.getStartTime());
              Period p2 = new Period(s.getStartTime(), s.getEndTime());
              Period p3 = new Period(s.getEndTime(), pEndTime);
              if (!periods.contains(p1))
                periods.add(p1); 
              if (!periods.contains(p2))
                periods.add(p2); 
              if (!periods.contains(p3))
                periods.add(p3); 
              removeIndex = i1;
              splitFound = true;
              break;
            } 
          } 
        } 
        if (splitFound) {
          periods.remove(removeIndex);
          break;
        } 
      } 
    } 
    for (CoverSpecification spec : this.CoverRequirements.GetCoverSpecifications()) {
      ArrayList<Cover> oldArray = spec.CoverRequirements;
      ArrayList<Cover> newArray = new ArrayList<Cover>();
      for (int n = 0; n < oldArray.size(); n++) {
        Cover cover = oldArray.get(n);
        if (cover.Type == Cover.CoverType.Period) {
          boolean match = false;
          for (Period p : periods) {
            if (p.getStartTime().isGreaterThanOrEqual(cover.Period.getStartTime()) && 
              p.getEndTime().isLessThanOrEqual(cover.Period.getEndTime())) {
              Cover cover2 = (Cover)cover.Clone();
              cover2.Period = p;
              newArray.add(cover2);
              match = true;
              continue;
            } 
            if (cover.Period.SpansMidnight)
              if (p.getStartTime().AddHours(24).isGreaterThanOrEqual(cover.Period.getStartTime()) && 
                p.getEndTime().AddHours(24).isLessThanOrEqual(cover.Period.getEndTime())) {
                Cover cover2 = (Cover)cover.Clone();
                cover2.Period = p;
                newArray.add(cover2);
                match = true;
              }  
          } 
          if (!match)
            newArray.add(cover); 
        } else {
          newArray.add(cover);
        } 
      } 
      spec.CoverRequirements.clear();
      spec.CoverRequirements.addAll(newArray);
    } 
    this.DayPeriods = new DayPeriod[periods.size()];
    int k;
    for (k = 0; k < periods.size(); k++) {
      Period period = periods.get(k);
      this.DayPeriods[k] = new DayPeriod(k, period);
    } 
    for (k = 0; k < this.ShiftTypesCount; k++) {
      ShiftType shiftType = GetShiftType(k);
      for (int n = 0; n < this.DayPeriods.length; n++) {
        DayPeriod period = this.DayPeriods[n];
        if (period.getStart().isGreaterThanOrEqual(shiftType.getStartTime()) && 
          period.getEnd().isLessThanOrEqual(shiftType.getEndTime()))
          shiftType.AddPeriod(n); 
        if (shiftType.getSpansMidnight())
          if (period.getStart().AddHours(24).isGreaterThanOrEqual(shiftType.getStartTime()) && 
            period.getEnd().AddHours(24).isLessThanOrEqual(shiftType.getEndTime()))
            shiftType.AddNextDayPeriod(n);  
      } 
    } 
    this.CoverRequirements.UpdateCoverRequirementArrays();
    this.employeeHasSkillsForShiftType = new boolean[this.EmployeesCount][this.ShiftTypesCount];
    this.employeeUsesSecondarySkillForShiftType = new boolean[this.EmployeesCount][this.ShiftTypesCount];
    this.employeeHasSkillForCover = new boolean[this.EmployeesCount][this.CoverRequirements.Requirements.length];
    this.shiftGroupsContainingShift = new int[this.ShiftTypesCount][];
    this.shiftGroupContainsShiftType = new boolean[this.ShiftGroupsCount][this.ShiftTypesCount];
    int j;
    for (j = 0; j < this.EmployeesCount; j++) {
      EmployeeDescription employeeDescription = GetEmployeeDescription(j);
      for (int n = 0; n < this.ShiftTypes.length; n++) {
        ShiftType shiftType = this.ShiftTypes[n];
        if (shiftType.RequiresSkills) {
          boolean hasSkills = true;
          for (String str : shiftType.GetSkills()) {
            if (!employeeDescription.HasSkillType(str, EmployeeDescription.SkillType.Primary) && 
              !employeeDescription.HasSkillType(str, EmployeeDescription.SkillType.Secondary)) {
              hasSkills = false;
              break;
            } 
          } 
          this.employeeHasSkillsForShiftType[employeeDescription.IndexID][shiftType.Index] = hasSkills;
        } else {
          this.employeeHasSkillsForShiftType[employeeDescription.IndexID][shiftType.Index] = true;
        } 
      } 
    } 
    for (j = 0; j < this.EmployeesCount; j++) {
      EmployeeDescription employeeDescription = GetEmployeeDescription(j);
      for (int n = 0; n < this.ShiftTypes.length; n++) {
        ShiftType shiftType = this.ShiftTypes[n];
        if (shiftType.RequiresSkills) {
          boolean usesSecSkill = false;
          for (String str : shiftType.GetSkills()) {
            if (employeeDescription.HasSkillType(str, EmployeeDescription.SkillType.Secondary)) {
              usesSecSkill = true;
              break;
            } 
          } 
          this.employeeUsesSecondarySkillForShiftType[employeeDescription.IndexID][shiftType.Index] = usesSecSkill;
        } else {
          this.employeeUsesSecondarySkillForShiftType[employeeDescription.IndexID][shiftType.Index] = false;
        } 
      } 
    } 
    for (j = 0; j < this.EmployeesCount; j++) {
      EmployeeDescription employeeDescription = GetEmployeeDescription(j);
      for (int n = 0; n < this.CoverRequirements.Requirements.length; n++) {
        CoverRequirement req = this.CoverRequirements.Requirements[n];
        if (req.SkillType == CoverRequirement.SkillTypes.AnySkill) {
          this.employeeHasSkillForCover[employeeDescription.IndexID][req.Index] = true;
        } else if (req.SkillType == CoverRequirement.SkillTypes.SingleSkill) {
          if (employeeDescription.HasSkillType(req.SkillID, EmployeeDescription.SkillType.Primary) || 
            employeeDescription.HasSkillType(req.SkillID, EmployeeDescription.SkillType.Secondary))
            this.employeeHasSkillForCover[employeeDescription.IndexID][req.Index] = true; 
        } else if (req.SkillType == CoverRequirement.SkillTypes.SkillGroup) {
          SkillGroup grp = GetSkillGroup(req.SkillID);
          for (int i1 = 0; i1 < grp.Skills.length; i1++) {
            String skillID = (grp.Skills[i1]).ID;
            if (employeeDescription.HasSkillType(skillID, EmployeeDescription.SkillType.Primary) || 
              employeeDescription.HasSkillType(skillID, EmployeeDescription.SkillType.Secondary)) {
              this.employeeHasSkillForCover[employeeDescription.IndexID][req.Index] = true;
              break;
            } 
          } 
        } 
      } 
    } 
    for (j = 0; j < this.ShiftTypesCount; j++) {
      ShiftType shiftType = this.ShiftTypes[j];
      ArrayList<Integer> grps = new ArrayList<Integer>();
      for (int y = 0; y < this.ShiftGroupsCount; y++) {
        ShiftGroup shiftGroup = this.ShiftGroups[y];
        for (int i1 = 0; i1 < shiftGroup.Group.length; i1++) {
          if (shiftType.Index == (shiftGroup.Group[i1]).Index) {
            grps.add(new Integer(shiftGroup.Index));
            break;
          } 
        } 
      } 
      this.shiftGroupsContainingShift[shiftType.Index] = new int[grps.size()];
      for (int n = 0; n < grps.size(); n++)
        this.shiftGroupsContainingShift[shiftType.Index][n] = ((Integer)grps.get(n)).intValue(); 
    } 
    for (int i = 0; i < this.ShiftGroupsCount; i++) {
      ShiftGroup grp = this.ShiftGroups[i];
      for (int n = 0; n < grp.Group.length; n++) {
        ShiftType st = grp.Group[n];
        this.shiftGroupContainsShiftType[i][st.Index] = true;
      } 
    } 
    for (int x = 0; x < this.EmployeesCount; x++) {
      EmployeeDescription employeeDescription = GetEmployeeDescription(x);
      employeeDescription.CreateSoftConstraints();
    } 
  }
  
  public boolean EmployeeHasSkillsForShiftType(EmployeeDescription employeeDescription, int shiftTypeIndex) {
    return this.employeeHasSkillsForShiftType[employeeDescription.IndexID][shiftTypeIndex];
  }
  
  public boolean EmployeeUsesSecondarySkillForShiftType(EmployeeDescription employeeDescription, int shiftTypeIndex) {
    return this.employeeUsesSecondarySkillForShiftType[employeeDescription.IndexID][shiftTypeIndex];
  }
  
  public boolean EmployeeHasSkillForCover(EmployeeDescription employeeDescription, CoverRequirement req) {
    return this.employeeHasSkillForCover[employeeDescription.IndexID][req.Index];
  }
  
  public int[] GetShiftGroupsContainingShift(int shiftIndex) {
    return this.shiftGroupsContainingShift[shiftIndex];
  }
  
  public boolean ShiftGroupContainsShiftType(int groupIndex, int shiftIndex) {
    return this.shiftGroupContainsShiftType[groupIndex][shiftIndex];
  }
}
