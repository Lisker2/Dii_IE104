package org.example.util.ASAP.NRP.Core;

import org.example.util.ASAP.NRP.Core.Constraints.QMC.ShiftGroupRequest;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.Edge;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.Pattern;
import org.example.util.ASAP.NRP.Core.Constraints.TEC.PatternGroup;

public class CSharpConversionHelper {
  public static Object[] ArrayResize(Object[] input, int newSize) {
    Object[] newArray = new Object[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static Edge[] ArrayResize(Edge[] input, int newSize) {
    Edge[] newArray = new Edge[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static Pattern[] ArrayResize(Pattern[] input, int newSize) {
    Pattern[] newArray = new Pattern[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static PatternGroup[] ArrayResize(PatternGroup[] input, int newSize) {
    PatternGroup[] newArray = new PatternGroup[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static HoursBetweenDates[] ArrayResize(HoursBetweenDates[] input, int newSize) {
    HoursBetweenDates[] newArray = new HoursBetweenDates[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static DaysOffRequestBetweenDates[] ArrayResize(DaysOffRequestBetweenDates[] input, int newSize) {
    DaysOffRequestBetweenDates[] newArray = new DaysOffRequestBetweenDates[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static ShiftGroupRequest[] ArrayResize(ShiftGroupRequest[] input, int newSize) {
    ShiftGroupRequest[] newArray = new ShiftGroupRequest[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static EmployeeDescription[] ArrayResize(EmployeeDescription[] input, int newSize) {
    EmployeeDescription[] newArray = new EmployeeDescription[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static SkillGroup[] ArrayResize(SkillGroup[] input, int newSize) {
    SkillGroup[] newArray = new SkillGroup[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static ShiftGroup[] ArrayResize(ShiftGroup[] input, int newSize) {
    ShiftGroup[] newArray = new ShiftGroup[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static ShiftType[] ArrayResize(ShiftType[] input, int newSize) {
    ShiftType[] newArray = new ShiftType[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static Skill[] ArrayResize(Skill[] input, int newSize) {
    Skill[] newArray = new Skill[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static int[] ArrayResize(int[] input, int newSize) {
    int[] newArray = new int[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
  
  public static boolean[] ArrayResize(boolean[] input, int newSize) {
    boolean[] newArray = new boolean[newSize];
    System.arraycopy(input, 0, newArray, 0, (newSize < input.length) ? newSize : input.length);
    return newArray;
  }
}
