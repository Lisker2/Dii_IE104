package org.example.util.ASAP.NRP.Core.Constraints.QMC;

import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.ShiftGroup;

public class ShiftGroupRequest {
  public int Day;
  
  public DateTime Date;
  
  public int Weight;
  
  public ShiftGroup ShiftGroup;
  
  public ShiftGroupRequest(ShiftGroup shiftGroup, int day, DateTime date, int Weight) {
    this.Weight = 0;
    this.ShiftGroup = shiftGroup;
    this.Day = day;
    this.Weight = Weight;
    this.Date = date;
  }
}
