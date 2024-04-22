package org.example.util.ASAP.NRP.Solvers.Crossover;

import org.example.util.ASAP.NRP.Core.Roster;

class Voter {
  int VotesSuccessful = 0;
  
  Roster roster;
  
  public Voter(Roster roster) {
    this.roster = roster;
  }
  
  public int getPenalty() {
    return this.roster.getTotalPenalty();
  }
}
