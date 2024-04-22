package org.example.util.ASAP.NRP.Core;

public class BankHoliday {
  public DateTime Date;
  
  public String ID;
  
  public String Name;
  
  public BankHoliday(String ID) {
    this.Date = new DateTime();
    this.ID = "";
    this.Name = "";
    this.ID = ID;
  }
}
