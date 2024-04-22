package org.example.util.ASAP.NRP.Solvers.VariableDepthSearch;

import org.example.util.ASAP.NRP.Core.Shift;
import org.example.util.ASAP.NRP.Core.ShiftType;

public interface TestShiftDB {
  Shift GetShift(ShiftType paramShiftType, int paramInt);
  
  Shift GetShift(int paramInt1, int paramInt2);
  
  Shift GetTestShift(int paramInt1, int paramInt2);
  
  boolean getSchedulingPeriodContainsNonAutoAssignShifts();
  
  boolean[] getEmployeeDescriptionHasFrozenDays();
}
