package com.oldturok.turok.gui.rgui.render.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringJoiner;

public final class StreamReader {
   private final InputStream stream;

   public StreamReader(InputStream stream) {
      this.stream = stream;
   }

   public final String read() {
      StringJoiner joiner = new StringJoiner("\n");

      try {
         BufferedReader br = new BufferedReader(new InputStreamReader(this.stream));

         String line;
         while((line = br.readLine()) != null) {
            joiner.add(line);
         }

         br.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      return joiner.toString();
   }
}
