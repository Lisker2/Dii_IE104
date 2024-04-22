package org.example.util.ASAP.NRP.Core;

public class ShiftTypeAndValue {
  public int Value;
  
  public ShiftType ShiftType;
  
  public ShiftTypeAndValue(ShiftType shiftType, int value) {
    this.ShiftType = shiftType;
    this.Value = value;
  }
}
