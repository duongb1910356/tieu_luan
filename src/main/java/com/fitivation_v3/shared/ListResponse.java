package com.fitivation_v3.shared;

import java.util.List;
import java.util.Map;

public class ListResponse<T> {
  private List<T> items;
  private int totals;
  private Map<String, Object> options;
}
