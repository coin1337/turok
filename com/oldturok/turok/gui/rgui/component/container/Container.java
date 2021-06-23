package com.oldturok.turok.gui.rgui.component.container;

import com.oldturok.turok.gui.rgui.component.Component;
import java.util.ArrayList;

public interface Container extends Component {
   ArrayList<Component> getChildren();

   Component getComponentAt(int var1, int var2);

   Container addChild(Component... var1);

   Container removeChild(Component var1);

   boolean hasChild(Component var1);

   void renderChildren();

   int getOriginOffsetX();

   int getOriginOffsetY();

   boolean penetrateTest(int var1, int var2);
}
