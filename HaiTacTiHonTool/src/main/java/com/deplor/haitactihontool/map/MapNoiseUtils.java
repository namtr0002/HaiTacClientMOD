package com.deplor.haitactihontool.map;

import java.util.Random;

public class MapNoiseUtils {
   public static float[][] generateValueNoise(int width, int height, long seed, float scale) {
      float[][] noise = new float[height][width];
      Random rand = new Random(seed);
      int gridW = Math.max(2, (int)(width * scale));
      int gridH = Math.max(2, (int)(height * scale));
      float[][] grid = new float[gridH + 1][gridW + 1];

      for (int y = 0; y <= gridH; y++) {
         for (int x = 0; x <= gridW; x++) {
            grid[y][x] = rand.nextFloat();
         }
      }

      for (int y = 0; y < height; y++) {
         float gy = (float)y / height * gridH;
         int y0 = (int)gy;
         int y1 = Math.min(y0 + 1, gridH);
         float fy = gy - y0;
         fy = smoothStep(fy);

         for (int x = 0; x < width; x++) {
            float gx = (float)x / width * gridW;
            int x0 = (int)gx;
            int x1 = Math.min(x0 + 1, gridW);
            float fx = gx - x0;
            fx = smoothStep(fx);
            float top = lerp(grid[y0][x0], grid[y0][x1], fx);
            float bottom = lerp(grid[y1][x0], grid[y1][x1], fx);
            noise[y][x] = lerp(top, bottom, fy);
         }
      }

      return noise;
   }

   public static float[][] generateIslandMask(int width, int height) {
      float[][] mask = new float[height][width];
      float cx = width / 2.0F;
      float cy = height / 2.0F;

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            float dx = (x - cx) / cx;
            float dy = (y - cy) / cy;
            float dist = (float)Math.sqrt(dx * dx + dy * dy);
            float val = 1.0F - dist;
            mask[y][x] = Math.max(0.0F, Math.min(1.0F, val));
         }
      }

      return mask;
   }

   public static float[][] smoothPass(float[][] input) {
      int height = input.length;
      int width = input[0].length;
      float[][] output = new float[height][width];

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            float sum = 0.0F;
            int count = 0;

            for (int dy = -1; dy <= 1; dy++) {
               for (int dx = -1; dx <= 1; dx++) {
                  int nx = x + dx;
                  int ny = y + dy;
                  if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                     sum += input[ny][nx];
                     count++;
                  }
               }
            }

            output[y][x] = sum / count;
         }
      }

      return output;
   }

   public static float[] generate1DHeightmap(int length, long seed, float frequency, int octaves) {
      float[] result = new float[length];
      Random rand = new Random(seed);
      float totalAmp = 0.0F;
      float amp = 1.0F;
      float freq = frequency;

      for (int oct = 0; oct < octaves; oct++) {
         int numKnots = Math.max(2, (int)(length * freq) + 2);
         float[] knots = new float[numKnots];

         for (int k = 0; k < numKnots; k++) {
            knots[k] = rand.nextFloat();
         }

         for (int x = 0; x < length; x++) {
            float gx = (float)x / length * (numKnots - 1);
            int x0 = (int)gx;
            int x1 = Math.min(x0 + 1, numKnots - 1);
            float fx = smoothStep(gx - x0);
            float val = lerp(knots[x0], knots[x1], fx);
            result[x] += val * amp;
         }

         totalAmp += amp;
         amp *= 0.5F;
         freq *= 2.0F;
      }

      if (totalAmp > 0.0F) {
         for (int x = 0; x < length; x++) {
            result[x] /= totalAmp;
         }
      }

      return result;
   }

   private static float lerp(float a, float b, float t) {
      return a + t * (b - a);
   }

   private static float smoothStep(float t) {
      return t * t * (3.0F - 2.0F * t);
   }
}
