package com.oldturok.turok.setting.builder.primitive;

import com.oldturok.turok.setting.builder.SettingBuilder;
import com.oldturok.turok.setting.impl.StringSetting;

public class StringSettingBuilder extends SettingBuilder<String> {
   public StringSetting build() {
      return new StringSetting((String)this.initialValue, this.predicate(), this.consumer(), this.name, this.visibilityPredicate());
   }
}
