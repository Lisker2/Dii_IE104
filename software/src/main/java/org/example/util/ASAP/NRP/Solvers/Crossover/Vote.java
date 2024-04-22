package org.example.util.ASAP.NRP.Solvers.Crossover;

import org.example.util.ASAP.NRP.Core.Shift;
import java.util.ArrayList;

class Vote implements Comparable<Vote> {
  public int ScheduleDay;
  
  public String ShiftTypeID;
  
  public String EmployeeID;
  
  public ArrayList<Voter> Voters;
  
  public Shift Shift;
  
  public Vote(int scheduleDay, String shiftTypeID, String employeeID, Shift shift) {
    this.ScheduleDay = scheduleDay;
    this.ShiftTypeID = shiftTypeID;
    this.EmployeeID = employeeID;
    this.Voters = new ArrayList<Voter>();
    this.Shift = shift;
  }
  
  public int getCount() {
    return this.Voters.size();
  }
  
  public int getVotesSuccessful() {
    int total = 0;
    for (int i = 0; i < this.Voters.size(); i++)
      total += ((Voter)this.Voters.get(i)).VotesSuccessful; 
    return total;
  }
  
  public int getTotalPenalty() {
    int total = 0;
    for (int i = 0; i < this.Voters.size(); i++)
      total += ((Voter)this.Voters.get(i)).getPenalty(); 
    return total;
  }
  
  public int compareTo(Vote v2) {
    if (getCount() != v2.getCount())
      return getCount() - v2.getCount(); 
    if (getVotesSuccessful() != v2.getVotesSuccessful())
      return v2.getVotesSuccessful() - getVotesSuccessful(); 
    if (getTotalPenalty() != v2.getTotalPenalty()) {
      if (v2.getTotalPenalty() - getTotalPenalty() > 0)
        return 1; 
      return -1;
    } 
    return 0;
  }
}
