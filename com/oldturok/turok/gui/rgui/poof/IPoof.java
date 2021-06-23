package com.oldturok.turok.gui.rgui.poof;

import com.oldturok.turok.gui.rgui.component.Component;

public interface IPoof<T extends Component, S extends PoofInfo> {
   void execute(T var1, S var2);

   Class getComponentClass();

   Class getInfoClass();
}
