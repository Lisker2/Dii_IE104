package org.example.util.ASAP.NRP.Core.Parsers;

import org.example.util.ASAP.NRP.Core.BankHoliday;
import org.example.util.ASAP.NRP.Core.CSharpConversionHelper;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.Pattern;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.PatternGroup;
import org.example.util.ASAP.NRP.Core.Contract;
import org.example.util.ASAP.NRP.Core.CoverRequirement;
import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.MasterWeights;
import org.example.util.ASAP.NRP.Core.Period;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;
import org.example.util.ASAP.NRP.Core.ShiftType;
import org.example.util.ASAP.NRP.Core.Skill;
import org.example.util.ASAP.NRP.Core.SkillGroup;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class SchedulingPeriodParser extends Parser {
  SchedulingPeriod schedulingPeriod;
  
  DateTime overridingStartDate;
  
  DateTime overridingEndDate;
  
  boolean overrideDates = false;
  
  Hashtable<String, Contract> ContractsHash = new Hashtable<String, Contract>();
  
  public SchedulingPeriod Parse(String filePath) {
    try {
      FileInputStream fis = new FileInputStream(filePath);
      SchedulingPeriod sp = Parse(fis);
      if (sp != null)
        sp.FilePath = filePath; 
      return sp;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    } 
  }
  
  public SchedulingPeriod Parse(InputStream inputStream) {
    try {
      XmlReader reader = new XmlReader(inputStream);
      if (this.VERBOSE)
        System.out.println("Parsing Scheduling Period ..."); 
      SchedulingPeriod sp = Parse(reader);
      return sp;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    } 
  }
  
  public void SwitchDateOverridingOn(DateTime startDate, DateTime endDate) {
    this.overrideDates = true;
    this.overridingStartDate = startDate;
    this.overridingEndDate = endDate;
  }
  
  public void SwitchDateOverridingOff() {
    this.overrideDates = false;
  }
  
  public SchedulingPeriod Parse(XmlReader reader) {
    reader.MoveToContent();
    String SchedulingPeriodID = reader.GetAttribute("ID");
    String OrganisationID = reader.GetAttribute("OrganisationID");
    String DepartmentID = reader.GetAttribute("DepartmentID");
    if (this.VERBOSE) {
      System.out.println("OrganisationID = " + OrganisationID);
      System.out.println("DepartmentID = " + DepartmentID);
    } 
    reader.ReadStartElement("SchedulingPeriod");
    DateTime StartDate = reader.ReadElementContentAsDateTime();
    DateTime EndDate = reader.ReadElementContentAsDateTime();
    if (this.overrideDates) {
      StartDate = this.overridingStartDate;
      EndDate = this.overridingEndDate;
    } 
    if (this.VERBOSE) {
      System.out.println("StartDate = " + StartDate.ToShortDateString());
      System.out.println("EndDate = " + EndDate.ToShortDateString());
    } 
    if (StartDate.isGreaterThan(EndDate)) {
      System.out.println("Error : End date is earlier than start date.");
      return null;
    } 
    EndDate = EndDate.AddDays(1);
    this.schedulingPeriod = new SchedulingPeriod(StartDate, EndDate);
    if (SchedulingPeriodID != null)
      this.schedulingPeriod.SchedulingPeriodID = SchedulingPeriodID; 
    if (OrganisationID != null)
      this.schedulingPeriod.OrganisationID = OrganisationID; 
    if (DepartmentID != null)
      this.schedulingPeriod.DepartmentID = DepartmentID; 
    if (reader.Name.equalsIgnoreCase("Skills")) {
      reader.ReadStartElement("Skills");
      while (!reader.Name.equalsIgnoreCase("Skills")) {
        Skill skill = ParseSkill(reader);
        this.schedulingPeriod.AddSkill(skill);
      } 
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("SkillGroups")) {
      reader.ReadStartElement("SkillGroups");
      while (!reader.Name.equalsIgnoreCase("SkillGroups")) {
        SkillGroup skillGroup = ParseSkillGroup(reader);
        this.schedulingPeriod.AddSkillGroup(skillGroup);
      } 
      reader.ReadEndElement();
    } 
    reader.ReadStartElement("ShiftTypes");
    while (!reader.Name.equalsIgnoreCase("ShiftTypes")) {
      ShiftType shiftType = ParseShiftType(reader);
      this.schedulingPeriod.AddShiftType(shiftType);
    } 
    reader.ReadEndElement();
    if (reader.Name.equalsIgnoreCase("ShiftGroups")) {
      reader.ReadStartElement("ShiftGroups");
      while (!reader.Name.equalsIgnoreCase("ShiftGroups")) {
        ShiftGroup shiftGroup = ParseShiftGroup(reader);
        if (shiftGroup.Group.length > 0)
          this.schedulingPeriod.AddShiftGroup(shiftGroup); 
      } 
      reader.ReadEndElement();
    } 
    reader.ReadStartElement("Contracts");
    while (!reader.Name.equalsIgnoreCase("Contracts")) {
      Contract contract = ParseContract(reader);
      AddContract(contract);
    } 
    reader.ReadEndElement();
    reader.ReadStartElement("Employees");
    while (!reader.Name.equalsIgnoreCase("Employees")) {
      EmployeeDescription employee = ParseEmployee(reader);
      this.schedulingPeriod.AddEmployeeDescription(employee);
    } 
    reader.ReadEndElement();
    reader.ReadStartElement("CoverRequirements");
    ParseCoverRequirements(reader);
    reader.ReadEndElement();
    if (reader.Name.equalsIgnoreCase("MasterWeights")) {
      reader.ReadStartElement("MasterWeights");
      this.schedulingPeriod.MasterWeights = ParseMasterWeights(reader);
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("DayOffRequests")) {
      reader.ReadStartElement("DayOffRequests");
      while (!reader.Name.equalsIgnoreCase("DayOffRequests"))
        ParseRequestedDayOff(reader); 
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("DayOnRequests")) {
      reader.ReadStartElement("DayOnRequests");
      while (!reader.Name.equalsIgnoreCase("DayOnRequests"))
        ParseRequestedDayOn(reader); 
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("ShiftOffRequests")) {
      reader.ReadStartElement("ShiftOffRequests");
      while (!reader.Name.equalsIgnoreCase("ShiftOffRequests"))
        ParseRequestedShiftOff(reader); 
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("ShiftOnRequests")) {
      reader.ReadStartElement("ShiftOnRequests");
      while (!reader.Name.equalsIgnoreCase("ShiftOnRequests"))
        ParseRequestedShiftOn(reader); 
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("DayOffRequestsBetweenDates")) {
      reader.ReadStartElement("DayOffRequestsBetweenDates");
      while (!reader.Name.equalsIgnoreCase("DayOffRequestsBetweenDates"))
        ParseDayOffRequestBetweenDates(reader); 
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("Partnerships")) {
      reader.ReadStartElement("Partnerships");
      while (!reader.Name.equalsIgnoreCase("Partnerships"))
        ParsePartnership(reader); 
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("BankHolidays")) {
      reader.ReadStartElement("BankHolidays");
      ParseBankHolidays(reader);
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("SchedulingHistory")) {
      reader.ReadStartElement("SchedulingHistory");
      ParseSchedulingHistory(reader);
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("PreAssignments")) {
      reader.ReadStartElement("PreAssignments");
      ParsePreAssignments(reader);
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("FrozenDays")) {
      reader.ReadStartElement("FrozenDays");
      ParseFrozenDays(reader);
      reader.ReadEndElement();
    } 
    reader.Close();
    this.schedulingPeriod.ParsingFinished();
    return this.schedulingPeriod;
  }
  
  private BankHoliday ParseBankHoliday(XmlReader reader) {
    String ID = reader.GetAttribute("ID");
    if (this.VERBOSE)
      System.out.println("Parsing BankHoliday, ID = " + ID); 
    BankHoliday bankHoliday = new BankHoliday(ID);
    reader.ReadStartElement("BankHoliday");
    reader.ReadStartElement("Name");
    String Name = reader.ReadString();
    bankHoliday.Name = Name;
    reader.ReadEndElement();
    reader.ReadStartElement("Date");
    String DateString = reader.ReadString();
    DateTime date = DateTime.ParseDate(DateString);
    bankHoliday.Date = date;
    reader.ReadEndElement();
    if (this.VERBOSE) {
      System.out.println("Name = " + Name);
      System.out.println("Date = " + date);
      System.out.println(" ");
    } 
    reader.ReadEndElement();
    return bankHoliday;
  }
  
  private boolean ParseBankHolidays(XmlReader reader) {
    while (!reader.Name.equalsIgnoreCase("BankHolidays")) {
      BankHoliday bankHoliday = ParseBankHoliday(reader);
      this.schedulingPeriod.AddBankHoliday(bankHoliday);
    } 
    if (this.VERBOSE) {
      System.out.println("BankHolidays Parsed.");
      System.out.println("----------------------------------------------");
    } 
    return true;
  }
  
  private Contract ParseContract(XmlReader reader) {
    String ContractID = reader.GetAttribute("ID");
    if (this.VERBOSE)
      System.out.println("Parsing Contract, ContractID = " + ContractID); 
    Contract contract = new Contract(ContractID, this.schedulingPeriod);
    reader.ReadStartElement("Contract");
    while (!reader.Name.equalsIgnoreCase("Contract")) {
      if (reader.Name.equalsIgnoreCase("Label")) {
        reader.ReadStartElement("Label");
        String Label = reader.ReadString();
        contract.Label = Label;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("Label=" + Label); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxNumAssignments")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxNumAssignmentsWeight = weight;
        int MaxNumAssignments = Integer.parseInt(reader.ReadString());
        contract.MaxNumAssignments = MaxNumAssignments;
        contract.MaxNumAssignmentsIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxNumAssignments=" + MaxNumAssignments + " weight=" + contract.MaxNumAssignmentsWeight + " contract.MaxNumAssignmentsIsOn=" + contract.MaxNumAssignmentsIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinNumAssignments")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinNumAssignmentsWeight = weight;
        int MinNumAssignments = Integer.parseInt(reader.ReadString());
        contract.MinNumAssignments = MinNumAssignments;
        contract.MinNumAssignmentsIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinNumAssignments=" + MinNumAssignments + " weight=" + contract.MinNumAssignmentsWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxDaysOff")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxDaysOffWeight = weight;
        int MaxDaysOff = Integer.parseInt(reader.ReadString());
        contract.MaxDaysOff = MaxDaysOff;
        contract.MaxDaysOffIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxDaysOff=" + MaxDaysOff + " weight=" + contract.MaxDaysOffWeight + " contract.MaxDaysOffIsOn=" + contract.MaxDaysOffIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinDaysOff")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinDaysOffWeight = weight;
        int MinDaysOff = Integer.parseInt(reader.ReadString());
        contract.MinDaysOff = MinDaysOff;
        contract.MinDaysOffIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinDaysOff=" + MinDaysOff + " weight=" + contract.MinDaysOffWeight + " contract.MinDaysOffIsOn=" + contract.MinDaysOffIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxConsecutiveWorkingDays")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxConsecutiveWorkingDaysWeight = weight;
        int MaxConsecutiveWorkingDays = Integer.parseInt(reader.ReadString());
        contract.MaxConsecutiveWorkingDays = MaxConsecutiveWorkingDays;
        contract.MaxConsecutiveWorkingDaysIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxConsecutiveWorkingDays=" + MaxConsecutiveWorkingDays + " weight=" + contract.MaxConsecutiveWorkingDaysWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinConsecutiveWorkingDays")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinConsecutiveWorkingDaysWeight = weight;
        int MinConsecutiveWorkingDays = Integer.parseInt(reader.ReadString());
        contract.MinConsecutiveWorkingDays = MinConsecutiveWorkingDays;
        contract.MinConsecutiveWorkingDaysIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinConsecutiveWorkingDays=" + MinConsecutiveWorkingDays + " weight=" + contract.MinConsecutiveWorkingDaysWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxWorkingBankHolidays")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxWorkingBankHolidaysWeight = weight;
        int MaxWorkingBankHolidays = Integer.parseInt(reader.ReadString());
        contract.MaxWorkingBankHolidays = MaxWorkingBankHolidays;
        contract.MaxWorkingBankHolidaysIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxWorkingBankHolidays=" + MaxWorkingBankHolidays + " weight=" + contract.MaxWorkingBankHolidaysWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxConsecutiveFreeDays")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxConsecutiveFreeDaysWeight = weight;
        int MaxConsecutiveFreeDays = Integer.parseInt(reader.ReadString());
        contract.MaxConsecutiveFreeDays = MaxConsecutiveFreeDays;
        contract.MaxConsecutiveFreeDaysIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxConsecutiveFreeDays=" + MaxConsecutiveFreeDays + " weight=" + contract.MaxConsecutiveFreeDaysWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinConsecutiveFreeDays")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinConsecutiveFreeDaysWeight = weight;
        int MinConsecutiveFreeDays = Integer.parseInt(reader.ReadString());
        contract.MinConsecutiveFreeDays = MinConsecutiveFreeDays;
        contract.MinConsecutiveFreeDaysIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinConsecutiveFreeDays=" + MinConsecutiveFreeDays + " weight=" + contract.MinConsecutiveFreeDaysWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxConsecutiveWorkingWeekends")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxConsecutiveWorkingWeekendsWeight = weight;
        int MaxConsecutiveWorkingWeekends = Integer.parseInt(reader.ReadString());
        contract.MaxConsecutiveWorkingWeekends = MaxConsecutiveWorkingWeekends;
        contract.MaxConsecutiveWorkingWeekendsIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxConsecutiveWorkingWeekends=" + MaxConsecutiveWorkingWeekends + " weight=" + contract.MaxConsecutiveWorkingWeekendsWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxWorkingWeekends")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxWorkingWeekendsWeight = weight;
        int MaxWorkingWeekends = Integer.parseInt(reader.ReadString());
        contract.MaxWorkingWeekends = MaxWorkingWeekends;
        contract.MaxWorkingWeekendsIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxWorkingWeekends=" + MaxWorkingWeekends + " weight=" + contract.MaxWorkingWeekendsWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxWorkingWeekendsInFourWeeks")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxWorkingWeekendsInFourWeeksWeight = weight;
        int MaxWorkingWeekendsInFourWeeks = Integer.parseInt(reader.ReadString());
        contract.MaxWorkingWeekendsInFourWeeks = MaxWorkingWeekendsInFourWeeks;
        contract.MaxWorkingWeekendsInFourWeeksIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxWorkingWeekendsInFourWeeks=" + MaxWorkingWeekendsInFourWeeks + " weight=" + contract.MaxWorkingWeekendsInFourWeeksWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxWorkingWeekendsIncFriNight")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxWorkingWeekendsIncFriNightWeight = weight;
        int MaxWorkingWeekendsIncFriNight = Integer.parseInt(reader.ReadString());
        contract.MaxWorkingWeekendsIncFriNight = MaxWorkingWeekendsIncFriNight;
        contract.MaxWorkingWeekendsIncFriNightIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxWorkingWeekendsIncFriNight=" + MaxWorkingWeekendsIncFriNight + " weight=" + contract.MaxWorkingWeekendsIncFriNightWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxWeekendsOff")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxWeekendsOffWeight = weight;
        int MaxWeekendsOff = Integer.parseInt(reader.ReadString());
        contract.MaxWeekendsOff = MaxWeekendsOff;
        contract.MaxWeekendsOffIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxWeekendsOff=" + MaxWeekendsOff + " weight=" + contract.MaxWeekendsOffWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxWorkingDaysPerWeek")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxWorkingDaysPerWeekWeight = weight;
        int MaxWorkingDaysPerWeek = Integer.parseInt(reader.ReadString());
        contract.MaxWorkingDaysPerWeek = MaxWorkingDaysPerWeek;
        contract.MaxWorkingDaysPerWeekIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxWorkingDaysPerWeek=" + MaxWorkingDaysPerWeek + " weight=" + contract.MaxWorkingDaysPerWeekWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxShiftsPerWeekStartMon")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxShiftsPerWeekStartMonWeight = weight;
        int MaxShiftsPerWeekStartMon = Integer.parseInt(reader.ReadString());
        contract.MaxShiftsPerWeekStartMon = MaxShiftsPerWeekStartMon;
        contract.MaxShiftsPerWeekStartMonIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxShiftsPerWeekStartMon=" + MaxShiftsPerWeekStartMon + " weight=" + contract.MaxShiftsPerWeekStartMonWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinShiftsPerWeekStartMon")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinShiftsPerWeekStartMonWeight = weight;
        int MinShiftsPerWeekStartMon = Integer.parseInt(reader.ReadString());
        contract.MinShiftsPerWeekStartMon = MinShiftsPerWeekStartMon;
        contract.MinShiftsPerWeekStartMonIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinShiftsPerWeekStartMon=" + MinShiftsPerWeekStartMon + " weight=" + contract.MinShiftsPerWeekStartMonWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxWeekendDays")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxWeekendDaysWeight = weight;
        int MaxWeekendDays = Integer.parseInt(reader.ReadString());
        contract.MaxWeekendDays = MaxWeekendDays;
        contract.MaxWeekendDaysIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxWeekendDays=" + MaxWeekendDays + " weight=" + contract.MaxWeekendDaysWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("WeekendDefinition")) {
        reader.ReadStartElement("WeekendDefinition");
        String WeekendDefinition = reader.ReadString();
        contract.SetWeekendDefinition(WeekendDefinition);
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("WeekendDefinition=" + WeekendDefinition); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("CompleteWeekends")) {
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.CompleteWeekendsWeight = weight;
        boolean CompleteWeekends = reader.ReadElementContentAsBoolean();
        contract.CompleteWeekends = CompleteWeekends;
        if (this.VERBOSE)
          System.out.println("CompleteWeekends=" + CompleteWeekends); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("BrokenWeekends")) {
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.BrokenWeekendsWeight = weight;
        boolean BrokenWeekends = reader.ReadElementContentAsBoolean();
        contract.BrokenWeekends = BrokenWeekends;
        if (this.VERBOSE)
          System.out.println("BrokenWeekends=" + BrokenWeekends); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("IdenticalShiftTypesDuringWeekend")) {
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.IdenticalShiftTypesDuringWeekendWeight = weight;
        boolean IdenticalShiftTypesDuringWeekend = reader.ReadElementContentAsBoolean();
        contract.IdenticalShiftTypesDuringWeekend = IdenticalShiftTypesDuringWeekend;
        if (this.VERBOSE)
          System.out.println("IdenticalShiftTypesDuringWeekend=" + IdenticalShiftTypesDuringWeekend); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("NoNightShiftBeforeFreeWeekend")) {
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.NoNightShiftBeforeFreeWeekendWeight = weight;
        boolean NoNightShiftBeforeFreeWeekend = reader.ReadElementContentAsBoolean();
        contract.NoNightShiftBeforeFreeWeekend = NoNightShiftBeforeFreeWeekend;
        if (this.VERBOSE)
          System.out.println("NoNightShiftBeforeFreeWeekend=" + NoNightShiftBeforeFreeWeekend); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("TwoFreeDaysAfterNightShifts")) {
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.TwoFreeDaysAfterNightShiftsWeight = weight;
        boolean TwoFreeDaysAfterNightShifts = reader.ReadElementContentAsBoolean();
        contract.TwoFreeDaysAfterNightShifts = TwoFreeDaysAfterNightShifts;
        if (this.VERBOSE)
          System.out.println("TwoFreeDaysAfterNightShifts=" + TwoFreeDaysAfterNightShifts); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("AlternativeSkillCategory")) {
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.AlternativeSkillCategoryWeight = weight;
        boolean AlternativeSkillCategory = reader.ReadElementContentAsBoolean();
        contract.AlternativeSkillCategory = AlternativeSkillCategory;
        if (this.VERBOSE)
          System.out.println("AlternativeSkillCategory=" + AlternativeSkillCategory); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxAssignmentsForDayOfWeek")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxAssignmentsForDayOfWeekIsOn = on;
        contract.MaxAssignmentsForDayOfWeekWeight = weight;
        if (this.VERBOSE)
          System.out.println("MaxAssignmentsForDayOfWeekIsOn=" + on); 
        reader.ReadStartElement("MaxAssignmentsForDayOfWeek");
        while (!reader.Name.equalsIgnoreCase("MaxAssignmentsForDayOfWeek")) {
          reader.ReadStartElement("MaxAssignments");
          reader.ReadStartElement("Day");
          String Day = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("MaxAssignmentsForDayOfWeek: Day = " + Day + ", val = " + val); 
          if (Day.equalsIgnoreCase("Monday")) {
            contract.MaxAssignmentsForAllMondays = val;
            contract.MaxAssignmentsForAllMondaysIsOn = true;
            continue;
          } 
          if (Day.equalsIgnoreCase("Tuesday")) {
            contract.MaxAssignmentsForAllTuesdays = val;
            contract.MaxAssignmentsForAllTuesdaysIsOn = true;
            continue;
          } 
          if (Day.equalsIgnoreCase("Wednesday")) {
            contract.MaxAssignmentsForAllWednesdays = val;
            contract.MaxAssignmentsForAllWednesdaysIsOn = true;
            continue;
          } 
          if (Day.equalsIgnoreCase("Thursday")) {
            contract.MaxAssignmentsForAllThursdays = val;
            contract.MaxAssignmentsForAllThursdaysIsOn = true;
            continue;
          } 
          if (Day.equalsIgnoreCase("Friday")) {
            contract.MaxAssignmentsForAllFridays = val;
            contract.MaxAssignmentsForAllFridaysIsOn = true;
            continue;
          } 
          if (Day.equalsIgnoreCase("Saturday")) {
            contract.MaxAssignmentsForAllSaturdays = val;
            contract.MaxAssignmentsForAllSaturdaysIsOn = true;
            continue;
          } 
          if (Day.equalsIgnoreCase("Sunday")) {
            contract.MaxAssignmentsForAllSundays = val;
            contract.MaxAssignmentsForAllSundaysIsOn = true;
          } 
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxAssignmentsForDayOfWeekIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("ValidNumConsecutiveShiftTypes")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.ValidNumConsecutiveShiftTypesIsOn = on;
        contract.ValidNumConsecutiveShiftTypesWeight = weight;
        if (this.VERBOSE)
          System.out.println("ValidNumConsecutiveShiftTypesIsOn=" + on); 
        reader.ReadStartElement("ValidNumConsecutiveShiftTypes");
        while (!reader.Name.equalsIgnoreCase("ValidNumConsecutiveShiftTypes")) {
          reader.ReadStartElement("NumConsecutiveShiftType");
          reader.ReadStartElement("ShiftType");
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("ValidNumConsecutiveShiftType: ID = " + ID + ", val = " + val); 
          ShiftType sh = this.schedulingPeriod.GetShiftType(ID);
          if (sh == null) {
            System.out.println("Warning: Constraint ValidNumConsecutiveShiftTypes for ShiftType: " + ID + ", but no definition of ShiftType: " + ID + " found.");
            continue;
          } 
          contract.ValidNumConsecutiveShiftTypes[sh.Index][val - 1] = true;
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("ValidNumConsecutiveShiftTypesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("ValidNumConsecutiveShiftGroups")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.ValidNumConsecutiveShiftGroupsIsOn = on;
        contract.ValidNumConsecutiveShiftGroupsWeight = weight;
        if (this.VERBOSE)
          System.out.println("ValidNumConsecutiveShiftGroupsIsOn=" + on); 
        reader.ReadStartElement("ValidNumConsecutiveShiftGroups");
        while (!reader.Name.equalsIgnoreCase("ValidNumConsecutiveShiftGroups")) {
          reader.ReadStartElement("NumConsecutiveShiftGroup");
          reader.ReadStartElement("ShiftGroup");
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("ValidNumConsecutiveShiftGroup: ID = " + ID + ", val = " + val); 
          ShiftGroup sh = this.schedulingPeriod.GetShiftGroup(ID);
          if (sh == null) {
            System.out.println("Warning: Constraint ValidNumConsecutiveShiftGroups for ShiftGroup: " + ID + ", but no definition of ShiftGroup: " + ID + " found.");
            continue;
          } 
          contract.ValidNumConsecutiveShiftGroups[sh.Index][val - 1] = true;
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("ValidNumConsecutiveShiftGroupsIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxShiftTypes")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxShiftTypesIsOn = on;
        contract.MaxShiftTypesWeight = weight;
        reader.ReadStartElement("MaxShiftTypes");
        while (!reader.Name.equalsIgnoreCase("MaxShiftTypes")) {
          reader.ReadStartElement("MaxShiftType");
          boolean shiftType = false;
          boolean shiftGroup = false;
          if (reader.Name.equalsIgnoreCase("ShiftType")) {
            shiftType = true;
          } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
            shiftGroup = true;
          } 
          reader.ReadStartElement();
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          if (shiftType) {
            ShiftType sh = this.schedulingPeriod.GetShiftType(ID);
            if (sh == null) {
              System.out.println("Warning: Constraint MaxShiftTypes for ShiftType: " + ID + ", but no definition of ShiftType: " + ID + " found.");
              continue;
            } 
            contract.SetMaxShiftType(sh, val);
            continue;
          } 
          if (shiftGroup) {
            ShiftGroup sh = this.schedulingPeriod.GetShiftGroup(ID);
            if (sh == null) {
              System.out.println("Warning: Constraint MaxShiftTypes for ShiftGroup: " + ID + ", but no definition of ShiftGroup: " + ID + " found.");
              continue;
            } 
            contract.SetMaxShiftGroup(sh, val);
          } 
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxShiftTypesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinShiftTypes")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinShiftTypesIsOn = on;
        contract.MinShiftTypesWeight = weight;
        reader.ReadStartElement("MinShiftTypes");
        while (!reader.Name.equalsIgnoreCase("MinShiftTypes")) {
          reader.ReadStartElement("MinShiftType");
          boolean shiftType = false;
          boolean shiftGroup = false;
          if (reader.Name.equalsIgnoreCase("ShiftType")) {
            shiftType = true;
          } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
            shiftGroup = true;
          } 
          reader.ReadStartElement();
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          if (shiftType) {
            ShiftType sh = this.schedulingPeriod.GetShiftType(ID);
            if (sh == null) {
              System.out.println("Warning: Constraint MinShiftTypes for ShiftType: " + ID + ", but no definition of ShiftType: " + ID + " found.");
              continue;
            } 
            contract.SetMinShiftType(sh, val);
            continue;
          } 
          if (shiftGroup) {
            ShiftGroup sh = this.schedulingPeriod.GetShiftGroup(ID);
            if (sh == null) {
              System.out.println("Warning: Constraint MinShiftTypes for ShiftGroup: " + ID + ", but no definition of ShiftGroup: " + ID + " found.");
              continue;
            } 
            contract.SetMinShiftGroup(sh, val);
          } 
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinShiftTypesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxShiftTypesPerWeek")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxShiftTypesPerWeekIsOn = on;
        contract.MaxShiftTypesPerWeekWeight = weight;
        reader.ReadStartElement("MaxShiftTypesPerWeek");
        while (!reader.Name.equalsIgnoreCase("MaxShiftTypesPerWeek")) {
          reader.ReadStartElement("MaxShiftTypePerWeek");
          reader.ReadStartElement("ShiftType");
          String ShiftTypeID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Week");
          int week = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          ShiftType sh = this.schedulingPeriod.GetShiftType(ShiftTypeID);
          if (sh == null) {
            System.out.println("Warning: Constraint MaxShiftTypesPerWeek for ShiftType: " + ShiftTypeID + ", but no definition of ShiftType: " + ShiftTypeID + " found.");
            continue;
          } 
          contract.SetMaxShiftTypePerWeek(sh, val, week);
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxShiftTypesPerWeekIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxHoursWorked")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        String threshold = reader.GetAttribute("threshold");
        if (threshold != null) {
          double MaxHoursWorkedThreshold = Double.parseDouble(threshold);
          contract.MaxHoursWorkedThreshold = MaxHoursWorkedThreshold;
        } 
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxHoursWorkedIsOn = on;
        contract.MaxHoursWorkedWeight = weight;
        double MaxHoursWorked = Double.parseDouble(reader.ReadString());
        contract.MaxHoursWorked = MaxHoursWorked;
        reader.Read();
        if (this.VERBOSE)
          System.out.println("MaxHoursWorked=" + MaxHoursWorked + ", on=" + contract.MaxHoursWorkedIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinHoursWorked")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        String threshold = reader.GetAttribute("threshold");
        if (threshold != null) {
          double MinHoursWorkedThreshold = Double.parseDouble(threshold);
          contract.MinHoursWorkedThreshold = MinHoursWorkedThreshold;
        } 
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinHoursWorkedIsOn = on;
        contract.MinHoursWorkedWeight = weight;
        double MinHoursWorked = Double.parseDouble(reader.ReadString());
        contract.MinHoursWorked = MinHoursWorked;
        reader.Read();
        if (this.VERBOSE)
          System.out.println("MinHoursWorked=" + MinHoursWorked + ", on=" + contract.MinHoursWorkedIsOn + " threshold=" + contract.MinHoursWorkedThreshold); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("StandardPerformance")) {
        reader.ReadStartElement("StandardPerformance");
        double StandardPerformance = Double.parseDouble(reader.ReadString());
        contract.StandardPerformance = StandardPerformance;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("StandardPerformance=" + StandardPerformance); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxHoursPerWeek")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxHoursPerWeekIsOn = on;
        contract.MaxHoursPerWeekWeight = weight;
        double MaxHoursPerWeek = Double.parseDouble(reader.ReadString());
        contract.MaxHoursPerWeek = MaxHoursPerWeek;
        reader.Read();
        if (this.VERBOSE)
          System.out.println("MaxHoursPerWeek=" + MaxHoursPerWeek + ", on=" + contract.MaxHoursPerWeekIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxHoursPerFortnight")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxHoursPerFortnightIsOn = on;
        contract.MaxHoursPerFortnightWeight = weight;
        double MaxHoursPerFortnight = Double.parseDouble(reader.ReadString());
        contract.MaxHoursPerFortnight = MaxHoursPerFortnight;
        reader.Read();
        if (this.VERBOSE)
          System.out.println("MaxHoursPerFortnight=" + MaxHoursPerFortnight + ", on=" + contract.MaxHoursPerFortnightIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("ValidShiftTypeSuccessions")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.ValidShiftTypeSuccessionsIsOn = on;
        contract.ValidShiftTypeSuccessionsWeight = weight;
        reader.ReadStartElement("ValidShiftTypeSuccessions");
        while (!reader.Name.equalsIgnoreCase("ValidShiftTypeSuccessions")) {
          reader.ReadStartElement("Succession");
          reader.ReadStartElement("ShiftTypeID1");
          String ShiftTypeID1 = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("ShiftTypeID2");
          String ShiftTypeID2 = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadEndElement();
          if (!ShiftTypeID1.equalsIgnoreCase("")) {
            if (this.schedulingPeriod.GetShiftType(ShiftTypeID1) == null) {
              System.out.println("Warning: Constraint ShiftTypeSuccessions for ShiftType: " + ShiftTypeID1 + ", but no definition of ShiftType: " + ShiftTypeID1 + " found.");
              continue;
            } 
          } else if (!ShiftTypeID2.equalsIgnoreCase("")) {
            if (this.schedulingPeriod.GetShiftType(ShiftTypeID2) == null) {
              System.out.println("Warning: Constraint ShiftTypeSuccessions for ShiftType: " + ShiftTypeID2 + ", but no definition of ShiftType: " + ShiftTypeID2 + " found.");
              continue;
            } 
          } 
          contract.SetShiftTypeSuccession(ShiftTypeID1, ShiftTypeID2, true);
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("ValidShiftTypeSuccessionsIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("BadPatterns")) {
        contract.BadPatternsIsOn = true;
        reader.ReadStartElement("BadPatterns");
        while (!reader.Name.equalsIgnoreCase("BadPatterns")) {
          Pattern pattern = ParsePattern(reader);
          if (pattern != null)
            contract.AddBadPattern(pattern); 
          if (this.VERBOSE)
            System.out.println("BadPattern: StartDay=" + pattern.getStartDayOrDate() + " weight=" + pattern.Weight); 
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("BadPatternsIsOn=true"); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("GoodPatterns")) {
        contract.GoodPatternsIsOn = true;
        reader.ReadStartElement("GoodPatterns");
        while (!reader.Name.equalsIgnoreCase("GoodPatterns")) {
          int weight = Integer.parseInt(reader.GetAttribute("weight"));
          PatternGroup patternGroup = new PatternGroup(weight);
          reader.ReadStartElement("PatternGroup");
          while (!reader.Name.equalsIgnoreCase("PatternGroup")) {
            Pattern pattern = ParsePattern(reader);
            if (pattern != null)
              patternGroup.AddPattern(pattern); 
          } 
          reader.ReadEndElement();
          contract.AddGoodPattern(patternGroup);
          if (this.VERBOSE)
            System.out.println("PatternGroup parsed: weight=" + weight); 
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("GoodPatternsIsOn=true"); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinShiftsPerWeek")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinShiftsPerWeekWeight = weight;
        int MinShiftsPerWeek = Integer.parseInt(reader.ReadString());
        contract.MinShiftsPerWeek = MinShiftsPerWeek;
        contract.MinShiftsPerWeekIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinShiftsPerWeek=" + MinShiftsPerWeek + " weight=" + contract.MinShiftsPerWeekWeight + " contract.MinShiftsPerWeekIsOn=" + contract.MinShiftsPerWeekIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxShiftsPerWeek")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxShiftsPerWeekWeight = weight;
        int MaxShiftsPerWeek = Integer.parseInt(reader.ReadString());
        contract.MaxShiftsPerWeek = MaxShiftsPerWeek;
        contract.MaxShiftsPerWeekIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxShiftsPerWeek=" + MaxShiftsPerWeek + " weight=" + contract.MaxShiftsPerWeekWeight + " contract.MaxShiftsPerWeekIsOn=" + contract.MaxShiftsPerWeekIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinDaysBetweenShiftSeries")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinDaysBetweenShiftSeriesIsOn = on;
        contract.MinDaysBetweenShiftSeriesWeight = weight;
        reader.ReadStartElement("MinDaysBetweenShiftSeries");
        while (!reader.Name.equalsIgnoreCase("MinDaysBetweenShiftSeries")) {
          reader.ReadStartElement("MinDaysBetweenShifts");
          boolean shiftType = false;
          boolean shiftGroup = false;
          if (reader.Name.equalsIgnoreCase("ShiftType")) {
            shiftType = true;
          } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
            shiftGroup = true;
          } 
          reader.ReadStartElement();
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("Min DaysBetweenShifts = " + ID + ", Value = " + val); 
          if (shiftType) {
            ShiftType st = this.schedulingPeriod.GetShiftType(ID);
            if (st == null) {
              System.out.println("Warning: Constraint MinDaysBetweenShiftSeries for ShiftType: " + ID + ", but no shifts of ShiftType: " + ID + " are known.");
            } else {
              contract.SetMinDaysBetweenShiftSeries(st, val);
            } 
          } else if (shiftGroup) {
            ShiftGroup sg = this.schedulingPeriod.GetShiftGroup(ID);
            if (sg == null) {
              System.out.println("Warning: Constraint MinDaysBetweenShiftSeries for ShiftGroup: " + ID + ", but no shiftGroups of ID: " + ID + " are known.");
            } else {
              System.out.println("Warning : ShiftGroup for constraint MinDaysBetweenShiftSeries not yet implemented.");
            } 
          } 
          reader.ReadEndElement();
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinDaysBetweenShiftSeriesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxDaysBetweenShiftSeries")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxDaysBetweenShiftSeriesIsOn = on;
        contract.MaxDaysBetweenShiftSeriesWeight = weight;
        reader.ReadStartElement("MaxDaysBetweenShiftSeries");
        while (!reader.Name.equalsIgnoreCase("MaxDaysBetweenShiftSeries")) {
          reader.ReadStartElement("MaxDaysBetweenShifts");
          boolean shiftType = false;
          boolean shiftGroup = false;
          if (reader.Name.equalsIgnoreCase("ShiftType")) {
            shiftType = true;
          } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
            shiftGroup = true;
          } 
          reader.ReadStartElement();
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("Max DaysBetweenShifts = " + ID + ", Value = " + val); 
          if (shiftType) {
            ShiftType st = this.schedulingPeriod.GetShiftType(ID);
            if (st == null) {
              System.out.println("Warning: Constraint MaxDaysBetweenShiftSeries for ShiftType: " + ID + ", but no shifts of ShiftType: " + ID + " are known.");
            } else {
              contract.SetMaxDaysBetweenShiftSeries(st, val);
            } 
          } else if (shiftGroup) {
            ShiftGroup sg = this.schedulingPeriod.GetShiftGroup(ID);
            if (sg == null) {
              System.out.println("Warning: Constraint MaxDaysBetweenShiftSeries for ShiftGroup: " + ID + ", but no shiftGroups of ID: " + ID + " are known.");
            } else {
              System.out.println("Warning : ShiftGroup for constraint MaxDaysBetweenShiftSeries not yet implemented.");
            } 
          } 
          reader.ReadEndElement();
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxDaysBetweenShiftSeriesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinConsecutiveShiftTypes")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinConsecutiveShiftTypesIsOn = on;
        contract.MinConsecutiveShiftTypesWeight = weight;
        reader.ReadStartElement("MinConsecutiveShiftTypes");
        while (!reader.Name.equalsIgnoreCase("MinConsecutiveShiftTypes")) {
          reader.ReadStartElement("MinConsecutiveShiftType");
          boolean shiftType = false;
          boolean shiftGroup = false;
          if (reader.Name.equalsIgnoreCase("ShiftType")) {
            shiftType = true;
          } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
            shiftGroup = true;
          } 
          reader.ReadStartElement();
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("Min ConsecutiveShiftType = " + ID + ", Value = " + val); 
          if (shiftType) {
            ShiftType st = this.schedulingPeriod.GetShiftType(ID);
            if (st == null) {
              System.out.println("Warning: Constraint MinConsecutiveShiftType for ShiftType: " + ID + ", but no shifts of ShiftType: " + ID + " are known.");
            } else {
              contract.SetMinConsecutiveShiftType(st, val);
            } 
          } else if (shiftGroup) {
            ShiftGroup sg = this.schedulingPeriod.GetShiftGroup(ID);
            if (sg == null) {
              System.out.println("Warning: Constraint MinConsecutiveShiftType for ShiftGroup: " + ID + ", but no shiftGroups of ID: " + ID + " are known.");
            } else {
              contract.SetMinConsecutiveShiftGroup(sg, val);
            } 
          } 
          reader.ReadEndElement();
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinConsecutiveShiftTypesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxConsecutiveShiftTypes")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxConsecutiveShiftTypesIsOn = on;
        contract.MaxConsecutiveShiftTypesWeight = weight;
        reader.ReadStartElement("MaxConsecutiveShiftTypes");
        while (!reader.Name.equalsIgnoreCase("MaxConsecutiveShiftTypes")) {
          reader.ReadStartElement("MaxConsecutiveShiftType");
          boolean shiftType = false;
          boolean shiftGroup = false;
          if (reader.Name.equalsIgnoreCase("ShiftType")) {
            shiftType = true;
          } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
            shiftGroup = true;
          } 
          reader.ReadStartElement();
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("Max ConsecutiveShiftType = " + ID + ", Value = " + val); 
          if (shiftType) {
            ShiftType st = this.schedulingPeriod.GetShiftType(ID);
            if (st == null) {
              System.out.println("Warning: Constraint MaxConsecutiveShiftType for ShiftType: " + ID + ", but no shifts of ShiftType: " + ID + " are known.");
            } else {
              contract.SetMaxConsecutiveShiftType(st, val);
            } 
          } else if (shiftGroup) {
            ShiftGroup sg = this.schedulingPeriod.GetShiftGroup(ID);
            if (sg == null) {
              System.out.println("Warning: Constraint MaxConsecutiveShiftType for ShiftGroup: " + ID + ", but no ShiftGroup of ID: " + ID + " are known.");
            } else {
              contract.SetMaxConsecutiveShiftGroup(sg, val);
            } 
          } 
          reader.ReadEndElement();
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxConsecutiveShiftTypesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxShiftTypeRatios")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxShiftTypeRatiosIsOn = on;
        contract.MaxShiftTypeRatiosWeight = weight;
        reader.ReadStartElement("MaxShiftTypeRatios");
        while (!reader.Name.equalsIgnoreCase("MaxShiftTypeRatios")) {
          reader.ReadStartElement("MaxShiftTypeRatio");
          boolean shiftType = false;
          boolean shiftGroup = false;
          if (reader.Name.equalsIgnoreCase("ShiftType")) {
            shiftType = true;
          } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
            shiftGroup = true;
          } 
          reader.ReadStartElement();
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Ratio");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("MaxShiftTypeRatio = " + ID + ", Value = " + val); 
          if (shiftType) {
            ShiftType st = this.schedulingPeriod.GetShiftType(ID);
            if (st == null) {
              System.out.println("Warning: Constraint MaxShiftTypeRatio for ShiftType: " + ID + ", but no shift types of ShiftType: " + ID + " are known.");
            } else {
              contract.SetMaxShiftTypeRatio(st, val);
            } 
          } else if (shiftGroup) {
            ShiftGroup s = this.schedulingPeriod.GetShiftGroup(ID);
            if (s == null) {
              System.out.println("Warning: Constraint MaxShiftTypeRatio for ShiftGroup: " + ID + ", but no shift groups of ID: " + ID + " are known.");
            } else {
              contract.SetMaxShiftGroupRatio(s, val);
            } 
          } 
          reader.ReadEndElement();
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxShiftTypeRatioIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinShiftTypeRatios")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinShiftTypeRatiosIsOn = on;
        contract.MinShiftTypeRatiosWeight = weight;
        reader.ReadStartElement("MinShiftTypeRatios");
        while (!reader.Name.equalsIgnoreCase("MinShiftTypeRatios")) {
          reader.ReadStartElement("MinShiftTypeRatio");
          boolean shiftType = false;
          boolean shiftGroup = false;
          if (reader.Name.equalsIgnoreCase("ShiftType")) {
            shiftType = true;
          } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
            shiftGroup = true;
          } 
          reader.ReadStartElement();
          String ID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Ratio");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          if (this.VERBOSE)
            System.out.println("MinShiftTypeRatio = " + ID + ", Value = " + val); 
          if (shiftType) {
            ShiftType st = this.schedulingPeriod.GetShiftType(ID);
            if (st == null) {
              System.out.println("Warning: Constraint MinShiftTypeRatio for ShiftType: " + ID + ", but no shift types of ShiftType: " + ID + " are known.");
            } else {
              contract.SetMinShiftTypeRatio(st, val);
            } 
          } else if (shiftGroup) {
            ShiftGroup s = this.schedulingPeriod.GetShiftGroup(ID);
            if (s == null) {
              System.out.println("Warning: Constraint MinShiftTypeRatio for ShiftGroup: " + ID + ", but no shift Groups of : " + ID + " are known.");
            } else {
              contract.SetMinShiftGroupRatio(s, val);
            } 
          } 
          reader.ReadEndElement();
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinShiftTypeRatioIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxShiftsPerDay")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxShiftsPerDayWeight = weight;
        int MaxShiftsPerDay = Integer.parseInt(reader.ReadString());
        contract.MaxShiftsPerDay = MaxShiftsPerDay;
        contract.MaxShiftsPerDayIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxShiftsPerDay=" + MaxShiftsPerDay + " weight=" + contract.MaxShiftsPerDayWeight + " contract.MaxShiftsPerDayIsOn=" + contract.MaxShiftsPerDayIsOn); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinHoursWorkedBetweenDates")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinHoursWorkedBetweenDatesIsOn = on;
        contract.MinHoursWorkedBetweenDatesWeight = weight;
        reader.ReadStartElement("MinHoursWorkedBetweenDates");
        while (!reader.Name.equalsIgnoreCase("MinHoursWorkedBetweenDates")) {
          reader.ReadStartElement("MinHoursWorked");
          reader.ReadStartElement("StartDate");
          DateTime StartDate = DateTime.ParseDate(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadStartElement("EndDate");
          DateTime EndDate = DateTime.ParseDate(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          contract.AddMinHoursWorkedBetweenDates(StartDate, EndDate, val);
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinHoursWorkedBetweenDatesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxHoursWorkedBetweenDates")) {
        boolean on = true;
        String onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxHoursWorkedBetweenDatesIsOn = on;
        contract.MaxHoursWorkedBetweenDatesWeight = weight;
        reader.ReadStartElement("MaxHoursWorkedBetweenDates");
        while (!reader.Name.equalsIgnoreCase("MaxHoursWorkedBetweenDates")) {
          reader.ReadStartElement("MaxHoursWorked");
          reader.ReadStartElement("StartDate");
          DateTime StartDate = DateTime.ParseDate(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadStartElement("EndDate");
          DateTime EndDate = DateTime.ParseDate(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadStartElement("Value");
          int val = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          contract.AddMaxHoursWorkedBetweenDates(StartDate, EndDate, val);
        } 
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxHoursWorkedBetweenDatesIsOn=" + on); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinConsecutiveWorkingWeekends")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinConsecutiveWorkingWeekendsWeight = weight;
        int MinConsecutiveWorkingWeekends = Integer.parseInt(reader.ReadString());
        contract.MinConsecutiveWorkingWeekends = MinConsecutiveWorkingWeekends;
        contract.MinConsecutiveWorkingWeekendsIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinConsecutiveWorkingWeekends=" + MinConsecutiveWorkingWeekends + " weight=" + contract.MinConsecutiveWorkingWeekendsWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MaxConsecutiveFreeWeekends")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MaxConsecutiveFreeWeekendsWeight = weight;
        int MaxConsecutiveFreeWeekends = Integer.parseInt(reader.ReadString());
        contract.MaxConsecutiveFreeWeekends = MaxConsecutiveFreeWeekends;
        contract.MaxConsecutiveFreeWeekendsIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MaxConsecutiveFreeWeekends=" + MaxConsecutiveFreeWeekends + " weight=" + contract.MaxConsecutiveFreeWeekendsWeight); 
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("MinConsecutiveFreeWeekends")) {
        boolean on = true;
        String onStr = null;
        onStr = reader.GetAttribute("on");
        if (onStr != null)
          on = XmlConvert.ToBoolean(onStr); 
        int weight = -1;
        String weightStr = reader.GetAttribute("weight");
        if (weightStr != null)
          weight = Integer.parseInt(weightStr); 
        contract.MinConsecutiveFreeWeekendsWeight = weight;
        int MinConsecutiveFreeWeekends = Integer.parseInt(reader.ReadString());
        contract.MinConsecutiveFreeWeekends = MinConsecutiveFreeWeekends;
        contract.MinConsecutiveFreeWeekendsIsOn = on;
        reader.ReadEndElement();
        if (this.VERBOSE)
          System.out.println("MinConsecutiveFreeWeekends=" + MinConsecutiveFreeWeekends + " weight=" + contract.MinConsecutiveFreeWeekendsWeight); 
        continue;
      } 
      System.out.println("Error :: Unknown Element : " + reader.Name);
      break;
    } 
    reader.ReadEndElement();
    if (this.VERBOSE)
      System.out.println("----------------------------------------------"); 
    return contract;
  }
  
  private Cover ParseCover(XmlReader reader) {
    reader.ReadStartElement("Cover");
    String skillID = "";
    CoverRequirement.SkillTypes skillType = CoverRequirement.SkillTypes.AnySkill;
    if (reader.Name.equalsIgnoreCase("Skill")) {
      skillType = CoverRequirement.SkillTypes.SingleSkill;
      reader.ReadStartElement("Skill");
      skillID = reader.ReadString();
      reader.ReadEndElement();
    } else if (reader.Name.equalsIgnoreCase("SkillGroup")) {
      skillType = CoverRequirement.SkillTypes.SkillGroup;
      reader.ReadStartElement("SkillGroup");
      skillID = reader.ReadString();
      reader.ReadEndElement();
    } 
    String ID = "";
    Cover.CoverType type = Cover.CoverType.Shift;
    Period period = null;
    if (reader.Name.equalsIgnoreCase("TimePeriod")) {
      type = Cover.CoverType.Period;
      period = ParseTimePeriod(reader);
    } else if (reader.Name.equalsIgnoreCase("Shift")) {
      type = Cover.CoverType.Shift;
      reader.ReadStartElement();
      ID = reader.ReadString();
      reader.ReadEndElement();
    } else if (reader.Name.equalsIgnoreCase("ShiftGroup")) {
      type = Cover.CoverType.ShiftGroup;
      reader.ReadStartElement();
      ID = reader.ReadString();
      reader.ReadEndElement();
    } else {
      System.out.println("Warning: Unknown cover type.");
    } 
    int min = -1;
    int max = -1;
    int pref = -1;
    if (reader.Name.equalsIgnoreCase("Min")) {
      reader.ReadStartElement("Min");
      min = Integer.parseInt(reader.ReadString());
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("Max")) {
      reader.ReadStartElement("Max");
      max = Integer.parseInt(reader.ReadString());
      reader.ReadEndElement();
    } 
    if (reader.Name.equalsIgnoreCase("Preferred")) {
      reader.ReadStartElement("Preferred");
      pref = Integer.parseInt(reader.ReadString());
      reader.ReadEndElement();
    } 
    reader.ReadEndElement();
    if (this.VERBOSE)
      System.out.println("ID = " + ID + ", Min = " + min + ", Max = " + max + ", Pref = " + pref + ", Type = " + type); 
    if (type == Cover.CoverType.Shift && this.schedulingPeriod.GetShiftType(ID) == null) {
      System.out.println("Warning: unknown shift: " + ID + ". Unable to add cover request for this shift.");
      return null;
    } 
    if (type == Cover.CoverType.ShiftGroup && this.schedulingPeriod.GetShiftGroup(ID) == null) {
      System.out.println("Warning: unknown shift group: " + ID + ". Unable to add cover request for this shift group.");
      return null;
    } 
    Cover cover = new Cover(ID, type, skillID, skillType, period);
    cover.Min = min;
    cover.Max = max;
    cover.Preferred = pref;
    return cover;
  }
  
  private boolean ParseCoverRequirements(XmlReader reader) {
    while (!reader.Name.equalsIgnoreCase("CoverRequirements")) {
      if (reader.Name.equalsIgnoreCase("DayOfWeekCover")) {
        reader.ReadStartElement("DayOfWeekCover");
        reader.ReadStartElement("Day");
        String Day = reader.ReadString();
        reader.ReadEndElement();
        DayCoverSpecification dayCoverSpec = new DayCoverSpecification(Day);
        if (this.VERBOSE)
          System.out.println("Day Requirement, " + Day); 
        while (!reader.Name.equalsIgnoreCase("DayOfWeekCover")) {
          Cover cover = ParseCover(reader);
          if (cover != null)
            dayCoverSpec.AddCoverRequirement(cover); 
        } 
        this.schedulingPeriod.CoverRequirements.AddCoverSpecification(dayCoverSpec, false);
        reader.ReadEndElement();
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("DateSpecificCover")) {
        if (this.VERBOSE)
          System.out.println("DateSpecific Requirement:"); 
        reader.ReadStartElement("DateSpecificCover");
        reader.ReadStartElement("Date");
        String DateString = reader.ReadString();
        DateTime Date = DateTime.ParseDate(DateString);
        reader.ReadEndElement();
        DateCoverSpecification dateCoverSpec = new DateCoverSpecification(Date);
        if (this.VERBOSE)
          System.out.println("Date = " + Date); 
        while (!reader.Name.equalsIgnoreCase("DateSpecificCover")) {
          Cover cover = ParseCover(reader);
          if (cover != null)
            dateCoverSpec.AddCoverRequirement(cover); 
        } 
        this.schedulingPeriod.CoverRequirements.AddCoverSpecification(dateCoverSpec, false);
        reader.ReadEndElement();
        continue;
      } 
      System.out.println("Error: Unknown element " + reader.Name);
    } 
    if (this.VERBOSE) {
      System.out.println("CoverRequirements Parsed.");
      System.out.println("----------------------------------------------");
    } 
    return true;
  }
  
  private MasterWeights ParseMasterWeights(XmlReader reader) {
    MasterWeights mws = new MasterWeights();
    while (!reader.Name.equalsIgnoreCase("MasterWeights")) {
      String name = reader.Name;
      reader.ReadStartElement();
      int weight = Integer.parseInt(reader.ReadString());
      if (name.equalsIgnoreCase("MinTimeBetweenShifts")) {
        mws.MinTimeBetweenShifts = weight;
      } else if (name.equalsIgnoreCase("MaxNumAssignments")) {
        mws.MaxNumAssignments = weight;
      } else if (name.equalsIgnoreCase("MinNumAssignments")) {
        mws.MinNumAssignments = weight;
      } else if (name.equalsIgnoreCase("MaxDaysOff")) {
        mws.MaxDaysOff = weight;
      } else if (name.equalsIgnoreCase("MinDaysOff")) {
        mws.MinDaysOff = weight;
      } else if (name.equalsIgnoreCase("MaxConsecutiveWorkingDays")) {
        mws.MaxConsecutiveWorkingDays = weight;
      } else if (name.equalsIgnoreCase("MinConsecutiveWorkingDays")) {
        mws.MinConsecutiveWorkingDays = weight;
      } else if (name.equalsIgnoreCase("MaxWorkingBankHolidays")) {
        mws.MaxWorkingBankHolidays = weight;
      } else if (name.equalsIgnoreCase("MaxConsecutiveFreeDays")) {
        mws.MaxConsecutiveFreeDays = weight;
      } else if (name.equalsIgnoreCase("MinConsecutiveFreeDays")) {
        mws.MinConsecutiveFreeDays = weight;
      } else if (name.equalsIgnoreCase("MaxConsecutiveWorkingWeekends")) {
        mws.MaxConsecutiveWorkingWeekends = weight;
      } else if (name.equalsIgnoreCase("MaxWorkingDaysPerWeek")) {
        mws.MaxWorkingDaysPerWeek = weight;
      } else if (name.equalsIgnoreCase("MaxWorkingWeekendsInFourWeeks")) {
        mws.MaxWorkingWeekendsInFourWeeks = weight;
      } else if (name.equalsIgnoreCase("MaxWorkingWeekendsIncFriNight")) {
        mws.MaxWorkingWeekendsIncFriNight = weight;
      } else if (name.equalsIgnoreCase("MaxShiftsPerWeekStartMon")) {
        mws.MaxShiftsPerWeekStartMon = weight;
      } else if (name.equalsIgnoreCase("MinShiftsPerWeekStartMon")) {
        mws.MinShiftsPerWeekStartMon = weight;
      } else if (name.equalsIgnoreCase("MaxWorkingWeekends")) {
        mws.MaxWorkingWeekends = weight;
      } else if (name.equalsIgnoreCase("MaxWeekendsOff")) {
        mws.MaxWeekendsOff = weight;
      } else if (name.equalsIgnoreCase("CompleteWeekends")) {
        mws.CompleteWeekends = weight;
      } else if (name.equalsIgnoreCase("IdenticalShiftTypesDuringWeekend")) {
        mws.IdenticalShiftTypesDuringWeekend = weight;
      } else if (name.equalsIgnoreCase("NoNightShiftBeforeFreeWeekend")) {
        mws.NoNightShiftBeforeFreeWeekend = weight;
      } else if (name.equalsIgnoreCase("TwoFreeDaysAfterNightShifts")) {
        mws.TwoFreeDaysAfterNightShifts = weight;
      } else if (name.equalsIgnoreCase("MaxAssignmentsForDayOfWeek")) {
        mws.MaxAssignmentsForDayOfWeek = weight;
      } else if (name.equalsIgnoreCase("ValidNumConsecutiveShiftTypes")) {
        mws.ValidNumConsecutiveShiftTypes = weight;
      } else if (name.equalsIgnoreCase("ValidNumConsecutiveShiftGroups")) {
        mws.ValidNumConsecutiveShiftGroups = weight;
      } else if (name.equalsIgnoreCase("MaxShiftTypes")) {
        mws.MaxShiftTypes = weight;
      } else if (name.equalsIgnoreCase("MinShiftTypes")) {
        mws.MinShiftTypes = weight;
      } else if (name.equalsIgnoreCase("MaxShiftTypesPerWeek")) {
        mws.MaxShiftTypesPerWeek = weight;
      } else if (name.equalsIgnoreCase("MaxHoursWorked")) {
        mws.MaxHoursWorked = weight;
      } else if (name.equalsIgnoreCase("MinHoursWorked")) {
        mws.MinHoursWorked = weight;
      } else if (name.equalsIgnoreCase("MaxHoursPerWeek")) {
        mws.MaxHoursPerWeek = weight;
      } else if (name.equalsIgnoreCase("MaxHoursPerFortnight")) {
        mws.MaxHoursPerFortnight = weight;
      } else if (name.equalsIgnoreCase("ValidShiftTypeSuccessions")) {
        mws.ValidShiftTypeSuccessions = weight;
      } else if (name.equalsIgnoreCase("AlternativeSkillCategory")) {
        mws.AlternativeSkillCategory = weight;
      } else if (name.equalsIgnoreCase("Tutorship")) {
        mws.Tutorship = weight;
      } else if (name.equalsIgnoreCase("WorkSeparately")) {
        mws.WorkSeparately = weight;
      } else if (name.equalsIgnoreCase("MinShiftsPerWeek")) {
        mws.MinShiftsPerWeek = weight;
      } else if (name.equalsIgnoreCase("MaxShiftsPerWeek")) {
        mws.MaxShiftsPerWeek = weight;
      } else if (name.equalsIgnoreCase("MinConsecutiveShiftTypes")) {
        mws.MinConsecutiveShiftTypes = weight;
      } else if (name.equalsIgnoreCase("MaxConsecutiveShiftTypes")) {
        mws.MaxConsecutiveShiftTypes = weight;
      } else if (name.equalsIgnoreCase("MaxDaysBetweenShiftSeries")) {
        mws.MaxDaysBetweenShiftSeries = weight;
      } else if (name.equalsIgnoreCase("MinDaysBetweenShiftSeries")) {
        mws.MinDaysBetweenShiftSeries = weight;
      } else if (name.equalsIgnoreCase("MaxShiftTypeRatios")) {
        mws.MaxShiftTypeRatios = weight;
      } else if (name.equalsIgnoreCase("MinShiftTypeRatios")) {
        mws.MinShiftTypeRatios = weight;
      } else if (name.equalsIgnoreCase("MaxShiftsPerDay")) {
        mws.MaxShiftsPerDay = weight;
      } else if (name.equalsIgnoreCase("MinHoursWorkedBetweenDates")) {
        mws.MinHoursWorkedBetweenDates = weight;
      } else if (name.equalsIgnoreCase("MaxHoursWorkedBetweenDates")) {
        mws.MaxHoursWorkedBetweenDates = weight;
      } else if (name.equalsIgnoreCase("MinConsecutiveWorkingWeekends")) {
        mws.MinConsecutiveWorkingWeekends = weight;
      } else if (name.equalsIgnoreCase("MaxConsecutiveFreeWeekends")) {
        mws.MaxConsecutiveFreeWeekends = weight;
      } else if (name.equalsIgnoreCase("MinConsecutiveFreeWeekends")) {
        mws.MinConsecutiveFreeWeekends = weight;
      } else if (name.equalsIgnoreCase("PrefOverStaffing")) {
        mws.PrefOverStaffing = weight;
      } else if (name.equalsIgnoreCase("PrefUnderStaffing")) {
        mws.PrefUnderStaffing = weight;
      } else if (name.equalsIgnoreCase("MaxOverStaffing")) {
        mws.MaxOverStaffing = weight;
      } else if (name.equalsIgnoreCase("MinUnderStaffing")) {
        mws.MinUnderStaffing = weight;
      } else if (name.equalsIgnoreCase("EmployeeAvailability")) {
        mws.EmployeeAvailability = weight;
      } else if (name.equalsIgnoreCase("SkilledShifts")) {
        mws.SkilledShifts = weight;
      } else if (name.equalsIgnoreCase("MaxWeekendDays")) {
        mws.MaxWeekendDays = weight;
      } else {
        System.out.println("Warning: Unknown element '" + name + "' in MasterWeights");
      } 
      reader.ReadEndElement();
      if (this.VERBOSE)
        System.out.println("MasterWeight: " + name + "=" + weight); 
    } 
    if (this.VERBOSE) {
      System.out.println("MasterWeights Parsed.");
      System.out.println("----------------------------------------------");
    } 
    return mws;
  }
  
  private EmployeeDescription ParseEmployee(XmlReader reader) {
    String EmployeeID = reader.GetAttribute("ID");
    if (this.VERBOSE)
      System.out.println("Parsing Employee, EmployeeID = " + EmployeeID); 
    EmployeeDescription employee = new EmployeeDescription(EmployeeID, this.schedulingPeriod);
    employee.LastName = EmployeeID;
    reader.ReadStartElement("Employee");
    while (!reader.Name.equalsIgnoreCase("Employee")) {
      if (reader.Name.equalsIgnoreCase("Name")) {
        reader.ReadStartElement("Name");
        employee.LastName = reader.ReadString();
        reader.ReadEndElement();
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("FirstName")) {
        reader.ReadStartElement("FirstName");
        employee.FirstName = reader.ReadString();
        reader.ReadEndElement();
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("LastName")) {
        reader.ReadStartElement("LastName");
        employee.LastName = reader.ReadString();
        reader.ReadEndElement();
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("EmailAddress")) {
        reader.ReadStartElement("EmailAddress");
        employee.EmailAddress = reader.ReadString();
        reader.ReadEndElement();
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("PhoneNumber")) {
        reader.ReadStartElement("PhoneNumber");
        employee.PhoneNumber = reader.ReadString();
        reader.ReadEndElement();
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("InRoster")) {
        employee.InRoster = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("ContractID")) {
        reader.ReadStartElement("ContractID");
        String ContractID = reader.ReadString();
        Contract contract = GetContract(ContractID);
        if (contract == null) {
          System.out.println("Error :: ContractID: '" + ContractID + "' (for employee " + EmployeeID + ") not defined.");
        } else {
          contract.EmployeeCount++;
          employee.Contract = contract;
        } 
        reader.ReadEndElement();
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("EmploymentStartDate")) {
        DateTime EmploymentStartDate = reader.ReadElementContentAsDateTime();
        employee.setEmploymentStartDate(EmploymentStartDate);
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("EmploymentEndDate")) {
        DateTime EmploymentEndDate = reader.ReadElementContentAsDateTime();
        employee.setEmploymentEndDate(EmploymentEndDate);
        continue;
      } 
      if (reader.Name.equalsIgnoreCase("Skills")) {
        reader.ReadStartElement("Skills");
        while (!reader.Name.equalsIgnoreCase("Skills")) {
          String Type = null;
          Type = reader.GetAttribute("Type");
          if (Type == null)
            Type = "Primary"; 
          reader.ReadStartElement("Skill");
          String SkillID = reader.ReadString();
          reader.ReadEndElement();
          employee.AddSkill(SkillID, Type);
          if (this.VERBOSE)
            System.out.println("SkillID = " + SkillID + ", Type = " + Type); 
        } 
        reader.ReadEndElement();
        continue;
      } 
      System.out.println("Error :: Unknown Element : " + reader.Name);
      break;
    } 
    reader.ReadEndElement();
    if (employee.Contract == null) {
      Random rand = new Random();
      employee.Contract = new Contract(Integer.toString(rand.nextInt(2147483647)), this.schedulingPeriod);
      employee.Contract.EmployeeCount = 1;
    } 
    if (this.VERBOSE) {
      System.out.println("Name = " + employee.getName());
      System.out.println("ContractID = " + employee.Contract.ContractID);
      System.out.println("----------------------------------------------");
    } 
    return employee;
  }
  
  private Period ParseTimePeriod(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing Period"); 
    DateTime startDateTime = new DateTime();
    DateTime endDateTime = new DateTime();
    reader.ReadStartElement("TimePeriod");
    while (!reader.Name.equalsIgnoreCase("TimePeriod")) {
      String name = reader.Name;
      reader.ReadStartElement();
      if (name.equalsIgnoreCase("Start")) {
        startDateTime = DateTime.ParseTime(reader.ReadString());
      } else if (name.equalsIgnoreCase("End")) {
        endDateTime = DateTime.ParseTime(reader.ReadString());
      } else {
        System.out.println("Warning: Unknown element '" + name + "' in TimePeriod");
      } 
      reader.ReadEndElement();
    } 
    reader.ReadEndElement();
    Period period = new Period(startDateTime, endDateTime);
    if (this.VERBOSE) {
      System.out.println("Start = " + period.getStartTime().ToShortTimeString());
      System.out.println("End   = " + period.getEndTime().ToShortTimeString());
      System.out.println("---------------------------------------------");
    } 
    return period;
  }
  
  public ShiftType ParseShiftType(XmlReader reader) {
    String shiftTypeID = reader.GetAttribute("ID");
    if (this.VERBOSE)
      System.out.println("Parsing Shift. ShiftID = " + shiftTypeID); 
    DateTime startDateTime = new DateTime();
    DateTime endDateTime = new DateTime();
    String colour = null;
    String label = null;
    String shiftName = null;
    String description = null;
    double hoursWorked = -1.0D;
    double freeTimeBefore = -1.0D;
    double freeTimeAfter = -1.0D;
    ArrayList<String> skills = new ArrayList<String>();
    boolean autoAllocate = true;
    reader.ReadStartElement("Shift");
    while (!reader.Name.equalsIgnoreCase("Shift")) {
      String name = reader.Name;
      reader.ReadStartElement();
      if (name.equalsIgnoreCase("Label")) {
        label = reader.ReadString();
      } else if (name.equalsIgnoreCase("Name")) {
        shiftName = reader.ReadString();
      } else if (name.equalsIgnoreCase("Description")) {
        description = reader.ReadString();
      } else if (name.equalsIgnoreCase("Colour")) {
        colour = reader.ReadString();
      } else if (name.equalsIgnoreCase("StartTime")) {
        startDateTime = DateTime.ParseTime(reader.ReadString());
      } else if (name.equalsIgnoreCase("EndTime")) {
        endDateTime = DateTime.ParseTime(reader.ReadString());
      } else if (name.equalsIgnoreCase("HoursWorked")) {
        hoursWorked = Double.parseDouble(reader.ReadString());
      } else if (name.equalsIgnoreCase("FreeTimeBefore")) {
        freeTimeBefore = Double.parseDouble(reader.ReadString());
      } else if (name.equalsIgnoreCase("FreeTimeAfter")) {
        freeTimeAfter = Double.parseDouble(reader.ReadString());
      } else if (name.equalsIgnoreCase("AutoAllocate")) {
        autoAllocate = XmlConvert.ToBoolean(reader.ReadString());
      } else if (name.equalsIgnoreCase("Skills")) {
        while (!reader.Name.equalsIgnoreCase("Skills")) {
          reader.ReadStartElement("Skill");
          String SkillID = reader.ReadString();
          reader.ReadEndElement();
          skills.add(SkillID);
        } 
      } else {
        System.out.println("Warning: Unknown element '" + name + "' in ShiftType");
      } 
      reader.ReadEndElement();
    } 
    reader.ReadEndElement();
    ShiftType shiftType = new ShiftType(shiftTypeID, startDateTime, endDateTime);
    if (colour != null)
      shiftType.HtmlColor = colour; 
    if (description != null)
      shiftType.Description = description; 
    if (label != null)
      shiftType.Label = label; 
    if (shiftName != null)
      shiftType.Name = shiftName; 
    if (hoursWorked >= 0.0D)
      shiftType.HoursWorked = hoursWorked; 
    if (freeTimeBefore >= 0.0D)
      shiftType.FreeTimeBefore = freeTimeBefore; 
    if (freeTimeAfter >= 0.0D)
      shiftType.FreeTimeAfter = freeTimeAfter; 
    shiftType.AutoAllocate = autoAllocate;
    if (this.VERBOSE) {
      System.out.println("Colour = " + shiftType.HtmlColor);
      System.out.println("Description = " + shiftType.Description);
      System.out.println("Label = " + shiftType.Label);
      System.out.println("HoursWorked = " + shiftType.HoursWorked);
      System.out.println("FreeTimeBefore = " + shiftType.FreeTimeBefore);
      System.out.println("FreeTimeAfter = " + shiftType.FreeTimeAfter);
    } 
    for (String SkillID : skills) {
      shiftType.AddSkill(SkillID);
      if (this.VERBOSE)
        System.out.println("SkillID = " + SkillID); 
    } 
    if (this.VERBOSE)
      System.out.println("---------------------------------------------"); 
    return shiftType;
  }
  
  private ShiftGroup ParseShiftGroup(XmlReader reader) {
    String ID = reader.GetAttribute("ID");
    if (this.VERBOSE)
      System.out.print("Shift Group: " + ID + " = "); 
    reader.ReadStartElement("ShiftGroup");
    ShiftGroup shiftGroup = new ShiftGroup(ID);
    while (!reader.Name.equalsIgnoreCase("ShiftGroup")) {
      reader.ReadStartElement("Shift");
      String shiftTypeID = reader.ReadString();
      reader.ReadEndElement();
      if (this.VERBOSE)
        System.out.print(String.valueOf(shiftTypeID) + ", "); 
      ShiftType st = this.schedulingPeriod.GetShiftType(shiftTypeID);
      if (st == null) {
        System.out.println("\nWarning: Unable to add ShiftType: '" + shiftTypeID + "' to ShiftGroup '" + ID + "' as ShiftType not defined");
        continue;
      } 
      shiftGroup.AddShiftType(st, true);
    } 
    reader.ReadEndElement();
    if (this.VERBOSE)
      System.out.println("-----------------------------------------"); 
    return shiftGroup;
  }
  
  private Skill ParseSkill(XmlReader reader) {
    String ID = reader.GetAttribute("ID");
    reader.ReadStartElement("Skill");
    if (this.VERBOSE)
      System.out.println("SkillID : " + ID); 
    Skill skill = new Skill(ID, "");
    while (!reader.Name.equalsIgnoreCase("Skill")) {
      if (reader.Name.equalsIgnoreCase("Label")) {
        reader.ReadStartElement("Label");
        skill.Label = reader.ReadString();
        reader.ReadEndElement();
      } 
    } 
    reader.ReadEndElement();
    return skill;
  }
  
  private SkillGroup ParseSkillGroup(XmlReader reader) {
    String ID = reader.GetAttribute("ID");
    reader.ReadStartElement("SkillGroup");
    if (this.VERBOSE)
      System.out.print("Skill Group: " + ID + " = "); 
    SkillGroup skillGroup = new SkillGroup(ID);
    while (!reader.Name.equalsIgnoreCase("SkillGroup")) {
      reader.ReadStartElement("Skill");
      String skillID = reader.ReadString();
      reader.ReadEndElement();
      Skill skill = this.schedulingPeriod.GetSkill(skillID);
      if (skill == null) {
        System.out.println("Warning: Unable to find skill " + skillID);
        continue;
      } 
      skillGroup.AddSkill(skill);
      if (this.VERBOSE)
        System.out.print(String.valueOf(skillID) + ", "); 
    } 
    reader.ReadEndElement();
    if (this.VERBOSE)
      System.out.println("-----------------------------------------"); 
    return skillGroup;
  }
  
  private void ParseDayOffRequestBetweenDates(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing day off request between dates..."); 
    boolean holiday = true;
    String holStr = null;
    holStr = reader.GetAttribute("holiday");
    if (holStr != null)
      holiday = XmlConvert.ToBoolean(holStr); 
    int weight = Integer.parseInt(reader.GetAttribute("weight"));
    reader.ReadStartElement("Request");
    reader.ReadStartElement("EmployeeID");
    String EmployeeID = reader.ReadString();
    reader.ReadEndElement();
    reader.ReadStartElement("StartDate");
    DateTime StartDate = DateTime.ParseDate(reader.ReadString());
    reader.ReadEndElement();
    reader.ReadStartElement("EndDate");
    DateTime EndDate = DateTime.ParseDate(reader.ReadString());
    reader.ReadEndElement();
    reader.ReadStartElement("MinimumDaysOff");
    int MinimumDaysOff = Integer.parseInt(reader.ReadString());
    reader.ReadEndElement();
    reader.ReadStartElement("MaximumDaysOff");
    int MaximumDaysOff = Integer.parseInt(reader.ReadString());
    reader.ReadEndElement();
    this.schedulingPeriod.RequestDaysOffBetweenDates(EmployeeID, weight, StartDate, 
        EndDate, MinimumDaysOff, 
        MaximumDaysOff, holiday);
    if (this.VERBOSE) {
      System.out.println("Weight = " + weight);
      System.out.println("EmployeeID = " + EmployeeID);
      System.out.println("StartDate = " + StartDate);
      System.out.println("EndDate = " + EndDate);
      System.out.println("MinimumDaysOff = " + MinimumDaysOff);
      System.out.println("MaximumDaysOff = " + MaximumDaysOff);
      System.out.println("Holiday = " + holiday);
      System.out.println(" ");
    } 
    reader.ReadEndElement();
  }
  
  private Pattern ParsePattern(XmlReader reader) {
    int weight = Integer.parseInt(reader.GetAttribute("weight"));
    DateTime date = new DateTime();
    int weekdayIndex = 0;
    ArrayList<String> shiftsArr = new ArrayList<String>();
    ArrayList<Pattern.DayType> shiftOrGroup = new ArrayList<Pattern.DayType>();
    reader.ReadStartElement("Pattern");
    int start = 0;
    Pattern.StartType startType = Pattern.StartType.All;
    if (reader.Name.equalsIgnoreCase("StartDay")) {
      reader.ReadStartElement("StartDay");
      String StartDay = reader.ReadString();
      reader.ReadEndElement();
      startType = Pattern.StartType.Day;
      if (StartDay.equalsIgnoreCase("Monday")) {
        start = this.schedulingPeriod.MondayPatternIndex;
        weekdayIndex = 0;
      } else if (StartDay.equalsIgnoreCase("Tuesday")) {
        start = this.schedulingPeriod.TuesdayPatternIndex;
        weekdayIndex = 1;
      } else if (StartDay.equalsIgnoreCase("Wednesday")) {
        start = this.schedulingPeriod.WednesdayPatternIndex;
        weekdayIndex = 2;
      } else if (StartDay.equalsIgnoreCase("Thursday")) {
        start = this.schedulingPeriod.ThursdayPatternIndex;
        weekdayIndex = 3;
      } else if (StartDay.equalsIgnoreCase("Friday")) {
        start = this.schedulingPeriod.FridayPatternIndex;
        weekdayIndex = 4;
      } else if (StartDay.equalsIgnoreCase("Saturday")) {
        start = this.schedulingPeriod.SaturdayPatternIndex;
        weekdayIndex = 5;
      } else if (StartDay.equalsIgnoreCase("Sunday")) {
        start = this.schedulingPeriod.SundayPatternIndex;
        weekdayIndex = 6;
      } else {
        start = this.schedulingPeriod.MondayPatternIndex;
      } 
    } else if (reader.Name.equalsIgnoreCase("StartDate")) {
      reader.ReadStartElement("StartDate");
      String StartDate = reader.ReadString();
      reader.ReadEndElement();
      startType = Pattern.StartType.Date;
      date = DateTime.ParseDate(StartDate);
      start = this.schedulingPeriod.ConvertDateToRosterDay(date);
    } 
    while (!reader.Name.equalsIgnoreCase("Pattern")) {
      String name = reader.Name;
      reader.ReadStartElement();
      String val = reader.ReadString();
      reader.ReadEndElement();
      shiftsArr.add(val);
      if (val.equalsIgnoreCase("")) {
        shiftOrGroup.add(Pattern.DayType.Off);
        continue;
      } 
      if (val.equalsIgnoreCase("*")) {
        shiftOrGroup.add(Pattern.DayType.Any);
        continue;
      } 
      if (val.equalsIgnoreCase("+")) {
        shiftOrGroup.add(Pattern.DayType.OtherWork);
        continue;
      } 
      if (val.equalsIgnoreCase("$")) {
        shiftOrGroup.add(Pattern.DayType.WorkingDay);
        continue;
      } 
      if (name.equalsIgnoreCase("Shift")) {
        shiftOrGroup.add(Pattern.DayType.Shift);
        continue;
      } 
      if (name.equalsIgnoreCase("NotShift")) {
        shiftOrGroup.add(Pattern.DayType.NotShift);
        continue;
      } 
      if (name.equalsIgnoreCase("ShiftGroup")) {
        shiftOrGroup.add(Pattern.DayType.ShiftGroup);
        continue;
      } 
      System.out.println("Unknown name:" + name);
    } 
    reader.ReadEndElement();
    int[] pattern = new int[shiftsArr.size()];
    Pattern.DayType[] dayTypes = shiftOrGroup.<Pattern.DayType>toArray(new Pattern.DayType[0]);
    ShiftType[] Shifts = new ShiftType[dayTypes.length];
    ShiftGroup[] ShiftGroups = new ShiftGroup[dayTypes.length];
    for (int i = 0; i < shiftsArr.size(); i++) {
      String str = shiftsArr.get(i);
      if (dayTypes[i] == Pattern.DayType.Shift || 
        dayTypes[i] == Pattern.DayType.NotShift) {
        ShiftType shiftType = this.schedulingPeriod.GetShiftType(str);
        if (shiftType == null) {
          System.out.println("Warning: ShiftType '" + str + "', in constraint Patterns not defined.");
          return null;
        } 
        pattern[i] = shiftType.Index;
        Shifts[i] = shiftType;
      } else if (dayTypes[i] == Pattern.DayType.ShiftGroup) {
        ShiftGroup shiftGroup = this.schedulingPeriod.GetShiftGroup(str);
        if (shiftGroup == null) {
          System.out.println("Warning: ShiftGroup '" + str + "', in constraint Patterns not defined.");
          return null;
        } 
        pattern[i] = shiftGroup.Index;
        ShiftGroups[i] = shiftGroup;
      } else {
        pattern[i] = -1;
      } 
    } 
    return new Pattern(start, startType, 
        pattern, dayTypes, weight, 
        weekdayIndex, date, Shifts, ShiftGroups);
  }
  
  private void ParseRequestedDayOff(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing Day Off..."); 
    boolean holiday = true;
    String holStr = null;
    holStr = reader.GetAttribute("holiday");
    if (holStr != null)
      holiday = XmlConvert.ToBoolean(holStr); 
    boolean working = false;
    String workingStr = null;
    workingStr = reader.GetAttribute("working");
    if (workingStr != null)
      working = XmlConvert.ToBoolean(workingStr); 
    int weight = Integer.parseInt(reader.GetAttribute("weight"));
    reader.ReadStartElement("DayOff");
    reader.ReadStartElement("EmployeeID");
    String EmployeeID = reader.ReadString();
    reader.ReadEndElement();
    boolean isDate = true;
    if (reader.Name.equalsIgnoreCase("Day"))
      isDate = false; 
    reader.ReadStartElement();
    String dateString = reader.ReadString();
    reader.ReadEndElement();
    if (isDate) {
      DateTime date = DateTime.ParseDate(dateString);
      if (this.schedulingPeriod.IsDateWithinSchedulingPeriod(date)) {
        int day = this.schedulingPeriod.ConvertDateToRosterDay(date);
        this.schedulingPeriod.RequestDayOff(EmployeeID, day, date, weight, holiday, working);
      } else {
        System.out.println("Unable to request day off. Date : " + date + " out of scheduling range.");
      } 
    } else {
      int day = Integer.parseInt(dateString);
      if (this.schedulingPeriod.IsDayWithinSchedulingPeriod(day)) {
        DateTime date = this.schedulingPeriod.ConvertRosterDayToDate(day);
        this.schedulingPeriod.RequestDayOff(EmployeeID, day, date, weight, holiday, working);
      } else {
        System.out.println("Unable to request day off. day : " + day + " out of scheduling range.");
      } 
    } 
    if (this.VERBOSE) {
      System.out.println("Weight = " + weight);
      System.out.println("EmployeeID = " + EmployeeID);
      System.out.println("Day/Date = " + dateString);
      System.out.println("Holiday = " + holiday);
      System.out.println("Working = " + working);
      System.out.println(" ");
    } 
    reader.ReadEndElement();
  }
  
  private void ParseRequestedDayOn(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing Day On..."); 
    int weight = Integer.parseInt(reader.GetAttribute("weight"));
    reader.ReadStartElement("DayOn");
    reader.ReadStartElement("EmployeeID");
    String employeeID = reader.ReadString();
    reader.ReadEndElement();
    boolean isDate = true;
    if (reader.Name.equalsIgnoreCase("Day"))
      isDate = false; 
    reader.ReadStartElement();
    String dateString = reader.ReadString();
    reader.ReadEndElement();
    if (isDate) {
      DateTime date = DateTime.ParseDate(dateString);
      if (this.schedulingPeriod.IsDateWithinSchedulingPeriod(date)) {
        int day = this.schedulingPeriod.ConvertDateToRosterDay(date);
        this.schedulingPeriod.RequestDayOn(employeeID, day, date, weight);
      } else {
        System.out.println("Unable to request day on. Date : " + date + " out of scheduling range.");
      } 
    } else {
      int day = Integer.parseInt(dateString);
      if (this.schedulingPeriod.IsDayWithinSchedulingPeriod(day)) {
        DateTime date = this.schedulingPeriod.ConvertRosterDayToDate(day);
        this.schedulingPeriod.RequestDayOn(employeeID, day, date, weight);
      } else {
        System.out.println("Unable to request day on. day : " + day + " out of scheduling range.");
      } 
    } 
    if (this.VERBOSE) {
      System.out.println("Weight = " + weight);
      System.out.println("EmployeeID = " + employeeID);
      System.out.println("Day/Date = " + dateString);
    } 
    reader.ReadEndElement();
  }
  
  private void ParseRequestedShiftOff(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing Shift Off..."); 
    int weight = Integer.parseInt(reader.GetAttribute("weight"));
    reader.ReadStartElement("ShiftOff");
    reader.ReadStartElement("ShiftTypeID");
    String ShiftTypeID = reader.ReadString();
    reader.ReadEndElement();
    reader.ReadStartElement("EmployeeID");
    String EmployeeID = reader.ReadString();
    reader.ReadEndElement();
    boolean isDate = true;
    if (reader.Name.equalsIgnoreCase("Day"))
      isDate = false; 
    reader.ReadStartElement();
    String dateString = reader.ReadString();
    reader.ReadEndElement();
    if (isDate) {
      DateTime date = DateTime.ParseDate(dateString);
      if (this.schedulingPeriod.IsDateWithinSchedulingPeriod(date)) {
        int day = this.schedulingPeriod.ConvertDateToRosterDay(date);
        this.schedulingPeriod.RequestShiftOff(EmployeeID, ShiftTypeID, day, date, weight);
      } else {
        System.out.println("Unable to request shift off. Date : " + date + " out of scheduling range.");
      } 
    } else {
      int day = Integer.parseInt(dateString);
      if (this.schedulingPeriod.IsDayWithinSchedulingPeriod(day)) {
        DateTime date = this.schedulingPeriod.ConvertRosterDayToDate(day);
        this.schedulingPeriod.RequestShiftOff(EmployeeID, ShiftTypeID, day, date, weight);
      } else {
        System.out.println("Unable to request shift off. Day : " + day + " out of scheduling range.");
      } 
    } 
    if (this.VERBOSE) {
      System.out.println("Weight = " + weight);
      System.out.println("ShiftTypeID = " + ShiftTypeID);
      System.out.println("EmployeeID = " + EmployeeID);
      System.out.println("Date/Day = " + dateString);
    } 
    reader.ReadEndElement();
  }
  
  private void ParseRequestedShiftOn(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing Shift On..."); 
    int weight = Integer.parseInt(reader.GetAttribute("weight"));
    reader.ReadStartElement("ShiftOn");
    String shiftGroupType = reader.Name;
    String ID = null;
    ArrayList<ShiftType> shifts = new ArrayList<ShiftType>();
    if (shiftGroupType.equalsIgnoreCase("ShiftGroupID") || shiftGroupType.equalsIgnoreCase("ShiftTypeID")) {
      reader.ReadStartElement();
      ID = reader.ReadString();
      reader.ReadEndElement();
    } else {
      reader.ReadStartElement("ShiftGroup");
      while (!reader.Name.equalsIgnoreCase("ShiftGroup")) {
        reader.ReadStartElement("Shift");
        String shiftTypeID = reader.ReadString();
        reader.ReadEndElement();
        ShiftType st = this.schedulingPeriod.GetShiftType(shiftTypeID);
        if (st == null) {
          System.out.println("\nWarning: ShiftType: '" + shiftTypeID + " not defined");
          continue;
        } 
        shifts.add(st);
      } 
      reader.ReadEndElement();
    } 
    reader.ReadStartElement("EmployeeID");
    String EmployeeID = reader.ReadString();
    reader.ReadEndElement();
    boolean isDate = true;
    if (reader.Name.equalsIgnoreCase("Day"))
      isDate = false; 
    reader.ReadStartElement();
    String dateString = reader.ReadString();
    reader.ReadEndElement();
    if (this.VERBOSE) {
      System.out.println("Weight = " + weight);
      System.out.println("EmployeeID = " + EmployeeID);
      System.out.println("Day/Date = " + dateString);
    } 
    reader.ReadEndElement();
    if (isDate) {
      DateTime date = DateTime.ParseDate(dateString);
      if (this.schedulingPeriod.IsDateWithinSchedulingPeriod(date)) {
        int day = this.schedulingPeriod.ConvertDateToRosterDay(date);
        if (shiftGroupType.equalsIgnoreCase("ShiftTypeID")) {
          this.schedulingPeriod.RequestShiftOn(EmployeeID, ID, day, date, weight);
        } else if (shiftGroupType.equalsIgnoreCase("ShiftGroupID")) {
          this.schedulingPeriod.RequestShiftGroupOn(EmployeeID, ID, day, date, weight);
        } else {
          this.schedulingPeriod.RequestShiftGroupOn(EmployeeID, shifts, day, date, weight);
        } 
      } else {
        System.out.println("Unable to request shift on. Date : " + date + " out of scheduling range.");
        return;
      } 
    } else {
      int day = Integer.parseInt(dateString);
      if (!this.schedulingPeriod.IsDayWithinSchedulingPeriod(day)) {
        System.out.println("Unable to request shift on. Day : " + day + " out of scheduling range.");
        return;
      } 
      DateTime date = this.schedulingPeriod.ConvertRosterDayToDate(day);
      if (shiftGroupType.equalsIgnoreCase("ShiftTypeID")) {
        this.schedulingPeriod.RequestShiftOn(EmployeeID, ID, day, date, weight);
      } else if (shiftGroupType.equalsIgnoreCase("ShiftGroupID")) {
        this.schedulingPeriod.RequestShiftGroupOn(EmployeeID, ID, day, date, weight);
      } else {
        this.schedulingPeriod.RequestShiftGroupOn(EmployeeID, shifts, day, date, weight);
      } 
    } 
  }
  
  private void ParsePartnership(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing Partnership..."); 
    String Type = reader.GetAttribute("Type");
    reader.ReadStartElement("Partnership");
    reader.ReadStartElement("Employee1ID");
    String Employee1ID = reader.ReadString();
    reader.ReadEndElement();
    reader.ReadStartElement("Employee2ID");
    String Employee2ID = reader.ReadString();
    reader.ReadEndElement();
    if (Type.equalsIgnoreCase("Tutorship")) {
      this.schedulingPeriod.RequestCollaboration(Employee1ID, Employee2ID);
    } else if (Type.equalsIgnoreCase("Separation")) {
      this.schedulingPeriod.RequestSeparation(Employee1ID, Employee2ID);
    } else {
      System.out.println("Warning: Unknown Work Partnership type: " + Type);
    } 
    if (this.VERBOSE) {
      System.out.println("Type = " + Type);
      System.out.println("Employee1ID = " + Employee1ID);
      System.out.println("Employee2ID = " + Employee2ID);
    } 
    reader.ReadEndElement();
  }
  
  private void ParseSchedulingHistory(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing SchedulingHistory"); 
    while (!reader.Name.equalsIgnoreCase("SchedulingHistory"))
      ParseEmployeeHistory(reader); 
    if (this.VERBOSE) {
      System.out.println("SchedulingHistory Parsed.");
      System.out.println("----------------------------------------------");
    } 
  }
  
  private void ParseEmployeeHistory(XmlReader reader) {
    String ID = reader.GetAttribute("EmployeeID");
    if (this.VERBOSE)
      System.out.println("Parsing EmployeeHistory, EmployeeID = " + ID); 
    EmployeeDescription employee = this.schedulingPeriod.GetEmployeeDescription(ID);
    if (employee == null)
      System.out.println("Error :: Employee ID: '" + ID + "' in scheduling history not defined in employees."); 
    this.schedulingPeriod.HistoryLoadedFromSchedulingPeriodFile = true;
    employee.SchedulingHistory.Exists = true;
    reader.ReadStartElement("EmployeeHistory");
    while (!reader.Name.equalsIgnoreCase("EmployeeHistory")) {
      String name = reader.Name;
      if (name.equalsIgnoreCase("PreviousAssignments")) {
        int day = 0;
        reader.ReadStartElement("PreviousAssignments");
        while (!reader.Name.equalsIgnoreCase("PreviousAssignments")) {
          reader.ReadStartElement("Day");
          if (this.VERBOSE)
            System.out.println("PreviousAssignments Day : " + day); 
          employee.SchedulingHistory.PreviousShiftAssignments = CSharpConversionHelper.ArrayResize(employee.SchedulingHistory.PreviousShiftAssignments, 
              employee.SchedulingHistory.PreviousShiftAssignments.length + this.schedulingPeriod.ShiftTypesCount);
          employee.SchedulingHistory.PreviousDayOffRequestWasWork = CSharpConversionHelper.ArrayResize(employee.SchedulingHistory.PreviousDayOffRequestWasWork, employee.SchedulingHistory.PreviousDayOffRequestWasWork.length + 1);
          employee.SchedulingHistory.DaysOfHistoryProvided++;
          while (!reader.Name.equalsIgnoreCase("Day")) {
            reader.ReadStartElement("Shift");
            String shiftTypeID = reader.ReadString();
            reader.ReadEndElement();
            if (shiftTypeID.equalsIgnoreCase("+")) {
              employee.SchedulingHistory.PreviousDayOffRequestWasWork[day] = true;
            } else {
              ShiftType shiftType = this.schedulingPeriod.GetShiftType(shiftTypeID);
              if (shiftType == null) {
                System.out.println("Error :: ShiftType ID: '" + shiftTypeID + "' in scheduling history not defined.");
                continue;
              } 
              int index = day * this.schedulingPeriod.ShiftTypesCount + shiftType.Index;
              employee.SchedulingHistory.PreviousShiftAssignments[index] = true;
            } 
            if (this.VERBOSE)
              System.out.println("  Shift : " + shiftTypeID); 
          } 
          reader.ReadEndElement();
          day++;
        } 
        reader.ReadEndElement();
        employee.SchedulingHistory.UpdatePreviousDayType(day, this.schedulingPeriod.ShiftTypesCount);
        continue;
      } 
      if (name.equalsIgnoreCase("LastDayType")) {
        reader.ReadStartElement("LastDayType");
        String LastDayType = reader.ReadString();
        if (LastDayType.equalsIgnoreCase("WorkingDay")) {
          employee.SchedulingHistory.LastDayType = 1;
        } else if (LastDayType.equalsIgnoreCase("NonWorkingDay")) {
          employee.SchedulingHistory.LastDayType = 0;
        } else if (LastDayType.equalsIgnoreCase("HolidayNonWorkingDay")) {
          employee.SchedulingHistory.LastDayType = 2;
        } 
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("LastDayShifts")) {
        reader.ReadStartElement("LastDayShifts");
        while (!reader.Name.equalsIgnoreCase("LastDayShifts")) {
          reader.ReadStartElement("Shift");
          String shiftTypeID = reader.ReadString();
          reader.ReadEndElement();
          ShiftType shiftType = this.schedulingPeriod.GetShiftType(shiftTypeID);
          if (shiftType == null)
            System.out.println("Error :: ShiftType ID: '" + shiftTypeID + "' in scheduling history not defined."); 
          employee.SchedulingHistory.LastDayAssignments[shiftType.Index] = true;
          if (this.VERBOSE)
            System.out.println("LastDayShifts = " + shiftTypeID); 
        } 
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveWorkingDays")) {
        reader.ReadStartElement("PreviousConsecutiveWorkingDays");
        employee.SchedulingHistory.PreviousConsecutiveWorkingDays = Integer.parseInt(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveWorkingDaysAndHoliday")) {
        reader.ReadStartElement("PreviousConsecutiveWorkingDaysAndHoliday");
        employee.SchedulingHistory.PreviousConsecutiveWorkingDaysAndHoliday = Integer.parseInt(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveFreeDays")) {
        reader.ReadStartElement("PreviousConsecutiveFreeDays");
        employee.SchedulingHistory.PreviousConsecutiveFreeDays = Integer.parseInt(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveFreeDaysAndHoliday")) {
        reader.ReadStartElement("PreviousConsecutiveFreeDaysAndHoliday");
        employee.SchedulingHistory.PreviousConsecutiveFreeDaysAndHoliday = Integer.parseInt(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveFreeDaysAndHolidayIncludesAtLeastOneNonWorkingDay")) {
        employee.SchedulingHistory.PreviousConsecutiveFreeDaysAndHolidayIncludesAtLeastOneNonWorkingDay = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveWorkingWeekends")) {
        reader.ReadStartElement("PreviousConsecutiveWorkingWeekends");
        employee.SchedulingHistory.PreviousConsecutiveWorkingWeekends = Integer.parseInt(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousWorkingBankHolidays")) {
        reader.ReadStartElement("PreviousWorkingBankHolidays");
        employee.SchedulingHistory.PreviousWorkingBankHolidays = Integer.parseInt(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("WeekendWorkedThreeWeeksAgo")) {
        employee.SchedulingHistory.WeekendWorkedThreeWeeksAgo = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("WeekendWorkedTwoWeeksAgo")) {
        employee.SchedulingHistory.WeekendWorkedTwoWeeksAgo = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("WeekendWorkedOneWeekAgo")) {
        employee.SchedulingHistory.WeekendWorkedOneWeekAgo = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousSaturdayWorked")) {
        employee.SchedulingHistory.PreviousSaturdayWorked = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousSundayWorked")) {
        employee.SchedulingHistory.PreviousSundayWorked = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousSaturdayRequestedHoliday")) {
        employee.SchedulingHistory.PreviousSaturdayRequestedHoliday = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousSundayRequestedHoliday")) {
        employee.SchedulingHistory.PreviousSundayRequestedHoliday = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("NightShiftThursday")) {
        employee.SchedulingHistory.NightShiftThursday = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("NightShiftFriday")) {
        employee.SchedulingHistory.NightShiftFriday = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousFridayWorked")) {
        employee.SchedulingHistory.PreviousFridayWorked = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousNightShift")) {
        employee.SchedulingHistory.PreviousNightShift = reader.ReadElementContentAsBoolean();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousFreeDaysAfterNightShift")) {
        reader.ReadStartElement("PreviousFreeDaysAfterNightShift");
        employee.SchedulingHistory.PreviousFreeDaysAfterNightShift = Integer.parseInt(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveHolidayDaysOff")) {
        reader.ReadStartElement("PreviousConsecutiveHolidayDaysOff");
        employee.SchedulingHistory.PreviousConsecutiveHolidayDaysOff = Integer.parseInt(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveShifts")) {
        reader.ReadStartElement("PreviousConsecutiveShifts");
        while (!reader.Name.equalsIgnoreCase("PreviousConsecutiveShifts")) {
          reader.ReadStartElement("PreviousConsecutiveShift");
          reader.ReadStartElement("ShiftTypeID");
          String shiftTypeID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Count");
          int count = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          ShiftType ShiftType = this.schedulingPeriod.GetShiftType(shiftTypeID);
          if (ShiftType == null) {
            System.out.println("Error :: ShiftType ID: '" + shiftTypeID + "' in scheduling history not defined.");
          } else {
            employee.SchedulingHistory.PreviousConsecutiveShifts[ShiftType.Index] = count;
          } 
          if (this.VERBOSE)
            System.out.println("Previous '" + shiftTypeID + "' shifts = " + count); 
        } 
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousConsecutiveShiftGroups")) {
        reader.ReadStartElement("PreviousConsecutiveShiftGroups");
        while (!reader.Name.equalsIgnoreCase("PreviousConsecutiveShiftGroups")) {
          reader.ReadStartElement("PreviousConsecutiveShiftGroup");
          reader.ReadStartElement("ShiftGroupID");
          String shiftGroupID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Count");
          int count = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          ShiftGroup ShiftGroup = this.schedulingPeriod.GetShiftGroup(shiftGroupID);
          if (ShiftGroup == null) {
            System.out.println("Error :: ShiftGroupID: '" + shiftGroupID + "' in scheduling history not defined.");
          } else {
            employee.SchedulingHistory.PreviousConsecutiveShiftGroups[ShiftGroup.Index] = count;
          } 
          if (this.VERBOSE)
            System.out.println("Previous '" + shiftGroupID + "' shiftGroups = " + count); 
        } 
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("DaysSinceShifts")) {
        reader.ReadStartElement("DaysSinceShifts");
        while (!reader.Name.equalsIgnoreCase("DaysSinceShifts")) {
          reader.ReadStartElement("DaysSinceShift");
          reader.ReadStartElement("ShiftTypeID");
          String shiftTypeID = reader.ReadString();
          reader.ReadEndElement();
          reader.ReadStartElement("Count");
          int count = Integer.parseInt(reader.ReadString());
          reader.ReadEndElement();
          reader.ReadEndElement();
          ShiftType ShiftType = this.schedulingPeriod.GetShiftType(shiftTypeID);
          if (ShiftType == null) {
            System.out.println("Error :: ShiftType ID: '" + shiftTypeID + "' in scheduling history not defined.");
          } else {
            employee.SchedulingHistory.DaysSinceShiftType[ShiftType.Index] = count;
          } 
          if (this.VERBOSE)
            System.out.println("DaysSinceShift '" + shiftTypeID + "' shifts = " + count); 
        } 
        reader.ReadEndElement();
        continue;
      } 
      if (name.equalsIgnoreCase("PreviousOvertime")) {
        reader.ReadStartElement("PreviousOvertime");
        employee.SchedulingHistory.PreviousOvertime = Double.parseDouble(reader.ReadString());
        reader.ReadEndElement();
        continue;
      } 
      System.out.println("Warning: Unknown element: " + name);
      break;
    } 
    reader.ReadEndElement();
    if (this.VERBOSE) {
      System.out.println("PreviousConsecutiveWorkingDays           : " + employee.SchedulingHistory.PreviousConsecutiveWorkingDays);
      System.out.println("PreviousConsecutiveWorkingDaysAndHoliday : " + employee.SchedulingHistory.PreviousConsecutiveWorkingDaysAndHoliday);
      System.out.println("PreviousConsecutiveFreeDays              : " + employee.SchedulingHistory.PreviousConsecutiveFreeDays);
      System.out.println("PreviousConsecutiveWorkingWeekends       : " + employee.SchedulingHistory.PreviousConsecutiveWorkingWeekends);
      System.out.println("PreviousWorkingBankHolidays              : " + employee.SchedulingHistory.PreviousWorkingBankHolidays);
      System.out.println("WeekendWorkedThreeWeeksAgo               : " + employee.SchedulingHistory.WeekendWorkedThreeWeeksAgo);
      System.out.println("WeekendWorkedTwoWeeksAgo                 : " + employee.SchedulingHistory.WeekendWorkedTwoWeeksAgo);
      System.out.println("WeekendWorkedOneWeekAgo                  : " + employee.SchedulingHistory.WeekendWorkedOneWeekAgo);
      System.out.println("PreviousSaturdayWorked                   : " + employee.SchedulingHistory.PreviousSaturdayWorked);
      System.out.println("PreviousSundayWorked                     : " + employee.SchedulingHistory.PreviousSundayWorked);
      System.out.println("PreviousSaturdayRequestedHoliday         : " + employee.SchedulingHistory.PreviousSaturdayRequestedHoliday);
      System.out.println("PreviousSundayRequestedHoliday           : " + employee.SchedulingHistory.PreviousSundayRequestedHoliday);
      System.out.println("NightShiftThursday                       : " + employee.SchedulingHistory.NightShiftThursday);
      System.out.println("NightShiftFriday                         : " + employee.SchedulingHistory.NightShiftFriday);
      System.out.println("PreviousFridayWorked                     : " + employee.SchedulingHistory.PreviousFridayWorked);
      System.out.println("PreviousNightShift                       : " + employee.SchedulingHistory.PreviousNightShift);
      System.out.println("PreviousFreeDaysAfterNightShift          : " + employee.SchedulingHistory.PreviousFreeDaysAfterNightShift);
      System.out.println("PreviousConsecutiveHolidayDaysOff        : " + employee.SchedulingHistory.PreviousConsecutiveHolidayDaysOff);
      System.out.println("PreviousOvertime                         : " + employee.SchedulingHistory.PreviousOvertime);
    } 
  }
  
  private void ParseFrozenDays(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing FrozenDays"); 
    while (!reader.Name.equalsIgnoreCase("FrozenDays"))
      ParseEmployeeFrozenDays(reader); 
    if (this.VERBOSE) {
      System.out.println("FrozenDays Parsed.");
      System.out.println("----------------------------------------------");
    } 
  }
  
  private void ParseEmployeeFrozenDays(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing EmployeeFrozenDays"); 
    reader.ReadStartElement("EmployeeFrozenDays");
    reader.ReadStartElement("EmployeeID");
    String employeeID = reader.ReadString();
    reader.ReadEndElement();
    EmployeeDescription employee = this.schedulingPeriod.GetEmployeeDescription(employeeID);
    if (employee == null)
      System.out.println("Error :: Employee ID: '" + employeeID + "' in EmployeeFrozenDays not defined in employees."); 
    while (!reader.Name.equalsIgnoreCase("EmployeeFrozenDays")) {
      reader.ReadStartElement("Date");
      String dateString = reader.ReadString();
      reader.ReadEndElement();
      if (employee != null) {
        DateTime date = DateTime.ParseDate(dateString);
        if (this.schedulingPeriod.IsDateWithinSchedulingPeriod(date)) {
          int day = this.schedulingPeriod.ConvertDateToRosterDay(date);
          employee.FrozenDay[day] = true;
          continue;
        } 
        System.out.println("Unable to freeze day. Date : " + date + " out of scheduling range.");
      } 
    } 
    reader.ReadEndElement();
  }
  
  private void ParsePreAssignments(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing PreAssignments"); 
    this.schedulingPeriod.ContainsInitialAssignments = true;
    while (!reader.Name.equalsIgnoreCase("PreAssignments"))
      ParseEmployeePreAssignments(reader); 
    if (this.VERBOSE) {
      System.out.println("PreAssignments Parsed.");
      System.out.println("----------------------------------------------");
    } 
  }
  
  private void ParseEmployeePreAssignments(XmlReader reader) {
    if (this.VERBOSE)
      System.out.println("Parsing EmployeeAssignments"); 
    reader.ReadStartElement("EmployeeAssignments");
    reader.ReadStartElement("EmployeeID");
    String employeeID = reader.ReadString();
    reader.ReadEndElement();
    EmployeeDescription employee = this.schedulingPeriod.GetEmployeeDescription(employeeID);
    if (employee == null)
      System.out.println("Error :: Employee ID: '" + employeeID + "' in PreAssignments not defined in employees."); 
    reader.ReadStartElement("Assignments");
    while (!reader.Name.equalsIgnoreCase("Assignments")) {
      reader.ReadStartElement("Assignment");
      reader.ReadStartElement("ShiftTypeID");
      String shiftTypeID = reader.ReadString();
      ShiftType st = this.schedulingPeriod.GetShiftType(shiftTypeID);
      if (st == null)
        System.out.println("Error :: ShiftType ID: '" + shiftTypeID + "' in PreAssignments not defined."); 
      reader.ReadEndElement();
      reader.ReadStartElement("Date");
      DateTime date = DateTime.ParseDate(reader.ReadString());
      reader.ReadEndElement();
      if (this.schedulingPeriod.IsDateWithinSchedulingPeriod(date)) {
        int day = this.schedulingPeriod.ConvertDateToRosterDay(date);
        if (employee != null && st != null) {
          int index = day * this.schedulingPeriod.ShiftTypesCount + st.Index;
          if (index >= 0 && index < employee.PreAssignments.length) {
            employee.PreAssignments[index] = true;
          } else {
            System.out.println("Error :: Unable to set pre-assignment. Day=" + day + " ShiftTypeID=" + shiftTypeID + " (index out of range).");
          } 
        } 
      } else {
        System.out.println("Unable to make pre-assignment. Date : " + date + " out of scheduling range.");
      } 
      reader.ReadEndElement();
    } 
    reader.ReadEndElement();
    reader.ReadEndElement();
  }
  
  private boolean AddContract(Contract contract) {
    String contractID = contract.ContractID;
    if (this.ContractsHash.containsKey(contractID)) {
      System.out.println("Unable to add Contract : " + contractID + " as contract with same ID already exists.");
      return false;
    } 
    this.ContractsHash.put(contractID, contract);
    return true;
  }
  
  private Contract GetContract(String contractID) {
    return this.ContractsHash.get(contractID);
  }
}
