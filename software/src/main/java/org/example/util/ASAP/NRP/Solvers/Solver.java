package org.example.util.ASAP.NRP.Solvers;

import org.example.util.ASAP.NRP.Core.Roster;

public interface Solver {
  void Solve(Roster paramRoster);
  
  String getAuthor();
  
  String getTitle();
  
  long getTotalEvaluations();
  
  boolean getStopped();
  
  void setStopped(boolean paramBoolean);
  
  int getRandomSeed();
  
  void setRandomSeed(int paramInt);
}
