package org.example.util.ASAP.NRP.Core;

import org.example.util.ASAP.NRP.Core.Parsers.Cover;
import org.example.util.ASAP.NRP.Core.Parsers.CoverSpecification;
import org.example.util.ASAP.NRP.Core.Parsers.DateCoverSpecification;
import org.example.util.ASAP.NRP.Core.Parsers.DayCoverSpecification;
import java.util.ArrayList;
import java.util.Arrays;

public class CoverRequirements {
  SchedulingPeriod schedulingPeriod;
  
  ArrayList<CoverSpecification> CoverSpecifications = new ArrayList<CoverSpecification>();
  
  public CoverRequirement[] Requirements = new CoverRequirement[0];
  
  public CoverRequirements(SchedulingPeriod schedulingPeriod) {
    this.schedulingPeriod = schedulingPeriod;
  }
  
  public ArrayList<CoverSpecification> GetCoverSpecifications() {
    return this.CoverSpecifications;
  }
  
  public boolean AddCoverSpecification(CoverSpecification coverSpec, boolean updateArrays) {
    if (coverSpec instanceof DateCoverSpecification) {
      DateTime date = ((DateCoverSpecification)coverSpec).Date;
      if (!this.schedulingPeriod.IsDateWithinSchedulingPeriod(date)) {
        System.out.println("Date Specific Cover for date : " + date + " not in scheduling period range. ");
        return false;
      } 
    } 
    this.CoverSpecifications.add(coverSpec);
    if (updateArrays)
      UpdateCoverRequirementArrays(); 
    return true;
  }
  
  public void UpdateCoverRequirementArrays() {
    ArrayList<CoverRequirement> allReqs = new ArrayList<CoverRequirement>();
    boolean anySkillFound = false;
    ArrayList<String> singleSkills = new ArrayList<String>();
    ArrayList<String> skillGroups = new ArrayList<String>();
    for (CoverSpecification spec : GetCoverSpecifications()) {
      for (Cover cover : spec.CoverRequirements) {
        String skillID = cover.SkillID;
        if (cover.SkillType == CoverRequirement.SkillTypes.AnySkill) {
          if (!anySkillFound) {
            CoverRequirement req = new CoverRequirement(skillID, cover.SkillType, this.schedulingPeriod);
            allReqs.add(req);
            anySkillFound = true;
          } 
          continue;
        } 
        if (cover.SkillType == CoverRequirement.SkillTypes.SingleSkill) {
          if (!singleSkills.contains(skillID)) {
            singleSkills.add(skillID);
            CoverRequirement req = new CoverRequirement(skillID, cover.SkillType, this.schedulingPeriod);
            allReqs.add(req);
          } 
          continue;
        } 
        if (cover.SkillType == CoverRequirement.SkillTypes.SkillGroup)
          if (!skillGroups.contains(skillID)) {
            skillGroups.add(skillID);
            CoverRequirement req = new CoverRequirement(skillID, cover.SkillType, this.schedulingPeriod);
            allReqs.add(req);
          }  
      } 
    } 
    this.Requirements = allReqs.<CoverRequirement>toArray(this.Requirements);
    try {
      Arrays.sort((Object[])this.Requirements);
    } catch (RuntimeException e) {
      e.printStackTrace();
    } 
    try {
      for (int i = 0; i < this.Requirements.length; i++)
        (this.Requirements[i]).Index = i; 
      for (CoverSpecification obj : GetCoverSpecifications()) {
        if (!(obj instanceof DayCoverSpecification))
          continue; 
        DayCoverSpecification dcs = (DayCoverSpecification)obj;
        DateTime tempDate = this.schedulingPeriod.StartDate;
        DateTime endDate = this.schedulingPeriod.EndDate;
        int dayNum = 0;
        while (tempDate.isLessThan(endDate)) {
          if (dcs.Day.equals(tempDate.getDayOfWeekString()))
            for (Cover obj2 : dcs.CoverRequirements) {
              Cover cover = obj2;
              if (cover.Type == Cover.CoverType.Shift) {
                int index = (this.schedulingPeriod.GetShiftType(cover.ID)).Index;
                CoverRequirement req = GetCoverRequirement(cover.SkillID, cover.SkillType);
                req.CoverPerShiftUsed = true;
                req.MinCoverPerShift[index][dayNum] = cover.Min;
                req.MaxCoverPerShift[index][dayNum] = cover.Max;
                req.PrefCoverPerShift[index][dayNum] = cover.Preferred;
                continue;
              } 
              if (cover.Type == Cover.CoverType.ShiftGroup) {
                int index = (this.schedulingPeriod.GetShiftGroup(cover.ID)).Index;
                CoverRequirement req = GetCoverRequirement(cover.SkillID, cover.SkillType);
                req.CoverPerShiftGroupUsed = true;
                req.MinCoverPerShiftGroup[index][dayNum] = cover.Min;
                req.MaxCoverPerShiftGroup[index][dayNum] = cover.Max;
                req.PrefCoverPerShiftGroup[index][dayNum] = cover.Preferred;
                continue;
              } 
              if (cover.Type == Cover.CoverType.Period) {
                int index = (this.schedulingPeriod.GetDayPeriod(cover.Period)).Index;
                CoverRequirement req = GetCoverRequirement(cover.SkillID, cover.SkillType);
                req.CoverPerPeriodUsed = true;
                req.MinCoverPerPeriod[index][dayNum] = cover.Min;
                req.MaxCoverPerPeriod[index][dayNum] = cover.Max;
                req.PrefCoverPerPeriod[index][dayNum] = cover.Preferred;
              } 
            }  
          dayNum++;
          tempDate = tempDate.AddDays(1);
        } 
      } 
    } catch (RuntimeException e) {
      e.printStackTrace();
    } 
    for (CoverSpecification obj : GetCoverSpecifications()) {
      if (!(obj instanceof DateCoverSpecification))
        continue; 
      DateCoverSpecification dcs = (DateCoverSpecification)obj;
      if (!this.schedulingPeriod.IsDateWithinSchedulingPeriod(dcs.Date))
        continue; 
      int dayNum = this.schedulingPeriod.ConvertDateToRosterDay(dcs.Date);
      for (Cover obj2 : dcs.CoverRequirements) {
        Cover cover = obj2;
        if (cover.Type == Cover.CoverType.Shift) {
          int index = (this.schedulingPeriod.GetShiftType(cover.ID)).Index;
          CoverRequirement req = GetCoverRequirement(cover.SkillID, cover.SkillType);
          req.CoverPerShiftUsed = true;
          req.MinCoverPerShift[index][dayNum] = cover.Min;
          req.MaxCoverPerShift[index][dayNum] = cover.Max;
          req.PrefCoverPerShift[index][dayNum] = cover.Preferred;
          continue;
        } 
        if (cover.Type == Cover.CoverType.ShiftGroup) {
          int index = (this.schedulingPeriod.GetShiftGroup(cover.ID)).Index;
          CoverRequirement req = GetCoverRequirement(cover.SkillID, cover.SkillType);
          req.CoverPerShiftGroupUsed = true;
          req.MinCoverPerShiftGroup[index][dayNum] = cover.Min;
          req.MaxCoverPerShiftGroup[index][dayNum] = cover.Max;
          req.PrefCoverPerShiftGroup[index][dayNum] = cover.Preferred;
          continue;
        } 
        if (cover.Type == Cover.CoverType.Period) {
          int index = (this.schedulingPeriod.GetDayPeriod(cover.Period)).Index;
          CoverRequirement req = GetCoverRequirement(cover.SkillID, cover.SkillType);
          req.CoverPerPeriodUsed = true;
          req.MinCoverPerPeriod[index][dayNum] = cover.Min;
          req.MaxCoverPerPeriod[index][dayNum] = cover.Max;
          req.PrefCoverPerPeriod[index][dayNum] = cover.Preferred;
        } 
      } 
    } 
  }
  
  private CoverRequirement GetCoverRequirement(String skillID, CoverRequirement.SkillTypes skillType) {
    if (this.Requirements == null)
      return null; 
    for (int i = 0; i < this.Requirements.length; i++) {
      if ((this.Requirements[i]).SkillType == skillType && 
        skillID.equals((this.Requirements[i]).SkillID))
        return this.Requirements[i]; 
    } 
    return null;
  }
}
