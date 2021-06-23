package com.oldturok.turok.gui.rgui.component.use;

import com.oldturok.turok.gui.rgui.component.AbstractComponent;
import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import com.oldturok.turok.gui.rgui.poof.use.Poof;
import net.minecraft.util.math.MathHelper;

public class Slider extends AbstractComponent {
   double value;
   double minimum;
   double maximum;
   double step;
   String text;
   boolean integer;

   public Slider(double value, double minimum, double maximum, double step, String text, boolean integer) {
      this.value = value;
      this.minimum = minimum;
      this.maximum = maximum;
      this.step = step;
      this.text = text;
      this.integer = integer;
      this.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            Slider.this.setValue(Slider.this.calculateValue((double)event.getX()));
         }

         public void onMouseRelease(MouseListener.MouseButtonEvent event) {
         }

         public void onMouseDrag(MouseListener.MouseButtonEvent event) {
            Slider.this.setValue(Slider.this.calculateValue((double)event.getX()));
         }

         public void onMouseMove(MouseListener.MouseMoveEvent event) {
         }

         public void onScroll(MouseListener.MouseScrollEvent event) {
         }
      });
   }

   public Slider(double value, double minimum, double maximum, String text) {
      this(value, minimum, maximum, getDefaultStep(minimum, maximum), text, false);
   }

   private double calculateValue(double x) {
      double d1 = x / (double)this.getWidth();
      double d2 = this.maximum - this.minimum;
      double s = d1 * d2 + this.minimum;
      return MathHelper.func_151237_a(Math.floor((double)Math.round(s / this.step) * this.step * 100.0D) / 100.0D, this.minimum, this.maximum);
   }

   public static double getDefaultStep(double min, double max) {
      double s = gcd(min, max);
      if (s == max) {
         s = max / 20.0D;
      }

      if (max > 10.0D) {
         s = (double)Math.round(s);
      }

      if (s == 0.0D) {
         s = max;
      }

      return s;
   }

   public String getText() {
      return this.text;
   }

   public double getStep() {
      return this.step;
   }

   public double getValue() {
      return this.value;
   }

   public double getMaximum() {
      return this.maximum;
   }

   public double getMinimum() {
      return this.minimum;
   }

   public void setValue(double value) {
      Slider.SliderPoof.SliderPoofInfo info = new Slider.SliderPoof.SliderPoofInfo(this.value, value);
      this.callPoof(Slider.SliderPoof.class, info);
      double newValue = info.getNewValue();
      this.value = this.integer ? (double)((int)newValue) : newValue;
   }

   public static double gcd(double a, double b) {
      a = Math.floor(a);
      b = Math.floor(b);
      return a != 0.0D && b != 0.0D ? gcd(b, a % b) : a + b;
   }

   public abstract static class SliderPoof<T extends Component, S extends Slider.SliderPoof.SliderPoofInfo> extends Poof<T, S> {
      public static class SliderPoofInfo extends PoofInfo {
         double oldValue;
         double newValue;

         public SliderPoofInfo(double oldValue, double newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
         }

         public double getOldValue() {
            return this.oldValue;
         }

         public double getNewValue() {
            return this.newValue;
         }

         public void setNewValue(double newValue) {
            this.newValue = newValue;
         }
      }
   }
}
