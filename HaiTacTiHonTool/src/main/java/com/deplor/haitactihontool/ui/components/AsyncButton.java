package com.deplor.haitactihontool.ui.components;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class AsyncButton extends JButton {
   private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(r -> {
      Thread t = new Thread(r, "async-button");
      t.setDaemon(true);
      return t;
   });
   private Runnable task;
   private Consumer<Throwable> errorHandler;
   private String busyText = "Working...";
   private volatile boolean running;
   private String idleText;

   public AsyncButton(String text) {
      super(text);
      this.idleText = text;
      this.init();
   }

   public AsyncButton(String text, Runnable task) {
      this(text);
      this.task = task;
      this.addActionListener(e -> this.run());
   }

   private void init() {
      this.setFocusPainted(false);
      this.setCursor(Cursor.getPredefinedCursor(12));
      this.errorHandler = ex -> {
         Window w = SwingUtilities.getWindowAncestor(this);
         JOptionPane.showMessageDialog(w, ex.getMessage() == null ? ex.toString() : ex.getMessage(), "Error", 0);
      };
   }

   public void setTask(Runnable task) {
      this.task = task;
   }

   public void setBusyText(String busyText) {
      this.busyText = busyText != null && !busyText.isBlank() ? busyText : "Working...";
   }

   public void setErrorHandler(Consumer<Throwable> errorHandler) {
      this.errorHandler = errorHandler;
   }

   public boolean isRunning() {
      return this.running;
   }

   public void run() {
      Runnable job = this.task;
      if (job != null && !this.running) {
         this.running = true;
         this.idleText = this.getText();
         boolean wasEnabled = this.isEnabled();
         this.setEnabled(false);
         this.setText(this.busyText);
         EXECUTOR.execute(() -> {
            Throwable error = null;

            try {
               job.run();
            } catch (Throwable var5) {
               error = var5;
            }

            Throwable finalError = error;
            SwingUtilities.invokeLater(() -> {
               this.setText(this.idleText);
               this.setEnabled(wasEnabled);
               this.running = false;
               if (finalError != null) {
                  if (this.errorHandler != null) {
                     this.errorHandler.accept(finalError);
                  } else {
                     Toolkit.getDefaultToolkit().beep();
                  }
               }
            });
         });
      }
   }

   @Override
   public void doClick() {
      this.run();
   }
}
