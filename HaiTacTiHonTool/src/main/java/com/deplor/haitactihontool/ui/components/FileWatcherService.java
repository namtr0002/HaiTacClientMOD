package com.deplor.haitactihontool.ui.components;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

public final class FileWatcherService implements AutoCloseable, Runnable {
   private final WatchService watchService;
   private final Map<WatchKey, Path> keys = new ConcurrentHashMap<>();
   private final Consumer<Path> onChange;
   private final AtomicBoolean running = new AtomicBoolean(true);
   private final Thread thread;
   private final boolean dispatchOnEdt;

   public FileWatcherService(Path root, Consumer<Path> onChange) throws IOException {
      this(root, onChange, true);
   }

   public FileWatcherService(Path root, Consumer<Path> onChange, boolean dispatchOnEdt) throws IOException {
      this.watchService = FileSystems.getDefault().newWatchService();
      this.onChange = onChange == null ? p -> {} : onChange;
      this.dispatchOnEdt = dispatchOnEdt;
      this.registerAll(root);
      this.thread = new Thread(this, "file-watcher");
      this.thread.setDaemon(true);
      this.thread.start();
   }

   public void watch(Path root) throws IOException {
      this.registerAll(root);
   }

   @Override
   public void run() {
      while (this.running.get()) {
         WatchKey key;
         try {
            key = this.watchService.take();
         } catch (ClosedWatchServiceException | InterruptedException var10) {
            break;
         }

         Path dir = this.keys.get(key);
         if (dir == null) {
            key.reset();
         } else {
            for (WatchEvent<?> event : key.pollEvents()) {
               Kind<?> kind = event.kind();
               if (kind != StandardWatchEventKinds.OVERFLOW) {
                  Path name = (Path)event.context();
                  Path child = dir.resolve(name);
                  this.fire(child);
                  if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                     try {
                        if (Files.isDirectory(child)) {
                           this.registerAll(child);
                        }
                     } catch (IOException var9) {
                     }
                  }
               }
            }

            boolean valid = key.reset();
            if (!valid) {
               this.keys.remove(key);
            }
         }
      }
   }

   private void fire(Path path) {
      if (this.dispatchOnEdt) {
         SwingUtilities.invokeLater(() -> this.onChange.accept(path));
      } else {
         this.onChange.accept(path);
      }
   }

   private void registerAll(Path start) throws IOException {
      if (start != null && Files.exists(start)) {
         Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
               FileWatcherService.this.register(dir);
               return FileVisitResult.CONTINUE;
            }
         });
      }
   }

   private void register(Path dir) throws IOException {
      WatchKey key = dir.register(
         this.watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY
      );
      this.keys.put(key, dir);
   }

   @Override
   public void close() {
      if (this.running.compareAndSet(true, false)) {
         try {
            this.watchService.close();
         } catch (IOException var2) {
         }

         this.thread.interrupt();
         this.keys.clear();
      }
   }
}
