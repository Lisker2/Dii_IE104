package org.example.util.ASAP.NRP.Core.Parsers;

import java.util.ArrayList;

public class CoverSpecification {
  public ArrayList<Cover> CoverRequirements = new ArrayList<Cover>();
  
  public boolean AddCoverRequirement(Cover cover) {
    this.CoverRequirements.add(cover);
    return true;
  }
}
