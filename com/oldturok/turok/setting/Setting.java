package com.oldturok.turok.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.oldturok.turok.setting.converter.Convertable;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class Setting<T> implements ISettingUnknown, Convertable<T> {
   String name;
   T value;
   private Predicate<T> restriction;
   private Predicate<T> visibilityPredicate;
   private BiConsumer<T, T> consumer;
   private final Class valueType;

   public Setting(T value, Predicate<T> restriction, BiConsumer<T, T> consumer, String name, Predicate<T> visibilityPredicate) {
      this.value = value;
      this.valueType = value.getClass();
      this.restriction = restriction;
      this.consumer = consumer;
      this.name = name;
      this.visibilityPredicate = visibilityPredicate;
   }

   public String getName() {
      return this.name;
   }

   public T getValue() {
      return this.value;
   }

   public Class getValueClass() {
      return this.valueType;
   }

   public boolean setValue(T value) {
      T old = this.getValue();
      if (!this.restriction.test(value)) {
         return false;
      } else {
         this.value = value;
         this.consumer.accept(old, value);
         return true;
      }
   }

   public boolean isVisible() {
      return this.visibilityPredicate.test(this.getValue());
   }

   public BiConsumer<T, T> changeListener() {
      return this.consumer;
   }

   public void setValueFromString(String value) {
      JsonParser jp = new JsonParser();
      this.setValue(this.converter().reverse().convert(jp.parse(value)));
   }

   public String getValueAsString() {
      return ((JsonElement)this.converter().convert(this.getValue())).toString();
   }
}
