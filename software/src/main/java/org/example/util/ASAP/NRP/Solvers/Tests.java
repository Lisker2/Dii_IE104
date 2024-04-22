package org.example.util.ASAP.NRP.Solvers;

import org.example.util.ASAP.NRP.Core.DateTime;
import org.example.util.ASAP.NRP.Core.Roster;
import org.example.util.ASAP.NRP.Core.TimeSpan;
import org.example.util.ASAP.NRP.Core.Tools.RosterLoader;
import org.example.util.ASAP.NRP.Core.Tools.RosterPrinter;
import org.example.util.ASAP.NRP.Solvers.VariableDepthSearch.VDS_B;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Tests {
  RosterLoader rosterLoader = new RosterLoader();
  
  Solver search = null;
  
  boolean BATCH = false;
  
  String schedulingPeriodID = "GPost-B";
  
  String[] dataSets = new String[] { 
      "BCV-1.8.1", 
      "BCV-1.8.2", 
      "BCV-1.8.3", 
      "BCV-1.8.4", 
      "BCV-2.46.1", 
      "BCV-3.46.1", 
      "BCV-3.46.2", 
      "BCV-4.13.1", 
      "BCV-4.13.2", 
      "BCV-5.4.1", 
      "BCV-6.13.1", 
      "BCV-6.13.2", 
      "BCV-7.10.1", 
      "BCV-8.13.1", 
      "BCV-8.13.2", 
      "BCV-A.12.1", 
      "BCV-A.12.2", 
      "ORTEC01", 
      "QMC-1", 
      "QMC-2", 
      "SINTEF",
      "GPost",
      "GPost-B",
      "CHILD-A",
      "ERMGH-A",
      "ERRVH-A",
      "MER-A" };
  
  public void ExecuteSearch() {
    String dir = "";
    String dataDir = "ASAP/NRP/Solvers/resources/data/";
    String htmlSolutionDir = "ASAP/NRP/Solvers/resources/data/html/";
    String xmlSolutionDir = "ASAP/NRP/Solvers/resources/data/html/";
    String htmlDir = "ASAP/NRP/Solvers/resources/data/html/";
    String resultsFilePath = String.valueOf(htmlDir) + "Results/AllResults.txt";
    PrintWriter resultsStreamWriter = null;
    try {
      resultsStreamWriter = new PrintWriter(new FileWriter(resultsFilePath, true));
    } catch (Exception exception) {}
    String schedulingPeriodFileName = String.valueOf(dataDir) + this.schedulingPeriodID;
    this.rosterLoader.VERBOSE = false;
    Roster roster = null;
    roster = this.rosterLoader.CreateEmptyRoster(String.valueOf(schedulingPeriodFileName) + ".ros");
    if (roster == null)
      return; 
    if (!this.BATCH)
      this.search = new VDS_B(); 
    int penalty = roster.RecalculateAllPenalties();
    String intitialRosterDescription = "(Initial roster's penalty = " + penalty + ")";
    System.out.println("Initial roster's penalty is " + penalty + ".");
    DateTime start = DateTime.getNow();
    this.search.Solve(roster);
    TimeSpan elapsed = DateTime.getNow().subtract(start);
    System.out.println("Roster.Penalty=" + roster.getTotalPenalty());
    System.out.println("Roster RE-Penalty=" + roster.RecalculateAllPenalties());
    System.out.println("Roster.Penalty=" + roster.getTotalPenalty());
    System.out.println("Time Elapsed = " + elapsed.TotalSeconds + " seconds.");
    String computationTime = "Evaluations = " + this.search.getTotalEvaluations();
    System.out.println(computationTime);
    String htmlFile = String.valueOf(htmlSolutionDir) + this.schedulingPeriodID + ".Solution." + roster.getTotalPenalty() + ".html";
    if ((new File(htmlFile)).exists())
      for (int x = 1; x < 100; x++) {
        htmlFile = String.valueOf(htmlSolutionDir) + this.schedulingPeriodID + ".Solution." + roster.getTotalPenalty() + "_" + x + ".html";
        if (!(new File(htmlFile)).exists())
          break; 
      }  
    RosterPrinter.PrintRosterAsHTML(roster, 
        htmlFile, false, computationTime, 
        String.valueOf(this.search.getTitle()) + " " + intitialRosterDescription, (String)null, (String)null, (String)null, (String)null);
    System.out.println("Written " + htmlFile);
    String xmlFile = String.valueOf(xmlSolutionDir) + this.schedulingPeriodID + ".Solution." + roster.getTotalPenalty() + ".roster";
    if ((new File(xmlFile)).exists())
      for (int x = 1; x < 100; x++) {
        xmlFile = String.valueOf(xmlSolutionDir) + this.schedulingPeriodID + ".Solution." + roster.getTotalPenalty() + "_" + x + ".roster";
        if (!(new File(xmlFile)).exists())
          break; 
      }  
    RosterPrinter.PrintRosterAsXML(roster, xmlFile);
    System.out.println("Written " + xmlFile);
    if (resultsStreamWriter != null) {
      resultsStreamWriter.println(String.valueOf(this.schedulingPeriodID) + "\t" + roster.getTotalPenalty() + "\t" + this.search.getTotalEvaluations() + "\t" + "\t-\t" + DateTime.getNow() + "\t" + this.search.getTitle() + "\t" + this.search.getRandomSeed() + "\t" + elapsed.TotalSeconds);
      resultsStreamWriter.flush();
      resultsStreamWriter.close();
    } 
  }
  
  public void Start() {
    if (!this.BATCH) {
      ExecuteSearch();
    } else {
      BatchTests();
    } 
  }
  
  private void BatchTests() {}
}
