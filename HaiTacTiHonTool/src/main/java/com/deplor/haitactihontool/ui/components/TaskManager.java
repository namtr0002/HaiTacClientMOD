package com.deplor.haitactihontool.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

public final class TaskManager {
   private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() / 2), r -> {
      Thread t = new Thread(r, "ui-task");
      t.setDaemon(true);
      return t;
   });

   private TaskManager() {
   }

   public static void run(Component parent, String title, Runnable task) {
      run(parent, title, () -> {
         task.run();
         return null;
      }, null, null);
   }

   public static <T> void run(Component parent, String title, final Supplier<T> task, final Consumer<T> onSuccess, final Consumer<Throwable> onError) {
      final Window owner = parent == null ? KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() : SwingUtilities.getWindowAncestor(parent);
      final TaskManager.LoadingDialog dialog = new TaskManager.LoadingDialog(owner, title != null && !title.isBlank() ? title : "Processing...");
      SwingWorker<T, Void> worker = new SwingWorker<T, Void>() {
         @Override
         protected T doInBackground() {
            return task.get();
         }

         @Override
         protected void done() {
            dialog.close();

            try {
               T result = this.get();
               if (onSuccess != null) {
                  onSuccess.accept(result);
               }
            } catch (Throwable var3) {
               Throwable cause = var3.getCause() != null ? var3.getCause() : var3;
               if (onError != null) {
                  onError.accept(cause);
               } else {
                  TaskManager.showError(owner, cause);
               }
            }
         }
      };
      dialog.showAsync();
      EXECUTOR.execute(worker);
   }

   public static void bind(JButton button, Component parent, String title, Runnable task) {
      String originalText = button.getText();
      boolean originalEnabled = button.isEnabled();
      run(parent, title, () -> {
         SwingUtilities.invokeLater(() -> {
            button.setEnabled(false);
            button.setText("Working...");
         });

         try {
            task.run();
         } finally {
            SwingUtilities.invokeLater(() -> {
               button.setEnabled(originalEnabled);
               button.setText(originalText);
            });
         }
      });
   }

   public static void shutdown() {
      EXECUTOR.shutdownNow();
   }

   private static void showError(Window owner, Throwable ex) {
      Runnable r = () -> JOptionPane.showMessageDialog(owner, ex.getMessage() == null ? ex.toString() : ex.getMessage(), "Error", 0);
      if (SwingUtilities.isEventDispatchThread()) {
         r.run();
      } else {
         SwingUtilities.invokeLater(r);
      }
   }

   private static final class LoadingDialog extends JDialog {
      private final JProgressBar bar = new JProgressBar();

      LoadingDialog(Window owner, String title) {
         super(owner, ModalityType.APPLICATION_MODAL);
         this.setUndecorated(true);
         this.setDefaultCloseOperation(0);
         this.setSize(360, 120);
         this.setLocationRelativeTo(owner);
         this.setAlwaysOnTop(true);
         JPanel root = new JPanel(new BorderLayout(0, 10));
         root.setBackground(new Color(24, 24, 34));
         root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 1), new EmptyBorder(16, 16, 16, 16)));
         JLabel lbl = new JLabel(title);
         lbl.setForeground(Color.WHITE);
         lbl.setFont(new Font("Segoe UI", 1, 14));
         this.bar.setIndeterminate(true);
         this.bar.setBorderPainted(false);
         this.bar.setBackground(new Color(36, 36, 48));
         this.bar.setForeground(new Color(120, 90, 220));
         root.add(lbl, "North");
         root.add(this.bar, "Center");
         this.setContentPane(root);
      }

      void showAsync() {
         EventQueue.invokeLater(() -> this.setVisible(true));
      }

      void close() {
         if (this.isDisplayable()) {
            SwingUtilities.invokeLater(this::dispose);
         }
      }
   }
}
