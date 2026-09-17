package com.deplor.haitactihontool.leakres;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message {
   public byte command;
   private byte[] data;
   private DataInputStream dis;
   private ByteArrayOutputStream baos;
   private DataOutputStream dos;

   public Message(byte command) {
      this.command = command;
      this.baos = new ByteArrayOutputStream();
      this.dos = new DataOutputStream(this.baos);
   }

   public Message(byte command, byte[] data) {
      this.command = command;
      this.data = data;
      if (data != null) {
         this.dis = new DataInputStream(new ByteArrayInputStream(data));
      }
   }

   public DataOutputStream writer() {
      return this.dos;
   }

   public DataInputStream reader() {
      return this.dis;
   }

   public byte[] getData() {
      if (this.data != null) {
         return this.data;
      } else if (this.baos != null) {
         try {
            this.dos.flush();
            return this.baos.toByteArray();
         } catch (IOException var2) {
            return new byte[0];
         }
      } else {
         return null;
      }
   }

   public void cleanup() {
      try {
         if (this.dis != null) {
            this.dis.close();
         }

         if (this.dos != null) {
            this.dos.close();
         }

         if (this.baos != null) {
            this.baos.close();
         }
      } catch (IOException var2) {
      }
   }
}
