package org.example.util.ASAP.NRP.Core.Tools;

import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraint;
import org.example.util.ASAP.NRP.Core.Constraints.SoftConstraints;
import org.example.util.ASAP.NRP.Core.CoverProvided;
import org.example.util.ASAP.NRP.Core.CoverRequirement;
import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.DayPeriod;
import org.example.util.ASAP.NRP.Core.Employee;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.SchedulingPeriod;
import org.example.util.ASAP.NRP.Core.Shift;
import org.example.util.ASAP.NRP.Core.ShiftGroup;
import org.example.util.ASAP.NRP.Core.ShiftType;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

public class RosterPrinter {
  public static boolean PrintRosterAsHTML(Roster roster, String filePath) {
    return PrintRosterAsHTML(roster, filePath, false);
  }
  
  public static boolean PrintRosterAsHTML(Roster roster, String filePath, boolean embedCssFile) {
    return PrintRosterAsHTML(roster, filePath, embedCssFile, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null);
  }
  
  public static boolean PrintRosterAsHTML(Roster roster, String filePath, boolean embedCssFile, String computationTime, String algorithm, String foundBy, String date, String system, String CPU) {
    try {
      PrintWriter sw = new PrintWriter(new FileWriter(filePath));
      return PrintRosterAsHTML(roster, sw, embedCssFile, 
          computationTime, 
          algorithm, 
          foundBy, 
          date, 
          system, 
          CPU);
    } catch (Exception ex) {
      return false;
    } 
  }
  
  public static boolean PrintRosterAsHTML(Roster roster, Writer sw, boolean embedCssFile, String computationTime, String algorithm, String foundBy, String date, String system, String CPU) {
    boolean javascript = true;
    boolean multiObjInfo = false;
    embedCssFile = false;
    if (roster == null)
      return false; 
    String shiftClass = "shiftType";
    int shiftSize = 20;
    int shiftFontSize = 14;
    int cellPadding = 4;
    if (roster.SchedulingPeriod.NumDaysInPeriod > 31) {
      shiftClass = "shiftTypeSmall";
      shiftSize = 16;
      shiftFontSize = 10;
      cellPadding = 2;
    } 
    try {
      SchedulingPeriod schedulingPeriod = roster.SchedulingPeriod;
      int numDays = schedulingPeriod.NumDaysInPeriod;
      boolean[] MonsAndSats = new boolean[numDays];
      boolean[] FrisAndSuns = new boolean[numDays];
      String[][] cellTitles = new String[roster.Employees.length][schedulingPeriod.NumDaysInPeriod];
      for (int i = 0; i < cellTitles.length; i++) {
        for (int i2 = 0; i2 < (cellTitles[i]).length; i2++)
          cellTitles[i][i2] = ""; 
      } 
      String cssString = "<link rel=\"stylesheet\" type=\"text/css\" href=\"scheduleDefault.css\"/>" + System.getProperty("line.separator");
      cssString = String.valueOf(cssString) + "<link rel=\"alternate stylesheet\" type=\"text/css\" title=\"contrast\" href=\"scheduleContrast.css\"/>" + System.getProperty("line.separator");
      cssString = String.valueOf(cssString) + "<link rel=\"alternate stylesheet\" type=\"text/css\" title=\"empty\" href=\"empty.css\"/>" + System.getProperty("line.separator");
      cssString = String.valueOf(cssString) + "<style type=\"text/css\"></style>" + System.getProperty("line.separator");
      sw.write("<!-- saved from url=(0013)about:internet -->\n");
      sw.write("<html><head>\n");
      String htmlTitle = "Roster";
      if (schedulingPeriod.FilePath != null)
        htmlTitle = String.valueOf(htmlTitle) + " :: " + schedulingPeriod.FilePath; 
      sw.write("<title>" + htmlTitle + "</title>\n");
      sw.write(String.valueOf(cssString) + "\n");
      boolean requestsExist = false;
      boolean constraintViolationsExist = false;
      if (javascript) {
        int row = 0;
        String str1 = "";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        byte b;
        int i2;
        Employee[] arrayOfEmployee;
        for (i2 = (arrayOfEmployee = roster.Employees).length, b = 0; b < i2; ) {
          Employee employee = arrayOfEmployee[b];
          for (int i3 = 0; i3 < numDays; i3++) {
            String className = null;
            if (employee.EmployeeDescription.DayOffRequests[i3] != 0) {
              className = "dayOffHigh";
              cellTitles[row][i3] = String.valueOf(cellTitles[row][i3]) + "Requests day off (weight=" + employee.EmployeeDescription.DayOffRequests[i3] + ", holiday=" + employee.EmployeeDescription.DayOffRequestIsHoliday[i3] + ", work=" + employee.EmployeeDescription.DayOffRequestIsWork[i3] + "). " + System.getProperty("line.separator");
            } 
            if (employee.EmployeeDescription.DayOnRequests[i3] != 0) {
              className = "dayOnHigh";
              cellTitles[row][i3] = String.valueOf(cellTitles[row][i3]) + "Requests day on (weight=" + employee.EmployeeDescription.DayOnRequests[i3] + "). " + System.getProperty("line.separator");
            } 
            int c = schedulingPeriod.ShiftTypesCount;
            for (int i4 = 0; i4 < c; i4++) {
              if (employee.EmployeeDescription.ShiftOnRequests[i3 * c + i4] != 0) {
                className = "shiftOnHigh";
                cellTitles[row][i3] = String.valueOf(cellTitles[row][i3]) + "Requests '" + (schedulingPeriod.GetShiftType(i4)).Label + "' shift (weight=" + employee.EmployeeDescription.ShiftOnRequests[i3 * c + i4] + "). " + System.getProperty("line.separator");
              } 
              if (employee.EmployeeDescription.ShiftOffRequests[i3 * c + i4] != 0) {
                className = "shiftOffHigh";
                cellTitles[row][i3] = String.valueOf(cellTitles[row][i3]) + "Requests no '" + (schedulingPeriod.GetShiftType(i4)).Label + "' shifts (weight=" + employee.EmployeeDescription.ShiftOffRequests[i3 * c + i4] + "). " + System.getProperty("line.separator");
              } 
            } 
            String shiftGroupMsg = GetShiftGroupOnMessage(employee, i3);
            if (shiftGroupMsg != null) {
              className = "shiftOnHigh";
              cellTitles[row][i3] = String.valueOf(cellTitles[row][i3]) + shiftGroupMsg;
            } 
            if (className != null) {
              str1 = String.valueOf(str1) + "        document.getElementById('cell_" + i3 + "_" + row + "').className = '" + className + "';" + System.getProperty("line.separator");
              str2 = String.valueOf(str2) + "        document.getElementById('cell_" + i3 + "_" + row + "').className = 'shift';" + System.getProperty("line.separator");
              requestsExist = true;
            } 
            if (employee.ConstraintViolationPenalties[i3] > 0) {
              str3 = String.valueOf(str3) + "        document.getElementById('cell_" + i3 + "_" + row + "').style.borderColor = '#FF0000';" + System.getProperty("line.separator");
              str3 = String.valueOf(str3) + "        document.getElementById('cell_" + i3 + "_" + row + "').style.borderWidth = '2';" + System.getProperty("line.separator");
              str3 = String.valueOf(str3) + "        document.getElementById('cell_" + i3 + "_" + row + "').style.borderStyle = 'solid';" + System.getProperty("line.separator");
              str4 = String.valueOf(str4) + "        document.getElementById('cell_" + i3 + "_" + row + "').style.borderColor = '#E0E0E0';" + System.getProperty("line.separator");
              str4 = String.valueOf(str4) + "        document.getElementById('cell_" + i3 + "_" + row + "').style.borderWidth = '1';" + System.getProperty("line.separator");
              str4 = String.valueOf(str4) + "        document.getElementById('cell_" + i3 + "_" + row + "').style.borderStyle = 'solid';" + System.getProperty("line.separator");
              constraintViolationsExist = true;
            } 
          } 
          row++;
          b++;
        } 
        sw.write("<script src=\"schedule.js\" type=\"text/javascript\"></script>\n");
        sw.write("<script language=\"JavaScript\"><!--\n");
        sw.write("var ie = document.all;\n");
        sw.write("function toggleRequests( CB )\n");
        sw.write("{");
        sw.write("    if (CB.checked)\n");
        sw.write("    {\n");
        sw.write(String.valueOf(str1) + "\n");
        sw.write("    }\n");
        sw.write("    else\n");
        sw.write("    {\n");
        sw.write(String.valueOf(str2) + "\n");
        sw.write("    }\n");
        sw.write("}\n");
        sw.write("function toggleConstraintViolations( CB )\n");
        sw.write("{\n");
        sw.write("    if (CB.checked)\n");
        sw.write("    {\n");
        sw.write("        if ( ! ie )\n");
        sw.write("            document.getElementById( 'scheduleTable' ).style.borderCollapse =  'separate';\n");
        sw.write(String.valueOf(str3) + "\n");
        sw.write("    }\n");
        sw.write("    else\n");
        sw.write("    {\n");
        sw.write("        if ( ! ie )\n");
        sw.write("            document.getElementById( 'scheduleTable' ).style.borderCollapse =  'collapse';\n");
        sw.write(String.valueOf(str4) + "\n");
        sw.write("    }\n");
        sw.write("}\n");
        sw.write("var fontSize  = " + shiftFontSize + ";" + "\n");
        sw.write("var shiftSize = " + shiftSize + ";" + "\n");
        sw.write("function ChangeRosterFontSize( increase )\n");
        sw.write("{\n");
        sw.write("    var scheduleTable = document.getElementById('scheduleTable');\n");
        sw.write("    if ( increase )\n");
        sw.write("    {\n");
        sw.write("        if ( fontSize+2 <= 32 && shiftSize+2 < 40 )\n");
        sw.write("        {\n");
        sw.write("            fontSize  += 2;\n");
        sw.write("            shiftSize += 2;\n");
        sw.write("            if ( fontSize > 14 )\n");
        sw.write("                scheduleTable.cellPadding = 4;\n");
        sw.write("            else if ( fontSize > 12 )\n");
        sw.write("                scheduleTable.cellPadding = 3;\n");
        sw.write("            else if ( fontSize > 10 )\n");
        sw.write("                 scheduleTable.cellPadding = 2;\n");
        sw.write("            else if ( fontSize > 8 )\n");
        sw.write("                 scheduleTable.cellPadding = 1;\n");
        sw.write("        }");
        sw.write("    }");
        sw.write("    else");
        sw.write("    {");
        sw.write("        if ( fontSize-2 > 0 && shiftSize-2 > 0 )");
        sw.write("        {");
        sw.write("            fontSize  -= 2;");
        sw.write("            shiftSize -= 2;");
        sw.write("            if ( fontSize < 10 )");
        sw.write("                scheduleTable.cellPadding = 0;");
        sw.write("            else if ( fontSize < 12 )");
        sw.write("                scheduleTable.cellPadding = 1;");
        sw.write("            else if ( fontSize < 14 )");
        sw.write("                scheduleTable.cellPadding = 2;");
        sw.write("            else if ( fontSize < 16 )");
        sw.write("                scheduleTable.cellPadding = 3;");
        sw.write("        }\n");
        sw.write("    }\n");
        if (requestsExist) {
          sw.write("    document.getElementById('showRequestsCB').style.width=fontSize;\n");
          sw.write("    document.getElementById('showRequestsCB').style.height=fontSize;\n");
        } 
        if (constraintViolationsExist) {
          sw.write("    document.getElementById('showConstraintViolationsCB').style.width=fontSize;\n");
          sw.write("    document.getElementById('showConstraintViolationsCB').style.height=fontSize;\n");
        } 
        sw.write("    var ps = scheduleTable.getElementsByTagName(\"P\");");
        sw.write("    for (var i = 0; i < ps.length; i++) ");
        sw.write("    {");
        sw.write("       if (ps[i].className == '" + shiftClass + "')");
        sw.write("       {");
        sw.write("            ps[i].style.width=shiftSize;");
        sw.write("            ps[i].style.height=shiftSize;");
        sw.write("            ");
        sw.write("       }");
        sw.write("       ps[i].style.fontSize=fontSize;");
        sw.write("       ");
        sw.write("    }");
        sw.write("    var spans = scheduleTable.getElementsByTagName(\"SPAN\");");
        sw.write("    for (var i = 0; i < spans.length; i++) ");
        sw.write("    {");
        sw.write("       if (spans[i].className == '" + shiftClass + "')");
        sw.write("       {");
        sw.write("            spans[i].style.width=shiftSize;");
        sw.write("            spans[i].style.height=shiftSize;");
        sw.write("            ");
        sw.write("       }");
        sw.write("       spans[i].style.fontSize=fontSize;");
        sw.write("       ");
        sw.write("    }");
        sw.write("    var cells = scheduleTable.getElementsByTagName(\"td\");");
        sw.write("    for (var i = 0; i < cells.length; i++) ");
        sw.write("    {");
        sw.write("        cells[i].style.fontSize=fontSize;      ");
        sw.write("    }\n");
        sw.write("}\n");
        sw.write("//-->\n");
        sw.write("</script>\n");
      } 
      sw.write("</head><body>");
      if (javascript) {
        String version = "J_0.1";
        sw.write("<div id=\"OptionsPanel\" ");
        sw.write("style=\"position: absolute;");
        sw.write("       width: 320px; ");
        sw.write("       height: 80px; ");
        sw.write("       left: 0px; ");
        sw.write("       top: -100px; ");
        sw.write("       z-index:1; ");
        sw.write("       color: #000000; ");
        sw.write("       background-color: #FFFFFF; ");
        sw.write("       border: 4px ridge #33CCFF; ");
        sw.write("       border-top-width: 0px; ");
        sw.write("       border-left-width: 0px\"> ");
        sw.write("");
        sw.write("  <div style=\"margin-left: 5px; margin-top: 5px;\">");
        sw.write("    <p style=\"font-weight: bold\">About</p>");
        sw.write("    <p>Page created using <a href=\"http://www.cs.nott.ac.uk/~tec/NRP/\">Nurse Rostering Problem API</a> (" + version + ")</p>  ");
        sw.write("  </div>");
        sw.write("</div>");
      } 
      sw.write("<div class=\"shiftInfoPanel\" id=\"shiftInfoPanel\"> </div>");
      sw.write("<p style=\"text-align:center\"><table id=\"scheduleTable\" class=\"scheduleTable\" cellspacing=\"0\" cellpadding=\"" + 
          cellPadding + "\" border=\"0\">");
      sw.write("<tr><td align=\"center\"> ");
      if (javascript) {
        sw.write("<script language=\"JavaScript\"><!--\n");
        sw.write("document.write('<a title=\"Increase table size\" class=\"changeFontSize\" href=\"javascript:;\" OnClick=\"ChangeRosterFontSize( true ); return false;\">+</a> ');\n");
        sw.write("document.write('<a title=\"Decrease table size\" class=\"changeFontSize\" href=\"javascript:;\" OnClick=\"ChangeRosterFontSize( false ); return false;\">-</a>');\n");
        sw.write("//-->\n");
        sw.write("</script>\n");
      } 
      sw.write("</td>");
      int dayCount = 7;
      int week = 1;
      while (dayCount <= numDays) {
        sw.write("<td colspan=\"7\"  class=\"header\">" + week + "</td>");
        dayCount += 7;
        week++;
      } 
      int over = numDays - dayCount - 7;
      if (over != 0)
        sw.write("<td colspan=\"" + over + "\"  class=\"header\">" + week + "</td>" + "\n"); 
      sw.write("<td> </td></tr>");
      int day = 0;
      DateTime startDate = schedulingPeriod.StartDate;
      DateTime endDate = schedulingPeriod.EndDate;
      DateTime tempDate = startDate;
      String dayLine = "<tr><td></td>";
      String dateLine = "<tr><td>" + startDate.ToString("yyyy MMMM") + "</td>";
      while (tempDate.isLessThan(endDate)) {
        if (tempDate.getDayOfWeek() == 7 || 
          tempDate.getDayOfWeek() == 1) {
          dayLine = String.valueOf(dayLine) + "<td class=\"weekendHeader\" title=\"" + tempDate.getDayOfWeekString() + "\">" + tempDate.getDayOfWeekString().charAt(0) + "</td>";
        } else {
          dayLine = String.valueOf(dayLine) + "<td class=\"header\" title=\"" + tempDate.getDayOfWeekString() + "\">" + tempDate.getDayOfWeekString().charAt(0) + "</td>";
        } 
        String dayNum = Integer.toString(tempDate.getDay());
        if (tempDate.getDay() < 10)
          dayNum = "0" + tempDate.getDay(); 
        if (schedulingPeriod.BankHolidayArray[day]) {
          dateLine = String.valueOf(dateLine) + "<td class=\"bankHolidayHeader\" >" + dayNum + "</td>";
        } else {
          dateLine = String.valueOf(dateLine) + "<td class=\"header\" >" + dayNum + "</td>";
        } 
        if (tempDate.getDayOfWeek() == 7 || 
          tempDate.getDayOfWeek() == 2) {
          MonsAndSats[day] = true;
        } else if (tempDate.getDayOfWeek() == 6 || 
          tempDate.getDayOfWeek() == 1) {
          FrisAndSuns[day] = true;
        } 
        tempDate = tempDate.AddDays(1);
        day++;
      } 
      dateLine = String.valueOf(dateLine) + "<td style=\"border-left-width:  1px;\"></td></tr>";
      dayLine = String.valueOf(dayLine) + "<td style=\"border-left-width:  1px;\"></td></tr>";
      sw.write(String.valueOf(dateLine) + "\n");
      sw.write(String.valueOf(dayLine) + "\n");
      int r = 0;
      Hashtable<String, Integer> TotalPenaltyBreakdownHash = new Hashtable<String, Integer>();
      byte b1;
      int j;
      Employee[] arrayOfEmployee1;
      for (j = (arrayOfEmployee1 = roster.Employees).length, b1 = 0; b1 < j; ) {
        Employee employee = arrayOfEmployee1[b1];
        String employeeName = employee.EmployeeDescription.getName();
        String employeeID = employee.EmployeeDescription.ID;
        int penalty = SoftConstraints.CalculatePenalty(employee, true);
        String[] days = new String[numDays];
        int d;
        for (d = 0; d < days.length; d++)
          days[d] = ""; 
        for (d = 0; d < schedulingPeriod.NumDaysInPeriod; d++) {
          for (int index = 0; index < schedulingPeriod.ShiftTypesCount; index++) {
            Shift shift = employee.ShiftsOnDay[d][index];
            if (shift != null) {
              int daysAfterStartDate = shift.RosterDay;
              String shiftDescription = String.valueOf(shift.ShiftType.Description) + "  " + 
                shift.StartTime.ToString("HH:mm") + " - " + 
                shift.EndTime.ToString("HH:mm") + " " + 
                
                "(RosterDay=" + shift.RosterDay + ")";
              String title = shiftDescription;
              if (!employee.ViolationDescriptions[daysAfterStartDate].equalsIgnoreCase(""))
                title = String.valueOf(title) + System.getProperty("line.separator") + "Violations:" + System.getProperty("line.separator") + employee.ViolationDescriptions[daysAfterStartDate] + "(Penalty " + employee.ConstraintViolationPenalties[daysAfterStartDate] + ")"; 
              days[daysAfterStartDate] = String.valueOf(days[daysAfterStartDate]) + "<p class=\"" + shiftClass + "\" style=\"background-color:" + shift.ShiftType.HtmlColor + "\" ";
              String safeTitle = title;
              safeTitle = safeTitle.replaceAll("'", "\\\\'");
              safeTitle = safeTitle.replaceAll(System.getProperty("line.separator"), "</br>");
              days[daysAfterStartDate] = String.valueOf(days[daysAfterStartDate]) + "onMouseOver=\"this.style.cursor='pointer'\" onMouseOut=\"this.style.cursor='default'\" onClick=\"showShiftInfoPanel('" + safeTitle + "');\" ";
              days[daysAfterStartDate] = String.valueOf(days[daysAfterStartDate]) + ">" + shift.ShiftType.Label + "</p>";
            } 
          } 
        } 
        sw.write("<tr><td title=\"Skills: " + employee.EmployeeDescription.GetSkillsDescription() + "\" class=\"employee\"><a href=\"#" + employeeID + "\">" + employeeName + "</a></td>");
        for (int i2 = 0; i2 < days.length; i2++) {
          String styleClass = "shift";
          if (MonsAndSats[i2]) {
            styleClass = "monAndSat";
          } else if (FrisAndSuns[i2]) {
            styleClass = "friAndSun";
          } 
          if (!employee.EmployeeDescription.AvailableOnDay(i2)) {
            styleClass = "employeeUnavailable";
            cellTitles[r][i2] = String.valueOf(cellTitles[r][i2]) + "Employee unavailable. " + System.getProperty("line.separator");
          } 
          if (!employee.ViolationDescriptions[i2].equalsIgnoreCase(""))
            cellTitles[r][i2] = String.valueOf(cellTitles[r][i2]) + "Violations:" + System.getProperty("line.separator") + employee.ViolationDescriptions[i2] + "(Penalty " + employee.ConstraintViolationPenalties[i2] + ")"; 
          sw.write("<td ");
          if (!cellTitles[r][i2].equals("")) {
            String safeTitle = cellTitles[r][i2];
            safeTitle = safeTitle.replaceAll("'", "\\\\'");
            safeTitle = safeTitle.replaceAll(System.getProperty("line.separator"), "</br>");
            sw.write("onMouseOver=\"this.style.cursor='pointer'\" onMouseOut=\"this.style.cursor='default'\" onClick=\"showShiftInfoPanel('" + safeTitle + "');\" ");
          } 
          if (days[i2] == null)
            days[i2] = "&nbsp; "; 
          sw.write("name=\"cell_" + i2 + "_" + r + "\" id=\"cell_" + i2 + "_" + r + "\" class=\"" + styleClass + "\">" + days[i2] + "</td>");
          sw.flush();
        } 
        String penaltiesInfo = String.valueOf(employeeName) + ":" + System.getProperty("line.separator");
        for (int i3 = 0; i3 < employee.EmployeeDescription.AllSoftConstraints.length; i3++) {
          SoftConstraint sc = employee.EmployeeDescription.AllSoftConstraints[i3];
          int pen = sc.Calculate(employee);
          penaltiesInfo = String.valueOf(penaltiesInfo) + sc.getTitle() + " = " + pen + System.getProperty("line.separator");
          if (TotalPenaltyBreakdownHash.get(sc.getTitle()) == null)
            TotalPenaltyBreakdownHash.put(sc.getTitle(), new Integer(0)); 
          int totPen = ((Integer)TotalPenaltyBreakdownHash.get(sc.getTitle())).intValue();
          totPen += pen;
          TotalPenaltyBreakdownHash.put(sc.getTitle(), new Integer(totPen));
        } 
        sw.write("<td class=\"penalty\" title=\"" + penaltiesInfo + "\"><a class=\"penaltyLink\" href=\"#" + employeeID + "\">" + penalty + "</a></td>");
        sw.write("</tr>");
        r++;
        b1++;
      } 
      String totalPenaltyInfo = "";
      int cols = numDays + 1;
      int cols2 = cols;
      int cols3 = numDays + 2;
      int cols4 = cols;
      sw.write("<tr>");
      if (requestsExist) {
        sw.write("<td style=\"\"><p style=\"font-size: 11px;\"><form name=\"optionsForm\">Requests <input style=\"\" type=\"checkbox\" id=\"showRequestsCB\" name=\"showRequestsCB\" onClick=\"toggleRequests( this )\"></form></p></td>");
        cols2--;
      } 
      sw.write("<td style=\"text-align:right; \" colspan=\"" + cols2 + "\">Employees' Penalty</td>");
      sw.write("<td align=\"right\" style=\"\" title=\"" + totalPenaltyInfo + "\"><a class=\"penaltyLink\" href=\"#TotalPenaltyBreakdown\">" + roster.EmployeesPenalty + "</a></td>");
      sw.write("<tr>");
      if (constraintViolationsExist) {
        sw.write("<td style=\"padding-bottom:0px; padding-top:0px;\"><p style=\"font-size: 11px;\"><form name=\"options2Form\">Violations <input style=\"\" type=\"checkbox\" id=\"showConstraintViolationsCB\" name=\"showConstraintViolationsCB\" onClick=\"toggleConstraintViolations( this )\"></form></p></td>");
        cols4--;
      } 
      sw.write("<td style=\"text-align:right; padding-bottom:0px; padding-top:0px;\" colspan=\"" + cols4 + "\"><!--Unassigned Shifts--></td>" + 
          "<td style=\"text-align:right; padding-bottom:0px; padding-top:0px;\"></td>" + 
          
          "</tr>");
      sw.write("<tr style=\"height:10px\"><td colspan=\"" + cols3 + "\"></td></tr>");
      ShiftType[] sortedShiftTypes = new ShiftType[schedulingPeriod.ShiftTypesCount];
      for (int m = 0; m < sortedShiftTypes.length; m++)
        sortedShiftTypes[m] = schedulingPeriod.GetShiftType(m); 
      Arrays.sort((Object[])sortedShiftTypes);
      DayPeriod[] sortedPeriods = new DayPeriod[schedulingPeriod.getDayPeriodsCount()];
      for (int n = 0; n < sortedPeriods.length; n++)
        sortedPeriods[n] = schedulingPeriod.GetDayPeriod(n); 
      Arrays.sort((Object[])sortedPeriods);
      int k;
      for (k = 0; k < schedulingPeriod.CoverRequirements.Requirements.length; k++) {
        CoverRequirement req = schedulingPeriod.CoverRequirements.Requirements[k];
        CoverProvided prov = roster.CoverProvided[k];
        if (req.CoverPerShiftGroupUsed) {
          String skillID = "-";
          String skillLabel = "-";
          String plural = "";
          if (req.SkillType == CoverRequirement.SkillTypes.AnySkill) {
            skillID = "__All__";
            skillLabel = "All";
          } else if (req.SkillType == CoverRequirement.SkillTypes.SingleSkill) {
            skillID = req.SkillID;
            skillLabel = req.SkillID;
          } else if (req.SkillType == CoverRequirement.SkillTypes.SkillGroup) {
            skillID = "__grp__" + req.SkillID;
            skillLabel = (schedulingPeriod.GetSkillGroup(req.SkillID)).Label;
            plural = "s";
          } 
          boolean[] usedGroups = new boolean[schedulingPeriod.ShiftGroupsCount];
          int usedGroupsCount = 0;
          for (int i3 = 0; i3 < schedulingPeriod.ShiftGroupsCount; i3++) {
            boolean used = false;
            for (int i4 = 0; i4 < numDays; i4++) {
              if (req.MinCoverPerShiftGroup[i3][i4] >= 0 || 
                req.MaxCoverPerShiftGroup[i3][i4] >= 0 || 
                req.PrefCoverPerShiftGroup[i3][i4] >= 0) {
                used = true;
                break;
              } 
            } 
            usedGroupsCount++;
            usedGroups[i3] = used;
          } 
          sw.write("<tr style=\"\"><td colspan=\"" + cols3 + "\">Shift Group Cover (Skill" + plural + " : <span style=\"font-weight:bold\">" + skillLabel + "</span>) ");
          sw.write("<script language=\"JavaScript\"><!--");
          sw.write("document.write('<a id=\"HideCover_shiftGroup_skill_" + skillID + "\" title=\"Hide\" class=\"showHide\" href=\"javascript:;\" OnClick=\"hideCover(\\'shiftGroup\\',\\'" + skillID + "\\'," + usedGroupsCount + "); return false;\">Hide</a> ');");
          sw.write("document.write('<a id=\"ShowCover_shiftGroup_skill_" + skillID + "\"title=\"Show\" style=\"display:none\" class=\"showHide\" href=\"javascript:;\" OnClick=\"showCover(\\'shiftGroup\\',\\'" + skillID + "\\'," + usedGroupsCount + "); return false;\">Show</a>');");
          sw.write("//-->");
          sw.write("</script>");
          sw.write("</td></tr>");
          for (int i2 = 0; i2 < schedulingPeriod.ShiftGroupsCount; i2++) {
            if (usedGroups[i2]) {
              ShiftGroup grp = schedulingPeriod.GetShiftGroup(i2);
              String str = "";
              for (int y = 0; y < grp.Group.length; y++) {
                ShiftType shiftType = grp.Group[y];
                String shiftDescription = String.valueOf(shiftType.Description) + "  " + shiftType.getStartTime().ToString("t") + " - " + shiftType.getEndTime().ToString("t") + " ";
                str = String.valueOf(str) + "<span class=\"" + shiftClass + "\" style=\"background-color:" + shiftType.HtmlColor + "\" title=\"" + shiftDescription + "\">" + shiftType.Label + "</span> ";
              } 
              boolean showMin = false;
              boolean showMax = false;
              boolean showPref = false;
              int i4;
              for (i4 = 0; i4 < numDays; i4++) {
                if (req.MinCoverPerShiftGroup[grp.Index][i4] >= 0)
                  showMin = true; 
                if (req.MaxCoverPerShiftGroup[grp.Index][i4] >= 0)
                  showMax = true; 
                if (req.PrefCoverPerShiftGroup[grp.Index][i4] >= 0)
                  showPref = true; 
              } 
              if (showMin) {
                sw.write("<tr id=\"MinCover_shiftGroup_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Min</td>");
                for (i4 = 0; i4 < numDays; i4++) {
                  int val = req.MinCoverPerShiftGroup[grp.Index][i4];
                  String v = Integer.toString(val);
                  if (val < 0)
                    v = "-"; 
                  String cs = "coverSatisfied";
                  if (val >= 0 && val > prov.ProvidedCoverPerShiftGroup[grp.Index][i4])
                    cs = "coverNotSatisfied"; 
                  sw.write("<td class=\"" + cs + "\">" + v + "</td>");
                } 
                sw.write("<td>&nbsp;</td></tr>");
              } 
              if (showMax) {
                sw.write("<tr id=\"MaxCover_shiftGroup_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Max</td>");
                for (i4 = 0; i4 < numDays; i4++) {
                  int val = req.MaxCoverPerShiftGroup[grp.Index][i4];
                  String v = Integer.toString(val);
                  if (val < 0)
                    v = "-"; 
                  String cs = "coverSatisfied";
                  if (val >= 0 && val < prov.ProvidedCoverPerShiftGroup[grp.Index][i4])
                    cs = "coverNotSatisfied"; 
                  sw.write("<td class=\"" + cs + "\">" + v + "</td>");
                } 
                sw.write("<td>&nbsp;</td></tr>");
              } 
              if (showPref) {
                sw.write("<tr id=\"PrefCover_shiftGroup_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Preferred</td>");
                for (i4 = 0; i4 < numDays; i4++) {
                  int val = req.PrefCoverPerShiftGroup[grp.Index][i4];
                  String v = Integer.toString(val);
                  if (val < 0)
                    v = "-"; 
                  String cs = "coverSatisfied";
                  if (val >= 0 && val != prov.ProvidedCoverPerShiftGroup[grp.Index][i4])
                    cs = "coverNotSatisfied"; 
                  sw.write("<td class=\"" + cs + "\">" + v + "</td>");
                } 
                sw.write("<td>&nbsp;</td></tr>");
              } 
              int totPen = 0;
              sw.write("<tr id=\"ProvCover_shiftGroup_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Provided</td>");
              for (int i5 = 0; i5 < numDays; i5++) {
                sw.write("<td class=\"coverProvided\">" + prov.ProvidedCoverPerShiftGroup[grp.Index][i5] + "</td>");
                totPen += prov.CoverPerShiftGroupPenalty[grp.Index][i5];
              } 
              sw.write("<td align=\"right\">" + totPen + "</td></tr>");
            } 
          } 
        } 
      } 
      for (k = 0; k < schedulingPeriod.CoverRequirements.Requirements.length; k++) {
        CoverRequirement req = schedulingPeriod.CoverRequirements.Requirements[k];
        CoverProvided prov = roster.CoverProvided[k];
        if (req.CoverPerShiftUsed) {
          String skillID = "-";
          String skillLabel = "-";
          String plural = "";
          if (req.SkillType == CoverRequirement.SkillTypes.AnySkill) {
            skillID = "__All__";
            skillLabel = "All";
          } else if (req.SkillType == CoverRequirement.SkillTypes.SingleSkill) {
            skillID = req.SkillID;
            skillLabel = req.SkillID;
          } else if (req.SkillType == CoverRequirement.SkillTypes.SkillGroup) {
            skillID = "__grp__" + req.SkillID;
            skillLabel = "(" + (schedulingPeriod.GetSkillGroup(req.SkillID)).Label + ")";
            plural = "s";
          } 
          sw.write("<tr style=\"\"><td colspan=\"" + cols3 + "\">Shift Cover (Skill" + plural + " : <span style=\"font-weight:bold\">" + skillLabel + "</span>) ");
          sw.write("<script language=\"JavaScript\"><!--");
          sw.write("document.write('<a id=\"HideCover_shift_skill_" + skillID + "\" title=\"Hide\" class=\"showHide\" href=\"javascript:;\" OnClick=\"hideCover(\\'shift\\',\\'" + skillID + "\\'," + sortedShiftTypes.length + "); return false;\">Hide</a> ');");
          sw.write("document.write('<a id=\"ShowCover_shift_skill_" + skillID + "\"title=\"Show\" style=\"display:none\" class=\"showHide\" href=\"javascript:;\" OnClick=\"showCover(\\'shift\\',\\'" + skillID + "\\'," + sortedShiftTypes.length + "); return false;\">Show</a>');");
          sw.write("//-->");
          sw.write("</script>");
          sw.write("</td></tr>");
          for (int i2 = 0; i2 < sortedShiftTypes.length; i2++) {
            ShiftType shiftType = sortedShiftTypes[i2];
            String shiftDescription = String.valueOf(shiftType.Description) + "  " + shiftType.getStartTime().ToString("HH:mm") + " - " + shiftType.getEndTime().ToString("HH:mm") + " ";
            String str = "<span class=\"" + shiftClass + "\" style=\"background-color:" + shiftType.HtmlColor + "\" title=\"" + shiftDescription + "\">" + shiftType.Label + "</span>";
            boolean showMin = false;
            boolean showMax = false;
            boolean showPref = false;
            int i3;
            for (i3 = 0; i3 < numDays; i3++) {
              if (req.MinCoverPerShift[shiftType.Index][i3] >= 0)
                showMin = true; 
              if (req.MaxCoverPerShift[shiftType.Index][i3] >= 0)
                showMax = true; 
              if (req.PrefCoverPerShift[shiftType.Index][i3] >= 0)
                showPref = true; 
            } 
            if (showMin) {
              sw.write("<tr id=\"MinCover_shift_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Min</td>");
              for (i3 = 0; i3 < numDays; i3++) {
                int val = req.MinCoverPerShift[shiftType.Index][i3];
                String v = Integer.toString(val);
                if (val < 0)
                  v = "-"; 
                String cs = "coverSatisfied";
                if (val >= 0 && val > prov.ProvidedCoverPerShift[shiftType.Index][i3])
                  cs = "coverNotSatisfied"; 
                sw.write("<td class=\"" + cs + "\">" + v + "</td>");
              } 
              sw.write("<td>&nbsp;</td></tr>");
            } 
            if (showMax) {
              sw.write("<tr id=\"MaxCover_shift_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Max</td>");
              for (i3 = 0; i3 < numDays; i3++) {
                int val = req.MaxCoverPerShift[shiftType.Index][i3];
                String v = Integer.toString(val);
                if (val < 0)
                  v = "-"; 
                String cs = "coverSatisfied";
                if (val >= 0 && val < prov.ProvidedCoverPerShift[shiftType.Index][i3])
                  cs = "coverNotSatisfied"; 
                sw.write("<td class=\"" + cs + "\">" + v + "</td>");
              } 
              sw.write("<td>&nbsp;</td></tr>");
            } 
            if (showPref) {
              sw.write("<tr id=\"PrefCover_shift_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Preferred</td>");
              for (i3 = 0; i3 < numDays; i3++) {
                int val = req.PrefCoverPerShift[shiftType.Index][i3];
                String v = Integer.toString(val);
                if (val < 0)
                  v = "-"; 
                String cs = "coverSatisfied";
                if (val >= 0 && val != prov.ProvidedCoverPerShift[shiftType.Index][i3])
                  cs = "coverNotSatisfied"; 
                sw.write("<td class=\"" + cs + "\">" + v + "</td>");
              } 
              sw.write("<td>&nbsp;</td></tr>");
            } 
            int totPen = 0;
            sw.write("<tr id=\"ProvCover_shift_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Provided</td>");
            for (int i4 = 0; i4 < numDays; i4++) {
              sw.write("<td class=\"coverProvided\">" + prov.ProvidedCoverPerShift[shiftType.Index][i4] + "</td>");
              totPen += prov.CoverPerShiftPenalty[shiftType.Index][i4];
            } 
            sw.write("<td align=\"right\">" + totPen + "</td></tr>");
          } 
        } 
      } 
      for (k = 0; k < schedulingPeriod.CoverRequirements.Requirements.length; k++) {
        CoverRequirement req = schedulingPeriod.CoverRequirements.Requirements[k];
        CoverProvided prov = roster.CoverProvided[k];
        if (req.CoverPerPeriodUsed) {
          String skillID = "-";
          String skillLabel = "-";
          String plural = "";
          if (req.SkillType == CoverRequirement.SkillTypes.AnySkill) {
            skillID = "__All__";
            skillLabel = "All";
          } else if (req.SkillType == CoverRequirement.SkillTypes.SingleSkill) {
            skillID = req.SkillID;
            skillLabel = req.SkillID;
          } else if (req.SkillType == CoverRequirement.SkillTypes.SkillGroup) {
            skillID = "__grp__" + req.SkillID;
            skillLabel = "(" + (schedulingPeriod.GetSkillGroup(req.SkillID)).Label + ")";
            plural = "s";
          } 
          sw.write("<tr style=\"\"><td colspan=\"" + cols3 + "\">Period Cover (Skill" + plural + " : <span style=\"font-weight:bold\">" + skillLabel + "</span>) ");
          sw.write("<script language=\"JavaScript\"><!--");
          sw.write("document.write('<a id=\"HideCover_period_skill_" + skillID + "\" title=\"Hide\" class=\"showHide\" href=\"javascript:;\" OnClick=\"hideCover(\\'period\\',\\'" + skillID + "\\'," + sortedPeriods.length + "); return false;\">Hide</a> ');");
          sw.write("document.write('<a id=\"ShowCover_period_skill_" + skillID + "\"title=\"Show\" style=\"display:none\" class=\"showHide\" href=\"javascript:;\" OnClick=\"showCover(\\'period\\',\\'" + skillID + "\\'," + sortedPeriods.length + "); return false;\">Show</a>');");
          sw.write("//-->");
          sw.write("</script>");
          sw.write("</td></tr>");
          for (int i2 = 0; i2 < sortedPeriods.length; i2++) {
            DayPeriod period = sortedPeriods[i2];
            String str = " (" + period.getStart().ToString("HH:mm") + "-" + period.getEnd().ToString("HH:mm") + ")";
            boolean showMin = false;
            boolean showMax = false;
            boolean showPref = false;
            int i3;
            for (i3 = 0; i3 < numDays; i3++) {
              if (req.MinCoverPerPeriod[period.Index][i3] >= 0)
                showMin = true; 
              if (req.MaxCoverPerPeriod[period.Index][i3] >= 0)
                showMax = true; 
              if (req.PrefCoverPerPeriod[period.Index][i3] >= 0)
                showPref = true; 
            } 
            if (showMin) {
              sw.write("<tr id=\"MinCover_period_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Min</td>");
              for (i3 = 0; i3 < numDays; i3++) {
                int val = req.MinCoverPerPeriod[period.Index][i3];
                String v = Integer.toString(val);
                if (val < 0)
                  v = "-"; 
                String cs = "coverSatisfied";
                if (val >= 0 && val > prov.ProvidedCoverPerPeriod[period.Index][i3])
                  cs = "coverNotSatisfied"; 
                sw.write("<td class=\"" + cs + "\">" + v + "</td>");
              } 
              sw.write("<td>&nbsp;</td></tr>");
            } 
            if (showMax) {
              sw.write("<tr id=\"MaxCover_period_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Max</td>");
              for (i3 = 0; i3 < numDays; i3++) {
                int val = req.MaxCoverPerPeriod[period.Index][i3];
                String v = Integer.toString(val);
                if (val < 0)
                  v = "-"; 
                String cs = "coverSatisfied";
                if (val >= 0 && val < prov.ProvidedCoverPerPeriod[period.Index][i3])
                  cs = "coverNotSatisfied"; 
                sw.write("<td class=\"" + cs + "\">" + v + "</td>");
              } 
              sw.write("<td>&nbsp;</td></tr>");
            } 
            if (showPref) {
              sw.write("<tr id=\"PrefCover_period_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Preferred</td>");
              for (i3 = 0; i3 < numDays; i3++) {
                int val = req.PrefCoverPerPeriod[period.Index][i3];
                String v = Integer.toString(val);
                if (val < 0)
                  v = "-"; 
                String cs = "coverSatisfied";
                if (val >= 0 && val != prov.ProvidedCoverPerPeriod[period.Index][i3])
                  cs = "coverNotSatisfied"; 
                sw.write("<td class=\"" + cs + "\">" + v + "</td>");
              } 
              sw.write("<td>&nbsp;</td></tr>");
            } 
            int totPen = 0;
            sw.write("<tr id=\"ProvCover_period_" + i2 + "skill_" + skillID + "\"><td class=\"\">" + str + " Provided</td>");
            for (int i4 = 0; i4 < numDays; i4++) {
              sw.write("<td class=\"coverProvided\">" + prov.ProvidedCoverPerPeriod[period.Index][i4] + "</td>");
              totPen += prov.CoverPerPeriodPenalty[period.Index][i4];
            } 
            sw.write("<td align=\"right\">" + totPen + "</td></tr>");
          } 
        } 
      } 
      sw.write("<tr><td align=\"right\" colspan=\"" + cols + "\">Total Penalty</td><td align=\"right\">" + roster.getTotalPenalty() + "</td></tr>");
      sw.write("</table></p>");
      sw.write("<table border=\"0\"><tr><td valign=\"top\">");
      sw.write("<table border=\"0\"><tr><td style=\"font-weight:bold;\">Shifts</td><td align=\"right\">Start</td><td align=\"right\">End</td><td>Description</td></tr>");
      for (int x = 0; x < sortedShiftTypes.length; x++) {
        ShiftType st = sortedShiftTypes[x];
        String str = "";
        str = String.valueOf(str) + st.Description + ", ";
        if (st.getSpansMidnight())
          str = String.valueOf(str) + " (night shift=true),"; 
        str = String.valueOf(str) + " hours worked=" + st.HoursWorked + " hours,";
        str = String.valueOf(str) + " duration=" + st.getDuration() + " hours. ";
        if (schedulingPeriod.MasterWeights.MinTimeBetweenShifts > 0 && (
          st.FreeTimeBefore > 0.0D || st.FreeTimeAfter > 0.0D)) {
          str = String.valueOf(str) + "Minimum " + st.FreeTimeBefore + " minutes rest required before,";
          str = String.valueOf(str) + " minimum " + st.FreeTimeAfter + " minutes rest required after. ";
        } 
        if (st.RequiresSkills) {
          if (st.GetSkills().size() == 1) {
            str = String.valueOf(str) + "Requires skill: ";
          } else {
            str = String.valueOf(str) + "Requires skills: ";
          } 
          int i2 = 0;
          for (String skill : st.GetSkills()) {
            str = String.valueOf(str) + skill;
            if (i2++ != st.GetSkills().size() - 1)
              str = String.valueOf(str) + ","; 
          } 
          str = String.valueOf(str) + ". ";
        } 
        sw.write("<tr>");
        sw.write("<td><p class=\"shiftType\" style=\"background-color:" + st.HtmlColor + "\">" + st.Label + "</p></td>");
        sw.write("<td align=\"right\">" + st.getStartTime().ToString("HH:mm") + "</td>");
        sw.write("<td align=\"right\">" + st.getEndTime().ToString("HH:mm") + "</td>");
        sw.write("<td>" + str + "</td>");
        sw.write("</tr>");
      } 
      sw.write("</table>");
      sw.write("</td><td style=\"width: 10px; border-right: 1px solid #E0E0E0;\"> &nbsp; </td><td valign=\"top\">");
      sw.write("<table border=\"0\"><tr><td style=\"font-weight:bold;\">Cover weights</td><td></td></tr>");
      sw.write("<tr><td>Over preferred</td>");
      sw.write("<td>" + schedulingPeriod.MasterWeights.PrefOverStaffing + "</td></tr>");
      sw.write("<tr><td>Under preferred</td>");
      sw.write("<td>" + schedulingPeriod.MasterWeights.PrefUnderStaffing + "</td></tr>");
      sw.write("<tr><td>Over maximum</td>");
      sw.write("<td>" + schedulingPeriod.MasterWeights.MaxOverStaffing + "</td></tr>");
      sw.write("<tr><td>Under minimum</td>");
      sw.write("<td>" + schedulingPeriod.MasterWeights.MinUnderStaffing + "</td></tr>");
      sw.write("</table>");
      sw.write("</td></tr></table></p>");
      sw.write("<p><table border=\"0\"><tr>");
      int keyCellHeight = 18;
      sw.write("<td valign=\"top\"><p><table>");
      sw.write("<tr><td style=\"width : 25px; height : " + keyCellHeight + "px;\"></td><td style=\"font-weight:bold; width:450px\">KEY</td></tr>");
      sw.write("<tr><td style=\"height : " + keyCellHeight + "px;\" class=\"bankHolidayHeader\"> &nbsp; </td><td>Bank holiday</td></tr>");
      sw.write("<tr><td style=\"height : " + keyCellHeight + "px;\" class=\"weekendHeader\">&nbsp;</td><td>Weekend</td></tr>");
      sw.write("<tr><td style=\"height : " + keyCellHeight + "px;\" class=\"employeeUnavailable\">&nbsp;</td><td>Employee unavailable</td></tr>");
      if (javascript) {
        sw.write("<tr><td style=\"height : " + keyCellHeight + "px;\" class=\"dayOffHigh\">&nbsp;</td><td>Day off request</td></tr>");
        sw.write("<tr><td style=\"height : " + keyCellHeight + "px;\" class=\"dayOnHigh\">&nbsp;</td><td>Day on request</td></tr>");
        sw.write("<tr><td style=\"height : " + keyCellHeight + "px;\" class=\"shiftOffHigh\">&nbsp;</td><td>Shift off request</td></tr>");
        sw.write("<tr><td style=\"height : " + keyCellHeight + "px;\" class=\"shiftOnHigh\">&nbsp;</td><td>Shift on request</td></tr>");
      } 
      sw.write("</table></p></td>");
      sw.write("<td style=\"width: 10px; border-right: 1px solid #E0E0E0;\"> &nbsp; </td>");
      sw.write("<td valign=\"top\"><p><table>");
      if (system == null)
        system = ""; 
      if (foundBy == null)
        foundBy = ""; 
      if (date == null)
        date = DateTime.getNow().ToLongDateString(); 
      if (algorithm == null)
        algorithm = "..."; 
      if (computationTime == null)
        computationTime = "..."; 
      sw.write("<tr><td colspan=\"3\" style=\"font-weight:bold\">Computation</td></tr>");
      sw.write("<tr><td style=\"width:120px\">Found by</td><td style=\"width: 10px;\"> &nbsp; </td>\n<td>" + 
          foundBy + "</td></tr>\n");
      sw.write("<tr><td valign=\"top\">Algorithm</td><td></td>\n<td>" + 
          algorithm + "</td></tr>\n");
      sw.write("<tr><td valign=\"top\">Computation time</td><td></td>\n<td>" + 
          computationTime + "</td></tr>\n");
      sw.write("<tr><td>Date</td><td></td>\n<td>" + 
          date + "</td></tr>\n");
      sw.write("<tr><td valign=\"top\">System</td><td></td>\n<td>" + 
          system + "</td></tr>\n");
      if (CPU == null) {
        CPU = "";
      } else {
        sw.write("<tr><td valign=\"top\">CPU</td><td></td>\n<td>" + 
            CPU + "</td></tr>\n");
      } 
      sw.write("</table></p></td>");
      sw.write("</tr></table></p>");
      sw.write("<table class=\"separator\" border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">");
      sw.write("<tr><td style=\"border-width: 0px 0px 0px 0px\">&nbsp;</td></tr></table>");
      sw.write("<div class=\"penaltyBreakdown\"><p style=\"font-weight:bold; font-size: 16px;\">Penalties</p>");
      byte b2;
      int i1;
      Employee[] arrayOfEmployee2;
      for (i1 = (arrayOfEmployee2 = roster.Employees).length, b2 = 0; b2 < i1; ) {
        Employee employee = arrayOfEmployee2[b2];
        String employeeName = employee.EmployeeDescription.getName();
        sw.write("<a name=\"" + employee.EmployeeDescription.ID + "\"><span class=\"sideTitle\">" + employeeName + "</span></a>");
        if (!employee.EmployeeDescription.GetSkillsDescription().equalsIgnoreCase(""))
          sw.write("<br/>Skills: " + employee.EmployeeDescription.GetSkillsDescription()); 
        if (employee.EmployeeDescription.Contract != null) {
          sw.write("<br/>Contract: " + employee.EmployeeDescription.Contract.ContractID);
          sw.write("<table border=\"0\" cellspacing=\"3\" cellpadding=\"2\"><tr>");
          if (multiObjInfo)
            sw.write("<td class=\"penaltiesTableHeader\"> </td>"); 
          sw.write("<td class=\"penaltiesTableHeader\">Constraint</td>");
          sw.write("<td style=\"padding-right:10px;\" class=\"penaltiesTableHeader\">Penalty</td>");
          sw.write("<td class=\"penaltiesTableHeader\">Required</td>");
          if (multiObjInfo)
            sw.write("<td class=\"penaltiesTableHeader\">Used</td>"); 
          sw.write("<td style=\"padding-right:10px;\" class=\"penaltiesTableHeader\">Weight</td>");
          sw.write("<td class=\"penaltiesTableHeader\">Details</td></tr>");
          for (int i2 = 0; i2 < employee.EmployeeDescription.AllSoftConstraints.length; i2++) {
            SoftConstraint sc = employee.EmployeeDescription.AllSoftConstraints[i2];
            int pen = sc.Calculate(employee);
            String required = "Yes";
            String used = "Yes";
            sw.write("<tr>");
            if (multiObjInfo)
              sw.write("<td style=\"padding-right:8px; text-align : right\" valign=\"top\">" + (i2 + 1) + "</td>"); 
            sw.write("<td valign=\"top\">" + sc.getTitle() + "</td>");
            sw.write("<td style=\"padding-right:10px; text-align : right\" valign=\"top\">" + pen + "</td>");
            sw.write("<td valign=\"top\">" + required + "</td>");
            if (multiObjInfo)
              sw.write("<td valign=\"top\">" + used + "</td>"); 
            sw.write("<td style=\"padding-right:10px; text-align : right\" valign=\"top\">" + sc.getWeight() + "</td>");
            sw.write("<td valign=\"top\">" + sc.GetDescription(employee) + "</td></tr>");
          } 
          sw.write("</table>");
        } 
        sw.write("<table class=\"separator\" border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">");
        sw.write("<tr><td style=\"border-width: 0px 0px 0px 0px\">&nbsp;</td></tr></table>");
        b2++;
      } 
      sw.write("<a name=\"TotalPenaltyBreakdown\"><h3 class=\"sideTitle\">Total Employees' Penalty</h3></a>");
      sw.write("<table border=\"0\" cellspacing=\"2\" cellpadding=\"1\">");
      sw.write("<tr><td></td><td class=\"penaltiesTableHeader\">Constraint</td>");
      sw.write("<td class=\"penaltiesTableHeader\">Penalty</td>");
      int totalPenalty = 0;
      sw.write("<tr><td></td><td style=\"font-weight:bold\" valign=\"top\">Total</td>");
      sw.write("<td style=\"text-align : right; font-weight:bold\" valign=\"top\">" + totalPenalty + "</td>");
      sw.write("</table>");
      sw.write("</div>");
      sw.write("</body></html>");
      sw.close();
    } catch (Exception ex) {
      System.out.println("Error:" + ex.getMessage());
      ex.printStackTrace();
      return false;
    } 
    return true;
  }
  
  public static boolean PrintRosterAsXML(Roster roster, String filePath) {
    try {
      PrintWriter sw = new PrintWriter(new FileWriter(filePath));
      return PrintRosterAsXML(roster, sw);
    } catch (Exception ex) {
      return false;
    } 
  }
  
  public static boolean PrintRosterAsXML(Roster roster, Writer sw) {
    try {
      SchedulingPeriod period = roster.SchedulingPeriod;
      sw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      sw.write("<?cocoon-format type=\"text/xml\"?>\n");
      sw.write("<Schedule xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"Schedule.xsd\">" + System.getProperty("line.separator"));
      sw.write("  <OrganisationID>" + period.OrganisationID + "</OrganisationID>\n");
      sw.write("  <SchedulingPeriodID>" + period.SchedulingPeriodID + "</SchedulingPeriodID>" + System.getProperty("line.separator"));
      byte b;
      int i;
      Employee[] arrayOfEmployee;
      for (i = (arrayOfEmployee = roster.Employees).length, b = 0; b < i; ) {
        Employee employee = arrayOfEmployee[b];
        for (int day = 0; day < roster.SchedulingPeriod.NumDaysInPeriod; day++) {
          for (int index = 0; index < roster.SchedulingPeriod.ShiftTypesCount; index++) {
            Shift shift = employee.ShiftsOnDay[day][index];
            if (shift != null) {
              sw.write("  <Assignment>");
              sw.write("    <Date>" + shift.StartTime.ToString("yyyy-MM-dd", Locale.US) + "</Date>\n");
              sw.write("    <EmployeeID>" + employee.EmployeeDescription.ID + "</EmployeeID>\n");
              sw.write("    <ShiftID>" + shift.ShiftType.ID + "</ShiftID>\n");
              sw.write("  </Assignment>\n");
            } 
          } 
        } 
        sw.write("");
        b++;
      } 
      sw.write("</Schedule>\n");
      sw.close();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return false;
    } 
    return true;
  }
  
  public static String GetShiftGroupOnMessage(Employee employee, int testDay) {
    String msg = null;
    if (employee.EmployeeDescription.ShiftGroupOnRequests == null)
      return msg; 
    for (int i = 0; i < employee.EmployeeDescription.ShiftGroupOnRequests.length; i++) {
      ShiftGroup group = (employee.EmployeeDescription.ShiftGroupOnRequests[i]).ShiftGroup;
      int day = (employee.EmployeeDescription.ShiftGroupOnRequests[i]).Day;
      if (day == testDay) {
        msg = String.valueOf(msg) + "Requests shift from group (";
        if (group.Group.length > 0)
          msg = String.valueOf(msg) + (group.Group[0]).Label; 
        for (int j = 1; j < group.Group.length; j++)
          msg = String.valueOf(msg) + "," + (group.Group[j]).Label; 
        msg = String.valueOf(msg) + ") [weight=" + (employee.EmployeeDescription.ShiftGroupOnRequests[i]).Weight + "]. " + System.getProperty("line.separator");
      } 
    } 
    return msg;
  }
}
