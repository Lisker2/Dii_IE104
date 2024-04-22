package org.example.util.PersonnelScheduling;

class Instance {
  String name;
  
  int best;
  
  boolean knownOptimal;
  
  public Instance(String name, int bestKnown, boolean knownOptimal) {
    this.name = name;
    this.best = bestKnown;
    this.knownOptimal = knownOptimal;
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isBestKnownOptimal() {
    return this.knownOptimal;
  }
  
  public int getBestKnown() {
    return this.best;
  }
}
