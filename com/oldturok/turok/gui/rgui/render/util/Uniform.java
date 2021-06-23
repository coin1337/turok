package com.oldturok.turok.gui.rgui.render.util;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public final class Uniform {
   private final String name;
   private final int location;

   private Uniform(String name, int location) {
      this.name = name;
      this.location = location;
   }

   public final void setInt(int value) {
      ARBShaderObjects.glUniform1iARB(this.location, value);
   }

   public final void setFloat(float value) {
      ARBShaderObjects.glUniform1fARB(this.location, value);
   }

   public final void setBoolean(boolean value) {
      ARBShaderObjects.glUniform1fARB(this.location, value ? 1.0F : 0.0F);
   }

   public final void setVec(Vector2f value) {
      ARBShaderObjects.glUniform2fARB(this.location, value.x, value.y);
   }

   public final void setVec(Vector3f value) {
      ARBShaderObjects.glUniform3fARB(this.location, value.x, value.y, value.z);
   }

   public final String getName() {
      return this.name;
   }

   public final int getLocation() {
      return this.location;
   }

   public static Uniform get(int shaderID, String uniformName) {
      return new Uniform(uniformName, ARBShaderObjects.glGetUniformLocationARB(shaderID, uniformName));
   }
}
