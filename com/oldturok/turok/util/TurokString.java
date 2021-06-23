package com.oldturok.turok.util;

import java.util.LinkedList;
import java.util.List;

public class TurokString {
   public static String[] remove_element(String[] input, int indexToDelete) {
      List result = new LinkedList();

      for(int int_ = 0; int_ < input.length; ++int_) {
         if (int_ != indexToDelete) {
            result.add(input[int_]);
         }
      }

      return (String[])((String[])result.toArray(input));
   }

   public static String strip(String str, String key) {
      return str.startsWith(key) && str.endsWith(key) ? str.substring(key.length(), str.length() - key.length()) : str;
   }
}
