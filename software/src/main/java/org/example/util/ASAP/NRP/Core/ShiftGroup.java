package org.example.util.ASAP.NRP.Core;

public class ShiftGroup {
  public ShiftType[] Group = new ShiftType[0];
  
  public String ID;
  
  public int Index;
  
  public String Label;
  
  public ShiftGroup(String id) {
    this.ID = "";
    this.Index = -1;
    this.Label = "";
    this.ID = id;
  }
  
  public void AddShiftType(ShiftType shiftType, boolean notifyShiftType) {
    if (Contains(shiftType)) {
      System.out.println("Warning: ShiftGroup: '" + this.ID + "' already contains ShiftType " + shiftType.ID + ".");
      return;
    } 
    this.Group = CSharpConversionHelper.ArrayResize(this.Group, this.Group.length + 1);
    this.Group[this.Group.length - 1] = shiftType;
    this.Label = (this.Group[0]).Label;
    for (int i = 1; i < this.Group.length; i++)
      this.Label = String.valueOf(this.Label) + " or " + (this.Group[i]).Label; 
    if (notifyShiftType)
      shiftType.AddShiftGroup(this); 
  }
  
  private boolean Contains(ShiftType shiftType) {
    for (int i = 0; i < this.Group.length; i++) {
      if (this.Group[i].equals(shiftType))
        return true; 
    } 
    return false;
  }
  
  public boolean Contains(String ShiftTypeID) {
    byte b;
    int i;
    ShiftType[] arrayOfShiftType;
    for (i = (arrayOfShiftType = this.Group).length, b = 0; b < i; ) {
      ShiftType st = arrayOfShiftType[b];
      if (st.ID.equals(ShiftTypeID))
        return true; 
      b++;
    } 
    return false;
  }
}
