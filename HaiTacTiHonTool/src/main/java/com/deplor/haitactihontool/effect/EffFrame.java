package com.deplor.haitactihontool.effect;

import java.util.ArrayList;
import java.util.List;

public class EffFrame {
   public final List<EffPartFrame> partsBottom;
   public final List<EffPartFrame> partsTop;
   public final List<EffPartFrame> allParts;

   public EffFrame(List<EffPartFrame> partsBottom, List<EffPartFrame> partsTop, List<EffPartFrame> allParts) {
      this.partsBottom = partsBottom;
      this.partsTop = partsTop;
      this.allParts = allParts;
   }

   public int getTotalParts() {
      return this.partsBottom.size() + this.partsTop.size();
   }

   public void rebuildLayers() {
      this.partsBottom.clear();
      this.partsTop.clear();

      for (EffPartFrame p : this.allParts) {
         if (p.onTop == 0) {
            this.partsBottom.add(p);
         } else {
            this.partsTop.add(p);
         }
      }
   }

   public static EffFrame createEmpty() {
      return new EffFrame(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }

   public EffFrame deepClone() {
      List<EffPartFrame> newAll = new ArrayList<>();
      List<EffPartFrame> newBottom = new ArrayList<>();
      List<EffPartFrame> newTop = new ArrayList<>();

      for (EffPartFrame p : this.allParts) {
         EffPartFrame cp = p.deepClone();
         newAll.add(cp);
         if (cp.onTop == 0) {
            newBottom.add(cp);
         } else {
            newTop.add(cp);
         }
      }

      return new EffFrame(newBottom, newTop, newAll);
   }
}
