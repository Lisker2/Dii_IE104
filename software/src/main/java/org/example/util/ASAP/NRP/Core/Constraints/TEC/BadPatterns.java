package org.example.util.ASAP.NRP.Core.Constraints.TEC;

import org.example.util.ASAP.NRP.Core.CSharpConversionHelper;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.EmployeeDescription;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.ShiftGroup;
import java.util.Random;

public class BadPatterns implements SoftConstraint {
  Node node0;
  
  int nodesCount = 0;
  
  int penalty = 0;
  
  int[] dayPens;
  
  int numDaysInPeriod;
  
  int shiftTypeCount;
  
  Stack[] stacks;
  
  int[] hashNodesRandVals;
  
  Stack tempStack1;
  
  Stack tempStack2;
  
  boolean temp1 = true;
  
  EmployeeDescription employeeDescription;
  
  boolean datePatternsExist = false;
  
  public String Title = "Sequences to avoid";
  
  public String getTitle() {
    return this.Title;
  }
  
  public int getWeight() {
    return this.Weight;
  }
  
  public String LongTitle = "Sequences which are not allowed";
  
  public int Weight = 0;
  
  public String ID = "TEC.BadPatterns";
  
  public void Delete(EmployeeDescription employee) {
    employee.Contract.BadPatternsIsOn = false;
    employee.Contract.BadPatterns = new Pattern[0];
    employee.Contract.WeekDayBadPatterns = new Pattern[0];
    employee.Contract.DateBadPatterns = new Pattern[0];
  }
  
  public boolean IncludeDateSpecificXML = true;
  
  boolean VIOLATION_FLAGGING;
  
  public String ToXml(EmployeeDescription employee) {
    return "";
  }
  
  public String GetDescription(EmployeeDescription employeeDescription) {
    if (employeeDescription.Contract.BadPatterns == null || 
      employeeDescription.Contract.BadPatterns.length == 0)
      return ""; 
    int maxLength = 0;
    for (int i = 0; i < employeeDescription.Contract.BadPatterns.length; i++) {
      Pattern pattern = employeeDescription.Contract.BadPatterns[i];
      if (pattern.Length > maxLength)
        maxLength = pattern.Length; 
    } 
    String str = "<table cellpadding=\"2\" cellspacing=\"0\" style=\"font-family: arial, sans-serif; font-size: x-small; border: 0px solid #E0E0E0; border-collapse: collapse;\" border=\"1\"><tr style=\"background-color: #F8F8F8\"><td style=\"border-width: 0px\">Start day</td>";
    int j;
    for (j = 1; j <= maxLength; j++)
      str = String.valueOf(str) + "<td style=\"width:24px; text-align: center; border-width: 0px\">" + j + "</td>"; 
    str = String.valueOf(str) + "<td style=\"border-width: 0px\">Weight</td></tr>";
    for (j = 0; j < employeeDescription.Contract.BadPatterns.length; j++) {
      Pattern pattern = employeeDescription.Contract.BadPatterns[j];
      str = String.valueOf(str) + "<tr>";
      if (pattern.StartDayType == Pattern.StartType.Day) {
        str = String.valueOf(str) + "<td class=\"ptrnTblCell\" valign=\"top\">" + pattern.getStartDayOrDate() + "s</td>";
      } else if (pattern.StartDayType == Pattern.StartType.Date) {
        str = String.valueOf(str) + "<td class=\"ptrnTblCell\" valign=\"top\">" + pattern.getStartDayOrDate() + "</td>";
      } else {
        str = String.valueOf(str) + "<td class=\"ptrnTblCell\" valign=\"top\">All days</td>";
      } 
      str = String.valueOf(str) + pattern.ToHtmlTableRow();
      for (int k = 1; k <= maxLength - pattern.Length; k++)
        str = String.valueOf(str) + "<td class=\"ptrnTblCell\">&nbsp;</td>"; 
      str = String.valueOf(str) + "<td class=\"ptrnTblCell\" valign=\"top\" style=\"text-align: right\">" + pattern.Weight + "</td></tr>";
    } 
    str = String.valueOf(str) + "</table>";
    return str;
  }
  
  public String GetDescription(Employee employee) {
    return GetDescription(employee.EmployeeDescription);
  }
  
  class Node {
    public boolean accepting = false;
    
    public Pattern acceptingPattern;
    
    public Edge[] Edges = new Edge[0];
    
    public int index;
    
    public boolean terminal = true;
    
    public Node(int index) {
      this.index = index;
    }
    
    public void AddEdge(Edge edge) {
      this.terminal = false;
      this.Edges = CSharpConversionHelper.ArrayResize(this.Edges, this.Edges.length + 1);
      this.Edges[this.Edges.length - 1] = edge;
    }
  }
  
  class Stack {
    public Node[] items;
    
    public int size = 0;
    
    public int hash = 0;
    
    public Stack(int maxSize) {
      this.items = new Node[maxSize];
    }
  }
  
  public void ConstructGraph() {
    this.penalty = 0;
    this.node0 = new Node(0);
    this.nodesCount = 1;
    this.numDaysInPeriod = this.employeeDescription.SchedulingPeriod.NumDaysInPeriod;
    this.shiftTypeCount = this.employeeDescription.SchedulingPeriod.ShiftTypesCount;
    this.dayPens = new int[this.numDaysInPeriod];
    this.datePatternsExist = false;
    for (int patternIndex = 0; patternIndex < this.employeeDescription.Contract.BadPatterns.length; patternIndex++) {
      Pattern pattern = this.employeeDescription.Contract.BadPatterns[patternIndex];
      AddPattern(pattern);
    } 
    Random rand = new Random(1L);
    this.hashNodesRandVals = new int[this.nodesCount];
    int i;
    for (i = 0; i < this.nodesCount; i++)
      this.hashNodesRandVals[i] = rand.nextInt(2147483647); 
    this.stacks = new Stack[this.numDaysInPeriod + 1];
    for (i = 0; i < this.stacks.length; i++) {
      Stack stack = new Stack(this.nodesCount);
      stack.items[0] = this.node0;
      stack.size = 1;
      stack.hash = this.hashNodesRandVals[0];
      this.stacks[i] = stack;
    } 
    this.tempStack1 = new Stack(this.nodesCount);
    this.tempStack1.items[0] = this.node0;
    this.tempStack1.size = 1;
    this.tempStack1.hash = this.hashNodesRandVals[0];
    this.tempStack2 = new Stack(this.nodesCount);
    this.tempStack2.items[0] = this.node0;
    this.tempStack2.size = 1;
    this.tempStack2.hash = this.hashNodesRandVals[0];
    ProcessHistory();
  }
  
  private void ProcessHistory() {
    if (!this.employeeDescription.SchedulingHistory.Exists)
      return; 
    int daysCount = this.employeeDescription.SchedulingHistory.PreviousDayType.length;
    Stack nextStack = new Stack(this.nodesCount);
    nextStack.items[0] = this.node0;
    nextStack.size = 1;
    nextStack.hash = this.hashNodesRandVals[0];
    Stack tStack1 = new Stack(this.nodesCount);
    tStack1.items[0] = this.node0;
    tStack1.size = 1;
    tStack1.hash = this.hashNodesRandVals[0];
    Stack tStack2 = new Stack(this.nodesCount);
    tStack2.items[0] = this.node0;
    tStack2.size = 1;
    tStack2.hash = this.hashNodesRandVals[0];
    boolean t1 = true;
    for (int day = 0; day < daysCount; day++) {
      Stack stack = nextStack;
      if (t1) {
        nextStack = tStack1;
      } else {
        nextStack = tStack2;
      } 
      t1 = !t1;
      nextStack.size = 1;
      nextStack.hash = this.hashNodesRandVals[0];
      for (int i = stack.size - 1; i >= 0; i--) {
        Node node = stack.items[i];
        for (int j = 0; j < node.Edges.length; j++) {
          Edge edge = node.Edges[j];
          boolean match = false;
          if (edge.type == Pattern.DayType.Off) {
            if (this.employeeDescription.SchedulingHistory.PreviousDayType[day] != 1 && 
              !this.employeeDescription.SchedulingHistory.PreviousDayOffRequestWasWork[day])
              match = true; 
          } else if (edge.type == Pattern.DayType.OtherWork) {
            if (this.employeeDescription.SchedulingHistory.PreviousDayOffRequestWasWork[day])
              match = true; 
          } else if (edge.type == Pattern.DayType.WorkingDay) {
            if (this.employeeDescription.SchedulingHistory.PreviousDayType[day] == 1 || 
              this.employeeDescription.SchedulingHistory.PreviousDayOffRequestWasWork[day])
              match = true; 
          } else if (edge.type == Pattern.DayType.Any) {
            match = true;
          } else if (edge.type == Pattern.DayType.Shift) {
            if (this.employeeDescription.SchedulingHistory.PreviousShiftAssignments[day * this.shiftTypeCount + edge.value])
              match = true; 
          } else if (edge.type == Pattern.DayType.NotShift) {
            if (!this.employeeDescription.SchedulingHistory.PreviousShiftAssignments[day * this.shiftTypeCount + edge.value])
              match = true; 
          } else if (edge.type == Pattern.DayType.ShiftGroup) {
            ShiftGroup grp = this.employeeDescription.SchedulingPeriod.GetShiftGroup(edge.value);
            for (int x = 0; x < grp.Group.length; x++) {
              if (this.employeeDescription.SchedulingHistory.PreviousShiftAssignments[day * this.shiftTypeCount + (grp.Group[x]).Index]) {
                match = true;
                break;
              } 
            } 
          } else {
            System.out.println("Uknown edge type!");
          } 
          if (match) {
            Node nextNode = edge.nextNode;
            if (!nextNode.terminal) {
              nextStack.items[nextStack.size] = nextNode;
              nextStack.size++;
              nextStack.hash ^= this.hashNodesRandVals[nextNode.index];
            } 
          } 
        } 
      } 
    } 
    this.stacks[0] = nextStack;
  }
  
  private void AddPattern(Pattern pattern) {
    if (pattern.StartDayType == Pattern.StartType.Day || 
      pattern.StartDayType == Pattern.StartType.Date) {
      this.datePatternsExist = true;
      return;
    } 
    Node currentNode = this.node0;
    for (int k = 0; k < pattern.ShiftIndices.length; k++) {
      int index = pattern.ShiftIndices[k];
      boolean match = false;
      for (int j = 0; j < currentNode.Edges.length; j++) {
        Edge edge = currentNode.Edges[j];
        if (edge.type == pattern.DayTypes[k])
          if (edge.type == Pattern.DayType.Shift || 
            edge.type == Pattern.DayType.NotShift || 
            edge.type == Pattern.DayType.ShiftGroup) {
            if (edge.value == index) {
              currentNode = edge.nextNode;
              match = true;
              break;
            } 
          } else {
            currentNode = edge.nextNode;
            match = true;
            break;
          }  
      } 
      if (!match) {
        Node newNode = new Node(this.nodesCount);
        this.nodesCount++;
        Edge e = new Edge();
        e.nextNode = newNode;
        e.type = pattern.DayTypes[k];
        e.value = index;
        currentNode.AddEdge(e);
        currentNode = newNode;
      } 
    } 
    currentNode.accepting = true;
    currentNode.acceptingPattern = pattern;
  }
  
  public BadPatterns(EmployeeDescription employeeDescription) {
    this.VIOLATION_FLAGGING = true;
    this.employeeDescription = employeeDescription;
    ConstructGraph();
  }
  
  public int Calculate(Employee employee, int startDay, int endDay, boolean updateStructure) {
    Stack nextStack = this.stacks[startDay];
    int newPenalty = this.penalty;
    for (int day = startDay; day < this.numDaysInPeriod; day++) {
      Stack stack = nextStack;
      int originalHash = (this.stacks[day + 1]).hash;
      newPenalty -= this.dayPens[day];
      if (updateStructure) {
        nextStack = this.stacks[day + 1];
        this.dayPens[day] = 0;
      } else {
        if (this.temp1) {
          nextStack = this.tempStack1;
        } else {
          nextStack = this.tempStack2;
        } 
        this.temp1 = !this.temp1;
      } 
      nextStack.size = 1;
      nextStack.hash = this.hashNodesRandVals[0];
      for (int i = stack.size - 1; i >= 0; i--) {
        Node node = stack.items[i];
        for (int j = 0; j < node.Edges.length; j++) {
          Edge edge = node.Edges[j];
          boolean match = false;
          if (edge.type == Pattern.DayType.Off) {
            if (employee.DayType[day] != 1 && !employee.EmployeeDescription.DayOffRequestIsWork[day])
              match = true; 
          } else if (edge.type == Pattern.DayType.OtherWork) {
            if (employee.EmployeeDescription.DayOffRequestIsWork[day])
              match = true; 
          } else if (edge.type == Pattern.DayType.WorkingDay) {
            if (employee.DayType[day] == 1 || employee.EmployeeDescription.DayOffRequestIsWork[day])
              match = true; 
          } else if (edge.type == Pattern.DayType.Any) {
            match = true;
          } else if (edge.type == Pattern.DayType.Shift) {
            if (employee.ShiftsOnDay[day][edge.value] != null)
              match = true; 
          } else if (edge.type == Pattern.DayType.NotShift) {
            if (employee.ShiftsOnDay[day][edge.value] == null)
              match = true; 
          } else if (edge.type == Pattern.DayType.ShiftGroup) {
            if (employee.ShiftGroupPerDayCount[day][edge.value] > 0)
              match = true; 
          } else {
            System.out.println("Uknown edge type!");
          } 
          if (match) {
            Node nextNode = edge.nextNode;
            if (nextNode.accepting) {
              newPenalty += nextNode.acceptingPattern.Weight;
              if (updateStructure) {
                this.dayPens[day] = this.dayPens[day] + nextNode.acceptingPattern.Weight;
                if (this.VIOLATION_FLAGGING) {
                  int start = day - nextNode.acceptingPattern.Length + 1;
                  if (start < 0)
                    start = 0; 
                  int x;
                  for (x = start; x <= day; x++)
                    employee.ConstraintViolationPenalties[x] = employee.ConstraintViolationPenalties[x] + nextNode.acceptingPattern.Weight; 
                  if (SoftConstraints.UpdateViolationDescriptions)
                    for (x = start; x <= day; x++) {
                      if (day - nextNode.acceptingPattern.Length < -1) {
                        employee.ViolationDescriptions[x] = String.valueOf(employee.ViolationDescriptions[x]) + "Undesirable sequence (includes previous schedule): " + nextNode.acceptingPattern.toString() + System.getProperty("line.separator");
                      } else {
                        employee.ViolationDescriptions[x] = String.valueOf(employee.ViolationDescriptions[x]) + "Undesirable sequence: " + nextNode.acceptingPattern.toString() + System.getProperty("line.separator");
                      } 
                    }  
                } 
              } 
            } 
            if (!nextNode.terminal) {
              nextStack.items[nextStack.size] = nextNode;
              nextStack.size++;
              nextStack.hash ^= this.hashNodesRandVals[nextNode.index];
            } 
          } 
        } 
      } 
      if (day + 1 > endDay && originalHash == nextStack.hash)
        break; 
    } 
    if (updateStructure)
      this.penalty = newPenalty; 
    if (this.datePatternsExist)
      newPenalty += TestDatePatterns(employee, startDay, endDay); 
    return newPenalty;
  }
  
  public int Calculate(Employee employee, int startDay, int endDay) {
    return Calculate(employee, startDay, endDay, true);
  }
  
  public int Calculate(Employee employee, int maxPenalty, int startDay, int endDay) {
    return Calculate(employee, startDay, endDay, true);
  }
  
  public int Calculate(Employee employee) {
    return Calculate(employee, 0, this.numDaysInPeriod - 1, true);
  }
  
  private int TestDatePatterns(Employee employee, int startDay, int endDay) {
    int penalty = 0;
    int patternIndex;
    for (patternIndex = 0; patternIndex < this.employeeDescription.Contract.WeekDayBadPatterns.length; patternIndex++) {
      Pattern pattern = this.employeeDescription.Contract.WeekDayBadPatterns[patternIndex];
      int newStart = pattern.StartDay;
      while ((newStart - 7) * -1 <= this.employeeDescription.SchedulingHistory.PreviousDayType.length && 
        newStart - 7 + pattern.ShiftIndices.length > 0) {
        newStart -= 7;
        penalty += TestPattern(employee, pattern, newStart);
      } 
      for (int i = 0; i <= this.numDaysInPeriod - pattern.ShiftIndices.length; i += 7)
        penalty += TestPattern(employee, pattern, i + pattern.StartDay); 
    } 
    for (patternIndex = 0; patternIndex < this.employeeDescription.Contract.DateBadPatterns.length; patternIndex++) {
      Pattern pattern = this.employeeDescription.Contract.DateBadPatterns[patternIndex];
      if (pattern.StartDay + pattern.ShiftIndices.length <= this.numDaysInPeriod && 
        pattern.StartDay + pattern.ShiftIndices.length > 0 && (
        pattern.StartDay >= 0 || this.employeeDescription.SchedulingHistory.PreviousDayType.length + pattern.StartDay >= 0))
        penalty += TestPattern(employee, pattern, pattern.StartDay); 
    } 
    return penalty;
  }
  
  private int TestPattern(Employee employee, Pattern pattern, int startDay) {
    SchedulingPeriod schedulingPeriod = employee.Roster.SchedulingPeriod;
    boolean patternMatched = true;
    int penalty = 0;
    int k;
    for (k = 0; k < pattern.ShiftIndices.length; k++) {
      int index = pattern.ShiftIndices[k];
      int day = startDay + k;
      if (day < 0) {
        day = this.employeeDescription.SchedulingHistory.PreviousDayType.length + day;
        if (pattern.DayTypes[k] != Pattern.DayType.Any)
          if (pattern.DayTypes[k] == Pattern.DayType.WorkingDay) {
            if (this.employeeDescription.SchedulingHistory.PreviousDayType[day] != 1) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.Off) {
            if (this.employeeDescription.SchedulingHistory.PreviousDayType[day] == 1) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.Shift) {
            if (!this.employeeDescription.SchedulingHistory.PreviousShiftAssignments[day * this.shiftTypeCount + index]) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.NotShift) {
            if (this.employeeDescription.SchedulingHistory.PreviousShiftAssignments[day * this.shiftTypeCount + index]) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.ShiftGroup) {
            ShiftGroup grp = schedulingPeriod.GetShiftGroup(index);
            boolean grpMatched = false;
            for (int x = 0; x < grp.Group.length; x++) {
              if (this.employeeDescription.SchedulingHistory.PreviousShiftAssignments[day * this.shiftTypeCount + (grp.Group[x]).Index]) {
                grpMatched = true;
                break;
              } 
            } 
            if (!grpMatched) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.OtherWork) {
            if (this.employeeDescription.SchedulingHistory.PreviousDayType[day] != 1) {
              patternMatched = false;
              break;
            } 
          }  
      } else {
        if (day >= schedulingPeriod.NumDaysInPeriod) {
          patternMatched = false;
          break;
        } 
        if (pattern.DayTypes[k] != Pattern.DayType.Any)
          if (pattern.DayTypes[k] == Pattern.DayType.WorkingDay) {
            if (employee.DayType[day] != 1) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.Off) {
            if (employee.DayType[day] == 1 || this.employeeDescription.DayOffRequestIsWork[day]) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.Shift) {
            if (employee.ShiftsOnDay[day][index] == null) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.NotShift) {
            if (employee.ShiftsOnDay[day][index] != null) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.ShiftGroup) {
            if (employee.ShiftGroupPerDayCount[day][index] <= 0) {
              patternMatched = false;
              break;
            } 
          } else if (pattern.DayTypes[k] == Pattern.DayType.OtherWork) {
            if (!this.employeeDescription.DayOffRequestIsWork[day]) {
              patternMatched = false;
              break;
            } 
          }  
      } 
    } 
    if (patternMatched) {
      penalty += pattern.Weight;
      for (k = 0; k < pattern.ShiftIndices.length; k++) {
        int day = startDay + k;
        if (day >= schedulingPeriod.NumDaysInPeriod)
          break; 
        if (day >= 0)
          employee.ConstraintViolationPenalties[day] = employee.ConstraintViolationPenalties[day] + pattern.Weight; 
      } 
      if (SoftConstraints.UpdateViolationDescriptions) {
        boolean includesPreviousSchedule = false;
        if (startDay < 0)
          includesPreviousSchedule = true; 
        for (int i = 0; i < pattern.ShiftIndices.length; i++) {
          int day = startDay + i;
          if (day >= schedulingPeriod.NumDaysInPeriod)
            break; 
          if (day >= 0)
            if (includesPreviousSchedule) {
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Undesirable sequence: " + pattern.toString() + " (includes previous schedule)" + System.getProperty("line.separator");
            } else {
              employee.ViolationDescriptions[day] = String.valueOf(employee.ViolationDescriptions[day]) + "Undesirable sequence: " + pattern.toString() + System.getProperty("line.separator");
            }  
        } 
      } 
    } 
    return penalty;
  }
}
