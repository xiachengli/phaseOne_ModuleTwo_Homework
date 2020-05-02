package com.xcl.service;

import java.math.BigDecimal;

public interface AccountService {

  void transfer(Integer from, Integer to, BigDecimal money);

}
