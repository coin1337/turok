package com.oldturok.turok.gui.turok.component;

import com.oldturok.turok.gui.rgui.component.Component;
import com.oldturok.turok.gui.rgui.component.container.OrganisedContainer;
import com.oldturok.turok.gui.rgui.component.use.CheckButton;
import com.oldturok.turok.gui.rgui.component.use.Slider;
import com.oldturok.turok.gui.rgui.render.theme.Theme;
import com.oldturok.turok.gui.turok.Stretcherlayout;
import com.oldturok.turok.module.Module;
import com.oldturok.turok.setting.Setting;
import com.oldturok.turok.setting.impl.BooleanSetting;
import com.oldturok.turok.setting.impl.EnumSetting;
import com.oldturok.turok.setting.impl.numerical.DoubleSetting;
import com.oldturok.turok.setting.impl.numerical.FloatSetting;
import com.oldturok.turok.setting.impl.numerical.IntegerSetting;
import com.oldturok.turok.setting.impl.numerical.NumberSetting;
import com.oldturok.turok.util.TurokBind;
import java.util.Arrays;
import java.util.Iterator;

public class SettingsPanel extends OrganisedContainer {
   Module module;

   public SettingsPanel(Theme theme, Module module) {
      super(theme, new Stretcherlayout(1));
      this.setAffectLayout(false);
      this.module = module;
      this.prepare();
   }

   public void renderChildren() {
      super.renderChildren();
   }

   public Module getModule() {
      return this.module;
   }

   private void prepare() {
      this.getChildren().clear();
      if (this.module == null) {
         this.setVisible(false);
      } else {
         if (!this.module.settingList.isEmpty()) {
            Iterator var1 = this.module.settingList.iterator();

            while(var1.hasNext()) {
               final Setting setting = (Setting)var1.next();
               if (setting.isVisible()) {
                  String name = setting.getName();
                  boolean is_number = setting instanceof NumberSetting;
                  boolean is_boolean = setting instanceof BooleanSetting;
                  boolean is_enum = setting instanceof EnumSetting;
                  if (setting.getValue() instanceof TurokBind) {
                     this.addChild(new Component[]{new BindButton("Bind", this.module)});
                  }

                  if (is_number) {
                     NumberSetting numberSetting = (NumberSetting)setting;
                     boolean is_bound = numberSetting.isBound();
                     double value = Double.parseDouble(numberSetting.getValue().toString());
                     if (!is_bound) {
                        UnboundSlider slider = new UnboundSlider(value, name, setting instanceof IntegerSetting);
                        slider.addPoof(new Slider.SliderPoof<UnboundSlider, Slider.SliderPoof.SliderPoofInfo>() {
                           public void execute(UnboundSlider component, Slider.SliderPoof.SliderPoofInfo info) {
                              if (setting instanceof IntegerSetting) {
                                 setting.setValue((int)info.getNewValue());
                              } else if (setting instanceof FloatSetting) {
                                 setting.setValue((float)info.getNewValue());
                              } else if (setting instanceof DoubleSetting) {
                                 setting.setValue(info.getNewValue());
                              }

                              SettingsPanel.this.setModule(SettingsPanel.this.module);
                           }
                        });
                        if (numberSetting.getMax() != null) {
                           slider.setMax(numberSetting.getMax().doubleValue());
                        }

                        if (numberSetting.getMin() != null) {
                           slider.setMin(numberSetting.getMin().doubleValue());
                        }

                        this.addChild(new Component[]{slider});
                     } else {
                        double min = Double.parseDouble(numberSetting.getMin().toString());
                        double max = Double.parseDouble(numberSetting.getMax().toString());
                        Slider slider = new Slider(value, min, max, Slider.getDefaultStep(min, max), name, setting instanceof IntegerSetting);
                        slider.addPoof(new Slider.SliderPoof<Slider, Slider.SliderPoof.SliderPoofInfo>() {
                           public void execute(Slider component, Slider.SliderPoof.SliderPoofInfo info) {
                              if (setting instanceof IntegerSetting) {
                                 setting.setValue((int)info.getNewValue());
                              } else if (setting instanceof FloatSetting) {
                                 setting.setValue((float)info.getNewValue());
                              } else if (setting instanceof DoubleSetting) {
                                 setting.setValue(info.getNewValue());
                              }

                           }
                        });
                        this.addChild(new Component[]{slider});
                     }
                  } else if (is_boolean) {
                     final CheckButton checkButton = new CheckButton(name);
                     checkButton.setToggled((Boolean)((BooleanSetting)setting).getValue());
                     checkButton.addPoof(new CheckButton.CheckButtonPoof<CheckButton, CheckButton.CheckButtonPoof.CheckButtonPoofInfo>() {
                        public void execute(CheckButton checkButton1, CheckButton.CheckButtonPoof.CheckButtonPoofInfo info) {
                           if (info.getAction() == CheckButton.CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE) {
                              setting.setValue(checkButton.isToggled());
                              SettingsPanel.this.setModule(SettingsPanel.this.module);
                           }

                        }
                     });
                     this.addChild(new Component[]{checkButton});
                  } else if (is_enum) {
                     Class<? extends Enum> type = ((EnumSetting)setting).clazz;
                     final Object[] con = type.getEnumConstants();
                     String[] modes = (String[])Arrays.stream(con).map((o) -> {
                        return o.toString().toUpperCase();
                     }).toArray((x$0) -> {
                        return new String[x$0];
                     });
                     EnumButton enumbutton = new EnumButton(name, modes);
                     enumbutton.addPoof(new EnumButton.EnumbuttonIndexPoof<EnumButton, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo>() {
                        public void execute(EnumButton component, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo info) {
                           setting.setValue(con[info.getNewIndex()]);
                           SettingsPanel.this.setModule(SettingsPanel.this.module);
                        }
                     });
                     enumbutton.setIndex(Arrays.asList(con).indexOf(setting.getValue()));
                     this.addChild(new Component[]{enumbutton});
                  }
               }
            }
         }

         if (this.children.isEmpty()) {
            this.setVisible(false);
         } else {
            this.setVisible(true);
         }
      }
   }

   public void setModule(Module module) {
      this.module = module;
      this.setMinimumWidth((int)((float)this.getParent().getWidth() * 1.0F));
      this.prepare();
      this.setAffectLayout(false);
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         Component component = (Component)var2.next();
         component.setX(5);
         component.setWidth(this.getWidth() - 5);
         if (component.getY() > this.getHeight()) {
            this.setHeight(this.getHeight() + component.getHeight() + 2);
         }
      }

   }
}
