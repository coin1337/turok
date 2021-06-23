package com.oldturok.turok.gui.rgui.component.container;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.layout.Layout;
import com.oldturok.turok.gui.rgui.render.theme.Theme;

public class OrganisedContainer extends AbstractContainer {
   Layout layout;

   public OrganisedContainer(Theme theme, Layout layout) {
      super(theme);
      this.layout = layout;
   }

   public Layout getLayout() {
      return this.layout;
   }

   public void setLayout(Layout layout) {
      this.layout = layout;
   }

   public Container addChild(Component... component) {
      super.addChild(component);
      this.layout.organiseContainer(this);
      return this;
   }

   public void setOriginOffsetX(int originoffsetX) {
      super.setOriginOffsetX(originoffsetX);
      this.layout.organiseContainer(this);
   }

   public void setOriginOffsetY(int originoffsetY) {
      super.setOriginOffsetY(originoffsetY);
      this.layout.organiseContainer(this);
   }
}
