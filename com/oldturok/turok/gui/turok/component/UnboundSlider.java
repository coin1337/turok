package com.oldturok.turok.gui.turok.component;

import com.oldturok.turok.gui.rgui.component.AbstractComponent;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.use.Slider;

public class UnboundSlider extends AbstractComponent {
   double value;
   String text;
   public int sensitivity = 5;
   int originX;
   double originValue;
   boolean integer;
   double max = Double.MAX_VALUE;
   double min = Double.MIN_VALUE;

   public UnboundSlider(double value, String text, boolean integer) {
      this.value = value;
      this.text = text;
      this.integer = integer;
      this.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            UnboundSlider.this.originX = event.getX();
            UnboundSlider.this.originValue = UnboundSlider.this.getValue();
         }

         public void onMouseRelease(MouseListener.MouseButtonEvent event) {
            UnboundSlider.this.originValue = UnboundSlider.this.getValue();
            UnboundSlider.this.originX = event.getX();
         }

         public void onMouseDrag(MouseListener.MouseButtonEvent event) {
            int diff = (UnboundSlider.this.originX - event.getX()) / UnboundSlider.this.sensitivity;
            UnboundSlider.this.setValue(Math.floor((UnboundSlider.this.originValue - (double)diff * (UnboundSlider.this.originValue == 0.0D ? 1.0D : Math.abs(UnboundSlider.this.originValue) / 10.0D)) * 10.0D) / 10.0D);
         }

         public void onMouseMove(MouseListener.MouseMoveEvent event) {
         }

         public void onScroll(MouseListener.MouseScrollEvent event) {
            UnboundSlider.this.setValue((double)Math.round(UnboundSlider.this.getValue() + (double)(event.isUp() ? 1 : -1)));
            UnboundSlider.this.originValue = UnboundSlider.this.getValue();
         }
      });
   }

   public void setMax(double max) {
      this.max = max;
   }

   public void setMin(double min) {
      this.min = min;
   }

   public void setValue(double value) {
      if (this.min != Double.MIN_VALUE) {
         value = Math.max(value, this.min);
      }

      if (this.max != Double.MAX_VALUE) {
         value = Math.min(value, this.max);
      }

      Slider.SliderPoof.SliderPoofInfo info = new Slider.SliderPoof.SliderPoofInfo(this.value, value);
      this.callPoof(Slider.SliderPoof.class, info);
      this.value = this.integer ? Math.floor(info.getNewValue()) : info.getNewValue();
   }

   public double getValue() {
      return this.value;
   }

   public String getText() {
      return this.text;
   }
}
