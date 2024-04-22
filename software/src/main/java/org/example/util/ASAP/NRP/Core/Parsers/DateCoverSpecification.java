package org.example.util.ASAP.NRP.Core.Parsers;

import org.example.util.ASAP.NRP.Core.DateTime;

public class DateCoverSpecification extends CoverSpecification {
  public DateTime Date;
  
  public DateCoverSpecification(DateTime date) {
    this.Date = date;
  }
}
