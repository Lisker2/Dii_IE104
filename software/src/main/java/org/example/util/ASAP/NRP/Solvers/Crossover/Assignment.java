package org.example.util.ASAP.NRP.Solvers.Crossover;

import org.example.util.ASAP.NRP.Core.Shift;

class Assignment implements Comparable<Assignment> {
  public int Difference;
  
  public Shift Shift;
  
  public Assignment(Shift shift, int difference) {
    this.Shift = shift;
    this.Difference = difference;
  }
  
  public int compareTo(Assignment a2) {
    return a2.Difference - this.Difference;
  }
}
