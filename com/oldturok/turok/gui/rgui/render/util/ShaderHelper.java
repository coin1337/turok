package com.oldturok.turok.gui.rgui.render.util;

import org.lwjgl.opengl.ARBShaderObjects;

public final class ShaderHelper {
   private ShaderHelper() {
   }

   public static void createProgram(int programID) {
      ARBShaderObjects.glLinkProgramARB(programID);
      checkObjecti(programID, 35714);
      ARBShaderObjects.glValidateProgramARB(programID);
      checkObjecti(programID, 35715);
   }

   public static int loadShader(String path, int type) {
      int shaderID = ARBShaderObjects.glCreateShaderObjectARB(type);
      if (shaderID == 0) {
         return 0;
      } else {
         String src = (new StreamReader(ShaderHelper.class.getResourceAsStream(path))).read();
         ARBShaderObjects.glShaderSourceARB(shaderID, src);
         ARBShaderObjects.glCompileShaderARB(shaderID);
         checkObjecti(shaderID, 35713);
         return shaderID;
      }
   }

   private static String getLogInfo(int objID) {
      return ARBShaderObjects.glGetInfoLogARB(objID, ARBShaderObjects.glGetObjectParameteriARB(objID, 35716));
   }

   private static void checkObjecti(int objID, int name) {
      if (ARBShaderObjects.glGetObjectParameteriARB(objID, name) == 0) {
         try {
            throw new Exception(getLogInfo(objID));
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }
}
