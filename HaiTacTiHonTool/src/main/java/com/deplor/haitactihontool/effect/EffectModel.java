package com.deplor.haitactihontool.effect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EffectModel {
   public int id;
   public int minAbsDy = 10000;
   public SmallImageDef[] smallImages;
   public EffFrame[] frames;
   public int[] sequence;
   public int[][] frameChar = new int[4][0];
   public int[] indexSplash = new int[4];
   public BufferedImage atlasImage;

   public EffectModel(int id) {
      this.id = id;
   }

   public int getFrameCount() {
      return this.frames != null ? this.frames.length : 0;
   }

   public int getSeqLen() {
      return this.sequence != null ? this.sequence.length : 0;
   }

   public int resolveSeqFrame(int seqPos) {
      if (this.sequence != null && this.sequence.length != 0 && this.frames != null && this.frames.length != 0) {
         int pos = (seqPos % this.sequence.length + this.sequence.length) % this.sequence.length;
         int idx = this.sequence[pos] & 0xFF;
         if (idx >= this.frames.length) {
            idx = 0;
         }

         return idx;
      } else {
         return 0;
      }
   }

   public EffFrame getSeqFrame(int seqPos) {
      return this.frames != null && this.frames.length != 0 ? this.frames[this.resolveSeqFrame(seqPos)] : null;
   }

   public int addFrame(int insertIndex, EffFrame frame) {
      if (frame == null) {
         frame = EffFrame.createEmpty();
      }

      int oldLen = this.getFrameCount();
      int targetIdx = insertIndex >= 0 && insertIndex <= oldLen ? insertIndex : oldLen;
      EffFrame[] newArr = new EffFrame[oldLen + 1];
      if (oldLen > 0) {
         System.arraycopy(this.frames, 0, newArr, 0, targetIdx);
         if (oldLen > targetIdx) {
            System.arraycopy(this.frames, targetIdx, newArr, targetIdx + 1, oldLen - targetIdx);
         }
      }

      newArr[targetIdx] = frame;
      this.frames = newArr;
      if (targetIdx < oldLen && this.sequence != null) {
         for (int i = 0; i < this.sequence.length; i++) {
            if (this.sequence[i] >= targetIdx) {
               this.sequence[i]++;
            }
         }
      }

      return targetIdx;
   }

   public int duplicateFrame(int frameIndex) {
      if (this.frames != null && frameIndex >= 0 && frameIndex < this.frames.length) {
         EffFrame cloned = this.frames[frameIndex].deepClone();
         return this.addFrame(frameIndex + 1, cloned);
      } else {
         return this.addFrame(-1, EffFrame.createEmpty());
      }
   }

   public boolean deleteFrame(int frameIndex) {
      if (this.frames != null && frameIndex >= 0 && frameIndex < this.frames.length) {
         int oldLen = this.frames.length;
         if (oldLen <= 1) {
            this.frames[0] = EffFrame.createEmpty();
            if (this.sequence != null && this.sequence.length > 0) {
               this.sequence = new int[]{0};
            }

            return true;
         } else {
            EffFrame[] newArr = new EffFrame[oldLen - 1];
            int ni = 0;

            for (int i = 0; i < oldLen; i++) {
               if (i != frameIndex) {
                  newArr[ni++] = this.frames[i];
               }
            }

            this.frames = newArr;
            if (this.sequence != null) {
               List<Integer> newSeq = new ArrayList<>();

               for (int s : this.sequence) {
                  if (s == frameIndex) {
                     int remapped = Math.max(0, Math.min(frameIndex, this.frames.length - 1));
                     newSeq.add(remapped);
                  } else if (s > frameIndex) {
                     newSeq.add(s - 1);
                  } else {
                     newSeq.add(s);
                  }
               }

               if (newSeq.isEmpty()) {
                  newSeq.add(0);
               }

               this.sequence = new int[newSeq.size()];

               for (int ix = 0; ix < newSeq.size(); ix++) {
                  this.sequence[ix] = newSeq.get(ix);
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void reorderFrames(int fromIdx, int toIdx) {
      if (this.frames != null && fromIdx >= 0 && fromIdx < this.frames.length && toIdx >= 0 && toIdx < this.frames.length) {
         if (fromIdx != toIdx) {
            EffFrame moving = this.frames[fromIdx];
            List<EffFrame> list = new ArrayList<>();

            for (EffFrame f : this.frames) {
               list.add(f);
            }

            list.remove(fromIdx);
            list.add(toIdx, moving);

            for (int i = 0; i < this.frames.length; i++) {
               this.frames[i] = list.get(i);
            }

            if (this.sequence != null) {
               for (int i = 0; i < this.sequence.length; i++) {
                  int s = this.sequence[i];
                  if (s == fromIdx) {
                     this.sequence[i] = toIdx;
                  } else if (fromIdx < toIdx) {
                     if (s > fromIdx && s <= toIdx) {
                        this.sequence[i] = s - 1;
                     }
                  } else if (s >= toIdx && s < fromIdx) {
                     this.sequence[i] = s + 1;
                  }
               }
            }
         }
      }
   }

   public int addSeqStep(int insertPos, int frameIdx) {
      if (this.frames != null && this.frames.length != 0) {
         int targetFrame = Math.max(0, Math.min(frameIdx, this.frames.length - 1));
         int seqLen = this.getSeqLen();
         int targetPos = insertPos >= 0 && insertPos <= seqLen ? insertPos : seqLen;
         int[] newSeq = new int[seqLen + 1];
         if (seqLen > 0) {
            System.arraycopy(this.sequence, 0, newSeq, 0, targetPos);
            if (seqLen > targetPos) {
               System.arraycopy(this.sequence, targetPos, newSeq, targetPos + 1, seqLen - targetPos);
            }
         }

         newSeq[targetPos] = targetFrame;
         this.sequence = newSeq;
         return targetPos;
      } else {
         return 0;
      }
   }

   public boolean deleteSeqStep(int pos) {
      if (this.sequence != null && pos >= 0 && pos < this.sequence.length) {
         if (this.sequence.length <= 1) {
            this.sequence[0] = 0;
            return true;
         } else {
            int[] newSeq = new int[this.sequence.length - 1];
            int idx = 0;

            for (int i = 0; i < this.sequence.length; i++) {
               if (i != pos) {
                  newSeq[idx++] = this.sequence[i];
               }
            }

            this.sequence = newSeq;
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean setSeqStepTarget(int pos, int frameIdx) {
      if (this.sequence == null || pos < 0 || pos >= this.sequence.length) {
         return false;
      } else if (this.frames != null && frameIdx >= 0 && frameIdx < this.frames.length) {
         this.sequence[pos] = frameIdx;
         return true;
      } else {
         return false;
      }
   }

   public void reorderSeqStep(int fromPos, int toPos) {
      if (this.sequence != null && fromPos >= 0 && fromPos < this.sequence.length && toPos >= 0 && toPos < this.sequence.length) {
         if (fromPos != toPos) {
            List<Integer> list = new ArrayList<>();

            for (int v : this.sequence) {
               list.add(v);
            }

            int moving = list.remove(fromPos);
            list.add(toPos, moving);

            for (int i = 0; i < this.sequence.length; i++) {
               this.sequence[i] = list.get(i);
            }
         }
      }
   }

   public void sanitizeSequence() {
      if (this.frames != null && this.frames.length != 0) {
         if (this.sequence != null && this.sequence.length != 0) {
            for (int i = 0; i < this.sequence.length; i++) {
               if (this.sequence[i] < 0 || this.sequence[i] >= this.frames.length) {
                  this.sequence[i] = 0;
               }
            }
         } else {
            this.sequence = new int[this.frames.length];
            int ix = 0;

            while (ix < this.frames.length) {
               this.sequence[ix] = ix++;
            }
         }
      } else {
         this.sequence = new int[0];
      }
   }

   public void autoGenerateSequenceLinear() {
      if (this.frames != null && this.frames.length != 0) {
         this.sequence = new int[this.frames.length];
         int i = 0;

         while (i < this.frames.length) {
            this.sequence[i] = i++;
         }
      } else {
         this.sequence = new int[]{0};
      }
   }

   public void autoGenerateSequenceHold(int holdFrames) {
      if (this.frames != null && this.frames.length != 0) {
         int hold = Math.max(1, holdFrames);
         this.sequence = new int[this.frames.length * hold];
         int idx = 0;

         for (int i = 0; i < this.frames.length; i++) {
            for (int h = 0; h < hold; h++) {
               this.sequence[idx++] = i;
            }
         }
      } else {
         this.sequence = new int[]{0};
      }
   }

   public void autoGenerateSequencePingPong() {
      if (this.frames != null && this.frames.length != 0) {
         int n = this.frames.length;
         if (n <= 2) {
            this.autoGenerateSequenceLinear();
         } else {
            int len = n + (n - 2);
            this.sequence = new int[len];
            int idx = 0;
            int i = 0;

            while (i < n) {
               this.sequence[idx++] = i++;
            }

            i = n - 2;

            while (i >= 1) {
               this.sequence[idx++] = i--;
            }
         }
      } else {
         this.sequence = new int[]{0};
      }
   }

   public void autoGenerateSequenceReverse() {
      if (this.frames != null && this.frames.length != 0) {
         this.sequence = new int[this.frames.length];

         for (int i = 0; i < this.frames.length; i++) {
            this.sequence[i] = this.frames.length - 1 - i;
         }
      } else {
         this.sequence = new int[]{0};
      }
   }

   public void setCustomSequence(int[] newSeq) {
      if (newSeq != null && newSeq.length != 0) {
         this.sequence = newSeq;
         this.sanitizeSequence();
      } else {
         this.autoGenerateSequenceLinear();
      }
   }

   public String getSequenceString() {
      if (this.sequence != null && this.sequence.length != 0) {
         StringBuilder sb = new StringBuilder();

         for (int i = 0; i < this.sequence.length; i++) {
            if (i > 0) {
               sb.append(", ");
            }

            sb.append(this.sequence[i]);
         }

         return sb.toString();
      } else {
         return "0";
      }
   }

   public static int[] parseSequenceString(String str, int frameCount) {
      if (str != null && !str.trim().isEmpty() && frameCount > 0) {
         List<Integer> list = new ArrayList<>();
         String[] tokens = str.split("[,;\\s]+");

         for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty()) {
               if (token.contains("-") && !token.startsWith("-")) {
                  String[] parts = token.split("-");
                  if (parts.length == 2) {
                     try {
                        int start = Integer.parseInt(parts[0].trim());
                        int end = Integer.parseInt(parts[1].trim());
                        if (start <= end) {
                           for (int k = start; k <= end; k++) {
                              list.add(Math.max(0, Math.min(k, frameCount - 1)));
                           }
                           continue;
                        }

                        for (int k = start; k >= end; k--) {
                           list.add(Math.max(0, Math.min(k, frameCount - 1)));
                        }
                        continue;
                     } catch (Exception var14) {
                     }
                  }
               }

               if (token.contains("*") || token.toLowerCase().contains("x")) {
                  String[] parts = token.split("[*xX]");
                  if (parts.length == 2) {
                     try {
                        int val = Integer.parseInt(parts[0].trim());
                        int count = Integer.parseInt(parts[1].trim());
                        val = Math.max(0, Math.min(val, frameCount - 1));
                        count = Math.max(1, Math.min(count, 100));

                        for (int k = 0; k < count; k++) {
                           list.add(val);
                        }
                        continue;
                     } catch (Exception var13) {
                     }
                  }
               }

               try {
                  int val = Integer.parseInt(token);
                  list.add(Math.max(0, Math.min(val, frameCount - 1)));
               } catch (Exception var12) {
               }
            }
         }

         if (list.isEmpty()) {
            list.add(0);
         }

         int[] res = new int[list.size()];

         for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
         }

         return res;
      } else {
         return new int[]{0};
      }
   }
}
