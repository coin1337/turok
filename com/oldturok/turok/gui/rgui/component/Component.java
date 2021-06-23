package com.oldturok.turok.gui.rgui.component;

import com.oldturok.turok.gui.rgui.component.container.Container;
import com.oldturok.turok.gui.rgui.component.container.use.Frame;
import com.oldturok.turok.gui.rgui.component.listen.KeyListener;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.listen.RenderListener;
import com.oldturok.turok.gui.rgui.component.listen.TickListener;
import com.oldturok.turok.gui.rgui.component.listen.UpdateListener;
import com.oldturok.turok.gui.rgui.poof.IPoof;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import com.oldturok.turok.gui.rgui.render.ComponentUI;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import java.util.ArrayList;

public interface Component {
   int getX();

   int getY();

   int getWidth();

   int getHeight();

   void setX(int var1);

   void setY(int var1);

   void setWidth(int var1);

   void setHeight(int var1);

   void embedFrame(Frame var1);

   Component setMinimumWidth(int var1);

   Component setMaximumWidth(int var1);

   Component setMinimumHeight(int var1);

   Component setMaximumHeight(int var1);

   int getMinimumWidth();

   int getMaximumWidth();

   int getMinimumHeight();

   int getMaximumHeight();

   float getOpacity();

   void setOpacity(float var1);

   boolean doAffectLayout();

   void setAffectLayout(boolean var1);

   Container getParent();

   void setParent(Container var1);

   boolean liesIn(Component var1);

   boolean isVisible();

   void setVisible(boolean var1);

   void setFocussed(boolean var1);

   boolean isFocussed();

   ComponentUI getUI();

   Theme getTheme();

   void setTheme(Theme var1);

   boolean isHovered();

   boolean isPressed();

   ArrayList<MouseListener> getMouseListeners();

   void addMouseListener(MouseListener var1);

   ArrayList<RenderListener> getRenderListeners();

   void addRenderListener(RenderListener var1);

   ArrayList<KeyListener> getKeyListeners();

   void addKeyListener(KeyListener var1);

   ArrayList<UpdateListener> getUpdateListeners();

   void addUpdateListener(UpdateListener var1);

   ArrayList<TickListener> getTickListeners();

   void addTickListener(TickListener var1);

   void addPoof(IPoof var1);

   void callPoof(Class<? extends IPoof> var1, PoofInfo var2);

   int getPriority();

   void kill();
}
