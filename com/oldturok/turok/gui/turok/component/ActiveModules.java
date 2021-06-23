package com.oldturok.turok.gui.turok.component;

import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.listen.RenderListener;
import com.oldturok.turok.gui.rgui.component.use.Label;
import com.oldturok.turok.gui.rgui.util.ContainerHelper;
import com.oldturok.turok.gui.rgui.util.Docking;

public class ActiveModules extends Label {
   public boolean sort_up = true;

   public ActiveModules() {
      super("");
      this.addRenderListener(new RenderListener() {
         public void onPreRender() {
            Frame parentFrame = (Frame)ContainerHelper.getFirstParent(Frame.class, ActiveModules.this);
            if (parentFrame != null) {
               Docking docking = parentFrame.getDocking();
               if (docking.isTop()) {
                  ActiveModules.this.sort_up = true;
               }

               if (docking.isBottom()) {
                  ActiveModules.this.sort_up = false;
               }

            }
         }

         public void onPostRender() {
         }
      });
   }
}
