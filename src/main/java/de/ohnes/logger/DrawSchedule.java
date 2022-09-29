package de.ohnes.logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import de.ohnes.util.Instance;
import de.ohnes.util.Job;
import de.ohnes.util.Machine;

public class DrawSchedule {

    public static void drawSchedule(Instance I) {
        try {

            final int machine_width = 50;
            int width = I.getM() * machine_width, height = 300;
      
            // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
            // into integer pixels
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D ig = bi.createGraphics();
          
            // ig.translate(width / 2, height / 2);
          //   ig.setBackground(Color.WHITE);
          //   ig.drawLine(-1, -1, -1, -height);
          //   ig.drawLine(-1, -1, -width, -1);
          //   ig.drawRect(0, 0, -100, -100);
      
          //   ImageIO.write(bi, "PNG", new File("./yourImageName.PNG"));
          //   // ImageIO.write(bi, "JPEG", new File("c:\\yourImageName.JPG"));
          //   // ImageIO.write(bi, "gif", new File("c:\\yourImageName.GIF"));
          //   // ImageIO.write(bi, "BMP", new File("c:\\yourImageName.BMP"));

          // ig.drawLine(width - machine_width, 0, width - machine_width, height);;
          
          Font font = new Font("TimesRoman", Font.PLAIN, 10);
          ig.setFont(font);
          
          for(int i = 0;i < I.getMachines().length; i++) {
            int x = machine_width * i;
            Machine m = I.getMachines()[i];
            for(Job job : m.getJobs()) {
              int start = job.getStartingTime();
              int duration = job.getProcessingTime(job.getAllotedMachines());
              ig.setPaint(Color.black);
              ig.drawRect(x + 1, start, machine_width - 1, duration);
              // ig.setPaint(Color.WHITE);
              ig.drawString(String.format("%03d", job.getId()), x + 5, start + 10);

              // ig.drawLine(width - machine_width, start + duration, width, start + duration);
              ig.setPaint(Color.BLUE);
              ig.drawString(String.format("%03d", start + duration), x + machine_width - 25, start + duration - 3);

            }
          }
    
          ImageIO.write(bi, "PNG", new File("./yourImageName.PNG"));

          
        } catch (IOException ie) {
          ie.printStackTrace();
        }
    }
    
}
