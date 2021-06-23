package com.oldturok.turok.gui.rgui.poof.use;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.poof.IPoof;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import java.lang.reflect.ParameterizedType;

public abstract class Poof<T extends Component, S extends PoofInfo> implements IPoof<T, S> {
   private Class<T> componentclass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
   private Class<S> infoclass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];

   public Class getComponentClass() {
      return this.componentclass;
   }

   public Class<S> getInfoClass() {
      return this.infoclass;
   }
}
