package org.example.util.ASAP.NRP.Core;

public class DayOffRequest {
  public DateTime Date;
  
  public int Weight;
  
  public boolean Working;
  
  public boolean Holiday;
  
  public DayOffRequest(DateTime date, int weight, boolean working, boolean holiday) {
    this.Date = date;
    this.Weight = weight;
    this.Working = working;
    this.Holiday = holiday;
  }
}
