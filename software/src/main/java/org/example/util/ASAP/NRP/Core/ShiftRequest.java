package org.example.util.ASAP.NRP.Core;

public class ShiftRequest {
  public DateTime Date;
  
  public ShiftType ShiftType;
  
  public int Weight;
  
  public ShiftRequest(DateTime date, ShiftType shiftType, int weight) {
    this.Date = date;
    this.ShiftType = shiftType;
    this.Weight = weight;
  }
}
