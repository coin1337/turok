package com.oldturok.turok.gui.rgui.render.util;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class Framebuffer {
   private int WIDTH;
   private int HEIGHT;
   private int framebufferID;
   private int framebufferTexture;
   private int framebufferDepthbuffer;

   public Framebuffer() {
      this(Display.getWidth(), Display.getHeight());
   }

   public Framebuffer(int WIDTH, int HEIGHT) {
      this.WIDTH = Display.getWidth();
      this.HEIGHT = Display.getHeight();
      this.WIDTH = WIDTH;
      this.HEIGHT = HEIGHT;
      this.initialiseFramebuffer();
   }

   public void cleanUp() {
      GL30.glDeleteFramebuffers(this.framebufferID);
      GL11.glDeleteTextures(this.framebufferTexture);
      GL30.glDeleteRenderbuffers(this.framebufferDepthbuffer);
   }

   public void bindFrameBuffer() {
      this.bindFrameBuffer(this.framebufferID, this.WIDTH, this.HEIGHT);
   }

   public void unbindFramebuffer() {
      GL30.glBindFramebuffer(36160, 0);
      GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
   }

   public int getFramebufferTexture() {
      return this.framebufferTexture;
   }

   private void initialiseFramebuffer() {
      this.framebufferID = this.createFrameBuffer();
      this.framebufferTexture = this.createTextureAttachment(this.WIDTH, this.HEIGHT);
      this.framebufferDepthbuffer = this.createDepthBufferAttachment(this.WIDTH, this.HEIGHT);
      this.unbindFramebuffer();
   }

   private void bindFrameBuffer(int frameBuffer, int width, int height) {
      GL11.glBindTexture(3553, 0);
      GL30.glBindFramebuffer(36160, frameBuffer);
      GL11.glViewport(0, 0, width, height);
   }

   private int createFrameBuffer() {
      int frameBuffer = GL30.glGenFramebuffers();
      GL30.glBindFramebuffer(36160, frameBuffer);
      GL11.glDrawBuffer(36064);
      return frameBuffer;
   }

   private int createTextureAttachment(int width, int height) {
      int texture = GL11.glGenTextures();
      GL11.glBindTexture(3553, texture);
      GL11.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, (ByteBuffer)null);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL32.glFramebufferTexture(36160, 36064, texture, 0);
      return texture;
   }

   private int createDepthTextureAttachment(int width, int height) {
      int texture = GL11.glGenTextures();
      GL11.glBindTexture(3553, texture);
      GL11.glTexImage2D(3553, 0, 33191, width, height, 0, 6402, 5126, (ByteBuffer)null);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL32.glFramebufferTexture(36160, 36096, texture, 0);
      return texture;
   }

   private int createDepthBufferAttachment(int width, int height) {
      int depthBuffer = GL30.glGenRenderbuffers();
      GL30.glBindRenderbuffer(36161, depthBuffer);
      GL30.glRenderbufferStorage(36161, 6402, width, height);
      GL30.glFramebufferRenderbuffer(36160, 36096, 36161, depthBuffer);
      return depthBuffer;
   }

   public void framebufferClear() {
      this.bindFrameBuffer();
      GL11.glClear(16384);
      this.unbindFramebuffer();
   }

   public int getWidth() {
      return this.WIDTH;
   }

   public int getHeight() {
      return this.HEIGHT;
   }
}
