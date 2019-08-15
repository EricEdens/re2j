package com.google.re2j;

public abstract class CharMatcher {

  public int indexOf(CharSequence seq, int from) {
    int p = from;
    while (p < seq.length()) {
      if (matches(seq.charAt(p))) {
        return p;
      } else {
        p++;
      }
    }
    return -1;
  }

  protected abstract boolean matches(char c);
}
