package org.example.util.ASAP.NRP.Core;

public class DaysOffRequestBetweenDates {
  public int Min;
  
  public int Max;
  
  public int Weight;
  
  public int StartDay;
  
  public int EndDay;
  
  public DaysOffRequestBetweenDates(int startDay, int endDay, int min, int max, int weight) {
    this.StartDay = startDay;
    this.EndDay = endDay;
    this.Min = min;
    this.Max = max;
    this.Weight = weight;
  }
}
