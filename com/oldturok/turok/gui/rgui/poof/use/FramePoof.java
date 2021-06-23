package com.oldturok.turok.gui.rgui.poof.use;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;

public abstract class FramePoof<T extends Component, S extends PoofInfo> extends Poof<T, S> {
   public static enum Action {
      MINIMIZE,
      MAXIMIZE,
      CLOSE;
   }

   public static class FramePoofInfo extends PoofInfo {
      private FramePoof.Action action;

      public FramePoofInfo(FramePoof.Action action) {
         this.action = action;
      }

      public FramePoof.Action getAction() {
         return this.action;
      }
   }
}
