package org.example.util.ASAP.NRP.Core.Constraints.TEC;

import org.example.util.ASAP.NRP.Core.CSharpConversionHelper;

public class PatternGroup {
  public Pattern[] Patterns = new Pattern[0];
  
  public int Weight;
  
  public PatternGroup(int Weight) {
    this.Weight = 0;
    this.Weight = Weight;
  }
  
  public void AddPattern(Pattern pattern) {
    this.Patterns = CSharpConversionHelper.ArrayResize(this.Patterns, this.Patterns.length + 1);
    this.Patterns[this.Patterns.length - 1] = pattern;
  }
}
