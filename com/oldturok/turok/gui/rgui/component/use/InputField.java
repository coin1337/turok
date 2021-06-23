package com.oldturok.turok.gui.rgui.component.use;

import com.oldturok.turok.gui.rgui.GUI;
import com.oldturok.turok.gui.rgui.component.AbstractComponent;
import com.oldturok.turok.gui.rgui.component.listen.KeyListener;
import com.oldturok.turok.gui.rgui.component.listen.MouseListener;
import com.oldturok.turok.gui.rgui.component.listen.RenderListener;
import com.oldturok.turok.gui.rgui.poof.PoofInfo;
import com.oldturok.turok.gui.rgui.poof.use.Poof;
import com.oldturok.turok.gui.rgui.render.font.FontRenderer;
import com.oldturok.turok.gui.turok.DisplayGuiScreen;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class InputField extends AbstractComponent {
   char echoChar;
   InputField.InputState currentState;
   long startRail;
   float railT;
   boolean rail;
   int railChar;
   KeyListener inputListener;
   int railDelay;
   int railRepeat;
   long lastTypeMS;
   int undoT;
   ArrayList<InputField.InputState> undoMap;
   ArrayList<InputField.InputState> redoMap;
   int scrollX;
   boolean shift;
   FontRenderer fontRenderer;

   public FontRenderer getFontRenderer() {
      return this.fontRenderer == null ? this.getTheme().getFontRenderer() : this.fontRenderer;
   }

   public void setFontRenderer(FontRenderer fontRenderer) {
      this.fontRenderer = fontRenderer;
   }

   public InputField(String text) {
      this.echoChar = 0;
      this.currentState = new InputField.InputState("", 0, false, 0, 0);
      this.startRail = 0L;
      this.railT = 0.0F;
      this.rail = false;
      this.railChar = 0;
      this.railDelay = 500;
      this.railRepeat = 32;
      this.lastTypeMS = 0L;
      this.undoT = 0;
      this.undoMap = new ArrayList();
      this.redoMap = new ArrayList();
      this.scrollX = 0;
      this.shift = false;
      this.fontRenderer = null;
      this.currentState.text = text;
      this.addRenderListener(new RenderListener() {
         public void onPreRender() {
         }

         public void onPostRender() {
            if (!InputField.this.isFocussed()) {
               InputField.this.currentState.selection = false;
            }

            int[] real = GUI.calculateRealPosition(InputField.this);
            int scale = DisplayGuiScreen.getScale();
            GL11.glScissor(real[0] * scale - InputField.this.getParent().getOriginOffsetX() - 1, Display.getHeight() - InputField.this.getHeight() * scale - real[1] * scale - 1, InputField.this.getWidth() * scale + InputField.this.getParent().getOriginOffsetX() + 1, InputField.this.getHeight() * scale + 1);
            GL11.glEnable(3089);
            GL11.glTranslatef((float)(-InputField.this.scrollX), 0.0F, 0.0F);
            FontRenderer fontRenderer = InputField.this.getFontRenderer();
            GL11.glLineWidth(1.0F);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            boolean cursor = (int)((System.currentTimeMillis() - InputField.this.lastTypeMS) / 500L) % 2 == 0 && InputField.this.isFocussed();
            int x = 0;
            int i = 0;
            boolean selection = false;
            if (InputField.this.getCursorRow() == 0 && cursor) {
               GL11.glBegin(1);
               GL11.glVertex2d(4.0D, 2.0D);
               GL11.glVertex2d(4.0D, (double)(fontRenderer.getFontHeight() - 1));
               GL11.glEnd();
            }

            char[] var8 = InputField.this.getDisplayText().toCharArray();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               char c = var8[var10];
               int w = fontRenderer.getStringWidth(c + "");
               if (InputField.this.getCurrentState().isSelection() && i == InputField.this.getCurrentState().getSelectionStart()) {
                  selection = true;
               }

               if (selection) {
                  GL11.glColor4f(0.2F, 0.6F, 1.0F, 0.3F);
                  GL11.glBegin(7);
                  GL11.glVertex2d((double)(x + 2), 2.0D);
                  GL11.glVertex2d((double)(x + 2), (double)(fontRenderer.getFontHeight() - 2));
                  GL11.glVertex2d((double)(x + w + 2), (double)(fontRenderer.getFontHeight() - 2));
                  GL11.glVertex2d((double)(x + w + 2), 2.0D);
                  GL11.glEnd();
               }

               ++i;
               x += w;
               if (i == InputField.this.getCursorRow() && cursor && !InputField.this.getCurrentState().isSelection()) {
                  GL11.glBegin(1);
                  GL11.glVertex2d((double)(x + 2), 2.0D);
                  GL11.glVertex2d((double)(x + 2), (double)fontRenderer.getFontHeight());
                  GL11.glEnd();
               }

               if (InputField.this.getCurrentState().isSelection() && i == InputField.this.getCurrentState().getSelectionEnd()) {
                  selection = false;
               }
            }

            String s = InputField.this.getDisplayText();
            if (s.isEmpty()) {
               s = " ";
            }

            GL11.glEnable(3042);
            fontRenderer.drawString(0, -1, s);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glTranslatef((float)InputField.this.scrollX, 0.0F, 0.0F);
            GL11.glDisable(3089);
         }
      });
      this.addKeyListener(this.inputListener = new KeyListener() {
         public void onKeyDown(KeyListener.KeyEvent event) {
            InputField.this.lastTypeMS = System.currentTimeMillis();
            if (event.getKey() == 14) {
               if (InputField.this.getText().length() > 0) {
                  InputField.this.pushUndo();
                  if (InputField.this.currentState.selection) {
                     InputField.this.currentState.cursorRow = InputField.this.currentState.selectionEnd;
                     InputField.this.scroll();
                     InputField.this.remove(InputField.this.currentState.selectionEnd - InputField.this.currentState.selectionStart);
                     InputField.this.currentState.selection = false;
                  } else {
                     InputField.this.remove(1);
                  }
               }
            } else if (Keyboard.getEventCharacter() == 26) {
               if (!InputField.this.undoMap.isEmpty()) {
                  InputField.this.redoMap.add(0, InputField.this.currentState.clone());
                  InputField.this.currentState = (InputField.InputState)InputField.this.undoMap.get(0);
                  InputField.this.undoMap.remove(0);
               }
            } else if (Keyboard.getEventCharacter() == 25) {
               if (!InputField.this.redoMap.isEmpty()) {
                  InputField.this.undoMap.add(0, InputField.this.currentState.clone());
                  InputField.this.currentState = (InputField.InputState)InputField.this.redoMap.get(0);
                  InputField.this.redoMap.remove(0);
               }
            } else if (Keyboard.getEventCharacter() == 1) {
               InputField.this.currentState.selection = true;
               InputField.this.currentState.selectionStart = 0;
               InputField.this.currentState.selectionEnd = InputField.this.currentState.getText().length();
            } else if (event.getKey() == 54) {
               InputField.this.shift = true;
            } else if (event.getKey() == 1) {
               InputField.this.currentState.selection = false;
            } else {
               Clipboard clipboard;
               if (Keyboard.getEventCharacter() == 22) {
                  clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                  try {
                     InputField.this.type((String)clipboard.getData(DataFlavor.stringFlavor));
                  } catch (UnsupportedFlavorException var4) {
                  } catch (IOException var5) {
                  }
               } else if (Keyboard.getEventCharacter() == 3) {
                  if (InputField.this.currentState.selection) {
                     clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                     StringSelection selection = new StringSelection(InputField.this.currentState.getText().substring(InputField.this.currentState.selectionStart, InputField.this.currentState.selectionEnd));
                     clipboard.setContents(selection, selection);
                  }
               } else if (event.getKey() == 205) {
                  if (InputField.this.currentState.cursorRow < InputField.this.getText().length()) {
                     if (InputField.this.shift) {
                        if (!InputField.this.currentState.selection) {
                           InputField.this.currentState.selectionStart = InputField.this.currentState.cursorRow;
                           InputField.this.currentState.selectionEnd = InputField.this.currentState.cursorRow;
                        }

                        InputField.this.currentState.selection = true;
                        InputField.this.currentState.selectionEnd = Math.min(InputField.this.getText().length(), InputField.this.currentState.selectionEnd + 1);
                     } else if (InputField.this.currentState.selection) {
                        InputField.this.currentState.selection = false;
                        InputField.this.currentState.cursorRow = InputField.this.currentState.selectionEnd;
                        InputField.this.scroll();
                     } else {
                        InputField.this.currentState.cursorRow = Math.min(InputField.this.getText().length(), InputField.this.currentState.cursorRow + 1);
                        InputField.this.scroll();
                     }
                  }
               } else if (event.getKey() == 203) {
                  if (InputField.this.currentState.cursorRow > 0) {
                     if (InputField.this.shift) {
                        if (!InputField.this.currentState.selection) {
                           InputField.this.currentState.selectionStart = InputField.this.currentState.cursorRow;
                           InputField.this.currentState.selectionEnd = InputField.this.currentState.cursorRow;
                        }

                        InputField.this.currentState.selection = true;
                        InputField.this.currentState.selectionStart = Math.max(0, InputField.this.currentState.selectionStart - 1);
                     } else if (InputField.this.currentState.selection) {
                        InputField.this.currentState.selection = false;
                        InputField.this.currentState.cursorRow = InputField.this.currentState.selectionStart;
                        InputField.this.scroll();
                     } else {
                        InputField.this.currentState.cursorRow = Math.max(0, InputField.this.currentState.cursorRow - 1);
                        InputField.this.scroll();
                     }
                  }
               } else if (Keyboard.getEventCharacter() != 0) {
                  InputField.this.pushUndo();
                  if (InputField.this.currentState.selection) {
                     InputField.this.currentState.cursorRow = InputField.this.currentState.selectionEnd;
                     InputField.this.remove(InputField.this.currentState.selectionEnd - InputField.this.currentState.selectionStart);
                     InputField.this.currentState.selection = false;
                  }

                  InputField.this.type(Keyboard.getEventCharacter() + "");
               }
            }

            if (event.getKey() != 42) {
               InputField.this.startRail = System.currentTimeMillis();
               InputField.this.railChar = event.getKey();
            }
         }

         public void onKeyUp(KeyListener.KeyEvent event) {
            InputField.this.rail = false;
            InputField.this.startRail = 0L;
            if (event.getKey() == 54) {
               InputField.this.shift = false;
            }

         }
      });
      this.addMouseListener(new MouseListener() {
         public void onMouseDown(MouseListener.MouseButtonEvent event) {
            InputField.this.currentState.selection = false;
            int x = -InputField.this.scrollX;
            int i = 0;
            char[] var4 = InputField.this.getText().toCharArray();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               char c = var4[var6];
               x += InputField.this.getFontRenderer().getStringWidth(c + "");
               if (event.getX() < x) {
                  InputField.this.currentState.cursorRow = i;
                  InputField.this.scroll();
                  return;
               }

               ++i;
            }

            InputField.this.currentState.cursorRow = i;
            InputField.this.scroll();
         }

         public void onMouseRelease(MouseListener.MouseButtonEvent event) {
         }

         public void onMouseDrag(MouseListener.MouseButtonEvent event) {
            InputField.this.currentState.selection = true;
            InputField.this.currentState.selectionStart = InputField.this.currentState.cursorRow;
            int x = -InputField.this.scrollX;
            int i = 0;
            char[] var4 = InputField.this.getText().toCharArray();
            int a = var4.length;

            for(int var6 = 0; var6 < a; ++var6) {
               char c = var4[var6];
               x += InputField.this.getFontRenderer().getStringWidth(c + "");
               if (event.getX() < x) {
                  InputField.this.currentState.selectionEnd = i;
                  InputField.this.scroll();
                  break;
               }

               ++i;
            }

            InputField.this.currentState.selectionEnd = i;
            int buf = InputField.this.currentState.cursorRow;
            InputField.this.currentState.cursorRow = i;
            InputField.this.scroll();
            InputField.this.currentState.cursorRow = buf;
            if (InputField.this.currentState.selectionStart > InputField.this.currentState.selectionEnd) {
               a = InputField.this.currentState.selectionStart;
               InputField.this.currentState.selectionStart = InputField.this.currentState.selectionEnd;
               InputField.this.currentState.selectionEnd = a;
            }

            if (InputField.this.currentState.selectionStart == InputField.this.currentState.selectionEnd) {
               InputField.this.currentState.selection = false;
            }

         }

         public void onMouseMove(MouseListener.MouseMoveEvent event) {
         }

         public void onScroll(MouseListener.MouseScrollEvent event) {
         }
      });
      this.addRenderListener(new RenderListener() {
         public void onPreRender() {
            if (InputField.this.startRail != 0L) {
               if (!InputField.this.rail) {
                  InputField.this.railT = (float)(System.currentTimeMillis() - InputField.this.startRail);
                  if (InputField.this.railT > (float)InputField.this.railDelay) {
                     InputField.this.rail = true;
                     InputField.this.startRail = System.currentTimeMillis();
                  }
               } else {
                  InputField.this.railT = (float)(System.currentTimeMillis() - InputField.this.startRail);
                  if (InputField.this.railT > (float)InputField.this.railRepeat) {
                     InputField.this.inputListener.onKeyDown(new KeyListener.KeyEvent(InputField.this.railChar));
                     InputField.this.startRail = System.currentTimeMillis();
                  }
               }

            }
         }

         public void onPostRender() {
         }
      });
   }

   public InputField() {
      this("");
   }

   public InputField(int width) {
      this("");
   }

   public InputField.InputState getCurrentState() {
      return this.currentState;
   }

   public void type(String text) {
      try {
         this.setText(this.getText().substring(0, this.currentState.getCursorRow()) + text + this.getText().substring(this.currentState.getCursorRow()));
         InputField.InputState var10000 = this.currentState;
         var10000.cursorRow += text.length();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      this.scroll();
   }

   public void remove(int back) {
      back = Math.min(back, this.currentState.getCursorRow());
      boolean a = this.setText(this.getText().substring(0, Math.max(this.currentState.getCursorRow() - back, 0)) + this.getText().substring(this.currentState.getCursorRow()));
      if (!a) {
         InputField.InputState var10000 = this.currentState;
         var10000.cursorRow -= back;
      }

      this.scroll();
   }

   private void scroll() {
      int aX = 0;
      int i = 0;
      String a = "";
      char[] var4 = this.getText().toCharArray();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         char c = var4[var6];
         aX += this.getFontRenderer().getStringWidth(c + "");
         ++i;
         a = a + c;
         if (i >= this.currentState.cursorRow) {
            break;
         }
      }

      int diff = aX - this.scrollX;
      if (diff > this.getWidth()) {
         this.scrollX = aX - this.getWidth() + 8;
      } else if (diff < 0) {
         this.scrollX = aX + 8;
      }

      if (this.currentState.cursorRow == 0) {
         this.scrollX = 0;
      }

   }

   public int getCursorRow() {
      return this.currentState.getCursorRow();
   }

   private void pushUndo() {
      ++this.undoT;
      if (this.undoT > 3) {
         this.undoT = 0;
         this.undoMap.add(0, this.currentState.clone());
      }

   }

   public String getText() {
      return this.currentState.getText();
   }

   public String getDisplayText() {
      return this.isEchoCharSet() ? this.getText().replaceAll(".", this.getEchoChar() + "") : this.getText();
   }

   public boolean setText(String text) {
      this.currentState.text = text;
      this.callPoof(InputField.InputFieldTextPoof.class, (PoofInfo)null);
      if (this.currentState.cursorRow > this.currentState.text.length()) {
         this.currentState.cursorRow = this.currentState.text.length();
         this.scroll();
         return true;
      } else {
         return false;
      }
   }

   public char getEchoChar() {
      return this.echoChar;
   }

   public InputField setEchoChar(char echoChar) {
      this.echoChar = echoChar;
      return this;
   }

   public boolean isEchoCharSet() {
      return this.echoChar != 0;
   }

   public abstract static class InputFieldTextPoof<T extends InputField, S extends PoofInfo> extends Poof<T, S> {
   }

   public class InputState {
      String text;
      int cursorRow;
      boolean selection;
      int selectionStart;
      int selectionEnd;

      public InputState(String text, int cursorRow, boolean selection, int selectionStart, int selectionEnd) {
         this.text = text;
         this.cursorRow = cursorRow;
         this.selection = selection;
         this.selectionStart = selectionStart;
         this.selectionEnd = selectionEnd;
      }

      protected InputField.InputState clone() {
         return InputField.this.new InputState(this.getText(), this.getCursorRow(), this.isSelection(), this.getSelectionStart(), this.getSelectionEnd());
      }

      public String getText() {
         return this.text;
      }

      public void setText(String text) {
         this.text = text;
      }

      public int getCursorRow() {
         return this.cursorRow;
      }

      public void setCursorRow(int cursorRow) {
         this.cursorRow = cursorRow;
         InputField.this.scroll();
      }

      public boolean isSelection() {
         return this.selection;
      }

      public void setSelection(boolean selection) {
         this.selection = selection;
      }

      public int getSelectionStart() {
         return this.selectionStart;
      }

      public void setSelectionStart(int selectionStart) {
         this.selectionStart = selectionStart;
      }

      public int getSelectionEnd() {
         return this.selectionEnd;
      }

      public void setSelectionEnd(int selectionEnd) {
         this.selectionEnd = selectionEnd;
      }
   }
}
