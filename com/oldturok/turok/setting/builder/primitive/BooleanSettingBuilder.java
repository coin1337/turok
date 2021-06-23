package com.oldturok.turok.setting.builder.primitive;

import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.setting.impl.BooleanSetting;

public class BooleanSettingBuilder extends SettingBuilder<Boolean> {
   public BooleanSetting build() {
      return new BooleanSetting((Boolean)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
   }

   public BooleanSettingBuilder withName(String name) {
      return (BooleanSettingBuilder)super.withName(name);
   }
}
